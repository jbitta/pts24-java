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
    private static final int LESS_THAN_FOUR_RESTRICTION = 2;
    private static final int MAX_PLAYERS = 4;
    private static final int MIN_PLAYERS = 2;

    public ToolMakerHutFields(final int playerCount) {
        if (playerCount > MAX_PLAYERS || playerCount < MIN_PLAYERS) {
            throw new IllegalArgumentException("Only 2-4 players");
        }
        toolMakerFigures = new ArrayList<>();
        hutFigures = new ArrayList<>();
        fieldsFigures = new ArrayList<>();
        restriction = playerCount;
    }

    private boolean restrictionViolated() {
        if (restriction == MAX_PLAYERS) {
            return false;
        }
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
        return occupiedLocations == LESS_THAN_FOUR_RESTRICTION;
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
            toolMakerFigures.remove(player.playerOrder());
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
            hutFigures.remove(player.playerOrder());
            hutFigures.remove(player.playerOrder());
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
            fieldsFigures.remove(player.playerOrder());
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
        String restrictionString = "" + restriction;
        Map<String, String> state = Map.of("toolMakerFigures", toolMakerFigures.toString(), "hutFigures",
                hutFigures.toString(), "fieldsFigures", fieldsFigures.toString(), "restriction", restrictionString);
        return new JSONObject(state).toString();
    }
}
