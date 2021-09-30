package it.niko.model;

import java.util.ArrayDeque;
import java.util.Queue;

public class ScaleESerpenti {
    private final int numGiocatori;
    private final int numCaselle;
    private final boolean dadoSingolo;
    private final boolean lancioSoloDado;
    private final boolean doppioSei;
    private final boolean caselleSosta;
    private final boolean casellePremio;
    private final boolean casellePesca;
    private final boolean carteDivieto;
    private Tabellone tabellone;
    private final Dado dado;
    private final Mazzo mazzo;
    private final Queue<Giocatore> giocatori;

    private boolean partitaTerminata;

    public ScaleESerpenti(Configurazione c) {
        tabellone = c.getTabellone();
        mazzo = c.getMazzo();
        numGiocatori = c.getNumGiocatori();
        numCaselle = c.getNumCaselle();
        dadoSingolo = c.isDadoSingolo();
        lancioSoloDado = c.isLancioSoloDado();
        doppioSei = c.isDoppioSei();
        caselleSosta = c.isCaselleSosta();
        casellePremio = c.isCasellePremio();
        casellePesca = c.isCasellePesca();
        carteDivieto = c.isCarteDivieto();
        dado = Dado.getInstance();
        giocatori = new ArrayDeque<>(numGiocatori);
        partitaTerminata = false;

        if(mazzo != null) mazzo.mescola();

        for(int i=0; i<numGiocatori; i++) {
            giocatori.add(new Giocatore(String.valueOf(i)));
        }
    }

    public void avviaPartitaAutomatico() {
        while(!partitaTerminata) {
            Giocatore g = giocatori.peek();
            if(g == null) throw new IllegalStateException();

            if(controllaSoste(g)) break;

            int lancio = lanciaDadi(g);

            int nuovaPos = g.getPosizione() + lancio;
            if(nuovaPos > numCaselle){
                nuovaPos = numCaselle - (nuovaPos - numCaselle);
            }

            int vecchiaPos = nuovaPos;
            nuovaPos = controllaCasella(g, nuovaPos, lancio);
            while(tabellone.contenutoCasella(nuovaPos) != null) {
                if(nuovaPos == vecchiaPos) break;
                vecchiaPos = nuovaPos;
                nuovaPos = controllaCasella(g, nuovaPos, lancio);
                if(nuovaPos > numCaselle){
                    nuovaPos = numCaselle - (nuovaPos - numCaselle);
                }
            }

            if(nuovaPos > numCaselle){
                nuovaPos = numCaselle - (nuovaPos - numCaselle);
            }

            g.setPosizione(nuovaPos);
            if(nuovaPos == numCaselle) {
                partitaTerminata = true;
            }

            if(!doppioSei) {
                giocatori.poll();
                giocatori.offer(g);
            }
            else if(lancio != 12) {
                giocatori.poll();
                giocatori.offer(g);
            }
            //else ripete turno
        }
    }

    private int controllaCasella(Giocatore g, int nuovaPos, int lancio) {
        CasellaSpeciale casella = tabellone.contenutoCasella(nuovaPos);
        if(casella == null) return nuovaPos;
        else switch (casella) {
            case locanda -> g.daiSosta(CasellaSpeciale.locanda);
            case panchina -> g.daiSosta(CasellaSpeciale.panchina);
            case dadi -> { return nuovaPos + dado.lancia(); }
            case molla -> { return nuovaPos + lancio; }
            case pesca -> { return pescaCarta(g, nuovaPos, lancio); }
            case base, testa -> { return tabellone.effettoCasella(nuovaPos); }
        }
        return nuovaPos;
    }

    private int pescaCarta(Giocatore g, int nuovaPos, int lancio) {
        switch (mazzo.pescaCarta()) {
            case panchina -> g.daiSosta(CasellaSpeciale.panchina);
            case locanda -> g.daiSosta(CasellaSpeciale.locanda);
            case divieto -> g.daiDivieto();
            case dadi -> { return nuovaPos + dado.lancia(); }
            case molla -> { return nuovaPos + lancio; }
        }
        return nuovaPos;
    }

    private boolean controllaSoste(Giocatore g) {
        if(caselleSosta && g.haSoste()) {
            if(carteDivieto && g.haDivieto()) {
                g.usaDivieto();
                g.usaSosta();
                return false;
            }
            else {
                g.usaSosta();
                return true;
            }
        }
        return false;
    }

    private int lanciaDadi(Giocatore g) {
        if(dadoSingolo)
            return dado.lancia();
        if(g.getPosizione() >= numCaselle - dado.getFacce())
            return dado.lancia();
        else return dado.lancia() + dado.lancia();
    }
}
