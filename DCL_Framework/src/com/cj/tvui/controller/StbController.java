package com.cj.tvui.controller;

import com.cj.tvui.dmc.interfaces.StbInterface;
import com.cj.tvui.util.RemoteClassLoader;

/**
 * 플랫폼 사업자 연관 인터페이스
 * @author daegon.kim (daegon.kim1@cj.net)
 *
 */
public final class StbController {

    private StbInterface stbIntf;
    
    private static StbController instance = null;
    
    public static StbController getInstance() {
    	if(instance == null) instance = new StbController();
    	return instance;
    }
    
    private StbController() {
    	stbIntf = RemoteClassLoader.loadStbInterface();
    }

    /**
     * StbInterface 반환, 플랫폼 마다 생성되는 객체가 다음
     * @return StbInterface
     * @see StbInterface
     */
    public StbInterface getStbInterface() {
    	return stbIntf;
    }

    /**
     * 플랫폼 로그 전송
     * @param logType 로그타입
     * @param serviceID 서비스 ID (KT한정)
     */
    public void sendLog(int logType, String serviceID) {
    	stbIntf.sendLog(logType, serviceID);
    }

    /**
     * 구독자 ID를 반환, 플랫폼에 따라 다른 ID로 대체
     * @return 구독자 ID
     */
    public String getSubscribeID() {
    	return stbIntf.getSubscribeID();
    }

    /**
     * SO코드 반환
     * @return SO 코드
     */
    public String getSOCode() {
    	return stbIntf.getSOCode();
    }

    /**
     * 현재 사용되는 셋탑모델 반환
     * @return 셋탑모델번호
     */
    public String getModelNo() {
    	return stbIntf.getModelNo();
    }

    /**
     * 스마트카드 ID반환, 플랫폼에 따라 다른 ID로 대체
     * @return 스마트카드 ID
     */
    public String getSmartcardID() {
    	return stbIntf.getSmartcardID();
    }

    /**
     * MacAddress 반환, OCAP의 경우 케이블모뎀 Mac Address
     * @return MacAddress
     */
    public String getMacAddr() {
    	return stbIntf.getMacAddr();
    }

    /**
     * 단말 시리얼넘버 반환
     * @return 단말 시리얼 넘버
     */
    public String getDeviceSN() {
    	return stbIntf.getDeviceSN();
    }

    /**
     * 어플리케이션 종료
     * 패키지 경로가 com.cjtmall.MainXlet으로 할당되어 있음, 추후 올바른 경로로 변경해야 함 -> com.cj.tvui.MainXlet 전환
     */
    public void closeApp() {
        com.cj.tvui.MainXlet.closeApp();
        //com.cjtmall.MainXlet.closeApp();
    }


    /**
     * 클래스 로드 오류 리스너
     *
     */
    public interface ClassLoadErrorListener {
        public void onLoadError(String msg);
    }

    public interface DestoryAppListener {
        public void onApplicationDestory();
    }

    private ClassLoadErrorListener _classLoadErrorListener;
    private DestoryAppListener _destroyAppListener;
    public ClassLoadErrorListener getClassLoadErrorListener() {
        return this._classLoadErrorListener;
    }
    public void setClassLoadErrorListener(ClassLoadErrorListener listener) {
        this._classLoadErrorListener = listener;

    }

    public DestoryAppListener getDestoryAppListener() {
        return this._destroyAppListener;
    }

    public void setDestoryAppListener(DestoryAppListener listener) {
        this._destroyAppListener = listener;
    }

    public static void shutdown() {
        if(instance!=null) {
            instance._classLoadErrorListener=null;
            instance._destroyAppListener=null;
            instance.stbIntf=null;
        }
        instance=null;

    }





}
