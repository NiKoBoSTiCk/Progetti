package it.niko.scaleeserpenti.observer;

import it.niko.scaleeserpenti.builder.Configuration;
import it.niko.scaleeserpenti.game.PlayerState;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.HashMap;

/*
  Rappresentazione dello stato dei giocatori
 */
public class StateGameListener extends JPanel implements GameListener {

    private final HashMap<String, JLabel[]> data;
    private final JPanel statePanel;

    public StateGameListener() {
        data = new HashMap<>();
        statePanel = new JPanel();
        JPanel title = new JPanel();
        title.setLayout(new GridLayout(1, 4));
        title.add(new JLabel("Player", SwingConstants.CENTER));
        title.add(new JLabel("Position", SwingConstants.CENTER));
        title.add(new JLabel("Stops", SwingConstants.CENTER));
        title.add(new JLabel("Ban Card", SwingConstants.CENTER));
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(statePanel, BorderLayout.CENTER);
    }

    @Override
    public void update(GameEvent e) {
        switch(e.getEventType()) {
            case CONFIG -> {
                Configuration config = e.getSrc().getConfiguration();
                statePanel.removeAll();
                statePanel.setLayout(new GridLayout(config.getNumPlayers() , 4, 2, 2));
                statePanel.setBackground(Color.BLACK);
                statePanel.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));

                for(int i=1; i<=config.getNumPlayers(); i++) {
                    JLabel[] state = new JLabel[4];

                    JLabel player = new JLabel("Player" + i, SwingConstants.CENTER);
                    player.setOpaque(true);
                    player.setBackground(Color.CYAN);
                    state[0] = player;

                    JLabel pos = new JLabel("1", SwingConstants.CENTER);
                    pos.setOpaque(true);
                    pos.setBackground(Color.YELLOW);
                    state[1] = pos;

                    JLabel stops = new JLabel("0", SwingConstants.CENTER);
                    stops.setOpaque(true);
                    stops.setBackground(Color.WHITE);
                    state[2] = stops;

                    JLabel ban = new JLabel("n", SwingConstants.CENTER);
                    ban.setOpaque(true);
                    ban.setBackground(Color.WHITE);
                    state[3] = ban;

                    statePanel.add(state[0]);
                    statePanel.add(state[1]);
                    statePanel.add(state[2]);
                    statePanel.add(state[3]);
                    data.put("Player" + i, state);
                }
            }
            case ROUND -> {
                PlayerState playerState = e.getSrc().getCurrentPlayerState();
                JLabel[] state = data.get(playerState.name());
                state[1].setText(String.valueOf(playerState.pos()));
                state[2].setText(String.valueOf(playerState.stops()));
                state[3].setText(playerState.ban()?"y":"n");

                if(playerState.stops() > 1)
                    state[2].setBackground(Color.RED);
                else
                    state[2].setBackground(Color.WHITE);
                if(playerState.ban())
                    state[3].setBackground(Color.MAGENTA);
                else
                    state[3].setBackground(Color.WHITE);

                data.put(playerState.name(), state);

                if( e.getSrc().isFinish())
                    for(int i=0; i<4; i++)
                        state[i].setBackground(Color.GREEN);
            }
        }
        repaint();
        revalidate();
    }
}
