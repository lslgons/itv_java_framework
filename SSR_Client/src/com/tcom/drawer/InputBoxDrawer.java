package com.tcom.drawer;

import java.awt.*;

public class InputBoxDrawer {
    /**
     * 인풋박스와 포커스 및 입력값 draw
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     * @param focus
     * @param str
     * @param font
     */
    public static void drawInputArea(Graphics g, int x, int y, int w, int h, boolean focus, String str, Font font) {
        drawInputArea(g, x, y, w, h, focus, false, str, font);
    }

    /**
     * 인풋박스와 포커스 및 입력값 draw
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     * @param focus
     * @param isDim
     * @param str
     * @param font
     */
    public static void drawInputArea(Graphics g, int x, int y, int w, int h, boolean focus, boolean isDim, String str, Font font) {
        drawInputArea(g, x, y, w, h, focus, str, font, isDim ? DefinedColor.C_FFFFFF_40 : DefinedColor.C_FFFFFF);
    }

    /**
     * 인풋박스와 포커스 및 입력값 draw
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     * @param focus
     * @param str
     * @param font
     * @param color
     */
    public static void drawInputArea(Graphics g, int x, int y, int w, int h, boolean focus, String str, Font font, Color color) {
        drawInputBox(g, x, y, w, h, focus);
        StringDrawer.drawStringCenter(g, str, x, y, w, h, font, color);
    }

    /**
     * 인풋박스 및 포커스 draw
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     * @param focus
     */
    public static void drawInputBox(Graphics g, int x, int y, int w, int h, boolean focus) {
        drawInputBox(g, x, y, w, h, focus, false);
    }

    /**
     * 인풋박스 및 포커스 draw
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     * @param focus
     * @param isDim
     */
    public static void drawInputBox(Graphics g, int x, int y, int w, int h, boolean focus, boolean isDim) {
        drawInputBox(g, x, y, w, h, focus, DefinedColor.C_3C3C3C, isDim ? DefinedColor.C_AAAAAA_30 : DefinedColor.C_AAAAAA, DefinedColor.C_FF5B5B, 2);
    }

    /**
     * 인풋박스 및 포커스 draw
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     * @param focus
     * @param bgColor
     * @param norStrokeColor
     * @param focusStrokeColor
     * @param strokeSize
     */
    public static void drawInputBox(Graphics g, int x, int y, int w, int h, boolean focus, Color bgColor, Color norStrokeColor, Color focusStrokeColor, int strokeSize) {
        Drawer.drawFillRect(g, x, y, w, h, bgColor);
        Drawer.drawFocus(g, x, y, w, h, focus ? 4 : strokeSize, focus ? focusStrokeColor : norStrokeColor);
    }
}
