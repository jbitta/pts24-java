package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        JSONObject state = new JSONObject(hut.state());
        ArrayList<PlayerOrder> hutFigures = new ArrayList<>(List.of(player.playerOrder(), player.playerOrder()));
        return !state.get("hutFigures").equals(hutFigures.toString());
    }

    @Override
    public HasAction tryToMakeAction(final Player player) {
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public boolean newTurn() {
        return hut.newTurn();
    }
}
