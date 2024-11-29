package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerResourcesAndFoodTest {
    @Test
    public void testHasResourcesInitialFood() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();

        for (int i = 0; i <= 12; ++i) {
            assertTrue(playerResourcesAndFood.hasResources(Collections.nCopies(i, Effect.FOOD)));
        }

        assertFalse(playerResourcesAndFood.hasResources(Collections.nCopies(13, Effect.FOOD)));
        assertFalse(playerResourcesAndFood.hasResources(Collections.nCopies(14, Effect.FOOD)));
        assertFalse(playerResourcesAndFood.hasResources(Collections.nCopies(15, Effect.FOOD)));
    }

    @Test
    public void testHasResourcesNoResources() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();

        List<Effect> resources = List.of(Effect.STONE, Effect.WOOD, Effect.CLAY, Effect.GOLD);
        for (Effect effect : resources) {
            assertFalse(playerResourcesAndFood.hasResources(List.of(effect)));
        }
    }

    @Test
    public void testHasResourcesSomeResources() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        assertTrue(playerResourcesAndFood.giveResources(Collections.nCopies(10, Effect.FOOD)));
        assertTrue(playerResourcesAndFood.giveResources(Collections.nCopies(10, Effect.WOOD)));
        assertTrue(playerResourcesAndFood.giveResources(Collections.nCopies(10, Effect.GOLD)));

        List<Effect> resources = List.of(Effect.FOOD, Effect.WOOD, Effect.GOLD);
        for (Effect resource : resources) {
            for (int i = 0; i <= 10; ++i) {
                assertTrue(playerResourcesAndFood.hasResources(Collections.nCopies(i, resource)));
            }
        }

        for (int i = 11; i <= 22; ++i) {
            assertTrue(playerResourcesAndFood.hasResources(Collections.nCopies(i, Effect.FOOD)));
        }
        assertFalse(playerResourcesAndFood.hasResources(Collections.nCopies(23, Effect.FOOD)));

        for (Effect resource : resources) {
            if (resource.isResource()) {
                for (int i = 11; i <= 15; ++i) {
                    assertFalse(playerResourcesAndFood.hasResources(Collections.nCopies(i, resource)));
                }
            }
        }
    }

    @Test
    public void testHasResourcesAlwaysHasZeroResources() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        assertTrue(playerResourcesAndFood.hasResources(List.of()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasResourcesThrowsOnInvalidEffect1() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        playerResourcesAndFood.hasResources(List.of(Effect.TOOL));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasResourcesThrowsOnInvalidEffect2() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        playerResourcesAndFood.hasResources(List.of(Effect.FIELD));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasResourcesThrowsOnInvalidEffect3() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        playerResourcesAndFood.hasResources(List.of(Effect.BUILDING));
    }

    @Test
    public void testTakeResourcesNoResources() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();

        List<Effect> resources = List.of(Effect.WOOD, Effect.CLAY, Effect.STONE, Effect.CLAY);
        for (Effect resource : resources) {
            assertFalse(playerResourcesAndFood.takeResources(List.of(resource)));
        }

        assertFalse(playerResourcesAndFood
                .takeResources(List.of(Effect.STONE, Effect.STONE, Effect.CLAY, Effect.CLAY, Effect.GOLD)));
    }

    @Test
    public void testTakeResourcesSomeResources() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        assertTrue(playerResourcesAndFood.giveResources(Collections.nCopies(5, Effect.FOOD)));
        assertTrue(playerResourcesAndFood.giveResources(Collections.nCopies(5, Effect.WOOD)));
        assertTrue(playerResourcesAndFood.giveResources(Collections.nCopies(5, Effect.CLAY)));

        List<Effect> resources = List.of(Effect.FOOD, Effect.WOOD, Effect.CLAY);
        for (Effect resource : resources) {
            assertTrue(playerResourcesAndFood.takeResources(Collections.nCopies(2, resource)));
            assertTrue(playerResourcesAndFood.takeResources(Collections.nCopies(3, resource)));
        }

        for (Effect resource : resources) {
            if (resource.isResource()) {
                assertFalse(playerResourcesAndFood.takeResources(List.of(resource)));
            }
        }

        assertTrue(playerResourcesAndFood.takeResources(Collections.nCopies(5, Effect.FOOD)));
    }

    @Test
    public void testTakeResourcesAlwaysCanTakeZeroResources() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        assertTrue(playerResourcesAndFood.takeResources(List.of()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTakeResourcesThrowsOnInvalidEffect1() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        playerResourcesAndFood.takeResources(List.of(Effect.TOOL));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTakeResourcesThrowsOnInvalidEffect2() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        playerResourcesAndFood.takeResources(List.of(Effect.FIELD));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTakeResourcesThrowsOnInvalidEffect3() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        playerResourcesAndFood.takeResources(List.of(Effect.BUILDING));
    }

    @Test
    public void testGiveResourcesSomeResources() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        assertTrue(playerResourcesAndFood
                .giveResources(List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.STONE, Effect.CLAY, Effect.CLAY)));
        assertTrue(playerResourcesAndFood.giveResources(Collections.nCopies(2, Effect.FOOD)));
        assertTrue(playerResourcesAndFood.giveResources(Collections.nCopies(3, Effect.WOOD)));
        assertTrue(playerResourcesAndFood.giveResources(Collections.nCopies(4, Effect.CLAY)));
        assertTrue(playerResourcesAndFood.giveResources(Collections.nCopies(5, Effect.STONE)));
        assertTrue(playerResourcesAndFood.giveResources(Collections.nCopies(6, Effect.GOLD)));
    }

    @Test
    public void testGiveResourcesAlwaysCanGiveZeroResources() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        assertTrue(playerResourcesAndFood.giveResources(List.of()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGiveResourcesThrowsOnInvalidEffect1() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        playerResourcesAndFood.giveResources(List.of(Effect.ONE_TIME_TOOL2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGiveResourcesThrowsOnInvalidEffect2() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        playerResourcesAndFood.giveResources(List.of(Effect.ONE_TIME_TOOL3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGiveResourcesThrowsOnInvalidEffect3() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        playerResourcesAndFood.giveResources(List.of(Effect.ONE_TIME_TOOL4));
    }

    @Test
    public void testNumberOfResourcesForFinalPointsZeroPoints() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        assertEquals(0, playerResourcesAndFood.numberOfResourcesForFinalPoints());

        playerResourcesAndFood.giveResources(List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD));
        assertEquals(0, playerResourcesAndFood.numberOfResourcesForFinalPoints());
    }

    @Test
    public void testNumberOfResourcesForFinalPointsSomePoints() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();

        playerResourcesAndFood.giveResources(List.of(Effect.FOOD, Effect.WOOD, Effect.GOLD, Effect.GOLD, Effect.CLAY));
        assertEquals(4, playerResourcesAndFood.numberOfResourcesForFinalPoints());

        playerResourcesAndFood.giveResources(List.of(Effect.FOOD, Effect.FOOD));
        assertEquals(4, playerResourcesAndFood.numberOfResourcesForFinalPoints());

        playerResourcesAndFood.giveResources(Collections.nCopies(10, Effect.WOOD));
        assertEquals(14, playerResourcesAndFood.numberOfResourcesForFinalPoints());

        playerResourcesAndFood.takeResources(List.of(Effect.GOLD, Effect.FOOD, Effect.CLAY));
        assertEquals(12, playerResourcesAndFood.numberOfResourcesForFinalPoints());

        playerResourcesAndFood.giveResources(Collections.nCopies(7, Effect.STONE));
        playerResourcesAndFood.giveResources(Collections.nCopies(8, Effect.STONE));
        assertEquals(27, playerResourcesAndFood.numberOfResourcesForFinalPoints());

        playerResourcesAndFood.giveResources(List.of(Effect.GOLD, Effect.GOLD, Effect.CLAY, Effect.CLAY));
        playerResourcesAndFood.takeResources(Collections.nCopies(4, Effect.STONE));
        playerResourcesAndFood.takeResources(List.of(Effect.GOLD, Effect.STONE, Effect.WOOD));
        assertEquals(24, playerResourcesAndFood.numberOfResourcesForFinalPoints());

        assertFalse(playerResourcesAndFood.takeResources(Collections.nCopies(100, Effect.STONE)));
        assertFalse(playerResourcesAndFood.takeResources(Collections.nCopies(5, Effect.CLAY)));
        assertEquals(24, playerResourcesAndFood.numberOfResourcesForFinalPoints());
    }

    @Test
    public void testState() {
        PlayerResourcesAndFood playerResourcesAndFood = new PlayerResourcesAndFood();
        JSONObject state = new JSONObject(playerResourcesAndFood.state());
        assertEquals("12", state.getJSONObject("resources").getString("FOOD"));

        playerResourcesAndFood.giveResources(List.of(Effect.FOOD, Effect.FOOD, Effect.GOLD));
        state = new JSONObject(playerResourcesAndFood.state());
        assertEquals("14", state.getJSONObject("resources").getString("FOOD"));
        assertEquals("1", state.getJSONObject("resources").getString("GOLD"));

        playerResourcesAndFood.giveResources(Collections.nCopies(10, Effect.STONE));
        state = new JSONObject(playerResourcesAndFood.state());
        assertEquals("10", state.getJSONObject("resources").getString("STONE"));

        playerResourcesAndFood.takeResources(List.of(Effect.FOOD, Effect.GOLD, Effect.STONE, Effect.STONE));
        state = new JSONObject(playerResourcesAndFood.state());
        assertEquals("13", state.getJSONObject("resources").getString("FOOD"));
        assertEquals("0", state.getJSONObject("resources").getString("GOLD"));
        assertEquals("8", state.getJSONObject("resources").getString("STONE"));
    }
}
