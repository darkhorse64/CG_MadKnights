package com.codingame.game.ui;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.RoundedRectangle;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;

public class PlayerUI {
    Player player;
    RoundedRectangle rectangle;
    Sprite avatar;
    Sprite piece;
    public Text action;
    public Text msg;

    PlayerUI(Viewer viewer, Player player, int startx, int starty) {
        this.player = player;

        rectangle = viewer.graphics.createRoundedRectangle().setHeight(460).setWidth(300).setX(startx).setY(starty).setFillColor(0xFFFFFF).setAlpha(0.15).setLineWidth(4);

        viewer.graphics.createText(player.getNicknameToken()).setFontSize(42).setX(startx + 150).setAnchorX(0.5).setY(starty + 10).setFillColor(0xffffff);
        avatar = viewer.graphics.createSprite().setImage(player.getAvatarToken()).setX(startx + 150).setY(starty + 90).setAnchorX(0.5).setBaseHeight(100).setBaseWidth(100);
        piece = viewer.graphics.createSprite().setX(startx + 150 - viewer.circleRadius).setY(starty + 220).setBaseWidth(viewer.circleRadius * 2).setBaseHeight(viewer.circleRadius * 2);
        action = viewer.graphics.createText().setFillColor(0xffffff).setAnchorX(0.5).setX(startx + 150).setY(starty + 340).setFontSize(40);
        msg = viewer.graphics.createText().setFillColor(0xffffff).setAnchorX(0.5).setX(startx + 150).setY(starty + 400).setFontSize(36);
    }
}
