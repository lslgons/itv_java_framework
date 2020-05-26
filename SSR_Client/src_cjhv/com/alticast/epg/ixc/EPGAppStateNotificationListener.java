/*
 *  Copyright (c) 2014 Alticast Corp.
 *  All rights reserved. http://www.alticast.com/
 *
 *  This software is the confidential and proprietary information of
 *  Alticast Corp. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with Alticast.
 */
package com.alticast.epg.ixc;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EPGAppStateNotificationListener extends Remote {
    
    /*
     * Application should register this listener through
     * EpgService. Whenever there is a change is state this method 
     * is called. Please make sure the application has to return this
     * call immediately as this will block the call.
     */
    public void notifyEPGStateChange(int newState) throws RemoteException;

}
