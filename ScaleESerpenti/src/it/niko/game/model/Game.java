package it.niko.game.model;

import it.niko.game.Configuration;

public interface Game {

    void addGameListener(GameListener gl);

    void removeGameListener(GameListener gl);

    void nextRound();

    boolean isGameFinish();

    String getCurrentPlayerState();

    String getRoundLog();

    Configuration getConfiguration();
}
