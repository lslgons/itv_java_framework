package com.tcom.ssr;

import com.tcom.scene.BaseScene;
import com.tcom.util.LOG;

import java.awt.*;

public class SSRContainer extends BaseScene {
    SSRComponent mainComponent;
    SSRComponent overComponent;
    boolean isOverlayEnabled;

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
        this.mainComponent=new SSRComponent(this, false);
        this.overComponent=new SSRComponent(this, true);
        this.add(overComponent);
        this.add(mainComponent);
        isOverlayEnabled=false;
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
        if (isOverlayEnabled) {
            this.overComponent.onKeyDown(keycode);
        } else {
            this.mainComponent.onKeyDown(keycode);
        }
    }

    public void onPaint(Graphics g) {
    }

    public void timerWentOff() {

    }
}
