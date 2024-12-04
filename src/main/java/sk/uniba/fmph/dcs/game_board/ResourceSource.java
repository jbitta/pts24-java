package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.ArrayList;
import java.util.Collection;

public final class ResourceSource implements InterfaceFigureLocationInternal {
    private String name;
    private Effect resource;
    private int maxFigureColours;
    private ArrayList<PlayerOrder> figures;

    public ResourceSource(final String name, final Effect resource, final CurrentThrow currentThrow, final int playerCount) {
    }

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

    @Override
    public String state() {
        return "";
    }
}
