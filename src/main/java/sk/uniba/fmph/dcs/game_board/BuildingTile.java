package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Stack;
import java.util.List;

public final class BuildingTile implements InterfaceFigureLocationInternal {
    private Building building;
    private final Stack<Building> buildingStack;
    private final ArrayList<PlayerOrder> figures;
    private static final int STACK_SIZE = 7;

    public BuildingTile(final Collection<Building> buildingCards) {
        buildingStack = new Stack<>();
        buildingStack.addAll(buildingCards);
        if (buildingStack.isEmpty() || buildingStack.size() > STACK_SIZE) {
            throw new IllegalArgumentException("Stack must have 7 buildings");
        }

        building = buildingStack.peek();
        figures = new ArrayList<>();
    }

    @Override
    public boolean placeFigures(final Player player, final int figureCount) {
        if (tryToPlaceFigures(player, figureCount).equals(HasAction.NO_ACTION_POSSIBLE)
                || !player.playerBoard().takeFigures(figureCount)) {
            return false;
        }

        figures.add(player.playerOrder());
        return true;
    }

    @Override
    public HasAction tryToPlaceFigures(final Player player, final int count) {
        if (figures.isEmpty() && player.playerBoard().hasFigures(1) && count == 1 && !buildingStack.isEmpty()) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }

        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public ActionResult makeAction(final Player player, final Collection<Effect> inputResources,
            final Collection<Effect> outputResources) {
        if (tryToMakeAction(player).equals(HasAction.NO_ACTION_POSSIBLE)) {
            return ActionResult.FAILURE;
        }

        OptionalInt pointsFromBuilding = building.build(inputResources);
        if (pointsFromBuilding.isPresent()) {
            if (!player.playerBoard().takeResources(inputResources)) {
                return ActionResult.FAILURE;
            }

            player.playerBoard().givePoints(pointsFromBuilding.getAsInt());
            figures.remove(player.playerOrder());
            player.playerBoard().giveEffect(List.of(Effect.BUILDING));
            buildingStack.pop();
            if (!buildingStack.isEmpty()) {
                building = buildingStack.peek();
            } else {
                building = null;
            }

            return ActionResult.ACTION_DONE;
        }

        return ActionResult.FAILURE;
    }

    @Override
    public boolean skipAction(final Player player) {
        if (tryToMakeAction(player).equals(HasAction.NO_ACTION_POSSIBLE)) {
            return false;
        }

        figures.remove(player.playerOrder());
        return true;
    }

    @Override
    public HasAction tryToMakeAction(final Player player) {
        if (!figures.contains(player.playerOrder()) || buildingStack.isEmpty()) {
            return HasAction.NO_ACTION_POSSIBLE;
        }

        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public boolean newTurn() {
        return buildingStack.isEmpty();
    }

    String state() {
        String buildingStateOrNull = "{}";
        if (!buildingStack.isEmpty()) {
            buildingStateOrNull = building.state();
        }

        Map<String, String> stateMap = Map.of("currentBuilding", buildingStateOrNull, "figures", figures.toString(),
                "numberOfCardsInBuildingTile", Integer.toString(buildingStack.size()));
        return new JSONObject(stateMap).toString();
    }
}
