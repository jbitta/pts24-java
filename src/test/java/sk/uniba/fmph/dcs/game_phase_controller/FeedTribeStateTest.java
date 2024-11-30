package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.*;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class FeedTribeStateTest {
    static class FeedTribeMock implements InterfaceFeedTribe {
        private boolean feedTribeIfEnoughFood;
        private boolean feedTribe;
        private boolean doNotFeedThisTurn;
        private boolean isTribeFed;

        FeedTribeMock(final boolean feedTribeIfEnoughFood, final boolean feedTribe, final boolean doNotFeedThisTurn,
                final boolean isTribeFed) {
            this.feedTribeIfEnoughFood = feedTribeIfEnoughFood;
            this.feedTribe = feedTribe;
            this.doNotFeedThisTurn = doNotFeedThisTurn;
            this.isTribeFed = isTribeFed;

        }

        @Override
        public boolean feedTribeIfEnoughFood() {
            return feedTribeIfEnoughFood;
        }

        @Override
        public boolean feedTribe(Collection<Effect> resources) {
            return feedTribe;
        }

        @Override
        public boolean doNotFeedThisTurn() {
            return doNotFeedThisTurn;
        }

        @Override
        public boolean isTribeFed() {
            return isTribeFed;
        }
    }

    @Test
    public void feedTribeTest() {
        Map<PlayerOrder, InterfaceFeedTribe> feedTribeMap = new HashMap<>();
        PlayerOrder p1 = new PlayerOrder(1, 2);
        PlayerOrder p2 = new PlayerOrder(2, 2);
        feedTribeMap.put(p1, new FeedTribeMock(false, false, false, false));
        feedTribeMap.put(p2, new FeedTribeMock(true, true, true, true));
        FeedTribeState fts = new FeedTribeState(feedTribeMap);
        List<Effect> resources = new ArrayList<>();

        assertEquals(fts.feedTribe(p1, resources), ActionResult.FAILURE);
        assertEquals(fts.feedTribe(p2, resources), ActionResult.ACTION_DONE);
    }

    @Test
    public void doNotFeedThisTurnTest() {
        Map<PlayerOrder, InterfaceFeedTribe> feedTribeMap = new HashMap<>();
        PlayerOrder p1 = new PlayerOrder(1, 2);
        PlayerOrder p2 = new PlayerOrder(2, 2);
        feedTribeMap.put(p1, new FeedTribeMock(false, false, false, false));
        feedTribeMap.put(p2, new FeedTribeMock(true, true, true, true));
        FeedTribeState fts = new FeedTribeState(feedTribeMap);

        assertEquals(fts.doNotFeedThisTurn(p1), ActionResult.FAILURE);
        assertEquals(fts.doNotFeedThisTurn(p2), ActionResult.ACTION_DONE);
    }

    @Test
    public void tryToMakeAutomaticActionTest() {
        Map<PlayerOrder, InterfaceFeedTribe> feedTribeMap = new HashMap<>();
        PlayerOrder p1 = new PlayerOrder(1, 3);
        PlayerOrder p2 = new PlayerOrder(2, 3);
        PlayerOrder p3 = new PlayerOrder(3, 3);
        feedTribeMap.put(p1, new FeedTribeMock(false, false, false, true));
        feedTribeMap.put(p2, new FeedTribeMock(true, true, true, false));
        feedTribeMap.put(p3, new FeedTribeMock(false, false, false, false));
        FeedTribeState fts = new FeedTribeState(feedTribeMap);

        assertEquals(fts.tryToMakeAutomaticAction(p1), HasAction.NO_ACTION_POSSIBLE);
        assertEquals(fts.tryToMakeAutomaticAction(p2), HasAction.AUTOMATIC_ACTION_DONE);
        assertEquals(fts.tryToMakeAutomaticAction(p3), HasAction.WAITING_FOR_PLAYER_ACTION);

    }

}
