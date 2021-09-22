package it.niko.gioco;

public class Mazzo {
    private int size;
    private int cur;
    private final String[] carte;

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
