package sk.uniba.fmph.dcs.game_phase_controller;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

public class GameEndStateTest {
    @Test
    public void tryToMakeAutomaticActionTest() {
        GameEndState ges = new GameEndState();
        assertEquals(ges.tryToMakeAutomaticAction(new PlayerOrder(1, 1)), HasAction.WAITING_FOR_PLAYER_ACTION);
    }
}
