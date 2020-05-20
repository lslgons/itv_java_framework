package cjtosplus.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Vector;

import com.cj.tvui.ui.Scene;

public class StaticComScene extends Scene{
	
	/** 하단 Bar 관련 */
	public static final int BTM_ICON_BACK = 0;
	public static final int BTM_ICON_CLOSE = 1;
	public static final int BTM_ICON_SMALL = 2;
	public static final int BTM_ICON_MOVE = 3;
	public static final int BTM_ICON_OK= 4;
	
	public static final int BTM_MODE_COMMON = 0;
	public static final int BTM_MODE_FULLVOD = 1;
	
	public static Color c_black_40 = new Color(0, 0, 0, (255*40)/100);
	public static Color c_282828 = Color.decode("#282828");
	public static Color c_C8C8C8 = Color.decode("#c8c8c8");
	public static Font F13 = new Font("Korean iTV SanSerifD", Font.PLAIN, 13);
	public static Font F20 = new Font("Korean iTV SanSerifD", Font.PLAIN, 20);
	private static Image[] _btmIconImgs = new Image[5];
	
	private int currentBTN_MODE = 0;
	
	// 이전
	private static Image _common_bottom_back;
	// 닫기
	private static Image _common_bottom_close;
	// 작게보기
	private static Image _common_bottom_small;
	// 이동
	private static Image _common_bottom_move;
	// 선택
	private static Image _common_bottom_ok;
	
	
	private static int[] _btmIconWidth = {48, 48, 48, 48, 48};
	
	/*
	 * #####################################################################
	 *  상품 Type
	 * #####################################################################
	 */
	/**
	 * 일반상품
	 */
	public static final String PRODUCT_TYPE_NORMAL = "0";
	/**
	 * 상담상품
	 */
	public static final String PRODUCT_TYPE_COUNSEL = "1";
	/**
	 * 보험상품
	 */
	public static final String PRODUCT_TYPE_INSURANCE = "2";
	/**
	 * 렌탈상품
	 */
	public static final String PRODUCT_TYPE_RENTAL = "4";
	
	private String PRODUCT_TYPE="";
	
	
	
	public void onInit() {
		System.out.println("StaticComScene is onInit..");
		initImage();

	}

	public void onDataReceived(Object[] objects) {
		String msg = (String)objects[0];
		if (msg.equals("NormalMode")) { // 버튼들의 모드.
			this.setCurrentBTN_MODE(BTM_MODE_COMMON);
		} else if(msg.equals("AVFullSceneMode")) {
			this.setCurrentBTN_MODE(BTM_MODE_FULLVOD);
		}
		
		if(objects.length==2) { // 상품타입에 따른 처리.
			String msg02 = (String)objects[1]; 
			if(msg02.equals("P_TYPE_NORMAL")) { // 일반
				PRODUCT_TYPE = PRODUCT_TYPE_NORMAL;
			}else if(msg02.equals("P_TYPE_INSURANCE")) {
				PRODUCT_TYPE =PRODUCT_TYPE_INSURANCE; // 보험.
			}else if(msg02.equals("P_TYPE_RENTAL")) {// 랜탈
				PRODUCT_TYPE =PRODUCT_TYPE_RENTAL;
			}else { // 상담
				PRODUCT_TYPE=PRODUCT_TYPE_COUNSEL;
			}
		}
		
		
	}

	public void onShow() {
		System.out.println("StaticComScene is.onShow");

	}

	public void onHide() {

	}

	public void onDestroy() {
		if(_common_bottom_back != null) {
			_common_bottom_back.flush();
			_common_bottom_back = null;
		}
		
		if(_common_bottom_close != null) {
			_common_bottom_close.flush();
			_common_bottom_close = null;
		}
		
		if(_common_bottom_small != null) {
			_common_bottom_small.flush();
			_common_bottom_small = null;
		}
		
		if(_common_bottom_move != null) {
			_common_bottom_move.flush();
			_common_bottom_move = null;
		}
		
		if(_common_bottom_ok != null) {
			_common_bottom_ok.flush();
			_common_bottom_ok = null;
		}

		if (c_black_40 != null) {
			c_black_40 = null;
		}

		if (c_282828 != null) {
			c_282828 = null;
		}

		if (c_C8C8C8 != null) {
			c_C8C8C8 = null;
		}

		if (F13 != null) {
			F13 = null;
		}

		if (F20 != null) {
			F20 = null;
		}
		
		if(_btmIconImgs != null){
			for(int i=0; i<_btmIconImgs.length; i++) {
				_btmIconImgs[i].flush();
				_btmIconImgs[i] = null;
			}
		}
		
	}

	public void onKeyDown(int i) {

	}

	public void onPaint(Graphics g) {
//		drawBottomBar(g, new int[] {BTM_ICON_CLOSE, BTM_ICON_MOVE, BTM_ICON_OK}, BTM_MODE_COMMON, OnAirPrdMgr.getInstance().getOnAirProductType(), this);
		if(this.currentBTN_MODE == 0) { // 메인화면
			drawBottomBar(g, new int[] { BTM_ICON_MOVE, BTM_ICON_OK}, BTM_MODE_COMMON, PRODUCT_TYPE, this);
		}else if(this.currentBTN_MODE == 1) { // 크게보기.
			drawBottomBar(g, new int[] {BTM_ICON_CLOSE, BTM_ICON_MOVE, BTM_ICON_OK},BTM_MODE_FULLVOD , PRODUCT_TYPE, this);
		}
		
	}

	public void timerWentOff() {

	}
	
	public int getCurrentBTN_MODE() {
		return currentBTN_MODE;
	}

	public void setCurrentBTN_MODE(int currentBTN_MODE) {
		this.currentBTN_MODE = currentBTN_MODE;
	}

	private void initImage() {
		_common_bottom_back = loadImage("common_bottom_back.png", true);
		_common_bottom_close = loadImage("common_bottom_close.png", true);
		_common_bottom_small = loadImage("common_bottom_small.png", true); 
		_common_bottom_move = loadImage("common_bottom_move.png", true);
		_common_bottom_ok = loadImage("common_bottom_ok.png", true);

		_btmIconImgs[BTM_ICON_BACK] = _common_bottom_back;
		_btmIconImgs[BTM_ICON_CLOSE] = _common_bottom_back;
		_btmIconImgs[BTM_ICON_SMALL] = _common_bottom_back;
		_btmIconImgs[BTM_ICON_MOVE] = _common_bottom_move;
		_btmIconImgs[BTM_ICON_OK] = _common_bottom_ok;
	}
	
	/**
	 * 하단바 Draw
	 * @param g
	 * @param hotKey
	 * @param bottomType
	 * @param productType
	 * @param observer
	 */
	public static void drawBottomBar(Graphics g, int[] hotKey, int bottomType, String productType, ImageObserver observer) {
		drawBottomBar(g, hotKey, bottomType, productType, null, observer);
	}
	
	
	/**
	 * 하단바 Draw
	 * @param g
	 * @param hotKey
	 * @param bottomType
	 * @param productType
	 * @param color
	 * @param observer
	 */
	public static void drawBottomBar(Graphics g, int[] hotKey, int bottomType, String productType, Color color, ImageObserver observer) {
		int bgH = 0;	//하단 영역 높이
		int topY = 0;	//y상단좌표
		int bottomY = 0; //y하단좌표
		int paddingRight = 58;	//우측 여백
		int iconGap = 13;	//아이콘 간격
		int txtH = 26;	//텍스트 영역 높이
		int txtX = 60;
		String tel1 = "";	//자동주문 전화번호
		String tel2 = "";	//상담원 전화번호
		switch (bottomType) {
		case BTM_MODE_COMMON:
			bgH = 60;
			topY = 485;
			bottomY = 510;
			if (color == null) {
				g.setColor(c_black_40);
			} else {
				g.setColor(color);
			}
			break;
		case BTM_MODE_FULLVOD:
			bgH = 48;
			topY = 496;
			bottomY = 522;
			if (color == null) {
				g.setColor(c_282828);
			} else {
				g.setColor(color);
			}
			break;
		}

		//BG
//		g.fillRect(0, Rs.HEIGHT - bgH, Rs.WIDTH, bgH);
		g.fillRect(0, 540 - bgH, 960, bgH);

		//productType에 따른 전화번호 설정
		if (PRODUCT_TYPE_NORMAL.equals(productType)) {
			//일반상품
//			tel1 = SettingDA.getInstance().getDefaultDA().getNorTelArs();
//			tel2 = SettingDA.getInstance().getDefaultDA().getNorTel();
			tel1 ="080-000-0000";
			tel2 ="080-111-1111";
		} else if (PRODUCT_TYPE_INSURANCE.equals(productType)) {
			//보험상품
//			tel1 = SettingDA.getInstance().getDefaultDA().getInsuranceTelArs();
//			tel2 = SettingDA.getInstance().getDefaultDA().getInsuranceTel();
			tel1 ="080-222-2222";
			tel2 ="080-333-3333";
		} else if (PRODUCT_TYPE_RENTAL.equals(productType)) {
			//렌탈상품
//			tel1 = SettingDA.getInstance().getDefaultDA().getRentalTelArs();
//			tel2 = SettingDA.getInstance().getDefaultDA().getRentalTel();
			tel1 ="080-444-4444";
			tel2 ="080-555-5555";
		} else {
//			tel1 = SettingDA.getInstance().getDefaultDA().getNorTelArs();
//			tel2 = SettingDA.getInstance().getDefaultDA().getNorTel();
			tel1 ="080-666-6666";
			tel2 ="080-777-7777";
		}
//		System.out.println("productType=======["+productType+"]]");
		

		//전화번호
		g.setColor(c_C8C8C8);
		g.setFont(F13);
		int modX = 0;
		String tmpStr = "자동주문";
		drawStringLeft(g, tmpStr, txtX + modX, bottomY, txtH);
		modX = g.getFontMetrics().stringWidth(tmpStr) + 4;
		g.setFont(F20);
		String[] _tmp = tokenize(tel1, "-");

		if (_tmp != null) {
			for (int i = 0; i < _tmp.length; i++) {
				if (i > 0) {
					modX += 2;
					g.fillRoundRect(txtX + modX, bottomY - txtH / 2 + 1, 1, 1, 1, 1);
					modX += 3;
				}
				drawStringLeft(g, _tmp[i], txtX + modX, bottomY, txtH);
				modX += g.getFontMetrics().stringWidth(_tmp[i]);
			}
			modX += 13;
		}
		g.setFont(F13);
		tmpStr = "상담전화";
		drawStringLeft(g, tmpStr, txtX + modX, bottomY, txtH);
		modX += g.getFontMetrics().stringWidth(tmpStr) + 4;
		g.setFont(F20);
		_tmp = tokenize(tel2, "-");
		if (_tmp != null) {
			for (int i = 0; i < _tmp.length; i++) {
				if (i > 0) {
					modX += 2;
					g.fillRoundRect(txtX + modX, bottomY - txtH / 2 + 1, 1, 1, 1, 1);
					modX += 3;
				}
				drawStringLeft(g, _tmp[i], txtX + modX, bottomY, txtH);
				modX += g.getFontMetrics().stringWidth(_tmp[i]);
			}
		}

		//아이콘
		if (hotKey != null && hotKey.length > 0) {
			modX = 960 - paddingRight;
			for (int i = hotKey.length - 1; i >= 0; i--) {
				drawImage(g, _btmIconImgs[hotKey[i]], modX - _btmIconWidth[hotKey[i]], topY, _btmIconWidth[hotKey[i]], txtH, observer);
				modX -= _btmIconWidth[hotKey[i]] + iconGap;
			}
		}
	}
	
	
	
	/**
	 * 텍스트 좌측정렬, y는 중앙
	 * @param g
	 * @param str
	 * @param x
	 * @param y
	 * @param gap
	 */
	public static void drawStringLeft(Graphics g, String[] str, int x, int y, int gap) {
		if (str == null) return;
		FontMetrics fm = g.getFontMetrics();
		int fontHeight = fm.getHeight();
		int totalHeight = fontHeight + gap * (str.length - 1);
		int startY = y - totalHeight / 2 + fontHeight;
		for (int i = 0; i < str.length; i++) {
			g.drawString(str[i], x, startY + i * gap - fm.getDescent());
		}
	}
	
	/**
	 * 텍스트 좌측정렬, y는 중앙
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
	 * @param g
	 * @param str
	 * @param x
	 * @param y
	 */
	public static void drawStringLeft(Graphics g, String str, int x, int y) {
		if (str == null) return;
		g.drawString(str, x, y - g.getFontMetrics().getDescent());
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
		g.drawString(str, x, y - (h - g.getFontMetrics().getAscent())/2 - g.getFontMetrics().getDescent() / 2);
	}
	
	/**
	 * 이미지 Draw
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
			System.out.println(e);
		}
	}
	
	public static String[] tokenize(String str, String delim) {
		Vector v = null;
		try {
			v = new Vector();
			String s = str;
			String[] ret = null;
			if (s != null && s.length() > 0) {
				int p;
				while ((p = s.indexOf(delim)) != -1) {
					if (p == 0) {
						v.addElement("");
					} else {
						v.addElement(s.substring(0, p));
					}
					s = s.substring(p + 1);
				}
				v.add(s);
				ret = new String[v.size()];
				ret = (String[]) v.toArray(ret);
			}
			return ret;
		} finally {
			if (v != null) {
				v.clear();
				v = null;
			}
		}
	}
	

}
