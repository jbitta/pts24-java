package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;

public final class GameBoard implements InterfaceGetState {
    private final ToolMakerHutFields toolMakerHutFields;
    private final ResourceSource resourceSource;
    private final CurrentThrow currentThrow;
    private final CivilizationCardPlace civilizationCardPlace;
    private final CivilizationCardDeck civilizationCardDeck;
    private final RewardMenu rewardMenu;
    private final BuildingTile buildingTile;
    public GameBoard(final ToolMakerHutFields toolMakerHutFields,
                     final ResourceSource resourceSource,
                     final CurrentThrow currentThrow,
                     final CivilizationCardPlace civilizationCardPlace,
                     final CivilizationCardDeck civilizationCardDeck,
                     final RewardMenu rewardMenu,
                     final BuildingTile buildingTile) {
        this.toolMakerHutFields = toolMakerHutFields;
        this.resourceSource = resourceSource;
        this.currentThrow = currentThrow;
        this.civilizationCardPlace = civilizationCardPlace;
        this.civilizationCardDeck = civilizationCardDeck;
        this.rewardMenu = rewardMenu;
        this.buildingTile = buildingTile;
    }
    @Override
    public String state() {
        JSONObject state = new JSONObject(toolMakerHutFields.state());
        state.put(new JSONObject(resourceSource.state()));
        state.put(new JSONObject(currentThrow.state()));
        state.put(new JSONObject(civilizationCardPlace.state()));
        state.put(new JSONObject(civilizationCardDeck.state()));
        state.get(new JSONObject(rewardMenu.state()));
        state.put(new JSONObject(buildingTile.state()));
        return state.toString();
    }
}
