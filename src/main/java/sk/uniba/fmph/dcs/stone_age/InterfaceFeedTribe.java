package sk.uniba.fmph.dcs.stone_age;

import java.util.ArrayList;

public interface InterfaceFeedTribe {
    boolean feedTribeIfEnoughFood();

    boolean feedTribe(ArrayList<Effect> resources);

    boolean doNotFeedThisTurn();

    boolean isTribeFed();

}
