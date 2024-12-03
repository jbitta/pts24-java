package sk.uniba.fmph.dcs.game_phase_controller;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PlaceFigureStateTest {
    static class FigureLocationMock implements InterfaceFigureLocation {

        @Override
        public boolean placeFigures(PlayerOrder player, int figureCount) {
            return figureCount == 1;
        }

        @Override
        public HasAction tryToPlaceFigures(PlayerOrder player, int count) {
            if (player.getOrder() == 1) {
                return HasAction.WAITING_FOR_PLAYER_ACTION;
            } else {
                return HasAction.NO_ACTION_POSSIBLE;
            }
        }

        @Override
        public ActionResult makeAction(PlayerOrder player, Collection<Effect> inputResources,
                Collection<Effect> outputResources) {
            return null;
        }

        @Override
        public boolean skipAction(PlayerOrder player) {
            return false;
        }

        @Override
        public HasAction tryToMakeAction(PlayerOrder player) {
            return null;
        }

        @Override
        public boolean newTurn() {
            return false;
        }
    }

    @Test
    public void placeFiguresTest() {
        Map<Location, InterfaceFigureLocation> places = new HashMap<>();
        places.put(Location.TOOL_MAKER, new FigureLocationMock());
        PlaceFigureState pfs = new PlaceFigureState(places);
        assertEquals(pfs.placeFigures(new PlayerOrder(1, 1), Location.TOOL_MAKER, 1), ActionResult.ACTION_DONE);
        assertEquals(pfs.placeFigures(new PlayerOrder(1, 1), Location.TOOL_MAKER, 2), ActionResult.FAILURE);

    }

    @Test
    public void tryToMakeAutomaticActionTest() {
        Map<Location, InterfaceFigureLocation> places = new HashMap<>();
        places.put(Location.TOOL_MAKER, new FigureLocationMock());
        PlaceFigureState pfs = new PlaceFigureState(places);
        assertEquals(pfs.tryToMakeAutomaticAction(new PlayerOrder(1, 2)), HasAction.WAITING_FOR_PLAYER_ACTION);
        assertEquals(pfs.tryToMakeAutomaticAction(new PlayerOrder(2, 2)), HasAction.NO_ACTION_POSSIBLE);

    }

}
