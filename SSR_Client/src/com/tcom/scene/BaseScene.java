package com.tcom.scene;

import com.tcom.ssr.SSRConfig;
import com.tcom.util.ImagePool;
import com.tcom.util.LOG;


import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 화면 구성 UI 클래스
 * <pre>
 *     public class SomeScene extends BaseScene {
 *
 *     }
 * </pre>
 *
 * @author daegon.kim (daegon.kim1@cj.net)
 * @since 2020.05.22
 */
public abstract class BaseScene extends Container implements Serializable {

    /**
	 * 이미지 리소스 : 각 Scene 혹은 Popup마다 ImagePool을 가지고 있는다.
	 */
    private ImagePool imgPool;
    /**
     * 로드 이미지 경로 리스트, 최대 로드 가능한 이미지 설정을 위함
     */
    private int maxLoadableImgCount = 20;

    private Timer scheduleTimer=new Timer();

    public BaseScene() {
        LOG.print("Scene Constructor");
        setBounds(0,0, SSRConfig.getInstance().SCENE_WIDTH, SSRConfig.getInstance().SCENE_HEIGHT);
        imgPool = new ImagePool();
        imgPool.setComponent(this);
        scheduleTimer.schedule(new TimerTask() {
            public void run() {
                BaseScene.this.timerWentOff();
            }
        }, 1000L, 1000L);
        onInit();
    }

    /**
     * 프레임워크에 의해 초기화되는 단계
     */
    public abstract void onInit();


    /**
     * 화면이 노출되는 단계
     */
    public abstract void onShow();

    /**
     * 화면이 비활성화되는 단계
     */
    public abstract void onHide();

    /**
     * 인스턴스 정리
     */
    public abstract void onDestroy();

    /**
     * 리모컨 키 입력 수신
     * @param keycode 입력받은 키코드
     */
    public abstract void onKeyDown(int keycode);
    /**
     * paint(g) Wrapper 함수
     **/
    public abstract void onPaint(Graphics g);

    /**
     * 1초 마다 호출
     */
    public abstract void timerWentOff();


    /**
     * SceneController에서 사용, UI개발자 사용안함
     */
    public void _onDestroy() {
        LOG.print("Scene _onDestroy...");
    	imgPool.flush();
    	imgPool=null;
    	scheduleTimer.cancel();
    	this.onDestroy();
    }



    /**
     * Byte정보로부터 이미지 가져오기
     * @param data 이미지 byte array
     * @return Image객체
     */
    final protected Image loadImage(final byte[] data) {
    	if(imgPool.getLoadedImageCount() >= maxLoadableImgCount) {
    		LOG.print(this, "Exceed max loadable image count");
    		Set key = imgPool.getImagePath();
    		Iterator keyIT = key.iterator();
    		imgPool.flushImage((String)keyIT.next());
    	}
    	return imgPool.loadImage(data);
    	
    }


    final protected Image loadLocalImage(final String name) {
        Image img;
        LOG.print(this, "Local Image Load!!!!");
        File resFolder = new File("res");

        if(resFolder.exists()) {
            LOG.print(this, "Res Filder Exists!!!!");
            img = imgPool.getImage("res"+File.separator+name);
        } else {
            img = imgPool.getImage(name);
        }

        return img;
    }

    /**
     * 파일명으로 부터 이미지를 로드 함
     * @param name 파일명 or URL
     * @return
     */
    final protected Image loadImage(final String name) {
    	if(imgPool.getLoadedImageCount() >= maxLoadableImgCount) {
    		LOG.print(this, "Exceed max loadable image count");
    		Set key = imgPool.getImagePath();
    		Iterator keyIT = key.iterator();
    			imgPool.flushImage((String)keyIT.next());
    	}
    	Image img;
    	//1.Load Image from server
    	if (name.startsWith("http://") || name.startsWith("https://")) {
    		img = imgPool.getImage(name);
    		
    	} else {

            //2018-08-20, 서버 이미지 미존재시 로컬에서 가져옴
            LOG.print(this, "Local Image Load!!!!");
            File resFolder = new File("res");

            if(resFolder.exists()) {
                LOG.print(this, "Res Filder Exists!!!!");
                img = imgPool.getImage("res"+File.separator+name);
            } else {
                img = imgPool.getImage(name);
            }

        	ImageObserver imgObs = new ImageObserver() {	
    			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
    				return false;
    			}
    		};
    		try {
                if(img.getWidth(imgObs) == -1 && img.getHeight(imgObs) == -1) {
                    //3.Exception, Can't load image
                    //throw new RuntimeException("Image not found");
                    LOG.print(this, "Image not found... : " + name);
                } else {
                    LOG.print(this, "Image Load Complete : "+ name);
                }
            } catch(NullPointerException e) {
                LOG.print(this, "Image not found... : " + name);
            }



    	}
    	
    	
    	return img;
    }

    /**
     * 로드된 이미지 객체 삭제
     * @param name 파일명
     */
    final protected void unloadImage(String name) {
    	imgPool.flushImage(name);
    }

    final protected void unloadImage(Image img) {
        imgPool.flushImage(img);
    }

    /**
     * 현재 로드된 이미지 갯수 반환
     * @return 로드된 이미지 갯수
     */
    final protected int getLoadedImageCount() {
    	return imgPool.getLoadedImageCount();
    }

    /**
     * 캐시에 저장할 수 있는 최대 이미지 갯수 설정
     * @param maxCount 최대 이미지 캐시 갯수
     */
    final protected void setMaxLoadableImage(int maxCount) {
    	this.maxLoadableImgCount = maxCount;
    	if(imgPool.getLoadedImageCount() > maxCount) {
    		//최대 로드 가능 이미지 초과분 삭제
    		Set key = imgPool.getImagePath();
    		Iterator keyIT = key.iterator();
    		while(imgPool.getLoadedImageCount() == maxCount) {
    			imgPool.flushImage((String)keyIT.next());
    		}
    	}
    }
    
    
    
    /**
     * Overrided Method, UI개발자 사용안함
     */
    public void paint(Graphics g) {
    	this.onPaint(g);
    	super.paint(g);
	}

}
