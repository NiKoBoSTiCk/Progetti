package it.niko.model;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Queue;

public class Mazzo {
    private final int numCarte;
    private final Queue<ECarta> carte;

    public Mazzo(int numCarte) {
        this.numCarte = numCarte;
        carte = new ArrayDeque<>(numCarte);
    }

    public boolean aggiungiCarta(ECarta carta) {
        if(carte.size() == numCarte)
            return false;
        return carte.offer(carta);
    }

    public ECarta pescaCarta() {
        if(carte.isEmpty()) throw new RuntimeException();
        ECarta carta = carte.poll();
        if(carta != null && !carta.equals(ECarta.divieto))
            carte.offer(carta);
        return carta;
    }

    public void svuota() {
        carte.clear();
    }

    public void mescola() {
        Collections.shuffle(Arrays.asList(carte.toArray()));
    }
}
