package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;

import java.util.Collection;
import java.util.Map;
import java.util.OptionalInt;

public final class PlayerBoard implements InterfaceGetState {
    private int points;
    private int houses;
    private static final int POINTS_TO_TEKE_IF_TRIBE_IS_NOT_FED = -10;
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
        Map<String, String> state = Map.of("points", String.valueOf(points), "houses", String.valueOf(houses));
        return new JSONObject(state).toString();
    }

    public boolean feedTribeIfEnoughFood() {
        return tribeFedStatus.feedTribeIfEnoughFood();
    }

    public boolean feedTribe(final Collection<Effect> resources) {
        return tribeFedStatus.feedTribe(resources);
    }

    public boolean doNotFeedThisTurn() {
        boolean ret = tribeFedStatus.setTribeFed();
        if (!ret) {
            points += POINTS_TO_TEKE_IF_TRIBE_IS_NOT_FED;
        }
        return ret;
    }

    public boolean isTribeFed() {
        return tribeFedStatus.isTribeFed();
    }

    public void newTurn() {
        tribeFedStatus.newTurn();
        playerTools.newTurn();
        playerFigures.newTurn();
    }

    public void giveEffect(final Collection<Effect> stuff) {
        playerResourcesAndFood.giveResources(stuff);
    }

    public void giveEndOfGameEffect(final Collection<EndOfGameEffect> stuff) {
        playerCivilisationCards.addEndOfGameEffects(stuff);
    }

    public boolean takeResources(final Collection<Effect> stuff) {
        return playerResourcesAndFood.takeResources(stuff);
    }

    public boolean takeFigures(final int count) {
        return playerFigures.takeFigures(count);
    }

    public void giveFigures(final int count) {
        while (!playerFigures.hasFigures(count)) {
            playerFigures.addNewFigure();
        }
    }

    public boolean hasFigures(final int count) {
        return playerFigures.hasFigures(count);
    }

    public boolean hasSufficientTools(final int goal) {
        return playerTools.hasSufficientTools(goal);
    }

    public OptionalInt useTool(final int idx) {
        return playerTools.useTool(idx);
    }

    public void takePoints(final int points) {
        addPoints(-points);
    }

    public void givePoints(final int points) {
        addPoints(points);
    }
}
