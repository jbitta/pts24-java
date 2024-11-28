package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import java.util.ArrayList;

public class GetSomethingFixed implements EvaluateCivilisationCardImmediateEffect {
    private final ArrayList<Effect> resource;
    private boolean used = false;

    public GetSomethingFixed(final ArrayList<Effect> resource) {
        this.resource = resource;
    }

    @Override
    public final ActionResult performEffect(final Player player, final Effect choice) {
        if (used) {
            return ActionResult.FAILURE;
        }
        used = true;
        player.playerBoard().giveEffect(resource);
        return ActionResult.ACTION_DONE;
    }
}
