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

/**
 * The listener interface for receiving cardInfoUpdate events.
 * The class that is interested in processing a cardInfoUpdate
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addCardInfoUpdateListener<code> method. When
 * the cardInfoUpdate event occurs, that object's appropriate
 * method is invoked.
 *
 * @see CardInfoUpdateEvent
 */
public interface CardInfoUpdateListener extends Remote {
    
    /**
     * Invoked when notify card info update occurs.
     *
     * @param mask  변경된 카드정보의 mask 값.
     *              {@link EpgService#MASK_CARD_ID} | {@link #MASK_SUBS_ID} | {@link #MASK_SO_CODE}
     *              | {@link #MASK_REGION_BYTE} | {@link #MASK_CAS_ID} | {@link #MASK_PR}
     * 
     * @throws RemoteException
     */
    public void notifyCardInfoUpdated(int mask) throws RemoteException;
}