// $Header: /home/cvs/GS_Home_Shopping/CJ_2007/bound/src/monitor/ixc/ApplicationModeManager.java,v 1.1 2007/12/27 07:01:42 dragon19 Exp $

/*
 *  ApplicationModeManager.java	$Revision: 1.1 $ $Date: 2007/12/27 07:01:42 $
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
 * <code>ApplicationModeManager</code> provides APIs to avoid the conflict
 * bewteen enhanced bound application and system application. It gives
 * central and general way to solve the conflict between them instead of 
 * calling APIs between themselves one-to-one.
 * <ul>
 * <li>All enhanced bound application which needs full mode, should register
 * {@link ApplicationModeRequestHandler} and respond againt 
 * the request of mode change.</li>
 * <li>All enhanced bound application should call {@link #changeMode(int)}
 * to ask if system application allows, before it really changes</li>
 * <li>All system application which needs negotiation with enhanced bound
 * application, should register {@link ApplicationModeListener}
 * and respond on mode changes.</li>
 * <li>All system application should call {@link #requestModeChange(int)}
 * to ask if enhanced bound application allows, before it really changes</li>
 * </ul>
 * 
 * @author $Author: dragon19 $
 * @version $Revision: 1.1 $
 * @since Charles CW Kwak, 2007. 2. 9
 */
public interface ApplicationModeManager extends Remote {
    /**
     * IXC stub object name
     */
    public static final String IXC_OBJECT_NAME = "ApplicationModeManager";

    /**
     * Application mode constant indicating enhanced bound application shows
     * nothing.
     */
    public static final int APP_MODE_NONE = 0;
    /**
     * Application mode constant indicating enhanced bound application just
     * shows small-sized UI not interfering UI of system applications.
     */
    public static final int APP_MODE_ICON = 1;
    /**
     * Application mode constant indicating enhanced bound application ocuppies
     * resources such as Graphics, Video or Background plane interfering
     * system application UI in some degree or all.
     */
    public static final int APP_MODE_FULL = 2;

    /**
     * Adds {@link ApplicationModeListener}.<br>
     * 
     * <b>API for system application.</b>
     * 
     * @param l Implementation object to start listening application mode changes
     * @throws RemoteException when any exception occurs during processing 
     *      this function
     */
    public void addListener(ApplicationModeListener l) throws RemoteException;

    /**
     * Removes {@link ApplicationModeListener}.<br>
     * 
     * <b>API for system application</b>
     * 
     * @param l Implementation object to stop listening application mode changes
     * @throws RemoteException when any exception occurs during processing 
     *      this function
     */
    public void removeListener(ApplicationModeListener l)
        throws RemoteException;

    /**
     * Requests application mode to the given mode for requesting application
     * to avoid resource conflict such as Graphics, Video, or Background plane.
     * <br>
     * 
     * <b>API for system application</b>
     * 
     * @param newMode new application mode which enhanced bound application
     *      is requested to be
     * @return <code>true</code> means enhanced bound application changes or has
     *      already changed to the request mode. <code>false</code> means
     *      enhanced bound application refuses.
     * @throws RemoteException when any exception occurs during processing 
     *      this function
     */
    public boolean requestModeChange(int newMode) throws RemoteException;

    /**
     * Registers {@link ApplicationModeRequestHandler}.<br>
     * 
     * <b>API for enhanced bound application</b>
     * 
     * @param handler Implementation object to respond to mode change request.
     * @throws RemoteException when any exception occurs during processing 
     *      this function
     * @deprecated use #addRequestHandler(ApplicationModeRequestHandler) instead
     */
    public void registerRequestHandler(ApplicationModeRequestHandler handler)
        throws RemoteException;

    /**
     * Adds {@link ApplicationModeRequestHandler}.<br>
     * 
     * <b>API for enhanced bound application</b>
     * <br>
     * <b>REMARK: </b>{@link ApplicationModeRequestHandler} must be removed.
     * 
     * @param handler Implementation object to respond to mode change request.
     * @throws RemoteException when any exception occurs during processing
     *      this function
     * @see #addRequestHandler(ApplicationModeRequestHandler, int, int)
     */
    public void addRequestHandler(ApplicationModeRequestHandler handler)
        throws RemoteException;

    /**
     * Adds {@link ApplicationModeRequestHandler} with given registering
     * application ID.<br>
     * The big difference to 
     * {@link #addRequestHandler(ApplicationModeRequestHandler)} is that
     * it is not called back when {@link #changeMode(int, int, int)} is called
     * with same application ID as this function call. It is needed when
     * SMS application becomes full mode without calling back its own
     * {@link ApplicationModeRequestHandler}.
     * 
     * <b>API for SMS application or enhanced bound application if needed</b>
     * <br>
     * <b>REMARK: </b>{@link ApplicationModeRequestHandler} must be removed.
     * It replaces previous {@link ApplicationModeRequestHandler} of same
     * register, therefore, only one of same register can be added.
     * 
     * @param handler Implementation object to respond to mode change request.
     * @param oid organization ID of registering application
     * @param aid application ID of registering application
     * @throws RemoteException when any exception occurs during processing
     *      this function
     * @see #addRequestHandler(ApplicationModeRequestHandler)
     */
    public void addRequestHandler(ApplicationModeRequestHandler handler,
        int oid, int aid) throws RemoteException;

    /**
     * Removes {@link ApplicationModeRequestHandler}.<br>
     * 
     * <b>API for enhanced bound application and SMS</b>
     * 
     * @param handler Implementation object to respond to mode change request.
     * @throws RemoteException when any exception occurs during processing
     *      this function
     */
    public void removeRequestHandler(ApplicationModeRequestHandler handler)
        throws RemoteException;

    /**
     * Tries to change application mode to the given mode.<br> This call
     * with {@link #APP_MODE_FULL} does not cause calling back 
     * {@link ApplicationModeRequestHandler#modeChangeRequested(int, boolean)}
     * at all.
     * 
     * <b>API for enhanced bound application</b>
     * 
     * @param newMode new application mode which enhanced bound application 
     *      wants to be changed to
     * @return <code>true</code> means no system application refuses. 
     *      <code>false</code> means more than one system applications refuse.
     * @throws RemoteException when any exception occurs during processing 
     *      this function
     * @see #changeMode(int, int, int)
     */
    public boolean changeMode(int newMode) throws RemoteException;

    /**
     * Tries to change application mode to the given mode.</br> This call
     * with {@link #APP_MODE_FULL} causes calling back all other
     * {@link ApplicationModeRequestHandler#modeChangeRequested(int, boolean)}
     * with {@link #APP_MODE_NONE} except requester itself.
     * 
     * <b>API for SMS application or enhanced bound application if needed.</b>
     * 
     * @param newMode new application mode which enhanced bound application
     *      wants to be changed to
     * @param oid organization ID of mode change requester application
     * @param aid application ID of mode change requester application
     * @return <code>trueM/code> means no system applicatin refuses.
     *      <code>false</code> means more than one system applications refuse.
     * @throws RemoteException when any exception occurs during processing 
     *      this function
     * @see #changeMode(int)
     */
    public boolean changeMode(int newMode, int oid, int aid)
        throws RemoteException;

    /**
     * Returns current application mode.
     * 
     * @return Application mode constant.
     * @throws RemoteException when any exception occurs during processing 
     *      this function
     */
    public int getMode() throws RemoteException;
}
