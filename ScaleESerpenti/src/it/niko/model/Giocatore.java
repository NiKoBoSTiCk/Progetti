package it.niko.model;

public class Giocatore {
    private final String nome;
    private int posizione;
    private int soste;
    private boolean divieto;

    public Giocatore(String nome) {
        this.nome = nome;
        this.posizione = 0;
        this.soste = 0;
    }

    public void daiSosta(ECasella tipo) {
        switch(tipo) {
            case panchina -> soste += 1;
            case locanda  -> soste += 3;
        }
    }

    public void setPosizione(int posizione) { this.posizione = posizione; }

    public int getPosizione() { return posizione; }

    public String getNome() { return nome; }

    public void usaSosta() { soste--; }

    public boolean haSoste() { return soste > 0; }

    public void daiDivieto() { divieto = true; }

    public void usaDivieto() { divieto = false; }

    public boolean haDivieto() { return divieto; }
}
