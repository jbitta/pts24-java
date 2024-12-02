package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import java.util.List;

public class GetSomethingFixed implements EvaluateCivilisationCardImmediateEffect {
    private final Effect effect;
    public GetSomethingFixed(final Effect effect) {
        this.effect = effect;
    }

    @Override
    public final ActionResult performEffect(final Player player, final Effect choice) {
        if (effect.equals(Effect.BUILDING)) {
            return ActionResult.FAILURE;
        }
        player.playerBoard().giveEffect(List.of(effect));
        return ActionResult.ACTION_DONE;

    }
}
