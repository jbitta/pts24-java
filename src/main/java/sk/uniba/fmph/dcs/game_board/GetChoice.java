package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import java.util.ArrayList;

public class GetChoice implements EvaluateCivilisationCardImmediateEffect {

    public GetChoice() {
    }

    @Override
    public final ActionResult performEffect(final Player player, final Effect choice) {
        if (!choice.isResource()) {
            return ActionResult.FAILURE;
        }
        ArrayList<Effect> c = new ArrayList<>();
        c.add(choice);
        player.playerBoard().giveEffect(c);
        return ActionResult.ACTION_DONE;
    }

}
