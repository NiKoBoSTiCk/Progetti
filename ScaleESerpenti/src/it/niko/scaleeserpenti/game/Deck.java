package it.niko.scaleeserpenti.game;

import java.util.List;
/*
interfaccia di un mazzo che permette di visualizzare,
pescare e mescolare le carte.
 */
public interface Deck {

    void addCard(GameCards card);

    GameCards drawCard();

    int numCards();

    List<GameCards> getCards();

    void shuffle();
}
