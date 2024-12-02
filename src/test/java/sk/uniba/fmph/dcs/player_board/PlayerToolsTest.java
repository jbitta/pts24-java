package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import org.junit.Test;
import java.util.OptionalInt;

import static org.junit.Assert.*;

public class PlayerToolsTest {

    @Test
    public void testAddTool() {
        PlayerTools playerTools = new PlayerTools();
        playerTools.addTool();

        JSONObject state = new JSONObject(playerTools.state());

        assertEquals("[1][]", state.getString("tools"));
        assertEquals("[false][]", state.getString("usedTools"));

        playerTools.addTool();
        state = new JSONObject(playerTools.state());
        assertEquals("[1, 1][]", state.getString("tools"));
        assertEquals("[false, false][]", state.getString("usedTools"));

        // Tretí nástroj sa pridá
        playerTools.addTool();
        state = new JSONObject(playerTools.state());
        assertEquals("[1, 1, 1][]", state.getString("tools"));
        assertEquals("[false, false, false][]", state.getString("usedTools"));

        for (int i = 0; i < 9; i++) {
            playerTools.addTool();
        }

        assertThrows(IllegalStateException.class, () -> playerTools.addTool());
    }

    @Test
    public void testUseTool() {
        PlayerTools playerTools = new PlayerTools();
        playerTools.addTool();
        playerTools.addTool();

        OptionalInt result = playerTools.useTool(2);
        assertFalse(result.isPresent());

        playerTools.addTool();

        result = playerTools.useTool(0);
        assertTrue(result.isPresent());
        assertEquals(1, result.getAsInt());

        result = playerTools.useTool(0);
        assertFalse(result.isPresent());

        result = playerTools.useTool(1);
        assertTrue(result.isPresent());
        assertEquals(1, result.getAsInt());

        result = playerTools.useTool(2);
        assertTrue(result.isPresent());
        assertEquals(1, result.getAsInt());

        result = playerTools.useTool(3);
        assertFalse(result.isPresent());

        playerTools.addSingleUseTool(3);
        result = playerTools.useTool(3);
        assertTrue(result.isPresent());
        assertEquals(3, result.getAsInt());

        result = playerTools.useTool(3);
        assertFalse(result.isPresent());
    }

    @Test
    public void testHasSufficientTools() {
        PlayerTools playerTools = new PlayerTools();
        playerTools.addTool();
        playerTools.addTool();
        playerTools.addTool();

        assertTrue(playerTools.hasSufficientTools(3));

        assertFalse(playerTools.hasSufficientTools(5));
    }

    @Test
    public void testNewTurn() {
        PlayerTools playerTools = new PlayerTools();
        playerTools.addTool();
        playerTools.addTool();
        playerTools.addTool();
        playerTools.addSingleUseTool(2);
        playerTools.useTool(0);
        playerTools.useTool(1);
        playerTools.useTool(2);
        playerTools.useTool(3);

        assertFalse(playerTools.useTool(0).isPresent());
        assertFalse(playerTools.useTool(1).isPresent());
        assertFalse(playerTools.useTool(2).isPresent());
        assertFalse(playerTools.useTool(3).isPresent());

        playerTools.newTurn();

        assertTrue(playerTools.useTool(0).isPresent());
        assertTrue(playerTools.useTool(1).isPresent());
        assertTrue(playerTools.useTool(2).isPresent());
        assertFalse(playerTools.useTool(3).isPresent());
    }

    @Test
    public void testState() {
        PlayerTools playerTools = new PlayerTools();
        JSONObject state = new JSONObject(playerTools.state());
        assertEquals("[][]", state.getString("tools"));
        assertEquals("[][]", state.getString("usedTools"));

        playerTools.addTool();
        playerTools.addTool();
        playerTools.addTool();

        state = new JSONObject(playerTools.state());
        assertEquals("[1, 1, 1][]", state.getString("tools"));
        assertEquals("[false, false, false][]", state.getString("usedTools"));

        playerTools.addSingleUseTool(3);
        state = new JSONObject(playerTools.state());
        assertEquals("[1, 1, 1][3]", state.getString("tools"));
        assertEquals("[false, false, false][false]", state.getString("usedTools"));

        playerTools.useTool(3);
        state = new JSONObject(playerTools.state());
        assertEquals("[1, 1, 1][3]", state.getString("tools"));
        assertEquals("[false, false, false][true]", state.getString("usedTools"));

        playerTools.useTool(0);
        playerTools.useTool(1);
        state = new JSONObject(playerTools.state());
        assertEquals("[1, 1, 1][3]", state.getString("tools"));
        assertEquals("[true, true, false][true]", state.getString("usedTools"));

        playerTools.newTurn();
        state = new JSONObject(playerTools.state());
        assertEquals("[1, 1, 1][3]", state.getString("tools"));
        assertEquals("[false, false, false][true]", state.getString("usedTools"));
    }

}
