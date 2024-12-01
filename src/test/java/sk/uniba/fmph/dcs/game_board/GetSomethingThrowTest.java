package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import static org.junit.Assert.assertEquals;

public class GetSomethingThrowTest {

    @Test
    public void testCalculation() {
        Player p = new Player(null, null);
        CurrentThrow currentThrow = new CurrentThrow();
        GetSomethingThrow getThrow = new GetSomethingThrow(currentThrow);
        assertEquals(getThrow.performEffect(p, Effect.WOOD), ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE);

        assertEquals(getThrow.performEffect(p, Effect.FOOD), ActionResult.FAILURE);
    }

}
