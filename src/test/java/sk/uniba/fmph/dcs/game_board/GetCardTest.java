package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;
import java.util.Collection;
import java.util.OptionalInt;
import static org.junit.Assert.assertEquals;
import java.util.List;
import java.util.Optional;

public class GetCardTest {

    @Test
    public void testCalculation() {
        InterfacePlayerBoardGameBoard board = new InterfacePlayerBoardGameBoard() {
            @Override
            public void giveEffect(Collection<Effect> stuff) {

            }

            @Override
            public void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff) {

            }

            @Override
            public boolean takeResources(Collection<Effect> stuff) {
                return false;
            }

            @Override
            public boolean takeFigures(int count) {
                return false;
            }

            @Override
            public void giveFigures(int count) {

            }

            @Override
            public boolean hasFigures(int count) {
                return false;
            }

            @Override
            public boolean hasSufficientTools(int goal) {
                return false;
            }

            @Override
            public OptionalInt useTool(int idx) {
                return null;
            }

            @Override
            public void takePoints(int points) {

            }

            @Override
            public void givePoints(int points) {

            }
        };

        Player p = new Player(null, board);
        CivilizationCardDeck deck = new CivilizationCardDeck(List.of());

        GetCard getCard = new GetCard(deck);
        assertEquals(getCard.performEffect(p, Effect.WOOD), ActionResult.ACTION_DONE);

    }
}
