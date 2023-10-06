package com.codingame.game.engine;

import static com.codingame.game.Constants.*;

import java.util.ArrayList;
import java.text.ParseException;

public class Square {
    public int column;
    public int row;
    public Piece piece;
    public boolean free;

    Square(int x, int y) {
        this.column = x;
        this.row = y;
        this.piece = null;
        this.free = true;
    }
    public Square(String str) throws ParseException {
        if (str.length() != 2) throw new ParseException(String.format("'%s' is not a square.", str), 0);
        column = charToColumn(str.charAt(0));
        row = charToRow(str.charAt(1));
    }

    public int getColumn()   { return column; }
    public int getRow()      { return row; }

    // Returns a new square offset by the given directions or null if invalid.
    public Square offset(int deltaColumn, int deltaRow) {
        int newColumn = column + deltaColumn;
        int newRow = row + deltaRow;
        if (newColumn < 0 || newColumn >= Columns || newRow < 0 || newRow >= Rows)
            return null;
        return new Square(newColumn, newRow);
    }

    @Override public String toString() { return Character.toString(columnToChar(column)) + rowToChar(row); }

    @Override public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square other = (Square)o;
        return column == other.column && row == other.row;
    }
    @Override public int hashCode() { return row * 8 + column; }

    public static char columnToChar(int column) { return (char)('a' + column); }

    public static char rowToChar(int row) { return (char)('1' + row); }

    public static int charToColumn(char c) throws ParseException {
        c = Character.toLowerCase(c);
        if (c < 'a' || c > 'h') throw new ParseException(String.format("Character '%c' is not a column.", c), 0);
        return c - 'a';
    }

    public static int charToRow(char c) throws ParseException {
        if (c < '1' || c > '8') throw new ParseException(String.format("Character '%c' is not a row.", c), 0);
        return c - '1';
    }


    Piece setUnit(int owner) {
        this.piece = new Piece(this, owner);
        this.free = false;
        return this.piece;
    }
}
