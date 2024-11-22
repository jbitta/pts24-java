package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.OptionalInt;

public final class VariableBuilding implements Building {
    private final int numberOfResources;
    private final int numberOfResourcesTypes;

    public VariableBuilding(final int numberOfResources, final int numberOfResourcesTypes) {
        this.numberOfResources = numberOfResources;
        this.numberOfResourcesTypes = numberOfResourcesTypes;
        if (numberOfResources <= 0 || numberOfResourcesTypes <= 0) {
            throw new IllegalArgumentException("numberOfResources/numberOfResourceTypes must be greater than 0");
        }
        if (numberOfResourcesTypes > numberOfResources) {
            throw new IllegalArgumentException("numberOfResourceTypes must be less or equal than numberOfResources");
        }
    }

    @Override
    public OptionalInt build(final Collection<Effect> resources) {
        if (resources.size() != numberOfResources) {
            return OptionalInt.empty();
        }
        int sum = 0;
        Set<Effect> wasResourceType = new HashSet<>();
        for (Effect resource : resources) {
            if (!resource.isResource()) {
                return OptionalInt.empty();
            }
            wasResourceType.add(resource);
            sum += resource.points();
        }
        if (wasResourceType.size() != numberOfResourcesTypes) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(sum);
    }

    @Override
    public String state() {
        Map<String, String> stateMap = Map.of("numberOfResources", Integer.toString(numberOfResources),
                "NumberOfResourceTypes", Integer.toString(numberOfResourcesTypes));
        return new JSONObject(stateMap).toString();
    }
}
