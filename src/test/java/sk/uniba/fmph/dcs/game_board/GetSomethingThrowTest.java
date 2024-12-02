package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;

import java.util.Collection;
import java.util.OptionalInt;

import static org.junit.Assert.assertEquals;

public class GetSomethingThrowTest {

    InterfacePlayerBoardGameBoard playerBoardGameBoard = new InterfacePlayerBoardGameBoard() {
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
            return true;
        }

        @Override
        public OptionalInt useTool(int idx) {
            return OptionalInt.of(1);
        }

        @Override
        public void takePoints(int points) {

        }

        @Override
        public void givePoints(int points) {

        }
    };

    @Test
    public void testCalculation() {
        Player p = new Player(null, playerBoardGameBoard);
        CurrentThrow currentThrow = new CurrentThrow(new Throw(1));
        GetSomethingThrow getThrow = new GetSomethingThrow(currentThrow, Effect.WOOD);
        assertEquals(getThrow.performEffect(p, Effect.WOOD), ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE);
        getThrow = new GetSomethingThrow(currentThrow, Effect.FOOD);
        assertEquals(getThrow.performEffect(p, Effect.FOOD), ActionResult.FAILURE);
    }
}
