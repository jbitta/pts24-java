package sk.uniba.fmph.dcs.game_phase_controller;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

import static org.junit.Assert.*;

class MockMultiClass implements InterfaceToolUse, InterfaceFeedTribe, InterfaceFigureLocation, InterfaceTakeReward {
    private final Iterator<HasAction> hasActions;
    private final Iterator<ActionResult> actionResults;
    private final Iterator<Boolean> booleans;

    public MockMultiClass(List<HasAction> hasActions, List<ActionResult> actionResults, List<Boolean> booleans) {
        this.actionResults = actionResults.iterator();
        this.hasActions = hasActions.iterator();
        this.booleans = booleans.iterator();
    }

    @Override
    public boolean useTool(int idx) {
        return booleans.next();
    }

    @Override
    public boolean canUseTools() {
        return booleans.next();
    }

    @Override
    public boolean finishUsingTools() {
        return booleans.next();
    }

    @Override
    public boolean feedTribeIfEnoughFood() {
        return booleans.next();
    }

    @Override
    public boolean feedTribe(Collection<Effect> resources) {
        return booleans.next();
    }

    @Override
    public boolean doNotFeedThisTurn() {
        return booleans.next();
    }

    @Override
    public boolean isTribeFed() {
        return booleans.next();
    }

    @Override
    public boolean placeFigures(PlayerOrder player, int figureCount) {
        return booleans.next();
    }

    @Override
    public HasAction tryToPlaceFigures(PlayerOrder player, int count) {
        return hasActions.next();
    }

    @Override
    public ActionResult makeAction(PlayerOrder player, Collection<Effect> inputResources,
            Collection<Effect> outputResources) {
        return actionResults.next();
    }

    @Override
    public boolean skipAction(PlayerOrder player) {
        return booleans.next();
    }

    @Override
    public HasAction tryToMakeAction(PlayerOrder player) {
        return hasActions.next();
    }

    @Override
    public boolean newTurn() {
        return booleans.next();
    }

    @Override
    public boolean takeReward(PlayerOrder player, Effect reward) {
        return booleans.next();
    }

    @Override
    public HasAction tryMakeAction(PlayerOrder player) {
        return hasActions.next();
    }
}

class MockNewTurn implements InterfaceNewTurn {
    @Override
    public void newTurn() {
    }
}

public class GamePhaseControllerIntegrationTest {
    private final PlayerOrder player1 = new PlayerOrder(0, 3);
    private final PlayerOrder player2 = new PlayerOrder(1, 3);
    private final PlayerOrder player3 = new PlayerOrder(2, 3);
    private final ArrayList<PlayerOrder> playerOrders = new ArrayList<>(List.of(player1, player2, player3));
    private static final List<HasAction> hasActions = List.of(HasAction.WAITING_FOR_PLAYER_ACTION,
            HasAction.NO_ACTION_POSSIBLE, HasAction.WAITING_FOR_PLAYER_ACTION, HasAction.NO_ACTION_POSSIBLE,
            HasAction.NO_ACTION_POSSIBLE, HasAction.NO_ACTION_POSSIBLE, HasAction.NO_ACTION_POSSIBLE,
            HasAction.NO_ACTION_POSSIBLE, HasAction.NO_ACTION_POSSIBLE, HasAction.WAITING_FOR_PLAYER_ACTION,
            HasAction.NO_ACTION_POSSIBLE, HasAction.NO_ACTION_POSSIBLE, HasAction.WAITING_FOR_PLAYER_ACTION,
            HasAction.NO_ACTION_POSSIBLE, HasAction.NO_ACTION_POSSIBLE, HasAction.NO_ACTION_POSSIBLE,
            HasAction.WAITING_FOR_PLAYER_ACTION, HasAction.WAITING_FOR_PLAYER_ACTION,
            HasAction.WAITING_FOR_PLAYER_ACTION, HasAction.WAITING_FOR_PLAYER_ACTION, HasAction.NO_ACTION_POSSIBLE,
            HasAction.NO_ACTION_POSSIBLE, HasAction.NO_ACTION_POSSIBLE, HasAction.NO_ACTION_POSSIBLE,
            HasAction.NO_ACTION_POSSIBLE, HasAction.NO_ACTION_POSSIBLE, HasAction.NO_ACTION_POSSIBLE);
    private static final List<ActionResult> actionResults = List.of(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, ActionResult.ACTION_DONE_ALL_PLAYERS_TAKE_A_REWARD);
    private static final List<Boolean> booleans = List.of(true, true, false, true, true, true, true, true, false, true,
            true, true, true, false, true, false, false, true, false, false, true, true, true, true, true, false);
    private final MockMultiClass mock = new MockMultiClass(hasActions, actionResults, booleans);
    private final MockNewTurn mockNewTurn = new MockNewTurn();
    private InterfaceGamePhaseController gamePhaseController;

    @Before
    public void setUp() {
        Map<PlayerOrder, InterfaceFeedTribe> feedTribeMap = new HashMap<>();
        Map<PlayerOrder, InterfaceNewTurn> newTurnMap = new HashMap<>();
        for (PlayerOrder p : playerOrders) {
            feedTribeMap.put(p, mock);
            newTurnMap.put(p, mockNewTurn);
        }
        Map<Location, InterfaceFigureLocation> locations = Map.of(Location.FOREST, mock, Location.CIVILISATION_CARD1,
                mock);
        gamePhaseController = GamePhaseControllerFactory.createGamePhaseController(locations, feedTribeMap, newTurnMap,
                player1, mock, mock);
    }

    @Test
    public void oneRoundTest() {
        assertTrue(gamePhaseController.placeFigures(player1, Location.FOREST, 4));
        assertFalse(gamePhaseController.placeFigures(player1, Location.FOREST, 1));
        assertFalse(gamePhaseController.placeFigures(player3, Location.FOREST, 1));
        assertTrue(gamePhaseController.placeFigures(player2, Location.FOREST, 3));
        assertFalse(gamePhaseController.skipAction(player3, Location.FOREST));
        assertFalse(gamePhaseController.placeFigures(player3, Location.FOREST, 1));
        assertTrue(gamePhaseController.placeFigures(player3, Location.CIVILISATION_CARD1, 1));
        assertTrue(gamePhaseController.makeAction(player1, Location.FOREST, Collections.emptyList(),
                Collections.emptyList()));
        assertTrue(gamePhaseController.useTools(player1, 2));
        assertTrue(gamePhaseController.noMoreToolsThisThrow(player1));
        assertTrue(gamePhaseController.makeAction(player2, Location.FOREST, Collections.emptyList(),
                Collections.emptyList()));
        assertTrue(gamePhaseController.makeAction(player3, Location.CIVILISATION_CARD1, Collections.emptyList(),
                Collections.emptyList()));
        assertTrue(gamePhaseController.makeAllPlayersTakeARewardChoice(player3, Effect.WOOD));
        assertTrue(gamePhaseController.makeAllPlayersTakeARewardChoice(player1, Effect.WOOD));
        assertTrue(gamePhaseController.makeAllPlayersTakeARewardChoice(player2, Effect.STONE));
        assertTrue(gamePhaseController.feedTribe(player2, Collections.emptyList()));
        assertTrue(gamePhaseController.doNotFeedThisTurn(player3));
        assertEquals(new JSONObject(gamePhaseController.state()).get("game phase"), GamePhase.GAME_END.toString());
    }
}
