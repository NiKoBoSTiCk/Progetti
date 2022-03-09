package it.niko.scaleeserpenti.observer;

import it.niko.scaleeserpenti.game.GameBoxes;
import it.niko.scaleeserpenti.game.Board;
import javax.swing.*;
import java.awt.*;

/*
  Rappresentazione del contenuto delle caselle del tabellone
 */
public class BoxGameListener extends JPanel implements GameListener {
    private final JPanel configState;

    public BoxGameListener() {
        configState = new JPanel();
        JPanel title = new JPanel();
        title.setLayout(new GridLayout(1, 2));
        title.add(new JLabel("Box", SwingConstants.CENTER));
        title.add(new JLabel("Content", SwingConstants.CENTER));
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(configState, BorderLayout.CENTER);
    }

    @Override
    public void update(GameEvent e) {
        if(e.getEventType() == EventType.CONFIG) {
            configState.removeAll();
            Board board = e.getSrc().getConfiguration().getBoard();
            int row = 0;
            for(int i=1; i<board.getNumBoxes(); i++)
                if(board.boxContent(i) != null)
                    switch(board.boxContent(i)) {
                        case LadderBase, SnakeHead, Bench, Inn, Dice, Spring, DrawCard -> row++;
                    }
            System.out.println(row);
            configState.setLayout(new GridLayout(row, 2));
            for(int i=1; i<board.getNumBoxes(); i++) {
                GameBoxes gameBoxes = board.boxContent(i);
                if(gameBoxes != null) {
                    switch(gameBoxes) {
                        case LadderBase, SnakeHead -> {
                            configState.add(new JLabel("" + i, SwingConstants.CENTER));
                            configState.add(new JLabel(gameBoxes + " to " + board.boxEffect(i), SwingConstants.CENTER));
                            row++;
                        }
                        case Bench, Inn, Dice, Spring, DrawCard -> {
                            configState.add(new JLabel("" + i, SwingConstants.CENTER));
                            configState.add(new JLabel("" + gameBoxes, SwingConstants.CENTER));
                            row++;
                        }
                    }
                }
            }
            repaint();
            revalidate();
        }
    }
}
