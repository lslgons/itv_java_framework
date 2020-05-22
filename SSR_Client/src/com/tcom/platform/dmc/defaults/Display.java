package com.tcom.platform.dmc.defaults;

import java.awt.Container;
import java.awt.event.KeyListener;

import com.tcom.platform.controller.KeyController;
import com.tcom.platform.dmc.interfaces.DisplayInterface;
import org.havi.ui.HSceneFactory;
import org.havi.ui.HSceneTemplate;
import org.havi.ui.HScreenDimension;
import org.havi.ui.HScreenPoint;


/**
 * Created by daegon.kim on 2016-12-07.
 */
public class Display extends DisplayInterface {

    public Container getScene() {
        HSceneTemplate req = new HSceneTemplate();
        req.setPreference(HSceneTemplate.SCENE_SCREEN_LOCATION, new HScreenPoint(0.0f, 0.0f), HSceneTemplate.PREFERRED);
        req.setPreference(HSceneTemplate.SCENE_SCREEN_DIMENSION, new HScreenDimension(1.0f, 1.0f), HSceneTemplate.PREFERRED);
        Container container = HSceneFactory.getInstance().getBestScene(req);
        container.addKeyListener((KeyListener) KeyController.getInstance().getKeymap().getKeyListener());
        setRootScene(container);
        return container;
    }
}
