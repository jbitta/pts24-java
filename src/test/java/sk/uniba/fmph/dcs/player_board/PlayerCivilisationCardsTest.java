package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import static org.junit.Assert.*;

public class PlayerCivilisationCardsTest {

    @Test
    public void testAddEndOfGameEffects() {
        PlayerCivilisationCards playerCards = new PlayerCivilisationCards();

        playerCards.addEndOfGameEffects(EndOfGameEffect.MEDICINE);
        playerCards.addEndOfGameEffects(EndOfGameEffect.MEDICINE);

        assertThrows(IllegalArgumentException.class, () -> playerCards.addEndOfGameEffects(EndOfGameEffect.MEDICINE));

        JSONObject state = new JSONObject(playerCards.state());
        assertEquals("2", state.getJSONObject("endOfGameEffects").getString("MEDICINE"));

        playerCards.addEndOfGameEffects(EndOfGameEffect.FARMER);
        playerCards.addEndOfGameEffects(EndOfGameEffect.FARMER);
        playerCards.addEndOfGameEffects(EndOfGameEffect.FARMER);
        playerCards.addEndOfGameEffects(EndOfGameEffect.FARMER);

        state = new JSONObject(playerCards.state());
        assertEquals("4", state.getJSONObject("endOfGameEffects").getString("FARMER"));
    }

    @Test
    public void testCalculateEndOfGameCivilisationCardPoints() {
        PlayerCivilisationCards playerCards = new PlayerCivilisationCards();
        playerCards.addEndOfGameEffects(EndOfGameEffect.FARMER); // FARMER = 1
        playerCards.addEndOfGameEffects(EndOfGameEffect.TOOL_MAKER); // TOOL_MAKER = 1
        playerCards.addEndOfGameEffects(EndOfGameEffect.BUILDER); // BUILDER = 1
        playerCards.addEndOfGameEffects(EndOfGameEffect.SHAMAN); // SHAMAN = 1
        playerCards.addEndOfGameEffects(EndOfGameEffect.MEDICINE); // MEDICINE = 1

        int buildings = 1;
        int tools = 2;
        int fields = 5;
        int figures = 4;

        assertEquals(13, playerCards.calculateEndOfGameCivilisationCardPoints(buildings, tools, fields, figures));
    }

    @Test
    public void testState() {
        PlayerCivilisationCards playerCards = new PlayerCivilisationCards();
        playerCards.addEndOfGameEffects(EndOfGameEffect.FARMER);
        playerCards.addEndOfGameEffects(EndOfGameEffect.ART);
        playerCards.addEndOfGameEffects(EndOfGameEffect.MUSIC);

        JSONObject state = new JSONObject(playerCards.state());
        assertEquals("1", state.getJSONObject("endOfGameEffects").getString("FARMER"));
        assertEquals("1", state.getJSONObject("endOfGameEffects").getString("ART"));
        assertEquals("1", state.getJSONObject("endOfGameEffects").getString("MUSIC"));

        playerCards.addEndOfGameEffects(EndOfGameEffect.FARMER);
        state = new JSONObject(playerCards.state());
        assertEquals("2", state.getJSONObject("endOfGameEffects").getString("FARMER"));
    }

}
