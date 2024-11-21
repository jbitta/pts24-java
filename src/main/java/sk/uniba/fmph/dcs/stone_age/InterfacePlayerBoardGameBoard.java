package sk.uniba.fmph.dcs.stone_age;

import java.util.Collection;
import java.util.OptionalInt;

public interface InterfacePlayerBoardGameBoard {
    void giveEffect(Collection<Effect> stuff);

    void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff);

    boolean takeResources(Collection<Effect> stuff);

    boolean takeFigures(int count);

    void giveFigures(int count);

    boolean hasFigures(int count);

    boolean hasSufficientTools(int goal);

    OptionalInt useTool(int idx);

    void takePoints(int points);

    void givePoints(int points);
}
