package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllPlayersTakeReward implements EvaluateCivilisationCardImmediateEffect {
    private final RewardMenu menu;
    private static final int WOODNUMBER = 1;

    private static final int CLAYNUMBER = 2;
    private static final int STONENUMBER = 3;
    private static final int GOLDNUMBER = 4;
    private static final int TOOLNUMBER = 5;
    private static final int FIELDNUMBER = 6;
    private ArrayList<Integer> diceThrows;
    private final Throw t;

    public AllPlayersTakeReward(final RewardMenu menu, final Throw t) {
        this.menu = menu;
        diceThrows = new ArrayList<>();
        this.t = t;
    }

    private static Map<Integer, Effect> resourceMapInitiate() {
        Map<Integer, Effect> map = new HashMap<>();
        map.put(WOODNUMBER, Effect.WOOD);
        map.put(CLAYNUMBER, Effect.CLAY);
        map.put(STONENUMBER, Effect.STONE);
        map.put(GOLDNUMBER, Effect.GOLD);
        map.put(TOOLNUMBER, Effect.TOOL);
        map.put(FIELDNUMBER, Effect.FIELD);
        return map;
    }

    private ArrayList<Effect> menuItemsInitiate(final Player player) {
        int numberOfPlayers = player.playerOrder().getPlayers();
        diceThrows = t.throwDice(numberOfPlayers);
        Map<Integer, Effect> resourceMap = resourceMapInitiate();
        ArrayList<Effect> menuItems = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            menuItems.add(resourceMap.get(diceThrows.get(i)));
        }
        return menuItems;
    }

    @Override
    public final ActionResult performEffect(final Player player, final Effect choice) {
        ArrayList<Effect> menuItems = menuItemsInitiate(player);
        menu.initiate(menuItems);
        return ActionResult.ACTION_DONE_ALL_PLAYERS_TAKE_A_REWARD;
    }

}
