package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.ArrayList;
import java.util.Collection;

public final class CivilizationCardPlace implements InterfaceFigureLocationInternal {
    private int requiredResources;
    private ArrayList<PlayerOrder> figures;

    public CivilizationCardPlace(final CivilizationCardDeck deck, final CivilizationCardPlace next, final int requiredResources) {
    } // Zatial takto

    @Override
    public boolean placeFigures(final Player player, final int figureCount) {
        return false;
    }

    @Override
    public HasAction tryToPlaceFigures(final Player payer, final int count) {
        return null;
    }

    @Override
    public ActionResult makeAction(final Player player, final Collection<Effect> inputResources,
            final Collection<Effect> outputResources) {
        return null;
    }

    @Override
    public boolean skipAction(final Player player) {
        return false;
    }

    @Override
    public HasAction tryToMakeAction(final Player player) {
        return null;
    }

    @Override
    public boolean newTurn() {
        return false;
    }

    public String state() {
        return "";
    }
}
