package sk.uniba.fmph.dcs.game_board;

import java.util.ArrayList;

public class Throw {
    private static final int DICESIDES = 6;

    public final ArrayList<Integer> throwDice(final int dices) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < dices; i++) {
            result.add((int) ((Math.random() * DICESIDES) + 1));
        }
        return result;
    }
}
