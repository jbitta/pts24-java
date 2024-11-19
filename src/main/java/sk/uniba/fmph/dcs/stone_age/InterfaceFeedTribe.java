package sk.uniba.fmph.dcs.stone_age;

import java.util.Collection;

public interface InterfaceFeedTribe {
    boolean feedTribeIfEnoughFood();

    boolean feedTribe(Collection<Effect> resources);

    boolean doNotFeedThisTurn();

    boolean isTribeFed();

}
