package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.PlayerOrder;
import sk.uniba.fmph.dcs.stone_age.Location;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceGamePhaseController;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;

import java.util.Collection;

public final class GamePhaseController implements InterfaceGamePhaseController {
    private Map<GamePhase, InterfaceGamePhaseState> dispatchers;
    private PlayerOrder roundStartingPlayer;
    private PlayerOrder currentPlayer;
    private Optional<PlayerOrder> currentPlayerTakingReward;
    private GamePhase gamePhase;

    public GamePhaseController(final Map<GamePhase, InterfaceGamePhaseState> dispatchers,
            final PlayerOrder startingPlayer) {
        this.roundStartingPlayer = startingPlayer;
        this.currentPlayer = startingPlayer;
        this.currentPlayerTakingReward = Optional.empty();
        this.dispatchers = dispatchers;
        this.gamePhase = GamePhase.PLACE_FIGURES;
    }

    private boolean checkPlayersTurn(final PlayerOrder player) {
        switch (gamePhase) {
        case PLACE_FIGURES:
        case MAKE_ACTION:
        case WAITING_FOR_TOOL_USE:
            return player.equals(currentPlayer);
        case ALL_PLAYERS_TAKE_A_REWARD:
            return currentPlayerTakingReward.isPresent() && player.equals(currentPlayerTakingReward.get());
        case FEED_TRIBE:
            return true;
        case NEW_ROUND:
            return false;
        case GAME_END:
            throw new AssertionError();
        default:
            return false;
        }
    }

    private void progressStateAfterSuccessfulAction() {
        switch (gamePhase) {
        case PLACE_FIGURES:
        case FEED_TRIBE:
            currentPlayer = currentPlayer.forward();
            break;
        case MAKE_ACTION:
        case WAITING_FOR_TOOL_USE:
            break;
        case ALL_PLAYERS_TAKE_A_REWARD:
            if (currentPlayerTakingReward.isPresent()) {
                currentPlayerTakingReward = Optional.of(currentPlayerTakingReward.get().forward());
            }
            break;
        case NEW_ROUND:
            gamePhase = GamePhase.PLACE_FIGURES;
            roundStartingPlayer = roundStartingPlayer.forward();
            currentPlayer = roundStartingPlayer;
            break;
        case GAME_END:
        default:
            throw new AssertionError();
        }
    }

    private void progressStateAfterNoActionPossible() {
        switch (gamePhase) {
        case PLACE_FIGURES:
        case FEED_TRIBE:
        case MAKE_ACTION:
            currentPlayer = currentPlayer.forward();
            break;
        case ALL_PLAYERS_TAKE_A_REWARD:
        case WAITING_FOR_TOOL_USE:
            currentPlayerTakingReward = Optional.empty();
            gamePhase = GamePhase.MAKE_ACTION;
            break;
        case NEW_ROUND:
            gamePhase = GamePhase.GAME_END;
            break;
        case GAME_END:
        default:
            throw new AssertionError();
        }
    }

    private void progressStateAfterNoActionPossibleByAnyPlayer() {
        switch (gamePhase) {
        case PLACE_FIGURES:
            currentPlayer = roundStartingPlayer;
            gamePhase = GamePhase.MAKE_ACTION;
            break;
        case MAKE_ACTION:
            currentPlayer = roundStartingPlayer;
            gamePhase = GamePhase.FEED_TRIBE;
            break;
        case FEED_TRIBE:
            currentPlayer = roundStartingPlayer;
            gamePhase = GamePhase.NEW_ROUND;
            break;
        case NEW_ROUND:
        case WAITING_FOR_TOOL_USE:
        case ALL_PLAYERS_TAKE_A_REWARD:
        case GAME_END:
        default:
            throw new AssertionError();
        }
    }

    private void progressStateToolUse() {
        if (gamePhase == GamePhase.MAKE_ACTION) {
            gamePhase = GamePhase.WAITING_FOR_TOOL_USE;
        } else {
            throw new AssertionError();
        }
    }

    private void progressStateAllPlayersTakeAReward() {
        if (gamePhase == GamePhase.MAKE_ACTION) {
            gamePhase = GamePhase.ALL_PLAYERS_TAKE_A_REWARD;
            currentPlayerTakingReward = Optional.of(currentPlayer);
        } else {
            throw new AssertionError();
        }
    }

    private void tryToDoFurtherActions() {
        PlayerOrder firstUnsuccessfulPlayer = null;
        GamePhase unsuccessfulGamePhase = null;

        while (true) {
            InterfaceGamePhaseState dispatcher = dispatchers.get(gamePhase);
            PlayerOrder player = currentPlayerTakingReward.orElse(currentPlayer);
            if (firstUnsuccessfulPlayer != null && firstUnsuccessfulPlayer.equals(player)
                    && unsuccessfulGamePhase == gamePhase) {
                progressStateAfterNoActionPossibleByAnyPlayer();
                firstUnsuccessfulPlayer = null;
                continue;
            }

            HasAction actionResult = dispatcher.tryToMakeAutomaticAction(player);
            switch (actionResult) {
            case WAITING_FOR_PLAYER_ACTION:
                return;
            case AUTOMATIC_ACTION_DONE:
                firstUnsuccessfulPlayer = null;
                progressStateAfterSuccessfulAction();
                break;
            case NO_ACTION_POSSIBLE:
                if (firstUnsuccessfulPlayer == null || unsuccessfulGamePhase != gamePhase) {
                    firstUnsuccessfulPlayer = player;
                    unsuccessfulGamePhase = gamePhase;
                }
                progressStateAfterNoActionPossible();
                continue;
            default:
                throw new AssertionError();
            }
        }
    }

    public boolean placeFigures(final PlayerOrder player, final Location location, final int figuresCount) {
        if (!checkPlayersTurn(player)) {
            return false;
        }
        InterfaceGamePhaseState dispatcher = dispatchers.get(gamePhase);
        ActionResult actionResult = dispatcher.placeFigures(player, location, figuresCount);

        switch (actionResult) {
        case FAILURE:
            return false;
        case ACTION_DONE:
            progressStateAfterSuccessfulAction();
            tryToDoFurtherActions();
            return true;
        default:
            throw new AssertionError();
        }
    }

    public boolean makeAction(final PlayerOrder player, final Location location,
            final Collection<Effect> inputResources, final Collection<Effect> outputResources) {
        if (!checkPlayersTurn(player)) {
            return false;
        }
        InterfaceGamePhaseState dispatcher = dispatchers.get(gamePhase);
        ActionResult actionResult = dispatcher.makeAction(player, location, inputResources, outputResources);

        switch (actionResult) {
        case FAILURE:
            return false;
        case ACTION_DONE:
            progressStateAfterSuccessfulAction();
            tryToDoFurtherActions();
            return true;
        case ACTION_DONE_WAIT_FOR_TOOL_USE:
            progressStateToolUse();
            tryToDoFurtherActions();
            return true;
        case ACTION_DONE_ALL_PLAYERS_TAKE_A_REWARD:
            progressStateAllPlayersTakeAReward();
            tryToDoFurtherActions();
            return true;
        default:
            throw new AssertionError();
        }
    }

    public boolean skipAction(final PlayerOrder player, final Location location) {
        if (!checkPlayersTurn(player)) {
            return false;
        }
        InterfaceGamePhaseState dispatcher = dispatchers.get(gamePhase);
        ActionResult actionResult = dispatcher.skipAction(player, location);

        switch (actionResult) {
        case FAILURE:
            return false;
        case ACTION_DONE:
            progressStateAfterSuccessfulAction();
            tryToDoFurtherActions();
            return true;
        default:
            throw new AssertionError();
        }
    }

    public boolean useTools(final PlayerOrder player, final int toolIndex) {
        if (!checkPlayersTurn(player)) {
            return false;
        }
        InterfaceGamePhaseState dispatcher = dispatchers.get(gamePhase);
        ActionResult actionResult = dispatcher.useTools(player, toolIndex);

        switch (actionResult) {
        case FAILURE:
            return false;
        case ACTION_DONE:
            progressStateAfterSuccessfulAction();
            tryToDoFurtherActions();
            return true;
        default:
            throw new AssertionError();
        }
    }

    public boolean noMoreToolsThisThrow(final PlayerOrder player) {
        if (!checkPlayersTurn(player)) {
            return false;
        }
        InterfaceGamePhaseState dispatcher = dispatchers.get(gamePhase);
        ActionResult actionResult = dispatcher.noMoreToolsThisThrow(player);

        switch (actionResult) {
        case FAILURE:
            return false;
        case ACTION_DONE:
            progressStateAfterNoActionPossible();
            tryToDoFurtherActions();
            return true;
        default:
            throw new AssertionError();
        }
    }

    public boolean feedTribe(final PlayerOrder player, final Collection<Effect> resources) {
        if (!checkPlayersTurn(player)) {
            return false;
        }
        InterfaceGamePhaseState dispatcher = dispatchers.get(gamePhase);
        ActionResult actionResult = dispatcher.feedTribe(player, resources);

        switch (actionResult) {
        case FAILURE:
            return false;
        case ACTION_DONE:
            progressStateAfterSuccessfulAction();
            tryToDoFurtherActions();
            return true;
        default:
            throw new AssertionError();
        }
    }

    public boolean doNotFeedThisTurn(final PlayerOrder player) {
        if (!checkPlayersTurn(player)) {
            return false;
        }
        InterfaceGamePhaseState dispatcher = dispatchers.get(gamePhase);
        ActionResult actionResult = dispatcher.doNotFeedThisTurn(player);

        switch (actionResult) {
        case FAILURE:
            return false;
        case ACTION_DONE:
            progressStateAfterSuccessfulAction();
            tryToDoFurtherActions();
            return true;
        default:
            throw new AssertionError();
        }
    }

    public boolean makeAllPlayersTakeARewardChoice(final PlayerOrder player, final Effect reward) {
        if (!checkPlayersTurn(player)) {
            return false;
        }
        InterfaceGamePhaseState dispatcher = dispatchers.get(gamePhase);
        ActionResult actionResult = dispatcher.makeAllPlayersTakeARewardChoice(player, reward);

        switch (actionResult) {
        case FAILURE:
            return false;
        case ACTION_DONE:
            progressStateAfterSuccessfulAction();
            tryToDoFurtherActions();
            return true;
        default:
            throw new AssertionError();
        }
    }

    public String state() {
        String playerTakingReward = "None";
        if (currentPlayerTakingReward.isPresent()) {
            playerTakingReward = String.valueOf(this.currentPlayerTakingReward.get().getOrder());
        }
        Map<String, String> state = Map.of("game phase", gamePhase.toString(), "round starting player",
                String.valueOf(roundStartingPlayer.getOrder()), "current_player",
                String.valueOf(currentPlayer.getOrder()), "player taking a reward", playerTakingReward);
        return new JSONObject(state).toString();
    }
}
