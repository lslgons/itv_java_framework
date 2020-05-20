package cjtcom.popup;

import com.cj.tvui.ui.Popup;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.Drawer;
import com.cj.tvui.util.LOG;

import java.awt.*;

/**
 * Created by user on 2016-12-07.
 */
public class SubPopup extends Popup {
    public SubPopup(Scene parent) {
        super(parent);
    }

    public void onInit() {

    }

    public void onShow() {

    }

    public void onHide() {

    }

    public void onDestroy() {

    }

    public void onKeyDown(int keycode) {
        LOG.print("SubPopup KeyDown : "+keycode);
        close();

    }

    public void onPaint(Graphics g) {
        LOG.print("SubPopup Paint");
        Drawer.setDimm(g, new Color(0,0,0,200));
        g.setColor(Color.magenta);
        g.fillRect(400, 0, 100, 100);
    }

    public void timerWentOff() {
        LOG.print(this, "timer");
    }

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}
}
