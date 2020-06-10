package com.tcom.drawer;


import com.tcom.ssr.SSRConfig;
import com.tcom.util.LOG;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.HashMap;

/**
 * 드로잉 유틸리티
 * @author daegon.kim
 * @since 2016-12-07
 */
public class Drawer {

    /**
     * get Font from cache (0:plain, 1:bold, 2:italic)
     * @param size
     * @param type
     * @return
     */
    static HashMap fontList[]=new HashMap[3];
    public static Font getFont(int size, int type) {
        if (fontList[type]==null) fontList[type]=new HashMap();
        Font f= (Font) fontList[type].get(String.valueOf(size));
        if(f==null) {
            f=new Font(SSRConfig.getInstance().ITV_FONT, type, size);
            fontList[type].put(String.valueOf((type)), f);
        }
        return f;
    }

    /**
     * 배경 Dimm처리
     * @param g Graphics
     * @param color 배경색
     */
    public static void setDimm(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(0,0, SSRConfig.getInstance().SCENE_WIDTH, SSRConfig.getInstance().SCENE_HEIGHT);
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



    /**
     * 포커스 draw
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     * @param strokeSize
     * @param color
     */
    public static void drawFocus(Graphics g, int x, int y, int w, int h, int strokeSize, Color color) {
        Color prevColor = g.getColor();

        g.setColor(color);
        g.fillRect(x, y, w, strokeSize);
        g.fillRect(x, y + strokeSize, strokeSize, h - strokeSize - strokeSize);
        g.fillRect(x + w - strokeSize, y + strokeSize, strokeSize, h - strokeSize - strokeSize);
        g.fillRect(x, y + h - strokeSize, w, strokeSize);

        g.setColor(prevColor);
    }

    /**
     * 지정된 영역 만큼 전달된 Color로 채워진 사각형을 그린다
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     * @param color
     */
    public static void drawFillRect(Graphics g, int x, int y, int w, int h, Color color) {
        Color prevColor = g.getColor();

        g.setColor(color);
        g.fillRect(x, y, w, h);

        g.setColor(prevColor);

    }

    /**
     * 전달된 영역을 제외한 영역을 전달된 Color로 채운다
     * @param g
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public static void drawFillRectMask(Graphics g, int startX, int startY, int endX, int endY, Color color) {
        int width=SSRConfig.getInstance().SCENE_WIDTH;
        int height=SSRConfig.getInstance().SCENE_HEIGHT;
        drawFillRect(g, 0, 0, width, startY, color);
        drawFillRect(g, 0, startY, startX, endY, color);
        drawFillRect(g, startX+endX, startY, width-(startX+endX), endY, color);
        drawFillRect(g, 0, startY+endY, width, height - (startY+endY), color);
    }




    /**
     * 점선 그리기
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     * @param dashW
     * @param gap
     * @param color
     */
    public static void drawDashLine(Graphics g, int x, int y, int w, int h, int dashW, int gap, Color color) {
        int totW = 0;
        g.setColor(color);
        while (totW <= w) {
            g.fillRect(x + totW, y, dashW, h);
            totW = totW + dashW + gap;
        }
    }
}
