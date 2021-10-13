package it.niko.scaleeserpenti.observer;

import it.niko.scaleeserpenti.builder.Configuration;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class BoardGameListener extends JPanel implements GameListener {

    private final JPanel boardPanel;

    public BoardGameListener() {
        JPanel title = new JPanel();
        title.add(new JLabel("Board", SwingConstants.CENTER));
        boardPanel = new JPanel();
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
    }

    @Override
    public void update(GameEvent e) {
        if(e.getEventType() == EventType.CONFIG) {
            boardPanel.removeAll();
            Configuration config = e.getSrc().getConfiguration();
            int row = config.getRow();
            int column = config.getColumn();
            boardPanel.setLayout(new GridLayout(row, column, 2, 2));
            boardPanel.setBackground(Color.BLACK);
            boardPanel.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
            JLabel[][] boxes = new JLabel[row][column];
            int x = 0;
            boolean alternati = true;
            for(int i=row-1; i>=0; i--) {
                if(alternati) {
                    for(int j=0; j<column; j++) {
                        JLabel box = new JLabel("" + ++x, SwingConstants.CENTER);
                        box.setOpaque(true);
                        if(x % 2 == 0) box.setBackground(Color.LIGHT_GRAY);
                        else box.setBackground(Color.WHITE);
                        boxes[i][j] = box;
                    }
                    alternati = false;
                }
                else {
                    for(int j=column-1; j>=0; j--) {
                        JLabel box = new JLabel("" + ++x, SwingConstants.CENTER);
                        box.setOpaque(true);
                        if(x % 2 == 0) box.setBackground(Color.LIGHT_GRAY);
                        else box.setBackground(Color.WHITE);
                        boxes[i][j] = box;
                    }
                    alternati = true;
                }
            }
            for(int i=0; i<row; i++)
                for(int j=0; j<column; j++)
                    boardPanel.add(boxes[i][j]);
            repaint();
            revalidate();
        }
    }
}
