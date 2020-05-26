package com.nds.platform.epg.app.control;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EPGAppStateNotificationListener extends Remote {
    
    /*
     * Application should register this listener thorugh
     * EPGXlet. Whenever there is a change is state this method 
     * is called. Please make sure the application has to return this
     * call immediately as this will block the call.
     */
    public void notifyEPGStateChange(int newState) throws RemoteException;

}
