package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import java.util.ArrayList;

public class GetChoice implements EvaluateCivilisationCardImmediateEffect {
    private int numberOfResources;

    public GetChoice(final int numberOfResources) {
        this.numberOfResources = numberOfResources;
    }

    @Override
    public final ActionResult performEffect(final Player player, final Effect choice) {
        if (!choice.isResource()) {
            return ActionResult.FAILURE;
        }
        if (numberOfResources <= 0) {
            return ActionResult.FAILURE;
        }
        numberOfResources--;
        ArrayList<Effect> c = new ArrayList<>();
        c.add(choice);
        player.playerBoard().giveEffect(c);
        return ActionResult.ACTION_DONE;
    }
}
