package com.codingame.game;

public class Constants {
    public static final int Columns = 8;
    public static final int Rows = 8;
    public static final int Colors = 2;

    public static final int KnightMoves = 8;
    public static final int[] KnightDeltaColumn = {-1,  1, -2,  2, -2,  2, -1,  1};
    public static final int[] KnightDeltaRow    = {-2, -2, -1, -1,  1,  1,  2,  2};

    public static final int maxCommentLength = 16;

    public static int maxTurns = 62;
    public static int firstTurnMaxTime = 1000;
    public static int turnMaxTime = 100;
    public static final int[] boardColors = new int[]{0xB09673, 0xD2C2A8};
    public static final int backgroundColor = 0x7F7F7F;
    public static final int[] highlightColors = new int[]{0x7e9ec2, 0x7e9ec2, 0xFF0000};
    public static final double moveTime = 700;
}
