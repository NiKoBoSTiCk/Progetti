package it.niko.scaleeserpenti.observer;

import it.niko.scaleeserpenti.game.Deck;
import it.niko.scaleeserpenti.game.GameCards;
import it.niko.scaleeserpenti.game.GameEvent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/*
   Rappresentazione del mazzo di gioco
 */
public class DeckGameListener extends JPanel implements GameListener {
    private final JPanel cardsPanel;

    public DeckGameListener() {
        cardsPanel = new JPanel();
        JPanel title = new JPanel();
        setLayout(new BorderLayout());
        title.add(new JLabel("Deck", SwingConstants.CENTER));
        add(title, BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);
    }

    @Override
    public void update(GameEvent e) {
        Deck deck = e.getSrc().getConfiguration().getDeck();
        if(deck == null) return;
        cardsPanel.removeAll();
        cardsPanel.setLayout(new GridLayout(deck.numCards(), 1));
        List<GameCards> cards = deck.getCards();
        for(int i = 0; i<deck.numCards(); i++) {
            cardsPanel.add(new JLabel("" + cards.get(i), SwingConstants.CENTER));
        }
        repaint();
        revalidate();
    }
}
