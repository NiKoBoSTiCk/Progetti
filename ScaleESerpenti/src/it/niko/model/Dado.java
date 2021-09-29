package it.niko.model;

import java.util.Random;

public class Dado {
    private static Dado instance = null;
    private final Random r;

    private Dado() {
        r = new Random();
    }

    public static synchronized Dado getInstance() {
        if(instance == null)
            return new Dado();
        return instance;
    }

    public int lancia() {
        return r.nextInt(6) + 1;
    }
}
