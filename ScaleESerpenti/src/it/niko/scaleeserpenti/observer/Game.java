package it.niko.scaleeserpenti.observer;

import it.niko.scaleeserpenti.config.Configuration;
import it.niko.scaleeserpenti.game.PlayerState;

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
