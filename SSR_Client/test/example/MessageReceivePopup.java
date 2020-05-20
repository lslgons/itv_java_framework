package example;

import java.awt.Color;
import java.awt.Graphics;

import com.cj.tvui.ui.Popup;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.Drawer;

public class MessageReceivePopup extends Popup {

	String receivedMsg;
	
	public MessageReceivePopup(Scene parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	public void onInit() {
		
		
	}

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		receivedMsg = (String)args[0];
		
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
		close();
		
	}

	public void onPaint(Graphics g) {
		Drawer.setDimm(g, new Color(0,0,0,200));
		if(receivedMsg != null) {
			g.setColor(new Color(255,255,255));
			g.drawString(receivedMsg, 100, 100);
		}
		
	}

	public void timerWentOff() {
		
		
	}

}
