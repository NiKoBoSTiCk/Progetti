package it.niko.game;

import it.niko.game.model.Game;

public class GameEvent {
    private final Game src;

    public GameEvent(Game src) {
        this.src = src;
    }

    public Game getSrc() {
        return src;
    }
}
