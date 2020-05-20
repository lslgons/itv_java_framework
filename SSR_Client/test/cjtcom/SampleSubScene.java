package cjtcom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.LOG;

public class SampleSubScene extends Scene {
    public void onInit() {
        LOG.print(this, "Sample SubScene Init");
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
        	System.out.println("keycode========0 pushScene call=======");
        	pushScene("cjtcom.SampleMainScene", null);
        	System.out.println("SceneController. scene Cnt is...["+SceneController.getInstance().getCount()+"]");
        }else if(keycode == Keys.VK_0) {
//         	System.out.println("==================20min ... popupScene ok");
//            popScene(); //20min.. 맨위에 있는 scene을 stack에서 한개 빼고 나오게 한다. // 스텍에 쌓이지 않는다.
//            System.out.println("SceneController. scene Cnt is...["+SceneController.getInstance().getCount()+"]");
        }
    }

    public void onPaint(Graphics g) {
        LOG.print(this, "SampleSubScene Paint");
        g.setFont(new Font("Korean iTV SanSerifD", Font.PLAIN, 17));
        
        ///////bg//////////////////////////////
        g.setColor(Color.gray);
        g.fillRect(0,0,960,540);
        g.clearRect(0, 0, 268, 197);
        ////////////////////////////////////////
    
        g.setColor(Color.black);
        g.fillRect(100+20,450-30,740,100);
        
        g.setColor(Color.WHITE);
        g.drawString("Sub Scene Layer", 130, 400+50+30 -30-10);
        
        ///////button/////////////////
        g.setColor(Color.YELLOW);
        g.fillRect(40,210,90,40);
        
        g.setColor(Color.BLACK);
        g.drawString("Button 1", 40+5,210 + 25);
        
        g.setColor(Color.BLACK);
        g.drawRect(40,210,90,40);
        ///////////////////////////////
        
        //////Text Description//////////////////////////////////////
  		g.setColor(Color.BLACK);
  		g.fillRect(320, 10, 450, 200);

  		g.setColor(Color.WHITE);
  		g.drawString("1. Button 1 Press: To the SampleMainScene", 320+10, 30);
  		/////////////////////////////////////////////////////////////
    }

    public void timerWentOff() {
        LOG.print(this, "timer");
    }

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}
}