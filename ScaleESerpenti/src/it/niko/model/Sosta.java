package it.niko.model;

public class Sosta extends TabelloneDecorator {
    private final int posizione;
    private final String tipo;
    private final int PANCHINA = 1;
    private final int LOCANDA = 3;

    public Sosta(TabelloneIF tabellone, int posizione, String tipo) {
        super(tabellone);
        this.posizione = posizione;
        this.tipo = tipo;
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
        if(giocatore.getPosizione() == posizione) {
            switch (tipo) {
                case "panchina"  -> giocatore.aggiungiSosta(PANCHINA);
                case "locanda" -> giocatore.aggiungiSosta(LOCANDA);
            }
        }
    }
}
