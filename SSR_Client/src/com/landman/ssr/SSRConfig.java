package com.landman.ssr;


import com.landman.util.PropertyReader;

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
        this.SSR_HOST=config.read("SSR_HOST");


        IS_EMUL=(System.getProperty("os.name").indexOf("Windows") > -1);
        //KT OTS박스가 아닐경우 서비스 종료, SKYLIFE에서 사용됨

        //boolean kt_ots_only_flag=hostConfig.read("KT_OTS_ONLY").equalsIgnoreCase("true");
        //하드코딩 - daegon
        boolean kt_ots_only_flag=false;
        System.out.println("====> KT_OTS_ONLY : "+kt_ots_only_flag);
        if(kt_ots_only_flag) {
            //KT OTS박스 여부 확인
            System.out.println("Check OTS Box");
            if(!Constants.IS_EMUL) {
                //에뮬레이터가 아니여야 함
                try {
                    String ktStbType=System.getProperty("KT_STB_TYPE");
                    System.out.println("KT_STB_TYPE : "+ktStbType);
                    if(ktStbType==null) {
                        System.out.println("KT_STB_TYPE is null, Exit application");
                        return false;
                    } else if(ktStbType.equals("STB_QTS")) {
                        System.out.println("Detect OTS Box");
                    } else {
                        System.out.println("Unable to run application cause not OTS");
                        return false;
                    }
                } catch(Exception e) {
                    System.out.println("Error while get KT_STB_TYPE");
                }

            }
        }
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
     * Target : PC or STB
     */
    public boolean IS_EMUL;
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





}
