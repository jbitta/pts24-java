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

    public PlayerBoardFacade(final PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    @Override
    public boolean feedTribeIfEnoughFood() {
        return playerBoard.tribeFedStatus().feedTribeIfEnoughFood();
    }

    @Override
    public boolean feedTribe(final Collection<Effect> resources) {
        return playerBoard.tribeFedStatus().feedTribe(resources);
    }

    @Override
    public boolean doNotFeedThisTurn() {
        boolean ret = playerBoard.tribeFedStatus().setTribeFed();
        if (!ret) {
            playerBoard.takePoints(PlayerBoard.POINTS_TO_TAKE_IF_TRIBE_IS_NOT_FED);
        }
        return ret;
    }

    @Override
    public boolean isTribeFed() {
        return playerBoard.tribeFedStatus().isTribeFed();
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
        playerBoard.playerCivilisationCards().addEndOfGameEffects(stuff);
    }

    @Override
    public boolean takeResources(final Collection<Effect> stuff) {
        return playerBoard.playerResourcesAndFood().takeResources(stuff);
    }

    @Override
    public boolean takeFigures(final int count) {
        return playerBoard.playerFigures().takeFigures(count);
    }

    @Override
    public void giveFigures(final int count) {
        assert count == 1; // v jednom kole sa moze pridat len 1 figurka
        playerBoard.playerFigures().addNewFigure();
    }

    @Override
    public boolean hasFigures(final int count) {
        return playerBoard.playerFigures().hasFigures(count);
    }

    @Override
    public boolean hasSufficientTools(final int goal) {
        return playerBoard.playerTools().hasSufficientTools(goal);
    }

    @Override
    public OptionalInt useTool(final int idx) {
        return playerBoard.playerTools().useTool(idx);
    }

    @Override
    public void takePoints(final int points) {
        playerBoard.takePoints(points);
    }

    @Override
    public void givePoints(final int points) {
        playerBoard.addPoints(points);
    }
}
