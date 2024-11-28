package sk.uniba.fmph.dcs.game_board;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;
import java.util.Collection;
import java.util.OptionalInt;
import static org.junit.Assert.assertEquals;

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
        CivilisationCard c = new CivilisationCard(null, null);
        GetCard getCard = new GetCard(c);
        assertEquals(getCard.performEffect(p, Effect.WOOD), ActionResult.ACTION_DONE);
        assertEquals(getCard.performEffect(p, Effect.WOOD), ActionResult.FAILURE);
    }
}
