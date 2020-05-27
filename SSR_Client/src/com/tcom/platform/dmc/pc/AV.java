package com.tcom.platform.dmc.pc;

import com.tcom.platform.dmc.interfaces.AVInterface;
import com.tcom.platform.dmc.interfaces.VODInterface;
import com.tcom.util.LOG;

import java.awt.Rectangle;


public class AV implements AVInterface, VODInterface {

	public void resetVideoSize() {

	}

	public void changeVideoSize(Rectangle rect) {
		// TODO Auto-generated method stub
		LOG.print(this, "Change video size to "+rect.toString());
	}

	public void turnOnVideo() {
		// TODO Auto-generated method stub
		LOG.print(this, "Turn On Video");
	}

	public void turnOffVideo() {
		// TODO Auto-generated method stub
		LOG.print(this, "Turn Off Video");
	}

	public Rectangle getCurrentVideoSize() {
		// TODO Auto-generated method stub
		return new Rectangle(0,0,960,540);
	}
	
	//VOD Interface
	

	public void startVOD(String assetId, boolean loop) {
		LOG.print(this, "VOD Start : "+ assetId);
		
	}

	public void destroyVOD() {
		LOG.print(this, "VOD Stopped");
		
	}

	public void vodKeyHandler(int keyCode) {
		LOG.print(this, "VOD Key handler : "+keyCode);
		
	}

	public int getTotPlayTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getTotLongPlayTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCutPlayTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getCurLongPlayTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getPlayStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addVodEventListener(VODEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void removeVodEventListener(VODEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void onlyVODStop() {
		// TODO Auto-generated method stub
		
	}

	public void forcedVODPause() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

}
