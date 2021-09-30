package it.niko.model;

import java.util.HashMap;
import java.util.Map;

public class TabelloneBuilder implements Tabellone {
    private final int numCaselle;
    private final Map<Integer, CasellaSpeciale> caselleOccupate;
    private final Map<Integer, Integer> serpenti;
    private final Map<Integer, Integer> scale;

    private TabelloneBuilder(Builder builder) {
        numCaselle = builder.numCaselle;
        caselleOccupate = builder.caselleOccupate;
        serpenti = builder.serpenti;
        scale = builder.scale;
    }

    @Override
    public CasellaSpeciale contenutoCasella(int posizione) {
        return caselleOccupate.get(posizione);
    }

    @Override
    public int effettoCasella(int posizione) {
        return switch(caselleOccupate.get(posizione)) {
            case testa -> serpenti.get(posizione);
            case base  -> scale.get(posizione);
            default -> posizione;
        };
    }

    @Override
    public int getNumCaselle() {
        return numCaselle;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Integer, CasellaSpeciale> entry: caselleOccupate.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public static class Builder {
        private int numCaselle = 100;
        private int righe = 10;
        private int colonne = 10;

        private final Map<Integer, CasellaSpeciale> caselleOccupate = new HashMap<>();
        private final Map<Integer, Integer> caselleRighe = new HashMap<>();
        private final Map<Integer, Integer> serpenti = new HashMap<>();
        private final Map<Integer, Integer> scale = new HashMap<>();

        public Builder() {
            int x = 0;
            for(int i=righe-1; i>=0; i--)
                for(int j=0; j<colonne; j++)
                    caselleRighe.put(x++, i);
        }

        public Builder(int numCaselle, int righe, int colonne) {
            if(righe * colonne != numCaselle) throw new IllegalArgumentException();
            if(righe < 3 || colonne < 3) throw new IllegalArgumentException();
            this.numCaselle = numCaselle;
            this.righe = righe;
            this.colonne = colonne;

            int x = 0;
            for(int i=righe-1; i>=0; i--)
                for(int j=0; j<colonne; j++)
                    caselleRighe.put(x++, i);
        }

        public Builder serpente(int testa, int coda) {
            if(testa < coda || testa < righe || testa == numCaselle)
                throw new IllegalArgumentException();
            if(caselleRighe.get(testa).equals(caselleRighe.get(coda)))
                throw new IllegalArgumentException();
            if(caselleOccupate.containsKey(testa) || caselleOccupate.containsKey(coda))
                throw new IllegalArgumentException();
            caselleOccupate.put(testa, CasellaSpeciale.testa);
            caselleOccupate.put(coda,  CasellaSpeciale.coda);
            serpenti.put(testa, coda);
            return this;
        }

        public Builder scala(int base, int cima) {
            if(base > cima || cima < righe || base > numCaselle -righe)
                throw new IllegalArgumentException();
            if(caselleRighe.get(base).equals(caselleRighe.get(cima)))
                throw new IllegalArgumentException();
            if(caselleOccupate.containsKey(base) || caselleOccupate.containsKey(cima))
                throw new IllegalArgumentException();
            caselleOccupate.put(base, CasellaSpeciale.base);
            caselleOccupate.put(cima, CasellaSpeciale.cima);
            scale.put(base, cima);
            return this;
        }

        public Builder casellaSpeciale(int posizione, CasellaSpeciale tipo) {
            if(caselleOccupate.containsKey(posizione))
                throw new IllegalArgumentException();
            caselleOccupate.put(posizione, tipo);
            return this;
        }

        public TabelloneBuilder build() { return new TabelloneBuilder(this); }
    }

    public static void main(String[] args) {
        TabelloneBuilder tabellone = new TabelloneBuilder
                .Builder(100, 10, 10)
                .scala(3, 15)
                .serpente(22, 12)
                .casellaSpeciale(45, CasellaSpeciale.pesca)
                .casellaSpeciale(99, CasellaSpeciale.locanda)
                .build();
        System.out.println(tabellone);
    }
}
