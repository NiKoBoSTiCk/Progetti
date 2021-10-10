package it.niko.gui;

import it.niko.game.Configuration;
import it.niko.game.model.Game;
import javax.swing.*;
import java.awt.*;

public class ConfigurationDialog extends JDialog {

    private final Game game;
    private final JTextField numPlayersText, numBoxesText, columnText, rowText;
    private final JCheckBox singleDice, rollSingleDice, doubleSix, stopBoxes, rewardBoxes, drawCardBoxes, banCards;

    public ConfigurationDialog(Frame parent, Game game) {
        super(parent, "Game Configuration", true);
        this.game = game;

        Point p = parent.getLocation();
        setLocation(p.x + 80,p.y + 80);

        JPanel panel = new JPanel();
        JLabel numPlayersLabel = new JLabel("Players");
        JLabel numBoxesLabel = new JLabel("Board Boxes");
        JLabel rowLabel = new JLabel("Board row");
        JLabel columnLabel = new JLabel("Board column");
        numPlayersText = new JTextField();
        numBoxesText = new JTextField();
        rowText = new JTextField();
        columnText = new JTextField();

        //aggiungere scale, serpenti e carte al mazzo

        JPanel variants = new JPanel();
        JLabel selectVariantsLabel = new JLabel("Select Variants");
        singleDice = new JCheckBox("Single Dice");
        rollSingleDice = new JCheckBox("Roll Single Dice");
        doubleSix = new JCheckBox("Double Six");
        stopBoxes = new JCheckBox("Stop Boxes");
        rewardBoxes = new JCheckBox("Reward Boxes");
        drawCardBoxes = new JCheckBox("Draw Card Boxes");
        banCards = new JCheckBox("Ban Cards");

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            setConfiguration();
            dispose();
        });

        variants.setLayout(new GridLayout(7, 1));
        variants.add(singleDice);
        variants.add(rollSingleDice);
        variants.add(doubleSix);
        variants.add(stopBoxes);
        variants.add(rewardBoxes);
        variants.add(drawCardBoxes);
        variants.add(banCards);
        panel.setLayout(new GridLayout(6, 2));
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
        panel.add(okButton);
        add(panel);
        setSize(600, 600);
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
            game.configGame(builder.build());

        } catch(Exception e) {
            e.printStackTrace();
           // JOptionPane.showConfirmDialog(null, "Configuration not valid!", "ERROR", JOptionPane.DEFAULT_OPTION);
        }
    }
}

