package it.niko.model;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TabelloneNaive implements Tabellone, Serializable {
    private final int numCaselle;
    private final int righe;
    private final int colonne;
    private final Map<Integer, ECasella> caselleOccupate;
    private final Map<Integer, Integer> caselleRighe;
    private final Map<Integer, Integer> serpenti;
    private final Map<Integer, Integer> scale;

    public TabelloneNaive(int numCaselle, int righe, int colonne) {
        if(righe * colonne != numCaselle) throw new IllegalArgumentException();
        if(righe < 3 || colonne < 3) throw new IllegalArgumentException();
        this.numCaselle = numCaselle;
        this.righe = righe;
        this.colonne = colonne;

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
    public ECasella contenutoCasella(int posizione) {
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
        caselleOccupate.put(testa, ECasella.testa);
        caselleOccupate.put(coda,  ECasella.coda);
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
        caselleOccupate.put(base, ECasella.base);
        caselleOccupate.put(cima, ECasella.cima);
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

    public boolean aggiungiCasellaSpeciale(int posizione, ECasella tipo) {
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

    public JPanel getGraphics() {
        JPanel tabellone = new JPanel();
        tabellone.setLayout(new GridLayout(righe, colonne));
        JLabel[][] caselle = new JLabel[righe][colonne];
       // HashMap<Integer, Point> posizioniCaselle = new HashMap<>();
        boolean alternati = true;
        int x = 0;
        for(int i=righe-1; i>=0; i--) {
            if(alternati) {
                for(int j=0; j<colonne; j++) {
                    caselle[i][j] = new JLabel("" + ++x);
                    caselle[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                }
                alternati = false;
            }
            else {
                for(int j=colonne-1; j>=0; j--) {
                    caselle[i][j] = new JLabel("" + ++x);
                    caselle[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                }
                alternati = true;
            }
        }
        for(int i = 0; i<righe; i++) {
            for(int j=0; j<colonne; j++) {
                tabellone.add(caselle[i][j]);
             //   posizioniCaselle.put(Integer.valueOf(caselle[i][j].getText()), caselle[i][j].getLocation());
            }
        }
      //  drawSerpentiEScale(tabellone, posizioniCaselle, serpenti);
      //  drawSerpentiEScale(tabellone, posizioniCaselle, scale);
        return tabellone;
    }

    private void drawSerpentiEScale(JPanel tabellone, HashMap<Integer, Point> posizioniCaselle, Map<Integer, Integer> m) {
        for(Map.Entry<Integer, Integer> entry : m.entrySet()) {
            Point p1 = posizioniCaselle.get(entry.getKey());
            Point p2 = posizioniCaselle.get(entry.getValue());
            //TODO
        }
    }
}

