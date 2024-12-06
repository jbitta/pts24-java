package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;

import java.util.Collection;

public final class PlaceOnHutAdaptor implements InterfaceFigureLocationInternal {
    private final ToolMakerHutFields hut;

    public PlaceOnHutAdaptor(final ToolMakerHutFields hut) {
        this.hut = hut;
    }

    @Override
    public boolean placeFigures(final Player player, final int figureCount) {
        if (tryToPlaceFigures(player, figureCount).equals(HasAction.NO_ACTION_POSSIBLE)) {
            return false;
        }
        return hut.placeOnHut(player);
    }

    @Override
    public HasAction tryToPlaceFigures(final Player player, final int count) {
        if (count != 2 || !hut.canPlaceOnHut(player) || !player.playerBoard().hasFigures(count)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public ActionResult makeAction(final Player player, final Collection<Effect> inputResources,
            final Collection<Effect> outputResources) {
        if (tryToMakeAction(player).equals(HasAction.NO_ACTION_POSSIBLE) || !hut.actionHut(player)) {
            return ActionResult.FAILURE;
        }
        return ActionResult.ACTION_DONE;
    }

    @Override
    public boolean skipAction(final Player player) {
        return hut.placeOnHut(player);
    }

    @Override
    public HasAction tryToMakeAction(final Player player) {
        if (hut.tryToMakeActionHut(player)) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public boolean newTurn() {
        return hut.newTurn();
    }

    @Override
    public String state() {
        return "";
    }
}
