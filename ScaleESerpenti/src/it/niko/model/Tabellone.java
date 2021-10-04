package it.niko.model;

public interface Tabellone {

    int getNumCaselle();

    Casella contenutoCasella(int posizione);

    int effettoCasella(int posizione);
}
