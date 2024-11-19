package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.PlayerOrder;
import sk.uniba.fmph.dcs.stone_age.InterfaceFigureLocation;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import java.util.Collection;

public class FigureLocationAdaptor implements InterfaceFigureLocation {
    private final InterfaceFigureLocationInternal internal;
    private final Collection<Player> players;

    public FigureLocationAdaptor(InterfaceFigureLocationInternal internal, Collection<Player> players) {
        this.internal = internal;
        this.players = players;
    }

    private Player adaptPlayerOrder(PlayerOrder player) {
        for (Player p : players) {
            if (p.playerOrder().equals(player)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean placeFigures(PlayerOrder player, int figureCount) {
        Player p = adaptPlayerOrder(player);
        if (p == null) {
            return false;
        }
        return internal.placeFigures(p, figureCount);
    }

    @Override
    public HasAction tryToPlaceFigures(PlayerOrder player, int count) {
        Player p = adaptPlayerOrder(player);
        if (p == null) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return internal.tryToPlaceFigures(p, count);
    }

    @Override
    public ActionResult makeAction(PlayerOrder player, Collection<Effect> inputResources,
            Collection<Effect> outputResources) {
        Player p = adaptPlayerOrder(player);
        if (p == null) {
            return ActionResult.FAILURE;
        }
        return internal.makeAction(p, inputResources, outputResources);
    }

    @Override
    public boolean skipAction(PlayerOrder player) {
        Player p = adaptPlayerOrder(player);
        if (p == null) {
            return false;
        }
        return internal.skipAction(p);
    }

    @Override
    public HasAction tryToMakeAction(PlayerOrder player) {
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
