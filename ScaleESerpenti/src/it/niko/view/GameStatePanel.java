package it.niko.view;

import it.niko.game.model.Game;
import it.niko.game.GameEvent;
import it.niko.game.model.GameListener;
import javax.swing.*;

public class GameStatePanel extends JComponent implements GameListener {

    @Override
    public void update(GameEvent e) {
        Game game = e.getSource();
        game.getCurrentPlayerState();
        game.isGameFinish();
    }
    //TODO
}
