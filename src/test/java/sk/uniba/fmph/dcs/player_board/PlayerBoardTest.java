package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
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
        assertEquals("15", state.getJSONObject("points").toString());
    }

    @Test
    public void testAddHouse() {
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.addHouse();
        playerBoard.addHouse();

        JSONObject state = new JSONObject(playerBoard.state());
        assertEquals("2", state.getJSONObject("houses").toString());
    }

    @Test
    public void testAddEndOfGamePoints() {
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.addEndOfGamePoints();

        JSONObject state = new JSONObject(playerBoard.state());
        assertEquals("0", state.getJSONObject("points").toString());
    }

    @Test
    public void testState() {
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.addPoints(50);
        playerBoard.addHouse();

        JSONObject state = new JSONObject(playerBoard.state());
        assertEquals("1", state.getJSONObject("houses").toString());
        assertEquals("50", state.getJSONObject("points").toString());
    }

    @Test
    public void testFeedTribeIfEnoughFood() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        playerResourcesAndFood.giveResources(List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD));
        PlayerFigures playerFigures = new PlayerFigures();
        TribeFedStatus tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);
        PlayerBoard playerBoard = new PlayerBoard(playerResourcesAndFood, new PlayerCivilisationCards(), playerFigures,
                tribeFedStatus, new PlayerTools());
        assertTrue(playerBoard.feedTribeIfEnoughFood());

    }

    @Test
    public void testDoNotFeedThisTurn() {
        PlayerBoard playerBoard = new PlayerBoard();
        assertFalse(playerBoard.doNotFeedThisTurn());

        JSONObject state = new JSONObject(playerBoard.state());
        assertEquals("-10", state.getJSONObject("points").toString());

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
        assertFalse(tribeFedStatus.feedTribeIfEnoughFood());
        assertTrue(playerTools.useTool(0).isPresent());

    }

    @Test
    public void testGiveEffect() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        PlayerBoard playerBoard = new PlayerBoard(playerResourcesAndFood, new PlayerCivilisationCards(),
                new PlayerFigures(), new TribeFedStatus(playerResourcesAndFood, new PlayerFigures()),
                new PlayerTools());

        assertFalse(playerResourcesAndFood.hasResources(List.of(Effect.WOOD)));
        playerBoard.giveEffect(List.of(Effect.WOOD));
        assertTrue(playerResourcesAndFood.hasResources(List.of(Effect.WOOD)));

    }

    @Test
    public void testGiveEndOfGameEffect() {
        PlayerCivilisationCards playerCards = new PlayerCivilisationCards();
        PlayerBoard playerBoard = new PlayerBoard(new PlayerResourcesAndFood(), playerCards, new PlayerFigures(),
                new TribeFedStatus(new PlayerResourcesAndFood(), new PlayerFigures()), new PlayerTools());

        playerBoard.giveEndOfGameEffect(List.of(EndOfGameEffect.FARMER));
        JSONObject state = new JSONObject(playerCards.state());
        assertEquals("1", state.getJSONObject("endOfGameEffects").getString("FARMER"));
    }

    @Test
    public void testTakeResources() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        PlayerBoard playerBoard = new PlayerBoard(playerResourcesAndFood, new PlayerCivilisationCards(),
                new PlayerFigures(), new TribeFedStatus(playerResourcesAndFood, new PlayerFigures()),
                new PlayerTools());
        assertFalse(playerResourcesAndFood.takeResources(List.of(Effect.WOOD)));
        assertFalse(playerBoard.takeResources(List.of(Effect.WOOD)));
    }

    @Test
    public void testTakeFigures() {
        PlayerFigures playerFigures = new PlayerFigures();
        PlayerBoard playerBoard = new PlayerBoard(new PlayerResourcesAndFood(), new PlayerCivilisationCards(),
                playerFigures, new TribeFedStatus(new PlayerResourcesAndFood(), new PlayerFigures()),
                new PlayerTools());
        assertTrue(playerFigures.takeFigures(1));
        assertTrue(playerBoard.takeFigures(1));

    }

    @Test
    public void testGiveFigures() {
        PlayerFigures playerFigures = new PlayerFigures();
        PlayerBoard playerBoard = new PlayerBoard(new PlayerResourcesAndFood(), new PlayerCivilisationCards(),
                playerFigures, new TribeFedStatus(new PlayerResourcesAndFood(), new PlayerFigures()),
                new PlayerTools());
        assertEquals(5, playerFigures.getTotalFigures());
        playerBoard.giveFigures(2);
        assertEquals(7, playerFigures.getTotalFigures());
    }

    @Test
    public void testHasFigures() {
        PlayerFigures playerFigures = new PlayerFigures();
        PlayerBoard playerBoard = new PlayerBoard(new PlayerResourcesAndFood(), new PlayerCivilisationCards(),
                playerFigures, new TribeFedStatus(new PlayerResourcesAndFood(), new PlayerFigures()),
                new PlayerTools());
        assertEquals(playerFigures.hasFigures(5), playerBoard.hasFigures(5));

    }

    @Test
    public void testHasSufficientTools() {
        PlayerTools playerTools = new PlayerTools();
        PlayerBoard playerBoard = new PlayerBoard(new PlayerResourcesAndFood(), new PlayerCivilisationCards(),
                new PlayerFigures(), new TribeFedStatus(new PlayerResourcesAndFood(), new PlayerFigures()),
                playerTools);
        playerTools.addTool();
        playerTools.addTool();

        assertTrue(playerTools.hasSufficientTools(2));
        assertTrue(playerBoard.hasSufficientTools(2));
        assertFalse(playerTools.hasSufficientTools(3));
        assertFalse(playerBoard.hasSufficientTools(3));
    }

    @Test
    public void testUseTool() {
        PlayerTools playerTools = new PlayerTools();
        PlayerBoard playerBoard = new PlayerBoard(new PlayerResourcesAndFood(), new PlayerCivilisationCards(),
                new PlayerFigures(), new TribeFedStatus(new PlayerResourcesAndFood(), new PlayerFigures()),
                playerTools);
        playerTools.addTool();
        playerTools.addTool();
        assertTrue(playerTools.useTool(0).isPresent());
        assertFalse(playerTools.useTool(0).isPresent());

        assertTrue(playerBoard.useTool(1).isPresent());
        assertFalse(playerBoard.useTool(1).isPresent());

    }

    @Test
    public void testTakePoints() {
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.takePoints(10);

        JSONObject state = new JSONObject(playerBoard.state());
        assertEquals("-10", state.getJSONObject("points").toString());
    }

    @Test
    public void testGivePoints() {
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.givePoints(15);

        JSONObject state = new JSONObject(playerBoard.state());
        assertEquals("15", state.getJSONObject("points").toString());
    }
}
