package cjtosplus.scenes;

import com.cj.tvui.ui.Scene;
import com.cj.tvui.ui.Widget;
import com.cj.tvui.util.LOG;

import java.awt.*;


/**
 * Created by user on 2017-10-12.
 */
public class HomeScene extends Scene {

    /* Buttons */
    class Button extends Widget {

        public Button(int width, int height, String text, Image icon) {

        }

        public void onActivated() {

        }

        public void onDeactivated() {

        }

        public void okKeyPressed() {

        }
    }
    /* Banner */
    abstract class Banner extends Widget {

        public Banner(int type) {

        }

        abstract public Image onBannerChanged();

        public void onActivated() {

        }


        public void onDeactivated() {

        }

        public void okKeyPressed() {

        }
    }

    /* Flow text */
    class FlowtextBar extends Component {

    }


    public void onInit() {

    }


    public void onDataReceived(Object[] objects) {

    }


    public void onShow() {

    }


    public void onHide() {

    }


    public void onDestroy() {

    }


    public void onKeyDown(int i) {

    }


    public void onPaint(Graphics graphics) {

    }


    public void timerWentOff() {
        LOG.print("timerWentOff");
    }
}
