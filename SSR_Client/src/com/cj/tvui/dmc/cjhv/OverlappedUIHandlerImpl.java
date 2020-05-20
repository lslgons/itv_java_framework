package com.cj.tvui.dmc.cjhv;

import org.dvb.event.UserEvent;

import com.alticast.navsuite.service.OverlappedDialogHandler;
import com.cj.tvui.controller.KeyController;

public class OverlappedUIHandlerImpl implements OverlappedDialogHandler {

	public void requestDispose(boolean arg0) {
		// TODO Auto-generated method stub

	}

	public boolean handleKeyEvent(UserEvent arg0) {
		return KeyController.getInstance().keyProcess(arg0.getCode());
	}

}
