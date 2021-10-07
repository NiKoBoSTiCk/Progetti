package it.niko;


import it.niko.game.*;

public class Test {

    public static void main(String[] args) {
        Configuration c = new Configuration.ConfigurationBuilder(8, 100, 10, 10)
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

        ScaleESerpentiGame game = new ScaleESerpentiGame(c);
        while(!game.isGameFinish()){
            game.nextRound();
            System.out.println(game.getRoundLog());
            System.out.println(game.getCurrentPlayerState());
        }
    }
}
