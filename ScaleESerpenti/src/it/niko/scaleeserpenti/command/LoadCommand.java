package it.niko.scaleeserpenti.command;

import it.niko.scaleeserpenti.game.Game;
import javax.swing.*;
import java.io.File;

public class LoadCommand implements Command {
    private final JFrame frame;
    private final Game game;

    public LoadCommand(JFrame frame, Game game) {
        this.frame = frame;
        this.game = game;
    }

    @Override
    public void execute() {
        JFileChooser fileChooser = new JFileChooser();
        try {
            if(fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().exists()) {
                File f = fileChooser.getSelectedFile();
                game.load(f.getAbsolutePath());
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Load failed!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
}
