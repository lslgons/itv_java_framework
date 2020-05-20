package cjtcom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.network.HttpConnect;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.ImageUtil;
import com.cj.tvui.util.LOG;

public class SampleMainScene extends Scene {
	private Image sampleSea = null;
	private Image sampleMount = null;
	private int naviIndex = 0;
	
	private String receiveString = "상담원들의 말";
	private boolean isSoketOpen = false;

    public void onInit() {
        LOG.print(this, "Sample Main Scene Init");
        SceneController.getInstance().setStaticScene("cjtcom.SampleStaticScene", null);
    }

    public void onShow() {
        LOG.print(this, "onShow");
        imageLoad();
    }

    public void onHide() {
        LOG.print(this, "onHide");
        imageFlush();
    }

    public void onDestroy() {
        LOG.print(this, "onDestroy");
        imageFlush();

    }

    public void onKeyDown(int keycode) {
        LOG.print(this, "Key Down :: " + keycode);
		if (keycode == Keys.VK_OK) {
			if (naviIndex == 0) {
				// 엔터 입력 시 SubScene으로 전환
//				pushScene("cjtcom.SubScene", null);
				pushScene("cjtcom.SampleSubScene", null);
			} else if (naviIndex == 1) {
				openPopup("cjtcom.popup.SampleMainPopup", null);
			}
		} else if (keycode == Keys.VK_RIGHT) {
        	this.naviIndex++;
        	if(this.naviIndex > 1) {
        		naviIndex = 0;
        	}
        	repaint();
        }else if(keycode == Keys.VK_LEFT) {
        	this.naviIndex--;
        	if(this.naviIndex == -1) {
        		naviIndex = 1;
        	}
        	repaint();
        }else if(keycode == Keys.VK_1) {
        	System.out.println("SocketReceive is OK-===");
        	isSoketOpen = true;
        	
        } else if(keycode == Keys.VK_2) {
        	System.out.println("SocketReceive is Close-==="); // 실제로 끊기는 것은 Client가 종료되면 끊어진다.
        	isSoketOpen = false;
        	
        }
    }

    public void onPaint(Graphics g) {
        LOG.print(this, "MainScene Paint");
        g.setFont(new Font("Korean iTV SanSerifD", Font.PLAIN, 17));
        
        g.setColor(Color.BLUE);
        g.fillRect(100+20,450-30,740,100);
        
        g.setColor(Color.WHITE);
        g.drawString("Main Scene Layer", 130, 400+50+30 -30-10);
        
        ///////button/////////////////
        g.setColor(Color.GREEN);
        g.fillRect(40,210,90,40);
        g.fillRect(40+100,210,90,40);
        
        g.setColor(Color.BLACK);
        g.drawString("Button 1", 40+5,210 + 25);
        g.drawString("Button 2", 40+100+5,210 +25);
        ///////////////////////////////
        
		if (naviIndex == 0) {
			g.drawImage(sampleSea, 0, 0, 268, 197, this);
			 //////focus//////////////////
	        g.setColor(Color.BLACK);
	        g.drawRect(40,210,90,40);
	        /////////////////////////////
		} else {
			g.drawImage(sampleMount, 0 , 0, 268, 197, this);
    		 //////focus//////////////////
	        g.setColor(Color.BLACK);
	        g.drawRect(40+100,210,90,40);
	        /////////////////////////////
		}
		
		//////Text Description//////////////////////////////////////
		g.setColor(Color.BLACK);
		g.fillRect(320, 10, 450, 200);

		g.setColor(Color.WHITE);
		g.drawString("1. Button 1 Press: To the SampleSubScene", 320+10, 30);
		g.drawString("2. Button 2 Press: Popup Scene is Visible", 320+10, 30+30);
		g.drawString("3. Num 1 Press: Socket Open and Ready..", 320+10, 30+60);
		/////////////////////////////////////////////////////////////
		
		if(isSoketOpen) {
			g.drawString("Text Here:",320+10,  30+30+30+30);
			g.drawString(receiveString, 320+10+20+60,  30+30+30+30);
		}

    }

    public void timerWentOff() {
        //LOG.print(this, "timer");
    }
    
    private void imageLoad() {
    	if(sampleSea == null) {
			try {
				sampleSea = ImageUtil.createImage(HttpConnect.ImgRequest("http://10.119.160.232//images/sea.png"));
			} catch (Exception e) {
				e.printStackTrace();
				Toolkit tool = Toolkit.getDefaultToolkit(); 
				sampleSea = tool.getImage("img/sea.png"); 
				tool = null;
//				try {
//					sampleSea = ImageIO.read(new File("img/sea.png")); // Local data // STB에서 실행 불가.
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
			}
			
		}
		if (sampleMount == null) {
			try {
				sampleMount = ImageUtil.createImage(HttpConnect.ImgRequest("http://10.119.160.232//images/mount.png"));
			} catch (Exception e) {
				e.printStackTrace();
				Toolkit tool = Toolkit.getDefaultToolkit(); 
				sampleMount = tool.getImage("img/mount.png");  // Local data
				tool = null;
//				try {
//					sampleMount = ImageIO.read(new File("img/mount.png")); // Local data //STB에서 실행 불가.
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
			}
			
		}
    }
    
    private void imageFlush() {
    	if(sampleSea != null) {
    		sampleSea.flush();
    	}
    	sampleSea = null;
    	
    	if(sampleMount != null) {
    		sampleMount.flush();
    	}
    	sampleMount = null;
    }

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}
    
}
