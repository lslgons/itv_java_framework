package cjtcom.popup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.cj.tvui.Keys;
import com.cj.tvui.ui.Popup;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.Drawer;
import com.cj.tvui.util.LOG;

public class SampleMainPopup extends Popup {
    public SampleMainPopup(Scene parent) {
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
//            openPopup("cjtcom.popup.SubPopup", null);
        } else if(keycode == Keys.VK_OK){
			close();
		}

    }

    public void onPaint(Graphics g) {
        LOG.print(this, "MainPopup Paint");
        g.setFont(new Font("Korean iTV SanSerifD", Font.PLAIN, 17));
        
        Drawer.setDimm(g, new Color(0,0,0,200));
        
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
    }

    public void timerWentOff() {
        LOG.print(this, "timer");
    }

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}
}