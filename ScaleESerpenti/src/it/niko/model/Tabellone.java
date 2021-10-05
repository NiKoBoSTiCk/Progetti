package it.niko.model;

public interface Tabellone {

    int getNumCaselle();

    ECasella contenutoCasella(int posizione);

    int effettoCasella(int posizione);
}
