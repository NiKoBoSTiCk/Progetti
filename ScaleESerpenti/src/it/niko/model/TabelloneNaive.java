package it.niko.model;

import java.util.HashMap;
import java.util.Map;

public class TabelloneNaive implements Tabellone {
    private final int numCaselle;
    private final int righe;
    private final Map<Integer, CasellaSpeciale> caselleOccupate;
    private final Map<Integer, Integer> caselleRighe;
    private final Map<Integer, Integer> serpenti;
    private final Map<Integer, Integer> scale;

    public TabelloneNaive(int numCaselle, int righe, int colonne) {
        if(righe * colonne != numCaselle) throw new IllegalArgumentException();
        if(righe < 3 || colonne < 3) throw new IllegalArgumentException();
        this.numCaselle = numCaselle;
        this.righe = righe;

        this.caselleRighe = new HashMap<>();
        int x = 0;
        for(int i=righe-1; i>=0; i--)
            for(int j=0; j<colonne; j++)
                caselleRighe.put(x++, i);

        caselleOccupate = new HashMap<>();
        serpenti = new HashMap<>();
        scale = new HashMap<>();
    }

    @Override
    public int getNumCaselle() {
        return numCaselle;
    }

    @Override
    public CasellaSpeciale contenutoCasella(int posizione) {
        return caselleOccupate.get(posizione);
    }

    @Override
    public int effettoCasella(int posizione){
        return switch(caselleOccupate.get(posizione)) {
            case testa -> serpenti.get(posizione);
            case base  -> scale.get(posizione);
            default -> posizione;
        };
    }

    public boolean aggiungiSerpente(int testa, int coda) {
        if(testa < coda || testa < righe || testa == numCaselle)
            return false;
        if(caselleRighe.get(testa).equals(caselleRighe.get(coda)))
            return false;
        if(caselleOccupate.containsKey(testa) || caselleOccupate.containsKey(coda))
            return false;
        caselleOccupate.put(testa, CasellaSpeciale.testa);
        caselleOccupate.put(coda,  CasellaSpeciale.coda);
        serpenti.put(testa, coda);
        return true;
    }

    public boolean rimuoviSerpente(int testa, int coda) {
        if(!caselleOccupate.containsKey(testa) && !caselleOccupate.containsKey(coda))
            return false;
        caselleOccupate.remove(testa);
        caselleOccupate.remove(coda);
        serpenti.remove(testa);
        return true;
    }

    public boolean aggiungiScala(int base, int cima) {
        if(base > cima || cima < righe || base > numCaselle -righe)
            return false;
        if(caselleRighe.get(base).equals(caselleRighe.get(cima)))
            return false;
        if(caselleOccupate.containsKey(base) || caselleOccupate.containsKey(cima))
            return false;
        caselleOccupate.put(base, CasellaSpeciale.base);
        caselleOccupate.put(cima, CasellaSpeciale.cima);
        scale.put(base, cima);
        return true;
    }

    public boolean rimuoviScala(int base, int cima) {
        if(!caselleOccupate.containsKey(base) && !caselleOccupate.containsKey(cima))
            return false;
        caselleOccupate.remove(base);
        caselleOccupate.remove(cima);
        scale.remove(base);
        return true;
    }

    public boolean aggiungiCasellaSpeciale(int posizione, CasellaSpeciale tipo) {
        if(caselleOccupate.containsKey(posizione))
            return false;
        caselleOccupate.put(posizione, tipo);
        return true;
    }

    public boolean rimuoviCasellaSpeciale(int posizione) {
        if(!caselleOccupate.containsKey(posizione))
            return false;
        caselleOccupate.remove(posizione);
        return true;
    }
}
