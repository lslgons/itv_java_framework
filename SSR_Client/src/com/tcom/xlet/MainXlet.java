package com.tcom.xlet;


import com.tcom.platform.controller.KeyController;
import com.tcom.platform.controller.StbController;
import com.tcom.platform.dmc.interfaces.DisplayInterface;
import com.tcom.scene.DiagnosticScene;
import com.tcom.ssr.DataManager;
import com.tcom.ssr.SSRConfig;
import com.tcom.util.LOG;
import com.tcom.util.RemoteClassLoader;

import javax.tv.xlet.Xlet;
import javax.tv.xlet.XletContext;
import javax.tv.xlet.XletStateChangeException;
import java.awt.*;


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
    private DiagnosticScene diagScene;

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
        dispInterface = RemoteClassLoader.loadDisplayInterface();

    }

    public void startXlet() throws XletStateChangeException {


        SSRConfig config = SSRConfig.getInstance();
        config.ROOT_SCENE=dispInterface.getScene();
        config.ROOT_SCENE.setBounds(0,0, config.SCENE_WIDTH, config.SCENE_HEIGHT);
        if(config.ENABLE_DIAGNOSTIC) {
            diagScene = new DiagnosticScene();
            config.ROOT_SCENE.add(diagScene, -1);

        }

        //SSRContext
        DataManager.getInstance().requestData(0,0);

        //SSRContainer 등록

        //1. MainLayer


        //2. OverLayer





        LOG.print("Start Application");
        LOG.print(this, "rootScene created");
        config.ROOT_SCENE.setVisible(true);
        config.ROOT_SCENE.requestFocus();

    }

    public void pauseXlet() {

    }

    public void destroyXlet(boolean b) throws XletStateChangeException {
        LOG.print("=========================== Destroy Xlet ====================");
        System.out.println("****************** CJ Tmall Destroy all resource");
        if(StbController.getInstance().getDestoryAppListener() != null) {
            StbController.getInstance().getDestoryAppListener().onApplicationDestory();
        }
        RemoteClassLoader.flushClassCache();
        KeyController.getInstance().shutdown();
        StbController.shutdown();
        System.out.println("****************** CJ Tmall Destroy all resource Complete!!!!!!!");

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
