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
    private boolean avanzamentoAutomatico;
    private boolean partitaTerminata;
    private boolean config;
    private Tabellone tabellone;
    private Dado dado;
    private Mazzo mazzo;
    private Queue<Giocatore> giocatori;
    private Configurazione configurazione;
    private StringBuilder logTurno;

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
        avanzamentoAutomatico = c.isAvanzamentoAutomatico();
        dado = Dado.getInstance();
        giocatori = new ArrayDeque<>(c.getNumGiocatori());
        tabellone = c.getTabellone();
        mazzo = c.getMazzo();
        if(mazzo != null) mazzo.mescola();
        for(int i=1; i<=c.getNumGiocatori(); i++) {
            giocatori.add(new Giocatore("Giocatore " + i));
        }
        partitaTerminata = false;
        config = true;
    }

    public String avviaPartita() {
        if(!config) throw new IllegalStateException("Carica una configurazione");
        if(partitaTerminata) throw new IllegalStateException();
        if(avanzamentoAutomatico) {
            StringBuilder logPartita = new StringBuilder();
            while(!partitaTerminata) {
                logPartita.append(turno());
            }
            return logPartita.toString();
        }
        return turno();
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

    private String turno() {
        logTurno = new StringBuilder();
        Giocatore g = giocatori.peek();
        if(g == null) throw new IllegalStateException();
        logTurno.append("\n");
        logTurno.append(String.format("Turno %s\n", g.getNome()));
        if(controllaSoste(g)) {
            giocatori.poll();
            giocatori.offer(g);
            return logTurno.toString();
        }

        int lancio = lanciaDadi(g);

        int nuovaPos = calcolaPosizione(g, lancio);
        g.setPosizione(nuovaPos);
        if(nuovaPos == numCaselle) {
            logTurno.append(String.format("Fine Partita. Vince %s.\n", g.getNome()));
            partitaTerminata = true;
            return logTurno.toString();
        }
        if(doppioSei && lancio == 12) {
            logTurno.append(String.format("%s ha realizzato un doppio sei, ripete il turno.\n", g.getNome()));
            return logTurno.toString();
        }
        else {
            logTurno.append(String.format("Fine Turno %s.\n", g.getNome()));
            giocatori.poll();
            giocatori.offer(g);
        }
        return logTurno.toString();
    }

    private int calcolaPosizione(Giocatore g, int lancio) {
        int nuovaPos = g.getPosizione() + lancio;
        logTurno.append(String.format("%s si muove da %d a %d.\n", g.getNome(), g.getPosizione(), nuovaPos));
        nuovaPos = verificaSuperamentoCaselle(g, nuovaPos);
        int vecchiaPos = nuovaPos;
        while(tabellone.contenutoCasella(nuovaPos) != null) {
            nuovaPos = controllaCasella(g, nuovaPos, lancio);
            if(nuovaPos == vecchiaPos) break;
            logTurno.append(String.format("%s si muove da %d a %d.\n", g.getNome(), vecchiaPos, nuovaPos));
            nuovaPos = verificaSuperamentoCaselle(g, nuovaPos);
            vecchiaPos = nuovaPos;
        }
        return nuovaPos;
    }

    private int verificaSuperamentoCaselle(Giocatore g, int nuovaPos) {
        if(nuovaPos > numCaselle){
            nuovaPos = numCaselle - (nuovaPos - numCaselle);
            logTurno.append(String.format("%s ha superato il limite del tabellone, tornando alla posizione %d\n", g.getNome(), nuovaPos));
        }
        return nuovaPos;
    }

    private int controllaCasella(Giocatore g, int nuovaPos, int lancio) {
        ECasella casella = tabellone.contenutoCasella(nuovaPos);
        if(casella == null || casella == ECasella.cima || casella == ECasella.coda) return nuovaPos;
        logTurno.append(String.format("%s è su una casella %s.\n", g.getNome(), casella));
        switch(casella) {
            case locanda, panchina -> { if(caselleSosta) g.daiSosta(casella); }
            case dadi -> { if(casellePremio) return nuovaPos + lanciaDadi(g); }
            case molla -> { if(casellePremio) return nuovaPos + lancio; }
            case pesca -> { if(casellePesca) return pescaCarta(g, nuovaPos, lancio); }
            case base, testa -> { return tabellone.effettoCasella(nuovaPos); }
            default -> { return nuovaPos; }
        }
        return nuovaPos;
    }

    private int pescaCarta(Giocatore g, int nuovaPos, int lancio) {
        ECarta carta = mazzo.pescaCarta();
        logTurno.append(String.format("%s pesca la carta %s.\n", g.getNome(), carta));
        switch(carta) {
            case panchina -> g.daiSosta(ECasella.panchina);
            case locanda -> g.daiSosta(ECasella.locanda);
            case divieto -> { if(carteDivieto) g.daiDivieto(); }
            case dadi -> { return nuovaPos + lanciaDadi(g); }
            case molla -> { return nuovaPos + lancio; }
        }
        return nuovaPos;
    }

    private boolean controllaSoste(Giocatore g) {
        if(caselleSosta && g.haSoste()) {
            logTurno.append(String.format("%s è in sosta.\n", g.getNome()));
            if(carteDivieto && g.haDivieto()) {
                g.usaDivieto();
                logTurno.append(String.format("%s usa una carta divieto evitando la sosta.\n", g.getNome()));
                return false;
            }
            else {
                g.usaSosta();
                logTurno.append(String.format("%s sconta la sosta.\n", g.getNome()));
                return true;
            }
        }
        return false;
    }

    private int lanciaDadi(Giocatore g) {
        int lancio;
        if(dadoSingolo)
            lancio = dado.lancia();
        else if(lancioSoloDado && g.getPosizione() >= numCaselle - dado.getFacce())
            lancio = dado.lancia();
        else lancio = dado.lancia() + dado.lancia();
        logTurno.append(String.format("%s lancia i dadi e ottiene %d.\n", g.getNome(), lancio));
        return lancio;
    }
}
