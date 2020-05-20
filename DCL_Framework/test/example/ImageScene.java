package example;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.cj.tvui.Keys;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.LOG;

public class ImageScene extends Scene {

	int count = 0;
	int offset = 10;
	String[] images = {
		"example/K-002.png",
		"http://image.cjmall.com/goods_images/42/934/42435934OPK.jpg",
		"http://image.cjmall.com/goods_images/42/747/42427747OPK.jpg",
		"http://image.cjmall.com/goods_images/38/530/38842530OPK.jpg",
		"http://image.cjmall.com/goods_images/42/296/42457296OPK.jpg",
		"http://image.cjmall.com/goods_images/42/958/42447958OPK.jpg",
		"http://image.cjmall.com/goods_images/42/251/42402251OPK.jpg",
		"http://image.cjmall.com/goods_images/42/715/42460715OPK.jpg",
		"http://image.cjmall.com/goods_images/42/362/42452362OPK.jpg",
		"http://image.cjmall.com/goods_images/40/054/40920054OPK.jpg",
		"http://image.cjmall.com/goods_images/40/520/40237520OPK.jpg",
		"http://image.cjmall.com/goods_images/40/863/40210863OPK.jpg",
		"http://image.cjmall.com/goods_images/39/159/39070159OPK.jpg",
		"http://image.cjmall.com/goods_images/35/486/35080486OPK.jpg"
	};
	
	public void onInit() {
		int width = getWidth();
	}

	public void onShow() {
		// TODO Auto-generated method stub

	}

	public void onHide() {
		// TODO Auto-generated method stub

	}

	public void onDestroy() {
		// TODO Auto-generated method stub

	}

	public void onKeyDown(int keycode) {
		// TODO Auto-generated method stub
		switch(keycode) {
		case Keys.VK_BACK:
			popScene();
			break;
		case Keys.VK_OK:
			count++;
			break;
		}

	}

	public void onPaint(Graphics g) {
		
		for (int i=0;i<count;++i) {
			if(i<images.length) {
				g.drawImage(loadImage(images[i], false), i*50, 300, 100, 100, this);
			}
		}
		g.drawString("Press OK to Increate Draw Image Count", 100, 100);
		g.drawString("Press Back to Close Scene", 100, 120);
		LOG.print(this, "Loaded Images : "+ String.valueOf(getLoadedImageCount()));

	}

	public void timerWentOff() {
		// TODO Auto-generated method stub

	}

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}

}
