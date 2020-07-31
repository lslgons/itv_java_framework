package com.tcom.platform.dmc.cjhv;

import com.alticast.navsuite.service.OverlappedDialogHandler;


import com.tcom.platform.controller.KeyCode;
import com.tcom.platform.dmc.interfaces.KeymapInterface;
import com.tcom.util.LOG;
import org.havi.ui.event.HRcEvent;
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
        LOG.print("Keymap : "+keycode);
        switch(keycode) {
            case HRcEvent.VK_ENTER :
                mapping_code = KeyCode.VK_OK;
                break;
            case HRcEvent.VK_INFO:
                mapping_code = KeyCode.VK_INFO;
                break;
            case 403:
                mapping_code = KeyCode.VK_R;
                break;
            case 405:
                mapping_code = KeyCode.VK_G;
                break;
            case 404:
                mapping_code = KeyCode.VK_Y;
                break;
            case 406:
                mapping_code = KeyCode.VK_B;
                break;
            case 607:
                mapping_code = KeyCode.VK_BACK;
                break;
            default:
                mapping_code = keycode;
        }
        LOG.print("Return to mapping code :: "+mapping_code);
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
