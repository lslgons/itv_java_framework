package com.tcom.platform.dmc.interfaces;

import java.awt.*;

/**
 * Created by daegon.kim on 2016-12-07.
 */
public abstract class DisplayInterface {
    public Container rootScene;
    public abstract Container getScene();
    public void setRootScene(Container c) {
        this.rootScene=c;
    }
    public Container getRootScene() {
        if (rootScene==null) {
            this.getScene();
        }
        return this.rootScene;
    }
}
