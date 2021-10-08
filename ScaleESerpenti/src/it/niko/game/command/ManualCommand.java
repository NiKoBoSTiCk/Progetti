package it.niko.game.command;

import it.niko.game.model.Game;
import it.niko.gui.GameLoggerPanel;
import it.niko.gui.GameStatePanel;

public class ManualCommand implements Command {

    private final GameStatePanel gameState;
    private final GameLoggerPanel gameLogger;
    private final Game game;

    public ManualCommand(GameStatePanel gameState, GameLoggerPanel gameLogger, Game game) {
        this.gameState = gameState;
        this.gameLogger = gameLogger;
        this.game = game;
    }

    @Override
    public boolean execute() {
        if(game.isFinish())
            return false;
        game.nextRound();
        gameLogger.setLog(game.getRoundLog());
        gameState.setState(game.getCurrentPlayerState());
        return true;
    }
}
