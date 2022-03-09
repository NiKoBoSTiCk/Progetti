package it.niko.scaleeserpenti.game;

import java.util.List;
/*
  Interfaccia di un mazzo che permette di visualizzare,
  pescare e mescolare le carte.
 */
public interface Deck {

    int numCards();

    void addCard(GameCards card);

    void shuffle();

    GameCards drawCard();

    List<GameCards> getCards();
}
