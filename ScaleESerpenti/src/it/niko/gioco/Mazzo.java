package it.niko.gioco;

public class Mazzo {
    private final String[] carte;
    private final int size;
    private int cur;

    public Mazzo(String[] carte){
        this.carte = carte;
        size = carte.length;
        cur = -1;
    }

    public String pescaCarta(){
        cur = (cur + 1) % size;
        return carte[cur];
    }
}
