package example;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.MediaController;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.LOG;

public class AVScene extends Scene {

	boolean isChanged =false;
	public void onInit() {
		// TODO Auto-generated method stub

	}

	public void onShow() {
		// TODO Auto-generated method stub

	}

	public void onHide() {
		// TODO Auto-generated method stub

	}

	public void onDestroy() {
		// TODO Auto-generated method stub

	}

	public void onKeyDown(int keycode) {
		// TODO Auto-generated method stub
		switch(keycode) {
		case Keys.VK_OK : 
			if(!isChanged) {
				LOG.print(this, "get Current Video Size");
				Rectangle rect = MediaController.getInstance().getCurrentVideoSize();
				LOG.print(this, rect.x +" "+rect.y+" "+rect.width+" "+rect.height);
				LOG.print(this, "Resize AV to 50, 50, 500, 300");
				Rectangle newRect = new Rectangle(50,50,500,300);
				MediaController.getInstance().changeVideoSize(newRect);
				isChanged = true;
			}
			
			break;
		case Keys.VK_BACK :
			LOG.print("Close Current Scene");
			MediaController.getInstance().changeVideoSize(new Rectangle(0,0,960,540));
			popScene();
			break;
		default:
			break;
			
		}
	}

	public void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		if(isChanged) {
			g.drawString("Press Back key to close current scene", 100, 100);
		} else {
			g.drawString("Press OK key to resize video size", 100, 100);
		}
	}

	public void timerWentOff() {
		// TODO Auto-generated method stub

	}

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}

}
