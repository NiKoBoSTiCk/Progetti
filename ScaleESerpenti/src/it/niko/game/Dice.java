package it.niko.game;

import java.util.Random;

public class Dice {
    private static Dice INSTANCE = null;
    private final int faces = 6;

    private Dice() {}

    public static synchronized Dice getInstance() {
        if(INSTANCE == null)
            return new Dice();
        return INSTANCE;
    }

    public int throwDice() {
        return new Random().nextInt(faces) + 1;
    }

    public int getFaces() {
        return faces;
    }
}
