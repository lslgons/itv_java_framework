package com.tcom.drawer;

import java.awt.*;
import java.awt.image.ImageObserver;

public class PagerDrawer {

//    /**
//     * 상품목록 하단 페이저 Draw
//     * 페이저 영역 시작 x, y 좌표와 전체 넓이(w)를 기준으로 페이저 중앙에 Draw
//     * @param g
//     * @param x
//     * @param y
//     * @param w
//     * @param curPage
//     * @param totPage
//     * @param isFocus
//     * @param isUp
//     * @param isDown
//     * @param observer
//     */
//    public static void drawPager(Graphics g, int x, int y, int w, int curPage, int totPage, boolean isFocus, boolean isUp, boolean isDown, ImageObserver observer) {
//        Color prevColor = g.getColor();
//        Font prevFont = g.getFont();
//
//        // 하단페이저 구분라인
//        Drawer.drawFillRect(g, x, y, w, 1, C_FFFFFF_40);
//
//        // 페이저 텍스트
//        g.setFont(F14);
//        FontMetrics fm = g.getFontMetrics();
//        String pagerTxt = curPage + "/" + totPage;
//        int pagerTxtW = fm.stringWidth(pagerTxt);
//        int startTxtX = x + (w - pagerTxtW) / 2;
//        g.setColor(C_FF5B5B);
//        Drawer.drawStringLeft(g, "" + curPage, startTxtX, y + 4);
//        g.setColor(C_FFFFFF);
//        Drawer.drawStringLeft(g, "/" + totPage, startTxtX + fm.stringWidth("" + curPage), y + 4);
//
//        // 페이저 상/하 화살표
//        if (totPage > 1) {
//            int startUpX = startTxtX - 39;
//            int startDownX = startTxtX + pagerTxtW + 21;
//
//            Drawer.drawImage(g, isFocus && isUp ? _page_up_focus : _page_up, startUpX, y + 8, 18, 9, observer);
//            Drawer.drawImage(g, (isFocus && isDown) ? _page_down_focus : _page_down, startDownX, y + 8, 18, 9, observer);
//        }
//
//        g.setColor(prevColor);
//        g.setFont(prevFont);
//    }
//
//    /**
//     * 페이저 우측정렬 Draw
//     * @param g
//     * @param x
//     * @param y
//     * @param font
//     * @param curPage
//     * @param totPage
//     */
//    public static void drawPagerRight(Graphics g, int x, int y, Font font, int curPage, int totPage) {
//        Font prevFont = g.getFont();
//
//        g.setFont(font);
//
//        FontMetrics fm = g.getFontMetrics();
//        String pagerTxt = "/" + totPage;
//        int curPageEndX = x - fm.stringWidth(pagerTxt);
//        g.setColor(C_FF5B5B);
//        Drawer.drawStringRight(g, "" + curPage, curPageEndX, y);
//        g.setColor(DrawUtil.C_FFFFFF);
//        Drawer.drawStringRight(g, pagerTxt, x, y);
//
//        g.setFont(prevFont);
//    }
}
