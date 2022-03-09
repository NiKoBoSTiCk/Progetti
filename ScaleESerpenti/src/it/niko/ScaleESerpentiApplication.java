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
        LoggerGameListener loggerGameListener = new LoggerGameListener();
        BoxGameListener boxGameListener = new BoxGameListener();
        BoardGameListener boardGameListener = new BoardGameListener();
        DeckGameListener deckGameListener = new DeckGameListener();

        game.addGameListener(stateGameListener);
        game.addGameListener(loggerGameListener);
        game.addGameListener(boxGameListener);
        game.addGameListener(boardGameListener);
        game.addGameListener(deckGameListener);

        SaveCommand save = new SaveCommand(f, game);
        LoadCommand load = new LoadCommand(f, game);
        AutoCommand auto = new AutoCommand(game);
        ManualCommand manual = new ManualCommand(game);
        GameCommandHandler handler = GameCommandHandler.getINSTANCE();

        JMenuBar menu = new JMenuBar();
        JMenu configMenu = new JMenu("Settings");
        menu.setBorderPainted(true);
        menu.add(configMenu);

        JMenuItem create = new JMenuItem("Create Configuration");
        create.addActionListener(e -> {
            ConfigurationDialog gcd = new ConfigurationDialog(f, game);
            gcd.setVisible(true);
        });
        configMenu.add(create);

        JMenuItem saveMenu = new JMenuItem("Save");
        saveMenu.addActionListener(e -> handler.handle(save));
        configMenu.add(saveMenu);

        JMenuItem loadMenu = new JMenuItem("Load");
        loadMenu.addActionListener(e -> handler.handle(load));
        configMenu.add(loadMenu);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        JButton manualButton = new JButton("Manual Advance");
        manualButton.addActionListener(e -> handler.handle(manual));
        buttons.add(manualButton);

        JButton autoButton= new JButton("Auto Advance");
        autoButton.addActionListener(e -> handler.handle(auto));
        buttons.add(autoButton);

        for(;;) {
            try {
                String value = JOptionPane.showInputDialog(f, "Number of Players", "Set Players", JOptionPane.QUESTION_MESSAGE);
                if(value == null) System.exit(0);
                int players = Integer.parseInt(value);
                game.configGame(new Configuration.ConfigurationBuilder(players, 10, 10).build());
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
