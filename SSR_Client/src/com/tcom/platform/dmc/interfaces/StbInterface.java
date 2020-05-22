package com.tcom.platform.dmc.interfaces;

/**
 * Created by daegon.kim on 2016-12-05.
 */
public interface StbInterface {
	
	public int LOG_START=10;
	public int LOG_END=11;
	
    public void sendLog(int logType, String serviceID);
    public String getSubscribeID();
    public String getSOCode();
    public String getDMCCode();
    //IPTV에서의 SO코드 설정을 고려
    public void setSOCode(String socd);
    public String getModelNo();
    public String getSmartcardID();
    public String getMacAddr();
    public String getDeviceSN();
}
