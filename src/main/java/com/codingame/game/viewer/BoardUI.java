package com.codingame.game.viewer;

import com.codingame.game.engine.Board;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Line;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.Sprite;

import static com.codingame.game.Constants.*;

public class BoardUI {
    Rectangle[][] rectangles;
    Sprite[][] sprites;
    Line[] lines;

    public BoardUI(GraphicEntityModule graphics, Board board)
    {
        int viewerWidth = graphics.getWorld().getWidth();
        int viewerHeight = graphics.getWorld().getHeight();
        int rows = board.rows();
        int columns = board.columns();

        rectangles = new Rectangle[rows][columns];
        sprites = new Sprite[rows][columns];
        lines = new Line[rows + columns + 2];

        int RECTANGLE_SIZE = viewerHeight / -~rows;
        int START_X = viewerWidth / 2 - RECTANGLE_SIZE * columns / 2;
        int FONT_SIZE = RECTANGLE_SIZE / 3;

        for (int y = 0; y < rows; ++y) {
            int yG = rows - y - 1;
            for (int x = 0; x < columns; ++x) {
                int xG = x;
                rectangles[y][x] = graphics.createRectangle().setZIndex(1).setFillColor(boardColors[(x + y + 1) & 1])
                        .setWidth(RECTANGLE_SIZE).setHeight(RECTANGLE_SIZE).setX(START_X + xG * RECTANGLE_SIZE).setY((int) (RECTANGLE_SIZE / 2) + yG * RECTANGLE_SIZE - FONT_SIZE / 2);
                sprites[y][x] = graphics.createSprite().setZIndex(2).setImage("forbidden.png")
                        .setBaseWidth(RECTANGLE_SIZE/2)
                        .setBaseHeight(RECTANGLE_SIZE/2)
                        .setX(rectangles[y][x].getX() + RECTANGLE_SIZE/4)
                        .setY(rectangles[y][x].getY() + RECTANGLE_SIZE/4)
                        .setVisible(false);
                ;
                if (x == 0) {
                    int length = -~y < 10 ? 1 : (int) (Math.log10(x) + 1);
                    graphics.createText(Integer.toString(-~y)).setZIndex(1).setX(rectangles[y][x].getX() - (int) (RECTANGLE_SIZE / 1.5) + (int) (FONT_SIZE / length * .5)).setY(rectangles[y][x].getY() + FONT_SIZE).setFontFamily("Verdana").setFontSize(FONT_SIZE).setFillColor(0xFEFEFE);
                }

                if (y == 0) {
                    graphics.createText(Character.toString((char) (97 + x))).setZIndex(1).setX(rectangles[y][x].getX() + (int) (FONT_SIZE * (1.25))).setY(rectangles[y][x].getY() + RECTANGLE_SIZE + FONT_SIZE / 4).setFontFamily("Verdana").setFontSize(FONT_SIZE).setFillColor(0xFEFEFE);
                }
            }
        }

        for (int y = 0; y <= rows; ++y) {
            lines[y] = graphics.createLine().setFillColor(0x0).setLineWidth(3.0).setZIndex(3)
                    .setX(START_X).setX2(START_X + columns * RECTANGLE_SIZE)
                    .setY((int)(RECTANGLE_SIZE / 2) + y * RECTANGLE_SIZE - FONT_SIZE / 2)
                    .setY2((int)(RECTANGLE_SIZE / 2) + y * RECTANGLE_SIZE - FONT_SIZE / 2);
        }

        for (int x = 0; x <= columns; ++x) {
            lines[columns + 1 + x] = graphics.createLine().setFillColor(0x0).setLineWidth(3.0).setZIndex(3)
                    .setX(START_X + x * RECTANGLE_SIZE).setX2(START_X + x * RECTANGLE_SIZE)
                    .setY((int)(RECTANGLE_SIZE / 2) - FONT_SIZE / 2)
                    .setY2((int)(RECTANGLE_SIZE / 2) + rows * RECTANGLE_SIZE - FONT_SIZE / 2);
        }

        for (int y = 0; y < rows; ++y) {
            for (int x = 0; x < columns; ++x) {
                graphics.commitEntityState(0.4, rectangles[y][x]);
            }
        }
    }
}
