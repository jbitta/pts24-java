package sk.uniba.fmph.dcs.stone_age;

import java.util.ArrayList;

public interface InterfaceStoneAgeGame {
    boolean placeFigures(int playerId, Location location, int figuresCount);

    boolean makeAction(int playerId, Location location, ArrayList<Effect> usedResources, ArrayList<Effect> desiredResources);

    boolean skipAction(int playerId, Location location);

    boolean useTools(int playerId, int toolIndex);

    boolean noMoreToolsThisThrow(int playerId);

    boolean feedTribe(int playerId, ArrayList<Effect> resources);

    boolean doNotFeedThisTurn(int playerId);

    boolean makeAllPlayersTakeARewardChoice(int playerId, Effect reward);
}
