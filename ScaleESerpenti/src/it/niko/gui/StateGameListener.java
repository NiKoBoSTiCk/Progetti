package it.niko.gui;

import it.niko.game.Configuration;
import it.niko.game.GameEvent;
import it.niko.game.PlayerState;
import it.niko.game.model.Game;
import it.niko.game.model.GameListener;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class StateGameListener extends JPanel implements GameListener {

    private final HashMap<String, JLabel[]> data;
    private final JPanel statePanel;

    public StateGameListener() {
        data = new HashMap<>();
        statePanel = new JPanel();
        JPanel panel = new JPanel();
        setLayout(new BorderLayout());
        panel.setLayout(new GridLayout(1, 4));
        panel.add(new JLabel("Player"), BorderLayout.CENTER);
        panel.add(new JLabel("Position"), BorderLayout.CENTER);
        panel.add(new JLabel("Stops"), BorderLayout.CENTER);
        panel.add(new JLabel("Ban"), BorderLayout.CENTER);
        add(panel, BorderLayout.NORTH);
        add(statePanel, BorderLayout.CENTER);
    }

    @Override
    public void update(GameEvent e) {
        Game game = e.getSrc();
        switch(e.getEventType()) {
            case CONFIG -> {
                Configuration config = game.getConfiguration();
                statePanel.removeAll();
                statePanel.setLayout(new GridLayout(config.getNumPlayers() + 1, 4));
                for(int i=1; i<=config.getNumPlayers(); i++) {
                    JLabel[] state = new JLabel[4];
                    state[0] = new JLabel("Player" + i);
                    state[1] = new JLabel("0");
                    state[2] = new JLabel("0");
                    state[3] = new JLabel("n");
                    statePanel.add(state[0]);
                    statePanel.add(state[1]);
                    statePanel.add(state[2]);
                    statePanel.add(state[3]);
                    data.put("Player " + i, state);
                    repaint();
                    revalidate();
                }
            }
            case ROUND -> {
                PlayerState state = game.getCurrentPlayerState();
                JLabel[] v = data.get(state.name());
                v[1].setText(String.valueOf(state.pos()));
                v[2].setText(String.valueOf(state.stops()));
                v[3].setText(state.ban()?"y":"n");
                data.put(state.name(), v);
                repaint();
                revalidate();
            }
        }
    }
}
