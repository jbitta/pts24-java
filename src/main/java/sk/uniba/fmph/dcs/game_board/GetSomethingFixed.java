package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import java.util.ArrayList;
import java.util.List;

public class GetSomethingFixed implements EvaluateCivilisationCardImmediateEffect {

    public GetSomethingFixed() {
    }

    @Override
    public final ActionResult performEffect(final Player player, final Effect choice) {
        if (choice.equals(Effect.BUILDING)) {
            return ActionResult.FAILURE;
        }
        player.playerBoard().giveEffect(new ArrayList<>(List.of(choice)));
        return ActionResult.ACTION_DONE;

    }
}
