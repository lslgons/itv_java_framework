package com.tcom.platform.dmc.kt;

import java.awt.event.KeyListener;

import com.tcom.platform.controller.KeyCode;
import com.tcom.platform.dmc.interfaces.KeymapInterface;
import com.tcom.ssr.SSRConfig;
import com.tcom.util.LOG;
import com.tcom.util.RemoteClassLoader;
import org.havi.ui.HScene;
import org.havi.ui.event.HEventGroup;
import org.havi.ui.event.HRcEvent;



/**
 * Created by daegon.kim on 2016-12-06.
 */
public class Keymap implements KeymapInterface {

	private KeyListener keyListener;
	private HEventGroup eventGroup;
    public Keymap() {
        LOG.print("Load KT Keymap");
        keyListener = new KeyListenerImpl();
        eventGroup = new HEventGroup();
        //Default Setting
        eventGroup.addAllArrowKeys();
        eventGroup.addAllColourKeys();
        eventGroup.addAllNumericKeys();
        eventGroup.addKey(HRcEvent.VK_ENTER);
        //eventGroup.addKey(HRcEvent.VK_F4);
    }

    public int keyMap(int keycode) {
        int mapping_code = 0;
        //Example...
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
            case 404:
                mapping_code = KeyCode.VK_G;
                break;
            case 405:
                mapping_code = KeyCode.VK_Y;
                break;
            case 406:
                mapping_code = KeyCode.VK_B;
                break;
            case 428:
                mapping_code = KeyCode.VK_BACK;
                break;
            default:
                mapping_code = keycode;
        }
        LOG.print("Return to "+mapping_code);
        return mapping_code;
    }

	public Object getKeyListener() {
		return this.keyListener;
	}

	public Object getEventGroup() {
		return this.eventGroup;
	}

	public void setEnableNumKey(boolean enable) {
		if(enable) {
			eventGroup.addAllNumericKeys();
		} else {
			eventGroup.removeAllNumericKeys();
		}
		HScene scene = (HScene) SSRConfig.getInstance().ROOT_SCENE;
		scene.setKeyEvents(eventGroup);

		
		
	}

	public void setEnableHotKey(boolean enable) {
		if(enable) {
			eventGroup.addAllColourKeys();
		} else {
			eventGroup.removeAllColourKeys();
		}
        HScene scene = (HScene) SSRConfig.getInstance().ROOT_SCENE;
		scene.setKeyEvents(eventGroup);
		
	}

	public void setEnableBackKey(boolean enable) {
		if(enable) {
			eventGroup.addKey(HRcEvent.VK_F4);
		} else {
			eventGroup.removeKey(HRcEvent.VK_F4);
		}
        HScene scene = (HScene) SSRConfig.getInstance().ROOT_SCENE;
		scene.setKeyEvents(eventGroup);
	}

	public void setEnableTrickKey(boolean enable) {
		if(enable) {
			eventGroup.addKey(HRcEvent.VK_REWIND);
			eventGroup.addKey(HRcEvent.VK_PLAY);
			eventGroup.addKey(HRcEvent.VK_STOP);
			eventGroup.addKey(HRcEvent.VK_FAST_FWD);
		} else {
			eventGroup.removeKey(HRcEvent.VK_REWIND);
			eventGroup.removeKey(HRcEvent.VK_PLAY);
			eventGroup.removeKey(HRcEvent.VK_STOP);
			eventGroup.removeKey(HRcEvent.VK_FAST_FWD);
		}
        HScene scene = (HScene) SSRConfig.getInstance().ROOT_SCENE;
		scene.setKeyEvents(eventGroup);
		
	}

    public void setEnableOkKey(boolean enable) {
        if(enable) {
            eventGroup.addKey(HRcEvent.VK_ENTER);
        } else {
            eventGroup.removeKey(HRcEvent.VK_ENTER);
        }
        HScene scene = (HScene) SSRConfig.getInstance().ROOT_SCENE;
        scene.setKeyEvents(eventGroup);
    }

    public void setEnableArrowKey(boolean enable) {
        if(enable) {
            eventGroup.addAllArrowKeys();
        } else {
            eventGroup.removeAllArrowKeys();
        }
        HScene scene = (HScene) SSRConfig.getInstance().ROOT_SCENE;
        scene.setKeyEvents(eventGroup);
    }
    	
}
