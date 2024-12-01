package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.OptionalInt;

import static org.junit.Assert.*;

public class RewardMenuTest {

    private class PlayerBoardMock implements InterfacePlayerBoardGameBoard {

        @Override
        public void giveEffect(Collection<Effect> stuff) {

        }

        @Override
        public void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff) {

        }

        @Override
        public boolean takeResources(Collection<Effect> stuff) {
            return false;
        }

        @Override
        public boolean takeFigures(int count) {
            return false;
        }

        @Override
        public void giveFigures(int count) {

        }

        @Override
        public boolean hasFigures(int count) {
            return false;
        }

        @Override
        public boolean hasSufficientTools(int goal) {
            return false;
        }

        @Override
        public OptionalInt useTool(int idx) {
            return null;
        }

        @Override
        public void takePoints(int points) {

        }

        @Override
        public void givePoints(int points) {

        }
    }

    @Test
    public void testCalculation() {
        PlayerOrder playerOrder1 = new PlayerOrder(0, 2);
        PlayerOrder playerOrder2 = new PlayerOrder(1, 2);
        ArrayList<Player> players = new ArrayList<>();
        PlayerBoardMock boardMock = new PlayerBoardMock();
        Player player1 = new Player(playerOrder1, boardMock);
        Player player2 = new Player(playerOrder2, boardMock);
        players.add(player1);
        players.add(player2);
        RewardMenu menu = new RewardMenu(players);
        ArrayList<Effect> items = new ArrayList<>();
        items.add(Effect.WOOD);
        items.add(Effect.CLAY);
        menu.initiate(items);

        assertEquals(menu.tryMakeAction(playerOrder1), HasAction.WAITING_FOR_PLAYER_ACTION);
        assertEquals(menu.tryMakeAction(playerOrder2), HasAction.WAITING_FOR_PLAYER_ACTION);
        assertFalse(menu.takeReward(playerOrder1, Effect.GOLD));
        assertTrue(menu.takeReward(playerOrder1, Effect.WOOD));
        assertEquals(menu.tryMakeAction(playerOrder1), HasAction.NO_ACTION_POSSIBLE);
        assertFalse(menu.takeReward(playerOrder2, Effect.WOOD));
        assertEquals(menu.tryMakeAction(playerOrder2), HasAction.AUTOMATIC_ACTION_DONE);
        assertFalse(menu.takeReward(playerOrder2, Effect.CLAY));
        assertEquals(menu.tryMakeAction(playerOrder2), HasAction.NO_ACTION_POSSIBLE);
    }

    @Test
    public void noPlayersTest() {
        PlayerOrder playerOrder = new PlayerOrder(0, 0);
        RewardMenu menu = new RewardMenu(new ArrayList<>());
        ArrayList<Effect> items = new ArrayList<>();
        items.add(Effect.WOOD);
        menu.initiate(items);

        assertEquals(menu.tryMakeAction(playerOrder), HasAction.NO_ACTION_POSSIBLE);
        assertFalse(menu.takeReward(playerOrder, Effect.WOOD));
    }

    @Test
    public void noItemsTest() {
        PlayerOrder playerOrder = new PlayerOrder(0, 0);
        ArrayList<Player> players = new ArrayList<>();
        PlayerBoardMock boardMock = new PlayerBoardMock();
        Player player = new Player(playerOrder, boardMock);
        players.add(player);
        RewardMenu menu = new RewardMenu(players);
        menu.initiate(new ArrayList<>());

        assertEquals(menu.tryMakeAction(playerOrder), HasAction.NO_ACTION_POSSIBLE);
        assertFalse(menu.takeReward(playerOrder, Effect.WOOD));
    }
}
