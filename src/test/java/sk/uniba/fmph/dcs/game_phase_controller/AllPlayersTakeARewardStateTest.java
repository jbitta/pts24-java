package sk.uniba.fmph.dcs.game_phase_controller;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import static org.junit.Assert.assertEquals;

public class AllPlayersTakeARewardStateTest {
    static class TakeRewardMock implements InterfaceTakeReward {
        private boolean takeReward;
        private HasAction tryMakeAction;

        TakeRewardMock(boolean takeReward, HasAction tryMakeAction) {
            this.takeReward = takeReward;
            this.tryMakeAction = tryMakeAction;
        }

        @Override
        public boolean takeReward(PlayerOrder player, Effect reward) {
            return takeReward;
        }

        @Override
        public HasAction tryMakeAction(PlayerOrder player) {
            return tryMakeAction;
        }
    }

    @Test
    public void makeAllPlayersTakeARewardChoiceTest() {
        AllPlayersTakeARewardState aptars = new AllPlayersTakeARewardState(
                new TakeRewardMock(true, HasAction.AUTOMATIC_ACTION_DONE));
        assertEquals(aptars.makeAllPlayersTakeARewardChoice(new PlayerOrder(1, 1), Effect.FOOD),
                ActionResult.ACTION_DONE);
        AllPlayersTakeARewardState aptars2 = new AllPlayersTakeARewardState(
                new TakeRewardMock(false, HasAction.AUTOMATIC_ACTION_DONE));
        assertEquals(aptars2.makeAllPlayersTakeARewardChoice(new PlayerOrder(1, 1), Effect.FOOD), ActionResult.FAILURE);
    }

    @Test
    public void tryToMakeAutomaticActionTest() {
        AllPlayersTakeARewardState aptars = new AllPlayersTakeARewardState(
                new TakeRewardMock(true, HasAction.AUTOMATIC_ACTION_DONE));
        assertEquals(aptars.tryToMakeAutomaticAction(new PlayerOrder(1, 1)), HasAction.AUTOMATIC_ACTION_DONE);
        AllPlayersTakeARewardState aptars2 = new AllPlayersTakeARewardState(
                new TakeRewardMock(true, HasAction.WAITING_FOR_PLAYER_ACTION));
        assertEquals(aptars2.tryToMakeAutomaticAction(new PlayerOrder(1, 1)), HasAction.WAITING_FOR_PLAYER_ACTION);
        AllPlayersTakeARewardState aptars3 = new AllPlayersTakeARewardState(
                new TakeRewardMock(true, HasAction.NO_ACTION_POSSIBLE));
        assertEquals(aptars3.tryToMakeAutomaticAction(new PlayerOrder(1, 1)), HasAction.NO_ACTION_POSSIBLE);
    }
}
