package example;

import java.awt.Graphics;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.KeyController;
import com.cj.tvui.controller.MediaController;
import com.cj.tvui.dmc.interfaces.VODInterface;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.LOG;

public class VODScene extends Scene {

	boolean isPlaying = false;
	public void onInit() {
		VODInterface.VODEventListener listener = new VODInterface.VODEventListener() {
			
			public void vodPlayerStopEvent(String assetid) {
				// TODO Auto-generated method stub
				
			}
			
			public void vodPlayerState(boolean played, boolean exception, String message) {
				// TODO Auto-generated method stub
				
			}
			
			public void vodPlayerEndEvent() {
				// TODO Auto-generated method stub
				
			}
			
			public void rateChangeEvent(float rate) {
				// TODO Auto-generated method stub
				
			}
		};
		
		MediaController.getInstance().addVodEventListener(listener);
		KeyController.getInstance().setEnableBackKey(true);
	}

	public void onShow() {
		// TODO Auto-generated method stub

	}

	public void onHide() {
		// TODO Auto-generated method stub

	}

	public void onDestroy() {
		MediaController.getInstance().stopVOD();

	}

	public void onKeyDown(int keycode) {
		switch(keycode) {
		case Keys.VK_BACK:
			LOG.print(this, "Key back");
			popScene();
			break;
		case Keys.VK_OK:
			if(!isPlaying) {
				MediaController.getInstance().startVOD("M68G90A9SGL100004100");
				isPlaying = true;
			} else {
				MediaController.getInstance().stopVOD();
				isPlaying = false;
			}
			
			break;
		}

	}

	public void onPaint(Graphics g) {
		g.drawString("Press OK to Play and Stop VOD", 100, 100);
		g.drawString("Press Back to Close Scene", 100, 120);

	}

	public void timerWentOff() {
		// TODO Auto-generated method stub

	}

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}

}
