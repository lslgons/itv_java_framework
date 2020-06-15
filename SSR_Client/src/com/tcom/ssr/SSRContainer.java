package com.tcom.ssr;

import com.tcom.scene.BaseScene;
import com.tcom.util.LOG;

import java.awt.*;

public class SSRContainer extends BaseScene {
    private static final int LOADING_WAIT_TIME=500;

    SSRComponent mainComponent;
    SSRComponent overComponent;
    SSRComponent loadingComponent;
    boolean isOverlayEnabled;
    boolean isShowLoading;
    boolean showLoadingCancelRequested; //데이터 요청 후 0.5 이후에 로딩바가 보여져야 함
    public void enableOverlay(String componentName) {
        if(!isOverlayEnabled) {
            LOG.print("enable overlay");
            this.overComponent.requestData(componentName);
            this.overComponent.setVisible(true);
            isOverlayEnabled=true;
            repaint();
        } else {
            LOG.print("already enabled overlay: "+overComponent.getName());
        }

    }

    public void disableOverlay() {
        if(isOverlayEnabled) {
            this.overComponent.invalidate();
            this.overComponent.setVisible(false);
            isOverlayEnabled=false;
        } else {
            LOG.print("no overlay");
        }

    }


    public void onInit() {
        this.mainComponent=new SSRComponent(this, SSRConstant.COMPONENT_MODE_NORMAL);
        this.overComponent=new SSRComponent(this, SSRConstant.COMPONENT_MODE_OVERLAY);
        this.loadingComponent=new SSRComponent(this, SSRConstant.COMPONENT_MODE_LOADING);

        this.add(loadingComponent);
        this.add(overComponent);
        this.add(mainComponent);

        isOverlayEnabled=false;
        isShowLoading=false;
        loadingComponent.setVisible(false);
        //this.overComponent.setVisible(false);
        this.mainComponent.requestData(null);
    }

    public void onShow() {

    }

    public void onHide() {

    }

    public void onDestroy() {

    }

    public void onKeyDown(int keycode) {
        LOG.print(this, "onKeyDown : "+keycode);
        if(isShowLoading) {
            LOG.print("Can't propagate key event while loading display available");

        } else {
            if (isOverlayEnabled) {
                this.overComponent.onKeyDown(keycode);
            } else {
                this.mainComponent.onKeyDown(keycode);
            }
        }

    }

    Object loadingMutex=new Object();
    public void showLoading(boolean enable) {
        if(loadingComponent!=null) {
            LOG.print("Show Loading : "+enable);
            if(enable) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            synchronized (loadingMutex) {
                                showLoadingCancelRequested=false;
                                Thread.sleep(SSRContainer.LOADING_WAIT_TIME);
                                loadingComponent.startInterval();
                                loadingComponent.setVisible(true);

                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            } else {
                synchronized(loadingMutex) {
                    loadingComponent.stopInterval();
                    loadingComponent.setVisible(false);
                }

            }
        }


    }

    public void onPaint(Graphics g) {
    }

    public void timerWentOff() {

    }
}
