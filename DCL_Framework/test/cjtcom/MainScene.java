package cjtcom;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.network.HttpConnect;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.LOG;

import java.awt.*;
import java.awt.event.KeyEvent;

import org.json.simple.JSONObject;

/**
 * Created by daegon.kim on 2016-12-02.
 */
public class MainScene extends Scene {

    public void onInit() {
        LOG.print(this, "Main Scene Init");
        //SceneController.getInstance().setStaticScene("cjtcom.StaticScene", null);

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
            pushScene("cjtcom.SubScene", null);
        } else if(keycode ==Keys.VK_UP) {
            openPopup("cjtcom.popup.MainPopup", null);
        } else if(keycode == Keys.VK_1) {
        	SceneController.getInstance().showStaticScene(true);
        } else if(keycode == Keys.VK_2) {
        	SceneController.getInstance().showStaticScene(false);
        } else if(keycode == Keys.VK_3) {
        	SceneController.getInstance().removeStaticScene(); // 제거
        } else if(keycode == Keys.VK_4) {
        	SceneController.getInstance().removeStaticScene(); // 제거
        	SceneController.getInstance().setStaticScene("cjtcom.StaticScene", null); // 등록
        } else if(keycode == Keys.VK_5) {
        	HttpConnect.respDataHttp("210.122.102.109","10080","cjtosplus/kt/index.html",new HttpConnect.HttpResponse() {
				
				public void onSuccess(int code, String resp) {
					LOG.print(this, resp);
				}
				
				public void onFail(int code, String resp) {
					
				}
			}); //Test.. 20min
        }
    }

    public void onPaint(Graphics g) {
        LOG.print(this, "MainScene Paint");
        g.setColor(Color.blue);
        g.drawRect(0,0,100,100);
        g.fillRect(100,100,200,200);
    }

    public void timerWentOff() {
        //LOG.print(this, "timer");
    }

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}
}
