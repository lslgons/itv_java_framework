package com.tcom.ssr;

import com.tcom.scene.BaseScene;
import com.tcom.util.LOG;

import java.awt.*;

public class SSRContainer extends BaseScene {
    SSRComponent mainComponent;
    SSRComponent overComponent;
    boolean isOverlayEnabled;
    public void onInit() {
        this.mainComponent=new SSRComponent(this);
        this.add(mainComponent);
        this.overComponent=new SSRComponent(this);
        this.add(overComponent);
        isOverlayEnabled=false;
        this.overComponent.setVisible(false);
        this.mainComponent.requestData("");
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
