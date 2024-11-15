package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;

import java.util.ArrayList;
import java.util.OptionalInt;

import static org.junit.Assert.assertEquals;

public class ArbitraryBuildingTest {
    @Test
    public void test_calculation() {
        ArbitraryBuilding building = new ArbitraryBuilding(7);
        ArrayList<Effect> buildingResources = new ArrayList<>();
        assertEquals(building.build(buildingResources), OptionalInt.empty());

        buildingResources.add(Effect.WOOD);
        assertEquals(building.build(buildingResources), OptionalInt.of(3));

        ArrayList<Effect> otherResources = new ArrayList<>();
        otherResources.add(Effect.WOOD);
        otherResources.add(Effect.CLAY);
        otherResources.add(Effect.CLAY);
        otherResources.add(Effect.STONE);
        assertEquals(building.build(otherResources), OptionalInt.of(16));

        ArrayList<Effect> otherResources2 = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            otherResources2.add(Effect.GOLD);
        }
        assertEquals(building.build(otherResources2), OptionalInt.empty());
        otherResources2.remove(4);
        assertEquals(building.build(otherResources2), OptionalInt.of(42));
        otherResources2.remove(2);
        otherResources2.add(Effect.WOOD);
        assertEquals(building.build(otherResources2), OptionalInt.of(39));
        ArrayList<Effect> otherResources3 = new ArrayList<>();
        otherResources3.add(Effect.FOOD);
        assertEquals(building.build(otherResources3), OptionalInt.empty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwWhenArgumentTooSmall() {
        new ArbitraryBuilding(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwWhenArgumentTooBig() {
        new ArbitraryBuilding(8);
    }
}
