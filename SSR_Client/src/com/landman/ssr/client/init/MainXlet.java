package com.landman.ssr.client.init;

import com.cj.tvui.Constants;
import com.cj.tvui.controller.KeyController;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.controller.StbController;
import com.cj.tvui.network.RPConnector;
import com.cj.tvui.util.GlobalImageManager;
import com.cj.tvui.util.LOG;
import com.cj.tvui.util.PropertyReader;
import com.cj.tvui.util.RemoteClassLoader;

import javax.tv.xlet.Xlet;
import javax.tv.xlet.XletContext;
import javax.tv.xlet.XletStateChangeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;


/**
 * 최초 진입 클래스, 송출 설정 시 이 클래스를 Initial Path로 잡아야 함
 * @author daegon.kim (daegon.kim1@cj.net)
 * @since 2016-12-02
 */
public class MainXlet implements Xlet{

    boolean isAvailableToRun;

    private boolean readHostConfig() {
        System.out.println("====> DCL Host for KT :: "+ Constants.DCL_HOST);
        PropertyReader hostConfig = PropertyReader.readProperty("host.properties");
        /**
         * KT에서 host.properties파일을 읽어오지 못함
         * QA/운영 주소 분리하여 하드코딩
         */
        Constants.DCL_HOST=hostConfig.read("HOST_ADDRESS")+hostConfig.read("HOST_PATH");
        //Constants.DCL_HOST="http://210.122.101.156:11080/cjlive/kt/acap/";
        System.out.println("====> DCL Host is set to "+Constants.DCL_HOST);
        Constants.IS_EMUL=(System.getProperty("os.name").indexOf("Windows") > -1);
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
        return true;

    }

    private void readConfig() {

        InputStream configInput = null;

        try {
            configInput = getClass().getResourceAsStream("/config.properties");
        } catch (Exception e) {
            System.out.println("Can't find config.properties on local, find remote repository");
        }
        if(configInput == null) {
            try {
                URL url = new URL(Constants.DCL_HOST+"config.properties");
                System.out.println("Touch to "+Constants.DCL_HOST+"config.properties");
                configInput = url.openStream();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        if(configInput == null) {
            System.out.println("[ERROR] Can't find config.properties on target server. close application...");
        }
        PropertyReader properties = PropertyReader.readProperty(configInput);
        //PropertyReader properties = PropertyReader.readProperty("config.properties");

        Constants.HOME_SCENE=properties.read("HOME_SCENE");
        Constants.IMG_HOST=properties.read("IMG_ADDRESS")+properties.read("IMG_PATH");
        Constants.APP_NAME=properties.read("APPLICATION_NAME");
        Constants.APP_VER=properties.read("APPLICATION_VER");
//        if (System.getProperty("os.name").indexOf("Windows") > -1) Constants.IS_EMUL = true;
//        else Constants.IS_EMUL=false;
        Constants.ENABLE_LOG=properties.read("ENABLE_LOG").equals("true");
        Constants.ENABLE_DIAGNOSTIC=properties.read("ENABLE_DIAGNOSTIC").equals("true");
        Constants.SCENE_WIDTH=Integer.parseInt(properties.read("SCENE_WIDTH"));
        Constants.SCENE_HEIGHT=Integer.parseInt(properties.read("SCENE_HEIGHT"));
        Constants.DMC_NAME = properties.read("DMC").toLowerCase();
        Constants.USE_CLASS_CACHE = properties.read("USE_CLASS_CACHE").equals("true");

        Constants.CONFIG=properties;
    }

    /*
    Implemented method
     */
    XletContext context;
    static MainXlet _app=null;

    public void initXlet(XletContext xletContext) throws XletStateChangeException {
        this.context=xletContext;
        _app=this;
        System.out.println("==> Init Application, Read Host Configurations...");
        isAvailableToRun=readHostConfig();
        System.out.println("Host read complete");
        System.out.println("===================================");
        System.out.println("DCL Host : "+Constants.DCL_HOST);
        System.out.println("KT_OTS_ONLY : "+false);
        System.out.println("===================================");
        if(isAvailableToRun) {
            System.out.println("Read Configuration from server...");
            readConfig();
            System.out.println("Read configration complete... Now Start Application...");
        } else {
            System.out.println("[UI Framework] Unable to run, exit application");
            closeApp();
        }
    }

    public void startXlet() throws XletStateChangeException {
        if(!isAvailableToRun) return;
        // set page from dynamic class
        LOG.print("Start Application");
        LOG.print(this, "rootScene created");
        SceneController.getInstance().pushScene(Constants.HOME_SCENE, null);
        SceneController.getInstance().getRootScene().setVisible(true);
        SceneController.getInstance().getRootScene().requestFocus();
    }

    public void pauseXlet() {

    }

    public void destroyXlet(boolean b) throws XletStateChangeException {
        LOG.print("=========================== Destroy Xlet ====================");
        if(isAvailableToRun) {

            //2018-11-16, 디스트로이 영역을 Thread 태워서 완전실행하도록 함
//            new Thread(new Runnable() {
//                public void run() {
                    System.out.println("****************** CJ Tmall Destroy all resource");
                    if(StbController.getInstance().getDestoryAppListener() != null) {
                        StbController.getInstance().getDestoryAppListener().onApplicationDestory();
                    }
                    GlobalImageManager.getInstance().shutdown();
                    RemoteClassLoader.flushClassCache();
                    SceneController.getInstance().shutdown();
                    KeyController.getInstance().shutdown();
                    RPConnector.shutdown();
                    StbController.shutdown();
                    System.out.println("****************** CJ Tmall Destroy all resource Complete!!!!!!!");
//                }
//            }).start();

        }
        LOG.print("========================= End of destroy xlet ================");
        if(context != null) {
            context.notifyDestroyed();
            context=null;
        }

    }

    public static void closeApp() {
        try {
            _app.destroyXlet(true);
            if(_app.context != null) _app.context.notifyDestroyed();
        } catch (XletStateChangeException e) {
            e.printStackTrace();
        }

    }
}
