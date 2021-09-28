package it.niko.model;

public class Giocatore {
    private final String name;
    private int posizione;
    private int soste;
    private int divieti;
    private int ultimoLancio;


    public Giocatore(String name) {
        this.name = name;
        this.posizione = 0;
        this.soste = 0;
        this.divieti = 0;
    }

    public String getName() { return name; }

    public int getPosizione() { return posizione; }

    public int getSoste() { return soste; }

    public int getDivieti() { return divieti; }

    public int getUltimoLancio() { return ultimoLancio; }

    public void setPosizione(int posizione) { this.posizione = posizione; }

    public void aggiungiDivieto() { this.divieti++; }

    public void usaDivieto() { this.divieti--; }

    public void aggiungiSosta(int sosta) { this.soste += sosta; }

    public void scontaSosta() { this.soste--; }

    public void setUltimoLancio(int ultimoLancio) { this.ultimoLancio = ultimoLancio; }
}
