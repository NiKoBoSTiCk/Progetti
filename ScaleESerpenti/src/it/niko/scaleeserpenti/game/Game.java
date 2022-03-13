package it.niko.scaleeserpenti.game;

import it.niko.scaleeserpenti.builder.Configuration;
import it.niko.scaleeserpenti.observer.GameListener;

import java.io.IOException;

/*
  Interfaccia di un gioco configurabile a turni su tabellone con dei giocatori
  che cambiano stato e che permette il salvataggio/caricamento
*/
public interface Game {

    /*
       metodo che aggiunge un ascoltatore di eventi al gioco
     */
    void addGameListener(GameListener gl);

    void removeGameListener(GameListener gl);

    void configGame(Configuration config);

    void nextRound();

    boolean isFinish();

    boolean isConfigurationSet();

    PlayerState getCurrentPlayerState();

    String getRoundLog();

    Configuration getConfiguration();

    void save(String fileName) throws IOException;

    void load(String fileName) throws IOException, ClassNotFoundException;
}
