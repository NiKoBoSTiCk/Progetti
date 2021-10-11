package it.niko.gui;

import it.niko.game.GameBoxes;
import it.niko.game.GameEvent;
import it.niko.game.model.Board;
import it.niko.game.model.Game;
import it.niko.game.model.GameListener;
import javax.swing.*;
import java.awt.*;

public class BoardGameListener extends JPanel implements GameListener {

    @Override
    public void update(GameEvent e) {
        Game game = e.getSrc();
        switch(e.getEventType()) {
            case CONFIG -> {
                int row = 0;
                add(new JLabel("Position"));
                add(new JLabel("Content"));
                Board board = game.getConfiguration().getBoard();
                for(int i=1; i<board.getNumBoxes(); i++) {
                    GameBoxes gameBoxes = board.boxContent(i);
                    if(gameBoxes != null) {
                        switch(gameBoxes) {
                            case LadderBase -> {
                                add(new JLabel("" + i));
                                add(new JLabel("Ladder to" + board.boxEffect(i)));
                                row++;
                            }
                            case SnakeHead -> {
                                add(new JLabel("" + i));
                                add(new JLabel("Snake to" + board.boxEffect(i)));
                                row++;
                            }
                            case Bench, Inn, Dice, Spring, DrawCard -> {
                                add(new JLabel("" + i));
                                add(new JLabel("" + gameBoxes));
                                row++;
                            }
                        }
                    }
                }
                setLayout(new GridLayout(row, 2));
                repaint();
                revalidate();
            }
            case ROUND -> {}
        }
    }
}
