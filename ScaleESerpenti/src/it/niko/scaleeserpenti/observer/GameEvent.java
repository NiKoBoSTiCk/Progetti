package it.niko.scaleeserpenti.observer;

import it.niko.scaleeserpenti.game.Game;

public class GameEvent {
    private final Game src;
    private final EventType eventType;

    public GameEvent(Game src, EventType eventType) {
        this.src = src;
        this.eventType = eventType;
    }

    public Game getSrc() {
        return src;
    }

    public EventType getEventType() {
        return eventType;
    }
}
