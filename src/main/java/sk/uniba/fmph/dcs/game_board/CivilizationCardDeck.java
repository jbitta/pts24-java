package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.CivilisationCard;

import java.util.Collection;
import java.util.Optional;

public final class CivilizationCardDeck {
    public CivilizationCardDeck(final Collection<CivilisationCard> cards) {
    }

    public Optional<CivilisationCard> getTop() {
        return Optional.empty();
    }

    public String state() {
        return "";
    }
}
