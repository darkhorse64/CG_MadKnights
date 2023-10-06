package com.codingame.game.engine;

public class Piece {
    // 0 white, 1 black
    public int owner;
    Square square;

    Piece(Square square, int owner) {
        this.square = square;
        this.owner = owner;
    }

    void moveTo(Square square) {
        this.square.piece = null;
        this.square = square;
        this.square.piece = this;
        this.square.free = false;
    }

    public int getX() {
        return square.column;
    }

    public int getY() {
        return square.row;
    }

    @Override
    public String toString() {
        return square.toString();
    }
}
