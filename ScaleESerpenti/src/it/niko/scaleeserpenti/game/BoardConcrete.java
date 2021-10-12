package it.niko.scaleeserpenti.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BoardConcrete implements Board, Serializable {
    private final int numBoxes;
    private final int row;
    private final int column;
    private final Map<Integer, GameBoxes> boxesOccupied;
    private final Map<Integer, Integer> boxesRow;
    private final Map<Integer, Integer> snakes;
    private final Map<Integer, Integer> ladders;

    public BoardConcrete(int numBoxes, int row, int column) {
        if(row * column != numBoxes) throw new IllegalArgumentException();
        if(row < 3 || column < 3) throw new IllegalArgumentException();
        this.numBoxes = numBoxes;
        this.row = row;
        this.column = column;

        this.boxesRow = new HashMap<>();
        int x = 0;
        for(int i=row-1; i>=0; i--)
            for(int j=0; j<column; j++)
                boxesRow.put(++x, i);

        boxesOccupied = new HashMap<>();
        snakes = new HashMap<>();
        ladders = new HashMap<>();
    }

    @Override
    public int getNumBoxes() {
        return numBoxes;
    }

    @Override
    public GameBoxes boxContent(int pos) {
        return boxesOccupied.get(pos);
    }

    @Override
    public int boxEffect(int pos) {
        return switch(boxesOccupied.get(pos)) {
            case SnakeHead -> snakes.get(pos);
            case LadderBase -> ladders.get(pos);
            default -> pos;
        };
    }

    @Override
    public boolean addSnake(int head, int tail) {
        if(head < tail || head < row || head == numBoxes)
            return false;
        if(boxesRow.get(head).equals(boxesRow.get(tail))) //testa e coda non sulla stessa riga
            return false;
        if(boxesOccupied.containsKey(head) || boxesOccupied.containsKey(tail))
            return false;
        boxesOccupied.put(head, GameBoxes.SnakeHead);
        boxesOccupied.put(tail,  GameBoxes.SnakeTail);
        snakes.put(head, tail);
        return true;
    }

    @Override
    public boolean removeSnake(int head, int tail) {
        if(!boxesOccupied.containsKey(head) && !boxesOccupied.containsKey(tail))
            return false;
        boxesOccupied.remove(head);
        boxesOccupied.remove(tail);
        snakes.remove(head);
        return true;
    }

    @Override
    public boolean addLadder(int base, int top) {
        if(base > top || top <= column || base > numBoxes - column)
            return false;
        if(boxesRow.get(base).equals(boxesRow.get(top))) //base e cima non sulla stessa riga
            return false;
        if(boxesOccupied.containsKey(base) || boxesOccupied.containsKey(top))
            return false;
        boxesOccupied.put(base, GameBoxes.LadderBase);
        boxesOccupied.put(top, GameBoxes.LadderTop);
        ladders.put(base, top);
        return true;
    }

    @Override
    public boolean removeLadder(int base, int top) {
        if(!boxesOccupied.containsKey(base) && !boxesOccupied.containsKey(top))
            return false;
        boxesOccupied.remove(base);
        boxesOccupied.remove(top);
        ladders.remove(base);
        return true;
    }

    @Override
    public boolean addSpecialBox(int pos, GameBoxes type) {
        if(boxesOccupied.containsKey(pos))
            return false;
        boxesOccupied.put(pos, type);
        return true;
    }

    @Override
    public boolean removeSpecialBox(int pos) {
        if(!boxesOccupied.containsKey(pos))
            return false;
        boxesOccupied.remove(pos);
        return true;
    }
}

