package sk.uniba.fmph.dcs.game_phase_controller;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

import sk.uniba.fmph.dcs.stone_age.PlayerOrder;
import sk.uniba.fmph.dcs.stone_age.Location;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.Effect;

import static org.junit.Assert.*;

class StateMock implements InterfaceGamePhaseState {
    List<ActionResult> expectedActionResults;
    List<HasAction> expectedHasAction;

    public StateMock() {
        this.expectedActionResults = new ArrayList<>();
        this.expectedHasAction = new ArrayList<>();
    }

    @Override
    public ActionResult placeFigures(PlayerOrder player, Location location, int figuresCount) {
        assert !expectedActionResults.isEmpty();
        return expectedActionResults.remove(0);
    }

    @Override
    public ActionResult makeAction(PlayerOrder player, Location location, Collection<Effect> inputResources,
            Collection<Effect> outputResources) {
        assert !expectedActionResults.isEmpty();
        return expectedActionResults.remove(0);
    }

    @Override
    public ActionResult skipAction(PlayerOrder player, Location location) {
        assert !expectedActionResults.isEmpty();
        return expectedActionResults.remove(0);
    }

    @Override
    public ActionResult useTools(PlayerOrder player, int toolIndex) {
        assert !expectedActionResults.isEmpty();
        return expectedActionResults.remove(0);
    }

    @Override
    public ActionResult noMoreToolsThisThrow(PlayerOrder player) {
        assert !expectedActionResults.isEmpty();
        return expectedActionResults.remove(0);
    }

    @Override
    public ActionResult feedTribe(PlayerOrder player, Collection<Effect> resources) {
        assert !expectedActionResults.isEmpty();
        return expectedActionResults.remove(0);
    }

    @Override
    public ActionResult doNotFeedThisTurn(PlayerOrder player) {
        assert !expectedActionResults.isEmpty();
        return expectedActionResults.remove(0);
    }

    @Override
    public ActionResult makeAllPlayersTakeARewardChoice(PlayerOrder player, Effect reward) {
        assert !expectedActionResults.isEmpty();
        return expectedActionResults.remove(0);
    }

    @Override
    public HasAction tryToMakeAutomaticAction(PlayerOrder player) {
        assert !expectedHasAction.isEmpty();
        return expectedHasAction.remove(0);
    }
}

public class GamePhaseControllerTest {

    private StateMock placeFiguresState;
    private StateMock makeActionState;
    private StateMock feedTribeState;
    private StateMock newRoundState;
    private StateMock waitingForToolUseState;
    private StateMock allPlayersTakeARewardState;
    private StateMock gameEndState;
    private GamePhaseController controller;

    @Before
    public void setUp() {
        placeFiguresState = new StateMock();
        makeActionState = new StateMock();
        feedTribeState = new StateMock();
        newRoundState = new StateMock();
        waitingForToolUseState = new StateMock();
        allPlayersTakeARewardState = new StateMock();
        gameEndState = new StateMock();

        Map<GamePhase, InterfaceGamePhaseState> dispatchers = Map.of(GamePhase.PLACE_FIGURES, placeFiguresState,
                GamePhase.MAKE_ACTION, makeActionState, GamePhase.FEED_TRIBE, feedTribeState, GamePhase.NEW_ROUND,
                newRoundState, GamePhase.WAITING_FOR_TOOL_USE, waitingForToolUseState,
                GamePhase.ALL_PLAYERS_TAKE_A_REWARD, allPlayersTakeARewardState, GamePhase.GAME_END, gameEndState);

        controller = new GamePhaseController(dispatchers, new PlayerOrder(0, 2));
    }

    private void checkStateString(String expectedString) {
        JSONObject obj = new JSONObject(controller.state());
        String stateString = String.format("%s,%s/%s/%s", obj.getString("game phase"),
                obj.getString("round starting player"), obj.getString("current_player"),
                obj.getString("player taking a reward"));
        assertEquals(expectedString, stateString);
    }

    private void mockSetup(String description) {
        for (String part : description.split(" ")) {
            StateMock mock = switch (part.charAt(0)) {
            case 'p' -> placeFiguresState;
            case 'm' -> makeActionState;
            case 'f' -> feedTribeState;
            case 'n' -> newRoundState;
            case 'g' -> gameEndState;
            case 'w' -> waitingForToolUseState;
            case 'a' -> allPlayersTakeARewardState;
            default -> throw new IllegalStateException("Unexpected value: " + part.charAt(0));
            };

            for (char action : part.substring(1).toCharArray()) {
                switch (action) {
                case 'A' -> mock.expectedHasAction.add(HasAction.AUTOMATIC_ACTION_DONE);
                case 'N' -> mock.expectedHasAction.add(HasAction.NO_ACTION_POSSIBLE);
                case 'W' -> mock.expectedHasAction.add(HasAction.WAITING_FOR_PLAYER_ACTION);
                case 'F' -> mock.expectedActionResults.add(ActionResult.FAILURE);
                case 'D' -> mock.expectedActionResults.add(ActionResult.ACTION_DONE);
                case 'R' -> mock.expectedActionResults.add(ActionResult.ACTION_DONE_ALL_PLAYERS_TAKE_A_REWARD);
                case 'T' -> mock.expectedActionResults.add(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE);
                default -> throw new IllegalStateException("Unexpected value: " + action);
                }
            }
        }
    }

    private boolean placeFigures(int idx1, int idx2) {
        return controller.placeFigures(new PlayerOrder(idx1, idx2), Location.BUILDING_TILE1, 1);
    }

    private boolean makeAction(int idx1, int idx2) {
        return controller.makeAction(new PlayerOrder(idx1, idx2), Location.BUILDING_TILE1, List.of(), List.of());
    }

    private boolean feedTribe(int idx1, int idx2) {
        return controller.feedTribe(new PlayerOrder(idx1, idx2), List.of());
    }

    private boolean useTools(int idx1, int idx2) {
        return controller.useTools(new PlayerOrder(idx1, idx2), 1);
    }

    private boolean noMoreToolsThisThrow(int idx1, int idx2) {
        return controller.noMoreToolsThisThrow(new PlayerOrder(idx1, idx2));
    }

    private boolean makeAllPlayersTakeARewardChoice(int idx1, int idx2) {
        return controller.makeAllPlayersTakeARewardChoice(new PlayerOrder(idx1, idx2), Effect.WOOD);
    }

    private boolean placeFigures(int idx1) {
        return placeFigures(idx1, 2);
    }

    private boolean makeAction(int idx1) {
        return makeAction(idx1, 2);
    }

    private boolean feedTribe(int idx1) {
        return feedTribe(idx1, 2);
    }

    private boolean useTools(int idx1) {
        return useTools(idx1, 2);
    }

    private boolean noMoreToolsThisThrow(int idx1) {
        return noMoreToolsThisThrow(idx1, 2);
    }

    private boolean makeAllPlayersTakeARewardChoice(int idx1) {
        return makeAllPlayersTakeARewardChoice(idx1, 2);
    }

    @Test
    public void testStartingState() {
        checkStateString("PLACE_FIGURES,0/0/None");
    }

    @Test
    public void testIncorrectPlayerTriedToTakeTurn() {
        assertFalse(placeFigures(1));
        checkStateString("PLACE_FIGURES,0/0/None");
    }

    @Test(expected = AssertionError.class)
    public void testIncorrectPlayerOrderObjectFailure() {
        placeFigures(0, 3);
    }

    @Test
    public void testPlayersSwappingPlacingFigures() {
        mockSetup("pDW");
        assertTrue(placeFigures(0));
        checkStateString("PLACE_FIGURES,0/1/None");

        mockSetup("pDNW");
        assertTrue(placeFigures(1));
        checkStateString("PLACE_FIGURES,0/1/None");

        mockSetup("pDNN mW");
        assertTrue(placeFigures(1));
        checkStateString("MAKE_ACTION,0/0/None");
    }

    @Test
    public void testCorrectPlayerStartsMakingAction() {
        mockSetup("pDW");
        assertTrue(placeFigures(0));
        mockSetup("pDNN mW");
        assertTrue(placeFigures(1));
        checkStateString("MAKE_ACTION,0/0/None");
    }

    @Test
    public void testPlayersSwappingMakingAction() {
        mockSetup("pDNN mW");
        assertTrue(placeFigures(0));
        checkStateString("MAKE_ACTION,0/0/None");

        mockSetup("mDW");
        assertTrue(makeAction(0));
        checkStateString("MAKE_ACTION,0/0/None");

        mockSetup("mF");
        assertFalse(makeAction(0));
        checkStateString("MAKE_ACTION,0/0/None");

        mockSetup("mDNW");
        assertTrue(makeAction(0));
        checkStateString("MAKE_ACTION,0/1/None");

        mockSetup("mDW");
        assertTrue(makeAction(1));
        checkStateString("MAKE_ACTION,0/1/None");

        mockSetup("mDNN fW");
        assertTrue(makeAction(1));
        checkStateString("FEED_TRIBE,0/0/None");
    }

    @Test
    public void testFeedTribeOnAndOutOfOrder() {
        mockSetup("pDNN mNN fW");
        assertTrue(placeFigures(0));
        checkStateString("FEED_TRIBE,0/0/None");

        mockSetup("fDW");
        assertTrue(feedTribe(1));
        checkStateString("FEED_TRIBE,0/1/None");

        mockSetup("fDNAW");
        assertTrue(feedTribe(1));
        checkStateString("FEED_TRIBE,0/0/None");
    }

    @Test
    public void testNextTurn() {
        mockSetup("pDNN mNN fNN nA pW");
        assertTrue(placeFigures(0));
        checkStateString("PLACE_FIGURES,1/1/None");
    }

    @Test
    public void testGameEnd() {
        mockSetup("pDNN mNN fNN nN gW");
        assertTrue(placeFigures(0));
        checkStateString("GAME_END,0/0/None");
    }

    @Test
    public void testToolUseForcedStop() {
        mockSetup("pDNN mW");
        assertTrue(placeFigures(0));

        mockSetup("mT wW"); // make action - WAITING_FOR_TOOL_USE
        assertTrue(makeAction(0));
        checkStateString("WAITING_FOR_TOOL_USE,0/0/None");

        mockSetup("wF");
        assertFalse(useTools(0));
        checkStateString("WAITING_FOR_TOOL_USE,0/0/None");

        mockSetup("wDW");
        assertTrue(useTools(0));
        checkStateString("WAITING_FOR_TOOL_USE,0/0/None");

        mockSetup("wDN mW");
        assertTrue(useTools(0));
        checkStateString("MAKE_ACTION,0/0/None");
    }

    @Test
    public void testToolUseDecidedToStop() {
        mockSetup("pDNN mNW");
        assertTrue(placeFigures(0));
        checkStateString("MAKE_ACTION,0/1/None");

        mockSetup("mT wW");
        assertTrue(makeAction(1));
        checkStateString("WAITING_FOR_TOOL_USE,0/1/None");

        mockSetup("wDW");
        assertTrue(useTools(1));
        checkStateString("WAITING_FOR_TOOL_USE,0/1/None");

        mockSetup("wD mW"); // done no more tools
        assertTrue(noMoreToolsThisThrow(1));
        checkStateString("MAKE_ACTION,0/1/None");
    }

    @Test
    public void testAllPlayersTakeAReward() {
        mockSetup("pDNN mNW");
        assertTrue(placeFigures(0));
        checkStateString("MAKE_ACTION,0/1/None");

        mockSetup("mR aW");
        assertTrue(makeAction(1));
        checkStateString("ALL_PLAYERS_TAKE_A_REWARD,0/1/1");

        mockSetup("aF");
        assertFalse(makeAllPlayersTakeARewardChoice(1));
        checkStateString("ALL_PLAYERS_TAKE_A_REWARD,0/1/1");

        assertFalse(makeAllPlayersTakeARewardChoice(0));
        checkStateString("ALL_PLAYERS_TAKE_A_REWARD,0/1/1");

        mockSetup("aDW");
        assertTrue(makeAllPlayersTakeARewardChoice(1));
        checkStateString("ALL_PLAYERS_TAKE_A_REWARD,0/1/0");

        mockSetup("aDW");
        assertTrue(makeAllPlayersTakeARewardChoice(0));
        checkStateString("ALL_PLAYERS_TAKE_A_REWARD,0/1/1");

        mockSetup("aDAW");
        assertTrue(makeAllPlayersTakeARewardChoice(1));
        checkStateString("ALL_PLAYERS_TAKE_A_REWARD,0/1/1");

        mockSetup("aDN mW");
        assertTrue(makeAllPlayersTakeARewardChoice(1));
        checkStateString("MAKE_ACTION,0/1/None");
    }

    @Test
    public void test_no_tools_last_action() {
        mockSetup("pDNN mW");
        assertTrue(placeFigures(0));
        checkStateString("MAKE_ACTION,0/0/None");

        mockSetup("mT wN mNN fW");
        assertTrue(makeAction(0));
        checkStateString("FEED_TRIBE,0/0/None");
    }

    @Test
    public void test_all_players_take_a_reward_last_action() {
        mockSetup("pDNN mW");
        assertTrue(placeFigures(0));
        checkStateString("MAKE_ACTION,0/0/None");

        mockSetup("mR aN mNN fW");
        assertTrue(makeAction(0));
        checkStateString("FEED_TRIBE,0/0/None");
    }
}
