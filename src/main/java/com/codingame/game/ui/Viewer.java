package com.codingame.game.ui;

import com.codingame.game.Player;
import com.codingame.game.engine.Action;
import com.codingame.game.engine.Board;
import com.codingame.game.engine.Piece;
import com.codingame.game.engine.Square;
import static com.codingame.game.Constants.*;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.toggle.ToggleModule;

import java.util.ArrayList;
import java.util.List;

public class Viewer {
    MultiplayerGameManager<Player> gameManager;
    GraphicEntityModule graphics;
    BoardUI displayBoard;
    List<PieceUI> pieces;
    Rectangle[] lastActions;
    public PlayerUI[] players;

    int rows;
    int columns;
    int viewerWidth;
    int viewerHeight;
    int rectangleSize;
    int circleRadius;
    int gap;

    public Viewer(GraphicEntityModule graphics, Board board, MultiplayerGameManager<Player> gameManager, ToggleModule toggleModule) {
        this.graphics = graphics;
        this.gameManager = gameManager;

        viewerWidth = this.graphics.getWorld().getWidth();
        viewerHeight = this.graphics.getWorld().getHeight();
        rows = board.rows();
        columns = board.columns();

        displayBoard = new BoardUI(graphics, board);
        players = new PlayerUI[2];
        lastActions = new Rectangle[3];
        pieces = new ArrayList<>();

        this.graphics.createRectangle().setWidth(viewerWidth).setHeight(viewerHeight).setFillColor(backgroundColor);

        rectangleSize = viewerHeight / -~rows;

        circleRadius = (int)(rectangleSize * .42);
        gap = (rectangleSize - circleRadius * 2) / 2;

        for (int i = 0; i < 3; ++i) {
            lastActions[i] = graphics.createRectangle().setFillColor(highlightColors[i]).setWidth(rectangleSize).setHeight(rectangleSize).setX(-rectangleSize).setY(-rectangleSize).setFillAlpha(i < 2 ? 0.75 : 0.45).setZIndex(2);
            toggleModule.displayOnToggleState(lastActions[i], "debugToggle", true);
        }

        for (Piece piece : board.pieces) {
            int x = piece.getX();
            int y = piece.getY();
            pieces.add(new PieceUI(piece, graphics.createSprite()
                    .setImage(piece.owner == 0 ? "w.png" : "b.png")
                    .setBaseWidth(circleRadius * 2)
                    .setBaseHeight(circleRadius * 2)
                    .setX(displayBoard.rectangles[y][x].getX() + gap)
                    .setY(displayBoard.rectangles[y][x].getY() + gap)));
        }

        for (int i = 0; i < 2; ++i) {
            players[i] = new PlayerUI(this, gameManager.getPlayer(i));
        }

        for (int i = 0; i < 3; ++i) {
            lastActions[i].setX(-rectangleSize, Curve.IMMEDIATE);
        }

        for (int i = 0; i < board.pieces.size(); ++i) {
            graphics.commitEntityState(0.4, pieces.get(i).sprite);
            pieces.get(i).piece = board.pieces.get(i);
            int x = board.pieces.get(i).getX();
            int y = board.pieces.get(i).getY();
            pieces.get(i).sprite.setX(displayBoard.rectangles[y][x].getX() + gap).setY(displayBoard.rectangles[y][x].getY() + gap).setZIndex(3);
            graphics.commitEntityState(0.8, pieces.get(i).sprite);
        }

        for (int i = 0; i < 2; ++i) {
            players[i].msg.setText("");
            players[i].action.setText("");
        }

        players[0].piece.setImage("w.png");
        players[1].piece.setImage("b.png");
    }

    public void applyAction(Action action) {
        Piece piece = action.from.piece;
        Square target = action.to;
        double distance = Math.pow(Math.max(Math.abs(piece.getX() - target.column),Math.abs(piece.getY() - target.row)), 0.45);
        double frameDuration = distance * moveTime;
        gameManager.setFrameDuration((int)frameDuration);
        double commitTime = 1.0;

        for (PieceUI u : pieces) {
            if (u.piece != piece) continue;
            lastActions[0].setX(displayBoard.rectangles[piece.getY()][piece.getX()].getX(), Curve.IMMEDIATE).setY(displayBoard.rectangles[piece.getY()][piece.getX()].getY(), Curve.IMMEDIATE);
            displayBoard.sprites[action.from.getRow()][action.from.getColumn()].setVisible(true);
            int x = target.column;
            int y = target.row;
            lastActions[1].setX(displayBoard.rectangles[y][x].getX(), Curve.IMMEDIATE).setY(displayBoard.rectangles[y][x].getY(), Curve.IMMEDIATE);

            u.sprite.setZIndex(4);
            graphics.commitEntityState(0.0, u.sprite);
            u.sprite.setX(displayBoard.rectangles[y][x].getX() + gap).setY(displayBoard.rectangles[y][x].getY() + gap);
            u.sprite.setZIndex(3);
            graphics.commitEntityState(commitTime, u.sprite);
            break;
        }
    }
}
