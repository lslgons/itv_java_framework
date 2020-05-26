package com.tcom.platform.dmc.cjhv;

import com.tcom.platform.controller.KeyController;
import com.tcom.ssr.SSRConfig;
import com.tcom.util.LOG;
import org.dvb.event.UserEvent;

import com.alticast.navsuite.service.OverlappedDialogHandler;
import org.havi.ui.event.HRcEvent;

import java.awt.*;
import java.awt.event.KeyEvent;


public class OverlappedUIHandlerImpl implements OverlappedDialogHandler {
	int lastKeyCode=-1;
	long lastKeyInput;
	boolean modeAccel=false;
	public void requestDispose(boolean arg0) {
		Container rootScene = SSRConfig.getInstance().ROOT_SCENE;
		if(rootScene!=null) {
			rootScene.setVisible(false);
		}

	}

	public boolean handleKeyEvent(UserEvent evt) {
		if(evt.getType()==KeyEvent.KEY_PRESSED) {
			int code=evt.getCode();
			long when = evt.getWhen();
			long curTime=System.currentTimeMillis();
			long term = curTime-when;
			LOG.print(this, "OverlappedDialogHandler KeyEvent code: "+code+", term: "+term+
					", lastKeyCode: "+lastKeyCode+"lastInput: "+lastKeyInput);
			term=when-this.lastKeyInput;
			if(term<1000 && this.lastKeyCode==code) {
				LOG.print(this, "OverlappedDialoHandler KeyEvent press continue same key");
				switch(code) {
					case KeyEvent.VK_DOWN:
						code = HRcEvent.VK_BASS_BOOST_DOWN;
						this.modeAccel=true;
						break;
					case KeyEvent.VK_UP:
						code=HRcEvent.VK_BASS_BOOST_UP;
						this.modeAccel=true;
						break;
					default:
						this.modeAccel=false;
						break;

				}
			} else {
				LOG.print(this, "OverlappedDialogHandler KeyEvnt press key");
			}
			this.lastKeyCode=evt.getCode();
			this.lastKeyInput=when;
			return KeyController.getInstance().keyProcess(code);
		} else if(evt.getType()==KeyEvent.KEY_RELEASED) {
			this.modeAccel=false;
			this.lastKeyCode=-1;
		}
		return false;

	}

}
