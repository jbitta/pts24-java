package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerFiguresTest {
    @Test
    public void testInit() {
        PlayerFigures figures = new PlayerFigures();
        assertEquals(5, figures.getTotalFigures());

        for (int i = 1; i <= 5; ++i) {
            assertTrue(figures.hasFigures(i));
        }

        for (int i = 6; i <= 10; ++i) {
            assertFalse(figures.hasFigures(i));
        }

        JSONObject state = new JSONObject(figures.state());
        assertEquals("5", state.getString("totalFigures"));
        assertEquals("5", state.getString("figures"));
    }

    @Test
    public void testAddFiguresOnlyOncePerTurn() {
        PlayerFigures figures = new PlayerFigures();

        JSONObject state = new JSONObject(figures.state());
        assertEquals("5", state.getString("totalFigures"));
        assertEquals("5", state.getString("figures"));

        figures.addNewFigure();
        assertEquals(6, figures.getTotalFigures());

        figures.addNewFigure();
        assertEquals(6, figures.getTotalFigures());

        figures.addNewFigure();
        figures.addNewFigure();
        figures.addNewFigure();
        assertEquals(6, figures.getTotalFigures());

        state = new JSONObject(figures.state());
        assertEquals("6", state.getString("totalFigures"));
        assertEquals("5", state.getString("figures"));

        figures.newTurn();
        state = new JSONObject(figures.state());
        assertEquals("6", state.getString("totalFigures"));
        assertEquals("6", state.getString("figures"));
    }

    @Test
    public void testAddFiguresAfter10HasNoEffect() {
        PlayerFigures figures = new PlayerFigures();

        for (int i = 0; i < 5; ++i) {
            figures.addNewFigure();
            figures.newTurn();
        }

        assertEquals(10, figures.getTotalFigures());
        for (int i = 0; i < 10; ++i) {
            figures.addNewFigure();
            figures.newTurn();
        }

        assertEquals(10, figures.getTotalFigures());
    }

    @Test
    public void testCantTakeFiguresWhenAskingForTooMuch() {
        {
            PlayerFigures figures = new PlayerFigures();
            assertTrue(figures.takeFigures(5));
            assertFalse(figures.takeFigures(1));
        }

        {
            PlayerFigures figures = new PlayerFigures();
            for (int i = 6; i <= 10; ++i) {
                assertFalse(figures.takeFigures(i));
            }
        }

        {
            PlayerFigures figures = new PlayerFigures();
            for (int i = 0; i < 5; ++i) {
                figures.addNewFigure();
                figures.newTurn();
            }
            assertFalse(figures.takeFigures(11));
        }
    }
}
