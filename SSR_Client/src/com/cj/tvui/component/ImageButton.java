package com.cj.tvui.component;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

import com.cj.tvui.network.HttpConnect;
import com.cj.tvui.ui.Widget;
import com.cj.tvui.util.ImageUtil;

public class ImageButton extends Widget{ // 노멀과 포커스가 이미지로 이루어진 버튼 처리.
	private int type;
	private boolean  isActivated = false; // Nor , Focus 여부
	private ItemCompEvent evt = null; // ok Event 처리.
	
	private Image norImg = null; // 사용할 노멀 이미지.
	private Image focImg = null; // 사용할 포커스 이미지
	
	protected String norImagePath = null;	 // 노멀 이미지의 경로
	protected String focImagePath = null;    // 포커스 이미지의 경로
	protected boolean isUrlImage = false; // URL로 가지고 오는 이미지 여부
	private boolean isReSize = false; // 리사이징 여부

	public ImageButton(String norImagePath, String focImagePath, int x, int y, boolean isUrlImage,  ItemCompEvent e) {
		// TODO Auto-generated constructor stub
		this(norImagePath, focImagePath, x, y, 0, 0, isUrlImage, e);
		this.isReSize = false;
		this.isUrlImage = isUrlImage;
		
		if (norImagePath == null || norImagePath.trim().equals("") || focImagePath == null
				|| focImagePath.trim().equals("")) {
			return;
		}
			setSize(this.getNorImg().getWidth(this),this.getNorImg().getHeight(this));
		
	}
	
	public ImageButton(String norImagePath, String focImagePath, int x, int y, int w, int h, boolean isUrlImage,  ItemCompEvent e) {
		// TODO Auto-generated constructor stub
		setBounds(x, y, w, h);
     	this.isReSize = true;
     	this.isUrlImage = isUrlImage;
     	if (norImagePath == null || norImagePath.trim().equals("") || focImagePath == null
				|| focImagePath.trim().equals("")) {
			return;
		}
     	
     	setNorImagePath(norImagePath); 
     	setFocImagePath(focImagePath);
         
        imageLoad(norImagePath,focImagePath );
        
        this.evt = e;
     	
	}

	public void onActivated() {
		// TODO Auto-generated method stub
		this.isActivated = true;
		
	}

	public void onDeactivated() {
		// TODO Auto-generated method stub
		this.isActivated = false;
		
	}

	public void okKeyPressed() {
		// TODO Auto-generated method stub
		evt.doAction();
	}
	
	 public void paint(Graphics g) {
	        if (norImagePath != null && !norImagePath.equals("") && focImagePath != null && !focImagePath.equals("")) {
	        	if(isReSize() == true) {
	        		if(this.isActivated) { // 포커스
	        			g.drawImage(this.getNorImg(), 0, 0, getWidth(), getHeight(), this);
	        			g.drawImage(this.getFocImg(), 0, 0, getWidth(), getHeight(), this);
	        		}else { // 노멀
	        			g.drawImage(this.getNorImg(), 0, 0, getWidth(), getHeight(), this);
	        		}
	        		
	        	} else {
	        		if(this.isActivated) { // 포커스
	        			g.drawImage(this.getNorImg(), 0, 0, this);
	        			g.drawImage(this.getFocImg(), 0, 0, this);
	        		}else { // 노멀
	        			g.drawImage(this.getNorImg(), 0, 0, this);
	        		}
	        	}
	        }
	    }
	
	private void imageLoad(String norImagePath, String focImagePath) {
    	if(this.isUrlImage) {
    		imageLoadURL(norImagePath, focImagePath);
    	}else {
    		imageLoadLocal(norImagePath, focImagePath);
    	}
    }
	
	  private void imageLoadURL(String norUrl, String focUrl) {
	    	if(norImg == null && focImg == null) {
	    		try{
	    			norImg =  ImageUtil.createImage(HttpConnect.ImgRequest(norUrl));
	    			focImg =  ImageUtil.createImage(HttpConnect.ImgRequest(focUrl));
	    		}catch(Exception e) {
	    			e.printStackTrace();
	    		}
	    	}
	    }
	  
	  private void imageLoadLocal(String norUrl, String focUrl) {
			if(norImg == null && focImg == null) {
				try {
					Toolkit tool = Toolkit.getDefaultToolkit();
					
					norImg = tool.getImage(norUrl);
					MediaTracker tracker = new MediaTracker(this);
					tracker.addImage(norImg, 0);
					tracker.waitForID(0);
					tracker.removeImage(norImg,0);
					
					tool = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					Toolkit tool = Toolkit.getDefaultToolkit();
					
					focImg = tool.getImage(focUrl);
					MediaTracker tracker = new MediaTracker(this);
					tracker.addImage(focImg, 0);
					tracker.waitForID(0);
					tracker.removeImage(focImg,0);
					
					tool = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}else {
				System.out.println("img is not null.....====");
			}
	    }

	  private void imageFlush() {
	    	if(norImg != null) {
	    		norImg.flush();
	    	}
	    	norImg = null;
	    	
	    	if(focImg != null) {
	    		focImg.flush();
	    	}
	    	focImg = null;
	    }
	/**
	 * @return the norImg
	 */
	public Image getNorImg() {
		return norImg;
	}

	/**
	 * @param norImg the norImg to set
	 */
	public void setNorImg(Image norImg) {
		this.norImg = norImg;
	}
	

	/**
	 * @return the focImg
	 */
	public Image getFocImg() {
		return focImg;
	}

	/**
	 * @param focImg the focImg to set
	 */
	public void setFocImg(Image focImg) {
		this.focImg = focImg;
	}

	/**
	 * @return the norImagePath
	 */
	public String getNorImagePath() {
		return norImagePath;
	}

	/**
	 * @param norImagePath the norImagePath to set
	 */
	public void setNorImagePath(String norImagePath) {
		this.norImagePath = norImagePath;
	}

	/**
	 * @return the focImagePath
	 */
	public String getFocImagePath() {
		return focImagePath;
	}

	/**
	 * @param focImagePath the focImagePath to set
	 */
	public void setFocImagePath(String focImagePath) {
		this.focImagePath = focImagePath;
	}
	
	
	
	/**
	 * @return the isReSize
	 */
	public boolean isReSize() {
		return isReSize;
	}

	/**
	 * @param isReSize the isReSize to set
	 */
	public void setReSize(boolean isReSize) {
		this.isReSize = isReSize;
	}

	public void destroy() {
		this.imageFlush();
		norImagePath = null;
		focImagePath = null;
	}

}
