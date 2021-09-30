package it.niko.model;

public interface Tabellone {

    int getNumCaselle();

    CasellaSpeciale contenutoCasella(int posizione);

    int effettoCasella(int posizione);
}
