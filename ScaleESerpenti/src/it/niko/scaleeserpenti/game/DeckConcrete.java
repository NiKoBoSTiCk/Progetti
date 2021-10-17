package it.niko.scaleeserpenti.game;

import java.io.Serializable;
import java.util.*;

public class DeckConcrete implements Deck, Serializable {

    private final LinkedList<GameCards> cards;

    public DeckConcrete() {
        cards = new LinkedList<>();
    }

    @Override
    public void addCard(GameCards card) {
        cards.addLast(card);
    }

    @Override
    public GameCards drawCard() {
        if(cards.isEmpty())
            return null;
        GameCards card = cards.removeFirst();
        if(!card.equals(GameCards.Ban)) {
            cards.addLast(card);
        }
        return card;
    }

    @Override
    public int numCards() {
        return cards.size();
    }

    @Override
    public List<GameCards> getCards() {
        return List.copyOf(cards);
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }
}
