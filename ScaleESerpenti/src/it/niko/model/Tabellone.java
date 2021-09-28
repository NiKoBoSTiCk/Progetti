package it.niko.model;

import java.io.Serializable;

public class Tabellone implements TabelloneIF, Serializable {
    private final int fine;
    private boolean partitaTerminata;
    private final int numeroDadi;


    public Tabellone(int fine, int numeroDadi) {
        this.fine = fine;
        this.numeroDadi = numeroDadi;
        this.partitaTerminata = false;
    }


    @Override
    public int turno(Giocatore giocatore) {
        if(partitaTerminata) throw new IllegalStateException("Partita già terminata!");

        int lancio = lanciaDadi(giocatore);

        if(giocatore.getPosizione() == fine) {
            partitaTerminata = true;
        }

        return lancio;
    }

    @Override
    public int lanciaDadi(Giocatore giocatore) {
        Dado dado = Dado.getInstance();
        int lancio = 0;
        for(int i=0; i<numeroDadi; i++)
            lancio += dado.lanciaDado();

        giocatore.setPosizione(giocatore.getPosizione() + lancio);
        giocatore.setUltimoLancio(lancio);
        return lancio;
    }

    @Override
    public boolean eTerminata() {
        return partitaTerminata;
    }
}
