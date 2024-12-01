package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.CivilisationCard;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;

import java.util.Optional;

public final class GetCard implements EvaluateCivilisationCardImmediateEffect {
    private final CivilizationCardDeck deck;

    public GetCard(final CivilizationCardDeck deck) {
        this.deck = deck;
    }

    public ActionResult performEffect(final Player player, final Effect choice) {
        Optional<CivilisationCard> cardFromDeck = deck.getTop();
        cardFromDeck.ifPresent(card -> player.playerBoard().giveEndOfGameEffect(card.endOfGameEffect()));
        return ActionResult.ACTION_DONE;
    }
}
