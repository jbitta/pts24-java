package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;

import java.util.Map;

public final class PlayerFigures {
    private static final int INITIAL_FIGURE_COUNT = 5;
    private static final int PLAYER_MAX_FIGURE_COUNT = 10;

    private int totalFigures = INITIAL_FIGURE_COUNT;
    private int figures = INITIAL_FIGURE_COUNT;
    private boolean canTakeFigure = true;

    public void addNewFigure() {
        if (canTakeFigure && totalFigures < PLAYER_MAX_FIGURE_COUNT) {
            totalFigures++;
            canTakeFigure = false;
        }
    }

    public boolean hasFigures(final int count) {
        return figures >= count;
    }

    public int getTotalFigures() {
        return totalFigures;
    }

    public boolean takeFigures(final int count) {
        if (hasFigures(count)) {
            figures -= count;
            return true;
        }
        return false;
    }

    public void newTurn() {
        figures = totalFigures;
        canTakeFigure = true;
    }

    public String state() {
        Map<String, String> state = Map.of("totalFigures", String.valueOf(totalFigures), "figures",
                String.valueOf(figures));
        return new JSONObject(state).toString();
    }
}
