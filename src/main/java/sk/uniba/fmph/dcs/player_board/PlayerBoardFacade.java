package sk.uniba.fmph.dcs.player_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.InterfaceFeedTribe;
import sk.uniba.fmph.dcs.stone_age.InterfaceNewTurn;
import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;

import java.util.Collection;
import java.util.OptionalInt;

public final class PlayerBoardFacade implements InterfaceFeedTribe, InterfaceNewTurn, InterfacePlayerBoardGameBoard {
    private final PlayerBoard playerBoard;

    public PlayerBoardFacade() {
        this.playerBoard = new PlayerBoard();
    }

    @Override
    public boolean feedTribeIfEnoughFood() {
        return playerBoard.feedTribeIfEnoughFood();
    }

    @Override
    public boolean feedTribe(final Collection<Effect> resources) {
        return playerBoard.feedTribe(resources);
    }

    @Override
    public boolean doNotFeedThisTurn() {
        return playerBoard.doNotFeedThisTurn();
    }

    @Override
    public boolean isTribeFed() {
        return playerBoard.isTribeFed();
    }

    @Override
    public void newTurn() {
        playerBoard.newTurn();
    }

    @Override
    public void giveEffect(final Collection<Effect> stuff) {
        playerBoard.giveEffect(stuff);
    }

    @Override
    public void giveEndOfGameEffect(final Collection<EndOfGameEffect> stuff) {
        playerBoard.giveEndOfGameEffect(stuff);
    }

    @Override
    public boolean takeResources(final Collection<Effect> stuff) {
        return playerBoard.takeResources(stuff);
    }

    @Override
    public boolean takeFigures(final int count) {
        return playerBoard.takeFigures(count);
    }

    @Override
    public void giveFigures(final int count) {
        playerBoard.giveFigures(count);
    }

    @Override
    public boolean hasFigures(final int count) {
        return playerBoard.hasFigures(count);
    }

    @Override
    public boolean hasSufficientTools(final int goal) {
        return playerBoard.hasSufficientTools(goal);
    }

    @Override
    public OptionalInt useTool(final int idx) {
        return playerBoard.useTool(idx);
    }

    @Override
    public void takePoints(final int points) {
        playerBoard.takePoints(points);
    }

    @Override
    public void givePoints(final int points) {
        playerBoard.givePoints(points);
    }
}
