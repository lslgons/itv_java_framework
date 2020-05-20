package example;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.LOG;

import java.awt.*;

/**
 * Created by daegon.kim on 2016-12-02.
 */
public class SecondScene extends Scene {


    public void onInit() {
        LOG.print(this, "Second Scene Init");

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
        if(keycode == Keys.VK_UP) {
        	if(isShowStaticScene()) {
        		hideStaticScene();
        	} else {
        		showStaticScene();
        	}
        }else {
	        popScene();
        }
        
    }

    public void onPaint(Graphics g) {
        LOG.print(this, "SubScene Paint");
        g.setColor(new Color(255,0,0));
        g.drawString("This is Second Scene. Draw Red Rectangle", 100, 100);
        g.drawString("Press Up Key to toggle Static Scene", 100, 120);
        g.drawString("Press Other key to close scene", 100, 140);
        
        g.fillRect(100,200,250,250);
    }

    public void timerWentOff() {
        LOG.print(this, "timer");
    }

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}
}
