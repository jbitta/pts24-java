package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class PlayerBoard implements InterfaceGetState {
    public static final int POINTS_TO_TAKE_IF_TRIBE_IS_NOT_FED = 10;
    private static final int ONE_TIME_TOOL2_STRENGTH = 2;
    private static final int ONE_TIME_TOOL3_STRENGTH = 3;
    private static final int ONE_TIME_TOOL4_STRENGTH = 4;

    private int points;
    private int houses;
    private final PlayerResourcesAndFood playerResourcesAndFood;
    private final TribeFedStatus tribeFedStatus;
    private final PlayerFigures playerFigures;
    private final PlayerCivilisationCards playerCivilisationCards;
    private final PlayerTools playerTools;

    public PlayerBoard() {
        playerResourcesAndFood = new PlayerResourcesAndFood();
        playerFigures = new PlayerFigures();
        playerCivilisationCards = new PlayerCivilisationCards();
        playerTools = new PlayerTools();
        tribeFedStatus = new TribeFedStatus(playerResourcesAndFood, playerFigures);
    }

    public PlayerBoard(final PlayerResourcesAndFood playerResourcesAndFood,
                       final PlayerCivilisationCards playerCivilisationCards, final PlayerFigures playerFigures,
                       final TribeFedStatus tribeFedStatus, final PlayerTools playerTools) {
        this.playerResourcesAndFood = playerResourcesAndFood;
        this.playerCivilisationCards = playerCivilisationCards;
        this.playerFigures = playerFigures;
        this.tribeFedStatus = tribeFedStatus;
        this.playerTools = playerTools;
    }

    public void addPoints(final int points) {
        this.points += points;
    }

    public int getPoints() {
        int resultPoints = points;

        resultPoints += playerCivilisationCards.calculateEndOfGameCivilisationCardPoints(
                houses,
                playerTools.getToolCount(),
                tribeFedStatus.getFields(),
                playerFigures.getTotalFigures()
        );

        resultPoints += playerResourcesAndFood.numberOfResourcesForFinalPoints();

        return resultPoints;
    }

    public void addHouse() {
        houses++;
    }

    public void addEndOfGamePoints() {
        points += playerCivilisationCards.calculateEndOfGameCivilisationCardPoints(houses, playerTools.getToolCount(),
                tribeFedStatus.getFields(), playerFigures.getTotalFigures());
        points += playerResourcesAndFood.numberOfResourcesForFinalPoints();
    }

    @Override
    public String state() {
        Map<String, String> state = Map.of(
                "points", String.valueOf(points),
                "houses", String.valueOf(houses),
                "playerResourcesAndFood", playerResourcesAndFood.state(),
                "tribeFedStatus", tribeFedStatus.state(),
                "playerFigures", playerFigures.state(),
                "playerCivilisationCards", playerCivilisationCards.state(),
                "playerTools", playerTools.state()
        );
        return new JSONObject(state).toString();
    }

    public PlayerResourcesAndFood playerResourcesAndFood() {
        return this.playerResourcesAndFood;
    }

    public TribeFedStatus tribeFedStatus() {
        return this.tribeFedStatus;
    }

    public PlayerFigures playerFigures() {
        return this.playerFigures;
    }

    public PlayerCivilisationCards playerCivilisationCards() {
        return this.playerCivilisationCards;
    }

    public PlayerTools playerTools() {
        return this.playerTools;
    }

    public void giveEffect(final Collection<Effect> stuff) {
        for (Effect effect : stuff) {
            switch (effect) {
                case FOOD, WOOD, CLAY, STONE, GOLD:
                    playerResourcesAndFood.giveResources(List.of(effect));
                    break;
                case TOOL:
                    playerTools.addTool();
                    break;
                case FIELD:
                    tribeFedStatus.addField();
                    break;
                case BUILDING:
                    addHouse();
                    break;
                case ONE_TIME_TOOL2:
                    playerTools.addSingleUseTool(ONE_TIME_TOOL2_STRENGTH);
                    break;
                case ONE_TIME_TOOL3:
                    playerTools.addSingleUseTool(ONE_TIME_TOOL3_STRENGTH);
                    break;
                case ONE_TIME_TOOL4:
                    playerTools.addSingleUseTool(ONE_TIME_TOOL4_STRENGTH);
                    break;
                case POINT:
                    addPoints(1);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Invalid effect '%s'.", effect));
            }
        }
    }

    public void newTurn() {
        tribeFedStatus.newTurn();
        playerTools.newTurn();
        playerFigures.newTurn();
    }

    public void takePoints(final int points) {
        addPoints(-points);
    }
}
