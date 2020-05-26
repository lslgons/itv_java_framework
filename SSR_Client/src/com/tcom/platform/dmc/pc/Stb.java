package com.tcom.platform.dmc.pc;


import com.tcom.platform.dmc.interfaces.StbInterface;

import java.rmi.RemoteException;

/**
 * Created by user on 2016-12-07.
 */
public class Stb implements StbInterface {
	
	private String soCode = "99";
	
	public void sendLog(int logType, String serviceID) {
		// TODO Auto-generated method stub
		
	}

	public String getSubscribeID() {
		return "1234567890";
	}

	public String getSOCode() {
		return soCode;
	}

	public String getDMCCode() {
		return "3026";
	}

	public String getModelNo() {
		return "Emulator";
	}

	public String getSmartcardID() {
		return "1234567890ABCDEF";
	}

	public void setSOCode(String socd) {
		this.soCode = socd;
		
	}

	public String getMacAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDeviceSN() {
		// TODO Auto-generated method stub
		return null;
	}

	public void destroy() {

	}


}
