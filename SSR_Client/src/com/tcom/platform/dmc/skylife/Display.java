package com.landman.ssr.dmc.skylife;

import java.awt.Container;
import java.awt.event.KeyListener;

import org.havi.ui.HGraphicsConfiguration;
import org.havi.ui.HGraphicsDevice;
import org.havi.ui.HScene;
import org.havi.ui.HSceneFactory;
import org.havi.ui.HSceneTemplate;
import org.havi.ui.HScreen;
import org.havi.ui.HScreenDimension;
import org.havi.ui.HScreenPoint;
import org.havi.ui.event.HEventGroup;

import com.cj.tvui.controller.KeyController;
import com.cj.tvui.dmc.interfaces.DisplayInterface;
import com.cj.tvui.util.LOG;


/**
 * Created by daegon.kim on 2016-12-07.
 */
public class Display implements DisplayInterface{

    public Container __getScene() {
        HSceneTemplate req = new HSceneTemplate();
        req.setPreference(HSceneTemplate.SCENE_SCREEN_LOCATION, new HScreenPoint(0.0f, 0.0f), HSceneTemplate.PREFERRED);
        req.setPreference(HSceneTemplate.SCENE_SCREEN_DIMENSION, new HScreenDimension(1.0f, 1.0f), HSceneTemplate.PREFERRED);
        Container container = HSceneFactory.getInstance().getBestScene(req);
        container.addKeyListener((KeyListener)KeyController.getInstance().getKeymap().getKeyListener());
        return container;
    }
    
    public Container getScene() {

		HGraphicsDevice hgds[] = HScreen.getDefaultHScreen().getHGraphicsDevices();
		HGraphicsConfiguration curConfig = null;
		for (int i = 0; i < hgds.length; i++) {
			HGraphicsConfiguration config = hgds[i].getCurrentConfiguration();
			LOG.print("CHECK " + i + " th Device: " + config.getPixelResolution());
			if (config.getPixelResolution().width == com.cj.tvui.Constants.SCENE_WIDTH) {
				LOG.print("OK, GOT IT HD");
				curConfig = config;
				break;
			}
		}
		if (curConfig == null) {
			//HD 지원안하는 STB의 경우 SD scene 사용
			LOG.print("ERROR: Couldn't find " + com.cj.tvui.Constants.SCENE_WIDTH + "x" + com.cj.tvui.Constants.SCENE_HEIGHT + " scene!");
			com.cj.tvui.Constants.IS_HD = false;

			for (int i = 0; i < hgds.length; i++) {
				HGraphicsConfiguration config = hgds[i].getCurrentConfiguration();
				LOG.print("CHECK " + i + " th Device: " + config.getPixelResolution());
				if (config.getPixelResolution().width == 720) {
					LOG.print("OK, GOT IT SD");
					curConfig = config;
					break;
				}
			}

			if (curConfig == null) {
				LOG.print("ERROR: Couldn't find " + 720 + "x" + 480 + " scene!");
			}
		}
		LOG.print(this, "getHScene");
		HSceneTemplate hst = new HSceneTemplate();
		hst.setPreference(HSceneTemplate.GRAPHICS_CONFIGURATION, curConfig, HSceneTemplate.REQUIRED);
		HScene scene = HSceneFactory.getInstance().getBestScene(hst);
		scene.addKeyListener((KeyListener)KeyController.getInstance().getKeymap().getKeyListener());
		scene.setKeyEvents((HEventGroup)KeyController.getInstance().getKeymap().getEventGroup());
		return scene;
	
    }
    
}
