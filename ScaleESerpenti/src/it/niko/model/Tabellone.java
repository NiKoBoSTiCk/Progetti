package it.niko.model;

import javax.swing.*;

public interface Tabellone {

    int getNumCaselle();

    int effettoCasella(int posizione);

    ECasella contenutoCasella(int posizione);

    boolean aggiungiSerpente(int testa, int coda);

    boolean rimuoviSerpente(int testa, int coda);

    boolean aggiungiScala(int base, int cima);

    boolean rimuoviScala(int base, int cima);

    boolean aggiungiCasellaSpeciale(int posizione, ECasella tipo);

    boolean rimuoviCasellaSpeciale(int posizione);

    JPanel draw();
}
