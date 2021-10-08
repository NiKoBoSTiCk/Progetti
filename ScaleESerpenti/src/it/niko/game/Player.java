package it.niko.game;

public class Player {
    private final String name;
    private int pos;
    private int stops;
    private boolean banCard;

    public Player(String name) {
        this.name = name;
        this.pos = 0;
        this.stops = 0;
    }

    public void giveStop(GameBoxes type) {
        switch(type) {
            case Bench -> stops += 1;
            case Inn -> stops += 3;
        }
    }

    public void setPos(int pos) { this.pos = pos; }

    public int getPos() { return pos; }

    public String getName() { return name; }

    public void makeStop() { stops--; }

    public boolean hasStops() { return stops > 0; }

    public int getStops() { return stops; }

    public void giveBanCard() { banCard = true; }

    public void usesBanCard() { stops = 0; banCard = false; }

    public boolean hasBanCard() { return banCard; }
}
