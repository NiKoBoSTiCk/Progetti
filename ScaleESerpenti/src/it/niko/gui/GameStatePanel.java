package it.niko.gui;

import it.niko.game.Configuration;
import it.niko.game.PlayerState;
import it.niko.game.model.GameListener;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class GameStatePanel extends JPanel implements GameListener {

    private final HashMap<String, JLabel[]> data;

    public GameStatePanel(Configuration config) {
        data = new HashMap<>();

        setLayout(new GridLayout(config.getNumPlayers() + 1, 4));

        add(new JLabel("Player"), BorderLayout.CENTER);
        add(new JLabel("Position"), BorderLayout.CENTER);
        add(new JLabel("Stops"), BorderLayout.CENTER);
        add(new JLabel("Ban"), BorderLayout.CENTER);

        for(int i = 1; i<= config.getNumPlayers(); i++) {
            JLabel[] state = new JLabel[4];
            state[0] = new JLabel("Player" + i);
            state[1] = new JLabel("0");
            state[2] = new JLabel("0");
            state[3] = new JLabel("n");
            add(state[0]); add(state[1]); add(state[2]); add(state[3]);
            data.put("Player " + i, state);
        }
    }

    @Override
    public void update() {
        repaint();
        revalidate();
    }

    public void setState(PlayerState state) {
        JLabel[] v = data.get(state.name());
        v[1].setText(String.valueOf(state.pos()));
        v[2].setText(String.valueOf(state.stops()));
        v[3].setText(String.valueOf(state.ban()));
        data.put(state.name(), v);
    }
}
