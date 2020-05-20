package com.cj.tvui;

import com.cj.tvui.util.PropertyReader;

/**
 * 초기 설정 값, config.property파일에 의해 관리됨
 * Created by user on 2016-12-02.
 */
public class Constants {
	 /**
     * 첫 진입 Scene클래스 경로
     */
    public static String HOME_SCENE;
    /**
     * 클래스 다운로드 호스트 (IP:Port)
     * 20190328 - daegon KT 임시로 박아넣음
     */
    public static String DCL_HOST="http://210.122.101.156:11080/cjlive/kt/acap/";
    
    /**
     * 이미지 호스트 (IP:Port)
     */
    public static String IMG_HOST;
    /**
     * Target : PC or STB
     */
    public static boolean IS_EMUL;
    /**
     * 어플리케이션 명
     */
    public static String APP_NAME;
    /**
     * 어플리케이션 버전
     */
    public static String APP_VER;
    /**
     * 로그 활성화 여부
     */
    public static boolean ENABLE_LOG;
    /**
     * 진단용 Scene 활성화여부
     */
    public static boolean ENABLE_DIAGNOSTIC;
    /**
     * 해상도 W
     */
    public static int SCENE_WIDTH;
    /**
     * 해상도 H
     */
    public static int SCENE_HEIGHT;

    /**
     * SD Config
     */
    public static boolean IS_HD=true;
    public final static int SD_WIDTH=720;
    public final static int SD_HEIGHT=480;

    /**
     * 대상 플랫폼 사업자
     */
    public static String DMC_NAME;

    /**
     * 클래스 캐시 사용여부
     */
    public static boolean USE_CLASS_CACHE;
    
    /**
     * 클라이언트 소켓 사용여부
     * @deprecated
     */
    public static boolean STB_SERVER_ENABLE=false;
    /**
     * 클라이언트 소켓 포트 설정
     * @deprecated
     */
    public static int STB_SERVER_PORT=64302;


    /**
     * config.properties 객체
     */
    public static PropertyReader CONFIG;

}
