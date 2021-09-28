package it.niko.model;

public class Premio extends TabelloneDecorator {
    private final int inizio;
    private final String tipo;

    public Premio(TabelloneIF tabellone, int inizio, String tipo) {
        super(tabellone);
        this.inizio = inizio;
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
        if(giocatore.getPosizione() == inizio) {
            switch (tipo) {
                case "dadi"  -> super.lanciaDadi(giocatore);
                case "molla" -> giocatore.setPosizione(giocatore.getPosizione() + giocatore.getUltimoLancio());
            }
        }
    }
}
