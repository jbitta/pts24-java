package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.ArrayList;
import java.util.Map;

public final class ToolMakerHutFields {
    private final ArrayList<PlayerOrder> toolMakerFigures;
    private final ArrayList<PlayerOrder> hutFigures;
    private final ArrayList<PlayerOrder> fieldsFigures;
    private final int restriction;
    private final int lessThenFourRestriction = 3;
    private final int four = 4;

    public ToolMakerHutFields(final int playerCount) {
        toolMakerFigures = new ArrayList<>();
        hutFigures = new ArrayList<>();
        fieldsFigures = new ArrayList<>();
        restriction = playerCount;
    }

    private boolean restrictionViolated() {
        int occupiedLocations = 0;
        if (!toolMakerFigures.isEmpty()) {
            occupiedLocations++;
        }
        if (!hutFigures.isEmpty()) {
            occupiedLocations++;
        }
        if (!fieldsFigures.isEmpty()) {
            occupiedLocations++;
        }
        if (restriction == four) {
            return false;
        }
        return occupiedLocations < lessThenFourRestriction;
    }

    public boolean placeOnToolMaker(final Player player) {
        if (canPlaceOnToolMaker(player)) {
            toolMakerFigures.add(player.playerOrder());
            return true;
        }
        return false;
    }

    public boolean actionToolMaker(final Player player) {
        if (toolMakerFigures.contains(player.playerOrder())) {
            ArrayList<Effect> addTool = new ArrayList<>();
            addTool.add(Effect.TOOL);
            player.playerBoard().giveEffect(addTool);
            return true;
        }
        return false;
    }

    public boolean canPlaceOnToolMaker(final Player player) {
        if (restrictionViolated()) {
            return false;
        }
        return toolMakerFigures.isEmpty();
    }

    public boolean placeOnHut(final Player player) {
        if (canPlaceOnHut(player)) {
            hutFigures.add(player.playerOrder());
            hutFigures.add(player.playerOrder());
            return true;
        }
        return false;
    }

    public boolean actionHut(final Player player) {
        if (hutFigures.contains(player.playerOrder())) {
            player.playerBoard().giveFigures(1);
            return true;
        }
        return false;
    }

    public boolean canPlaceOnHut(final Player player) {
        if (restrictionViolated()) {
            return false;
        }
        return hutFigures.isEmpty();
    }

    public boolean placeOnFields(final Player player) {
        if (canPlaceOnFields(player)) {
            fieldsFigures.add(player.playerOrder());
            return true;
        }
        return false;
    }

    public boolean actionFields(final Player player) {
        if (fieldsFigures.contains(player.playerOrder())) {
            ArrayList<Effect> increaseAgricultureLevel = new ArrayList<>();
            increaseAgricultureLevel.add(Effect.FIELD);
            player.playerBoard().giveEffect(increaseAgricultureLevel);
            return true;
        }
        return false;
    }

    public boolean canPlaceOnFields(final Player player) {
        if (restrictionViolated()) {
            return false;
        }
        return fieldsFigures.isEmpty();
    }

    public boolean newTurn() {
        return toolMakerFigures.isEmpty() && hutFigures.isEmpty() && fieldsFigures.isEmpty();
    }

    String state() {
        Map<String, String> state = Map.of("tool maker figures", toolMakerFigures.toString(), "hut figures",
                hutFigures.toString(), "field figures", fieldsFigures.toString());
        return new JSONObject(state).toString();
    }
}
