package it.niko.scaleeserpenti.game;

import it.niko.scaleeserpenti.observer.AbstractGame;
import it.niko.scaleeserpenti.config.Configuration;
import it.niko.scaleeserpenti.observer.EventType;
import it.niko.scaleeserpenti.observer.GameEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class ScaleESerpentiGame extends AbstractGame {
    private int numBoxes;
    private boolean isSingleDice;
    private boolean isRollSingleDice;
    private boolean isDoubleSix;
    private boolean isStopBoxes;
    private boolean isRewardBoxes;
    private boolean isDrawCardBoxes;
    private boolean isBanCards;
    private Board board;
    private Queue<Player> players;
    private Dice dice;
    private Deck deck;

    //STATO GIOCO
    private boolean isConfigurationSet;
    private boolean isGameFinish;
    private Player currentPlayer;
    private Configuration configuration;
    private StringBuilder roundLog;

    public ScaleESerpentiGame() {
        isConfigurationSet = false;
    }

    @Override
    public void configGame(Configuration c) {
        if(c == null) {
            isConfigurationSet = false;
            return;
        }
        if(c.getNumPlayers() <= 0 || c.getNumPlayers() > 20)
            throw new IllegalStateException();
        configuration = c;
        isConfigurationSet = true;
        isGameFinish = false;
        isSingleDice = c.isSingleDice();
        isRollSingleDice = c.isRollSingleDice();
        isDoubleSix = c.isDoubleSix();
        isStopBoxes = c.isStopBoxes();
        isRewardBoxes = c.isRewardBoxes();
        isDrawCardBoxes = c.isDrawCardBoxes();
        isBanCards = c.isBanCards();
        numBoxes = c.getNumBoxes();
        board = c.getBoard();
        deck = c.getDeck();

        dice = Dice.getInstance();
        players = new ArrayDeque<>(c.getNumPlayers());

        if(deck != null) deck.shuffle();

        for(int i = 1; i<=c.getNumPlayers(); i++) {
            players.add(new Player("Player " + i));
        }

        notifyListeners(new GameEvent(this, EventType.CONFIG));
    }

    @Override
    public void nextRound() {
        if(!isConfigurationSet) throw new IllegalStateException();
        if(isGameFinish) throw new IllegalStateException();
        currentPlayerRound();
        notifyListeners(new GameEvent(this, EventType.ROUND)); // notifico i listeners del cambiamento di stato del gioco
    }

    @Override
    public PlayerState getCurrentPlayerState() {
        return new PlayerState(currentPlayer.getName(), currentPlayer.getPos(),
                currentPlayer.getStops(), currentPlayer.hasBanCard());
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public boolean isFinish() {
        return isGameFinish;
    }

    @Override
    public String getRoundLog() {
        return roundLog.toString();
    }

    @Override
    public boolean isConfigurationSet() {
        return isConfigurationSet;
    }

    @Override
    public void save(String fileName) {
        ObjectOutputStream oss;
        try{
            oss = new ObjectOutputStream(new FileOutputStream(fileName));
            oss.writeObject(configuration);
            oss.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void load(String nomeFile){
        ObjectInputStream ois;
        try{
            ois = new ObjectInputStream(new FileInputStream(nomeFile));
            configuration = (Configuration) ois.readObject();
            configGame(configuration);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void currentPlayerRound() {
        roundLog = new StringBuilder();
        currentPlayer = players.peek();
        if(currentPlayer == null) throw new IllegalStateException();
        roundLog.append("\n");
        roundLog.append(String.format("Round %s\n", currentPlayer.getName()));
        if(checkStops()) {
            players.poll();
            players.offer(currentPlayer);
            return;
        }
        int thr = throwDice();
        int newPos = calculatePosition(thr);
        currentPlayer.setPos(newPos);
        if(newPos == numBoxes) {
            roundLog.append(String.format("End of Game. %s wins.\n", currentPlayer.getName()));
            isGameFinish = true;
            return;
        }
        if(isDoubleSix && thr == 12) {
            roundLog.append(String.format("%s rolls a double six, repeats the round.\n", currentPlayer.getName()));
        }
        else {
            roundLog.append(String.format("End %s's Round.\n", currentPlayer.getName()));
            players.poll();
            players.offer(currentPlayer);
        }
    }

    private int calculatePosition(int thr) {
        int newPos = currentPlayer.getPos() + thr;
        roundLog.append(String.format("%s move from %d to %d.\n", currentPlayer.getName(), currentPlayer.getPos(), newPos));
        newPos = checkBoardLimit(newPos);
        int oldPos = newPos;
        while(board.boxContent(newPos) != null) {
            newPos = checkBox(newPos, thr);
            if(newPos == oldPos) break;
            roundLog.append(String.format("%s move from %d to %d.\n", currentPlayer.getName(), oldPos, newPos));
            newPos = checkBoardLimit(newPos);
            oldPos = newPos;
        }
        return newPos;
    }

    private int checkBoardLimit(int newPos) {
        if(newPos > numBoxes){
            newPos = numBoxes - (newPos - numBoxes);
            roundLog.append(String.format("%s exceeded the board limit, going back to %d\n", currentPlayer.getName(), newPos));
        }
        return newPos;
    }

    private int checkBox(int newPos, int thr) {
        GameBoxes box = board.boxContent(newPos);
        if(box == null || box == GameBoxes.LadderTop || box == GameBoxes.SnakeTail) return newPos;
        roundLog.append(String.format("%s is on a [%s] box.\n", currentPlayer.getName(), box));
        switch(box) {
            case Inn, Bench -> { if(isStopBoxes) currentPlayer.giveStop(box); }
            case Dice -> { if(isRewardBoxes) return newPos + throwDice(); }
            case Spring -> { if(isRewardBoxes) return newPos + thr; }
            case DrawCard -> { if(isDrawCardBoxes) return drawCard(newPos, thr); }
            case LadderBase, SnakeHead -> { return board.boxEffect(newPos); }
            default -> { return newPos; }
        }
        return newPos;
    }

    private boolean checkStops() {
        if(isStopBoxes && currentPlayer.hasStops()) {
            roundLog.append(String.format("%s has a stop.\n", currentPlayer.getName()));
            if(isBanCards && currentPlayer.hasBanCard()) {
                currentPlayer.usesBanCard();
                deck.addCard(GameCards.Ban); // rimetti la carta nel mazzo
                roundLog.append(String.format("%s uses a ban card to avoid the stop.\n", currentPlayer.getName()));
                return false;
            }
            else {
                currentPlayer.makeStop();
                roundLog.append(String.format("%s makes the stop.\n", currentPlayer.getName()));
                return true;
            }
        }
        return false;
    }

    private int drawCard(int newPos, int thr) {
        GameCards card = deck.drawCard();
        if(card == null) {
            roundLog.append(String.format("%s doesn't draw because the cards are out.\n", currentPlayer.getName()));
            return newPos;
        }
        roundLog.append(String.format("%s draws the card [%s].\n", currentPlayer.getName(), card));
        switch(card) {
            case Bench -> currentPlayer.giveStop(GameBoxes.Bench);
            case Inn -> currentPlayer.giveStop(GameBoxes.Inn);
            case Ban -> { if(isBanCards) currentPlayer.giveBanCard(); }
            case Dice -> { return newPos + throwDice(); }
            case Spring -> { return newPos + thr; }
        }
        return newPos;
    }
    
    private int throwDice() {
        int thr;
        if(isSingleDice)
            thr = dice.throwDice();
        else if(isRollSingleDice && currentPlayer.getPos() >= numBoxes - dice.getFaces())
            thr = dice.throwDice();
        else thr = dice.throwDice() + dice.throwDice();
        roundLog.append(String.format("%s throws %d.\n", currentPlayer.getName(), thr));
        return thr;
    }
}
