package sk.uniba.fmph.dcs.game_board;

import java.util.ArrayList;

public class Throw {
    private static final int DICESIDES = 6;
    private final int desiredResult;    //used for tests, -1 if throw should be random

    public Throw(final int desiredResult) {
        this.desiredResult = desiredResult;
    }

    public final ArrayList<Integer> throwDice(final int dices) {
        if (desiredResult != -1) {
            ArrayList<Integer> result = new ArrayList<>();
            result.add(desiredResult);
            return result;
        }
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < dices; i++) {
            result.add((int) ((Math.random() * DICESIDES) + 1));
        }
        return result;
    }
}
