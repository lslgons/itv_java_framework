package com.landman.ssr.controller;

import com.cj.tvui.dmc.interfaces.AVInterface;
import com.cj.tvui.dmc.interfaces.VODInterface;
import com.cj.tvui.util.RemoteClassLoader;

import java.awt.*;

/**
 * Media Controller
 * 현재 방송 중 AV 사이즈 변경
 * VOD 재생관련 인터페이스 제공
 * 
 * @author daegon.kim
 *
 */
public final class MediaController {
	
	private static MediaController instance = null;
	public static MediaController getInstance() {
		if(instance == null) instance = new MediaController();
		return instance;
	}
	
	/*
	 * RemoteClassLoader로 가져오는 해당 객체는 AVInterface를 구현하면서 VODInterface를 구현함, Type Casting을 통해 사용하도록 함
	 */
	AVInterface avInterface = null;
	
	boolean isVODPlaying = false;
	
	private MediaController() {
		avInterface = RemoteClassLoader.loadAVInterface();
	}

    /**
     * 현재 방송 중인 영상 사이즈 변경
     * @param x
     * @param y
     * @param w
     * @param h
     */
	public void changeVideoSize(int x, int y, int w, int h) {
		Rectangle rect = new Rectangle(x, y, w, h);
		this.changeVideoSize(rect);
	}

    /**
     * 현재 방송 중인 영상 사이즈 변경
     * @param rect 좌표
     */
	public void changeVideoSize(Rectangle rect) {
		avInterface.changeVideoSize(rect);
	}

    /**
     * 현재 방송 중인 영상 사이즈 반환
     * @return 영상 사이즈 및 좌표
     */
	public Rectangle getCurrentVideoSize() {
		return avInterface.getCurrentVideoSize();
	}

    /**
     * 현재 방송중인 영상 비활성화
     */
	public void turnOffVideo() {
		avInterface.turnOffVideo();
	}

    /**
     * 현재 방송 중인 영상 활성화
     */
	public void turnOnVideo() {
		avInterface.turnOnVideo();
	}
	
	/*
	 * VOD Control
	 */

    /**
     * VOD 재생 시작
     * @param assetID
     */
	public void startVOD(String assetID) {
		((VODInterface)avInterface).startVOD(assetID);
		this.isVODPlaying = true;
		KeyController.getInstance().setEnableTrickKey(true);
	}

    /**
     * 재생 중 VOD종료
     */
	public void stopVOD() {
		((VODInterface)avInterface).destroyVOD();
		this.isVODPlaying = false;
		KeyController.getInstance().setEnableTrickKey(false);
	}

    /**
     * VOD재생 영상 이벤트 수신 리스너 등록
     * @param listener 리스너 객체
     * @see VODInterface.VODEventListener
     */
	public void addVodEventListener(VODInterface.VODEventListener listener) {
		((VODInterface) avInterface).addVodEventListener(listener);
	}

    /**
     * 등록된 VOD영상 이벤트 리스너 제거
     * @param listener 리스너 객체
     * @see VODInterface.VODEventListener
     */
	public void removeVodEventListener(VODInterface.VODEventListener listener) {
		((VODInterface) avInterface).removeVodEventListener(listener);
	}
	

}
