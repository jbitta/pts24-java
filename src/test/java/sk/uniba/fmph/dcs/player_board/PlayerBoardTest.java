package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class PlayerBoardTest {
    @Test
    public void testAddPoints() {
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.addPoints(10);
        playerBoard.addPoints(5);

        JSONObject state = new JSONObject(playerBoard.state());
        assertEquals("15", state.getString("points"));
    }

    @Test
    public void testAddHouse() {
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.addHouse();
        playerBoard.addHouse();

        JSONObject state = new JSONObject(playerBoard.state());
        assertEquals("2", state.getString("houses"));
    }

    @Test
    public void testAddEndOfGamePoints() {
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.addEndOfGamePoints();

        JSONObject state = new JSONObject(playerBoard.state());
        assertEquals("0", state.getString("points"));
    }

    @Test
    public void testState() {
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.addPoints(50);
        playerBoard.addHouse();

        JSONObject state = new JSONObject(playerBoard.state());
        assertEquals("1", state.getString("houses"));
        assertEquals("50", state.getString("points"));
    }

    @Test
    public void testNewTurn() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        playerResourcesAndFood.giveResources(List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD));
        PlayerFigures playerFigures = new PlayerFigures();
        TribeFedStatus tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);
        PlayerTools playerTools = new PlayerTools();
        playerTools.addTool();
        PlayerBoard playerBoard = new PlayerBoard(playerResourcesAndFood, new PlayerCivilisationCards(), playerFigures,
                tribeFedStatus, playerTools);
        assertTrue(tribeFedStatus.feedTribeIfEnoughFood());
        playerTools.useTool(0);
        assertFalse(playerTools.useTool(0).isPresent());

        playerBoard.newTurn();
        assertTrue(tribeFedStatus.feedTribeIfEnoughFood());
        playerBoard.newTurn();
        assertTrue(tribeFedStatus.feedTribeIfEnoughFood());
        playerBoard.newTurn();
        assertFalse(tribeFedStatus.feedTribeIfEnoughFood());
        assertTrue(playerTools.useTool(0).isPresent());

    }

    @Test
    public void testTakePoints() {
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.takePoints(PlayerBoard.POINTS_TO_TAKE_IF_TRIBE_IS_NOT_FED);

        JSONObject state = new JSONObject(playerBoard.state());
        assertEquals("-10", state.getString("points"));
    }
}
