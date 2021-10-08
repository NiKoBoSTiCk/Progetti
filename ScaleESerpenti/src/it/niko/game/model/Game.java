package it.niko.game.model;

import it.niko.game.Configuration;
import it.niko.game.PlayerState;

public interface Game {

    void addGameListener(GameListener gl);

    void removeGameListener(GameListener gl);

    void nextRound();

    boolean isFinish();

    PlayerState getCurrentPlayerState();

    String getRoundLog();

    Configuration getConfiguration();
}
