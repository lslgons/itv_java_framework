package com.landman.ssr.dmc.defaults;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.cj.tvui.controller.KeyController;
import com.cj.tvui.util.LOG;

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
