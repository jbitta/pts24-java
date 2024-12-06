package sk.uniba.fmph.dcs.stone_age;

import java.util.HashMap;
import java.util.Map;

public final class StoneAgeObservable implements InterfaceStoneAgeObservable {
    private final Map<Integer, InterfaceStoneAgeObserver> playerObserver;

    public StoneAgeObservable() {
        playerObserver = new HashMap<>();
    }

    @Override
    public void registerObserver(final int playerId, final InterfaceStoneAgeObserver observer) {
        playerObserver.put(playerId, observer);
    }

    public void notify(final String state) {
        for (int player : playerObserver.keySet()) {
            playerObserver.get(player).update(state);
        }
    }
}
