package it.niko.model;

import java.io.Serializable;

public class Configurazione implements Serializable {
    private final int numGiocatori;
    private final int numCaselle;
    private final boolean dadoSingolo;
    private final boolean lancioSoloDado;
    private final boolean doppioSei;
    private final boolean caselleSosta;
    private final boolean casellePremio;
    private final boolean casellePesca;
    private final boolean carteDivieto;
    private final Tabellone tabellone;
    private final Mazzo mazzo;

    private Configurazione(Builder builder) {
        numGiocatori = builder.numGiocatori;
        numCaselle = builder.numCaselle;
        dadoSingolo = builder.dadoSingolo;
        lancioSoloDado = builder.lancioSoloDado;
        doppioSei = builder.doppioSei;
        caselleSosta = builder.caselleSosta;
        casellePremio = builder.casellePremio;
        casellePesca = builder.casellePesca;
        carteDivieto = builder.carteDivieto;
        tabellone = builder.tabellone;
        mazzo = builder.mazzo;
    }

    public int getNumGiocatori() { return numGiocatori; }
    public int getNumCaselle() { return numCaselle; }
    public boolean isDadoSingolo() { return dadoSingolo; }
    public boolean isLancioSoloDado() { return lancioSoloDado; }
    public boolean isDoppioSei() { return doppioSei; }
    public boolean isCaselleSosta() { return caselleSosta; }
    public boolean isCasellePremio() { return casellePremio; }
    public boolean isCasellePesca() { return casellePesca; }
    public boolean isCarteDivieto() { return carteDivieto; }
    public Tabellone getTabellone() { return tabellone; }
    public Mazzo getMazzo() { return mazzo; }

    public static class Builder {
        //required
        private final int numGiocatori;
        private final int numCaselle;
        private final Tabellone tabellone;
        //optional
        private boolean dadoSingolo = false;
        private boolean lancioSoloDado = false;
        private boolean doppioSei = false;
        private boolean caselleSosta = false;
        private boolean casellePremio = false;
        private boolean casellePesca = false;
        private boolean carteDivieto = false;
        private Mazzo mazzo;

        public Builder(int numGiocatori, Tabellone tabellone) {
            this.numGiocatori = numGiocatori;
            this.numCaselle = tabellone.getNumCaselle();
            this.tabellone = tabellone;
        }

        public Builder dadoSingolo(boolean dadoSingolo) {
            if(lancioSoloDado || doppioSei)
                throw new IllegalArgumentException();
            this.dadoSingolo = dadoSingolo;
            return this;
        }

        public Builder lancioSoloDado(boolean lancioSoloDado) {
            if(dadoSingolo) throw new IllegalArgumentException();
            this.lancioSoloDado = lancioSoloDado;
            return this;
        }

        public Builder doppioSei(boolean doppioSei) {
            if(dadoSingolo) throw new IllegalArgumentException();
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

        public Builder casellePesca(boolean casellePesca, Mazzo mazzo) {
            this.casellePesca = casellePesca;
            this.mazzo = mazzo;
            return this;
        }

        public Builder carteDivieto(boolean carteDivieto) {
            if(!casellePesca) throw new IllegalArgumentException();
            this.carteDivieto = carteDivieto;
            return this;
        }

        public Configurazione build() { return new Configurazione(this); }
    }
}
