package it.niko.model;

public class Scala extends TabelloneDecorator {
    private final int inizio, fine;

    public Scala(TabelloneIF tabellone, int inizio, int fine) {
        super(tabellone);
        if(inizio >= fine) throw new IllegalStateException("Posizione Serpente non valida!");
        this.inizio = inizio;
        this.fine = fine;
    }

    @Override
    public int turno(Giocatore giocatore) {
        int lancio = super.turno(giocatore);
        comportamento(giocatore);
        return lancio;
    }

    @Override
    public int lanciaDadi(Giocatore giocatore) {
        return super.lanciaDadi(giocatore);
    }

    @Override
    public boolean eTerminata() {
        return super.eTerminata();
    }

    private void comportamento(Giocatore giocatore) {
        if(giocatore.getPosizione() == inizio)
            giocatore.setPosizione(fine);
    }
}
