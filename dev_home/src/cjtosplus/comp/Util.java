package cjtosplus.comp;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

public class Util {
	/** Font */
	public static Font F10_B = new Font("Korean iTV SanSerifD", Font.BOLD, 10);
	public static Font F11 = new Font("Korean iTV SanSerifD", Font.PLAIN, 11);
	public static Font F11_B = new Font("Korean iTV SanSerifD", Font.BOLD, 11);
	public static Font F12 = new Font("Korean iTV SanSerifD", Font.PLAIN, 12);
	public static Font F12_B = new Font("Korean iTV SanSerifD", Font.BOLD, 12);
	public static Font F13 = new Font("Korean iTV SanSerifD", Font.PLAIN, 13);
	public static Font F14 = new Font("Korean iTV SanSerifD", Font.PLAIN, 14);
	public static Font F14_B = new Font("Korean iTV SanSerifD", Font.BOLD, 14);
	public static Font F15 = new Font("Korean iTV SanSerifD", Font.PLAIN, 15);
	public static Font F16 = new Font("Korean iTV SanSerifD", Font.PLAIN, 16);
	public static Font F16_B = new Font("Korean iTV SanSerifD", Font.BOLD, 16);
	public static Font F18 = new Font("Korean iTV SanSerifD", Font.PLAIN, 18);
	public static Font F18_B = new Font("Korean iTV SanSerifD", Font.BOLD, 18);
	public static Font F20 = new Font("Korean iTV SanSerifD", Font.PLAIN, 20);
	public static Font F22 = new Font("Korean iTV SanSerifD", Font.PLAIN, 22);
	public static Font F22_B = new Font("Korean iTV SanSerifD", Font.BOLD, 22);
	public static Font F27 = new Font("Korean iTV SanSerifD", Font.PLAIN, 27);
	public static Font F28 = new Font("Korean iTV SanSerifD", Font.PLAIN, 28);
	public static Font F36 = new Font("Korean iTV SanSerifD", Font.PLAIN, 36);
	public static Font F36_B = new Font("Korean iTV SanSerifD", Font.BOLD, 36);

	/** Color */
	public static Color c_FFFFFF = Color.decode("#ffffff");
	public static Color c_C8C8C8 = Color.decode("#c8c8c8");
	public static Color c_EC447A = Color.decode("#ec447a");
	public static Color c_303030 = Color.decode("#303030");
	public static Color c_454748 = Color.decode("#454748");
	public static Color c_F65073 = Color.decode("#f65073");
	public static Color c_7F7F7F = Color.decode("#7f7f7f");
	public static Color c_838383 = Color.decode("#838383");
	public static Color c_FF5377 = Color.decode("#ff5377");
	public static Color c_6F6F6F = Color.decode("#6f6f6f");
	public static Color c_7B7B7B = Color.decode("#7b7b7b");
	public static Color c_2E2E2E = Color.decode("#2e2e2e");
	public static Color c_ADADAD = Color.decode("#adadad");
	public static Color c_444444 = Color.decode("#444444");
	public static Color c_919191 = Color.decode("#919191");
	public static Color c_989898 = Color.decode("#989898");
	public static Color c_CECECE = Color.decode("#cecece");
	public static Color c_D62255 = Color.decode("#d62255");
	public static Color c_A7A7A7 = Color.decode("#a7a7a7");
	public static Color c_414141 = Color.decode("#414141");
	public static Color c_282828 = Color.decode("#282828");
	public static Color c_292929 = Color.decode("#292929");
	public static Color c_181818 = Color.decode("#181818");
	public static Color c_272727 = Color.decode("#272727");
	public static Color c_7D7D7D = Color.decode("#7d7d7d");
	public static Color c_D1248C = Color.decode("#d1248c");
	public static Color c_575757 = Color.decode("#575757");
	public static Color c_575757_50 = new Color(87, 87, 87, (255*40)/50);
	public static Color c_494949 = Color.decode("#494949");
	public static Color c_313D4B = Color.decode("#313d4b");
	public static Color c_3E3E3F = Color.decode("#3e3e3f");

	public static Color c_black_30 = new Color(0, 0, 0, (255*30)/100);
	public static Color c_black_40 = new Color(0, 0, 0, (255*40)/100);
	public static Color c_black_70 = new Color(0, 0, 0, (255*70)/100);
	public static Color c_black_90 = new Color(0, 0, 0, (255*90)/100);

	/**
	 * 텍스트 좌측정렬, y는 중앙
	 * 
	 * @param g
	 * @param str
	 * @param x
	 * @param y
	 * @param gap
	 * @param font
	 * @param color
	 */
	public static void drawStringLeft(Graphics g, String[] str, int x, int y, int gap, Font font, Color color) {
		g.setFont(font);
		g.setColor(color);
		drawStringLeft(g, str, x, y, gap);
	}

	/**
	 * 텍스트 좌측정렬
	 * 
	 * @param g
	 * @param str
	 * @param x
	 * @param y
	 * @param font
	 * @param color
	 */
	public static void drawStringLeft(Graphics g, String str, int x, int y, Font font, Color color) {
		g.setFont(font);
		g.setColor(color);
		drawStringLeft(g, str, x, y);
	}

	/**
	 * 텍스트 좌측정렬
	 * 
	 * @param g
	 * @param str
	 * @param x
	 * @param y
	 */
	public static void drawStringLeft(Graphics g, String str, int x, int y) {
		if (str == null)
			return;
		g.drawString(str, x, y - g.getFontMetrics().getDescent());
	}

	/**
	 * 텍스트 좌측정렬 실제 Draw 되는 y좌표는 h기준 중앙으로 재계산
	 * 
	 * @param g
	 * @param str
	 * @param x
	 * @param y
	 * @param h
	 */
	public static void drawStringLeft(Graphics g, String str, int x, int y, int h) {
		if (str == null)
			return;
		g.drawString(str, x, y - (h - g.getFontMetrics().getAscent()) / 2 - g.getFontMetrics().getDescent() / 2);
	}

	public static void drawStringLeft(Graphics g, String[] str, int x, int y, int gap) {
		if (str == null)
			return;
		FontMetrics fm = g.getFontMetrics();
		int fontHeight = fm.getHeight();
		int totalHeight = fontHeight + gap * (str.length - 1);
		int startY = y - totalHeight / 2 + fontHeight;
		for (int i = 0; i < str.length; i++) {
			g.drawString(str[i], x, startY + i * gap - fm.getDescent());
		}
	}
	
	/**
	 * 좌측정렬 strike 효과 문자열 그리기
	 * @param g
	 * @param str
	 * @param x
	 * @param y
	 */
	public static void drawStringLeftWithMiddleLine(Graphics g, String str, int x, int y) {
		if (str != null && str.length() > 0) {
			FontMetrics fm = g.getFontMetrics();
			int width = fm.stringWidth(str);
			g.drawString(str, x, y - fm.getDescent());
			g.fillRect(x + 2, y - (fm.getAscent() / 2) +  - fm.getDescent(), width, fm.getFont().getStyle() + 1);
		}
	}

	/**
	 * 좌측정렬 strike 효과 문자열 그리기
	 * @param g
	 * @param str
	 * @param x
	 * @param y
	 * @param linecolor
	 */
	public static void drawStringLeftWithMiddleLine(Graphics g, String str, int x, int y, Color linecolor) {
		if (str != null && str.length() > 0) {
			FontMetrics fm = g.getFontMetrics();
			int width = fm.stringWidth(str);
			g.drawString(str, x, y - fm.getDescent());
			Color oldcolor = g.getColor();
			g.setColor(linecolor);
			g.fillRect(x + 2, y - (fm.getAscent() / 2), width, fm.getFont().getStyle() + 1);
			g.setColor(oldcolor);
		}
	}

	/**
	 * 문자열 길이 반환
	 * 
	 * @param src
	 * @param fm
	 * @return
	 */
	public static int getStringWidth(String src, FontMetrics fm) {
		return fm.stringWidth(src);
	}

	public static String ELLIPSIS = "..";
	private static final String EMPTY_STRING = "";

	/**
	 * 말줄임 표시
	 * 
	 * @param src
	 * @param fm
	 * @param width
	 * @return
	 */
	public static String shorten(String src, FontMetrics fm, int width) {
		return shorten(src, fm, width, ELLIPSIS);
	}

	public static String shorten(String src, FontMetrics fm, int width, String ellipsis) {
		if (src == null || src.length() <= 0) {
			return EMPTY_STRING;
		}
		if (fm.stringWidth(src) <= width) {
			return src;
		}
		int ew = fm.stringWidth(ellipsis);
		int len;
		do {
			len = src.length();
			if (len <= 1) {
				return (width >= ew) ? ellipsis : EMPTY_STRING;
			}
			src = src.substring(0, len - 1);
		} while (fm.stringWidth(src) + ew > width);
		return src + ellipsis;
	}
	
	public static boolean isNotEmpty(String name) {
		return !isEmpty(name);
	}

	public static boolean isEmpty(String name) {
		return name == null || "".equals(name) || "".equals(name.trim());
	}
	
	
	/**
	 * 포커스 이미지 draw
	 * @param g
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param observer
	 */
//	public static void drawFocus(Graphics g, int x, int y, int w, int h, ImageObserver observer) {
//		drawImage(g, "common_popup_foc_02.png", x, y, w, 4, observer);	// top
//		drawImage(g, "common_popup_foc_01.png", x, y + 4, 4, h - 4 - 4, observer);	// left
//		drawImage(g, "common_popup_foc_03.png", x + w - 4, y + 4, 4, h - 4 - 4, observer);	// right
//		drawImage(g, "common_popup_foc_02.png", x, y + h - 4, w, 4, observer);	// bottom
//	}
	

	/**
	 * 텍스트 우측정렬
	 * @param g
	 * @param str
	 * @param x
	 * @param y
	 */
	public static void drawStringRight(Graphics g, String str, int x, int y) {
		if (str == null) return;
		g.drawString(str, x - g.getFontMetrics().stringWidth(str), y - g.getFontMetrics().getDescent());
	}

	/**
	 * 텍스트 우측정렬
	 * 실제 Draw 되는 y좌표는 h기준 중앙으로 재계산
	 * @param g
	 * @param str
	 * @param x
	 * @param y
	 * @param h
	 */
	public static void drawStringRight(Graphics g, String str, int x, int y, int h) {
		if (str == null) return;
		g.drawString(str, x - g.getFontMetrics().stringWidth(str), y - (h - g.getFontMetrics().getAscent()) / 2 - g.getFontMetrics().getDescent() / 2);
	}

	/**
	 * 텍스트 우측정렬
	 * @param g
	 * @param str
	 * @param x
	 * @param y
	 * @param font
	 * @param color
	 */
	public static void drawStringRight(Graphics g, String str, int x, int y, Font font, Color color) {
		g.setFont(font);
		g.setColor(color);
		drawStringRight(g, str, x, y);
	}

}

