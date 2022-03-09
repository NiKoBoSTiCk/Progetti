package it.niko.scaleeserpenti.command;

import it.niko.scaleeserpenti.game.Game;
import javax.swing.*;
import java.io.File;

public class SaveCommand implements Command {

    private final JFrame frame;
    private final Game game;

    public SaveCommand(JFrame frame, Game game) {
        this.frame = frame;
        this.game = game;
    }

    @Override
    public void execute() {
        JFileChooser fileChooser = new JFileChooser();
        try {
            if(fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                game.save(f.getAbsolutePath());
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Save failed!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
}
