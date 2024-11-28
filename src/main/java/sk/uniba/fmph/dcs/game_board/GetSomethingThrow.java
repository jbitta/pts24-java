package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;

public class GetSomethingThrow implements EvaluateCivilisationCardImmediateEffect {
    private final Effect resource;
    private final int numberOfDice;
    private boolean used = false;

    public GetSomethingThrow(final Effect resource, final int numberOfDice) {
        this.numberOfDice = numberOfDice;
        this.resource = resource;
    }

    @Override
    public final ActionResult performEffect(final Player player, final Effect choice) {
        if (used) {
            return ActionResult.FAILURE;
        }
        used = true;
        CurrentThrow currentThrow = new CurrentThrow();
        currentThrow.initiate(player, choice, numberOfDice);
        return ActionResult.ACTION_DONE;
    }
}
