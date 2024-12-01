package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.InterfaceTakeReward;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class RewardMenu implements InterfaceTakeReward {
    private ArrayList<Effect> items;
    private Map<PlayerOrder, Player> players;
    private final ArrayList<Player> allPlayers;

    public RewardMenu(final ArrayList<Player> players) {
        allPlayers = new ArrayList<>(players);

    }

    public final void initiate(final ArrayList<Effect> items) {
        this.items = new ArrayList<>(items);
        this.players = mapInitiate(allPlayers);
    }

    private Map<PlayerOrder, Player> mapInitiate(final ArrayList<Player> players) {
        Map<PlayerOrder, Player> map = new HashMap<>();
        for (Player p : players) {
            map.put(p.playerOrder(), p);
        }
        return map;
    }

    @Override
    public final boolean takeReward(final PlayerOrder player, final Effect reward) {
        if (!items.contains(reward)) {
            return false;
        }
        if (!players.containsKey(player)) {
            return false;
        }
        ArrayList<Effect> result = new ArrayList<>();
        result.add(reward);
        players.get(player).playerBoard().giveEffect(result);
        players.remove(player);
        items.remove(reward);
        return true;
    }

    @Override
    public final HasAction tryMakeAction(final PlayerOrder player) {
        if (items.isEmpty()) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if (!players.containsKey(player)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if (items.size() == 1) {
            ArrayList<Effect> result = new ArrayList<>();
            result.add(items.get(0));
            players.get(player).playerBoard().giveEffect(result);
            items.remove(0);
            players.remove(player);
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    public final String state() {
        Map<String, String> state = Map.of("items", items.toString());

        return new JSONObject(state).toString();
    }

}
