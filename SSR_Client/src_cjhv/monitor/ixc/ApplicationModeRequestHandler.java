// $Header: /home/cvs/GS_Home_Shopping/CJ_2007/bound/src/monitor/ixc/ApplicationModeRequestHandler.java,v 1.1 2007/12/27 07:01:42 dragon19 Exp $

/*
 *  ApplicationModeRequestHandler.java	$Revision: 1.1 $ $Date: 2007/12/27 07:01:42 $
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
 * <code>ApplicationModeRequestHandler</code> concerns application mode
 * change request from system applicaiton such as iEPG.
 * All enhanced bound application having full mode should implements this
 * interface.
 * 
 * @author $Author: dragon19 $
 * @version $Revision: 1.1 $
 * @since Charles CW Kwak, 2007. 2. 9
 */
public interface ApplicationModeRequestHandler extends Remote {
    /**
     * Notifies when system application askes enhanced bound application
     * to be changed to new application mode. enhanced bound application may
     * refuse to change.
     * 
     * @param newMode new application mode which system application wants 
     *      enhanced bound application to change to
     * @param forced <code>true</code> means enhanced bound application should
     *      change to the requested application mode. <code>false</code>
     *      depends on enhanced bound application's decision.
     * @return <code>true</code> means bound application changes to 
     *      the requested application mode. <code>false</code> means enhanced
     *      bound application refuses.
     * @throws RemoteException when any exception occurs during processing 
     *      this function
     */
    public boolean modeChangeRequested(int newMode, boolean forced)
        throws RemoteException;
}

