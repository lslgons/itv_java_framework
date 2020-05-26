package com.tcom.platform.dmc.cjhv;

import com.alticast.navsuite.service.OverlappedDialogHandler;


import com.tcom.platform.controller.KeyCode;
import com.tcom.platform.dmc.interfaces.KeymapInterface;
import com.tcom.util.LOG;
import org.ocap.ui.event.OCRcEvent;

import java.awt.event.KeyEvent;

/**
 * Created by daegon.kim on 2016-12-06.
 */
public class Keymap implements KeymapInterface {

    private OverlappedDialogHandler handler;
    UserKeyManager userKeyManager;
    public Keymap() {
        LOG.print("Load CJHV Keymap");
        userKeyManager=new UserKeyManager();
        handler = new OverlappedUIHandlerImpl();
    }


    public int keyMap(int keycode) {
        int mapping_code = 0;
        //Example...
        switch(keycode) {
            case OCRcEvent.VK_LAST :
                mapping_code = KeyCode.VK_BACK;
                break;
            case OCRcEvent.VK_EXIT:
                mapping_code = KeyCode.VK_EXIT;
                break;
            case KeyEvent.VK_F7:
                mapping_code = KeyCode.KEY_MODE_CHANGE;
                break;
            case KeyEvent.VK_F8:
                mapping_code = KeyCode.VK_DELETE;
                break;
            case KeyEvent.VK_F9:
                mapping_code = KeyCode.KEY_STAR;
                break;
            case KeyEvent.VK_F10:
                mapping_code = KeyCode.KEY_SHARP;
                break;
            default:
                mapping_code = keycode;
        }

        return mapping_code;
    }


	public Object getKeyListener() {
		return handler;
	}

	public Object getEventGroup() {
		return null;
	}

	public void setEnableNumKey(boolean enable) {
		if(enable) {
		    userKeyManager.reserveNumericKeys();
        } else {
		    userKeyManager.releaseNumericKeys();
        }
		
	}

	public void setEnableHotKey(boolean enable) {
		if(enable) {
		    userKeyManager.reserveHotKey();
        } else {
		    userKeyManager.releaseHotKey();
        }
		
	}

	public void setEnableBackKey(boolean enable) {
		if(enable) {
		    userKeyManager.reservePrevKey();
        } else {
		    userKeyManager.releasePrevKey();
        }
		
	}
	public void setEnableTrickKey(boolean enable) {
        if(enable) {
            userKeyManager.reserveTrickKeys();
        } else {
            userKeyManager.releaseTrickKeys();
        }
		
	}
    public void setEnableOkKey(boolean enable) {

    }

    public void setEnableArrowKey(boolean enable) {
        if(enable) {
            userKeyManager.reserveArrowKeys();
        } else {
            userKeyManager.releaseArrowKeys();
        }
    }

    public void destroy() {
        userKeyManager.destroy();
    }


}
