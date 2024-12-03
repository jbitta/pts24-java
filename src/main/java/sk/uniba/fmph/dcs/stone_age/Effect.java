package sk.uniba.fmph.dcs.stone_age;

public enum Effect {
    FOOD, WOOD, CLAY, STONE, GOLD, TOOL, FIELD, BUILDING, ONE_TIME_TOOL2, ONE_TIME_TOOL3, ONE_TIME_TOOL4, POINT;

    private static final int FOOD_POINTS = 2;
    private static final int WOOD_POINTS = 3;
    private static final int CLAY_POINTS = 4;
    private static final int STONE_POINTS = 5;
    private static final int GOLD_POINTS = 6;

    public boolean isResource() {
        return (this == WOOD || this == CLAY || this == STONE || this == GOLD);
    }

    public boolean isResourceOrFood() {
        return (this == FOOD || this.isResource());
    }

    public int points() {
        if (this == FOOD) {
            return FOOD_POINTS;
        }
        if (this == WOOD) {
            return WOOD_POINTS;
        }
        if (this == CLAY) {
            return CLAY_POINTS;
        }
        if (this == STONE) {
            return STONE_POINTS;
        }
        if (this == GOLD) {
            return GOLD_POINTS;
        }
        return 0;
    }
}
