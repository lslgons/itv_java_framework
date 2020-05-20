package cjtosplus.comp.main;

import com.cj.tvui.component.ItemCompEvent;
import com.cj.tvui.ui.Widget;

import java.awt.*;

/**
 * Created by user on 2017-10-31.
 */
/* Banner */
public abstract class Banner extends Widget {
    private boolean  isActivated = false; // Nor , Focus 여부
    private ItemCompEvent evt = null; // ok Event 처리.

    private int bannerType = 0; // 0: default; small. 1: big.

    private Image focTop = null; // 포커스의 분리
    private Image focLeft = null;
    private Image focRight= null;
    private Image focBottom = null;

    public Banner(int type, ItemCompEvent evt) {
        this.bannerType = type;
        this.evt = evt;
    }

    public Banner(int type, Image focTop, Image focLeft, Image focRight, Image focBottom, ItemCompEvent evt) {
        this.bannerType = type;
        this.focTop = focTop;
        this.focLeft = focLeft;
        this.focRight = focRight;
        this.focBottom = focBottom;
        this.evt = evt;
    }

    abstract public Image onBannerChanged();


    public void onActivated() {
        this.isActivated = true;
    }


    public void onDeactivated() {
        this.isActivated = false;
    }


    public void okKeyPressed() {
        evt.doAction();
    }

    public void paint(Graphics g) {
        if(this.isActivated) {  // 포커스
            System.out.println("is Banner Focus.......");

            if(this.focTop != null) {
                g.drawImage(this.focTop, 0, 0, getWidth(), 4, this);	// top
                g.drawImage(this.focLeft, 0, 0 + 4, 4, getHeight() - 4 - 4, this);	// left
                g.drawImage(this.focRight, 0 + getWidth() - 4, 0 + 4, 4, getHeight()  - 4 - 4, this);	// right
                g.drawImage(this.focBottom, 0, 0 + getHeight()  - 4, getWidth(), 4, this);	// bottom
            }else {// default Focus...
                g.setColor(new Color(255,255,0));
                g.drawRect(0, 0, getWidth()-1, getHeight()-1);
            }


        }else {
            System.out.println("is Banner Not Focus.......");
        }
    }

    public void destroy() {
        if (this.focTop != null) {
            this.focTop.flush();
            focTop = null;
        }

        if (this.focLeft != null) {
            this.focLeft.flush();
            focLeft = null;
        }

        if (this.focRight != null) {
            this.focRight.flush();
            focRight = null;
        }

        if (this.focBottom != null) {
            this.focBottom.flush();
            focBottom = null;
        }

    }
}
