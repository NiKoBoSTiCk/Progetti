package it.niko.model;

public abstract class TabelloneDecorator implements TabelloneIF {
    protected TabelloneIF tabellone;

    public TabelloneDecorator(TabelloneIF tabellone) {
        this.tabellone = tabellone;
    }

    @Override
    public int turno(Giocatore giocatore) {
        return tabellone.turno(giocatore);
    }

    @Override
    public int lanciaDadi(Giocatore giocatore) {
        return tabellone.lanciaDadi(giocatore);
    }

    @Override
    public boolean eTerminata() {
        return tabellone.eTerminata();
    }
}
