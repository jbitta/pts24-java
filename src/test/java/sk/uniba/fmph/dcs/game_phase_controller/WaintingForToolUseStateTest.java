package sk.uniba.fmph.dcs.game_phase_controller;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.InterfaceToolUse;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class WaintingForToolUseStateTest {
    static class ToolUseMock implements InterfaceToolUse {

        private boolean useTool;
        private boolean canUseTools;
        private boolean finishUsingTools;

        ToolUseMock(final boolean useTool, final boolean canUseTools, final boolean finishUsingTools) {
            this.useTool = useTool;
            this.canUseTools = canUseTools;
            this.finishUsingTools = finishUsingTools;
        }

        @Override
        public boolean useTool(int idx) {
            return useTool;
        }

        @Override
        public boolean canUseTools() {
            return canUseTools;
        }

        @Override
        public boolean finishUsingTools() {
            return finishUsingTools;
        }
    }

    @Test
    public void useToolsTest() {
        Map<PlayerOrder, InterfaceToolUse> toolUseMap = new HashMap<>();
        PlayerOrder p1 = new PlayerOrder(1, 2);
        PlayerOrder p2 = new PlayerOrder(2, 2);
        toolUseMap.put(p1, new ToolUseMock(true, true, false));
        toolUseMap.put(p2, new ToolUseMock(false, false, true));
        WaitingForToolUseState wftus = new WaitingForToolUseState(toolUseMap);

        assertEquals(wftus.useTools(p1, 1), ActionResult.ACTION_DONE);
        assertEquals(wftus.useTools(p2, 1), ActionResult.FAILURE);
    }

    @Test
    public void noMoreToolsThisThrowTest() {
        Map<PlayerOrder, InterfaceToolUse> toolUseMap = new HashMap<>();
        PlayerOrder p1 = new PlayerOrder(1, 2);
        PlayerOrder p2 = new PlayerOrder(2, 2);
        toolUseMap.put(p1, new ToolUseMock(true, true, false));
        toolUseMap.put(p2, new ToolUseMock(false, false, true));
        WaitingForToolUseState wftus = new WaitingForToolUseState(toolUseMap);

        assertEquals(wftus.noMoreToolsThisThrow(p1), ActionResult.FAILURE);
        assertEquals(wftus.noMoreToolsThisThrow(p2), ActionResult.ACTION_DONE);
    }

    @Test
    public void tryToMakeAutomaticActionTest() {
        Map<PlayerOrder, InterfaceToolUse> toolUseMap = new HashMap<>();
        PlayerOrder p1 = new PlayerOrder(1, 2);
        PlayerOrder p2 = new PlayerOrder(2, 2);
        toolUseMap.put(p1, new ToolUseMock(false, false, false));
        toolUseMap.put(p2, new ToolUseMock(true, true, false));
        WaitingForToolUseState wftus = new WaitingForToolUseState(toolUseMap);

        assertEquals(wftus.tryToMakeAutomaticAction(p1), HasAction.NO_ACTION_POSSIBLE);
        assertEquals(wftus.tryToMakeAutomaticAction(p2), HasAction.WAITING_FOR_PLAYER_ACTION);
    }

}
