package it.niko.model;

import java.io.Serializable;

public class Configurazione implements Serializable {
    private final int numGiocatori;
    private final int numCaselle;
    private final int righe;
    private final int colonne;
    private final boolean dadoSingolo;
    private final boolean lancioSoloDado;
    private final boolean doppioSei;
    private final boolean caselleSosta;
    private final boolean casellePremio;
    private final boolean casellePesca;
    private final boolean carteDivieto;
    private final boolean avanzamentoAutomatico;
    private final Tabellone tabellone;
    private final Mazzo mazzo;

    private Configurazione(Builder builder) {
        numGiocatori = builder.numGiocatori;
        numCaselle = builder.numCaselle;
        righe = builder.righe;
        colonne = builder.colonne;
        dadoSingolo = builder.dadoSingolo;
        lancioSoloDado = builder.lancioSoloDado;
        doppioSei = builder.doppioSei;
        caselleSosta = builder.caselleSosta;
        casellePremio = builder.casellePremio;
        casellePesca = builder.casellePesca;
        carteDivieto = builder.carteDivieto;
        avanzamentoAutomatico = builder.avanzamentoAutomatico;
        tabellone = builder.tabellone;
        mazzo = builder.mazzo;
    }

    public int getNumGiocatori() { return numGiocatori; }
    public int getNumCaselle() { return numCaselle; }
    public int getRighe() { return righe; }
    public int getColonne() { return colonne; }
    public boolean isDadoSingolo() { return dadoSingolo; }
    public boolean isLancioSoloDado() { return lancioSoloDado; }
    public boolean isDoppioSei() { return doppioSei; }
    public boolean isCaselleSosta() { return caselleSosta; }
    public boolean isCasellePremio() { return casellePremio; }
    public boolean isCasellePesca() { return casellePesca; }
    public boolean isCarteDivieto() { return carteDivieto; }
    public boolean isAvanzamentoAutomatico() { return avanzamentoAutomatico; }
    public Tabellone getTabellone() { return tabellone; }
    public Mazzo getMazzo() { return mazzo; }

    public static class Builder {
        //required
        private final int numGiocatori;
        private final int numCaselle;
        private final int righe;
        private final int colonne;
        private final Tabellone tabellone;
        //optional
        private boolean dadoSingolo = false;
        private boolean lancioSoloDado = false;
        private boolean doppioSei = false;
        private boolean caselleSosta = false;
        private boolean casellePremio = false;
        private boolean casellePesca = false;
        private boolean carteDivieto = false;
        private boolean avanzamentoAutomatico = false;
        private Mazzo mazzo = null;

        public Builder(int numGiocatori, int numCaselle, int righe, int colonne) {
            this.numGiocatori = numGiocatori;
            this.numCaselle = numCaselle;
            this.righe = righe;
            this.colonne = colonne;
            tabellone = new TabelloneNaive(numCaselle, righe, colonne);
        }

        public Builder aggiungiSerpente(int testa, int coda) {
            if(!tabellone.aggiungiSerpente(testa, coda))
                throw new IllegalArgumentException();
            return this;
        }

        public Builder rimuoviSerpente(int testa, int coda) {
            if(!tabellone.rimuoviSerpente(testa, coda))
                throw new IllegalArgumentException();
            return this;
        }
        public Builder aggiungiScala(int base, int cima) {
            if(!tabellone.aggiungiScala(base, cima))
                throw new IllegalArgumentException();
            return this;
        }
        public Builder rimuoviScala(int base, int cima) {
            if(!tabellone.rimuoviScala(base, cima))
                throw new IllegalArgumentException();
            return this;
        }
        public Builder aggiungiCasellaSpeciale(int posizione, ECasella tipo) {
            if(!tabellone.aggiungiCasellaSpeciale(posizione, tipo))
                throw new IllegalArgumentException();
            return this;
        }
        public Builder rimuoviCasellaSpeciale(int posizione) {
            if(!tabellone.rimuoviCasellaSpeciale(posizione))
                throw new IllegalArgumentException();
            return this;
        }

        public Builder dadoSingolo(boolean dadoSingolo) {
            if(lancioSoloDado || doppioSei)
                throw new IllegalArgumentException();
            this.dadoSingolo = dadoSingolo;
            return this;
        }

        public Builder lancioSoloDado(boolean lancioSoloDado) {
            if(dadoSingolo) throw new IllegalStateException();
            this.lancioSoloDado = lancioSoloDado;
            return this;
        }

        public Builder doppioSei(boolean doppioSei) {
            if(dadoSingolo) throw new IllegalStateException();
            this.doppioSei = doppioSei;
            return this;
        }

        public Builder caselleSosta(boolean caselleSosta) {
            this.caselleSosta = caselleSosta;
            return this;
        }

        public Builder casellePremio(boolean casellePremio) {
            this.casellePremio = casellePremio;
            return this;
        }

        public Builder casellePesca(boolean casellePesca, int numCarte) {
            this.casellePesca = casellePesca;
            mazzo = new Mazzo(numCarte);
            return this;
        }

        public Builder aggiungiCarta(ECarta carta) {
            if(!casellePesca) throw new IllegalStateException();
            if(!mazzo.aggiungiCarta(carta)) throw new RuntimeException();
            return this;
        }

        public Builder carteDivieto(boolean carteDivieto) {
            if(!casellePesca) throw new IllegalStateException();
            this.carteDivieto = carteDivieto;
            return this;
        }

        public Builder avanzamentoAutomatico(boolean avanzamentoAutomatico) {
            this.avanzamentoAutomatico = avanzamentoAutomatico;
            return this;
        }

        public Configurazione build() { return new Configurazione(this); }
    }
}
