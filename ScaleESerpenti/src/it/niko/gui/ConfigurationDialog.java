package it.niko.gui;

import it.niko.game.model.Game;
import javax.swing.*;
import java.awt.*;

public class ConfigurationDialog extends JDialog {

    public ConfigurationDialog(Frame parent, Game game) {
        super(parent, "Game Configuration", true);

        Point loc = parent.getLocation();
        setLocation(loc.x+80,loc.y+80);

    }
}

