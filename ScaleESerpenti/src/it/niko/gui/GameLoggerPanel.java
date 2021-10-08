package it.niko.gui;

import it.niko.game.model.GameListener;
import javax.swing.*;
import java.awt.*;

public class GameLoggerPanel extends JPanel implements GameListener {

    private final JTextArea textArea;

    public GameLoggerPanel() {
        textArea = new JTextArea(20, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void update() {
        repaint();
        revalidate();
    }

    public void setLog(String log) {
        System.out.println(log);
        textArea.append(log);
    }
}
