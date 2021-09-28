package it.niko.model;

import java.util.Random;

public class Dado { // SINGLETON
    private static Dado INSTANCE = null;
    private final Random random = new Random();

    public Dado() {}

    public static synchronized Dado getInstance() {
        if(INSTANCE == null){
            INSTANCE = new Dado();
        }
        return INSTANCE;
    }

    public int lanciaDado(){
        return random.nextInt(6) + 1;
    }
}
