package com.tcom.platform.dmc.cjhv;


import com.alticast.epg.ixc.EpgService;
import com.alticast.navsuite.core.cj.CjSystemInfo;
import com.nds.platform.epg.app.control.EPGXlet;
import com.tcom.platform.dmc.interfaces.StbInterface;
import com.tcom.ssr.SSRConfig;
import com.tcom.util.LOG;
import com.tcom.util.StringUtil;
import monitor.ixc.ApplicationModeManager;
import org.dvb.io.ixc.IxcRegistry;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by user on 2016-12-07.
 */
public class Stb implements StbInterface {

	final static String ALTI_EPG_AID_OID="/10000000/3030/"+EpgService.RMI_APP_NAME;
	final static String NDS_EPG_AID_OID="/10000000/3000/NDSEPGXLET";
	private String subscriberID01=null;
	private String soCode=null;
	private EpgService epgController=null;
	private EPGXlet epgXlet=null;
	private ApplicationModeManager appModeManager;

	public Stb() {
		//LookupEPG
		if(SSRConfig.getInstance().XLET_CONTEXT==null) {
			LOG.print("Xlet Context is null, lookupEPG can't attempt");
		} else {
			LOG.print("STB constructor, find EPG Xlet");
			//lookup이 정상적으로 처리되면 신규 Alti EPG로 인식하여 EpgService연동
			try {
				epgController=(EpgService) IxcRegistry.lookup(SSRConfig.getInstance().XLET_CONTEXT, ALTI_EPG_AID_OID);
				LOG.print("XCAS EpgService lookup, epgController="+epgController);
				try {
					String version = epgController.getVersion();
					LOG.print("EPG version : "+version);
					if(version != null && version.length()!=0) {

						SSRConfig.getInstance().IS_ALASKA_UI=1;
					} else {
						SSRConfig.getInstance().IS_ALASKA_UI=0;
					}
				} catch (Exception e) {
					e.printStackTrace();
					SSRConfig.getInstance().IS_ALASKA_UI=0;
				}

			} catch (NotBoundException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				LOG.print("getVersion Exception :: Legacy EPG");
			}

			//epgController가 null이면 Cisco EPG로 판단
			if(epgController==null) {
				try {
					epgXlet=(EPGXlet) IxcRegistry.lookup(SSRConfig.getInstance().XLET_CONTEXT, NDS_EPG_AID_OID);
					LOG.print(this, "JCAS EPGXlet lookup : "+epgXlet);
				} catch (NotBoundException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void sendLog(int logType, String serviceID) {
		// TODO Auto-generated method stub
		
	}

	public String getSubscribeID() {
		LOG.print(this, "getSubscribeID");
		if(subscriberID01==null) {
			try {
				if(epgController==null) {
					//NDS EPG
					LOG.print("Get subscribeID from NDS EPG");
					this.subscriberID01=epgXlet.getSubscriberID();
				} else {
					LOG.print("get subscribeID from Alti EPG");
					this.subscriberID01 = epgController.getSubscriberID();
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		return subscriberID01;
	}

	public String getSOCode() {
		//TODO CJHAPIManager와 병합, EPG를 통해 가져오는가, API를 통해 가져오는가 차이...
		if(soCode==null) {
			LOG.print(this, "getSOCode, soCode is null, get soCode from EPG");
			try {
				if(epgController==null) {
					LOG.print("NDS EPG");
					soCode=String.valueOf(epgXlet.getSoNumber());
				} else {
					LOG.print("Alti EPG");
					soCode= String.valueOf(epgController.getSoNumber());

				}

			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (NullPointerException e1) {
				e1.printStackTrace();
				LOG.print("EPG Lookup Failed");
			}
		}
		return soCode;
	}

	public void setSOCode(String socd) {
		// TODO Auto-generated method stub
		
	}

	public String getDMCCode() {
		return "2997";
	}

	public String getModelNo() {
		return CjSystemInfo.getModelNumber();
	}

	/**
	 * CJH에서는 stbID식별자로 사용해야 함 (SMS_TAG)
	 * @return
	 */

	String SC_ID=null;
	public String getSmartcardID() {
		if(SC_ID!=null) return SC_ID;
		else if(SSRConfig.getInstance().getPropertyReader().read("USE_SMS_TAG").equalsIgnoreCase("true")) {
			//IS_JCN
			try {
				LOG.print(this, "Set Smartcard ID from sms_tag");
				SC_ID = CJHAPIManager.getInstance().getSMSTag();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			LOG.print(this, "Set Smartcard ID from EPG");
			String subscribeID=this.getSubscribeID();
			if(subscribeID.length()<11) {
				for (int i=subscribeID.length();i<11;i++) {
					subscribeID="0"+subscribeID;
				}
			}
			SC_ID=subscribeID;
		}
		return SC_ID;
	}
	
	public String getMacAddr() {
		// TODO Auto-generated method stub
		String mac_addr= StringUtil.bytesToHex(CjSystemInfo.getHostMacAddress());
		LOG.print("Host MAC Address : "+mac_addr);
		return mac_addr;
	}

	public String getDeviceSN() {
		// TODO Auto-generated method stub
		return null;
	}
}
