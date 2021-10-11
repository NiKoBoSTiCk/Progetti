package it.niko.gui;

import it.niko.game.Configuration;
import it.niko.game.GameBoxes;
import it.niko.game.GameCards;
import it.niko.game.model.Game;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ConfigurationDialog extends JDialog {

    private record Snake(int head, int tail) {}
    private record Ladder(int base, int top) {}
    private record SpecialBox(int pos, GameBoxes type) {}

    private final Game game;
    private final JTextField numPlayersText, numBoxesText, columnText, rowText;
    private final JCheckBox singleDice, rollSingleDice, doubleSix, stopBoxes, rewardBoxes, drawCardBoxes, banCards;
    private final LinkedList<Snake> snakes;
    private final LinkedList<Ladder> ladders;
    private final LinkedList<SpecialBox> specialBoxes;
    private final LinkedList<GameCards> cards;

    public ConfigurationDialog(Frame parent, Game game) {
        super(parent, "Game Configuration", true);
        this.game = game;
        this.snakes = new LinkedList<>();
        this.ladders = new LinkedList<>();
        this.specialBoxes = new LinkedList<>();
        this.cards = new LinkedList<>();

        Point p = parent.getLocation();
        setLocation(p.x + 80,p.y + 80);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2));
        JLabel numPlayersLabel = new JLabel("Players");
        JLabel numBoxesLabel = new JLabel("Board Boxes");
        JLabel rowLabel = new JLabel("Board Rows");
        JLabel columnLabel = new JLabel("Board Columns");
        numPlayersText = new JTextField();
        numBoxesText = new JTextField();
        rowText = new JTextField();
        columnText = new JTextField();

        JButton snakeButton = new JButton("Add Snake");
        snakeButton.addActionListener(e -> {
            String headS = JOptionPane.showInputDialog("Head Position");
            String tailS = JOptionPane.showInputDialog("Tail Position");
            if(headS != null && tailS != null) {
                int head = Integer.parseInt(headS);
                int tail = Integer.parseInt(tailS);
                snakes.add(new Snake(head, tail));
            }
        });

        JButton ladderButton = new JButton("Add Ladder");
        ladderButton.addActionListener(e -> {
            String baseS = JOptionPane.showInputDialog("Base Position");
            String topS = JOptionPane.showInputDialog("Top Position");
            if(baseS != null && topS != null) {
                int base = Integer.parseInt(baseS);
                int top = Integer.parseInt(topS);
                ladders.add(new Ladder(base, top));
            }
        });

        JButton specialBoxButton = new JButton("Add Special Box");
        specialBoxButton.addActionListener(e -> {
            String posS = JOptionPane.showInputDialog("Box Position");
            if(posS != null) {
                int pos = Integer.parseInt(posS);
                GameBoxes[] values = { GameBoxes.Bench, GameBoxes.Inn, GameBoxes.Dice, GameBoxes.Spring, GameBoxes.DrawCard};
                GameBoxes type = (GameBoxes) JOptionPane.showInputDialog(null, "Type",
                        "", JOptionPane.QUESTION_MESSAGE, null, values, GameBoxes.Bench);
                specialBoxes.add(new SpecialBox(pos, type));
            }
        });

        JButton cardButton = new JButton("Add Card to Deck");
        cardButton.addActionListener(e -> {
            GameCards card = (GameCards) JOptionPane.showInputDialog(null, "Type",
                    "", JOptionPane.QUESTION_MESSAGE, null, GameCards.values(), GameCards.Bench);
            cards.add(card);
        });

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            setConfiguration();
            dispose();
        });

        JPanel variants = new JPanel();
        variants.setLayout(new GridLayout(3, 3));
        JLabel selectVariantsLabel = new JLabel("Variants");
        singleDice = new JCheckBox("Single Dice");
        rollSingleDice = new JCheckBox("Roll Single Dice");
        doubleSix = new JCheckBox("Double Six");
        stopBoxes = new JCheckBox("Stop Boxes");
        rewardBoxes = new JCheckBox("Reward Boxes");
        drawCardBoxes = new JCheckBox("Draw Card Boxes");
        banCards = new JCheckBox("Ban Cards");

        variants.add(singleDice);
        variants.add(rollSingleDice);
        variants.add(doubleSix);
        variants.add(stopBoxes);
        variants.add(rewardBoxes);
        variants.add(drawCardBoxes);
        variants.add(banCards);

        panel.add(numPlayersLabel);
        panel.add(numPlayersText);
        panel.add(numBoxesLabel);
        panel.add(numBoxesText);
        panel.add(rowLabel);
        panel.add(rowText);
        panel.add(columnLabel);
        panel.add(columnText);
        panel.add(selectVariantsLabel);
        panel.add(variants);
        panel.add(snakeButton);
        panel.add(ladderButton);
        panel.add(specialBoxButton);
        panel.add(cardButton);
        panel.add(okButton);
        add(panel);
        setSize(800, 600);
    }

    private void setConfiguration() {
        try {
            int numPlayers = Integer.parseInt(numPlayersText.getText());
            int numBoxes= Integer.parseInt(numBoxesText.getText());
            int row = Integer.parseInt(rowText.getText());
            int column = Integer.parseInt(columnText.getText());

            Configuration.ConfigurationBuilder builder = new Configuration.ConfigurationBuilder(numPlayers, numBoxes, row, column);
            builder.singleDice(singleDice.isSelected());
            builder.rollSingleDice(rollSingleDice.isSelected());
            builder.doubleSix(doubleSix.isSelected());
            builder.stopBoxes(stopBoxes.isSelected());
            builder.rewardBoxes(rewardBoxes.isSelected());
            builder.drawCardBoxes(drawCardBoxes.isSelected(), 40);
            builder.banCards(banCards.isSelected());

            for(Snake snake: snakes)
                builder.addSnake(snake.head(), snake.tail());

            for(Ladder ladder: ladders)
                builder.addLadder(ladder.base(), ladder.top());

            for(SpecialBox specialBox: specialBoxes)
                builder.addSpecialBox(specialBox.pos(), specialBox.type());

            for(GameCards card: cards)
                builder.addCard(card);

            game.configGame(builder.build());
            JOptionPane.showConfirmDialog(null, "Configuration Set!", "", JOptionPane.DEFAULT_OPTION);
        } catch(Exception e) {
           JOptionPane.showConfirmDialog(null, "Configuration not valid!", "ERROR", JOptionPane.DEFAULT_OPTION);
        }
    }
}

