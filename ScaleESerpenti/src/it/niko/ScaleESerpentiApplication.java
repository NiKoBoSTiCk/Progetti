package it.niko;

import it.niko.game.Configuration;
import it.niko.game.ScaleESerpentiGame;
import it.niko.game.command.*;
import it.niko.gui.ConfigurationDialog;
import it.niko.gui.LoggerGameListener;
import it.niko.gui.StateGameListener;
import javax.swing.*;
import java.awt.*;

public class ScaleESerpentiApplication {

    public static void main(String[] args) {
        JFrame f = new JFrame();

        ScaleESerpentiGame game = new ScaleESerpentiGame();

        StateGameListener gsp = new StateGameListener();
        game.addGameListener(gsp);

        LoggerGameListener glp = new LoggerGameListener();
        game.addGameListener(glp);

        GameCommandHandler handler = GameCommandHandler.getINSTANCE();

        JMenuBar menu = new JMenuBar();
        JMenu configMenu = new JMenu("file");
        JMenuItem create = new JMenuItem("create configuration");
        JMenuItem save = new JMenuItem("save");
        JMenuItem load = new JMenuItem("load");

        create.addActionListener(e -> {
            ConfigurationDialog gcd = new ConfigurationDialog(f, game);
            gcd.setVisible(true);
        });

        save.addActionListener(e -> {
            if(game.isConfigurationSet())
                handler.handle(new SaveCommand(f, game));
        });

        load.addActionListener(e -> {
            if(game.isConfigurationSet())
                handler.handle(new LoadCommand(f, game));
        });

        configMenu.add(create);
        configMenu.add(save);
        configMenu.add(load);
        menu.add(configMenu);

        if(!game.isConfigurationSet()) {
            int players = Integer.parseInt(JOptionPane.showInputDialog("Giocatori"));
            game.configGame(new Configuration.ConfigurationBuilder(players, 100, 10, 10).build());
        }

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        JButton next = new JButton("MANUAL");
        next.addActionListener(e -> handler.handle(new ManualCommand(game)));
        buttons.add(next);

        JButton finish = new JButton("AUTO");
        finish.addActionListener(e -> handler.handle(new AutoCommand(game)));
        buttons.add(finish);

        f.setLayout(new GridLayout(1,3));
        f.setJMenuBar(menu);
        f.add(gsp);
        f.add(glp);
        f.add(buttons);
        f.setSize(800, 800);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}
