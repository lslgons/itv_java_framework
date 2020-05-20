package cjtcom;

import java.awt.Color;
import java.awt.Graphics;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.LOG;

public class StaticScene extends Scene {

    public void onInit() {
        LOG.print(this, "Static Scene Init");

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
        g.setColor(Color.GREEN);
        g.drawRect(0,0,720,480);
//        g.fillRect(0,0,720,480);
        
        g.setColor(Color.WHITE);
        g.drawString("Static Scene is Here", 50, 160);
      
        g.setColor(Color.GREEN);
        g.drawString("Static Scene is Here", 50, 190);
        
    }

    public void timerWentOff() {
        LOG.print(this, "timer");
    }

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}
}
