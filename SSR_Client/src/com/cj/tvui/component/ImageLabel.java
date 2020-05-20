package com.cj.tvui.component;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;


import com.cj.tvui.network.HttpConnect;
import com.cj.tvui.ui.Widget;
import com.cj.tvui.util.ImageUtil;

public class ImageLabel extends Widget{ // 간단한 이미지를 화면상에 표현(Network, Local image. 이미지 사이즈 확대 축소 혹은 원본 상태로 노출.)
	private int type;
	private boolean  isActivated = false;
	private ItemCompEvent evt = null;
	
	private Image img = null; // 사용할 이미지
	
	protected String imagePath = null;	 // 이미지의 경로
	protected boolean isUrlImage = false; // URL로 가지고 오는 이미지 여부
	private boolean isReSize = false; // 리사이징 여부
	
	public ImageLabel(String imagePath, int x, int y, boolean isUrlImage) {
		this(imagePath, x, y, 0, 0, isUrlImage);
		this.isReSize = false;
		this.isUrlImage = isUrlImage;
        if (imagePath == null || imagePath.trim().equals("")) {
            return;
        }
		setSize(this.getImg().getWidth(this),this.getImg().getHeight(this));
	}
	
	 public ImageLabel(String imagePath, int x, int y, int w, int h,boolean isUrlImage) {
		setBounds(x, y, w, h);
     	this.isReSize = true;
     	this.isUrlImage = isUrlImage;
     	if (imagePath == null || imagePath.trim().equals("")) {
            return;
        }
        setImagePath(imagePath);
        
       	imageLoad(imagePath);
    }
	
	public void onActivated() {
		// TODO Auto-generated method stub
		
	}

	public void onDeactivated() {
		// TODO Auto-generated method stub
		
	}

	public void okKeyPressed() {
		// TODO Auto-generated method stub
		
	}
	
   public void paint(Graphics g) {
        if (imagePath != null && !imagePath.equals("")) {
        	if(isReSize() == true) {
        		g.drawImage(this.getImg(), 0, 0, getWidth(), getHeight(), this);
        	} else {
        		g.drawImage(this.getImg(), 0, 0, this);
        	}
        }
    }
	
	 private void imageLoad(String path) {
	    	if(this.isUrlImage) {
	    		imageLoadURL(path);
	    	}else {
	    		imageLoadLocal(path);
	    	}
	    }
	
    private void imageLoadURL(String url) {
    	if(img == null) {
    		try{
//    			img =  ImageUtil.createImage(HttpConnect.ImgRequest("http://image.cjmall.com/goods_images/42/747/42427747OPK.jpg"));
    			img =  ImageUtil.createImage(HttpConnect.ImgRequest(url));
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    private void imageLoadLocal(String imgPath) {
		if(img == null) {
			try {
				Toolkit tool = Toolkit.getDefaultToolkit();
				
				img = tool.getImage(imgPath);
				MediaTracker tracker = new MediaTracker(this);
				tracker.addImage(img, 0);
				tracker.waitForID(0);
				tracker.removeImage(img,0);
				
				tool = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("img is not null.....====");
		}
    }
    
    private void imageFlush() {
    	if(img != null) {
    		img.flush();
    	}
    	img = null;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
	
	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}
	
	public boolean isReSize() {
        return isReSize;
    }

    public void setReSize(boolean isReSize) {
        this.isReSize = isReSize;
    }
    
    public void destroy() {
    	this.imageFlush();
        imagePath = null;
    }

}
