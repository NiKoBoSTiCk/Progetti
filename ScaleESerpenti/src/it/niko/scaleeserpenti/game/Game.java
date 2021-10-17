package it.niko.scaleeserpenti.game;

import it.niko.scaleeserpenti.builder.Configuration;
import it.niko.scaleeserpenti.observer.GameListener;

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
