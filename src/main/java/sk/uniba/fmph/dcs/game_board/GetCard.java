package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.CivilisationCard;
import sk.uniba.fmph.dcs.stone_age.Effect;

public class GetCard implements EvaluateCivilisationCardImmediateEffect {
    private boolean used = false;
    private final CivilisationCard card;

    public GetCard(final CivilisationCard card) {
        this.card = card;
    }

    @Override
    public final ActionResult performEffect(final Player player, final Effect choice) {
        if (used) {
            return ActionResult.FAILURE;
        }
        used = true;
        player.playerBoard().giveEndOfGameEffect((card.endOfGameEffect()));
        return ActionResult.ACTION_DONE;
    }
}
