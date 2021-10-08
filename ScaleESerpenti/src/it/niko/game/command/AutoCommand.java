package it.niko.game.command;

import it.niko.game.model.Game;

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
