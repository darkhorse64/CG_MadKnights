package com.codingame.game.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.codingame.game.Constants.*;

public class Board {
    public Square[][] squares;
    public List<Piece> pieces;

    public int rows() {
        return Rows;
    }

    public int columns() {
        return Columns;
    }

    public Board(long seed) {
        squares = new Square[Rows][Columns];
        pieces = new ArrayList<>();

        for (int y = 0; y < Rows; ++y) {
            for (int x = 0; x < Columns; ++x) {
                squares[y][x] = new Square(x, y);
            }
        }

        Random rng = new Random(seed);
        int x = rng.nextInt(Columns);
        int y = rng.nextInt(Rows);
        pieces.add(squares[x][y].setUnit(0));
        pieces.add(squares[Columns-x-1][Rows-y-1].setUnit(1));
   }

    boolean isInside(int x, int y) {
        return x >= 0 && x < Columns && y >= 0 && y < Rows;
    }

    // Returns if player won.
    public boolean hasPlayerWon(int player) {
        return getLegalActions(player ^1).isEmpty();
    }

    public ArrayList<Action> getLegalActions(int player) {
        ArrayList<Action> actions = new ArrayList<>();
        Square from = pieces.get(player).square;
        for (int i = 0; i < KnightMoves; i++)
        {
            Square to = from.offset(KnightDeltaColumn[i],KnightDeltaRow[i]);
            if (to == null) continue;
            to = squares[to.getRow()][to.getColumn()];
            if (!to.free) continue;
            actions.add(new Action(from, to));
        }
        return actions;
    }

    public void applyAction(Action action) {
        action.from.piece.moveTo(action.to);
    }
}
