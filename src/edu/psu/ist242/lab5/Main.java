package edu.psu.ist242.lab5;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) { // bad.. no file given, so kick out
            System.err.println("usage: [filename]");
            return;
        }
        try {
            Terrain t = new Terrain(args[0]);

            System.out.println(t.toString());

            System.out.println(t.generateIslandReport());


        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
