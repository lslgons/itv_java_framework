// $Header: /home/cvs/cj.homeshopping_enhanced_ehkim/App/monitor/ixc/ApplicationCoordinator.java,v 1.1 2007/05/15 00:46:22 ian80 Exp $

/*
 *  ApplicationCoordinator.java	$Revision: 1.1 $ $Date: 2007/05/15 00:46:22 $
 *
 *  Copyright (c) 2004 Alticast Corp.
 *  All rights reserved. http://www.alticast.com/
 *
 *  This software is the confidential and proprietary information of
 *  Alticast Corp. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with Alticast.
 */
package monitor.ixc;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * <code>ApplicationCoordinator</code> provides APIs to request external state
 * transition or handle unbound applications.
 * 
 * @author $Author: ian80 $
 * @version $Revision: 1.1 $
 * @since Charles CW Kwak, 2007. 2. 9
 */
public interface ApplicationCoordinator extends Remote {
    /**
     * IXC stub object name
     */
    public static final String IXC_OBJECT_NAME = "ApplicationCoordinator";

    /**
     * External state constant indicating AV state
     */
    public static final int STATE_AV = 0x00010000;
    /**
     * External state constant indicating ITV state 
     */
    public static final int STATE_ITV = 0x00020000;

    /**
     * Requests the external state transition. It handles state transition
     * asynchronously, so the completion of function call does not mean 
     * that of state transition.
     * 
     * @param state the complete state value which may include only external
     *      state or both external state and iEPG state. For instance, if 
     *      iEPG requests to change to Home menu state, <code>state</code>
     *      will be ({@link #STATE_AV} | {@link EPGXlet#EPG_MENU}). However if iEPG
     *      requests to change to iTV state, <code>state</code> will be
     *      {@link #STATE_ITV}.      
     * @param sourceId the source ID of the channel to select in new state.
     * @throws RemoteException when any exception occurs during processing 
     *      this function
     * @see #requestTransition(int)
     */
    public void requestTransition(int state, int sourceId)
        throws RemoteException;

    /**
     * Requests the external state transition. It handles state transition
     * asynchronously, so the completion of function call does not mean
     * that of state transition. 
     * 
     * <br>The state to transit to is determined by service of source ID. 
     * That is, if source ID specifies normal AV, PPV, or audio service,
     * state will be determined as TV viewing state. And if source ID 
     * specifies data broadcast service, state will be determined by ITV state.
     * 
     * @param sourceId the source ID of the channel to select and it also
     *      determines new state to transit to. If it's not valid source ID,
     *      it does nothing.
     * @throws RemoteException when any exception occurs during processing this
     *      function
     * @see #requestTransition(int, int)
     */
    public void requestTransition(int sourceId) throws RemoteException;

    /**
     * Starts unbound application of the given name, which should be specified 
     * in application_name_descriptor of XAIT.
     * 
     * @param name the unbound application name. It should be specified in
     *      application_name_descriptor of XAIT.
     * @return <code>false</code> if it fails to find application of the 
     *      specified name
     * @throws RemoteException when any exception occurs during processing
     *      this function
     */
    public boolean startUnboundApplication(String name) throws RemoteException;

    /**
     * Starts unbound application of the given application ID, which should be 
     * specified in XAIT.
     * 
     * @param oid Organization ID of the unbound application
     * @param aid Application ID of the unbound application
     * @return <code>false</code> if it fails to find application of the
     *      specified application ID.
     * @throws RemoteException when any exception occurs during processing
     *      this function
     */
    public boolean startUnboundApplication(int oid, int aid)
        throws RemoteException;

    /**
     * Stops unbound application of the given name, which should be specified
     * in application_name_descriptor of XAIT.
     * 
     * @param name the unbound application name. It should be specified in
     *      application_name_descriptor of XAIT.
     * @return <code>false</code> if it fails to find applicatin of the 
     *      specified name.
     * @throws RemoteException when any exception occurs during processing
     *      this function
     */
    public boolean stopUnboundApplication(String name) throws RemoteException;
    
    /**
     * Stops unbound application of the given application ID, which should be 
     * specified in XAIT.
     * 
     * @param oid Organization ID of the unbound application
     * @param aid Application ID of the unbound application
     * @return <code>false</code> if it fails to find application of the
     *      specified application ID.
     * @throws RemoteException when any exception occurs during processing
     *      this function
     */
    public boolean stopUnboundApplication(int oid, int aid)
        throws RemoteException;
}

