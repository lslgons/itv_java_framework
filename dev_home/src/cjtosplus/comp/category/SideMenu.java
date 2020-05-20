package cjtosplus.comp.category;

import com.cj.tvui.ui.Scene;
import com.cj.tvui.ui.Widget;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by user on 2017-10-31.
 */
public class SideMenu extends Widget {
    private Rectangle rect;
    private LinkedList items = new LinkedList();
    private Scene _scene = null;
    public SideMenu(Scene parent) {
        this._scene = parent;
    }
    protected void onActivated() {

    }

    protected void onDeactivated() {

    }

    public void okKeyPressed() {

    }
}
