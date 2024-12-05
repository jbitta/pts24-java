package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class PlayerCivilisationCardsTest {

    @Test
    public void testAddEndOfGameEffects() {
        PlayerCivilisationCards playerCards = new PlayerCivilisationCards();

        playerCards.addEndOfGameEffects(List.of(EndOfGameEffect.MEDICINE, EndOfGameEffect.MEDICINE));

        assertThrows(IllegalArgumentException.class,
                () -> playerCards.addEndOfGameEffects(List.of(EndOfGameEffect.MEDICINE)));

        JSONObject state = new JSONObject(playerCards.state());
        assertEquals("2", state.getJSONObject("endOfGameEffects").getString("MEDICINE"));

        playerCards.addEndOfGameEffects(List.of(EndOfGameEffect.FARMER, EndOfGameEffect.FARMER, EndOfGameEffect.FARMER,
                EndOfGameEffect.FARMER));

        state = new JSONObject(playerCards.state());
        assertEquals("4", state.getJSONObject("endOfGameEffects").getString("FARMER"));
    }

    @Test
    public void testCalculateEndOfGameCivilisationCardPoints() {
        PlayerCivilisationCards playerCards = new PlayerCivilisationCards();
        playerCards.addEndOfGameEffects(List.of(EndOfGameEffect.FARMER, EndOfGameEffect.TOOL_MAKER,
                EndOfGameEffect.BUILDER, EndOfGameEffect.SHAMAN, EndOfGameEffect.MEDICINE));

        int buildings = 1;
        int tools = 2;
        int fields = 5;
        int figures = 4;

        assertEquals(13, playerCards.calculateEndOfGameCivilisationCardPoints(buildings, tools, fields, figures));
    }

    @Test
    public void testState() {
        PlayerCivilisationCards playerCards = new PlayerCivilisationCards();
        playerCards.addEndOfGameEffects(List.of(EndOfGameEffect.FARMER, EndOfGameEffect.ART, EndOfGameEffect.MUSIC));

        JSONObject state = new JSONObject(playerCards.state());
        assertEquals("1", state.getJSONObject("endOfGameEffects").getString("FARMER"));
        assertEquals("1", state.getJSONObject("endOfGameEffects").getString("ART"));
        assertEquals("1", state.getJSONObject("endOfGameEffects").getString("MUSIC"));

        playerCards.addEndOfGameEffects(List.of(EndOfGameEffect.FARMER));
        state = new JSONObject(playerCards.state());
        assertEquals("2", state.getJSONObject("endOfGameEffects").getString("FARMER"));
    }

}
