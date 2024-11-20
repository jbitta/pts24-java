package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.PlayerOrder;
import sk.uniba.fmph.dcs.stone_age.InterfaceFigureLocation;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import java.util.Collection;

public final class FigureLocationAdaptor implements InterfaceFigureLocation {
    private final InterfaceFigureLocationInternal internal;
    private final Collection<Player> players;

    public FigureLocationAdaptor(final InterfaceFigureLocationInternal internal, final Collection<Player> players) {
        this.internal = internal;
        this.players = players;
    }

    private Player adaptPlayerOrder(final PlayerOrder player) {
        for (Player p : players) {
            if (p.playerOrder().equals(player)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean placeFigures(final PlayerOrder player, final int figureCount) {
        Player p = adaptPlayerOrder(player);
        if (p == null) {
            return false;
        }
        return internal.placeFigures(p, figureCount);
    }

    @Override
    public HasAction tryToPlaceFigures(final PlayerOrder player, final int count) {
        Player p = adaptPlayerOrder(player);
        if (p == null) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return internal.tryToPlaceFigures(p, count);
    }

    @Override
    public ActionResult makeAction(final PlayerOrder player, final Collection<Effect> inputResources,
            final Collection<Effect> outputResources) {
        Player p = adaptPlayerOrder(player);
        if (p == null) {
            return ActionResult.FAILURE;
        }
        return internal.makeAction(p, inputResources, outputResources);
    }

    @Override
    public boolean skipAction(final PlayerOrder player) {
        Player p = adaptPlayerOrder(player);
        if (p == null) {
            return false;
        }
        return internal.skipAction(p);
    }

    @Override
    public HasAction tryToMakeAction(final PlayerOrder player) {
        Player p = adaptPlayerOrder(player);
        if (p == null) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return internal.tryToMakeAction(p);
    }

    @Override
    public boolean newTurn() {
        return internal.newTurn();
    }
}
