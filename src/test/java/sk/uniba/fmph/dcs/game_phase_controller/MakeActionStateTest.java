package sk.uniba.fmph.dcs.game_phase_controller;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class MakeActionStateTest {
    static class FigureLocationMock implements InterfaceFigureLocation {
        private ActionResult makeAction;
        private boolean skipAction;
        private HasAction tryToMakeAction;

        FigureLocationMock(final ActionResult makeAction, final boolean skipAction, final HasAction tryToMakeAction) {
            this.makeAction = makeAction;
            this.skipAction = skipAction;
            this.tryToMakeAction = tryToMakeAction;
        }

        @Override
        public boolean placeFigures(PlayerOrder player, int figureCount) {
            return false;
        }

        @Override
        public HasAction tryToPlaceFigures(PlayerOrder player, int count) {
            return null;
        }

        @Override
        public ActionResult makeAction(PlayerOrder player, Collection<Effect> inputResources,
                Collection<Effect> outputResources) {
            return makeAction;
        }

        @Override
        public boolean skipAction(PlayerOrder player) {
            return skipAction;
        }

        @Override
        public HasAction tryToMakeAction(PlayerOrder player) {
            return tryToMakeAction;
        }

        @Override
        public boolean newTurn() {
            return false;
        }
    }

    @Test
    public void makeActionTest() {
        Map<Location, InterfaceFigureLocation> places = new HashMap<>();
        places.put(Location.TOOL_MAKER,
                new FigureLocationMock(ActionResult.ACTION_DONE, true, HasAction.AUTOMATIC_ACTION_DONE));
        places.put(Location.HUT, new FigureLocationMock(ActionResult.FAILURE, true, HasAction.AUTOMATIC_ACTION_DONE));
        MakeActionState mas = new MakeActionState(places);

        assertEquals(mas.makeAction(new PlayerOrder(1, 2), Location.TOOL_MAKER, List.of(Effect.STONE),
                List.of(Effect.STONE)), ActionResult.ACTION_DONE);
        assertEquals(mas.makeAction(new PlayerOrder(2, 2), Location.HUT, List.of(Effect.STONE), List.of(Effect.STONE)),
                ActionResult.FAILURE);

    }

    @Test
    public void skipActionTest() {
        Map<Location, InterfaceFigureLocation> places = new HashMap<>();
        places.put(Location.TOOL_MAKER,
                new FigureLocationMock(ActionResult.ACTION_DONE, true, HasAction.AUTOMATIC_ACTION_DONE));
        places.put(Location.HUT,
                new FigureLocationMock(ActionResult.ACTION_DONE, false, HasAction.AUTOMATIC_ACTION_DONE));
        MakeActionState mas = new MakeActionState(places);

        assertEquals(mas.skipAction(new PlayerOrder(1, 2), Location.TOOL_MAKER), ActionResult.ACTION_DONE);
        assertEquals(mas.skipAction(new PlayerOrder(2, 2), Location.HUT), ActionResult.FAILURE);

    }

    @Test
    public void tryToMakeAutomaticActionTest() {

        Map<Location, InterfaceFigureLocation> places1 = new HashMap<>();
        Map<Location, InterfaceFigureLocation> places2 = new HashMap<>();
        places1.put(Location.HUT,
                new FigureLocationMock(ActionResult.ACTION_DONE, true, HasAction.WAITING_FOR_PLAYER_ACTION));
        places2.put(Location.FOREST,
                new FigureLocationMock(ActionResult.ACTION_DONE, true, HasAction.NO_ACTION_POSSIBLE));
        MakeActionState mas1 = new MakeActionState(places1);
        MakeActionState mas2 = new MakeActionState(places2);

        assertEquals(mas1.tryToMakeAutomaticAction(new PlayerOrder(2, 3)), HasAction.WAITING_FOR_PLAYER_ACTION);
        assertEquals(mas2.tryToMakeAutomaticAction(new PlayerOrder(3, 3)), HasAction.NO_ACTION_POSSIBLE);

    }
}
