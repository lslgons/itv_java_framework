package cjtcom;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.LOG;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by daegon.kim on 2016-12-02.
 */
public class SubScene extends Scene {


    public void onInit() {
        LOG.print(this, "Sub Scene Init");

    }

    public void onShow() {
        LOG.print(this, "onShow");
    }

    public void onHide() {
        LOG.print(this, "onHide");
    }

    public void onDestroy() {
        LOG.print(this, "onDestroy");

    }

    public void onKeyDown(int keycode) {
        LOG.print(this, "Key Down :: " + keycode);
        if(keycode == Keys.VK_OK) {
            //엔터 입력 시 SubScene으로 전환
        	System.out.println("==================20min ... popupScene ok");
            popScene(); //20min.. 맨위에 있는 scene을 stack에서 한개 빼고 나오게 한다. // 스텍에 쌓이지 않는다.
            System.out.println("SceneController. scene Cnt is...["+SceneController.getInstance().getCount()+"]");
        }else if(keycode == Keys.VK_0) {
        	System.out.println("keycode========0 pushScene call=======");
        	pushScene("cjtcom.MainScene", null);
        	System.out.println("SceneController. scene Cnt is...["+SceneController.getInstance().getCount()+"]");
        	
        }
    }

    public void onPaint(Graphics g) {
        LOG.print(this, "SubScene Paint");
        g.setColor(Color.red);
        g.drawRect(0,0,100,100);
        g.fillRect(100,100,200,200);
    }

    public void timerWentOff() {
        LOG.print(this, "timer");
    }

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}
}
