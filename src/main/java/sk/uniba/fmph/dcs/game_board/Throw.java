package sk.uniba.fmph.dcs.game_board;

import java.util.ArrayList;
import java.util.Collections;

public class Throw {
    private static final int DICESIDES = 6;
    private final int desiredResult; // used for tests, -1 if throw should be random

    public Throw() {
        this(-1);
    }

    public Throw(final int desiredResult) {
        this.desiredResult = desiredResult;
    }

    public final ArrayList<Integer> throwDice(final int dices) {
        if (dices < 0) {
            throw new IllegalArgumentException("Number of dice must be equal or greater than 0");
        }
        if (desiredResult != -1) {
            return new ArrayList<>(Collections.nCopies(dices, desiredResult));
        }
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < dices; i++) {
            result.add((int) ((Math.random() * DICESIDES) + 1));
        }
        return result;
    }
}
