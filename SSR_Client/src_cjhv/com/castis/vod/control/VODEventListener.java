package com.castis.vod.control;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * VOD Event Listener
 * 
 * @version 1.2
 * @author castis
 * @since 1.0
 * 
 */
public interface VODEventListener extends Remote {

    /**
     * VOD Event 수신
     * 
     * @param vodEvent
     *            {@link VODPlayer#VODEVENT_SESSION_NORMAL}
     *            {@link VODPlayer#VODEVENT_SESSION_ERROR}
     *            {@link VODPlayer#VODEVENT_PAUSE_TIMEOUT}
     *            {@link VODPlayer#VODEVENT_BEGIN_OF_FILE}
     *            {@link VODPlayer#VODEVENT_END_OF_FILE}
     *            {@link VODPlayer#VODEVENT_TUNNING_ERROR}
     *            {@link VODPlayer#VODEVENT_NEED_TUNE}
     * @throws RemoteException
     */
    public void receiveEvent(int vodEvent) throws RemoteException;

}
