package it.niko.scaleeserpenti.builder;

import it.niko.scaleeserpenti.game.*;
import java.io.Serializable;

public class Configuration implements Serializable {
    private final int numPlayers, numBoxes, row, column;
    private final boolean singleDice;
    private final boolean rollSingleDice;
    private final boolean doubleSix;
    private final boolean stopBoxes;
    private final boolean rewardBoxes;
    private final boolean drawCardBoxes;
    private final boolean banCards;
    private final Board board;
    private final Deck deck;

    private Configuration(ConfigurationBuilder builder) {
        numPlayers = builder.numPlayers;
        numBoxes = builder.numBoxes;
        row = builder.row;
        column = builder.column;
        singleDice = builder.singleDice;
        rollSingleDice = builder.rollSingleDice;
        doubleSix = builder.doubleSix;
        stopBoxes = builder.stopBoxes;
        rewardBoxes = builder.rewardBoxes;
        drawCardBoxes = builder.drawCardBoxes;
        banCards = builder.banCards;
        board = builder.board;
        deck = builder.deck;
    }

    public int getNumPlayers() { return numPlayers; }
    public int getNumBoxes() { return numBoxes; }
    public int getRow() { return row; }
    public int getColumn() { return column; }
    public boolean isSingleDice() { return singleDice; }
    public boolean isRollSingleDice() { return rollSingleDice; }
    public boolean isDoubleSix() { return doubleSix; }
    public boolean isStopBoxes() { return stopBoxes; }
    public boolean isRewardBoxes() { return rewardBoxes; }
    public boolean isDrawCardBoxes() { return drawCardBoxes; }
    public boolean isBanCards() { return banCards; }
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
        private boolean singleDice = false;
        private boolean rollSingleDice = false;
        private boolean doubleSix = false;
        private boolean stopBoxes = false;
        private boolean rewardBoxes = false;
        private boolean drawCardBoxes = false;
        private boolean banCards = false;
        private Deck deck = null;

        public ConfigurationBuilder(int numPlayers, int numBoxes, int row, int column) {
            if(numPlayers < 1 || numPlayers > 20) throw new IllegalArgumentException();
            this.numPlayers = numPlayers;
            this.numBoxes = numBoxes;
            this.row = row;
            this.column = column;
            board = new BoardConcrete(numBoxes, row, column);
        }

        public ConfigurationBuilder addSnake(int head, int tail) {
            if(!board.addSnake(head, tail)) throw new IllegalArgumentException();
            return this;
        }

        public ConfigurationBuilder addLadder(int base, int top) {
            if(!board.addLadder(base, top)) throw new IllegalArgumentException();
            return this;
        }

        public ConfigurationBuilder addSpecialBox(int pos, GameBoxes type) {
            if(!board.addSpecialBox(pos, type)) throw new IllegalArgumentException();
            return this;
        }

        public ConfigurationBuilder singleDice(boolean singleDice) {
            if(rollSingleDice || doubleSix) throw new IllegalArgumentException();
            this.singleDice = singleDice;
            return this;
        }

        public ConfigurationBuilder rollSingleDice(boolean rollSingleDice) {
            if(singleDice && rollSingleDice) throw new IllegalStateException();
            this.rollSingleDice = rollSingleDice;
            return this;
        }

        public ConfigurationBuilder doubleSix(boolean doubleSix) {
            if(singleDice && doubleSix) throw new IllegalStateException();
            this.doubleSix = doubleSix;
            return this;
        }

        public ConfigurationBuilder stopBoxes(boolean stopBoxes) {
            this.stopBoxes = stopBoxes;
            return this;
        }

        public ConfigurationBuilder rewardBoxes(boolean rewardBoxes) {
            this.rewardBoxes = rewardBoxes;
            return this;
        }

        public ConfigurationBuilder drawCardBoxes(boolean drawCardBoxes) {
            this.drawCardBoxes = drawCardBoxes;
            deck = new DeckConcrete();
            return this;
        }

        public ConfigurationBuilder addCard(GameCards card) {
            if(!drawCardBoxes) throw new IllegalStateException();
            if(!banCards && card == GameCards.Ban) throw new IllegalStateException();
            deck.addCard(card);
            return this;
        }

        public ConfigurationBuilder banCards(boolean banCards) {
            if(!drawCardBoxes && banCards) throw new IllegalStateException();
            this.banCards = banCards;
            return this;
        }

        public Configuration build() { return new Configuration(this); }
    }
}

