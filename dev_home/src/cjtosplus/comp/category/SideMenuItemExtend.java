package cjtosplus.comp.category;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import com.cj.tvui.ui.Widget;
import com.cj.tvui.util.LOG;

import cjtosplus.comp.Util;

public class SideMenuItemExtend extends Widget {
    private String title;
    private Image iconNormal;
    private Image iconSelected;
    
    private Image focus01;
	private Image focus02;
	private Image focus03;
	private Image focus04;
    
    private boolean isSelected = false;
    
    private int xp;
    private int yp;
    private int width;
    private int height;
    
    public SideMenuItemExtend(String title) {
        this.title = title;
        this.setSize(300, 100);
        this.setLocation(0,0);
    }
    
    public SideMenuItemExtend(String title, Image iconNormal, Image iconSelected,int xp, int yp, int width, int height, Image focTop, Image focLeft, Image focRight, Image focBottom) {
		this.title = title;
		this.iconNormal = iconNormal;
		this.iconSelected = iconSelected;
		this.xp = xp;
		this.yp = yp;
		this.width = width;
		this.height = height;
		this.focus01 = focTop;
		this.focus02 = focLeft;
		this.focus03 = focRight;
		this.focus04 = focBottom;
		this.setBounds(xp, yp, width, height);
     
    }
  

    public String getTitle() {
        return this.title;
    }

    protected void onActivated() {

    }

    protected void onDeactivated() {

    }

    public void okKeyPressed() {
        LOG.print("SideMenuItem  OK Key");

    }
    
    public void onDestroy() {
    	if(iconNormal != null) {
    		iconNormal.flush();
    		iconNormal=null;
    	}
    	
    	if(iconSelected != null) {
    		iconSelected.flush();
    		iconSelected = null;
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
    }
    private void initImage() {
//    	focus01 = loadImage("common_popup_foc_02.png", true);
//		focus02 = loadImage("common_popup_foc_01.png", true);
//		focus03 = loadImage("common_popup_foc_03.png", true);
//		focus04 = loadImage("common_popup_foc_02.png", true);
    }
    
    public void paint(Graphics g) {
//        g.setColor(Color.cyan);
//        g.fillRect(0,0,300,100);
//        if(isActivated()) {
//            g.setColor(Color.red);
//            g.drawRect(0,0,300,100);
//        }
//
//
//        g.setFont(new Font("Korean iTV SanSerifD", Font.PLAIN, 15));
//        g.setColor(Color.black);
//        g.drawString(this.title, 10,10);
    	
    	 if(isSelected()) { // 선택
    		
			g.drawImage(iconSelected, 24, 8, 26, 26, this);
    		 
			g.setFont(new Font("Korean iTV SanSerifD", Font.PLAIN, 15));
			g.setColor(Util.c_EC447A);
//			g.drawString(this.title, 10 + 4 + 4 + 40, 10 + 4 + 10 + 4);
			Util.drawStringRight(g, this.title, 10 + 4 + 4 + 40 + 50 + 4, 10 + 4 + 10 + 4 + 4);
			return;
    	 }
    	
    	  if(isActivated()) { // 포커스
//            g.setColor(Color.red);
//            g.drawRect(0,0,width-1,height-1);
			g.drawImage(iconNormal, 24, 8, 26, 26, this);
            drawFocus(g, 0 , 0, width, height, this);
    	  }else { // 노멀
			g.drawImage(iconNormal, 24, 8, 26, 26, this);
    	  }
    	  
        g.setFont(new Font("Korean iTV SanSerifD", Font.PLAIN, 15));
        g.setColor(Color.white);
//        g.drawString(this.title, 10+4+4+40,10+4+10+4);
		Util.drawStringRight(g, this.title, 10 + 4 + 4 + 40 + 50 + 4, 10 + 4 + 10 + 4 + 4);
    	
        
//        drawFocus(g, 0 - 24, 0 - 9 + 0, 145, 42, this);
        super.paint(g);
    }
    
	public  void drawFocus(Graphics g, int x, int y, int w, int h, ImageObserver observer) {
		g.drawImage(focus01, x, y, w, 4, observer);	// top
		g.drawImage(focus02, x, y + 4, 4, h - 4 - 4, observer);	// left
		g.drawImage(focus03, x + w - 4, y + 4, 4, h - 4 - 4, observer);	// right
		g.drawImage(focus04, x, y + h - 4, w, 4, observer);	// bottom
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
}