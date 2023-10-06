package com.codingame.game.viewer;

import com.codingame.game.engine.Piece;
import com.codingame.gameengine.module.entities.Sprite;

public class PieceUI {
    Piece piece;
    Sprite sprite;

    PieceUI(Piece piece, Sprite sprite) {
        this.piece = piece;
        this.sprite = sprite;
    }
}
