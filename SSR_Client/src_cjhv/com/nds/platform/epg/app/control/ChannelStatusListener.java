package com.nds.platform.epg.app.control;

public interface ChannelStatusListener {
	
	public static final int AV_OK = 0;
	public static final int USER_BLOCKED = 1;
	public static final int PR_BLOCKED = 2;
	public static final int UNSUBSCRIBED_BLOCKED = 3;
	
	/**
	 * This method returns the status of current tuned channel
	 * @param status
	 * @return 
	 * 0 : A/V OK
	 * 1 : user blocked
	 * 2 : parental rating blocked
	 * 3 : unsubscribed blocked
	 */
	public void notifyChannelStatus(int status);
}
