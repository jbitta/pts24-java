package sk.uniba.fmph.dcs.player_board;

import static org.junit.Assert.*;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;

import java.util.Collections;
import java.util.List;

public class PlayerBoardIntegrationTest {
    @Test
    public void testPlayerBoardTribeFeeding() {
        PlayerBoard playerBoard = PlayerBoardFactory.createPlayerBoard();

        assertTrue(playerBoard.playerResourcesAndFood().hasResources(Collections.nCopies(12, Effect.FOOD)));
        assertFalse(playerBoard.playerResourcesAndFood().hasResources(Collections.nCopies(13, Effect.FOOD)));

        assertTrue(playerBoard.playerFigures().hasFigures(5));
        assertFalse(playerBoard.playerFigures().hasFigures(6));

        assertTrue(playerBoard.tribeFedStatus().feedTribeIfEnoughFood());
        playerBoard.newTurn();
        assertTrue(playerBoard.tribeFedStatus().feedTribeIfEnoughFood());
        playerBoard.newTurn();
        assertFalse(playerBoard.tribeFedStatus().feedTribeIfEnoughFood());

        playerBoard.playerResourcesAndFood().giveResources(Collections.nCopies(3, Effect.FOOD));
        assertTrue(playerBoard.tribeFedStatus().feedTribeIfEnoughFood());
        playerBoard.newTurn();

        playerBoard.playerFigures().addNewFigure();
        playerBoard.newTurn();
        playerBoard.playerFigures().addNewFigure();
        playerBoard.newTurn();

        for (int i = 1; i < 7; ++i) {
            assertFalse(playerBoard.tribeFedStatus().feedTribe(Collections.nCopies(i, Effect.FOOD)));
        }
        assertFalse(playerBoard.tribeFedStatus().feedTribe(Collections.nCopies(7, Effect.FOOD)));
        playerBoard.playerResourcesAndFood().giveResources(Collections.nCopies(7, Effect.FOOD));
        assertTrue(playerBoard.tribeFedStatus().feedTribe(Collections.nCopies(7, Effect.FOOD)));
    }

    @Test
    public void testPlayerBoardFinalPoints() {
        PlayerBoard playerBoard = PlayerBoardFactory.createPlayerBoard();

        assertEquals(0, playerBoard.getPoints());

        playerBoard.playerResourcesAndFood().giveResources(List.of(
                Effect.GOLD,
                Effect.FOOD,
                Effect.FOOD,
                Effect.CLAY,
                Effect.STONE,
                Effect.FOOD,
                Effect.STONE
        ));

        // jedlo sa nerata do bodov za resources
        assertEquals(4, playerBoard.getPoints());

        playerBoard.playerCivilisationCards().addEndOfGameEffects(List.of(
                EndOfGameEffect.FARMER,
                EndOfGameEffect.FARMER,
                EndOfGameEffect.BUILDER,
                EndOfGameEffect.ART,
                EndOfGameEffect.MUSIC,
                EndOfGameEffect.WRITING,
                EndOfGameEffect.SHAMAN,
                EndOfGameEffect.SHAMAN,
                EndOfGameEffect.TOOL_MAKER,
                EndOfGameEffect.TOOL_MAKER
        ));

        // 4 za resources, 9 za ART, MUSIC, WRITING, 10 za dvoch SHAMAN (hrac ma 5 figurok)
        // ostatne civilizacne karty sa rataju za 0, lebo hrac nema ziadne tools, fields
        assertEquals(4 + 9 + 10, playerBoard.getPoints());

        playerBoard.tribeFedStatus().addField();
        playerBoard.tribeFedStatus().addField();

        // 23 su predosle body, 4 za dvoch FARMERov (hrac ma 2 polia)
        assertEquals(23 + 4, playerBoard.getPoints());

        playerBoard.playerTools().addTool();
        playerBoard.playerTools().addTool();
        playerBoard.playerTools().addTool();
        playerBoard.playerTools().addTool();

        // 27 za predosle body, 8 za dvoch TOOL_MAKERov (hrac ma 4 tools)
        assertEquals(27 + 8, playerBoard.getPoints());

        playerBoard.addHouse();
        playerBoard.addHouse();
        playerBoard.addHouse();

        // 35 za predosle body, 3 za BULIDERa (hrac ma 3 buildings)
        assertEquals(35 + 3, playerBoard.getPoints());

        playerBoard.playerCivilisationCards().addEndOfGameEffects(List.of(
                EndOfGameEffect.ART,
                EndOfGameEffect.MEDICINE,
                EndOfGameEffect.POTTERY,
                EndOfGameEffect.WRITING
        ));

        // 29 su predosle body okrem bodov za civilizacne karty
        // 25 za novy set civilizacnych kariet ART, MUSIC, WRITING, MEDICINE, POTTERY
        // 4 za druhy set kariet ART, WRITING
        assertEquals(29 + 25 + 4, playerBoard.getPoints());

        // hrac nechcel nakrmit kmen
        playerBoard.takePoints(PlayerBoard.POINTS_TO_TAKE_IF_TRIBE_IS_NOT_FED);
        assertEquals(58 - 10, playerBoard.getPoints());
    }
}
