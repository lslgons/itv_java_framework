package com.cj.tvui.dmc.defaults;

import java.awt.Container;
import java.awt.event.KeyListener;

import org.havi.ui.HSceneFactory;
import org.havi.ui.HSceneTemplate;
import org.havi.ui.HScreenDimension;
import org.havi.ui.HScreenPoint;

import com.cj.tvui.controller.KeyController;
import com.cj.tvui.dmc.interfaces.DisplayInterface;

/**
 * Created by daegon.kim on 2016-12-07.
 */
public class Display implements DisplayInterface{

    public Container getScene() {
        HSceneTemplate req = new HSceneTemplate();
        req.setPreference(HSceneTemplate.SCENE_SCREEN_LOCATION, new HScreenPoint(0.0f, 0.0f), HSceneTemplate.PREFERRED);
        req.setPreference(HSceneTemplate.SCENE_SCREEN_DIMENSION, new HScreenDimension(1.0f, 1.0f), HSceneTemplate.PREFERRED);
        Container container = HSceneFactory.getInstance().getBestScene(req);
        container.addKeyListener((KeyListener)KeyController.getInstance().getKeymap().getKeyListener());
        return container;
    }
}
