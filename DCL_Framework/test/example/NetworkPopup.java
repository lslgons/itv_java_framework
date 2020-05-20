package example;

import com.cj.tvui.network.RPConnector;
import com.cj.tvui.network.RPResponse;
import com.cj.tvui.ui.Popup;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.Drawer;
import com.cj.tvui.util.LOG;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daegon.kim on 2016-12-07.
 */
public class NetworkPopup extends Popup {

	RPResponse response = new RPResponse() {
		
		public void onReceived(int code, Object response) {
			LOG.print(this,"=====>> Response received" + (String)response);
			responseData = (String)response;
			repaint();
		}
		
		public void onFailed(int code, Object response) {
			// TODO Auto-generated method stub
			LOG.print(this, "Request Failed... : "+ code +",  "+(String)response);
			
		}
	};		
	boolean isConnected =false;
	String responseData;
	
	public NetworkPopup(Scene parent) {
        super(parent);
    }

    public void onInit() {
        LOG.print(this, "MainPopup Init");
    }

    public void onShow() {
        LOG.print(this, "MainPopup OnShow()");
    }

    public void onHide() {

    }

    public void onDestroy() {

    }

    public void onKeyDown(int keycode) {
    	
        LOG.print(this, "NetworkPopup KeyEvent : "+keycode);
        if(!isConnected) {
        	LOG.print(this, "RP Test");
//        	for(int i=0;i<100;++i) {
//        		RPConnector.request("10.119.160.232", 80, "http_test/sample.txt", RPConnector.RP_METHOD.RP_GET, null, response);
//        	}
          
          
          //Practical Example... 운영계 서버 접근으로 참조만...
          Map newHeader = new HashMap();
          newHeader.put("Content-Type", "application/x-www-form-urlencoded");
          RPConnector.setHeader(newHeader);
          Map payload = new HashMap();
          payload.put("dmcCd", "3348");
          payload.put("appCd", "30");
          payload.put("soCd", "99");
          payload.put("stbId", "1234567890");
          payload.put("chnCd", "10011009");
          payload.put("logId", "123456789020161026101752");
          payload.put("isoGb", "H960");
          payload.put("encYn", "1");
          payload.put("shop_id", "2015050781");
          payload.put("itemCd", "41288779");
          RPConnector.request("http://210.122.102.171", "cjtosplus/appRp/getProductDetail.jsp", RPConnector.RP_METHOD.RP_POST, payload, response);
            isConnected = true;
        } else close();
        

    }

    public void onPaint(Graphics g) {
        LOG.print(this, "MainPopup Paint");
        Drawer.setDimm(g, new Color(0,0,0,200));
        g.setColor(new Color(255,255,255));
        if(!isConnected) {
        	g.drawString("Press Any key to Connect with server", 100, 100);
            
        } else {
        	g.drawString("RP Request message Received", 100, 100);
        	if(responseData != null && responseData.length() != 0) {
        		LOG.print(this, "Paint.... Response Data :: "+responseData);
        		g.drawString(responseData, 100, 200);
        	}
        	
        }
        
    }

    public void timerWentOff() {
        //LOG.print(this, "timer");
    }

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}
}
