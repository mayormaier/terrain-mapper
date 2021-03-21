package edu.psu.ist242.lab5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Terrain {

    private final int ROWS = 10;
    private final int COLS = 10;
    public TerrainType[][] grid = new TerrainType[ROWS][COLS];

    public Set<Island> islandFoundSet = new LinkedHashSet<>();

    public enum TerrainType {
        LAND, LAVA
    }

    /**
     * Builds a terrain map based on a particular file. <strong>NOTE: Map contained within the file must be 10x10 unless otherwise
     * specified.</strong>
     * @param fileName the name of the {@link File} that contains the Terrain map
     * @throws FileNotFoundException if file does not exist
     */
    public Terrain(String fileName) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(fileName));
        int row = 0;
        while (scan.hasNextLine()) {
            String curLine;
            curLine = scan.nextLine();
            for (int i = 0; i < curLine.length(); i++){
                if (curLine.charAt(i) == '.'){
                    grid[row][i] = TerrainType.LAVA;
                }
                if (curLine.charAt(i) == 'X'){
                    grid[row][i] = TerrainType.LAND;
                }
            }
            row++;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        //implement me: iterate over the grid... appending to result as you go
        for (int j = 0; j < ROWS; j++){
            for (int k = 0; k < COLS; k++){
                if (grid[j][k].equals(TerrainType.LAND)){result.append("X");}
                if (grid[j][k].equals(TerrainType.LAVA)){result.append(".");}
                result.append("  ");
            }
            result.append("\n");
        }

        return result.toString();
    }

    /**
     * Creates an {@link Island} object for a particular position on the {@link Terrain}
     * @param pos the {@link TerrainPos} that an {@link Island} is being calculated for
     * @return an {@link Island} object that contains a {@link TerrainPos} list of all adjacent land {@link TerrainType}s
     */
    private Island findIslandFor(TerrainPos pos){
        if (pos.value() == TerrainType.LAND){
            Set<TerrainPos> posSet = new LinkedHashSet<>();
            findIslandFor(pos, posSet);
            Island i = new Island(posSet);
            if (!islandFoundSet.contains(i)){
                islandFoundSet.add(i);
                return i;
            } else {return null;}
        } else {return null;}
    }

    /**
     * Adds coordinates to a list of {@link TerrainPos} for a particular position.
     * @param pos the {@link TerrainPos} of a particular position within a {@link Terrain}
     * @param accumulator a list of calculated {@link TerrainPos} within the current {@link Island}
     */
    private void findIslandFor(TerrainPos pos, Set<TerrainPos> accumulator){

        if (pos.value() == TerrainType.LAND & !accumulator.contains(pos)){
            accumulator.add(pos);
            for (TerrainPos p : pos.adjacencies()){
                findIslandFor(p, accumulator);
            }
        }
    }

    /**
     * Generates a report of all existing {@link Island} in a {@link Terrain}
     * @return String listing all coordinates in groups of land clusters (an {@link Island}) contained within an
     * {@link Terrain}
     */
    public String generateIslandReport(){
        StringBuilder b = new StringBuilder();
        int count = 1;
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLS; j++){
                TerrainPos p = new TerrainPos(i, j);

                if (p.value() == TerrainType.LAND){
                    Island isl = null;
                    isl = findIslandFor(p);
                    if (isl != null){
                        b.append("group ").append(count).append(": ");
                        b.append(isl.toString());
                        b.append("\n");
                        count++;
                    }
                }
            }
        }
        return b.toString();
    }

    private class Island {

        /**
         * This set holds all {@link TerrainPos} instances that comprise this
         * island. Note that we use a {@link LinkedHashSet} specifically to make
         * output of these positions deterministic.
         */
        private final Set<TerrainPos> positions = new LinkedHashSet<>();

        public Island(Set<TerrainPos> pos) {
            this.positions.addAll(pos);
        }

        @Override
        public int hashCode() {
            return positions.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            boolean result = o instanceof Island;
            if (result) {
                result = positions.containsAll(((Island) o).positions) &&
                        ((Island) o).positions.size() == positions.size();
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (TerrainPos pos : positions) {
                result.append(pos).append(" ");
            }
            return result.toString();
        }
    }

    /*
     * represents a position on the terrain grid
     */
    private class TerrainPos {
        int row;
        int col;

        private TerrainPos(int row, int col){
            this.row = row;
            this.col = col;
        }

        /**
         * Finds all adjacent positions to the current {@link TerrainPos} object
         * @return a {@link Set} of all adjacent positions
         */
        public List<TerrainPos> adjacencies() {
            List<TerrainPos> adjacent = new ArrayList<>();

            //northern point
            if (row > 0) {
                adjacent.add(new TerrainPos(row - 1, col));
            }
            //southern point
            if (row < ROWS - 1){
                adjacent.add(new TerrainPos(row + 1, col));
            }
            //western point
            if (col > 0){
                adjacent.add(new TerrainPos(row, col - 1));
            }
            //eastern point
            if (col < COLS - 1){
                adjacent.add(new TerrainPos(row, col + 1));
            }

            return adjacent;
        }

        /**
         * Returns the value of the {@link TerrainPos} object
         * @return value of current position
         */
        public TerrainType value() {
            int x=0;
            if (grid[row][col] == TerrainType.LAND) {
                return TerrainType.LAND;
            }
            if (grid[row][col] == TerrainType.LAVA) {
                return TerrainType.LAVA;
            } else return null;
        }

        @Override
        public String toString(){
            return "(" + row + "," + col + ")";
        }

        @Override
        public int hashCode(){
            return Integer.hashCode(row)*7 + Integer.hashCode(col)*17;
        }

        @Override
        public boolean equals(Object o){
            boolean result = o instanceof TerrainPos;
            if (result) {
                result = row == ((TerrainPos) o).row && col == ((TerrainPos) o).col;
            }
            return result;
        }
    }
}
