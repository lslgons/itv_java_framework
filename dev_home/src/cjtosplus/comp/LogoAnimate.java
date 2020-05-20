package cjtosplus.comp;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import com.cj.tvui.network.HttpConnect;
import com.cj.tvui.util.ImageUtil;

public class LogoAnimate extends Component implements Runnable {
	
	private Thread thread = null;
	private int sleepTime = 5000*2;
	
	private Image[] logos; // 로고 이미지들.
	
	private String[] logoURL; // 로그의 URL 주소. (연동 필요)

	private int logoCnt=0; // 로고의 개수.

	private int logoIndx=0;// 로고의 index
	
	private int width = 0;
	private int height = 0;
	
	public LogoAnimate(int x, int y, int w, int h) {
		super();
		
		setBounds(x, y, w, h);
		// TODO Auto-generated constructor stub
	}
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
		width = w;
		height = h;
	}

	public void paint(Graphics g) {
		if(logos == null) return;
		try {
			g.drawImage(logos[logoIndx], 0, 0, width, height, this);
		} catch (Exception e) {
			System.out.println( "PAINT: " + e.getMessage() );
		}
	}
	
	public void destory() {
		if(this.thread != null) {
			thread = null;
		}
		 flushImage();
		 logoURL = null;
	}
	public void run() {
		// TODO Auto-generated method stub
		try{
			while(true) {
				Thread.yield();
				if(sleepTime <= 0) {
					break;
				}
				
				logoIndx++;

				if(logoIndx == logoCnt) {
					logoIndx =0;
				}
				
				repaint();
				System.out.println("("+logoIndx+") LogoAnimated Thread is Here... ");
				
				Thread.sleep(sleepTime);
			}
			
		}catch(InterruptedException e) {
			 System.out.println("intterpt trigger thread.");
		}
		
	}
	
	public void startLogo() {
		System.out.println("Start startLogo thread.");
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
	}
	
	public void stopLogo() {
		System.out.println("Stop stopLogo thread.");
		if (thread != null) {
			thread.interrupt();
		}
		thread = null;
	}
	
	public void setLogoImg() {
		logoIndx = 0;
		flushImage();
		
//		logoCnt = SettingDA.getInstance().getLogoDA().getTotalCount();
		this.setLogoCnt(2);
		
		if (this.logoCnt > 0) {
			logos = new Image[logoCnt];
			
			logoURL = new String[logoCnt];
			/////////Sample start////////////////////////////
			logoURL[0] = "http://210.122.102.109:10080/ktbmt2/images/logo_l1_960.png";
			logoURL[1] = "http://210.122.102.109:10080/ktbmt2/images/logo_l2_960.png";
			/////////Sample end////////////////////////////
			
			logoIndx = 0;
			
			for (int i = 0; i < logos.length; i++) {
				logos[i] = ImageUtil.createImage(HttpConnect.ImgRequest(logoURL[i]));
			}
			
		}
		
	}
	
	private void flushImage() {
		if (logos != null) {
			for (int i = 0; i < logos.length; i++) {
				if (logos[i] != null) {
					try {
						logos[i].flush();
					} catch (Exception e) {
					}
					logos[i] = null;
				}
			}
			logos = null;
		}
	}
	public int getLogoCnt() {
		return logoCnt;
	}
	public void setLogoCnt(int logoCnt) {
		this.logoCnt = logoCnt;
	}
	
	public String[] getLogoURL() {
		return logoURL;
	}
	public void setLogoURL(String[] logoURL) {
		this.logoURL = logoURL;
	}

}
