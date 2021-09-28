package it.niko.model;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class Mazzo implements MazzoIF {
    private final Queue<String> carte;

    public Mazzo() {
        this.carte = new ArrayDeque<>();
    }

    @Override
    public String pescaCarta() {
        if(carte.isEmpty())
            throw new IllegalStateException("Inserisci delle carte nel mazzo prima di pescare!");
        String carta = carte.poll();
        if(carta.equals("divieto"))
            return carta;
        carte.offer(carta);
        return carta;
    }

    @Override
    public void aggiungiCarta(String carta) {
        carte.offer(carta);
    }

    @Override
    public void rimuoviCarta(String daRimuovere) {
        Iterator<String> it = carte.iterator();
        while(it.hasNext()){
            String carta = it.next();
            if(carta.equals(daRimuovere)) {
                it.remove();
                break;
            }
        }
    }
}
