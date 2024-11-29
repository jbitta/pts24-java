package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;

import static org.junit.Assert.*;

public class ToolMakerHutFieldsTest {
    int playerCount = 3;
    InterfacePlayerBoardGameBoard playerBoard = new InterfacePlayerBoardGameBoard() {
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
    PlayerOrder playerOrder1 = new PlayerOrder(1, playerCount);
    PlayerOrder playerOrder2 = new PlayerOrder(2, playerCount);
    PlayerOrder playerOrder3 = new PlayerOrder(3, playerCount);
    Player player1 = new Player(playerOrder1, playerBoard);
    Player player2 = new Player(playerOrder2, playerBoard);
    Player player3 = new Player(playerOrder3, playerBoard);
    ArrayList<Player> players = new ArrayList<>(List.of(player1, player2, player3));


    @Test(expected = IllegalArgumentException.class)
    public void testTooManyPlayers() {
        ToolMakerHutFields toolMakerHutFields = new ToolMakerHutFields(5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooFewPlayers() {
        ToolMakerHutFields toolMakerHutFields = new ToolMakerHutFields(1);
    }

    @Test
    public void testCanPlaceOnToolMakerHutField() {
        ToolMakerHutFields toolMakerHutFields = new ToolMakerHutFields(playerCount);
        assertTrue(toolMakerHutFields.canPlaceOnToolMaker(player1));
        assertTrue(toolMakerHutFields.placeOnToolMaker(player1));
        assertTrue(toolMakerHutFields.canPlaceOnFields(player1));
        assertTrue(toolMakerHutFields.canPlaceOnHut(player1));
        assertFalse(toolMakerHutFields.canPlaceOnToolMaker(player2));
        assertTrue(toolMakerHutFields.canPlaceOnFields(player2));
        assertTrue(toolMakerHutFields.canPlaceOnHut(player2));
        assertFalse(toolMakerHutFields.canPlaceOnToolMaker(player3));
        assertTrue(toolMakerHutFields.canPlaceOnFields(player3));
        assertTrue(toolMakerHutFields.canPlaceOnHut(player3));
        JSONObject state = new JSONObject(toolMakerHutFields.state());
        ArrayList<PlayerOrder> toolMakerFigures = new ArrayList<>(List.of(player1.playerOrder()));
        assertEquals(state.get("toolMakerFigures"), toolMakerFigures.toString());
        assertEquals(state.get("restriction"), "3");
    }

    @Test
    public void testPlaceOnToolMakerHutFields() {
        ToolMakerHutFields toolMakerHutFields = new ToolMakerHutFields(playerCount);
        assertTrue(toolMakerHutFields.placeOnToolMaker(player1));
        for (Player p : players) {
            assertFalse(toolMakerHutFields.placeOnToolMaker(p));
        }
        assertTrue(toolMakerHutFields.placeOnHut(player2));
        for (Player p : players) {
            assertFalse(toolMakerHutFields.placeOnHut(p));
        }
        JSONObject state = new JSONObject(toolMakerHutFields.state());
        ArrayList<PlayerOrder> toolMakerFigures = new ArrayList<>(List.of(player1.playerOrder()));
        ArrayList<PlayerOrder> hutFigures = new ArrayList<>(List.of(player2.playerOrder(), player2.playerOrder()));
        assertEquals(state.get("toolMakerFigures"), toolMakerFigures.toString());
        assertEquals(state.get("hutFigures"), hutFigures.toString());
        assertEquals(state.get("restriction"), "3");
    }

    @Test
    public void testRestrictionViolated() {
        ToolMakerHutFields toolMakerHutFields = new ToolMakerHutFields(playerCount);
        toolMakerHutFields.placeOnFields(player1);
        assertTrue(toolMakerHutFields.placeOnHut(player1));
        for (Player p : players) {
            assertFalse(toolMakerHutFields.placeOnToolMaker(p));
        }
        int playerCount1 = 4;
        ToolMakerHutFields toolMakerHutFields1 = new ToolMakerHutFields(playerCount1);
        Player player4 = new Player(new PlayerOrder(4, playerCount1), playerBoard);
        ArrayList<Player> fourPlayers = new ArrayList<>();
        fourPlayers.add(player4);
        for (int i = 0; i < players.size(); i++) {
            fourPlayers.add(new Player(new PlayerOrder(i + 1, playerCount1), playerBoard));
        }
        assertTrue(toolMakerHutFields1.placeOnToolMaker(fourPlayers.get(0)));
        assertTrue(toolMakerHutFields1.placeOnFields(fourPlayers.get(1)));
        assertTrue(toolMakerHutFields1.placeOnHut(fourPlayers.get(2)));
        JSONObject state = new JSONObject(toolMakerHutFields1.state());
        ArrayList<PlayerOrder> toolMakerFigures = new ArrayList<>(List.of(fourPlayers.get(0).playerOrder()));
        ArrayList<PlayerOrder> fieldsFigures = new ArrayList<>(List.of(fourPlayers.get(1).playerOrder()));
        ArrayList<PlayerOrder> hutFigures = new ArrayList<>(
                List.of(fourPlayers.get(2).playerOrder(), fourPlayers.get(2).playerOrder()));
        assertEquals(state.get("toolMakerFigures"), toolMakerFigures.toString());
        assertEquals(state.get("fieldsFigures"), fieldsFigures.toString());
        assertEquals(state.get("hutFigures"), hutFigures.toString());
        assertEquals(state.get("restriction"), "4");
    }

    @Test
    public void testActionsAndNewTurn() {
        ToolMakerHutFields toolMakerHutFields = new ToolMakerHutFields(playerCount);
        toolMakerHutFields.placeOnToolMaker(player1);
        toolMakerHutFields.placeOnHut(player2);
        assertFalse(toolMakerHutFields.placeOnFields(player3));
        for (Player p : players) {
            if (!p.equals(player1)) {
                assertFalse(toolMakerHutFields.actionToolMaker(p));
            }
        }
        assertTrue(toolMakerHutFields.actionToolMaker(player1));
        assertFalse(toolMakerHutFields.newTurn());
        for (Player p : players) {
            if (!p.equals(player2)) {
                assertFalse(toolMakerHutFields.actionHut(p));
            }
        }
        assertTrue(toolMakerHutFields.actionHut(player2));
        assertTrue(toolMakerHutFields.newTurn());
        assertTrue(toolMakerHutFields.placeOnFields(player3));
        for (Player p : players) {
            if (!p.equals(player3)) {
                assertFalse(toolMakerHutFields.actionFields(p));
            }
        }
        assertTrue(toolMakerHutFields.actionFields(player3));
        assertTrue(toolMakerHutFields.newTurn());
        JSONObject state = new JSONObject(toolMakerHutFields.state());
        ArrayList<PlayerOrder> toolMakerFigures = new ArrayList<>();
        ArrayList<PlayerOrder> fieldsFigures = new ArrayList<>();
        ArrayList<PlayerOrder> hutFigures = new ArrayList<>();
        assertEquals(state.get("toolMakerFigures"), toolMakerFigures.toString());
        assertEquals(state.get("fieldsFigures"), fieldsFigures.toString());
        assertEquals(state.get("hutFigures"), hutFigures.toString());
        assertEquals(state.get("restriction"), "3");
    }
}
