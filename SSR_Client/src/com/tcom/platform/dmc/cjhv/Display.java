package com.tcom.platform.dmc.cjhv;

import java.awt.Container;

import com.alticast.navsuite.service.OverlappedDialogHandler;
import com.alticast.navsuite.service.OverlappedUIManager;
import com.tcom.platform.controller.KeyController;
import com.tcom.platform.dmc.interfaces.DisplayInterface;
import com.tcom.ssr.SSRConfig;
import com.tcom.util.LOG;
import org.dvb.event.UserEvent;

/**
 * Created by daegon.kim on 2016-12-07.
 */




public class Display implements DisplayInterface {

    public Container getScene() {
    	LOG.print(this, "*********************************************");
		LOG.print(this, "* OverlappedDialogHandler Set!!!            *");
		LOG.print(this, "*********************************************");

		Container container = OverlappedUIManager.getInstance().createOverlappedDialog(50, (OverlappedDialogHandler) KeyController.getInstance().getKeymap().getKeyListener(),OverlappedUIManager.GRAPHICS_960_540);
    	container.setBounds(0, 0, SSRConfig.getInstance().SCENE_WIDTH, SSRConfig.getInstance().SCENE_HEIGHT);
    	container.setVisible(true);
		SSRConfig.getInstance().ROOT_SCENE=container;
    	return container;
    }


}
