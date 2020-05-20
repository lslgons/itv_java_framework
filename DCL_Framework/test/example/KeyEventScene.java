package example;

import java.awt.Graphics;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.KeyController;
import com.cj.tvui.ui.Scene;

public class KeyEventScene extends Scene {

	String keyTyped = new String();
	KeyController keyCtrl = KeyController.getInstance();
	
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
		keyTyped = String.valueOf(keycode);
		switch(keycode) {
		case Keys.VK_OK:
			keyCtrl.setEnableBackKey(!keyCtrl.isEnableBackKey());			
			break;
		case Keys.VK_UP:
			keyCtrl.setEnableNumKey(!keyCtrl.isEnableNumKey());
			break;
		case Keys.VK_DOWN:
			keyCtrl.setEnableHotKey(!keyCtrl.isEnableHotKey());
			break;
		case Keys.VK_RIGHT:
			keyCtrl.setEnableTrickKey(!keyCtrl.isEnableTrickKey());
		case Keys.VK_LEFT:
			popScene();
			break;
		default:
			break;
		}

	}

	public void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		g.drawString("Press OK to Enable/Disable Back Key, State : "+ (keyCtrl.isEnableBackKey()?"Enable":"Disable"), 100, 100);
		g.drawString("Press Up to Enable/Disable Num Key, State : "+ (keyCtrl.isEnableNumKey()?"Enable":"Disable"), 100, 120);
		g.drawString("Press Down Key to Enable/Disable Color Key, State : "+ (keyCtrl.isEnableHotKey()?"Enable":"Disable"), 100, 140);
		g.drawString("Press Right Key to Enable/Disable Trick Key, State : "+ (keyCtrl.isEnableTrickKey()?"Enable":"Disable"), 100, 160);
		g.drawString("Press Left to Exit Current Scene", 100, 180);
		g.drawString("Key Typed : "+keyTyped, 100, 250);

	}

	public void timerWentOff() {
		// TODO Auto-generated method stub

	}

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}

}
