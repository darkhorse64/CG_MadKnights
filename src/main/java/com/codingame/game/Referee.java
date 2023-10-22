package com.codingame.game;

import com.codingame.game.engine.*;
import com.codingame.game.ui.Viewer;
import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.toggle.ToggleModule;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Random;
import java.util.Comparator;

import static com.codingame.game.Constants.*;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphics;
    @Inject private ToggleModule toggleModule;
    @Inject private EndScreenModule endScreenModule;

    Board board;
    Viewer viewer;
    Player currentPlayer;
    Random rand;
    int score = 0;

    @Override
    public void init() {
        rand = new Random(gameManager.getSeed());
        board = new Board(gameManager.getPlayerCount(), gameManager.getSeed());
        viewer = new Viewer(graphics, board, gameManager, toggleModule);
        gameManager.setMaxTurns(maxTurns);
        gameManager.setFirstTurnMaxTime(firstTurnMaxTime);
        gameManager.setTurnMaxTime(turnMaxTime);
        gameManager.setFrameDuration(1800);
        currentPlayer = gameManager.getPlayer(0);
    }

    @Override
    public void gameTurn(int turn) {

        if (gameManager.getActivePlayers().size() == 1) {
            gameManager.addToGameSummary(gameManager.formatErrorMessage(currentPlayer.getNicknameToken() + " wins!"));
            gameManager.addTooltip(currentPlayer, currentPlayer.getNicknameToken() + " wins");
            currentPlayer.setScore(score++);
            gameManager.endGame();
            return;
        }

        ArrayList<Action> actions = board.getLegalActions(currentPlayer.getIndex());

        if (actions.isEmpty()) {
            currentPlayer.setScore(score++);
            gameManager.addToGameSummary(gameManager.formatErrorMessage(currentPlayer.getNicknameToken() + " has no moves."));
            currentPlayer.deactivate(currentPlayer.getNicknameToken() + " has no more moves");
        } else {
            try {
                sendInputs(turn);
                currentPlayer.execute();

                String output = currentPlayer.getOutputs().get(0);
                String comment = null;
                // Split comment from output.
                int spaceIndex = output.indexOf(' ');
                if (spaceIndex != -1) {
                    comment = output.substring(spaceIndex + 1);
                    if (comment.length() > maxCommentLength)
                        comment = comment.substring(0, maxCommentLength);
                    output = output.substring(0, spaceIndex);
                }

                if (comment != null) {
                    viewer.players[currentPlayer.getIndex()].msg.setText(comment);
                } else {
                    viewer.players[currentPlayer.getIndex()].msg.setText("");
                }

                boolean found = false;

                if (output.equals("random")) {
                    found = true;
                    int a = rand.nextInt(actions.size());

                    currentPlayer.lastAction = actions.get(a).toString();

                    viewer.applyAction(actions.get(a));
                    board.applyAction(actions.get(a));

                } else {
                    for (Action action : actions) {
                        String s = action.toString();
                        if (output.startsWith(s)) {
                            currentPlayer.lastAction = s;
                            viewer.applyAction(action);
                            board.applyAction(action);
                            found = true;
                            break;
                        }
                    }
                }

                if (!found) {
                    throw new InvalidActionException(String.format("Action %s was not valid!", actions.toString()));
                }

                viewer.players[currentPlayer.getIndex()].action.setText(currentPlayer.lastAction);
                graphics.commitEntityState(0, viewer.players[currentPlayer.getIndex()].action, viewer.players[currentPlayer.getIndex()].msg);

            } catch (AbstractPlayer.TimeoutException e) {
                gameManager.addToGameSummary(GameManager.formatErrorMessage(currentPlayer.getNicknameToken() + " did not output in time!"));
                currentPlayer.deactivate(currentPlayer.getNicknameToken() + " timeout");
                currentPlayer.setScore(-1);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException | InvalidActionException e) {
                gameManager.addToGameSummary(GameManager.formatErrorMessage(currentPlayer.getNicknameToken() + " made an invalid action!"));
                currentPlayer.deactivate(currentPlayer.getNicknameToken() + " made an invalid action");
                currentPlayer.setScore(-1);
            }
        }

        currentPlayer = getNextActivePlayer(currentPlayer.getIndex());
    }

    void sendInputs(int turn) {

        if (turn < gameManager.getPlayerCount() + 1) {
            // Id
            currentPlayer.sendInputLine(String.valueOf("rgb".charAt(currentPlayer.getIndex())));
        }

        // Player state
        for (Player player : gameManager.getPlayers()) {
            String s = String.valueOf("rgb".charAt(player.getIndex())) + " ";
            s += player.isActive() ? "1 " : "0 ";
            s += player.lastAction;
            currentPlayer.sendInputLine(s);
        }

        // Board
        for(int y = 0; y < board.rows(); ++y) {
            String s = "";
            for (int x = 0; x < board.columns(); ++x) {
                Square square = board.squares[board.rows() - y - 1][x];
                if (square.piece != null) {
                    s += "rgb".charAt(square.piece.owner);
                } else if (square.free) {
                    s += ".";
                } else {
                    s += "#";
                }
            }
            currentPlayer.sendInputLine(s);
        }

        // Number of actions
        ArrayList<Action> actions = board.getLegalActions(currentPlayer.getIndex());
        actions.sort(Comparator.comparing(Action::toString));
        currentPlayer.sendInputLine(Integer.toString(actions.size()));
        for (Action action : actions)
            currentPlayer.sendInputLine(action.toString());
    }

    @Override
    public void onEnd() {
        int[] scores = new int[gameManager.getPlayerCount()];
        for (int i = 0; i < gameManager.getPlayerCount(); i++) {
            scores[i] = gameManager.getPlayer(i).getScore();
        }
        endScreenModule.setScores(scores);
   }
    public Player getNextActivePlayer(int currentId) {

        int nextIdPlayer = ((currentId + 1) % gameManager.getPlayerCount());
        for (int i = 1; i <= gameManager.getPlayerCount(); i++) {
            int id = (currentId + i) % gameManager.getPlayerCount();
            Player player = gameManager.getPlayer(id);

            if (player.isActive()) return player;
        }
        return null;
    }
}
