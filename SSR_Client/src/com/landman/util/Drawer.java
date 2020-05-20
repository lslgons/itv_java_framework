package com.landman.util;

import com.cj.tvui.Constants;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * 드로잉 유틸리티
 * @author daegon.kim
 * @since 2016-12-07
 */
public class Drawer {

    /**
     * 배경 Dimm처리
     * @param g Graphics
     * @param color 배경색
     */
    public static void setDimm(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(0,0, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
    }


    /**
     * 이미지 드로잉
     * @param g
     * @param image
     * @param x
     * @param y
     * @param w
     * @param h
     * @param observer
     */
    public static void drawImage(Graphics g, Image image, int x, int y, int w, int h, ImageObserver observer) {
        if (image == null) return;
        try {
            g.drawImage(image, x, y, w, h, observer);
        } catch (Exception e) {
            LOG.print(e);
        }
    }
}
