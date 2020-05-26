package com.tcom.platform.controller;

import com.tcom.platform.dmc.interfaces.KeymapInterface;
import com.tcom.platform.dmc.interfaces.VODInterface;
import com.tcom.util.LOG;
import com.tcom.util.RemoteClassLoader;

import java.awt.event.KeyEvent;

/**
 * 키 이벤트 설정관련 컨트롤러
 * @author daegon.kim (daegon.kim1@cj.net)
 * @since 2016-12-05
 */
public final class KeyController{

    private static KeyController instance = null;

    /**
     * 단일 객체로 가져옴
     * @return KeyController
     */
    public static KeyController getInstance() {
        if(instance == null) instance = new KeyController();
        return instance;
    }

    private KeyController(){
        LOG.print("Init KeyController");
        keymapIntf = RemoteClassLoader.loadKeymapInterface();
        keymapIntf.setEnableBackKey(isEnableBackKey);
        keymapIntf.setEnableNumKey(isEnableNumKey);
        keymapIntf.setEnableHotKey(isEnableHotKey);
        keymapIntf.setEnableTrickKey(isEnableTrickKey);
        keymapIntf.setEnableOkKey(isEnableOkKey);
        keymapIntf.setEnableArrowKey(isEnableArrowKey);
    }

    public void shutdown() {
        keymapIntf.destroy();
        keymapIntf=null;
        instance=null;
        keyProcessListener=null;
    }

    private KeymapInterface keymapIntf;
    private boolean isEnableBackKey = false;
    private boolean isEnableNumKey = true;
    private boolean isEnableHotKey = true;
    private boolean isEnableTrickKey = false;
    private boolean isEnableOkKey = true;
    private boolean isEnableArrowKey = true;

    public interface KeyProcessListener {
        void onKeyDown(int keyCode);
    }

    KeyProcessListener keyProcessListener = null;

    public void setKeyProcessListener(KeyProcessListener keyProcessListener) {
        this.keyProcessListener=keyProcessListener;
    }


    /**
     * 대상 단말의 키이벤트를 UI로 맵핑하여 이벤트 발생, UI개발자는 사용안함
     * @param e KeyEvent
     * @return true면 사용, false면 미사용되어 키 이벤트 bypass
     */
    public boolean keyProcess(KeyEvent e) {
    	return this.keyProcess(e.getKeyCode());
    }





    /**
     * 대상 단말의 키이벤트를 UI로 맵핑하여 이벤트 발생, UI개발자는 사용안함
     * @param keycode 이벤트 발생 키코드
     * @return true면 사용, false면 미사용되어 키 이벤트 bypass
     */
    public boolean keyProcess(int keycode) {
    	LOG.print(this, "Key Process : "+keycode);
    	int parsedKeyCode = keymapIntf.keyMap(keycode);


    	if(!isEnableOkKey) {
    	    if(parsedKeyCode == KeyCode.VK_OK) {
    	        return false;
            }
        }

        if(!isEnableArrowKey) {
    	    switch(parsedKeyCode) {
                case KeyCode.VK_LEFT:
                case KeyCode.VK_RIGHT:
                case KeyCode.VK_UP:
                case KeyCode.VK_DOWN:
                    return false;
                default:
                    break;
            }
        }

    	if(!isEnableBackKey && parsedKeyCode == KeyCode.VK_BACK) {
    		return false;
    	}
    	if(!isEnableNumKey) {
    		switch(parsedKeyCode) {
    		case KeyCode.VK_0:
    		case KeyCode.VK_1:
    		case KeyCode.VK_2:
    		case KeyCode.VK_3:
    		case KeyCode.VK_4:
    		case KeyCode.VK_5:
    		case KeyCode.VK_6:
    		case KeyCode.VK_7:
    		case KeyCode.VK_8:
    		case KeyCode.VK_9:
    			return false;
    		default:
    			break;
    		}
    	}
    	if(!isEnableHotKey) {
    		switch(parsedKeyCode) {
    		case KeyCode.VK_R:
    		case KeyCode.VK_B:
    		case KeyCode.VK_G:
    		case KeyCode.VK_Y:
    			return false;
			default:
				break;
    		}
    	}

    	//VOD 재생 시 미디어 플레이어 제어
    	if(MediaController.getInstance().isVODPlaying) {
    		((VODInterface) MediaController.getInstance().avInterface).vodKeyHandler(parsedKeyCode);
    	}
    	if(keyProcessListener != null) keyProcessListener.onKeyDown(parsedKeyCode);
        return true;
    }

    /**
     * 이전키 활성화 설정
     * @param b true면 활성화
     */
    public void setEnableBackKey(boolean b) {
        if(this.keymapIntf != null) {
            this.keymapIntf.setEnableBackKey(b);
            this.isEnableBackKey = b;
        }

    }

    /**
     * 숫자키 활성화 설정
     * @param b true면 활성화
     */
    public void setEnableNumKey(boolean b) {
        if(this.keymapIntf != null) {
            this.keymapIntf.setEnableNumKey(b);
            this.isEnableNumKey = b;
        }

    }

    /**
     * 단축키 활성화 설정
     * @param b true면 활성화
     */
    public void setEnableHotKey(boolean b) {
        if(this.keymapIntf != null) {
            this.keymapIntf.setEnableHotKey(b);
            this.isEnableHotKey = b;
        }

    }

    /**
     * 미디어 재생 제어키 활성화 설정
     * @param b true면 활성화
     */
    public void setEnableTrickKey(boolean b) {
        if(this.keymapIntf != null) {
            this.keymapIntf.setEnableTrickKey(b);
            this.isEnableTrickKey = b;
        }

    }

    /**
     * 확인키 활성화 설정
     * @param b
     */
    public void setEnableOkKey(boolean b) {
        if(this.keymapIntf != null) {
            this.keymapIntf.setEnableOkKey(b);
            this.isEnableOkKey = b;
        }
    }


    /**
     * 방향키 활성화 설정
     * @param b
     */
    public void setEnableArrowKey(boolean b) {
        if(this.keymapIntf != null) {
            this.keymapIntf.setEnableArrowKey(b);
            this.isEnableArrowKey = b;
        }
    }


    /**
     * 이전키 활성화 여부
     * @return true면 활성화
     */
    public boolean isEnableBackKey() {
		return isEnableBackKey;
	}

    /**
     * 숫자키 활성화 여부
     * @return true면 활성화
     */
	public boolean isEnableNumKey() {
		return isEnableNumKey;
	}

    /**
     * 단축키 활서화 여부
     * @return true면 활성화
     */
	public boolean isEnableHotKey() {
		return isEnableHotKey;
	}

    /**
     * 미디어 제어키 활성화 여부
     * @return true면 활성화
     */
	public boolean isEnableTrickKey() {
		return isEnableTrickKey;
	}

    /**
     * 확인키 활성화 여부
     * @return true면 활성화
     */
	public boolean isEnableOkKey() {
	    return isEnableOkKey;
    }

    /**
     * 방향키 활성화 여부
     * @return true면 활성화
     */
    public boolean isEnableArrowKey() {
	    return isEnableArrowKey;
    }

    /**
     * 해당 플랫폼 키 인터페이스 객체 반환
     * @return 인터페이스 객체
     * @see KeymapInterface
     */
	public KeymapInterface getKeymap() {return this.keymapIntf;}
}
