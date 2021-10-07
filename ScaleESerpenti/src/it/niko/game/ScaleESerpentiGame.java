package it.niko.game;

import it.niko.game.model.Board;
import it.niko.game.model.GameAbstract;

import java.util.*;

public class ScaleESerpentiGame extends GameAbstract {
    private int numBoxes;
    private boolean dadoSingolo;
    private boolean lancioSoloDado;
    private boolean doppioSei;
    private boolean caselleSosta;
    private boolean casellePremio;
    private boolean casellePesca;
    private boolean carteDivieto;
    private boolean isConfigurationSet;
    private Board board;
    private Queue<Player> players;
    private Dice dice;
    private Deck deck;

    //STATO GIOCO
    private boolean isGameFinish;
    private Player currentPlayer; //posizione, soste e divieti
    private Configuration configuration;
    private StringBuilder roundLog;

    public ScaleESerpentiGame() {
        isConfigurationSet = false;
    }

    public ScaleESerpentiGame(Configuration c) {
        configGame(c);
    }

    public void configGame(Configuration c) {
        configuration = c;
        numBoxes = c.getNumCaselle();
        dadoSingolo = c.isDadoSingolo();
        lancioSoloDado = c.isLancioSoloDado();
        doppioSei = c.isDoppioSei();
        caselleSosta = c.isCaselleSosta();
        casellePremio = c.isCasellePremio();
        casellePesca = c.isCasellePesca();
        carteDivieto = c.isCarteDivieto();
        dice = Dice.getInstance();
        players = new ArrayDeque<>(c.getNumPlayers());
        board = c.getBoard();
        deck = c.getDeck();
        if(deck != null) deck.shuffle();
        for(int i = 1; i<=c.getNumPlayers(); i++) {
            players.add(new Player("Giocatore " + i));
        }
        isGameFinish = false;
        isConfigurationSet = true;
    }

    @Override
    public void nextRound() {
        if(!isConfigurationSet) throw new IllegalStateException();
        if(isGameFinish) throw new IllegalStateException();
        currentPlayerRound();
        notify(new GameEvent(this)); // notifico i listeners del cambiamento di stato del gioco
    }

    @Override
    public boolean isGameFinish() {
        return isGameFinish;
    }

    @Override
    public String getRoundLog() {
        return roundLog.toString();
    }

    @Override
    public String getCurrentPlayerState() {
        return currentPlayer.getName()
                + " Posizione = " + currentPlayer.getPos()
                + " Soste = " + currentPlayer.getStops()
                + " Divieto = " + (currentPlayer.hasBanCard()?"si":"no");
    }

    @Override
    public Configuration getConfiguration() {
        return configuration; //immutabile
    }

    private void currentPlayerRound() {
        roundLog = new StringBuilder();
        currentPlayer = players.peek();
        if(currentPlayer == null) throw new IllegalStateException();
        roundLog.append("\n");
        roundLog.append(String.format("Turno %s\n", currentPlayer.getName()));
        if(checkStops()) {
            players.poll();
            players.offer(currentPlayer);
            return;
        }
        int thr = throwDice();
        int newPos = calculatePosition(thr);
        currentPlayer.setPos(newPos);
        if(newPos == numBoxes) {
            roundLog.append(String.format("Fine Partita. Vince %s.\n", currentPlayer.getName()));
            isGameFinish = true;
            return;
        }
        if(doppioSei && thr == 12) {
            roundLog.append(String.format("%s ha realizzato un doppio sei, ripete il turno.\n", currentPlayer.getName()));
        }
        else {
            roundLog.append(String.format("Fine Turno %s.\n", currentPlayer.getName()));
            players.poll();
            players.offer(currentPlayer);
        }
    }

    private int calculatePosition(int thr) {
        int newPos = currentPlayer.getPos() + thr;
        roundLog.append(String.format("%s si muove da %d a %d.\n", currentPlayer.getName(), currentPlayer.getPos(), newPos));
        newPos = checkNumBoxesExceeded(newPos);
        int oldPos = newPos;
        while(board.boxContent(newPos) != null) {
            newPos = checkBox(newPos, thr);
            if(newPos == oldPos) break;
            roundLog.append(String.format("%s si muove da %d a %d.\n", currentPlayer.getName(), oldPos, newPos));
            newPos = checkNumBoxesExceeded(newPos);
            oldPos = newPos;
        }
        return newPos;
    }

    private int checkNumBoxesExceeded(int newPos) {
        if(newPos > numBoxes){
            newPos = numBoxes - (newPos - numBoxes);
            roundLog.append(String.format("%s ha superato il limite del tabellone, tornando alla posizione %d\n", currentPlayer.getName(), newPos));
        }
        return newPos;
    }

    private int checkBox(int newPos, int thr) {
        GameBoxes box = board.boxContent(newPos);
        if(box == null || box == GameBoxes.top || box == GameBoxes.tail) return newPos;
        roundLog.append(String.format("%s è su una casella %s.\n", currentPlayer.getName(), box));
        switch(box) {
            case inn, bench -> { if(caselleSosta) currentPlayer.giveStop(box); }
            case dice -> { if(casellePremio) return newPos + throwDice(); }
            case spring -> { if(casellePremio) return newPos + thr; }
            case drawCard -> { if(casellePesca) return drawCard(newPos, thr); }
            case base, head -> { return board.boxEffect(newPos); }
            default -> { return newPos; }
        }
        return newPos;
    }

    private boolean checkStops() {
        if(caselleSosta && currentPlayer.hasStops()) {
            roundLog.append(String.format("%s è in sosta.\n", currentPlayer.getName()));
            if(carteDivieto && currentPlayer.hasBanCard()) {
                currentPlayer.usesBanCard();
                roundLog.append(String.format("%s usa una carta divieto evitando la sosta.\n", currentPlayer.getName()));
                return false;
            }
            else {
                currentPlayer.makeStop();
                roundLog.append(String.format("%s sconta la sosta.\n", currentPlayer.getName()));
                return true;
            }
        }
        return false;
    }

    private int drawCard(int newPos, int thr) {
        GameCards card = deck.drawCard();
        roundLog.append(String.format("%s pesca la carta %s.\n", currentPlayer.getName(), card));
        switch(card) {
            case bench -> currentPlayer.giveStop(GameBoxes.bench);
            case inn -> currentPlayer.giveStop(GameBoxes.inn);
            case ban -> { if(carteDivieto) currentPlayer.giveBanCard(); }
            case dice -> { return newPos + throwDice(); }
            case spring -> { return newPos + thr; }
        }
        return newPos;
    }
    
    private int throwDice() {
        int thr;
        if(dadoSingolo)
            thr = dice.throwDice();
        else if(lancioSoloDado && currentPlayer.getPos() >= numBoxes - dice.getFaces())
            thr = dice.throwDice();
        else thr = dice.throwDice() + dice.throwDice();
        roundLog.append(String.format("%s lancia i dadi e ottiene %d.\n", currentPlayer.getName(), thr));
        return thr;
    }
}
