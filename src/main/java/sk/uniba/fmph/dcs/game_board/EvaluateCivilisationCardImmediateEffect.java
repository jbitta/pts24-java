package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;

public interface EvaluateCivilisationCardImmediateEffect {
    ActionResult performEffect(Player player, Effect choice);
}
