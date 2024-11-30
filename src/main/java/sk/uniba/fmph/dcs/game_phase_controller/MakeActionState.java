package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.InterfaceFigureLocation;
import sk.uniba.fmph.dcs.stone_age.Location;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.Collection;
import java.util.Map;

public final class MakeActionState implements InterfaceGamePhaseState {
    private final Map<Location, InterfaceFigureLocation> places;

    public MakeActionState(final Map<Location, InterfaceFigureLocation> places) {
        this.places = places;
    }

    @Override
    public ActionResult placeFigures(final PlayerOrder player, final Location location, final int figuresCount) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult makeAction(final PlayerOrder player, final Location location,
            final Collection<Effect> inputResources, final Collection<Effect> outputResources) {
        return places.get(location).makeAction(player, inputResources, outputResources);
    }

    @Override
    public ActionResult skipAction(final PlayerOrder player, final Location location) {
        if (places.get(location).skipAction(player)) {
            return ActionResult.ACTION_DONE;
        }
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult useTools(final PlayerOrder player, final int toolIndex) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult noMoreToolsThisThrow(final PlayerOrder player) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult feedTribe(final PlayerOrder player, final Collection<Effect> resources) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult doNotFeedThisTurn(final PlayerOrder player) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult makeAllPlayersTakeARewardChoice(final PlayerOrder player, final Effect reward) {
        return ActionResult.FAILURE;
    }

    @Override
    public HasAction tryToMakeAutomaticAction(final PlayerOrder player) {
        for (Location location : places.keySet()) {
            if (places.get(location).tryToMakeAction(player) != HasAction.NO_ACTION_POSSIBLE) {
                return places.get(location).tryToMakeAction(player);
            }
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }
}
