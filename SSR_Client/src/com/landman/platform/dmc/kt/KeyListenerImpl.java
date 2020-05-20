package com.landman.ssr.dmc.kt;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.cj.tvui.controller.KeyController;
import com.cj.tvui.util.LOG;

public class KeyListenerImpl implements KeyListener {

	public void keyTyped(KeyEvent e) {
		LOG.print(this, "KT Key Typed...");
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		LOG.print(this, "KT Key Pressed");
		KeyController.getInstance().keyProcess(e);
		
	}

	public void keyReleased(KeyEvent e) {
		LOG.print(this, "KT Key Released");
		// TODO Auto-generated method stub
		
	}

}
