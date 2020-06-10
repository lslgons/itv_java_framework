package com.tcom.drawer;

import java.awt.*;

public class StringDrawer {

    /**
     * 텍스트 중앙 정렬, y는 상단
     * @param g
     * @param str
     * @param x
     * @param y
     */
    public static void drawStringCenter(Graphics g, String str, int x, int y, int w) {
        if (str == null) return;
        FontMetrics fm = g.getFontMetrics();
        int modX = x + ((w - fm.stringWidth(str)) / 2);
        int modY = y + fm.getAscent() - (fm.getDescent() / 2);
        g.drawString(str, modX, modY);
    }

    /**
     * 텍스트 중앙 정렬, y는 상단
     * @param g
     * @param str
     * @param x
     * @param y
     * @param w
     * @param lineHeight
     */
    public static void drawStringCenter(Graphics g, String[] str, int x, int y, int w, int lineHeight) {
        if (str == null) return;
        FontMetrics fm = g.getFontMetrics();
        int modY = y + fm.getAscent() - (fm.getDescent() / 2);
        for (int i = 0; i < str.length; i++) {
            int modX = x + ((w - fm.stringWidth(str[i])) / 2);
            g.drawString(str[i], modX, modY);
            modY += lineHeight;
        }
    }

    /**
     * 텍스트 중앙 정렬, y는 상단
     * 실제 Draw 되는 x좌표는 w기준 중앙으로 재계산
     * 실제 Draw 되는 y좌표는 h기준 중앙으로 재계산
     * @param g
     * @param str
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public static void drawStringCenter(Graphics g, String str, int x, int y, int w, int h) {
        if (str == null) return;
        FontMetrics fm = g.getFontMetrics();
        int modX = x + ((w - fm.stringWidth(str)) / 2);
        int modY = y + (h - fm.getAscent())/2 + fm.getAscent() - (fm.getDescent() / 2);
        g.drawString(str, modX, modY);
    }

    /**
     * 텍스트 중앙 정렬, y는 상단
     * 실제 Draw 되는 x좌표는 w기준 중앙으로 재계산
     * 실제 Draw 되는 y좌표는 h기준 중앙으로 재계산
     * @param g
     * @param str
     * @param x
     * @param y
     * @param w
     * @param h
     * @param font
     * @param color
     */
    public static void drawStringCenter(Graphics g, String str, int x, int y, int w, int h, Font font, Color color) {
        if (str == null) return;

        Font prevFont = g.getFont();
        Color prevColor = g.getColor();

        g.setFont(font);
        g.setColor(color);

        drawStringCenter(g, str, x, y, w, h);

        g.setFont(prevFont);
        g.setColor(prevColor);
    }

    /**
     * 텍스트 중앙 정렬, 시작 y좌표는 h기준으로 전체 텍스트가 가운데위치하도록 재계산
     * @param g
     * @param str
     * @param x
     * @param y
     * @param w
     * @param h
     * @param lineHeight
     */
    public static void drawStringCenter(Graphics g, String[] str, int x, int y, int w, int h, int lineHeight) {
        if (str == null || str.length < 1) return;
        int totH = str.length * lineHeight;
        int startY = y + (h - totH) / 2;
        for (int i = 0; i < str.length; i++) {
            drawStringCenter(g, str[i], x, startY + (lineHeight * i), w, lineHeight);
        }
    }

    /**
     * 텍스트 중앙 정렬, 시작 y좌표는 h기준으로 전체 텍스트가 가운데위치하도록 재계산
     * @param g
     * @param str
     * @param x
     * @param y
     * @param w
     * @param h
     * @param lineHeight
     * @param font
     * @param color
     */
    public static void drawStringCenter(Graphics g, String[] str, int x, int y, int w, int h, int lineHeight, Font font, Color color) {
        Font prevFont = g.getFont();
        Color prevColor = g.getColor();

        g.setFont(font);
        g.setColor(color);

        drawStringCenter(g, str, x, y, w, h, lineHeight);

        g.setFont(prevFont);
        g.setColor(prevColor);
    }


    /**
     * 텍스트 좌측정렬
     * @param g
     * @param str
     * @param x
     * @param y
     */
    public static void drawStringLeft(Graphics g, String str, int x, int y) {
        if (str == null) return;

        FontMetrics fm = g.getFontMetrics();
//        if (isDebugLine) {
//            Color c = g.getColor();
//            g.setColor(Color.cyan);
//            g.drawRect(x, y, fm.stringWidth(str) - 1, fm.getAscent() - 1);
//            g.setColor(c);
//        }
        g.drawString(str, x, y + fm.getAscent() - (fm.getDescent() / 2));
    }

    /**
     * 텍스트 좌측정렬
     * @param g
     * @param str
     * @param x
     * @param y
     * @param lineHeight
     */
    public static void drawStringLeft(Graphics g, String[] str, int x, int y, int lineHeight) {
        if (str == null) return;

        for (int i = 0; i < str.length; i++) {
            drawStringLeft(g, str[i], x, y + lineHeight * i);
        }
    }

    public static void drawStringLeft(Graphics g, String[] str, int x, int y, int lineHeight, Font f, Color c) {
        if (str==null) return;
        Color prevColor=g.getColor();
        Font prevFont=g.getFont();
        g.setColor(c);
        g.setFont(f);
        for (int i = 0; i < str.length; i++) {
            drawStringLeft(g, str[i], x, y + lineHeight * i);
        }

        g.setColor(prevColor);
        g.setFont(prevFont);
    }


    /**
     * 텍스트 좌측정렬
     * 실제 Draw 되는 y좌표는 h기준 중앙으로 재계산
     * @param g
     * @param str
     * @param x
     * @param y
     * @param h
     */
    public static void drawStringLeft(Graphics g, String str, int x, int y, int h) {
        if (str == null) return;

//        if (isDebugLine) {
//            Color c = g.getColor();
//            g.setColor(Color.cyan);
//            g.drawRect(x, y, g.getFontMetrics().stringWidth(str), h - 1);
//            g.setColor(c);
//        }
        drawStringLeft(g, str, x, y + (h - g.getFontMetrics().getHeight()) / 2);
    }

    /**
     * 텍스트 좌측정렬
     * 실제 Draw 되는 y좌표는 h기준 중앙으로 재계산
     * @param g
     * @param str
     * @param x
     * @param y
     * @param h
     * @param lineHeight
     */
    public static void drawStringLeftMiddle(Graphics g, String[] str, int x, int y, int h, int lineHeight) {
        if (str == null || str.length < 1) return;
        int totH = str.length * lineHeight;
        int startY = y + (h - totH) / 2;
        for (int i = 0; i < str.length; i++) {
            drawStringLeft(g, str[i], x, startY + (lineHeight * i));
        }
    }

    /**
     * 텍스트 우측정렬
     * @param g
     * @param str
     * @param x
     * @param y
     */
    public static void drawStringRight(Graphics g, String str, int x, int y) {
        if (str == null) return;

        FontMetrics fm = g.getFontMetrics();
//        if (isDebugLine) {
//            Color c = g.getColor();
//            g.setColor(Color.cyan);
//            g.drawRect(x, y, fm.stringWidth(str) - 1, fm.getAscent() - 1);
//            g.setColor(c);
//        }
        g.drawString(str, x - fm.stringWidth(str), y + fm.getAscent() - (fm.getDescent() / 2));
    }
}
