package it.niko.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Queue;

public class ScaleESerpenti {
    private int numCaselle;
    private boolean dadoSingolo;
    private boolean lancioSoloDado;
    private boolean doppioSei;
    private boolean caselleSosta;
    private boolean casellePremio;
    private boolean casellePesca;
    private boolean carteDivieto;
    private boolean partitaTerminata;
    private Tabellone tabellone;
    private Dado dado;
    private Mazzo mazzo;
    private Queue<Giocatore> giocatori;
    private Configurazione configurazione;
    private boolean config;

    public ScaleESerpenti() {
        config = false;
    }

    public ScaleESerpenti(Configurazione c) {
        configurazione = c;
        configuraGioco(c);
    }

    public void configuraGioco(Configurazione c) {
        configurazione = c;
        numCaselle = c.getNumCaselle();
        dadoSingolo = c.isDadoSingolo();
        lancioSoloDado = c.isLancioSoloDado();
        doppioSei = c.isDoppioSei();
        caselleSosta = c.isCaselleSosta();
        casellePremio = c.isCasellePremio();
        casellePesca = c.isCasellePesca();
        carteDivieto = c.isCarteDivieto();
        dado = Dado.getInstance();
        giocatori = new ArrayDeque<>(c.getNumGiocatori());
        tabellone = c.getTabellone();
        mazzo = c.getMazzo();
        if(mazzo != null) mazzo.mescola();
        partitaTerminata = false;
        config = true;
        for(int i=1; i<=c.getNumGiocatori(); i++) {
            giocatori.add(new Giocatore(String.valueOf(i)));
        }
    }

    public void avviaPartitaAutomatico() {
        if(!config) throw new IllegalStateException("Carica una configurazione");
        while(!partitaTerminata) {
            turno();
        }
    }

    public void avviaPartitaManuale() {
        if(!config) throw new IllegalStateException("Carica una configurazione");
        if(partitaTerminata) throw new IllegalStateException();
        turno();
    }

    public void salva(String nomeFile) {
        ObjectOutputStream oss;
        try{
            oss = new ObjectOutputStream(new FileOutputStream(nomeFile));
            oss.writeObject(configurazione);
            oss.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void carica(String nomeFile) {
        ObjectInputStream ois;
        try{
            ois = new ObjectInputStream(new FileInputStream(nomeFile));
            configurazione = (Configurazione) ois.readObject();
            configuraGioco(configurazione);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void turno() {
        Giocatore g = giocatori.peek();
        if(g == null) throw new IllegalStateException();
        if(controllaSoste(g)) {
            giocatori.poll();
            giocatori.offer(g);
            return;
        }
        int lancio = lanciaDadi(g);
        int nuovaPos = calcolaPosizione(g, lancio);
        g.setPosizione(nuovaPos);
        if(nuovaPos == numCaselle) {
            partitaTerminata = true;
            System.out.println(g.getNome() + "ha vinto");
        }
        if(!doppioSei) {
            giocatori.poll();
            giocatori.offer(g);
        }
        else if(lancio != 12) {
            giocatori.poll();
            giocatori.offer(g);
        }
    }

    private int calcolaPosizione(Giocatore g, int lancio) {
        int nuovaPos = g.getPosizione() + lancio;
        if(nuovaPos > numCaselle) {
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
        return nuovaPos;
    }

    private int controllaCasella(Giocatore g, int nuovaPos, int lancio) {
        Casella casella = tabellone.contenutoCasella(nuovaPos);
        if(casella == null) return nuovaPos;
        else switch(casella) {
            case locanda -> { if(caselleSosta) g.daiSosta(Casella.locanda); }
            case panchina -> { if(caselleSosta) g.daiSosta(Casella.panchina); }
            case dadi -> { if(casellePremio) return nuovaPos + dado.lancia(); }
            case molla -> { if(casellePremio) return nuovaPos + lancio; }
            case pesca -> { if(casellePesca) return pescaCarta(g, nuovaPos, lancio); }
            case base, testa -> { return tabellone.effettoCasella(nuovaPos); }
            default -> { return nuovaPos; }
        }
        return nuovaPos;
    }

    private int pescaCarta(Giocatore g, int nuovaPos, int lancio) {
        switch(mazzo.pescaCarta()) {
            case panchina -> g.daiSosta(Casella.panchina);
            case locanda -> g.daiSosta(Casella.locanda);
            case divieto -> { if(carteDivieto) g.daiDivieto(); }
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
        if(lancioSoloDado && g.getPosizione() >= numCaselle - dado.getFacce())
            return dado.lancia();
        else return dado.lancia() + dado.lancia();
    }
}
