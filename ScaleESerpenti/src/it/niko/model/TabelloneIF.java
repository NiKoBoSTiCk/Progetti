package it.niko.model;

public interface TabelloneIF {

    int turno(Giocatore giocatore);

    int lanciaDadi(Giocatore giocatore);

    boolean eTerminata();
}
