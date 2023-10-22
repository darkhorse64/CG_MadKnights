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

    public Board(int nbPlayers, long seed) {
        squares = new Square[Rows][Columns];
        pieces = new ArrayList<>();

        for (int y = 0; y < Rows; ++y) {
            for (int x = 0; x < Columns; ++x) {
                squares[y][x] = new Square(x, y);
            }
        }

        Random rng = new Random(seed);
        for (int i = 0; i < nbPlayers; i++) {
            int x,y;

            do {
                x = rng.nextInt(Columns);
                y = rng.nextInt(Rows);
            } while (!squares[x][y].free || x < 1 || x > Rows - 2 || y < 1 || y > Columns - 2);
            pieces.add(squares[x][y].setUnit(i));
        }
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
