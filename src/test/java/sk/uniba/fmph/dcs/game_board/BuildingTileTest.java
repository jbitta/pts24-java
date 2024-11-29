package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

import static org.junit.Assert.*;

public class BuildingTileTest {

    InterfacePlayerBoardGameBoard interfacePlayerBoardGameBoard = new InterfacePlayerBoardGameBoard() {

        @Override
        public void giveEffect(Collection<Effect> stuff) {
        }

        @Override
        public void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff) {
        }

        @Override
        public boolean takeResources(Collection<Effect> stuff) {
            return true;
        }

        @Override
        public boolean takeFigures(int count) {
            return true;
        }

        @Override
        public void giveFigures(int count) {
        }

        @Override
        public boolean hasFigures(int count) {
            return true;
        }

        @Override
        public boolean hasSufficientTools(int goal) {
            return false;
        }

        @Override
        public OptionalInt useTool(int idx) {
            return OptionalInt.empty();
        }

        @Override
        public void takePoints(int points) {
        }

        @Override
        public void givePoints(int points) {
        }
    };
    Player player1 = new Player(new PlayerOrder(1, 2), interfacePlayerBoardGameBoard);
    Player player2 = new Player(new PlayerOrder(2, 2), interfacePlayerBoardGameBoard);
    ArrayList<Building> buildingCards = new ArrayList<>();
    ArrayList<PlayerOrder> playerOrder1 = new ArrayList<>(List.of(player1.playerOrder()));
    ArrayList<PlayerOrder> playerOrder2 = new ArrayList<>(List.of(player2.playerOrder()));
    ArrayList<PlayerOrder> emptyOrder = new ArrayList<>();
    SimpleBuilding simpleBuilding = new SimpleBuilding(List.of(Effect.STONE, Effect.GOLD, Effect.WOOD));
    VariableBuilding variableBuilding = new VariableBuilding(1, 1);

    public ArrayList<Building> setBuildingCards() {
        ArrayList<Building> a = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            a.add(new ArbitraryBuilding(1));
        }

        for (int i = 1; i < 5; i++) {
            a.add(new VariableBuilding(1, 1));
        }

        a.add(new SimpleBuilding(List.of(Effect.STONE, Effect.GOLD, Effect.WOOD)));
        return a;
    }

    public void test_state(JSONObject state, Building building, int expectedNum, ArrayList<PlayerOrder> playerOrder) {
        assertEquals((building != null ? building.state() : "{}"), state.getString("currentBuilding"));
        assertEquals(Integer.toString(expectedNum), state.getString("numberOfCardsInBuildingTile"));
        assertEquals(playerOrder.toString(), state.getString("figures"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_TooFewCards() {
        assertEquals(buildingCards.size(), 0);
        BuildingTile buildingTile1 = new BuildingTile(buildingCards);
    }

    @Test
    public void test_GoodInitialization() {
        buildingCards = setBuildingCards();
        for (int i = 7; i >= 1; i--) {
            assertEquals(buildingCards.size(), i);
            BuildingTile buildingTile1 = new BuildingTile(buildingCards);
            JSONObject state = new JSONObject(buildingTile1.state());
            test_state(state, simpleBuilding, i, emptyOrder);
            buildingCards.remove(0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_TooManyCards() {
        buildingCards = setBuildingCards();
        buildingCards.add(new ArbitraryBuilding(2));
        assertEquals(buildingCards.size(), 8);
        BuildingTile buildingTile1 = new BuildingTile(buildingCards);
    }

    @Test
    public void test_tryToPlaceFigures() {
        buildingCards = setBuildingCards();

        assertEquals(buildingCards.size(), 7);
        BuildingTile buildingTile1 = new BuildingTile(buildingCards);
        JSONObject state = new JSONObject(buildingTile1.state());
        test_state(state, simpleBuilding, 7, emptyOrder);

        for (int i = 0; i <= 10; i++) {
            if (i != 1) {
                assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToPlaceFigures(player2, i));
                assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToPlaceFigures(player1, i));
                continue;
            }
            assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, buildingTile1.tryToPlaceFigures(player1, i));
            assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, buildingTile1.tryToPlaceFigures(player2, i));
        }
        state = new JSONObject(buildingTile1.state());
        test_state(state, simpleBuilding, 7, emptyOrder);
    }

    @Test
    public void test_placeFigures() {
        buildingCards = setBuildingCards();
        assertEquals(buildingCards.size(), 7);
        BuildingTile buildingTile1 = new BuildingTile(buildingCards);
        JSONObject state = new JSONObject(buildingTile1.state());
        test_state(state, simpleBuilding, 7, emptyOrder);

        assertFalse(buildingTile1.placeFigures(player1, 2));
        assertFalse(buildingTile1.placeFigures(player1, 0));
        assertTrue(buildingTile1.placeFigures(player1, 1));
        assertFalse(buildingTile1.placeFigures(player2, 1));

        state = new JSONObject(buildingTile1.state());
        test_state(state, simpleBuilding, 7, playerOrder1);
    }

    @Test
    public void test_tryToMakeAction() {
        buildingCards = setBuildingCards();
        assertEquals(buildingCards.size(), 7);
        BuildingTile buildingTile1 = new BuildingTile(buildingCards);

        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToMakeAction(player1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToMakeAction(player2));

        assertTrue(buildingTile1.placeFigures(player1, 1));
        JSONObject state = new JSONObject(buildingTile1.state());
        test_state(state, simpleBuilding, 7, playerOrder1);

        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, buildingTile1.tryToMakeAction(player1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToMakeAction(player2));

        state = new JSONObject(buildingTile1.state());
        test_state(state, simpleBuilding, 7, playerOrder1);
    }

    @Test
    public void test_makeAction() {
        buildingCards = setBuildingCards();
        assertEquals(buildingCards.size(), 7);
        BuildingTile buildingTile1 = new BuildingTile(buildingCards);
        ArrayList<Effect> inputResources1 = new ArrayList<>(List.of(Effect.STONE, Effect.WOOD, Effect.GOLD));
        ArrayList<Effect> inputResources2 = new ArrayList<>(List.of(Effect.WOOD));
        JSONObject state = new JSONObject(buildingTile1.state());
        test_state(state, simpleBuilding, 7, emptyOrder);

        assertEquals(ActionResult.FAILURE, buildingTile1.makeAction(player2, inputResources1, inputResources1));
        assertEquals(ActionResult.FAILURE, buildingTile1.makeAction(player1, inputResources2, inputResources1));
        assertTrue(buildingTile1.placeFigures(player1, 1));
        assertEquals(ActionResult.ACTION_DONE, buildingTile1.makeAction(player1, inputResources1, inputResources1));

        state = new JSONObject(buildingTile1.state());
        test_state(state, variableBuilding, 6, emptyOrder);

        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToMakeAction(player1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToMakeAction(player2));

        state = new JSONObject(buildingTile1.state());
        test_state(state, variableBuilding, 6, emptyOrder);

        assertTrue(buildingTile1.placeFigures(player2, 1));

        state = new JSONObject(buildingTile1.state());
        test_state(state, variableBuilding, 6, playerOrder2);

        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToMakeAction(player1));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, buildingTile1.tryToMakeAction(player2));

        assertEquals(ActionResult.FAILURE, buildingTile1.makeAction(player2, inputResources1, inputResources2));
        assertEquals(ActionResult.FAILURE, buildingTile1.makeAction(player1, inputResources2, Collections.emptyList()));
        assertEquals(ActionResult.ACTION_DONE,
                buildingTile1.makeAction(player2, inputResources2, Collections.emptyList()));

        state = new JSONObject(buildingTile1.state());
        test_state(state, variableBuilding, 5, emptyOrder);
    }

    @Test
    public void test_endOfGame() {
        buildingCards = new ArrayList<>();
        SimpleBuilding simpleBuildingOnlyWood = new SimpleBuilding((List.of(Effect.WOOD)));
        buildingCards.add(variableBuilding);
        for (int i = 1; i < 7; i++) {
            buildingCards.add(simpleBuildingOnlyWood);
        }
        assertEquals(buildingCards.size(), 7);
        BuildingTile buildingTile1 = new BuildingTile(buildingCards);
        JSONObject state;
        ArrayList<Effect> inputResources1 = new ArrayList<>(List.of(Effect.STONE, Effect.WOOD, Effect.GOLD));
        ArrayList<Effect> inputResources2 = new ArrayList<>(List.of(Effect.WOOD));
        for (int i = 7; i > 1; i--) {
            state = new JSONObject(buildingTile1.state());
            test_state(state, simpleBuildingOnlyWood, i, emptyOrder);

            assertFalse(buildingTile1.newTurn());
            assertTrue(buildingTile1.placeFigures(player1, 1));
            assertEquals(ActionResult.ACTION_DONE, buildingTile1.makeAction(player1, inputResources2, inputResources1));
        }
        state = new JSONObject(buildingTile1.state());
        test_state(state, variableBuilding, 1, emptyOrder);

        assertFalse(buildingTile1.newTurn());
        assertTrue(buildingTile1.placeFigures(player1, 1));
        assertEquals(ActionResult.ACTION_DONE, buildingTile1.makeAction(player1, inputResources2, inputResources1));

        assertTrue(buildingTile1.newTurn());
        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToPlaceFigures(player1, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToPlaceFigures(player2, 1));

        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToMakeAction(player1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, buildingTile1.tryToMakeAction(player2));

        state = new JSONObject(buildingTile1.state());
        test_state(state, null, 0, emptyOrder);
    }

    @Test
    public void test_skipAction() {
        buildingCards = setBuildingCards();
        assertEquals(buildingCards.size(), 7);
        BuildingTile buildingTile1 = new BuildingTile(buildingCards);
        JSONObject state = new JSONObject(buildingTile1.state());
        test_state(state, simpleBuilding, 7, emptyOrder);

        assertTrue(buildingTile1.placeFigures(player1, 1));

        state = new JSONObject(buildingTile1.state());
        test_state(state, simpleBuilding, 7, playerOrder1);

        assertTrue(buildingTile1.skipAction(player1));

        state = new JSONObject(buildingTile1.state());
        test_state(state, simpleBuilding, 7, emptyOrder);

        assertFalse(buildingTile1.skipAction(player2));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, buildingTile1.tryToPlaceFigures(player1, 1));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, buildingTile1.tryToPlaceFigures(player2, 1));
        assertFalse(buildingTile1.skipAction(player1));
        assertFalse(buildingTile1.skipAction(player2));
    }
}
