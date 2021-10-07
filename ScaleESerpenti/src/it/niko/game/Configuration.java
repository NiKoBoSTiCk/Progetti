package it.niko.game;

import it.niko.game.model.Board;

import java.io.Serializable;

public class Configuration implements Serializable {
    private final int numPlayers;
    private final int numBoxes;
    private final int row;
    private final int column;
    private final boolean dadoSingolo;
    private final boolean lancioSoloDado;
    private final boolean doppioSei;
    private final boolean caselleSosta;
    private final boolean casellePremio;
    private final boolean casellePesca;
    private final boolean carteDivieto;
    private final Board board;
    private final Deck deck;

    private Configuration(ConfigurationBuilder builder) {
        numPlayers = builder.numPlayers;
        numBoxes = builder.numBoxes;
        row = builder.row;
        column = builder.column;
        dadoSingolo = builder.dadoSingolo;
        lancioSoloDado = builder.lancioSoloDado;
        doppioSei = builder.doppioSei;
        caselleSosta = builder.caselleSosta;
        casellePremio = builder.casellePremio;
        casellePesca = builder.casellePesca;
        carteDivieto = builder.carteDivieto;
        board = builder.board;
        deck = builder.deck;
    }

    public int getNumPlayers() { return numPlayers; }
    public int getNumCaselle() { return numBoxes; }
    public int getRow() { return row; }
    public int getColumn() { return column; }
    public boolean isDadoSingolo() { return dadoSingolo; }
    public boolean isLancioSoloDado() { return lancioSoloDado; }
    public boolean isDoppioSei() { return doppioSei; }
    public boolean isCaselleSosta() { return caselleSosta; }
    public boolean isCasellePremio() { return casellePremio; }
    public boolean isCasellePesca() { return casellePesca; }
    public boolean isCarteDivieto() { return carteDivieto; }
    public Board getBoard() { return board; }
    public Deck getDeck() { return deck; }

    public static class ConfigurationBuilder {
        //required
        private final int numPlayers;
        private final int numBoxes;
        private final int row;
        private final int column;
        private final Board board;
        //optional
        private boolean dadoSingolo = false;
        private boolean lancioSoloDado = false;
        private boolean doppioSei = false;
        private boolean caselleSosta = false;
        private boolean casellePremio = false;
        private boolean casellePesca = false;
        private boolean carteDivieto = false;
        private Deck deck = null;

        public ConfigurationBuilder(int numPlayers, int numBoxes, int row, int column) {
            this.numPlayers = numPlayers;
            this.numBoxes = numBoxes;
            this.row = row;
            this.column = column;
            board = new BoardConcrete(numBoxes, row, column);
        }

        public ConfigurationBuilder addSnake(int head, int tail) {
            if(!board.addSnake(head, tail))
                throw new IllegalArgumentException();
            return this;
        }

        public ConfigurationBuilder removeSnake(int head, int tail) {
            if(!board.removeSnake(head, tail))
                throw new IllegalArgumentException();
            return this;
        }

        public ConfigurationBuilder addLadder(int base, int top) {
            if(!board.addLadder(base, top))
                throw new IllegalArgumentException();
            return this;
        }

        public ConfigurationBuilder removeLadder(int base, int top) {
            if(!board.removeLadder(base, top))
                throw new IllegalArgumentException();
            return this;
        }

        public ConfigurationBuilder addSpecialBox(int pos, GameBoxes type) {
            if(!board.addSpecialBox(pos, type))
                throw new IllegalArgumentException();
            return this;
        }

        public ConfigurationBuilder removeSpecialBox(int pos) {
            if(!board.removeSpecialBox(pos))
                throw new IllegalArgumentException();
            return this;
        }

        public ConfigurationBuilder dadoSingolo(boolean dadoSingolo) {
            if(lancioSoloDado || doppioSei)
                throw new IllegalArgumentException();
            this.dadoSingolo = dadoSingolo;
            return this;
        }

        public ConfigurationBuilder lancioSoloDado(boolean lancioSoloDado) {
            if(dadoSingolo) throw new IllegalStateException();
            this.lancioSoloDado = lancioSoloDado;
            return this;
        }

        public ConfigurationBuilder doppioSei(boolean doppioSei) {
            if(dadoSingolo) throw new IllegalStateException();
            this.doppioSei = doppioSei;
            return this;
        }

        public ConfigurationBuilder caselleSosta(boolean caselleSosta) {
            this.caselleSosta = caselleSosta;
            return this;
        }

        public ConfigurationBuilder casellePremio(boolean casellePremio) {
            this.casellePremio = casellePremio;
            return this;
        }

        public ConfigurationBuilder casellePesca(boolean casellePesca, int numCarte) {
            this.casellePesca = casellePesca;
            deck = new Deck(numCarte);
            return this;
        }

        public ConfigurationBuilder addCard(GameCards carta) {
            if(!casellePesca) throw new IllegalStateException();
            if(!deck.addCard(carta)) throw new RuntimeException();
            return this;
        }

        public ConfigurationBuilder carteDivieto(boolean carteDivieto) {
            if(!casellePesca) throw new IllegalStateException();
            this.carteDivieto = carteDivieto;
            return this;
        }

        public Configuration build() { return new Configuration(this); }
    }
}
