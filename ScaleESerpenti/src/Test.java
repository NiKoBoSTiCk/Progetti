import it.niko.scaleeserpenti.builder.Configuration;
import it.niko.scaleeserpenti.game.GameBoxes;
import it.niko.scaleeserpenti.game.GameCards;
import it.niko.scaleeserpenti.game.ScaleESerpentiGame;

import java.io.IOException;

/*
Test di salvataggio di una configurazione
 */
public class Test {
    public static void main(String[] args) throws IOException {
        ScaleESerpentiGame game = new ScaleESerpentiGame();
        Configuration c = new Configuration.ConfigurationBuilder(15, 10, 10)
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
                .stopBoxes(true)
                .addSpecialBox(99, GameBoxes.Inn)
                .addSpecialBox(2, GameBoxes.Bench)
                .rewardBoxes(true)
                .addSpecialBox(90, GameBoxes.Spring)
                .addSpecialBox(97, GameBoxes.Dice)
                .doubleSix(true)
                .drawCardBoxes(true)
                .addSpecialBox(55, GameBoxes.DrawCard)
                .addSpecialBox(3, GameBoxes.DrawCard)
                .addSpecialBox(89, GameBoxes.DrawCard)
                .banCards(true)
                .addCard(GameCards.Ban)
                .addCard(GameCards.Spring)
                .addCard(GameCards.Ban)
                .addCard(GameCards.Inn)
                .addCard(GameCards.Dice)
                .addCard(GameCards.Ban)
                .addCard(GameCards.Ban)
                .addCard(GameCards.Bench)
                .addCard(GameCards.Spring)
                .addCard(GameCards.Dice)
                .build();
        game.configGame(c);
        game.save("C:\\Users\\User\\Documents\\ScaleESerpentiSave\\complex");
    }
}
