package sk.uniba.fmph.dcs.stone_age;

import java.util.ArrayList;

public interface InterfaceFigureLocation {
    boolean placeFigures(PlayerOrder player, int figureCount);

    HasAction tryToPlaceFigures(PlayerOrder player, int count);

    ActionResult makeAction(PlayerOrder player, ArrayList<Effect> inputResources, ArrayList<Effect> outputResources);

    boolean skipAction(PlayerOrder player);

    HasAction tryToMakeAction(PlayerOrder player);

    boolean newTurn();

}
