package it.niko.game.model;

import it.niko.game.Configuration;
import it.niko.game.PlayerState;

public interface Game {

    void addGameListener(GameListener gl);

    void removeGameListener(GameListener gl);

    void configGame(Configuration config);

    void nextRound();

    boolean isFinish();

    boolean isConfigurationSet();

    PlayerState getCurrentPlayerState();

    String getRoundLog();

    Configuration getConfiguration();

    void save(String fileName);

    void load(String fileName);
}
