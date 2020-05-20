package cjtosplus.comp.category;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import com.cj.tvui.ui.Widget;
import com.cj.tvui.util.LOG;

public class GridItemExtend extends Widget {
    private String title;
    private Image goodImage;
    
    private Image focus01;
   	private Image focus02;
   	private Image focus03;
   	private Image focus04;
    
    private int xp;
    private int yp;
    private int width;
    private int height;
    
	/**
	 * 포커스 상품 상품명
	 */
//	private String prdNmF = "프로스펙스 여성 17FW 퍼포먼스 히트웨어.";
	private String prdNmF = "프로스펙스 여성 17FW 퍼포먼스";
	/**
	 * 포커스 상품 판매가
	 */
//	private String slPrice = null;
	private String slPrice = "59,900원";
	
	/**
	 * 포커스 상품 고객맞춤가
	 */
//	private String lastprice = null;
	private String lastprice = "58,900원";
	
	/** 상품혜택가 화살표  */
	private Image main_arrow_price;
    
    public GridItemExtend(String title) {
        this.title = title;
        this.setSize(200, 300);
        this.setLocation(0,0);
    }
    
    public  GridItemExtend(String title, int xp, int yp, int width, int height, Image main_arrow_price) {
    	this.title = title;
    	this.xp = xp;
		this.yp = yp;
		this.width = width;
		this.height = height;
		
		this.main_arrow_price = main_arrow_price;
		
    	this.setBounds(xp, yp, width, height);
//    	this.setSize(100,100);
    	
    }

    public String getTitle() {
        return this.title;
    }

    protected void onActivated() {

    }

    protected void onDeactivated() {

    }

    public void okKeyPressed() {
        LOG.print("GridItem  OK Key");

    }
    
    public void onDestroy() {
    	if(goodImage != null) {
    		goodImage.flush();
    		goodImage = null;
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
		
		if (main_arrow_price != null) {
			 main_arrow_price.flush();
			 main_arrow_price = null;
		 }
    }
    

    public void paint(Graphics g) {
//        g.setColor(Color.lightGray);
//        g.fillRect(0,0,200,200);
//        if(isActivated()) {
//            g.setColor(Color.green);
//            g.drawRect(0,0,298,98);
//        }
    	   g.setColor(Color.lightGray);
//           g.fillRect(xp,yp,width,height);
           g.drawRect(xp,yp,width,height);
           if(isActivated()) {
               g.setColor(Color.red);
               g.drawRect(xp,yp,width-1,height-1);
           }


        g.setFont(new Font("Korean iTV SanSerifD", Font.PLAIN, 15));
        g.setColor(Color.white);
        g.drawString(this.title, 30,30);
        
        g.drawString(this.prdNmF, 10,70+100);
        g.drawString(this.slPrice, 10,90+100);
        
        g.drawImage(main_arrow_price,10+50+2,190-14, this);
        
        g.drawString(this.lastprice, 10+80,90+100);

        super.paint(g);
        
        
    }
    
    public  void drawFocus(Graphics g, int x, int y, int w, int h, ImageObserver observer) {
		g.drawImage(focus01, x, y, w, 4, observer);	// top
		g.drawImage(focus02, x, y + 4, 4, h - 4 - 4, observer);	// left
		g.drawImage(focus03, x + w - 4, y + 4, 4, h - 4 - 4, observer);	// right
		g.drawImage(focus04, x, y + h - 4, w, 4, observer);	// bottom
	}
}