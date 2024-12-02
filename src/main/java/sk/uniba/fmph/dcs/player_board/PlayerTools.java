package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.OptionalInt;

public final class PlayerTools {
    private static final int MAX_TOOL_VALUE = 4;
    private static final int NUMBER_OF_TOOL_PLACES = 3;
    private final ArrayList<Integer> tools;
    private final ArrayList<Boolean> usedTools;
    private final ArrayList<Integer> singleUseTools;
    private final ArrayList<Boolean> usedSingleUseTools;
    private int indexToIncrement = 0;

    public PlayerTools() {
        this.tools = new ArrayList<>(NUMBER_OF_TOOL_PLACES);
        this.usedTools = new ArrayList<>(NUMBER_OF_TOOL_PLACES);
        this.singleUseTools = new ArrayList<>();
        this.usedSingleUseTools = new ArrayList<>();
    }

    public void newTurn() {
        for (int i = 0; i < usedTools.size(); i++) {
            if (usedTools.get(i)) {
                usedTools.set(i, false);
            }
        }
    }

    public void addTool() {
        if (tools.size() < NUMBER_OF_TOOL_PLACES) {
            tools.add(1);
            usedTools.add(false);
        } else if (tools.get(indexToIncrement) < MAX_TOOL_VALUE) {
            tools.set(indexToIncrement, tools.get(indexToIncrement) + 1);
            indexToIncrement = (indexToIncrement + 1) % NUMBER_OF_TOOL_PLACES;
        } else {
            throw new IllegalStateException("All tools values equals MAX_TOOL_VALUE");
        }
    }

    public void addSingleUseTool(final int strength) {
        if (strength <= 1 || strength > MAX_TOOL_VALUE) {
            throw new IllegalArgumentException("Strength is not from interval <2, 4>");
        } else {
            singleUseTools.add(strength);
            usedSingleUseTools.add(false);
        }
    }

    public OptionalInt useTool(final int index) {
        if (index >= NUMBER_OF_TOOL_PLACES + singleUseTools.size()) {
            return OptionalInt.empty();
        } else if (tools.size() <= index && index < NUMBER_OF_TOOL_PLACES) {
            return OptionalInt.empty();
        } else if (index < NUMBER_OF_TOOL_PLACES) {
            if (!usedTools.get(index)) {
                usedTools.set(index, true);
                return OptionalInt.of(tools.get(index));
            }
            return OptionalInt.empty();
        } else if (!usedSingleUseTools.get(index - NUMBER_OF_TOOL_PLACES)) {
            usedSingleUseTools.set(index - NUMBER_OF_TOOL_PLACES, true);
            return OptionalInt.of(singleUseTools.get(index - NUMBER_OF_TOOL_PLACES));
        }
        return OptionalInt.empty();
    }

    public boolean hasSufficientTools(final int goal) {
        int sum = 0;
        for (int i = 0; i < tools.size(); i++) {
            if (!usedTools.get(i)) {
                sum += tools.get(i);
            }
            if (sum >= goal) {
                return true;
            }
        }

        for (int i = 0; i < singleUseTools.size(); i++) {
            if (!usedSingleUseTools.get(i)) {
                sum += singleUseTools.get(i);
            }
            if (sum >= goal) {
                return true;
            }
        }
        return false;
    }

    public String state() {
        Map<String, String> state = Map.of(
                "tools", tools.toString(),
                "singleUseTools", singleUseTools.toString(),
                "usedTools", usedTools.toString(),
                "usedSingleUseTools", usedSingleUseTools.toString()
        );
        return new JSONObject(state).toString();
    }
}
