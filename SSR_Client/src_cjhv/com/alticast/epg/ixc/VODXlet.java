package com.alticast.epg.ixc;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VODXlet extends Remote {

    /**
     * EPG Organization ID.
     */
    public static final String EPG_OID = "10000000";

    /**
     * type category
     */
    public static final String TYPE_CATEGORY = "ct01";

    /**
     * type asset
     */
    public static final String TYPE_ASSET = "as01";

    /**
     * EPG Application ID.
     */
    public static final String EPG_APP_ID = "3030";

    public static final String RMI_APP_NAME = "VODXLET";

    public void requestVODLink(String type, String value) throws RemoteException;

}
