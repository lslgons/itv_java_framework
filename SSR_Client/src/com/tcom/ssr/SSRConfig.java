package com.tcom.ssr;


import com.tcom.util.PropertyReader;

import javax.tv.xlet.XletContext;
import java.awt.*;

/**
 * 초기 설정 값, config.property파일에 의해 관리됨
 * Created by user on 2016-12-02.
 */



public class SSRConfig {

    /**
     * config.properties 객체
     */
    PropertyReader config;
    private SSRConfig() {

    }
    private static SSRConfig instance;
    public static SSRConfig getInstance() {
        if(instance==null) {
            instance=new SSRConfig();
            instance.readConfig();
        }
        return instance;
    }

    public void readConfig() {
        config = PropertyReader.readProperty("config.properties");
        /**
         * KT에서 host.properties파일을 읽어오지 못함
         * QA/운영 주소 분리하여 하드코딩
         */
        SSR_HOST=config.read("SSR_HOST");
        SSR_PORT=Integer.parseInt(config.read("SSR_PORT"));
        APP_NAME=config.read("APP_NAME");
        APP_VER=config.read("APP_VER");
        ENABLE_LOG=config.read("ENABLE_LOG").equalsIgnoreCase("true");
        ENABLE_DIAGNOSTIC=config.read("ENABLE_DIAGNOSTIC").equalsIgnoreCase("true");
        SCENE_WIDTH=Integer.parseInt(config.read("SCENE_WIDTH"));
        SCENE_HEIGHT=Integer.parseInt(config.read("SCENE_HEIGHT"));
        SSR_URI="H"+SCENE_HEIGHT;
        DMC_NAME=config.read("DMC_NAME");


        IS_EMUL=(System.getProperty("os.name").indexOf("Windows") > -1);
        //KT OTS박스가 아닐경우 서비스 종료, SKYLIFE에서 사용됨
        System.out.println("Check OTS Box");
        String ktStbType = System.getProperty("KT_STB_TYPE");
        if (ktStbType !=null) {
            IS_KT_OTS=ktStbType.equalsIgnoreCase("STB_QTS");
        } else {
            IS_KT_OTS=false;
        }

    }

    public PropertyReader getPropertyReader() {
        return config;
    }

    /**
     * 어플리케이션 명
     */
    public String APP_NAME;

    /**
     * 어플리케이션 버전
     */
    public String APP_VER;

    /**
     * 어플리케이션 릴리즈
     */
    public String APP_RELEASE;
    /**
     * SSR Host
     */
    public String SSR_HOST;
    /**
     * SSR PORT
     */
    public int SSR_PORT;

    /**
     * SSR RESOLUTION URI
     */
    public String SSR_URI;

    /**
     * Target : PC or STB
     */
    public boolean IS_EMUL;

    public boolean IS_KT_OTS;
    /**
     * 로그 활성화 여부
     */
    public boolean ENABLE_LOG;
    /**
     * 진단용 Scene 활성화여부
     */
    public boolean ENABLE_DIAGNOSTIC;
    /**
     * 해상도 W
     */
    public int SCENE_WIDTH;
    /**
     * 해상도 H
     */
    public int SCENE_HEIGHT;

    /**
     * SD Config
     */
    public boolean IS_HD=true;
    public final int SD_WIDTH=720;
    public final int SD_HEIGHT=480;

    /**
     * 대상 플랫폼 사업자
     */
    public String DMC_NAME;

    /**
     * RootScene
     */

    public Container ROOT_SCENE;

    /**
     * XletContext
     */

    public XletContext XLET_CONTEXT;

    /**
     * 알래스카 SMS TAG 정보
     * Use only LGHV, HCN
     */
    public String ALASKA_BIZ_TAG;
    public String ALASKA_CPN_TAG;
    public int IS_ALASKA_UI=-1; //-1: Not Define, 0:False, 1:True


    public String ITV_FONT="Korean iTV SanSerifD";




}
