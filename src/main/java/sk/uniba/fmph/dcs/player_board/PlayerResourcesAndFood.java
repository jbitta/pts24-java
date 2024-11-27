package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class PlayerResourcesAndFood {
    private final Map<Effect, Integer> resources = new HashMap<>(Map.of(Effect.FOOD, 12));

    public boolean hasResources(final Collection<Effect> resources) {
        Map<Effect, Integer> requestedResources = new HashMap<>();
        for (Effect resource : resources) {
            if (!resource.isResourceOrFood()) {
                throw new IllegalArgumentException(String.format("'%s' is not food nor a resource", resource));
            }
            int amount = requestedResources.getOrDefault(resource, 0);
            requestedResources.put(resource, amount + 1);
        }

        for (Map.Entry<Effect, Integer> entry : requestedResources.entrySet()) {
            Effect requestedEffect = entry.getKey();
            int requestedAmount = entry.getValue();

            int availableAmount = this.resources.getOrDefault(requestedEffect, 0);
            if (availableAmount < requestedAmount) {
                return false;
            }
        }

        return true;
    }

    public boolean takeResources(final Collection<Effect> resources) {
        if (!hasResources(resources)) {
            return false;
        }

        Map<Effect, Integer> requestedResources = new HashMap<>();
        for (Effect resource : resources) {
            int amount = requestedResources.getOrDefault(resource, 0);
            requestedResources.put(resource, amount + 1);
        }

        for (Effect effect : requestedResources.keySet()) {
            int availableAmount = this.resources.get(effect);
            int requestedAmount = requestedResources.get(effect);
            this.resources.replace(effect, availableAmount - requestedAmount);
        }

        return true;
    }

    public boolean giveResources(final Collection<Effect> resources) {
        for (Effect resource : resources) {
            if (!resource.isResourceOrFood()) {
                throw new IllegalArgumentException(String.format("'%s' is not food nor a resource", resource));
            }
            int amount = this.resources.getOrDefault(resource, 0);
            this.resources.put(resource, amount + 1);
        }

        return true;
    }

    public int numberOfResourcesForFinalPoints() {
        int points = 0;

        for (Map.Entry<Effect, Integer> entry : resources.entrySet()) {
            Effect effect = entry.getKey();
            int amount = entry.getValue();
            points += effect.isResource() ? amount : 0;
        }

        return points;
    }

    public String state() {
        Map<Effect, String> jsonResources = new HashMap<>(resources.size());
        for (Map.Entry<Effect, Integer> entry : resources.entrySet()) {
            jsonResources.put(entry.getKey(), entry.getValue().toString());
        }

        Map<String, Map<Effect, String>> state = Map.of("resources", jsonResources);
        return new JSONObject(state).toString();
    }
}
