package it.niko.scaleeserpenti.command;

import it.niko.scaleeserpenti.observer.Game;

public class AutoCommand implements Command {
    private final Game game;

    public AutoCommand(Game game) {
        this.game = game;
    }

    @Override
    public boolean execute() {
        while(!game.isFinish()) {
            game.nextRound();
        }
        return game.isFinish();
    }
}
