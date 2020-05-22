package com.tcom.platform.dmc.pc;

import com.tcom.platform.controller.KeyController;
import com.tcom.util.LOG;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyListenerImpl implements KeyListener {

	public void keyTyped(KeyEvent e) {
		
		
	}

	public void keyPressed(KeyEvent e) {
		LOG.print(this, "Key Pressed");
		KeyController.getInstance().keyProcess(e);
		
	}

	public void keyReleased(KeyEvent e) {
		
		
	}

}
