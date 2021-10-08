package it.niko;

import it.niko.game.Configuration;
import it.niko.game.GameBoxes;
import it.niko.game.GameCards;
import it.niko.game.ScaleESerpentiGame;
import it.niko.game.command.*;
import it.niko.gui.GameConfigurationDialog;
import it.niko.gui.GameLoggerPanel;
import it.niko.gui.GameStatePanel;
import javax.swing.*;
import java.awt.*;

public class ScaleESerpentiApplication {

    public static void main(String[] args) {
        JFrame f = new JFrame();

        ScaleESerpentiGame game = new ScaleESerpentiGame();

        GameCommandHandler handler = GameCommandHandler.getINSTANCE();

        JMenuBar menu = new JMenuBar();

        JMenu configMenu = new JMenu("file");

        JMenuItem create = new JMenuItem("create configuration");
        JMenuItem save = new JMenuItem("save");
        JMenuItem load = new JMenuItem("load");

        create.addActionListener(e -> {
            GameConfigurationDialog gcd = new GameConfigurationDialog(f, game);
            gcd.setVisible(true);
        });

        save.addActionListener(e -> {
            if(game.isConfigurationSet())
                handler.handle(new SaveCommand());
        });

        load.addActionListener(e -> {
            if(game.isConfigurationSet())
                handler.handle(new LoadCommand());
        });

        configMenu.add(create);
        configMenu.add(save);
        configMenu.add(load);
        menu.add(configMenu);

        Configuration c = new Configuration.ConfigurationBuilder(15, 100, 10, 10)
                .addSnake(98, 79)
                .addSnake(95, 75)
                .addSnake(93, 73)
                .addSnake(87, 24)
                .addSnake(64, 60)
                .addSnake(62, 19)
                .addSnake(54, 34)
                .addSnake(17, 7)
                .addLadder(80, 100)
                .addLadder(71, 91)
                .addLadder(28, 84)
                .addLadder(51, 67)
                .addLadder(21, 42)
                .addLadder(1, 38)
                .addLadder(9, 31)
                .addLadder(4, 14)
                .caselleSosta(true)
                .addSpecialBox(99, GameBoxes.inn)
                .addSpecialBox(2, GameBoxes.bench)
                .casellePremio(true)
                .addSpecialBox(90, GameBoxes.spring)
                .addSpecialBox(97, GameBoxes.dice)
                .doppioSei(true)
                .casellePesca(true, 10)
                .addSpecialBox(55, GameBoxes.drawCard)
                .addSpecialBox(3, GameBoxes.drawCard)
                .addSpecialBox(89, GameBoxes.drawCard)
                .carteDivieto(true)
                .addCard(GameCards.ban)
                .addCard(GameCards.spring)
                .addCard(GameCards.ban)
                .addCard(GameCards.inn)
                .addCard(GameCards.dice)
                .addCard(GameCards.ban)
                .addCard(GameCards.ban)
                .addCard(GameCards.bench)
                .addCard(GameCards.spring)
                .addCard(GameCards.dice)
                .build();
        game.configGame(c);

        GameStatePanel gsp = new GameStatePanel(c);
        game.addGameListener(gsp);

        GameLoggerPanel glp = new GameLoggerPanel();
        game.addGameListener(glp);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        JButton next = new JButton("MANUAL");
        next.addActionListener(e -> handler.handle(new ManualCommand(gsp, glp, game)));
        buttons.add(next);

        JButton finish = new JButton("AUTO");
        finish.addActionListener(e -> handler.handle(new AutoCommand(gsp, glp, game)));
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
