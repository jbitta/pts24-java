package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.OptionalInt;
import static org.junit.Assert.assertEquals;

public class AllPlayersTakeRewardTest {

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
        RewardMenu menu = new RewardMenu(new ArrayList<>());
        Throw t = new Throw(-1);

        AllPlayersTakeReward a = new AllPlayersTakeReward(menu, t);

        PlayerOrder playerOrder = new PlayerOrder(0, 0);
        Player p = new Player(playerOrder, board);
        assertEquals(a.performEffect(p, null), ActionResult.ACTION_DONE_ALL_PLAYERS_TAKE_A_REWARD);

    }
}
