package example;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.ui.Popup;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.Drawer;
import com.cj.tvui.util.LOG;

import java.awt.*;

/**
 * Created by user on 2016-12-07.
 */
public class MainPopup extends Popup {
    public MainPopup(Scene parent) {
        super(parent);
    }

    public void onInit() {
        LOG.print(this, "MainPopup Init");
    }

    public void onShow() {
        LOG.print(this, "MainPopup OnShow()");
    }

    public void onHide() {

    }

    public void onDestroy() {

    }

    public void onKeyDown(int keycode) {
        LOG.print(this, "MainPopup KeyEvent : "+keycode);
        if(keycode == Keys.VK_DOWN) {
            openPopup("example.SubPopup", null);
        } else if(keycode == Keys.VK_1) {
        	showStaticScene();
		} else if (keycode == Keys.VK_2) {
			hideStaticScene();
		} else {
			close();
		}

    }

    public void onPaint(Graphics g) {
        LOG.print(this, "MainPopup Paint");
        Drawer.setDimm(g, new Color(0,0,0,200));
        g.setColor(new Color(255,0,255));
        g.drawString("Popup Opened...", 200, 200);
        g.drawString("Press 1 key to show Static Scene", 200, 220);
        g.drawString("Press 2 key to hide Static Scene", 200, 240);
        g.drawString("Press Down key to open sub popup", 200, 260);
        
        
        g.fillOval(300, 300, 50, 50);
    }

    public void timerWentOff() {
        
    }

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}
}
