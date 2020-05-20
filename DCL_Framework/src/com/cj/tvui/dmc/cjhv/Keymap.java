package com.cj.tvui.dmc.cjhv;

import com.alticast.navsuite.service.OverlappedDialogHandler;
import com.cj.tvui.Keys;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.dmc.interfaces.KeymapInterface;
import com.cj.tvui.util.LOG;

import org.havi.ui.event.HRcEvent;

/**
 * Created by daegon.kim on 2016-12-06.
 */
public class Keymap implements KeymapInterface{

    public Keymap() {
        LOG.print("Load CJHV Keymap");
        handler = new OverlappedUIHandlerImpl();
    }
    private OverlappedDialogHandler handler;

    public int keyMap(int keycode) {
        int mapping_code = 0;
        //Example...
        switch(keycode) {
            case HRcEvent.VK_ENTER :
                mapping_code = Keys.VK_OK;
            case HRcEvent.VK_INFO:
                mapping_code = Keys.VK_INFO;
            default:
                mapping_code = keycode;
        }

        return mapping_code;
    }

	public int keyProcess(int keycode) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getKeyListener() {
		// TODO Auto-generated method stub
		return handler;
	}

	public Object getEventGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setEnableNumKey(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	public void setEnableHotKey(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	public void setEnableBackKey(boolean enable) {
		// TODO Auto-generated method stub
		
	}
	public void setEnableTrickKey(boolean enable) {
		
	}
    public void setEnableOkKey(boolean enable) {

    }

    public void setEnableArrowKey(boolean enable) {

    }


}
