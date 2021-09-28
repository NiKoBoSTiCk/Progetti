package it.niko.model;

public class Pesca extends TabelloneDecorator {
    private final int posizione;
    private final Mazzo mazzo;
    private final int PANCHINA = 1;
    private final int LOCANDA = 3;

    public Pesca(TabelloneIF tabellone, int posizione, Mazzo mazzo) {
        super(tabellone);
        this.posizione = posizione;
        this.mazzo = mazzo;
    }

    @Override
    public int lanciaDadi(Giocatore giocatore) {
        return super.lanciaDadi(giocatore);
    }

    @Override
    public boolean eTerminata() {
        return super.eTerminata();
    }

    @Override
    public int turno(Giocatore giocatore) {
        int lancio = super.turno(giocatore);
        comportamento(giocatore);
        return lancio;
    }

    private void comportamento(Giocatore giocatore) {
        if(giocatore.getPosizione() == posizione) {
            String carta = mazzo.pescaCarta();
            switch (carta) {
                case "molla" -> giocatore.setPosizione(giocatore.getPosizione() + giocatore.getUltimoLancio());
                case "dadi" -> lanciaDadi(giocatore);
                case "panchina" -> giocatore.aggiungiSosta(PANCHINA);
                case "locanda" -> giocatore.aggiungiSosta(LOCANDA);
                case "divieto" -> giocatore.aggiungiDivieto();
            }
        }
    }
}
