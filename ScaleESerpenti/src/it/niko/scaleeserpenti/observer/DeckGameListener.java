package it.niko.scaleeserpenti.observer;

import it.niko.scaleeserpenti.game.Deck;
import it.niko.scaleeserpenti.game.GameCards;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DeckGameListener extends JPanel implements GameListener {

    private final JPanel cardsPanel;

    public DeckGameListener() {
        cardsPanel = new JPanel();
        JPanel panel = new JPanel();
        setLayout(new BorderLayout());
        panel.add(new JLabel("Deck"), SwingConstants.CENTER);
        add(panel, BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);
    }

    @Override
    public void update(GameEvent e) {
        Deck deck = e.getSrc().getConfiguration().getDeck();
        if(deck == null) return;
        cardsPanel.removeAll();
        cardsPanel.setLayout(new GridLayout(deck.numCard(), 1));
        List<GameCards> cards = deck.getCards();
        for(int i=0; i<deck.numCard(); i++) {
            JLabel card = new JLabel("" + cards.get(i), SwingConstants.CENTER);
            cardsPanel.add(card);
        }
        repaint();
        revalidate();
    }
}
