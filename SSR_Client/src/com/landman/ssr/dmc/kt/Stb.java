package com.landman.ssr.dmc.kt;

import com.cj.tvui.dmc.interfaces.StbInterface;
import com.cj.tvui.util.LOG;
import com.cj.tvui.util.StringUtil;

import kt.alticast.interactive.service.common.ServiceUsage;
import kt.alticast.interactive.service.common.client.ServiceUsageClientStub;

/**
 * Created by user on 2016-12-07.
 */
public class Stb implements StbInterface {
	private String subscribeID = null;
	private String soCode = null;
	private String modelNo = null;
	private String scId = null;
	
	private String serviceId = null;
	private String startTime;
	private String endTime;
	private long usageID;
    
	public void sendLog(int logType, String serviceID) {
		if (logType == LOG_START) {
			this.serviceId = serviceID;
			if (StringUtil.isEmpty(this.serviceId)) return;

			this.startTime = ServiceUsageClientStub.getCurrentTime(System.currentTimeMillis());

			LOG.print(this, "startLog serviceId : " + this.serviceId + ", startTime : " + this.startTime);

			if (StringUtil.isEmpty(this.startTime)) return;

			ServiceUsage service = new ServiceUsageClientStub( ServiceUsage.class );
			this.usageID = service.startServiceUsage(getSubscribeID(), null, this.serviceId, this.startTime, null);

			LOG.print(this, "startLog End - usageID : " + this.usageID);
		} else if(logType == LOG_END) {
			LOG.print(this, "endLog Call");

			if (StringUtil.isEmpty(this.serviceId)) {
				return;
			}

			this.endTime = ServiceUsageClientStub.getCurrentTime(System.currentTimeMillis());

			LOG.print(this, "endLog serviceId : " + this.serviceId + ", startTime : " + this.startTime + ", endTime : " + this.endTime);

			if (StringUtil.isEmpty(this.startTime) || StringUtil.isEmpty(this.endTime)) return;

			ServiceUsage service = new ServiceUsageClientStub( ServiceUsage.class );

			if (this.usageID == 0L) {
				//시작로그를 정상적으로 처리 하지 못한 경우 시작/종료 로그
				service.createServiceUsage(getSubscribeID(), null, this.serviceId, this.startTime, this.endTime, null);
				LOG.print(this, "endLog createServiceUsage");
			} else {
				//종료로그
				service.endServiceUsage(this.usageID, this.endTime, null);
				LOG.print(this, "endLog endServiceUsage");
			}
			LOG.print(this, "endLog End - SAID : " + getSubscribeID() + ", usageID : " + this.usageID + ", serviceId : " + this.serviceId + ", startTime : " + this.startTime);
		} else {
			throw new RuntimeException("Invalid Log Type");
		}
		
	}

	public String getSubscribeID() {
		if(subscribeID == null) subscribeID = com.kt.sso.SSOClient.getInstance().getSAID();
		return subscribeID;
	}

	public String getSOCode() {
		//KT는 SO코드 없음...
		if(soCode == null) {
			//오쇼핑플러스 SO코드, 범용성 고려하면 setSOCode필요함
			soCode = "3063";
		}
		return soCode;
	}
	public void setSOCode(String socd) {
		this.soCode = socd;
		
	}

	public String getModelNo() {
		if(modelNo == null) modelNo = com.kt.util.sysinfo.STBInformation.getInstance().getModelNumber();
		return modelNo;
	}

	public String getSmartcardID() {
		if(scId == null) scId = com.kt.util.sysinfo.STBInformation.getInstance().getSmartCardId();
		return scId;
	}

	public String getMacAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDeviceSN() {
		// TODO Auto-generated method stub
		return null;
	}



	
}
