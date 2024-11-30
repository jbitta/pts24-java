package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TribeFedStatusTest {
    @Test
    public void TribeFedStatusTestNoFields() {
        PlayerFigures playerFigures = new PlayerFigures();
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        TribeFedStatus tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);

        // player has 12 food initially
        playerResourcesAndFood.giveResources(Collections.nCopies(38, Effect.FOOD));

        // simulate 10 turns
        for (int i = 0; i < 10; ++i) {
            assertTrue(tribeFedStatus.feedTribeIfEnoughFood());
            playerFigures.newTurn();
            tribeFedStatus.newTurn();
        }
        assertFalse(tribeFedStatus.feedTribeIfEnoughFood());
        playerResourcesAndFood.giveResources(Collections.nCopies(4, Effect.FOOD));
        assertFalse(tribeFedStatus.feedTribeIfEnoughFood());
        playerResourcesAndFood.giveResources(List.of(Effect.FOOD));
        assertTrue(tribeFedStatus.feedTribeIfEnoughFood());
    }

    @Test
    public void TribeFedStatusTestAddField() {
        PlayerFigures playerFigures = new PlayerFigures();
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        TribeFedStatus tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);

        // player has 12 food initially
        playerResourcesAndFood.giveResources(Collections.nCopies(18, Effect.FOOD));
        tribeFedStatus.addField();
        tribeFedStatus.addField();

        // simulate 10 turns
        for (int i = 0; i < 10; ++i) {
            assertTrue(tribeFedStatus.feedTribeIfEnoughFood());
            playerFigures.newTurn();
            tribeFedStatus.newTurn();
        }
        assertFalse(tribeFedStatus.feedTribeIfEnoughFood());
        playerFigures.newTurn();
        tribeFedStatus.newTurn();
        playerResourcesAndFood.giveResources(Collections.nCopies(3, Effect.FOOD));
        assertTrue(tribeFedStatus.feedTribeIfEnoughFood());
        playerFigures.newTurn();
        tribeFedStatus.newTurn();
        assertFalse(tribeFedStatus.feedTribeIfEnoughFood());
    }

    @Test
    public void TribeFedStatusTestAddFieldMaxFields() {
        PlayerFigures playerFigures = new PlayerFigures();
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        TribeFedStatus tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);

        assertTrue(playerResourcesAndFood.hasResources(Collections.nCopies(12, Effect.FOOD)));
        assertFalse(playerResourcesAndFood.hasResources(Collections.nCopies(13, Effect.FOOD)));

        for (int i = 0; i < 20; ++i) {
            tribeFedStatus.addField();
        }
        tribeFedStatus.newTurn();

        assertTrue(playerResourcesAndFood.hasResources(Collections.nCopies(22, Effect.FOOD)));
        assertFalse(playerResourcesAndFood.hasResources(Collections.nCopies(23, Effect.FOOD)));
    }

    @Test
    public void TribeFedStatusTestFeedTribeIfEnoughFood() {
        PlayerFigures playerFigures = new PlayerFigures();
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        TribeFedStatus tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);

        // player has 12 food initially
        playerResourcesAndFood.giveResources(Collections.nCopies(88, Effect.FOOD));

        // player has 5 figures initially
        for (int i = 0; i < 5; ++i) {
            playerFigures.addNewFigure();
            playerFigures.newTurn();
        }

        for (int i = 0; i < 10; ++i) {
            assertTrue(tribeFedStatus.feedTribeIfEnoughFood());

            // feeding multiple times in one turn has no effect
            assertTrue(tribeFedStatus.feedTribeIfEnoughFood());
            tribeFedStatus.newTurn();
        }
        assertFalse(tribeFedStatus.feedTribeIfEnoughFood());
    }

    @Test
    public void TribeFedStatusTestFeedTribeWithFood() {
        PlayerFigures playerFigures = new PlayerFigures();
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        TribeFedStatus tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);

        playerResourcesAndFood.giveResources(Collections.nCopies(48, Effect.FOOD));
        playerFigures.addNewFigure();
        playerFigures.newTurn();

        for (int i = 0; i < 10; ++i) {
            assertTrue(tribeFedStatus.feedTribe(Collections.nCopies(6, Effect.FOOD)));
            assertTrue(playerResourcesAndFood.hasResources(Collections.nCopies(60 - 6 * (i + 1), Effect.FOOD)));
            assertFalse(playerResourcesAndFood.hasResources(Collections.nCopies(60 - 6 * (i + 1) + 1, Effect.FOOD)));

            // feeding multiple times in one turn has no effect, doesn't take food
            assertTrue(tribeFedStatus.feedTribe(Collections.nCopies(6, Effect.FOOD)));
            assertTrue(playerResourcesAndFood.hasResources(Collections.nCopies(60 - 6 * (i + 1), Effect.FOOD)));
            assertFalse(playerResourcesAndFood.hasResources(Collections.nCopies(60 - 6 * (i + 1) + 1, Effect.FOOD)));
            tribeFedStatus.newTurn();
        }
        assertFalse(tribeFedStatus.feedTribe(Collections.nCopies(6, Effect.FOOD)));
    }

    @Test
    public void TribeFedStatusTestFeedTribeWithResources() {
        {
            PlayerFigures playerFigures = new PlayerFigures();
            PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
            TribeFedStatus tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);

            playerFigures.addNewFigure();
            playerFigures.newTurn();
            playerResourcesAndFood.giveResources(Collections.nCopies(12, Effect.WOOD));
            playerResourcesAndFood.giveResources(Collections.nCopies(12, Effect.STONE));
            playerResourcesAndFood.giveResources(Collections.nCopies(12, Effect.CLAY));
            playerResourcesAndFood.giveResources(Collections.nCopies(12, Effect.GOLD));

            for (Effect resource : List.of(Effect.FOOD, Effect.WOOD, Effect.STONE, Effect.CLAY, Effect.GOLD)) {
                for (int i = 0; i < 2; ++i) {
                    assertTrue(tribeFedStatus.feedTribe(Collections.nCopies(6, resource)));
                    tribeFedStatus.newTurn();
                }
                assertFalse(tribeFedStatus.feedTribe(Collections.nCopies(6, resource)));
            }
        }

        {
            PlayerFigures playerFigures = new PlayerFigures();
            PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
            TribeFedStatus tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);

            playerResourcesAndFood.giveResources(Collections.nCopies(10, Effect.WOOD));

            assertTrue(tribeFedStatus.feedTribe(Collections.nCopies(5, Effect.FOOD)));
            tribeFedStatus.newTurn();
            assertTrue(tribeFedStatus.feedTribe(Collections.nCopies(5, Effect.FOOD)));
            tribeFedStatus.newTurn();

            // not enough food
            assertFalse(tribeFedStatus.feedTribe(Collections.nCopies(5, Effect.FOOD)));

            // you need to spend food first
            assertFalse(tribeFedStatus.feedTribe(Collections.nCopies(5, Effect.WOOD)));

            // not enough food + resources
            assertFalse(tribeFedStatus.feedTribe(List.of(Effect.FOOD, Effect.FOOD, Effect.WOOD, Effect.WOOD)));

            Collection<Effect> resources = new ArrayList<>(List.of(Effect.FOOD, Effect.FOOD));
            resources.addAll(List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD));
            assertTrue(tribeFedStatus.feedTribe(resources));
        }
    }

    @Test
    public void TribeFedStatusTestSetTribeFed() {
        PlayerFigures playerFigures = new PlayerFigures();
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        TribeFedStatus tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);

        tribeFedStatus.addField();
        tribeFedStatus.addField();

        // if enough food, feed automatically
        assertTrue(tribeFedStatus.setTribeFed()); // 9 food after setTribeFed()
        assertTrue(tribeFedStatus.setTribeFed()); // no effect in same turn
        tribeFedStatus.newTurn();
        assertTrue(tribeFedStatus.setTribeFed()); // 6 food
        tribeFedStatus.newTurn();
        assertTrue(tribeFedStatus.setTribeFed()); // 3 food
        tribeFedStatus.newTurn();
        assertTrue(tribeFedStatus.setTribeFed()); // 0 food
        tribeFedStatus.newTurn();

        assertFalse(playerResourcesAndFood.hasResources(List.of(Effect.FOOD)));

        // can't feed automatically
        assertFalse(tribeFedStatus.setTribeFed());

        // take away all food
        assertFalse(playerResourcesAndFood.hasResources(List.of(Effect.FOOD)));
    }

    @Test
    public void TribeFedStatusTestIsTribeFed() {
        PlayerFigures playerFigures = new PlayerFigures();
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        TribeFedStatus tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);

        playerResourcesAndFood.giveResources(Collections.nCopies(38, Effect.FOOD));

        for (int i = 0; i < 10; ++i) {
            assertFalse(tribeFedStatus.isTribeFed());
            tribeFedStatus.feedTribeIfEnoughFood();
            assertTrue(tribeFedStatus.isTribeFed());
            tribeFedStatus.newTurn();
            assertFalse(tribeFedStatus.isTribeFed());
        }

        for (int i = 0; i < 5; ++i) {
            tribeFedStatus.addField();
        }

        for (int i = 0; i < 10; ++i) {
            assertFalse(tribeFedStatus.isTribeFed());
            tribeFedStatus.feedTribe(Collections.nCopies(5, Effect.FOOD));
            assertTrue(tribeFedStatus.isTribeFed());
            tribeFedStatus.newTurn();
            assertFalse(tribeFedStatus.isTribeFed());
        }

        tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);

        // take away all food
        playerResourcesAndFood.takeResources(Collections.nCopies(12, Effect.FOOD));

        for (Effect resource : List.of(Effect.WOOD, Effect.STONE, Effect.CLAY, Effect.GOLD)) {
            playerResourcesAndFood.giveResources(Collections.nCopies(5, resource));

            assertFalse(tribeFedStatus.isTribeFed());
            tribeFedStatus.feedTribe(Collections.nCopies(5, resource));
            assertTrue(tribeFedStatus.isTribeFed());
            tribeFedStatus.newTurn();
            assertFalse(tribeFedStatus.isTribeFed());
        }
    }

    @Test
    public void TribeFedStatusTestState() {
        PlayerFigures playerFigures = new PlayerFigures();
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        TribeFedStatus tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);

        {
            JSONObject state = new JSONObject(tribeFedStatus.state());
            assertEquals("false", state.getString("tribeFed"));
            assertEquals("0", state.getString("fields"));
        }

        tribeFedStatus.addField();
        {
            JSONObject state = new JSONObject(tribeFedStatus.state());
            assertEquals("false", state.getString("tribeFed"));
            assertEquals("1", state.getString("fields"));
        }

        tribeFedStatus.feedTribeIfEnoughFood();
        {
            JSONObject state = new JSONObject(tribeFedStatus.state());
            assertEquals("true", state.getString("tribeFed"));
            assertEquals("1", state.getString("fields"));
        }

        tribeFedStatus.newTurn();
        {
            JSONObject state = new JSONObject(tribeFedStatus.state());
            assertEquals("false", state.getString("tribeFed"));
            assertEquals("1", state.getString("fields"));
        }
    }
}
