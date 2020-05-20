package cjtosplus.popup;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import com.cj.tvui.Keys;
import com.cj.tvui.network.HttpConnect;
import com.cj.tvui.ui.Popup;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.Drawer;
import com.cj.tvui.util.ImageUtil;

import cjtosplus.comp.Util;
import cjtosplus.comp.main.FlowTextBar;

public class SchedulePopup extends Popup {
	
	/**
	 * 편성표 상단첫줄 renderer
	 */
	private TimeLineRenderer timeLineRenderer1;
	/**
	 * 편성표 상단두번쩨줄 renderer
	 */
	private TimeLineRenderer timeLineRenderer2;
	/**
	 * 편성표 하단첫줄 renderer
	 */
	private TimeLineRenderer timeLineRenderer3;
	/**
	 * 편성표 하단두번쩨줄줄 renderer
	 */
	private TimeLineRenderer timeLineRenderer4;
	
	private Image productImage;
	
	
	/**
	 * 포커스 상품 방송시간텍스트 길이
	 */
	private int timeStrFW = 0;
	
	/**
	 * 포커스 상품 판매가 길이
	 */
	private int priceFW = 0;
	
	/**
	 * 포커스 상품 이미지 Url
	 */
	private String prdImgUrl;
	
	/**
	 * 현재 포커스 인덱스
	 */
//	private int focus = 0;
	private int focus = 4; // Test용.
	
	private ResScheduleList list;
	
	/**
	 * 포커스 상품 방송시간
	 */
//	private String timeStrF = null;
	private String timeStrF = "15:30 ~ 16:30";
	
	/**
	 * 포커스 상품 현재방송중 상품 여부
	 */
	private boolean isOnAirF = true;
	/**
	 * 포커스 상품 상품명
	 */
	private String[] prdNmF = new String[2];
	/**
	 * 포커스 상품 판매가
	 */
//	private String slPrice = null;
	private String slPrice = "79,000원";
	
	/**
	 * 포커스 상품 고객맞춤가
	 */
//	private String lastprice = null;
	private String lastprice = "78,900원";
	
	private Image main_icon_onair;
	private Image main_arrow_timetable_price;
	
	private Image focus01;
	private Image focus02;
	private Image focus03;
	private Image focus04;
	
	private Image main_bg_timetable_dim;
	private Image main_bg_timetable_list2;
	private Image main_bg_timetable_list_onair;

//	private String[][] sampleData = new String[10][8]; 
	private String[][] sampleData = new String[][] 
//			 "썸네일 URL", "VOD여부", "방송시간","온에어여부","상품명01","상품명02","본가격","할인가격" 
		{ { "http://210.122.102.171:80/goods_images/47/694/47313694OPJ.jpg", "F", "08:00~08:55","F","조르지오페리 루비 크로커백팩","4종세트","59,000월","58,900원" }, 
		{ "http://210.122.102.171:80/goods_images/47/228/47142228OPJ.jpg", "T", "09:00~10:00","T","송지오 2017 소프트웜", "기모 본딩팬츠 4종 set", "79,900원","78,900원" }, 
		{ "http://210.122.102.171:80/goods_images/47/151/47200151OPJ.jpg", "T", "09:10~10:10","T","송지오 2017 01소프트웜", "기모 본딩팬츠 40종 set", "79,100원","78,100원" }, 
		{ "http://210.122.102.171:80/goods_images/47/157/47149157OPJ.jpg", "T", "09:20~10:20","T","송지오 2017 02소프트웜", "기모 본딩팬츠 41종 set", "79,200원","78,200원" }, 
		{ "http://210.122.102.171:80/goods_images/47/228/47142228OPJ.jpg", "T", "09:30~10:30","T","송지오 2017 03소프트웜", "기모 본딩팬츠 42종 set", "79,300원","78,300원" }, 
		{ "http://210.122.102.171:80/goods_images/47/694/47313694OPJ.jpg", "T", "09:40~10:40","T","송지오 2017 04소프트웜", "기모 본딩팬츠 43종 set", "79,400원","78,400원" }, 
		{ "http://210.122.102.171:80/goods_images/46/455/46828455OPJ.jpg", "T", "09:50~10:50","T","송지오 2017 05소프트웜", "기모 본딩팬츠 44종 set", "79,500원","78,500원" }, 
		{ "http://210.122.102.171:80/goods_images/43/360/43235360OPJ.jpg", "T", "09:55~10:55","T","송지오 2017 06소프트웜", "기모 본딩팬츠 45종 set", "79,600원","78,600원" }, 
		{ "http://210.122.102.171:80/goods_images/47/228/47142228OPJ.jpg", "T", "10:00~11:00","T","송지오 2017 07소프트웜", "기모 본딩팬츠 46종 set", "79,700원","78,700원" }, 
		{ "http://210.122.102.171:80/goods_images/47/157/47149157OPJ.jpg", "F", "11:00~12:00","F","제이코시 패딩 올코드&스카프", "2종","69,000원", "68,000원" } }; // Sample Data.


	public SchedulePopup(Scene parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}


	public void onInit() {
		// TODO Auto-generated method stub
		System.out.println("SchedulePopup onInit is Ok......");
		
		timeLineRenderer1 = new TimeLineRenderer(656, 54, true);
		timeLineRenderer2 = new TimeLineRenderer(656, 54 + 68 + 1, true);
		timeLineRenderer3 = new TimeLineRenderer(656, 54 + (68 + 1) * 2 + 150 + 1, false);
		timeLineRenderer4 = new TimeLineRenderer(656, 54 + (68 + 1) * 3 + 150 + 1, false);
		
//		add(timeLineRenderer1);
//		add(timeLineRenderer2);
//		add(timeLineRenderer3);
//		add(timeLineRenderer4);
		
		timeLineRenderer1.setVisible(true);
		timeLineRenderer2.setVisible(true);
		timeLineRenderer3.setVisible(true);
		timeLineRenderer4.setVisible(true);
//		timeLineRenderer4.isVisible = false;

		
		addBackgroundImage("image_frame_150", loadImage("image_frame_150.png", true),  506, 192, 150, 150);
		getBackgroundImage("image_frame_150").setVisible(true);
		
		addBackgroundImage("main_icon_vod", loadImage("main_icon_vod.png", true), 556, 242, 50, 50);
		addBackgroundImage("main_arrow_up", loadImage("main_arrow_up.png", true), 766, 162, 30, 30); // 포커스상품
		getBackgroundImage("main_arrow_up").setVisible(true);
		
		addBackgroundImage("main_arrow_down", loadImage("main_arrow_down.png", true), 766, 342, 30, 30);
		getBackgroundImage("main_arrow_down").setVisible(true);
		
//		addBackgroundImage("main_icon_onair", loadImage("main_icon_onair.png", true), 668 + timeStrFW + 5, 205, 45, 22);
//		addBackgroundImage("main_icon_onair", loadImage("main_icon_onair.png", true), 668 + timeStrFW + 5+115, 205, 45, 22);
//		getBackgroundImage("main_icon_onair").setVisible(true);
		main_icon_onair = loadImage("main_icon_onair.png", true);
		
		addBackgroundImage("main_icon_onair_dim", loadImage("main_icon_onair_dim.png", true), 61,36+320-3,625,3);
		getBackgroundImage("main_icon_onair_dim").setVisible(false);
		
//		addBackgroundImage("main_arrow_timetable_price", loadImage("main_arrow_timetable_price.png", true), 668 + priceFW, 304, 24, 22);
//		getBackgroundImage("main_arrow_timetable_price").setVisible(false);
//		addBackgroundImage("main_arrow_timetable_price", loadImage("main_arrow_timetable_price.png", true), 668 + priceFW+58, 304, 24, 22);
//		getBackgroundImage("main_arrow_timetable_price").setVisible(true);
		
		main_arrow_timetable_price = loadImage("main_arrow_timetable_price.png", true);
		
//		addBackgroundImage("main_bg_timetable_dim", loadImage("main_bg_timetable_dim.png", true), 0, 0, 960 - 300, 480); // 일단 막어둔다. 20min. // 4회 이상 호출되는 듯. 검게 나온다.
//		addBackgroundImage("main_bg_timetable_list2", loadImage("main_bg_timetable_list2.png", true), 636, 0, 324, 480);
//		addBackgroundImage("main_bg_timetable_list_onair", loadImage("main_bg_timetable_list_onair.png", true), 483, 175, 173, 183); 
		
	}
	
	public void onShow() {
		// TODO Auto-generated method stub
		System.out.println("SchedulePopup onShow is Ok......");
		
		setTimeLineInitData();
		
		setCurrentTimeLineData(this.focus);
		
		setTimeLineData();
	}
	
	public void onHide() {
		// TODO Auto-generated method stub
		
	}
	
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		if(productImage!= null) {
			productImage.flush();
			productImage = null;
		}
		
		if(main_icon_onair != null) {
			main_icon_onair.flush();
			main_icon_onair = null;
		}
		
		if(main_arrow_timetable_price != null) {
			main_arrow_timetable_price.flush();
			main_arrow_timetable_price = null;
		}
		
		if(focus01 != null) {
			focus01.flush();
			focus01 = null;
		}
		
		if(focus02 != null) {
			focus02.flush();
			focus02 = null;
		}
		
		if(focus03 != null) {
			focus03.flush();
			focus03 = null;
		}
		
		if(focus04 != null) {
			focus04.flush();
			focus04 = null;
		}
		
		if (main_bg_timetable_dim != null) {
			main_bg_timetable_dim.flush();
			main_bg_timetable_dim = null;
		}

		if(main_bg_timetable_list2 != null) {
			main_bg_timetable_list2.flush();
			main_bg_timetable_list2 = null;
		}
		
		if(main_bg_timetable_list_onair != null) {
			main_bg_timetable_list_onair.flush();
			main_bg_timetable_list_onair = null;
		}
		
	}
	
	public void onDataReceived(Object[] arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onKeyDown(int keyCode) {
		// TODO Auto-generated method stub
		 if(keyCode == Keys.VK_DOWN) {
//           openPopup("cjtcom.popup.SubPopup", null);
//			 getBackgroundImage("main_icon_onair_dim").setVisible(false);
			 
			
			 this.focus++;
			 if(this.focus > 7) { // 현재 10개의 데이타만 임의로 넣음.
				 this.focus = 7;
			 }
			 this.setCurrentTimeLineData(this.focus);
			 
			 setTimeLineData();
			
//			 repaint();
			
			 
			 
       } else if(keyCode == Keys.VK_OK){
//			close();
		}else if(keyCode == 8) { // BackSpace key.
			close();
		}else if(keyCode == Keys.VK_UP) {
//			getBackgroundImage("main_icon_onair_dim").setVisible(true);
			 
			 this.focus--;
			 System.out.println("this.focus value is ["+this.focus+"]");
			 if(this.focus < 2) {
				 this.focus = 2;
			 }
			 this.setCurrentTimeLineData(this.focus);
			 
			 setTimeLineData();
			 
		}
	}

	public void onPaint(Graphics g) { // Componet 보다 먼저 불리는 계층.
		// TODO Auto-generated method stub
//		 LOG.print(this, "MainPopup Paint");
/*	        g.setFont(new Font("Korean iTV SanSerifD", Font.PLAIN, 17));
	        
//	        super.paint(g);
	        
//	        super.repaint();
	        
//	        timeLineRenderer1.render(g);
//			timeLineRenderer2.render(g);
//			timeLineRenderer3.render(g);
//			timeLineRenderer4.render(g);
			
			
			
			
//	        Drawer.setDimm(g, new Color(0,0,0,200)); // 딤처리...
	        
	        g.setColor(Color.BLUE);
	        g.fillRect(270,100,400,30+20);
	        
	        g.setColor(Color.CYAN);
	        g.fillRect(270,100+30+20,400,300-20);
	        
	        g.setColor(Color.WHITE);
	        g.drawString("* [INFORMATION] *", 270+130, 100 +30);
	        
	        g.setColor(Color.BLACK);
	        g.drawString("PopUp Scene is Visible OK", 270+100, 100 +30+60);
	        
	        ///////button/////////////////
	        g.setColor(Color.YELLOW);
	        g.fillRect(270+150,210+160,90,40);
	        
	        g.setColor(Color.BLACK);
	        g.drawString("  CLOSE", 270+150+5,210 + 25+160);
	        
	        g.setColor(Color.BLACK);
	        g.drawRect(270+150,210+160,90,40);
	        ///////////////////////////////
	        */
//		Drawer.setDimm(g, new Color(0,0,0,70)); // 딤처리...
	         
	}
	
	public void paint(Graphics g) { // 컴포넌트 보다 위의 계층.
		super.paint(g);
		
		
		g.drawImage(main_bg_timetable_dim, 0, 0, 960 - 300, 480, this); // 2회~3회 정도 호출되는 듯. 검게 나온다.
//		g.drawImage(main_bg_timetable_dim, 0, 0, 100, 200, this);
		g.drawImage(main_bg_timetable_list2, 636, 0, 324, 480, this);
		g.drawImage(main_bg_timetable_list_onair,483, 175, 173, 183, this);
		
//		System.out.println("==============SchedulePopup Paint is Good...");
		
		
//		Drawer.setDimm(g, new Color(0,0,0,100)); // 딤처리...
		
		timeLineRenderer1.render(g);
		timeLineRenderer2.render(g);
		
		/**
		 * 포커스상품
		 */
		if (timeLineRenderer2.isVisible()) {
//			DrawUtil.drawImage(g, main_arrow_up, 766, 162, 30, 30, this);
			getBackgroundImage("main_arrow_up").setVisible(true);
		}

		//상품이미지 연동
		if (Util.isNotEmpty(this.prdImgUrl) /* && PrdImagePool.getInstance().hasCacheImage(this.prdImgUrl) */) {
//			DrawUtil.drawImage(g, PrdImagePool.getInstance().returnCacheImage(this.prdImgUrl), 506, 192, 150, 150, this);
			g.drawImage(productImage, 506, 192, 150, 150, this);
		} else {
//			g.drawImage(ImageUtil.createImage(HttpConnect.ImgRequest("http://210.122.102.171:80/goods_images/47/667/47142667OPJ.jpg")), 506, 192, 150, 150, this);
//			DrawUtil.drawImage(g, image_default_150, 506, 192, 150, 150, this);
			g.drawImage(productImage, 506, 192, 150, 150, this);
		}
		
/*		if (Util.isNotEmpty(this.list.assetId[this.focus])) {
			g.setColor(Util.c_black_30);
			g.fillRect(506, 192, 150, 150);
		}*/ // 추후 처리 20min..
		g.setColor(Util.c_black_30);
		g.fillRect(506, 192, 150, 150);
		
//		Util.drawImage(g, image_frame_150, 506, 192, 150, 150, this); 
		getBackgroundImage("image_frame_150").setVisible(true);
		
		this.drawFocus(g, 506, 192, 150, 150, this); // 포커스 
		
	/*	if (Util.isNotEmpty(this.list.assetId[this.focus])) {
//			DrawUtil.drawImage(g, main_icon_vod, 556, 242, 50, 50, this);
			getBackgroundImage("main_icon_vod").setVisible(true);
		} */ // 추후 처리 20min..
		getBackgroundImage("main_icon_vod").setVisible(true);

		//방송시간
		g.setFont(Util.F18);
		g.setColor(Util.c_FFFFFF);
		Util.drawStringLeft(g, timeStrF, 668, 227);
		//OnAir Icon
		if (isOnAirF) {
//			DrawUtil.drawImage(g, main_icon_onair, 668 + timeStrFW + 5, 205, 45, 22, this);
//			getBackgroundImage("main_icon_onair").setBounds(668 + timeStrFW + 5, 205, 45, 22);
//			getBackgroundImage("main_icon_onair").setVisible(true);
			g.drawImage( main_icon_onair, 668 + timeStrFW + 5, 205, 45, 22, this);
		}
		//상품명
		g.setFont(Util.F16);
		Util.drawStringLeft(g, prdNmF[0], 668, 252);
		Util.drawStringLeft(g, prdNmF[1], 668, 252 + 22 + 4);
		g.setFont(Util.F16);
		g.setColor(Util.c_838383);
		Util.drawStringLeftWithMiddleLine(g, slPrice, 668, 326 - (22 - g.getFontMetrics().getHeight()) / 2);
		if (priceFW > 0) {
//			Util.drawImage(g, main_arrow_timetable_price, 668 + priceFW, 304, 24, 22, this);
//			getBackgroundImage("main_arrow_timetable_price").setBounds(668 + priceFW, 304, 24, 22);
//			getBackgroundImage("main_arrow_timetable_price").setBounds(668 + priceFW, 304, 24, 22);
//			getBackgroundImage("main_arrow_timetable_price").setLocation(668 + priceFW, 304);
//			getBackgroundImage("main_arrow_timetable_price").setVisible(true);
			g.drawImage(main_arrow_timetable_price, 668 + priceFW, 304, 24, 22, this);
//			System.out.println("1888888888888888888888888888888888["+priceFW+"]");
		}
		
//		getBackgroundImage("main_arrow_timetable_price").setLocation(668 + priceFW, 304);
//		getBackgroundImage("main_arrow_timetable_price").setVisible(true);
		
		g.setFont(Util.F18);
		g.setColor(Util.c_FFFFFF);
		Util.drawStringLeft(g, lastprice, 668 + priceFW + (priceFW > 0 ? 24 : 0), 326 - 1, 22);

		if (timeLineRenderer3.isVisible()) {
//			DrawUtil.drawImage(g, main_arrow_down, 766, 342, 30, 30, this);
			getBackgroundImage("main_arrow_down").setVisible(true);
		}
		
		timeLineRenderer3.render(g);
		timeLineRenderer4.render(g);
		
	

	}

	public void timerWentOff() {
		// TODO Auto-generated method stub
		
	}
	
	public class TimeLineRenderer  {
		/**
		 * 노출 여부
		 */
		private boolean isVisible = false;
		/**
		 * 이전 편성표 여부
		 */
		private boolean isPreSchedul = false;
		/**
		 * 기준 X좌표
		 */
		int baseX = 0;
		/**
		 * 기준 Y좌표
		 */
		int baseY = 0;
		/**
		 * 상단여백
		 */
		int paddingTop = 12;
		/**
		 * 좌측 여백
		 */
		int paddingLeft = 12;
		/**
		 * 방송시간 높이
		 */
		int timeH = 22;
		/**
		 * 라인간격
		 */
		int lineGap = 4;
		/**
		 * 상품명 높이
		 */
		int prdNmH = 16;
		/**
		 * 최대넓이
		 */
		int maxW = 242;

		/**
		 * 방송시간
		 */
		String timeStr = "11:15 ~ 12:15";
		/**
		 * 상품명
		 */
		String prdNm = "키친플라워 시그니쳐 통3종 스텐프라이팬 ";
		/**
		 * 방송중 상품 여부
		 */
		boolean isOnAir = false;
		/**
		 * 방송시간 텍스트 폭
		 */
		int timeW = 0;

		public TimeLineRenderer(int baseX, int baseY, boolean isPreSchedul) {
			this.baseX = baseX;
			this.baseY = baseY;
			this.isPreSchedul = isPreSchedul;
			setVisible(false);
		}

		public void setVisible(boolean isVisible) {
			this.isVisible = isVisible;
		}

		public void update(String timeStr, String prdNm, boolean isOnAir) {
			this.timeStr = timeStr;
			this.prdNm = prdNm;
			this.isOnAir = isOnAir;

			this.timeW = Util.getStringWidth(timeStr, getFontMetrics(Util.F18)) + 5;

			this.prdNm = Util.shorten(this.prdNm, getFontMetrics(Util.F14), maxW);

			this.isVisible = true;
		}

		public boolean isVisible() {
			return isVisible;
		}

		public void render(Graphics g) {
			// public void paint(Graphics g) {
			g.setColor(Util.c_838383);
			// g.setColor(new Color(255,0,0));
			// drawStringLeft(g, "우리나라", 100,200);
			// System.out.println("render.. is Paint ok~~~~~~~~~~~~~~~~~~~");
			if (isVisible) {
				g.setFont(Util.F18);
				// 방송시간
				Util.drawStringLeft(g, timeStr, baseX + paddingLeft, baseY + paddingTop + timeH);
				// onAir Icon
				if (isOnAir) {
					 getBackgroundImage("main_icon_onair_dim").setBounds(baseX + paddingLeft + timeW, baseY + paddingTop, 45, 22);
					 getBackgroundImage("main_icon_onair_dim").setVisible(true);
//					g.drawImage(main_icon_onair_dim, baseX + paddingLeft + timeW, baseY + paddingTop, 45, 22, this);
				}

				// 상품명
				g.setFont(Util.F14);
				Util.drawStringLeft(g, prdNm, baseX + paddingLeft, baseY + paddingTop + timeH + lineGap + prdNmH);
			} else {
				g.setFont(Util.F14);
				Util.drawStringLeft(g, (this.isPreSchedul ? "이전" : "이후") + " 편성표가 없습니다.", baseX + paddingLeft,
						baseY + paddingTop + timeH + lineGap + prdNmH - 12);
			}
		}

	}
	
	private void setTimeLineInitData() {
		this.priceFW = Util.getStringWidth(slPrice, getFontMetrics(Util.F15));
		this.timeStrFW = Util.getStringWidth(timeStrF, getFontMetrics(Util.F18));
		prdNmF[0] = "디키즈 Dickies 패딩 점퍼";
		prdNmF[1] = "남녀공용 성인 2종세트";
		
		productImage =ImageUtil.createImage(HttpConnect.ImgRequest("http://210.122.102.171:80/goods_images/47/667/47142667OPJ.jpg"));
		
		focus01 = loadImage("common_popup_foc_02.png", true);
		focus02 = loadImage("common_popup_foc_01.png", true);
		focus03 = loadImage("common_popup_foc_03.png", true);
		focus04 = loadImage("common_popup_foc_02.png", true);
		
		main_bg_timetable_dim= loadImage("main_bg_timetable_dim.png",true);
		main_bg_timetable_list2 =loadImage("main_bg_timetable_list2.png", true);
		main_bg_timetable_list_onair=loadImage("main_bg_timetable_list_onair.png", true);
		
//		this.sampleData[][] ={ {"1","2","3","4","5","6","7","8"},
//                {"1","2","3","4","5","6","7","8"} };


	}
	
	/**
	 * 편성표 데이타 설정
	 */
	private void setTimeLineData() {
		this.timeLineRenderer1.update(this.sampleData[this.focus-2][2], this.sampleData[this.focus-2][4], true);
		this.timeLineRenderer2.update(this.sampleData[this.focus-1][2], this.sampleData[this.focus-1][4], true);
		
		this.timeLineRenderer3.update(this.sampleData[this.focus+1][2], this.sampleData[this.focus+1][4], true);
		this.timeLineRenderer4.update(this.sampleData[this.focus+2][2], this.sampleData[this.focus+2][4], true);
	}
	
	private void setCurrentTimeLineData(int index) {
		if(productImage != null) {
			productImage.flush();
		}
		productImage = ImageUtil.createImage(HttpConnect.ImgRequest( this.sampleData[index][0]));
		
		timeStrF = this.sampleData[index][2];
		
		prdNmF[0] = this.sampleData[index][4];
		prdNmF[1] = this.sampleData[index][5];
	
		slPrice =  this.sampleData[index][6];
		lastprice =  this.sampleData[index][7];
		
	}
	
	
	
	public  void drawFocus(Graphics g, int x, int y, int w, int h, ImageObserver observer) {
		g.drawImage(focus01, x, y, w, 4, observer);	// top
		g.drawImage(focus02, x, y + 4, 4, h - 4 - 4, observer);	// left
		g.drawImage(focus03, x + w - 4, y + 4, 4, h - 4 - 4, observer);	// right
		g.drawImage(focus04, x, y + h - 4, w, 4, observer);	// bottom
	}

}
