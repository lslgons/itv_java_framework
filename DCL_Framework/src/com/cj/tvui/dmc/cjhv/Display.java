package com.cj.tvui.dmc.cjhv;

import java.awt.Container;

import com.alticast.navsuite.service.OverlappedDialogHandler;
import com.alticast.navsuite.service.OverlappedUIManager;
import com.cj.tvui.Constants;
import com.cj.tvui.controller.KeyController;
import com.cj.tvui.dmc.interfaces.DisplayInterface;

/**
 * Created by daegon.kim on 2016-12-07.
 */
public class Display implements DisplayInterface{

    public Container getScene() {
    	Container container = OverlappedUIManager.getInstance().createOverlappedDialog(50, (OverlappedDialogHandler)KeyController.getInstance().getKeymap().getKeyListener(),OverlappedUIManager.GRAPHICS_960_540);
    	container.setBounds(0, 0, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
    	container.setVisible(true);
    	
    	return container;
    }
}
