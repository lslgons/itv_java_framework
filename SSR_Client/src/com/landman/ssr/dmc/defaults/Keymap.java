package com.landman.ssr.dmc.defaults;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.dmc.defaults.KeyListenerImpl;
import com.cj.tvui.dmc.interfaces.KeymapInterface;
import com.cj.tvui.util.LOG;

import java.awt.event.KeyListener;

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
        LOG.print("Load default Keymap");
        keyListener = new KeyListenerImpl();

        eventGroup = new HEventGroup();
        //Default Setting
        eventGroup.addAllArrowKeys();
        eventGroup.addAllColourKeys();
        //eventGroup.addAllNumericKeys();
        eventGroup.addKey(HRcEvent.VK_ENTER);
    }

    public int keyMap(int keycode) {
        int mapping_code = 0;
        //Example...
        LOG.print("Keymap : "+keycode);
        switch(keycode) {

            case HRcEvent.VK_ENTER :
                mapping_code = Keys.VK_OK;
                break;
            case HRcEvent.VK_INFO:
                mapping_code = Keys.VK_INFO;
                break;
            case HRcEvent.VK_A:
                mapping_code = Keys.VK_R;
                break;
            case HRcEvent.VK_S:
                mapping_code = Keys.VK_G;
                break;
            case HRcEvent.VK_D:
                mapping_code = Keys.VK_Y;
                break;
            case HRcEvent.VK_F:
                mapping_code = Keys.VK_B;
                break;
            case HRcEvent.VK_BACK_SPACE:
                mapping_code = Keys.VK_BACK;
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
		// TODO Auto-generated method stub
		return eventGroup;
	}

	public void setEnableNumKey(boolean enable) {
        if(enable) {
            eventGroup.addAllNumericKeys();
        } else {
            eventGroup.removeAllNumericKeys();
        }
        HScene scene = (HScene)SceneController.getInstance().getRootScene();
        scene.setKeyEvents(eventGroup);
		
	}

	public void setEnableHotKey(boolean enable) {
        if(enable) {
            eventGroup.addAllColourKeys();
        } else {
            eventGroup.removeAllColourKeys();
        }
        HScene scene = (HScene)SceneController.getInstance().getRootScene();
        scene.setKeyEvents(eventGroup);
		
	}

	public void setEnableBackKey(boolean enable) {
        if(enable) {
            eventGroup.addKey(HRcEvent.VK_F1);
        } else {
            eventGroup.removeKey(HRcEvent.VK_F1);
        }
        HScene scene = (HScene)SceneController.getInstance().getRootScene();
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
        HScene scene = (HScene)SceneController.getInstance().getRootScene();
        scene.setKeyEvents(eventGroup);
		
	}

    public void setEnableOkKey(boolean enable) {
        if(enable) {
            eventGroup.addKey(HRcEvent.VK_ENTER);
        } else {
            eventGroup.removeKey(HRcEvent.VK_ENTER);
        }
        HScene scene = (HScene)SceneController.getInstance().getRootScene();
        scene.setKeyEvents(eventGroup);
    }

    public void setEnableArrowKey(boolean enable) {
        if(enable) {
            eventGroup.addAllArrowKeys();
        } else {
            eventGroup.removeAllArrowKeys();
        }
        HScene scene = (HScene)SceneController.getInstance().getRootScene();
        scene.setKeyEvents(eventGroup);
    }
    	
}
