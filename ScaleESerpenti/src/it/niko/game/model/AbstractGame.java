package it.niko.game.model;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractGame implements Game {

    private final List<GameListener> listeners = new LinkedList<>();

    @Override
    public void addGameListener(GameListener gl) {
        if(listeners.contains(gl))
            return;
        listeners.add(gl);
    }

    @Override
    public void removeGameListener(GameListener gl) {
        listeners.remove(gl);
    }

    protected void notifyListeners() {
        for(GameListener gl : listeners)
            gl.update();
    }
}
