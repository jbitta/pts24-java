package sk.uniba.fmph.dcs.player_board;

public final class PlayerBoardFactory {
    private PlayerBoardFactory() {
    }

    public static PlayerBoard createPlayerBoard() {
        return new PlayerBoard();
    }

    public static PlayerBoard createPlayerBoard(
            final PlayerResourcesAndFood playerResourcesAndFood,
            final PlayerCivilisationCards playerCivilisationCards,
            final PlayerFigures playerFigures,
            final TribeFedStatus tribeFedStatus,
            final PlayerTools playerTools
    ) {
        return new PlayerBoard(
                playerResourcesAndFood,
                playerCivilisationCards,
                playerFigures,
                tribeFedStatus,
                playerTools
        );
    }
}
