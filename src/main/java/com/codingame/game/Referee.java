package com.codingame.game;

import com.codingame.game.engine.*;
import com.codingame.game.viewer.Viewer;
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
    String lastAction = "null";
    Random rand;

    @Override
    public void init() {
        rand = new Random(gameManager.getSeed());
        board = new Board(gameManager.getSeed());
        viewer = new Viewer(graphics, board, gameManager, toggleModule);
        gameManager.setMaxTurns(maxTurns);
        gameManager.setFirstTurnMaxTime(firstTurnMaxTime);
        gameManager.setTurnMaxTime(turnMaxTime);
        gameManager.setFrameDuration(1800);
        lastAction = "null";
        currentPlayer = gameManager.getPlayer(0);
    }

    @Override
    public void gameTurn(int turn) {

        ArrayList<Action> actions = board.getLegalActions(currentPlayer.getIndex());

        Player player = gameManager.getPlayer(currentPlayer.getIndex());

        try {
            sendInputs(turn);
            player.execute();

            String output = player.getOutputs().get(0);
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
                 viewer.players[player.getIndex()].msg.setText(comment);
            } else {
                viewer.players[player.getIndex()].msg.setText("");
            }

            boolean found = false;

            if (output.equals("random")) {
                found = true;
                int a = rand.nextInt(actions.size());

                lastAction = actions.get(a).toString();

                viewer.applyAction(actions.get(a));
                board.applyAction(actions.get(a));

            } else {
                for (Action action : actions) {
                    String s = action.toString();
                    if (output.startsWith(s)) {
                        lastAction = s;
                        viewer.applyAction(action);
                        board.applyAction(action);
                        found = true;
                        break;
                    }
                }
            }

            if(!found) {
                throw new InvalidActionException(String.format("Action %s was not valid!", actions.toString()));
            }

            viewer.players[player.getIndex()].action.setText(lastAction);
            viewer.players[player.getIndex()].frame.setVisible(true);
            viewer.players[player.getIndex() ^1].frame.setVisible(false);
            graphics.commitEntityState(0, viewer.players[player.getIndex()].action, viewer.players[player.getIndex()].msg);

        } catch (AbstractPlayer.TimeoutException e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " did not output in time!"));
            player.deactivate(player.getNicknameToken() + " timeout.");
            player.setScore(-1);
            gameManager.endGame();
            return;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | InvalidActionException e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " made an invalid action!"));
            player.deactivate(player.getNicknameToken() + " made an invalid action.");
            player.setScore(-1);
            gameManager.endGame();
            return;
        }

        if (board.hasPlayerWon(currentPlayer.getIndex())) {
            gameManager.getPlayer(currentPlayer.getIndex()).setScore(gameManager.getPlayer(currentPlayer.getIndex()).getScore() + 1);
            gameManager.endGame();
        } else {
            currentPlayer = gameManager.getPlayer(currentPlayer.getIndex() ^ 1);
        }
    }

    void sendInputs(int turn) {
        Player player = gameManager.getPlayer(currentPlayer.getIndex());

        if (turn < 3) {
            // Color
            player.sendInputLine(player.getIndex() == 0 ? "w" : "b");
        }

        // Board
        for(int y = 0; y < board.rows(); ++y) {
            String s = "";
            for (int x = 0; x < board.columns(); ++x) {
                Square square = board.squares[board.rows() - y - 1][x];
                if (square.piece != null) {
                    s += square.piece.owner == 0 ? 'w' : 'b';
                } else if (square.free) {
                    s += ".";
                } else {
                    s += "#";
                }
            }
            player.sendInputLine(s);
        }

        // Last action
        player.sendInputLine(lastAction);
        // Number of actions
        ArrayList<Action> actions = board.getLegalActions(player.getIndex());
        actions.sort(Comparator.comparing(Action::toString));
        player.sendInputLine(Integer.toString(actions.size()));
        for (Action action : actions)
            player.sendInputLine(action.toString());
    }

    @Override
    public void onEnd() {
        int[] scores = { gameManager.getPlayer(0).getScore(), gameManager.getPlayer(1).getScore() };
        String[] text = new String[2];
        if(scores[0] > scores[1]) {
            gameManager.addToGameSummary(gameManager.formatErrorMessage(gameManager.getPlayer(0).getNicknameToken() + " won"));
            gameManager.addTooltip(gameManager.getPlayer(0), gameManager.getPlayer(0).getNicknameToken() + " won");
            text[0] = "Won";
            text[1] = "Lost";
        } else if(scores[0] < scores[1]) {
            gameManager.addToGameSummary(gameManager.formatErrorMessage(gameManager.getPlayer(1).getNicknameToken() + " won"));
            gameManager.addTooltip(gameManager.getPlayer(1), gameManager.getPlayer(1).getNicknameToken() + " won");
            text[0] = "Lost";
            text[1] = "Won";
        } else {
        gameManager.addToGameSummary(gameManager.formatErrorMessage("Game is drawn"));
        gameManager.addTooltip(gameManager.getPlayer(1), "Draw");
        text[0] = "Draw";
        text[1] = "Draw";
    }

        endScreenModule.setScores(scores, text);
   }
}
