package com.codingame.game.engine;

public class Action {
    public Square from;
    public Square to;

    Action(Square from, Square to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return to.toString();
    }
}
