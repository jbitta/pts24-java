package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.ImmediateEffect;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;
import sk.uniba.fmph.dcs.stone_age.Location;
import sk.uniba.fmph.dcs.stone_age.InterfaceFigureLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class GameBoard implements InterfaceGetState {
    private final ArrayList<Player> players;
    private final ToolMakerHutFields toolMakerHutFields;
    private final CurrentThrow currentThrow;
    private final Throw throw1;
    private final CivilizationCardDeck deck;
    private final RewardMenu rewardMenu;
    private final Map<Location, InterfaceFigureLocationInternal> locations;
    private static final int REQUIRED_RESOURCES_CARD4 = 1;
    private static final int REQUIRED_RESOURCES_CARD3 = 2;
    private static final int REQUIRED_RESOURCES_CARD2 = 3;
    private static final int REQUIRED_RESOURCES_CARD1 = 4;
    private static final int NUMBER_BUILDING_TILES = 4;

    public GameBoard(final CivilizationCardDeck deck, final ArrayList<Building> buildings,
            final ArrayList<Player> players, final int desiredResultThrow) {
        this.deck = deck;
        this.players = players;
        throw1 = new Throw(desiredResultThrow);
        currentThrow = new CurrentThrow(throw1);
        rewardMenu = new RewardMenu(players);
        toolMakerHutFields = new ToolMakerHutFields(players.size());

        locations = new HashMap<>();
        locations.put(Location.TOOL_MAKER, new PlaceOnToolMakerAdaptor(toolMakerHutFields));
        locations.put(Location.HUT, new PlaceOnHutAdaptor(toolMakerHutFields));
        locations.put(Location.FIELD, new PlaceOnFieldsAdaptor(toolMakerHutFields));

        Map<ImmediateEffect, EvaluateCivilisationCardImmediateEffect> evaluate = initializeEvaluationMap();

        locations.put(Location.HUNTING_GROUNDS,
                new ResourceSource("HuntingGrounds", Effect.FOOD, currentThrow, players.size()));
        locations.put(Location.FOREST, new ResourceSource("Forest", Effect.WOOD, currentThrow, players.size()));
        locations.put(Location.CLAY_MOUND, new ResourceSource("ClayMound", Effect.CLAY, currentThrow, players.size()));
        locations.put(Location.QUARRY, new ResourceSource("Quarry", Effect.STONE, currentThrow, players.size()));
        locations.put(Location.RIVER, new ResourceSource("River", Effect.GOLD, currentThrow, players.size()));

        CivilizationCardPlace card4 = new CivilizationCardPlace(deck, deck.getTop(), null, REQUIRED_RESOURCES_CARD4,
                evaluate);
        CivilizationCardPlace card3 = new CivilizationCardPlace(deck, deck.getTop(), card4, REQUIRED_RESOURCES_CARD3,
                evaluate);
        CivilizationCardPlace card2 = new CivilizationCardPlace(deck, deck.getTop(), card3, REQUIRED_RESOURCES_CARD2,
                evaluate);
        CivilizationCardPlace card1 = new CivilizationCardPlace(deck, deck.getTop(), card2, REQUIRED_RESOURCES_CARD1,
                evaluate);
        locations.put(Location.CIVILISATION_CARD4, card4);
        locations.put(Location.CIVILISATION_CARD3, card3);
        locations.put(Location.CIVILISATION_CARD2, card2);
        locations.put(Location.CIVILISATION_CARD1, card1);

        ArrayList<Building> buildings1 = new ArrayList<>();
        ArrayList<Building> buildings2 = new ArrayList<>();
        ArrayList<Building> buildings3 = new ArrayList<>();
        ArrayList<Building> buildings4 = new ArrayList<>();
        for (int i = 0; i < buildings.size(); i++) {
            switch (i % NUMBER_BUILDING_TILES) {
                case 0 -> buildings1.add(buildings.get(i));
                case 1 -> buildings2.add(buildings.get(i));
                case 2 -> buildings3.add(buildings.get(i));
                case NUMBER_BUILDING_TILES - 1 -> buildings4.add(buildings.get(i));
                default -> { }
            }
        }
        locations.put(Location.BUILDING_TILE1, new BuildingTile(buildings1));
        locations.put(Location.BUILDING_TILE2, new BuildingTile(buildings2));
        locations.put(Location.BUILDING_TILE3, new BuildingTile(buildings3));
        locations.put(Location.BUILDING_TILE4, new BuildingTile(buildings4));
    }

    @Override
    public String state() {
        Map<String, String> state = new HashMap<>();
        state.put("AllPlayers", players.toString());
        state.put("ToolMakeHutFields", toolMakerHutFields.state());
        for (Location l : locations.keySet()) {
            if (l.equals(Location.TOOL_MAKER) || l.equals(Location.HUT) || l.equals(Location.FIELD)) {
                continue;
            }
            state.put(l.toString(), locations.get(l).state());
        }
        state.put("CurrentThrow", currentThrow.state());
        state.put("CardDeck", deck.state());
        state.put("RewardMenu", rewardMenu.state());
        return new JSONObject(state).toString();
    }

    private Map<ImmediateEffect, EvaluateCivilisationCardImmediateEffect> initializeEvaluationMap() {
        Map<ImmediateEffect, EvaluateCivilisationCardImmediateEffect> evaluate = new HashMap<>();
        evaluate.put(ImmediateEffect.THROW_WOOD, new GetSomethingThrow(currentThrow, Effect.WOOD));
        evaluate.put(ImmediateEffect.THROW_CLAY, new GetSomethingThrow(currentThrow, Effect.CLAY));
        evaluate.put(ImmediateEffect.THROW_STONE, new GetSomethingThrow(currentThrow, Effect.STONE));
        evaluate.put(ImmediateEffect.THROW_GOLD, new GetSomethingThrow(currentThrow, Effect.GOLD));
        evaluate.put(ImmediateEffect.POINT, new GetSomethingFixed(Effect.POINT));
        evaluate.put(ImmediateEffect.WOOD, new GetSomethingFixed(Effect.WOOD));
        evaluate.put(ImmediateEffect.CLAY, new GetSomethingFixed(Effect.CLAY));
        evaluate.put(ImmediateEffect.STONE, new GetSomethingFixed(Effect.STONE));
        evaluate.put(ImmediateEffect.GOLD, new GetSomethingFixed(Effect.GOLD));
        evaluate.put(ImmediateEffect.FOOD, new GetSomethingFixed(Effect.FOOD));
        evaluate.put(ImmediateEffect.TOOL, new GetSomethingFixed(Effect.TOOL));
        evaluate.put(ImmediateEffect.ONE_TIME_TOOL2, new GetSomethingFixed(Effect.ONE_TIME_TOOL2));
        evaluate.put(ImmediateEffect.ONE_TIME_TOOL3, new GetSomethingFixed(Effect.ONE_TIME_TOOL3));
        evaluate.put(ImmediateEffect.ONE_TIME_TOOL4, new GetSomethingFixed(Effect.ONE_TIME_TOOL4));
        evaluate.put(ImmediateEffect.CARD, new GetCard(deck));
        evaluate.put(ImmediateEffect.ARBITRARY_RESOURCE, new GetChoice());
        evaluate.put(ImmediateEffect.ALL_PLAYERS_TAKE_REWARD, new AllPlayersTakeReward(rewardMenu, throw1));
        return evaluate;
    }

    public RewardMenu getRewardMenu() {
        return rewardMenu;
    }

    public CurrentThrow getCurrentThrow() {
        return currentThrow;
    }

    public Map<Location, InterfaceFigureLocation> getLocationToFigureLocationMap() {
        Map<Location, InterfaceFigureLocation> map = new HashMap<>();
        for (Location l : locations.keySet()) {
            map.put(l, new FigureLocationAdaptor(locations.get(l), players));
        }
        return map;
    }

}
