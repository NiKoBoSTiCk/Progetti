package it.niko;

import it.niko.scaleeserpenti.builder.Configuration;
import it.niko.scaleeserpenti.game.ScaleESerpentiGame;
import it.niko.scaleeserpenti.command.*;
import it.niko.scaleeserpenti.observer.*;
import it.niko.scaleeserpenti.builder.ConfigurationDialog;
import javax.swing.*;
import java.awt.*;

public class ScaleESerpentiApplication {

    public static void main(String[] args) {
        JFrame f = new JFrame();

        ScaleESerpentiGame game = new ScaleESerpentiGame();

        StateGameListener stateGameListener = new StateGameListener();
        game.addGameListener(stateGameListener);

        LoggerGameListener loggerGameListener = new LoggerGameListener();
        game.addGameListener(loggerGameListener);

        BoxGameListener boxGameListener = new BoxGameListener();
        game.addGameListener(boxGameListener);

        BoardGameListener boardGameListener = new BoardGameListener();
        game.addGameListener(boardGameListener);

        DeckGameListener deckGameListener = new DeckGameListener();
        game.addGameListener(deckGameListener);

        GameCommandHandler handler = GameCommandHandler.getINSTANCE();

        JMenuBar menu = new JMenuBar();
        JMenu configMenu = new JMenu("File");
        menu.setBorderPainted(true);
        menu.add(configMenu);

        JMenuItem create = new JMenuItem("Create Configuration");
        create.addActionListener(e -> {
            ConfigurationDialog gcd = new ConfigurationDialog(f, game);
            gcd.setVisible(true);
        });
        configMenu.add(create);

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(e -> {
            if(game.isConfigurationSet())
                handler.handle(new SaveCommand(f, game));
        });
        configMenu.add(save);

        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(e -> {
            if(game.isConfigurationSet())
                handler.handle(new LoadCommand(f, game));
        });
        configMenu.add(load);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        JButton manual = new JButton("Manual Advance");
        manual.addActionListener(e -> handler.handle(new ManualCommand(game)));
        buttons.add(manual);

        JButton auto = new JButton("Auto Advance");
        auto.addActionListener(e -> handler.handle(new AutoCommand(game)));
        buttons.add(auto);

        for(;;) {
            try {
                String value = JOptionPane.showInputDialog(f, "Number of Players", "Set Players", JOptionPane.QUESTION_MESSAGE);
                if(value == null) System.exit(0);
                int players = Integer.parseInt(value);
                game.configGame(new Configuration.ConfigurationBuilder(players, 100, 10, 10).build());
                break;
            } catch(Exception e) {
                JOptionPane.showMessageDialog(null, "Number of Players is not valid!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

        f.setLayout(new GridLayout(2,3, 5, 5));
        f.setJMenuBar(menu);
        f.add(stateGameListener);
        f.add(boardGameListener);
        f.add(boxGameListener);
        f.add(deckGameListener);
        f.add(buttons);
        f.add(loggerGameListener);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}
