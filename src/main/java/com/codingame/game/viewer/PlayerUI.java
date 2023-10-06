package com.codingame.game.viewer;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.RoundedRectangle;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;

public class PlayerUI {
    Player player;
    RoundedRectangle rectangle;
    public RoundedRectangle frame;
    Sprite avatar;
    Sprite piece;
    public Text action;
    public Text msg;

    PlayerUI(Viewer viewer, Player player) {
        int START_Y = 200;
        int START_X = player.getIndex() == 1 ? viewer.graphics.getWorld().getWidth() - 350 : 50;
        this.player = player;

        rectangle = viewer.graphics.createRoundedRectangle().setHeight(600).setWidth(300).setX(START_X).setY(START_Y).setFillColor(0xFFFFFF).setAlpha(0.15).setLineWidth(4);
        frame = viewer.graphics.createRoundedRectangle().setHeight(600).setWidth(300).setX(START_X).setY(START_Y).setFillAlpha(0).setLineColor(0xffffff).setLineAlpha(0.25).setLineWidth(14).setVisible(false);

        viewer.graphics.createText(player.getNicknameToken()).setFontSize(42).setX(START_X + 150).setAnchorX(0.5).setY(START_Y + 10).setFillColor(0xffffff);
        avatar = viewer.graphics.createSprite().setImage(player.getAvatarToken()).setX(START_X + 150).setY(START_Y + 120).setAnchorX(0.5).setBaseHeight(100).setBaseWidth(100);
        piece = viewer.graphics.createSprite().setImage(player.getIndex() == 1 ? "w.png" : "b.png").setX(START_X + 150 - viewer.circleRadius).setY(START_Y + 270).setBaseWidth(viewer.circleRadius * 2).setBaseHeight(viewer.circleRadius * 2);
        action = viewer.graphics.createText().setFillColor(0xffffff).setAnchorX(0.5).setX(START_X + 150).setY(START_Y + 390).setFontSize(40);
        msg = viewer.graphics.createText().setFillColor(0xffffff).setAnchorX(0.5).setX(START_X + 150).setY(START_Y + 460).setFontSize(36);
    }
}
