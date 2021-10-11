package it.niko.game.command;

import it.niko.game.model.Game;
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
    public boolean execute() {
        JFileChooser fileChooser = new JFileChooser();
        try {
            if(fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                game.save(f.getAbsolutePath());
                return true;
            }
        } catch(Exception e) {
            JOptionPane.showConfirmDialog(null, "Game not saved!", "ERROR", JOptionPane.DEFAULT_OPTION);
        }
        return false;
    }
}
