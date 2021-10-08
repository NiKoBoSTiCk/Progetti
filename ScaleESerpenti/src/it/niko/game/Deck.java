package it.niko.game;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Queue;

public class Deck implements Serializable {
    private final int numCards;
    private final Queue<GameCards> cards;

    public Deck(int numCards) {
        this.numCards = numCards;
        cards = new ArrayDeque<>(numCards);
    }

    public boolean addCard(GameCards card) {
        if(cards.size() == numCards)
            return false;
        return cards.offer(card);
    }

    public GameCards drawCard() {
        if(cards.isEmpty()) return null;
        GameCards carta = cards.poll();
        if(carta != null && !carta.equals(GameCards.Ban))
            cards.offer(carta);
        return carta;
    }

    public void shuffle() {
        Collections.shuffle(Arrays.asList(cards.toArray()));
    }
}
