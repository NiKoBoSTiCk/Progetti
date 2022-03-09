package it.niko.scaleeserpenti.command;

import it.niko.scaleeserpenti.game.Game;

public class AutoCommand implements Command {
    private final Game game;

    public AutoCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        while(!game.isFinish()) {
            game.nextRound();
        }
    }
}
