package com.cj.tvui.dmc.skylife;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.cj.tvui.controller.KeyController;
import com.cj.tvui.util.LOG;
import org.havi.ui.event.HRcEvent;

public class KeyListenerImpl implements KeyListener {

	public void keyTyped(KeyEvent e) {
		LOG.print(this, "Skylife Key Typed...");
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		LOG.print(this, "Skylife Key Pressed");
		if(e.getKeyCode()==HRcEvent.VK_F1 || (e.getKeyCode()>=HRcEvent.VK_0 && e.getKeyCode()<=HRcEvent.VK_9)) {
		    //이전키 및 숫자키는 UserKeyManager.userEvent로 받는다.
        } else {
            KeyController.getInstance().keyProcess(e);
        }

		
	}

	public void keyReleased(KeyEvent e) {
		LOG.print(this, "Skylife Key Released");
		// TODO Auto-generated method stub
		
	}

}
