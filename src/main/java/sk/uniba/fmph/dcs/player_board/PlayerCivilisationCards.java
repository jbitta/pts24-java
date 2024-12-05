package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class PlayerCivilisationCards {
    private final Map<EndOfGameEffect, Integer> endOfGameEffects;
    private final ArrayList<EndOfGameEffect> greenBgCarts = new ArrayList<>(Arrays.asList(EndOfGameEffect.MEDICINE,
            EndOfGameEffect.ART, EndOfGameEffect.WRITING, EndOfGameEffect.POTTERY, EndOfGameEffect.SUNDIAL,
            EndOfGameEffect.TRANSPORT, EndOfGameEffect.MUSIC, EndOfGameEffect.WEAVING));

    public PlayerCivilisationCards() {
        endOfGameEffects = new HashMap<>();
        endOfGameEffects.put(EndOfGameEffect.FARMER, 0);
        endOfGameEffects.put(EndOfGameEffect.TOOL_MAKER, 0);
        endOfGameEffects.put(EndOfGameEffect.BUILDER, 0);
        endOfGameEffects.put(EndOfGameEffect.SHAMAN, 0);
        endOfGameEffects.put(EndOfGameEffect.MEDICINE, 0);
        endOfGameEffects.put(EndOfGameEffect.ART, 0);
        endOfGameEffects.put(EndOfGameEffect.MUSIC, 0);
        endOfGameEffects.put(EndOfGameEffect.WRITING, 0);
        endOfGameEffects.put(EndOfGameEffect.SUNDIAL, 0);
        endOfGameEffects.put(EndOfGameEffect.POTTERY, 0);
        endOfGameEffects.put(EndOfGameEffect.TRANSPORT, 0);
        endOfGameEffects.put(EndOfGameEffect.WEAVING, 0);

    }

    public void addEndOfGameEffects(final Collection<EndOfGameEffect> effects) {
        for (EndOfGameEffect effect : effects) {
            switch (effect) {
            case FARMER:
            case TOOL_MAKER:
            case BUILDER:
            case SHAMAN:
                endOfGameEffects.put(effect, endOfGameEffects.get(effect) + 1);
                break;
            case MEDICINE:
            case ART:
            case MUSIC:
            case WRITING:
            case SUNDIAL:
            case POTTERY:
            case TRANSPORT:
            case WEAVING:
                if (endOfGameEffects.get(effect) < 2) {
                    endOfGameEffects.put(effect, endOfGameEffects.get(effect) + 1);
                } else {
                    throw new IllegalArgumentException("Cannot have more than 2 " + effect);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown EndOfGameEffect: " + effect);
            }
        }
    }

    public int calculateEndOfGameCivilisationCardPoints(final int buildings, final int tools, final int fields,
            final int figures) {
        int sumOfDifferentGreenCarts1 = 0;
        int sumOfDifferentGreenCarts2 = 0;
        for (EndOfGameEffect effect : greenBgCarts) {
            if (endOfGameEffects.get(effect) == 1) {
                sumOfDifferentGreenCarts1++;
            } else if (endOfGameEffects.get(effect) == 2) {
                sumOfDifferentGreenCarts1++;
                sumOfDifferentGreenCarts2++;
            }
        }
        int points = (int) Math.pow(sumOfDifferentGreenCarts1, 2) + (int) Math.pow(sumOfDifferentGreenCarts2, 2);

        points += endOfGameEffects.get(EndOfGameEffect.FARMER) * fields;
        points += endOfGameEffects.get(EndOfGameEffect.TOOL_MAKER) * tools;
        points += endOfGameEffects.get(EndOfGameEffect.BUILDER) * buildings;
        points += endOfGameEffects.get(EndOfGameEffect.SHAMAN) * figures;

        return points;
    }

    public String state() {
        Map<EndOfGameEffect, String> jsonEndOfGameEffects = new HashMap<>(endOfGameEffects.size());
        for (Map.Entry<EndOfGameEffect, Integer> entry : endOfGameEffects.entrySet()) {
            jsonEndOfGameEffects.put(entry.getKey(), entry.getValue().toString());
        }

        Map<String, Map<EndOfGameEffect, String>> state = Map.of("endOfGameEffects", jsonEndOfGameEffects);
        return new JSONObject(state).toString();
    }
}
