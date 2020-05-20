package com.cj.tvui.dmc.cjhv;

import java.awt.Rectangle;

import com.cj.tvui.dmc.interfaces.AVInterface;
import com.cj.tvui.dmc.interfaces.VODInterface;

public class AV implements AVInterface, VODInterface{

	public void changeVideoSize(Rectangle rect) {
		// TODO Auto-generated method stub
		
	}

	public void turnOnVideo() {
		// TODO Auto-generated method stub
		
	}

	public void turnOffVideo() {
		// TODO Auto-generated method stub
		
	}

	public Rectangle getCurrentVideoSize() {
		// TODO Auto-generated method stub
		return null;
	}

	public void startVOD(String assetId) {
		// TODO Auto-generated method stub
		
	}

	public void destroyVOD() {
		// TODO Auto-generated method stub
		
	}

	public void vodKeyHandler(int keyCode) {
		// TODO Auto-generated method stub
		
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

	public int getPalyStatus() {
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
