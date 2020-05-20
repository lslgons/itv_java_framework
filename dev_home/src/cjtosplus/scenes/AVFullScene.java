package cjtosplus.scenes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import com.cj.tvui.Keys;
import com.cj.tvui.component.ImagePanel;
import com.cj.tvui.component.ItemCompEvent;
import com.cj.tvui.controller.MediaController;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.ui.Widget;
import com.cj.tvui.util.LOG;

import cjtosplus.comp.LogoAnimate;
import cjtosplus.comp.main.Button;

public class AVFullScene extends Scene {
	
private LogoAnimate logoAnimate;

/**
 * 버튼 노말 이미지
 */
private Image main_big_btn_nor;
/**
 * 버튼 포커스 이미지
 */
private Image main_big_btn_foc;
/**
 * 상품상세 아이콘
 */
private Image main_icon_01_dark;
/**
 * 바로주문 아이콘
 */
private Image main_icon_02_dark;
/**
 * 리모컨주문 아이콘
 */
private Image main_icon_03_dark;
/**
 * 모바일주문 아이콘
 */
private Image main_icon_04_dark;


private List compList; // 메인 메뉴
private List subCompList;  // 바로주문 하위 메뉴.
protected boolean subMenuVisible = false;

private Scene staticComScene;


    /* Buttons */
    class Button extends Widget {
    	private boolean  isActivated = false; // Nor , Focus 여부
    	private ItemCompEvent evt = null; // ok Event 처리.
    	
    	private String text = null;
    	private Color color = null;
    	
    	private Image iconImg = null;  // 사용할 아이콘 이미지.
    	private Image norImg = null; // 노멀 이미지.
    	private Image focImg = null; // 포커스 이미지.
    	
    	private int btnType = 0; // 0: default;

        public Button(int width, int height, String text, Image icon) {
        	setBounds(0, 0, width, height);
        	this.text = text;
        	this.iconImg = icon;
        }
        
        public Button(int width, int height, String text, Image icon, Image nor, Image foc,  int btnType, ItemCompEvent e) {
        	setBounds(0, 0, width, height);
        	this.text = text;
			this.iconImg = icon;
			this.norImg = nor;
			this.focImg = foc;
			this.btnType =btnType;
        	this.evt = e;
        }

        public void onActivated() {
        	this.isActivated = true;
        }

        public void onDeactivated() {
        	this.isActivated = false;
        }

        public void okKeyPressed() {
        	evt.doAction();
        }
        
        public void paint(Graphics g) {
        	g.setFont(new Font("Korean iTV SanSerifD", Font.PLAIN, 15));
        	
			if (this.btnType == 0) {
				if (this.isActivated) { // 포커스
					g.drawImage(this.getFocImg(), 0, 0, getWidth(), getHeight(), this);
					g.drawImage(this.getIconImg(), 18, 12, 32, 32, this);
					g.drawString(text, 51, 33);

				} else {// 노멀
					g.drawImage(this.getNorImg(), 0, 0, getWidth(), getHeight(), this);
					g.drawImage(this.getIconImg(), 18, 12, 32, 32, this);
					g.drawString(text, 51, 33);

				}
			} else if (this.btnType == 1) {
				if (this.isActivated) { // 포커스
					g.drawImage(this.getFocImg(), 0, 0, getWidth(), getHeight(), this);
					g.drawImage(this.getIconImg(), 18-6, 12, 32, 32, this);
					g.drawString(text, 51-6, 33);

				} else {// 노멀
					g.drawImage(this.getNorImg(), 0, 0, getWidth(), getHeight(), this);
					g.drawImage(this.getIconImg(), 18-6, 12, 32, 32, this);
					g.drawString(text, 51-6, 33);

				}
			}
        	
        }
        
        public Image getIconImg() {
    		return iconImg;
    	}

		public Image getNorImg() {
			return norImg;
		}

		public Image getFocImg() {
			return focImg;
		}
		
		public void destroy() {
			if (getIconImg() != null) {
				this.iconImg.flush();
				iconImg = null;
			}

			if (getNorImg() != null) {
				this.norImg.flush();
				norImg = null;
			}

			if (getFocImg() != null) {
				this.focImg.flush();
				focImg = null;
			}

			if (this.color != null) {
				this.color = null;
			}

		}

		public void setBtnType(int btnType) {
			this.btnType = btnType;
		}

    }
    /* Banner */
    abstract class Banner extends Widget {

        public Banner(int type) {

        }

        abstract public Image onBannerChanged();

        public void onActivated() {

        }


        public void onDeactivated() {

        }

        public void okKeyPressed() {

        }
    }

    /* Flow text */
    class FlowtextBar extends Component {

    }


    public void onInit() {
    	System.out.println("AVFullScene is onInit here........");
    	
    	logoAnimate =new LogoAnimate(50, 25, 170, 60);
		this.add(logoAnimate);
		logoAnimate.setLogoImg();
		logoAnimate.setVisible(true);
		logoAnimate.startLogo();
		
		compList = new LinkedList();
		
		compList.add(new Button(129, 54, "상품상세", loadImage("main_icon_01_dark.png", true),
				loadImage("main_big_btn_nor.png", true),
				loadImage("main_big_btn_foc.png", true),
				0, 
				new com.cj.tvui.component.ItemCompEvent() {
					public void doAction() {
						System.out.println("===========20min=AVFullScene=01====is Button Action Now...");
						
					}
				}));
		
		compList.add(new Button(129, 54, "바로주문", loadImage("main_icon_02_dark.png", true),
				loadImage("main_big_btn_nor.png", true),
				loadImage("main_big_btn_foc.png", true),
				0,
				new com.cj.tvui.component.ItemCompEvent() {
					public void doAction() {
						System.out.println("===========20min=AVFullScene=02====is Button Action Now...");
						for (int i = 0; i < subCompList.size(); i++) { 
							Button btn = (Button) subCompList.get(i);
							btn.setVisible(true);
							btn = null;
						}
						subMenuVisible = true;
						
						setWidgetActivate((Widget) subCompList.get(0)); // focus.. 준다.
					}
				}));
		
		for (int i = 0; i < compList.size(); i++) {
			if (i == 0) {
				((Component) compList.get(i)).setSize(129, 54);
				((Component) compList.get(i)).setLocation(45,  404);
				this.add((Component) compList.get(i));
			} else if (i == 1) {
				((Component) compList.get(i)).setSize(129, 54);
				((Component) compList.get(i)).setLocation(45+129, 404);
				this.add((Component) compList.get(i));
			}
		}
		setWidgetActivate((Widget) compList.get(0));  // focus.. 준다.
		
		//////////////////////////////// focus//////// 설정./////////////////////////////////////////////////////
		Button comp = (Button) compList.get(0);
		comp.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget) compList.get(1));
		comp.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget) compList.get(1));
		comp = null;

		Button comp2 = (Button) compList.get(1);
		comp2.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget) compList.get(0));
		comp2.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget) compList.get(0));
		comp2 = null;
		
		
		/////////////sub menu 시작////////////////////////////////
		subCompList = new LinkedList();
		
		subCompList.add(new Button(129, 54, "리모컨주문", loadImage("main_icon_03_dark.png", true),
				loadImage("main_big_btn_nor.png", true), 
				loadImage("main_big_btn_foc.png", true),
				1,
				new com.cj.tvui.component.ItemCompEvent() {
					public void doAction() {
						System.out.println("===========20min==sub 01====is Button Action Now...");
					}
				}));
		
		subCompList.add(new Button(129, 54, "모바일주문", loadImage("main_icon_04_dark.png", true),
				loadImage("main_big_btn_nor.png", true), 
				loadImage("main_big_btn_foc.png", true),
				1,
				new com.cj.tvui.component.ItemCompEvent() {
					public void doAction() {
						System.out.println("===========20min==sub 02====is Button Action Now...");
					}
				}));
		
		for (int i = 0; i < subCompList.size(); i++) {
			if (i == 0) {
				((Component) subCompList.get(i)).setSize(129, 54);
				((Component) subCompList.get(i)).setLocation( 175,  315);
				this.add((Component) subCompList.get(i));
			} else if (i == 1) {
				((Component) subCompList.get(i)).setSize(129, 54);
				((Component) subCompList.get(i)).setLocation(175,  357+3);
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
		subComp01.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget) compList.get(0));
		subComp01 = null;
		
		Button subComp02 = (Button) subCompList.get(1);
		subComp02.setFocusTraversal(Widget.AT_WIDGET_TOP, (Widget) subCompList.get(0));
		subComp02.setFocusTraversal(Widget.AT_WIDGET_BOTTOM, (Widget) subCompList.get(0));
		subComp02.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget) compList.get(0));
		subComp02.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget) compList.get(0));
		subComp02 = null;
		
		staticComScene = SceneController.getInstance().getStaticScene();
    	staticComScene.sendData(new String[] {"AVFullSceneMode","P_TYPE_RENTAL"});
    }


    public void onDataReceived(Object[] objects) {

    }


    public void onShow() {
    	if(logoAnimate != null) {
    		logoAnimate.startLogo();
    	}

    }


    public void onHide() {
    	if(logoAnimate != null) {
    		logoAnimate.stopLogo();
    	}
    }


    public void onDestroy() {
    	logoAnimate.stopLogo();
		logoAnimate.destory();
		if(logoAnimate != null) {
			logoAnimate = null;
		}
		
		for (int i = 0; i < compList.size(); i++) { // 자원 resource 제거.
			Button btn01 = (Button) compList.get(i);
			btn01.destroy();
		}
		
		for (int i = 0; i < subCompList.size(); i++) { // 자원 resource 제거.
			Button btn02 = (Button) subCompList.get(i);
			btn02.destroy();
		}
    }


    public void onKeyDown(int keyCode) {
    	System.out.println("i value is....["+keyCode+"]------------------");
    	switch(keyCode){
		case Keys.VK_BACK:
		case 8: // backSpace key For emul...
			int x=61;
			int y=36;
			int w=625;
			int h=360;
			LOG.print(this, "Resize AV to "+x+", "+y+", "+w+", "+h);
			Rectangle avSize = new Rectangle(x, y, w, h);
			MediaController.getInstance().changeVideoSize(avSize);
			
			popScene(); // 이전으로 처리. :맨위에 있는 scene을 stack에서 한개 빼고 나오게 한다.
			break;
		case Keys.VK_RIGHT:
			if(this.subMenuVisible==true) {
				for (int i = 0; i < subCompList.size(); i++) { 
					Button btn = (Button) subCompList.get(i);
					btn.setVisible(false);
					btn= null;
				}
    			this.subMenuVisible = false;
			}
			break;
		case Keys.VK_LEFT:
			if(this.subMenuVisible==true) {
				for (int i = 0; i < subCompList.size(); i++) { 
					Button btn = (Button) subCompList.get(i);
					btn.setVisible(false);
					btn= null;
				}
    			this.subMenuVisible = false;
			}
			break;
		}

    }


    public void onPaint(Graphics graphics) {

    }


    public void timerWentOff() {
        LOG.print("timerWentOff");
    }
}
