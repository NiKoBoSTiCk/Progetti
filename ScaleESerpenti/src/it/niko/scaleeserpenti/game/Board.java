package it.niko.scaleeserpenti.game;
/*
  Interfaccia di un tabellone del gioco Scale e Serpenti, che permette
  di sapere il contenuto di una casella,
  il suo “effetto” ovvero il cambiamento alla posizione che subisce un giocatore su quella casella
  e di aggiungere scale, serpenti e caselle speciali.
 */
public interface Board {

    int getNumBoxes();

    int boxEffect(int pos);

    GameBoxes boxContent(int pos);

    boolean addSnake(int head, int tail);

    boolean addLadder(int base, int top);

    boolean addSpecialBox(int pos, GameBoxes type);
}
