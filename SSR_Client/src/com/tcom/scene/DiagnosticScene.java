package com.tcom.scene;

import com.tcom.platform.controller.KeyController;
import com.tcom.ssr.SSRConfig;

import java.awt.*;

public class DiagnosticScene extends BaseScene {

    Color bgColor = new Color(255,255,0,100);
    Color fontColor = new Color(255,0,0);
    final int kbUnit = 1024;
    int refreshCount=0;

    final int width = 540;
    final int height = 480;

    public void onInit() {
        setBounds(SSRConfig.getInstance().SCENE_WIDTH-width, 0, width, height);
    }


    public void onShow() {

    }

    public void onHide() {

    }

    public void onDestroy() {

    }

    public void onKeyDown(int keycode) {

    }

    public void onPaint(Graphics g) {
        SSRConfig config = SSRConfig.getInstance();
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(fontColor);
        int startHPos = 20;
        int lineHeight = 20;
        if(config.DMC_NAME.equalsIgnoreCase("kt")) {
            startHPos = 50;
            lineHeight = 30;
        }
        g.drawString("** Diagnostics **", 20, startHPos);
        //1.Application Name
        g.drawString("Application : " + config.APP_NAME, 20, startHPos+=lineHeight);
        g.drawString("Version : "+config.APP_VER, 20, startHPos+=lineHeight);
        //2.SSR Host
        g.drawString("SSR Host : "+config.SSR_HOST, 20, startHPos+=lineHeight);
        //3.Memory
        Runtime runtime = Runtime.getRuntime();
        g.drawString("Used Memory : "+(runtime.totalMemory()-runtime.freeMemory())/kbUnit+"KB, ("+runtime.freeMemory()/kbUnit+"KB remained)", 20, startHPos+=lineHeight);
        g.drawString("Total Available Memory : "+(runtime.totalMemory())/kbUnit+"KB", 20, startHPos+=lineHeight);
        //4.Thread
        g.drawString("Activated Thread : "+Thread.activeCount(), 20, startHPos+=lineHeight);
        //5.Key Consume
        g.drawString("Back Key : "+(KeyController.getInstance().isEnableBackKey()?"Enable":"Disable"), 20, startHPos+=lineHeight);
        g.drawString("Number Key : "+(KeyController.getInstance().isEnableNumKey()?"Enable":"Disable"), 20, startHPos+=lineHeight);
        g.drawString("Hot Key : "+(KeyController.getInstance().isEnableHotKey()?"Enable":"Disable"), 20, startHPos+=lineHeight);
        g.drawString("Trick Key : "+(KeyController.getInstance().isEnableTrickKey()?"Enable":"Disable"), 20, startHPos+=lineHeight);
    }

    public void timerWentOff() {
        if(refreshCount == 5) {
            refreshCount = 0;
            repaint();
        }
        refreshCount++;
    }



}
