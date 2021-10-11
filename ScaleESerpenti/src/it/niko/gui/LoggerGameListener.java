package it.niko.gui;

import it.niko.game.GameEvent;
import it.niko.game.model.Game;
import it.niko.game.model.GameListener;
import javax.swing.*;
import java.awt.*;

public class LoggerGameListener extends JPanel implements GameListener {

    private final JTextArea textArea;

    public LoggerGameListener() {
        textArea = new JTextArea(20, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void update(GameEvent e) {
        switch(e.getEventType()) {
            case CONFIG -> textArea.setText("");
            case ROUND -> {
                Game game = e.getSrc();
                textArea.append(game.getRoundLog());
                repaint();
                revalidate();
            }
        }
    }
}
