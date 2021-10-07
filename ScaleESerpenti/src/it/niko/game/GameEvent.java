package it.niko.game;

import it.niko.game.model.Game;

public class GameEvent {

    private Game src;

    public GameEvent(Game src) {
        this.src = src;
    }

    public Game getSource() {
        return src;
    }

}
