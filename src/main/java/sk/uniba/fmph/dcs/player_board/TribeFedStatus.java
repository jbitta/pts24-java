package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TribeFedStatus {
    private static final int TRIBE_MAX_FIELDS = 10;

    private boolean tribeFed = false;
    private int fields = 0;
    private final PlayerResourcesAndFood playerResourcesAndFood;
    private final PlayerFigures playerFigures;
    private boolean foodHarvested = false;

    public TribeFedStatus(final PlayerResourcesAndFood playerResourcesAndFood, final PlayerFigures playerFigures) {
        this.playerResourcesAndFood = playerResourcesAndFood;
        this.playerFigures = playerFigures;
    }

    public int getFields() {
        return fields;
    }

    public void addField() {
        if (fields < TRIBE_MAX_FIELDS) {
            fields++;
        }
    }

    public void newTurn() {
        harvestFood();
        tribeFed = false;
        foodHarvested = false;
    }

    private void harvestFood() {
        if (!foodHarvested) {
            playerResourcesAndFood.giveResources(Collections.nCopies(fields, Effect.FOOD));
            foodHarvested = true;
        }
    }

    public boolean feedTribeIfEnoughFood() {
        if (isTribeFed()) {
            return true;
        }

        harvestFood();

        int figures = playerFigures.getTotalFigures();
        Collection<Effect> requiredFood = Collections.nCopies(figures, Effect.FOOD);
        if (playerResourcesAndFood.hasResources(requiredFood)) {
            tribeFed = true;
            return playerResourcesAndFood.takeResources(requiredFood);
        }
        return false;
    }

    public boolean feedTribe(final Collection<Effect> resources) {
        harvestFood();

        if (isTribeFed()) {
            return true;
        }

        if (!playerResourcesAndFood.hasResources(resources)) {
            return false;
        }

        int foodCount = 0;
        int resourceCount = 0;
        for (Effect effect : resources) {
            if (effect == Effect.FOOD) {
                foodCount++;
            } else if (effect.isResource()) {
                resourceCount++;
            }
        }

        int requiredFoodCount = playerFigures.getTotalFigures();
        if (foodCount >= requiredFoodCount) {
            tribeFed = true;
            playerResourcesAndFood.takeResources(Collections.nCopies(requiredFoodCount, Effect.FOOD));
            return true;
        }

        if (foodCount + resourceCount < requiredFoodCount) {
            return false;
        }

        // return false if player has more food
        if (playerResourcesAndFood.hasResources(Collections.nCopies(foodCount + 1, Effect.FOOD))) {
            return false;
        }

        // use all food
        playerResourcesAndFood.takeResources(Collections.nCopies(foodCount, Effect.FOOD));
        // use necessary resources
        playerResourcesAndFood.takeResources(resources.stream().filter(Effect::isResource)
                .limit(requiredFoodCount - foodCount).collect(Collectors.toList()));

        tribeFed = true;
        return true;
    }

    public boolean setTribeFed() {
        harvestFood();

        if (isTribeFed()) {
            return true;
        }

        if (feedTribeIfEnoughFood()) {
            return true;
        }

        // use all food
        while (playerResourcesAndFood.hasResources(List.of(Effect.FOOD))) {
            playerResourcesAndFood.takeResources(List.of(Effect.FOOD));
        }

        tribeFed = true;
        return false;
    }

    public boolean isTribeFed() {
        return tribeFed;
    }

    public String state() {
        Map<String, String> state = Map.of("tribeFed", String.valueOf(tribeFed), "fields", String.valueOf(fields));
        return new JSONObject(state).toString();
    }
}
