package com.cj.tvui.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.cj.tvui.Constants;
import com.cj.tvui.component.ImagePanel;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.network.RPConnector;
import com.cj.tvui.network.RPResponse;
import com.cj.tvui.util.GlobalImageManager;
import com.cj.tvui.util.ImagePool;
import com.cj.tvui.util.LOG;
import com.cj.tvui.util.RemoteClassLoader;

/**
 * 화면 구성 UI 클래스
 * <pre>
 *     public class SomeScene extends Scene {
 *
 *     }
 * </pre>
 *
 * @author daegon.kim (daegon.kim1@cj.net)
 * @since 2017.12.02
 */
public abstract class Scene extends Container implements Serializable {

    //TODO 팝업은 Scene당 하나의 팝업을 가질 수 있고 팝업 또한 팝업을 가지고 있게 됨, Scene을 날릴 경우 팝업 모두 제거됨
    //팝업은 Scene을 상속 받으며 pushScene과 popScene은 사용하지 못함
    Popup popup = null;
    
    //현재 사용되는 Scene이 Static으로 사용되는지 여부
    boolean isStatic = false;
    
	/**
	 * 이미지 리소스 : 각 Scene 혹은 Popup마다 ImagePool을 가지고 있는다.
	 */
    private ImagePool imgPool;
    /**
     * 로드 이미지 경로 리스트, 최대 로드 가능한 이미지 설정을 위함
     */
    private int maxLoadableImgCount = 20;
    
    /**
     * Widget을 상속받았을 경우 활성화된 Widget 변수
     */
    private Widget activatedWidget = null;

    /**
     * Background Pool
     */
    private HashMap bgPool = new HashMap();

    public Scene() {
        LOG.print("Scene Constructor");
        setBounds(0,0, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        imgPool = new ImagePool();
        imgPool.setComponent(this);
        onInit();
    }

    /**
     * 프레임워크에 의해 초기화되는 단계
     */
    public abstract void onInit();

    /**
     * 화면 전환 시 호출자에게 받은 데이터
     * @param args 전달받은 데이터 배열
     */
    public abstract void onDataReceived(Object[] args);

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
    public void onPaint(Graphics g) {

    };

    /**
     * 팝업이 닫힐경우의 이벤트
     * @param msg
     */
    public void onPopupClosed(String msg) {

    }

    /**
     * 1초 마다 호출
     */
    public abstract void timerWentOff();


    /**
     * 백그라운드 이미지 설정, 포커싱 및 인터랙션 없음
     * @param name 식별자
     * @param image 이미지객체
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void addBackgroundImage(String name, Image image, int x, int y, int w, int h) {
        ImagePanel imgPanel = new ImagePanel(image, x,y,w,h);
        this.add(imgPanel);
        bgPool.put(name, imgPanel);
    }

    /**
     * 백그라운드에 설정된 이미지를 가져옴
     * @param name 식별자
     * @return ImagePanel 객체
     * @see ImagePanel
     */
    public ImagePanel getBackgroundImage(String name) {
        return (ImagePanel) bgPool.get(name);
    }

    /**
     * SceneController에서 Static으로 사용되는 것을 알림, UI개발자 사용안함
     * @param b static scene 사용유무
     */

    public void setIsStatic(boolean b) {
    	this.isStatic = b;
    }

    /**
     * SceneController에서 사용, UI개발자 사용안함
     * @param g Graphics
     */
    public void repaintPopup(Graphics g) {
    	if(popup != null) {
    		popup.paint(g);
    	}
    }

    /**
     * SceneController에서 사용, UI개발자 사용안함
     * @param args
     */
    public void sendData(Object[] args) {
    	this.onDataReceived(args);
    }


    /**
     * SceneController에서 사용, UI개발자 사용안함
     * @param keycode
     */
    public void _onKeyDown(int keycode) {
        if(popup == null) {
            this.onKeyDown(keycode);
            if(activatedWidget != null) {
            	activatedWidget.onKeyDown(keycode);
            }
            
        } else {
            popup._onKeyDown(keycode);
        }
    }


    /**
     * SceneController에서 사용, UI개발자 사용안함
     */
    public void _timerWentOff() {
        if (popup != null) {
            popup._timerWentOff();
        }
        this.timerWentOff();

    }

    /**
     * SceneController에서 사용, UI개발자 사용안함
     */
    public void _onDestroy() {
        LOG.print("Scene _onDestroy...");
    	imgPool.flush();
    	bgPool.clear();
    	bgPool=null;
    	imgPool=null;
    	if(popup != null) {
    	    popup._onDestroy();
    	    popup=null;
        }
    	this.onDestroy();
    }

    /**
     * 현재 Scene에서 다른 Scene으로 넘어감
     * @param classPath 클래스 절대경로(String)
     * @param args 넘어가는 Scene으로 전달할 데이터
     */
    protected final void pushScene(final String classPath, Object[] args) {
        if (this instanceof Popup) {
            throw new RuntimeException("Can't push Scene on Popup Instance!!");
        } else {
            SceneController.getInstance().pushScene(classPath, args);
        }

    }

    /**
     * 현재 Scene을 종료 하고 이전 Scene으로 이동
     */
    protected final void popScene() {
        if (this instanceof Popup) {
            throw new RuntimeException("Can't pop Scene on Popup Instance!!");
        } else {
            SceneController.getInstance().popScene();
        }

    }

    /**
     * 팝업 오픈
     * @param classPath 팝업클래스 절대경로(String)
     * @param args 팝업으로 전달할 데이터
     */
    public final void openPopup(final String classPath, Object[] args) {
        if (popup == null) {
            popup = (Popup)RemoteClassLoader.newInstance(classPath, new Class[]{Scene.class}, new Object[]{this});
            //popup.onInit();
            if(popup == null) {
                //RemoteClass로 가져오지 못한 경우 처리중지
                LOG.print("Can't find Popup class");
                return;
            }
            this.add(popup);
            popup.onDataReceived(args);
            popup.onShow();
            popup.setVisible(true);
            //popup.repaint();
            this.repaint();
        } else {
            //throw new RuntimeException("Already Exist Popup...");
        	LOG.print(this, "Already Popup opened");
        }


    }

    /**
     * 현재 오픈된 팝업을 종료 함, 팝업이 없을경우 무시
     */
    public final void closePopup(String msg) {
        if (popup != null) {
            popup.setVisible(false);
            this.remove(popup);
            popup.onHide();
            popup.onDestroy();
            this.onPopupClosed(msg);
            popup = null;
        }

    }

    public final boolean isPopupActivated() {
        if(popup != null) return true;
        else return false;
    }

    public final void closePopup() {
        closePopup(null);
    }
    
    //Static Scene Control

    /**
     * StaticScene영역에 위치 할 Scene을 설정 한다.
     * @param classPath Scene 절대경로
     * @param args Scene에 전달할 데이터
     */
    protected final void setStaticScene(final String classPath, Object[] args) {
    	SceneController.getInstance().setStaticScene(classPath, args);
    }

    /**
     * 설정된 StaticScene이 보여지도록 함
     */
    protected final void showStaticScene() {
    	SceneController.getInstance().showStaticScene(true);
    }

    /**
     * 설정된 StaticScene을 숨김
     */
    protected final void hideStaticScene() {
    	SceneController.getInstance().showStaticScene(false);
    }

    /**
     * StaticScene이 설정되어 있는지 확인
     * @return true면 설정된 StaticScene이 존재함
     */
    protected final boolean isSetStaticScene() {
    	return SceneController.getInstance().isSetStaticScene();
    }

    /**
     * StaticScene이 지금 보여지고 있는지 확인
     * @return true면 StaticScene이 보여지고 있음
     */
    protected final boolean isShowStaticScene() {
    	return SceneController.getInstance().isShowStaticScene();
    }
    
    /**
     * Byte정보로부터 이미지 가져오기
     * @param data 이미지 byte array
     * @return Image객체
     */
    final protected Image loadImage(final byte[] data) {
//    	if(imgPool.getLoadedImageCount() >= maxLoadableImgCount) {
//    		LOG.print(this, "Exceed max loadable image count");
//    		Set key = imgPool.getImagePath();
//    		Iterator keyIT = key.iterator();
//    			imgPool.flushImage((String)keyIT.next());
//    	}
    	Image img = imgPool.loadImage(data);
    	
		return img;
    	
    }


    /**
     * 전역 이미지 로드
     * @param name 파일명
     * @return Image
     */

    final protected Image loadGlobalImage(final String name, boolean isImageHost) {
        return GlobalImageManager.getInstance().loadImage(name);
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
     * @param name 파일명
     * @param isImageHost true면 설정된 ImageHost를 URL로 설정, false면 DCL호스트 주소에서 가져옴
     * @return
     */
    final protected Image loadImage(final String name, boolean isImageHost) {
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
    		if (isImageHost) {
    			LOG.print(this, Constants.IMG_HOST+"res/"+name);
            	img = imgPool.getImage(Constants.IMG_HOST+name);
    		} else {
    			//LOG.print(this, Constants.DCL_HOST+name);
            	img = imgPool.getImage(Constants.DCL_HOST+name);
    		}
    		
        	if(img == null) {

    		    //2018-08-20, 서버 이미지 미존재시 로컬에서 가져옴
                LOG.print(this, "Local Image Load!!!!");
                File resFolder = new File("res");

                if(resFolder.exists()) {
                    LOG.print(this, "Res Filder Exists!!!!");
                    img = imgPool.getImage("res"+File.separator+name);
                } else {
                    img = imgPool.getImage(name);
                }


//        		//2.Load Image from local - 그냥 HTTP로 땡겨옴
//                if(!name.equals(" .png")) {
//                    LOG.print(this, Constants.IMG_HOST+"res/"+name);
//                    img = imgPool.getImage(Constants.IMG_HOST+"res/"+name);
//                } else {
//                    LOG.print(this, "Local Image Load!!!!");
//                    File resFolder = new File("res");
//
//                    if(resFolder.exists()) {
//                        LOG.print(this, "Res Filder Exists!!!!");
//                        img = imgPool.getImage("res"+File.separator+name);
//                    } else {
//                        img = imgPool.getImage(name);
//                    }
//                }


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
        
//      Component[] comps = this.getComponents();
//      for(int i=0; i<comps.length;++i) {
//      	comps[i].paint(g);
//      }
    	this.onPaint(g);
    	super.paint(g);
		if(popup != null) {
		    popup.paint(g);
		}
		//StaticScene상태에서 CurrentScene이 Popup이 뜬 상태이면 Popup위로 StaticScene이 올라오는 현상
		if(isStatic) {
			//LOG.print("Popup Repaint");
		    Scene curScene = SceneController.getInstance().getCurrentScene();
		    curScene.repaintPopup(g);
		}
		    
	}


    /**
     * 특정 Widget을 활성화(포커싱)
     * @param widget 활성화하기 위한 Widget
     * @see Widget
     */
    public void setWidgetActivate(Widget widget) {
    	if(this.activatedWidget != null) {
    		this.activatedWidget.deactivate();
    	}
    	this.activatedWidget = widget;
    	this.activatedWidget.activate();
    }

    /**
     * RPConnect Inner Implement...
     *
     */
    protected void requestConnection(final int trId, String uri, String method, Map payload) {
        try {
            RPConnector.request(uri, method, payload, new RPResponse() {
                public void onReceived(int code, Map respHeader, Object response) {
                    onRequestSuccess(trId, respHeader, response);
                }

                public void onFailed(int code, Object response) {
                    onRequestFailed(trId, code, response);
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected void onRequestSuccess(int trId, Map resHeader, Object response) {

    }
    protected void onRequestFailed(int trId, int errCode, Object result) {

    }

}
