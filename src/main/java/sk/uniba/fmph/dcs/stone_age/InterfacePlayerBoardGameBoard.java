package sk.uniba.fmph.dcs.stone_age;

import java.util.ArrayList;
import java.util.OptionalInt;

public interface InterfacePlayerBoardGameBoard {
    void giveEffect(ArrayList<Effect> stuff);

    void giveEndOfGameEffect(ArrayList<EndOfGameEffect> stuff);

    boolean takeResources(ArrayList<Effect> stuff);

    boolean takeFigures(int count);

    boolean hasFigures(int count);

    boolean hasSufficientTools(int goal);

    OptionalInt useTool(int idx);

}
