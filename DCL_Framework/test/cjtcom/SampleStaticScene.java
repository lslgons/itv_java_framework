package cjtcom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.cj.tvui.Keys;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.Drawer;
import com.cj.tvui.util.LOG;

public class SampleStaticScene extends Scene {
	
    public void onInit() {
        LOG.print(this, "Sample Static Scene Init");

    }

    public void onShow() {
        LOG.print(this, "onShow");
    }

    public void onHide() {
        LOG.print(this, "onHide");
    }

    public void onDestroy() {
        LOG.print(this, "onDestroy");

    }

    public void onKeyDown(int keycode) {
        LOG.print(this, "Key Down :: " + keycode);
        if(keycode == Keys.VK_OK) {
        	
        }else if(keycode == Keys.VK_0) {
        	
        }
    }

    public void onPaint(Graphics g) {
        LOG.print(this, "Static Scene Paint");
        g.setFont(new Font("Korean iTV SanSerifD", Font.PLAIN, 17));
        
//        g.setColor(Color.BLACK); // border Paint...
        g.setColor(Color.RED); 
        g.drawRect(0,0,960,540);
        g.drawRect(0+1,0+1,960-2,540-2);
        g.drawRect(0+2,0+2,960-4,540-4);
        g.drawRect(0+3,0+3,960-6,540-6);
        
        g.setColor(Color.BLACK);
        g.drawString("Static Scene Border ->", 790, 50);
        g.drawString("960-540", 790, 50+25);
        
        g.setColor(Color.RED);
        g.fillRect(100,450,740,50);
        
        g.setColor(Color.WHITE);
        g.drawString("Static Scene Layer", 130, 400+50+30);
        
    }
    
	public void timerWentOff() {
        LOG.print(this, "timer");
    }

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}
}