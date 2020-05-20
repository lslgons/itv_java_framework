package com.cj.tvui.ui;


import com.cj.tvui.util.LOG;

/**
 * 팝업 화면을 그리기위한 기초클래스
 * @author daegon.kim (daegon.kim1@cj.net)
 * @since 2016-12-05
 * @see Scene
 */
public abstract class Popup extends Scene {

    private Scene parentScene;
    public Popup(Scene parent) {
        LOG.print(this, "Popup Constructor");
        this.parentScene = parent;


    }

    /**
     * 현재 활성화된 팝업을 종료 함
     */
    protected void close() {
        parentScene.closePopup();
    }

    protected void close(String msg) {
        parentScene.closePopup(msg);
    }

}
