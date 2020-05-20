package cjtosplus.scenes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import cjtosplus.comp.main.*;
import com.cj.tvui.component.ImagePanel;
import com.cj.tvui.controller.MediaController;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.ui.Widget;
import com.cj.tvui.util.LOG;

import cjtosplus.comp.LogoAnimate;

public class MainScene extends Scene {
	private LogoAnimate logoAnimate;
	private FlowTextBar flowtextBar;
	
	private List compList; // 메인 메뉴
	private List subCompList;  // 바로주문 하위 메뉴.
	protected boolean subMenuVisible = false;
	
	private Scene staticComScene;

    public void onInit() {
    	System.out.println("It's MainScene onInit here...20min");
    
		
    	int x = 61;
		int y = 36;
		int w = 625;
		int h = 360;
		LOG.print(this, "Resize AV to " + x + ", " + y + ", " + w + ", " + h);
		Rectangle avSize = new Rectangle(x, y, w, h);
		MediaController.getInstance().changeVideoSize(avSize);
		
    	
    	logoAnimate =new LogoAnimate(61, 36, 170, 60);
		this.add(logoAnimate);
		logoAnimate.setLogoImg();
		logoAnimate.setVisible(true);
		logoAnimate.startLogo();
    	
		// 티커 출력
    	flowtextBar = new FlowTextBar(90, 437, 585,20,
    	"CJ오쇼핑 전매체 (CJmall, 방송, 카탈로그) 이용 시 The CJ 카드로 결제하면 5% 청구할인됩니다.\n자세한 사항은 상담원에게 문의하시기 바랍니다.",
    	new Font("Korean iTV SanSerifD", Font.PLAIN, 15), new Color(255,255,255));
		add(flowtextBar);
		flowtextBar.setVisible(true);
		flowtextBar.startTicker();
		flowtextBar.setTextGap(50);
		
		
		compList = new LinkedList();

		compList.add(new Button(152, 42, "크게보기", loadImage("main_icon_big.png", true),
				loadImage("main_btn_nor.png", true),
				loadImage("common_popup_foc_02.png", true),
				loadImage("common_popup_foc_01.png", true),
				loadImage("common_popup_foc_03.png", true),
				loadImage("common_popup_foc_02.png", true),
				loadImage("main_btn_sel.png", true),
				0,
				new com.cj.tvui.component.ItemCompEvent() {
					public void doAction() {
						System.out.println("===========20min==01====is Button Action Now...");
						
						Rectangle avSize = new Rectangle(0, 0, 960, 540);
						MediaController.getInstance().changeVideoSize(avSize);
						// 크게보기 화면으로 화면 이동..
						pushScene("cjtosplus.scenes.AVFullScene", null);
					}
				}));
		
		compList.add(new Button(152, 42, "바로주문", loadImage("main_icon_order.png", true),
				loadImage("main_btn_nor.png", true),
//				loadImage("main_big_btn_foc.png", true),
				loadImage("common_popup_foc_02.png", true),
				loadImage("common_popup_foc_01.png", true),
				loadImage("common_popup_foc_03.png", true),
				loadImage("common_popup_foc_02.png", true),
				loadImage("main_btn_sel.png", true),
				0,
				new com.cj.tvui.component.ItemCompEvent() {
					public void doAction() {
						System.out.println("===========20min==02====is Button Action Now...");
						
						for (int i = 0; i < subCompList.size(); i++) { 
							Button btn = (Button) subCompList.get(i);
							btn.setVisible(true);
							btn = null;
						}
						subMenuVisible = true;
						ImagePanel main_btn_expand = getBackgroundImage("main_btn_expand");
						main_btn_expand.setVisible(true);
						
						Button btn02 = (Button)compList.get(1);
						btn02.setSelected(true);
						btn02 = null;
						
						setWidgetActivate((Widget) subCompList.get(0)); // focus.. 준다.
					}
				}));
		
		compList.add(new Button(152, 42, "다른상품보기", loadImage("main_icon_list.png", true),
				loadImage("main_btn_nor.png", true),
//				loadImage("main_big_btn_foc.png", true),
				loadImage("common_popup_foc_02.png", true),
				loadImage("common_popup_foc_01.png", true),
				loadImage("common_popup_foc_03.png", true),
				loadImage("common_popup_foc_02.png", true),
				loadImage("main_btn_sel.png", true),
				1,
				new com.cj.tvui.component.ItemCompEvent() {
					public void doAction() {
						System.out.println("===========20min==03====is Button Action Now...");
//						pushScene("cjtosplus.scenes.CategoryScene", null);
						pushScene("cjtosplus.scenes.CategoryNewScene", null);
					}
				}));

		
		compList.add(new Button(152, 42, "편성표", new Color(0xec447a),
				loadImage("main_icon_timetable_sel.png", true),
				loadImage("main_btn_nor.png", true),
//				loadImage("main_big_btn_foc.png", true),
				loadImage("common_popup_foc_02.png", true),
				loadImage("common_popup_foc_01.png", true),
				loadImage("common_popup_foc_03.png", true),
				loadImage("common_popup_foc_02.png", true),
				loadImage("main_btn_sel.png", true),
				2,
				new com.cj.tvui.component.ItemCompEvent() {
					public void doAction() {
						System.out.println("===========20min==04=schedule===is Button Action Now..schedule.");
						openPopup("cjtosplus.popup.SchedulePopup", null);
					}
				}));
		
		compList.add(new ExtendableBanner(0, // 커지는 전체 배너.
				loadImage("common_popup_foc_02.png", true),
				loadImage("common_popup_foc_01.png", true),
				loadImage("common_popup_foc_03.png", true),
				loadImage("common_popup_foc_02.png", true),
				new com.cj.tvui.component.ItemCompEvent() {
					public void doAction() {
						System.out.println("===========20min==banner 03 (Full)====is Button Action Now...");
					}
				}));
		
		compList.add(new BigBanner(0, // 아래쪽 배너.
				loadImage("common_popup_foc_02.png", true),
				loadImage("common_popup_foc_01.png", true),
				loadImage("common_popup_foc_03.png", true),
				loadImage("common_popup_foc_02.png", true),
				new com.cj.tvui.component.ItemCompEvent() {
					public void doAction() {
						System.out.println("===========20min==banner 01 (bottom)====is Button Action Now...");
					}
				}));
		
		compList.add(new SmallBanner(0, // 위쪽 배너.
				loadImage("common_popup_foc_02.png", true),
				loadImage("common_popup_foc_01.png", true),
				loadImage("common_popup_foc_03.png", true),
				loadImage("common_popup_foc_02.png", true),
				new com.cj.tvui.component.ItemCompEvent() {
					public void doAction() {
						System.out.println("===========20min==banner 02 (top)====is Button Action Now...");
						setWidgetActivate((Widget) compList.get(6));
					}
				}));
		
		

		for (int i = 0; i < compList.size(); i++) {
			if (i == 0) {
				((Component) compList.get(i)).setSize(152, 42);
				((Component) compList.get(i)).setLocation(61, 382);
				this.add((Component) compList.get(i));
			} else if (i == 1) {
				((Component) compList.get(i)).setSize(152, 42);
				((Component) compList.get(i)).setLocation(61+152+5, 382);
				this.add((Component) compList.get(i));
			}else if (i == 2) {
				((Component) compList.get(i)).setSize(152, 42);
				((Component) compList.get(i)).setLocation(61+152+152+10, 382);
				this.add((Component) compList.get(i));
			}else if (i == 3) {
				((Component) compList.get(i)).setSize(152, 42);
				((Component) compList.get(i)).setLocation(61+152+152+152+15, 382);
				this.add((Component) compList.get(i));
			}else if (i == 4) { // 배너 확대
				((Component) compList.get(i)).setSize(200, 423); // 배너 확대
				((Component) compList.get(i)).setLocation(698, 36);// 배너 확대
				this.add((Component) compList.get(i));
			}else if (i == 5) { // 아래쪽 배너.
				((Component) compList.get(i)).setSize(200, 276);  // 아래 배너
				((Component) compList.get(i)).setLocation(698, 183);  // 아래 배너
				this.add((Component) compList.get(i));
			}else if (i == 6) { // 위쪽 배너..
				((Component) compList.get(i)).setSize(200, 132);  // 위쪽 배너.
				((Component) compList.get(i)).setLocation(698, 36);  // 위쪽 배너.
				this.add((Component) compList.get(i));
			}
				 
		}
		setWidgetActivate((Widget) compList.get(0));  // focus.. 준다.
				
		//////////////////////////////// focus//////// 설정./////////////////////////////////////////////////////
		Button comp = (Button) compList.get(0);
		comp.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget) compList.get(5));
		comp.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget) compList.get(1));
		comp = null;

		Button comp2 = (Button) compList.get(1);
		comp2.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget) compList.get(0));
		comp2.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget) compList.get(2));
		comp2 = null;
		
		Button comp3 = (Button) compList.get(2);
		comp3.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget) compList.get(1));
		comp3.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget) compList.get(3));
		comp3 = null;
		
		Button comp4 = (Button) compList.get(3);
		comp4.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget) compList.get(2));
		comp4.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget) compList.get(5));
		comp4 = null;
		
		
		Banner comp7 = (Banner) compList.get(4);
		comp7.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget) compList.get(3));
		comp7.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget) compList.get(0));
		comp7 = null;
		
		BigBanner comp5 = (BigBanner) compList.get(5);
		comp5.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget) compList.get(3));
		comp5.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget) compList.get(0));
		comp5.setFocusTraversal(Widget.AT_WIDGET_TOP, (Widget) compList.get(6));
		comp5.setBannerImg();
		comp5.startBanner();
		comp5 = null;
		////////////////////////////////////////////////////////////////////////////////////////////////
//		bannerComp01 compTemp01 = (bannerComp01) compList.get(4);
//		compTemp01.setBannerImg();
//		compTemp01.startBanner();
		///////////////////////////////////////////////////////////////////////////////////////////////
		
		SmallBanner comp6 = (SmallBanner) compList.get(6);
		comp6.setFocusTraversal(Widget.AT_WIDGET_BOTTOM, (Widget) compList.get(5));
		comp6.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget) compList.get(3));
		comp6.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget) compList.get(0));
		comp6.setBannerImg();
		comp6.startBanner();
		comp6 = null;
		
		
		subCompList = new LinkedList();
		
		subCompList.add(new Button(152, 42, "리모컨주문", loadImage("main_icon_remocon.png", true),
//				loadImage("main_btn_nor.png", true),
				loadImage(" .png", true), // 노멀이미지 없음 대신 배경 lable로 처리
//				loadImage("main_big_btn_foc.png", true),
				loadImage("common_popup_foc_02.png", true),
				loadImage("common_popup_foc_01.png", true),
				loadImage("common_popup_foc_03.png", true),
				loadImage("common_popup_foc_02.png", true),
				loadImage(" .png", true), // 선택이미지 없음 대신 배경 lable로  처리
				1,
				new com.cj.tvui.component.ItemCompEvent() {
					public void doAction() {
						System.out.println("===========20min==sub 01====is Button Action Now...");
					}
				}));
		
		subCompList.add(new Button(152, 42, "모바일주문", loadImage("main_icon_mobile.png", true),
//				loadImage("main_btn_nor.png", true),
				loadImage(" .png", true), // 노멀이미지 없음 대신 배경 lable로 처리
//				loadImage("main_big_btn_foc.png", true),
				loadImage("common_popup_foc_02.png", true),
				loadImage("common_popup_foc_01.png", true),
				loadImage("common_popup_foc_03.png", true),
				loadImage("common_popup_foc_02.png", true),
				loadImage(" .png", true),  // 선택이미지 없음 대신 배경 lable로 처리
				1,
				new com.cj.tvui.component.ItemCompEvent() {
					public void doAction() {
						System.out.println("===========20min==sub 02====is Button Action Now...");
					}
				}));
		
		for (int i = 0; i < subCompList.size(); i++) {
			if (i == 0) {
				((Component) subCompList.get(i)).setSize(152, 42);
				((Component) subCompList.get(i)).setLocation( 219 - 5+5, 289);
				this.add((Component) subCompList.get(i));
			} else if (i == 1) {
				((Component) subCompList.get(i)).setSize(152, 42);
				((Component) subCompList.get(i)).setLocation(219 - 5+5, 289+42);
				this.add((Component) subCompList.get(i));
			}
				 
		}
		
		for (int i = 0; i < subCompList.size(); i++) { 
			Button btn = (Button) subCompList.get(i);
			btn.setVisible(false);
		}
		
		Button subComp01 = (Button) subCompList.get(0);
		subComp01.setFocusTraversal(Widget.AT_WIDGET_TOP, (Widget) subCompList.get(1));
		subComp01.setFocusTraversal(Widget.AT_WIDGET_BOTTOM, (Widget) subCompList.get(1));
		subComp01.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget) compList.get(0));
		subComp01.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget) compList.get(2));
		subComp01 = null;
		
		Button subComp02 = (Button) subCompList.get(1);
		subComp02.setFocusTraversal(Widget.AT_WIDGET_TOP, (Widget) subCompList.get(0));
		subComp02.setFocusTraversal(Widget.AT_WIDGET_BOTTOM, (Widget) subCompList.get(0));
		subComp02.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget) compList.get(0));
		subComp02.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget) compList.get(2));
		subComp02 = null;
	
    	
		//공지사항//
        addBackgroundImage("main_bg_noti_left", loadImage("main_bg_noti_left.png", true), 61,435,24,25);
        addBackgroundImage("main_bg_noti_center", loadImage("main_bg_noti_center.png", true), 86,435,575,25);
        addBackgroundImage("main_bg_noti_right", loadImage("main_bg_noti_right.png", true), 661,435,25,25);
        addBackgroundImage("main_icon_noti", loadImage("main_icon_noti.png", true), 63,435,25,25);
    	/////////////
    	
    	//서브메뉴의 배경
        addBackgroundImage("main_btn_expand", loadImage("main_btn_expand.png", true), 219-5,289,165-2,93);
        getBackgroundImage("main_btn_expand").setVisible(false);
    	//좌측에서 2/3지점까지 bg
        addBackgroundImage("main_bg", loadImage("main_bg.png", true), 0,0,61,540);

        addBackgroundImage("main_bg_vod_up", loadImage("main_bg_vod_up.png", true), 61,0,625,36);

        addBackgroundImage("main_bg_vod_down", loadImage("main_bg_vod_down.png", true), 61,378-22,625,162+22);

    	//우측bg
        addBackgroundImage("main_bg_right", loadImage("main_bg.png", true), 686,0,274,540);

    	//상단
        addBackgroundImage("main_vod_frame_black_line", loadImage("main_vod_frame_black_line.png", true), 63,435,25,25);

    	//좌단
        addBackgroundImage("main_vod_frame_black_line_left", loadImage("main_vod_frame_black_line.png", true), 61,36+3,3,320-6);

    	//우단
        addBackgroundImage("main_vod_frame_black_line_right", loadImage("main_vod_frame_black_line.png", true), 61 + 625 - 3,36+3,3,320-6);


    	//하단
        addBackgroundImage("main_vod_frame_black_line_bottom", loadImage("main_vod_frame_black_line.png", true), 61,36+320-3,625,3);

        addBackgroundImage("main_vod_frame_shadow_01", loadImage("main_vod_frame_shadow_01.png", true), 61,356,25,22);

        addBackgroundImage("main_vod_frame_shadow_02", loadImage("main_vod_frame_shadow_02.png", true), 86,356,575,22);

        addBackgroundImage("main_vod_frame_shadow_03", loadImage("main_vod_frame_shadow_03.png", true), 661,356,25,22);
        
        setStaticScene("cjtosplus.scenes.StaticComScene", null);
        
    	staticComScene = SceneController.getInstance().getStaticScene();
    	staticComScene.sendData(new String[] {"MainSceneMode","P_TYPE_NORMAL"});

    }


    public void onDataReceived(Object[] objects) {

    }


    public void onShow() {
    	staticComScene.sendData(new String[] {"NormalMode","P_TYPE_NORMAL"});

    }


    public void onHide() {

    }


    public void onDestroy() {

//		for (int i = 0; i < compList.size(); i++) { // 자원 resource제거.
//			Button btn = (Button) compList.get(i);
//			btn.destroy();
//		}
		for (int i = 0; i < compList.size(); i++) { // 자원 resource 제거.
			if(i<4) {
				Button btn = (Button) compList.get(i);
				btn.destroy();
			}else if(i==4) {// 큰배너
				
			}else if(i==5) { // 아래배너
				BigBanner compTemp01 = (BigBanner) compList.get(5);
				compTemp01.stopBanner();
				compTemp01.destroy();
				compTemp01 = null;
			}else if(i==6) {  // 윗배너
				SmallBanner compTemp02 = (SmallBanner) compList.get(6);
				compTemp02.stopBanner();
				compTemp02.destroy();
				compTemp02 = null;
				
			}
			
		}
		
		for (int i = 0; i < subCompList.size(); i++) { // 자원 resource 제거.
			Button btn = (Button) subCompList.get(i);
			btn.destroy();
		}
		
		flowtextBar.stopTicker();
		flowtextBar.destroy();
		if(flowtextBar != null) {
			flowtextBar = null;
		}
		
		logoAnimate.stopLogo();
		logoAnimate.destory();
		if(logoAnimate != null) {
			logoAnimate = null;
		}
		
    }


    public void onKeyDown(int keyCode) {
    	System.out.println("onKeyDown is Here........["+"]=======");
    	
    	if(keyCode == KeyEvent.VK_RIGHT) {
    		System.out.println("VK_RIGHT is OK....");
    		
    		if(this.subMenuVisible==true) {
    			for (int i = 0; i < subCompList.size(); i++) { 
					Button btn = (Button) subCompList.get(i);
					btn.setVisible(false);
					btn= null;
				}
    			this.subMenuVisible = false;
    			ImagePanel main_btn_expand = getBackgroundImage("main_btn_expand");
    			main_btn_expand.setVisible(false);
    			
    			Button btn02 = (Button)compList.get(1);
				btn02.setSelected(false);
				btn02 = null;
    		}
    		
    	}else if(keyCode == KeyEvent.VK_LEFT) {
    		System.out.println("VK_LEFT is OK....");
    		if(this.subMenuVisible==true) {
    			for (int i = 0; i < subCompList.size(); i++) { 
					Button btn = (Button) subCompList.get(i);
					btn.setVisible(false);
					btn= null;
				}
    			this.subMenuVisible = false;
                ImagePanel main_btn_expand = getBackgroundImage("main_btn_expand");
    			main_btn_expand.setVisible(false);
    			
    			Button btn02 = (Button)compList.get(1);
				btn02.setSelected(false);
				btn02 = null;
    		}
    	}
    }


    public void onPaint(Graphics graphics) {
//    	graphics.setColor(new Color(0,0,0));
//    	graphics.fillRect(0, 0, 960,540);

    }


    public void timerWentOff() {

    }
    
//	public static void drawFocus(Graphics g, int x, int y, int w, int h, ImageObserver observer) {
//		drawImage(g, loadImage("images/common_popup_foc_02.png"), x, y, w, 4, observer);	// top
//		drawImage(g, Rs.IMG_POOL.getImage(_common_popup_focus_l), x, y + 4, 4, h - 4 - 4, observer);	// left
//		drawImage(g, Rs.IMG_POOL.getImage(_common_popup_focus_r), x + w - 4, y + 4, 4, h - 4 - 4, observer);	// right
//		drawImage(g, Rs.IMG_POOL.getImage(_common_popup_focus_c), x, y + h - 4, w, 4, observer);	// bottom
//	}
}