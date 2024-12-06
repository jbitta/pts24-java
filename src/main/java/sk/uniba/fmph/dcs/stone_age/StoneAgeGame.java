package sk.uniba.fmph.dcs.stone_age;

import sk.uniba.fmph.dcs.game_board.GameBoard;
import sk.uniba.fmph.dcs.game_board.GameBoardFactory;
import sk.uniba.fmph.dcs.game_board.Player;
import sk.uniba.fmph.dcs.game_phase_controller.GamePhaseControllerFactory;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardFacade;
import sk.uniba.fmph.dcs.player_board.PlayerBoardFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class StoneAgeGame implements InterfaceStoneAgeGame {
    private final Map<Integer, PlayerOrder> idToPlayerOrder;
    private final InterfaceGetState gameBoard;
    private final ArrayList<InterfaceGetState> playerBoards;
    private final InterfaceGamePhaseController gamePhaseController;
    private final StoneAgeObservable observable;
    private static final int MAX_PLAYERS = 4;
    private static final int MIN_PLAYERS = 2;

    public StoneAgeGame(final ArrayList<Integer> players, final ArrayList<InterfaceStoneAgeObserver> observers) {
        if (players.size() > MAX_PLAYERS || players.size() < MIN_PLAYERS || players.size() != observers.size()) {
            throw new IllegalArgumentException("2-4 players may play the game");
        }

        observable = initializeObservable(players, observers);
        idToPlayerOrder = initializeMap(players);
        playerBoards = new ArrayList<>();
        Map<PlayerOrder, PlayerBoardFacade> playerBoardsMap = new HashMap<>();
        ArrayList<Player> playersPlayer = new ArrayList<>();

        for (int player : players) {
            PlayerBoard pB = PlayerBoardFactory.createPlayerBoard();
            playerBoards.add(pB);
            PlayerBoardFacade pBF = new PlayerBoardFacade(pB);
            playerBoardsMap.put(idToPlayerOrder.get(player), pBF);
            playersPlayer.add(new Player(idToPlayerOrder.get(player), pBF));
        }

        GameBoard gb = GameBoardFactory.createGameBoard(playersPlayer);
        gamePhaseController = GamePhaseControllerFactory.createGamePhaseController(gb.getLocationToFigureLocationMap(),
                new HashMap<>(playerBoardsMap), new HashMap<>(playerBoardsMap), idToPlayerOrder.get(players.getFirst()),
                gb.getCurrentThrow(), gb.getRewardMenu());
        gameBoard = gb;
    }

    @Override
    public boolean placeFigures(final int playerId, final Location location, final int figuresCount) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.placeFigures(idToPlayerOrder.get(playerId), location, figuresCount);
        notifyObservable();
        return result;
    }

    @Override
    public boolean makeAction(final int playerId, final Location location, final Collection<Effect> usedResources,
            final Collection<Effect> desiredResources) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.makeAction(idToPlayerOrder.get(playerId), location, usedResources,
                desiredResources);
        notifyObservable();
        return result;
    }

    @Override
    public boolean skipAction(final int playerId, final Location location) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.skipAction(idToPlayerOrder.get(playerId), location);
        notifyObservable();
        return result;
    }

    @Override
    public boolean useTools(final int playerId, final int toolIndex) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.useTools(idToPlayerOrder.get(playerId), toolIndex);
        notifyObservable();
        return result;
    }

    @Override
    public boolean noMoreToolsThisThrow(final int playerId) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.noMoreToolsThisThrow(idToPlayerOrder.get(playerId));
        notifyObservable();
        return result;
    }

    @Override
    public boolean feedTribe(final int playerId, final Collection<Effect> resources) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.feedTribe(idToPlayerOrder.get(playerId), resources);
        notifyObservable();
        return result;
    }

    @Override
    public boolean doNotFeedThisTurn(final int playerId) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.doNotFeedThisTurn(idToPlayerOrder.get(playerId));
        notifyObservable();
        return result;
    }

    @Override
    public boolean makeAllPlayersTakeARewardChoice(final int playerId, final Effect reward) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.makeAllPlayersTakeARewardChoice(idToPlayerOrder.get(playerId), reward);
        notifyObservable();
        return result;
    }

    private void notifyObservable() {
        observable.notify(gameBoard.state());
        observable.notify(gamePhaseController.state());
        for (InterfaceGetState pB : playerBoards) {
            observable.notify(pB.state());
        }
    }

    private static Map<Integer, PlayerOrder> initializeMap(final ArrayList<Integer> players) {
        Map<Integer, PlayerOrder> map = new HashMap<>();
        for (int i = 0; i < players.size(); i++) {
            map.put(players.get(i), new PlayerOrder(i, players.size()));
        }
        return map;
    }

    private static StoneAgeObservable initializeObservable(final ArrayList<Integer> players,
            final ArrayList<InterfaceStoneAgeObserver> observers) {
        StoneAgeObservable observable = new StoneAgeObservable();
        for (int i = 0; i < players.size(); i++) {
            observable.registerObserver(players.get(i), observers.get(i));
        }
        return observable;
    }
}
