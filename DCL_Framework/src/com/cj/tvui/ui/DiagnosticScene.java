package com.cj.tvui.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;

import com.cj.tvui.util.LOG;
import com.cj.tvui.Constants;
import com.cj.tvui.controller.KeyController;
/**
 * @author daegon.kim
 * 개발용 정보 Scene, 배포 시 비활성화
 */

public class DiagnosticScene extends Scene {

	Color bgColor = new Color(255,255,0,100);
	Color fontColor = new Color(255,0,0);
	
	final int kbUnit = 1024;
	int refreshCount=0;
	
	final int width = 540;
	final int height = 480;
	public void onInit() {
		setBounds(Constants.SCENE_WIDTH-width, 0, width, height);
		
	}

	public void onShow() {

	}

	public void onHide() {

	}

	public void onDestroy() {

	}

	public void onKeyDown(int keycode) {

	}

	public void onPaint(Graphics g) {
		g.setColor(bgColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(fontColor);
		int startHPos = 20;
		int lineHeight = 20;
		if(Constants.DMC_NAME.equalsIgnoreCase("kt")) {
			startHPos = 50;
			lineHeight = 30;
		}
		g.drawString("** Diagnostics **", 20, startHPos);
		//1.Application Name
		g.drawString("Application : " + Constants.APP_NAME, 20, startHPos+=lineHeight);
		g.drawString("Version : "+Constants.APP_VER, 20, startHPos+=lineHeight);
		//2.DCL Host
		g.drawString("DCL Host : "+Constants.DCL_HOST, 20, startHPos+=lineHeight);
		//3.Memory
		Runtime runtime = Runtime.getRuntime();
		g.drawString("Used Memory : "+(runtime.totalMemory()-runtime.freeMemory())/kbUnit+"KB, ("+runtime.freeMemory()/kbUnit+"KB remained)", 20, startHPos+=lineHeight);
		g.drawString("Total Available Memory : "+(runtime.totalMemory())/kbUnit+"KB", 20, startHPos+=lineHeight);
		//4.Thread
		g.drawString("Activated Thread : "+Thread.activeCount(), 20, startHPos+=lineHeight);
		//5.Key Consume
		g.drawString("Back Key : "+(KeyController.getInstance().isEnableBackKey()?"Enable":"Disable"), 20, startHPos+=lineHeight);
		g.drawString("Number Key : "+(KeyController.getInstance().isEnableNumKey()?"Enable":"Disable"), 20, startHPos+=lineHeight);
		g.drawString("Hot Key : "+(KeyController.getInstance().isEnableHotKey()?"Enable":"Disable"), 20, startHPos+=lineHeight);
		g.drawString("Trick Key : "+(KeyController.getInstance().isEnableTrickKey()?"Enable":"Disable"), 20, startHPos+=lineHeight);
		
	}

	public void timerWentOff() {
		// TODO Auto-generated method stub
		if(refreshCount == 5) {
			refreshCount = 0;
			repaint();
		}
		refreshCount++;

	}

    protected void onRequestSuccess(int trId, Map resHeader, Object response) {

    }

    protected void onRequestFailed(int trId, int errCode, Object result) {

    }

    public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}

}
