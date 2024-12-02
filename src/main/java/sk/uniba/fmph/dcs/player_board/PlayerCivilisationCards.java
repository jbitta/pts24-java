package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class PlayerCivilisationCards {
    private Map<EndOfGameEffect, Integer> endOfGameEffects;
    private final ArrayList<EndOfGameEffect> greenBgCarts = new ArrayList<>(Arrays.asList(EndOfGameEffect.MEDICINE,
            EndOfGameEffect.ART, EndOfGameEffect.WRITING, EndOfGameEffect.POTTERY, EndOfGameEffect.SUNDIAL,
            EndOfGameEffect.TRANSPORT, EndOfGameEffect.MUSIC, EndOfGameEffect.WEAVING));
    private final ArrayList<EndOfGameEffect> sandBgCarts = new ArrayList<>(Arrays.asList(EndOfGameEffect.FARMER,
            EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.BUILDER, EndOfGameEffect.SHAMAN));

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

    public void addEndOfGameEffects(final EndOfGameEffect effect) {
        switch (effect) {
        case FARMER:
            endOfGameEffects.put(EndOfGameEffect.FARMER, endOfGameEffects.get(EndOfGameEffect.FARMER) + 1);
            break;
        case TOOL_MAKER:
            endOfGameEffects.put(EndOfGameEffect.TOOL_MAKER, endOfGameEffects.get(EndOfGameEffect.TOOL_MAKER) + 1);
            break;
        case BUILDER:
            endOfGameEffects.put(EndOfGameEffect.BUILDER, endOfGameEffects.get(EndOfGameEffect.BUILDER) + 1);
            break;
        case SHAMAN:
            endOfGameEffects.put(EndOfGameEffect.SHAMAN, endOfGameEffects.get(EndOfGameEffect.SHAMAN) + 1);
            break;
        case MEDICINE:
            if (endOfGameEffects.get(EndOfGameEffect.MEDICINE) < 2) {
                endOfGameEffects.put(EndOfGameEffect.MEDICINE, endOfGameEffects.get(EndOfGameEffect.MEDICINE) + 1);
            } else {
                throw new IllegalArgumentException("Cannot have more than 2 " + effect);
            }
            break;
        case ART:
            if (endOfGameEffects.get(EndOfGameEffect.ART) < 2) {
                endOfGameEffects.put(EndOfGameEffect.ART, endOfGameEffects.get(EndOfGameEffect.ART) + 1);
            } else {
                throw new IllegalArgumentException("Cannot have more than 2 " + effect);
            }
            break;
        case MUSIC:
            if (endOfGameEffects.get(EndOfGameEffect.MUSIC) < 2) {
                endOfGameEffects.put(EndOfGameEffect.MUSIC, endOfGameEffects.get(EndOfGameEffect.MUSIC) + 1);
            } else {
                throw new IllegalArgumentException("Cannot have more than 2 " + effect);
            }
            break;
        case WRITING:
            if (endOfGameEffects.get(EndOfGameEffect.WRITING) < 2) {
                endOfGameEffects.put(EndOfGameEffect.WRITING, endOfGameEffects.get(EndOfGameEffect.WRITING) + 1);
            } else {
                throw new IllegalArgumentException("Cannot have more than 2 " + effect);
            }
            break;
        case SUNDIAL:
            if (endOfGameEffects.get(EndOfGameEffect.SUNDIAL) < 2) {
                endOfGameEffects.put(EndOfGameEffect.SUNDIAL, endOfGameEffects.get(EndOfGameEffect.SUNDIAL) + 1);
            } else {
                throw new IllegalArgumentException("Cannot have more than 2 " + effect);
            }
            break;
        case POTTERY:
            if (endOfGameEffects.get(EndOfGameEffect.POTTERY) < 2) {
                endOfGameEffects.put(EndOfGameEffect.POTTERY, endOfGameEffects.get(EndOfGameEffect.POTTERY) + 1);
            } else {
                throw new IllegalArgumentException("Cannot have more than 2 " + effect);
            }
            break;
        case TRANSPORT:
            if (endOfGameEffects.get(EndOfGameEffect.TRANSPORT) < 2) {
                endOfGameEffects.put(EndOfGameEffect.TRANSPORT, endOfGameEffects.get(EndOfGameEffect.TRANSPORT) + 1);
            } else {
                throw new IllegalArgumentException("Cannot have more than 2 " + effect);
            }
            break;
        case WEAVING:
            if (endOfGameEffects.get(EndOfGameEffect.WEAVING) < 2) {
                endOfGameEffects.put(EndOfGameEffect.WEAVING, endOfGameEffects.get(EndOfGameEffect.WEAVING) + 1);
            } else {
                throw new IllegalArgumentException("Cannot have more than 2 " + effect);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown EndOfGameEffect: " + effect);
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
