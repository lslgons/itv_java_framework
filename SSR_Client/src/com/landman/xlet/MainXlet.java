package com.landman.xlet;


import com.landman.platform.controller.SceneController;
import com.landman.platform.dmc.interfaces.DisplayInterface;
import com.landman.ssr.SSRConfig;
import com.landman.util.LOG;
import com.landman.util.PropertyReader;
import com.landman.util.RemoteClassLoader;

import javax.tv.xlet.Xlet;
import javax.tv.xlet.XletContext;
import javax.tv.xlet.XletStateChangeException;
import java.awt.*;
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


    /*
    Implemented method
     */
    XletContext context;
    static MainXlet _app=null;

    /**
     * DMC별 UI레이아웃 설정을 위한 인터페이스 객체
     */

    DisplayInterface dispInterface = null;
    Container rootScene;
    public void initXlet(XletContext xletContext) throws XletStateChangeException {
        SSRConfig config = SSRConfig.getInstance();
        System.out.println("***************************************************");
        System.out.println("* iTV Server Side Rendering JAVA Client            ");
        System.out.println("* created by superiorgon (superiorgon@gmail.com)   ");
        System.out.println("* Version    : "+config.APP_VER);
        System.out.println("* Release    : "+config.APP_RELEASE);
        System.out.println("* SSR Host : "+config.SSR_HOST);
        System.out.println("***************************************************");
        this.context=xletContext;
        _app=this;
        System.out.println("==> Init Application, Request Home Page");


    }

    public void startXlet() throws XletStateChangeException {
        dispInterface = RemoteClassLoader.loadDisplayInterface();
        rootScene = dispInterface.getScene();
        rootScene.setBounds(0,0, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        if(Constants.ENABLE_DIAGNOSTIC) {
            diagScene = new DiagnosticScene();
        }
        if(Constants.ENABLE_DIAGNOSTIC) {
            rootScene.add(diagScene, -1);
        }
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
