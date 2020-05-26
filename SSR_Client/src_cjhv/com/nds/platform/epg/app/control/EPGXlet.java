/*=================================================================
 *  Copyright (c) 2006 -2007: NDS Ltd.
 *
 *  P R O P R I E T A R Y        C O N F I D E N T I A L
 *
 *  The copyright of this document is vested in NDS Ltd. without
 *  whose prior written permission its contents must not be published,adapted
 *  or reproduced in any form or disclosed or issued to any third party.
 *=================================================================*/
package com.nds.platform.epg.app.control;

import java.rmi.Remote;
import java.rmi.RemoteException;
import com.acetel.cj.vod.ixc.SearchResultModel;

/**
 * Desription -
 * 
 * @author kirans @ Jun 8, 2004
 */
public interface EPGXlet extends Remote {
    /**
     * Indicates the Pause state of EPG
     */
    public static final int    EPG_PAUSE         = 1;

    /**
     * Indicates the AV state of EPG
     */
    public static final int    EPG_TV_WATCHING    = 2;

  

    public static final int    VOD_WATCHING = 3;

    public static final int    EPG_MENU           = 4;

    // This is for VOD App.
    public static final int    VOD_MENU           = 5;

    public static final int    PARAM_LCW          = 6;

    public static final int    PARAM_FAV          = 7;

    public static final int    PARAM_NULL         = 8;

    public static final int    FOCUS_FIRST        = 9;

    public static final int    FOCUS_VOD_CATEGORY = 10;

    public static final int    FOCUS_VOD_HELP     = 11;

    public static final int    FOCUS_NULL         = 12;
    
    public static final int    EPG_SEARCHRESULT   = 13 ;
    
    public static final int VOD_STOPPED = 1;

    public static final int VOD_ERROR = 2;

    public static final int VOD_CONFIRM_OK = 1 ;
    
    public static final int VOD_CONFIRM_CANCEL = 0 ;
    
    //public static final int VOD_CONFIRM_BACK = 3 ;
    
    public static final String RMI_APP_NAME      = "NDSEPGXLET";

    /**
     * Description: Tunes to the given sourceID and changes the state specified
     * (MOSAIC,MINIEPG,SETTINGS)
     * 
     * @param sourceid -
     *            service source id to which PEG shal tune to
     * @param state -
     *            Dictates the state of EPG
     */
    public void EPG_changeState(int state,int sourceid ) throws RemoteException;
    /**New State
     * TV Watching Mode
            EPG plays general video, audio, PPD channels with MiniEPG
            EPG works with monitor application
            EPG handles hot keys, general navigation keys
       VOD Watching Mode
            VOD plays RVOD, SVOD contents with video controller
            VOD handles hot keys, general navigation keys
        EPG Menu Mode
            EPG displays 1st depth menu on AV background
            EPG handles hot keys, general navigation keys
        VOD Menu Mode
            VOD displays 2nd depth menu or purchase screen
            VOD handles general navigation keys but EPG handles hot keys
     * @param newState
     * @param param
     * @throws RemoteException
     */
    
    public void EPG_VOD_changeState(int newState, int param, int focus) throws RemoteException;

    /*
     * EPG will show the 1st depth menu upon VOD request
     * VOD should call this API only in VOD_MENU_MODE
     *  This doesn�t involve any state change
     */
   public void showEpgMenu() throws RemoteException;
   
   /*
    * EPG will hide the 1st depth menu upon VOD request
    * VOD should call this API only in VOD_MENU_MODE
    * This doesn�t involve any state change
    */
   public void hideEpgMenu() throws RemoteException;
   
   
    
    /**
     * 
     * 
     * @return int - State of EPG
     */
    public int EPG_getState() throws RemoteException;

    /**
     * Return's the caller true, if a package ID passed is Subscribed
     * 
     * @author bopannak TODO To change the template for this generated type
     *         comment go to Window - Preferences - Java - Code Style - Code
     *         Templates
     */
    public boolean isPackageSubscribed(int packageID) throws RemoteException;

    /**
     * Return's the current Parental Rating Value on the Smart Card
     * 
     * @return String 7 = 7 yrs 12 = 12 yrs 15 = 15 yrs 19 = 19 yrs 0 = No Limit
     */
    public String getCurrentParentalRating() throws RemoteException;

    /**
     * If SmartCard is ready, it returns SmartCard ID If not, it returns null
     * 
     * @return String
     * @throws RemoteException
     */
    public String getSmartCardID() throws RemoteException;

    /**
     * If SmartCard is ready, it returns Subscriber ID If not, it returns null
     * 
     * @return String
     * @throws RemoteException
     */
    public String getSubscriberID() throws RemoteException;
    
    /**
     * If SmartCard is ready, it returns CAS ID If not, it returns null
     * 
     * @return CASID
     * @throws RemoteException
     */
    public String getCASID() throws RemoteException;
    
    /**
     * If SmartCard is ready, it returns Super CAS ID If not, it returns null
     * 
     * @return SuperCASID
     * @throws RemoteException
     */
    public String getSuperCASID() throws RemoteException;

    /**
     * If SmartCard is ready, it returns 32 bytes region block If not, it
     * returns null
     * 
     * @return String
     * @throws RemoteException
     */
    public byte[] getRegionBits() throws RemoteException;

    /**
     * It returns specified range of region block.
     * see public byte[] getRegionBits() for more details
     * 
     * @return byte[]
     * @throws RemoteException
     */
    public byte[] getRegionBits(int from, int to) throws RemoteException;
    
    /**
     * If SmartCard is ready, it returns SO number as following 양천=43,
     * 북인천=46, 중부산=45, 경남=44, 가야=40,
     * 마산=41, 아름=47, 푸른=48, 남인천=49 If not, it returns
     * 0
     * 
     * @return int
     * @throws RemoteException
     */
    public int getSoNumber() throws RemoteException;
    
    
    /**
     * check pin code
     * 
     * @return true-if pin is correct. otherwise, return false
     * @throws RemoteException
     */
    public boolean checkPinCode(int pin) throws RemoteException;
    
    /**
     * To load the default iFrame downloaded by EPG.
     *
     */
    public void displayLoadingIframe() throws RemoteException;
    
    /**
     * Register for state change events through this listener
     */
    
    public void addStateChangeListener(EPGAppStateNotificationListener listener) throws RemoteException;
    
    /**
     * UnRegister for state change events through this listener
     */
    
    public void removeStateChangeListener(EPGAppStateNotificationListener listener) throws RemoteException;
    
    /**
     * VOD will call this method to return thier search retuls to EPG
     * @param resultList
     * @throws RemoteException
     */
    public void returnVODSearchResult(SearchResultModel[] resultList) throws RemoteException;
    
    /**
     * VOD will call this method to go back to the search result page in EPG
     * @param assetID
     * @throws RemoteException
     */
    public void requestShowVODResultPage(String assetID) throws RemoteException ;
    
    
    /**
     * VOD will call this API to go back to EPG from Folder view or asset view
     */
    public void backFromiAD() throws RemoteException ;
    
    
    public void confirmVODStop(int parameter) throws RemoteException;

    public void notifyVODevent(int vodEvent) throws RemoteException;
    
    
    /**
     * 3 below methods(getChannelStatus, addChannelListener, removeChannelListener) are for Daum Search
     */
    
    /**
     * 
     * @return 
     * 0 : A/V OK
     * 1 : user blocked
     * 2 : parental rating blocked
     * 3 : unsubscribed blocked
     */
    public int getChannelStatus() throws RemoteException;
    
    /**
     * 
     * @author johnlim
     * @param listener
     * @throws RemoteException
     * @author johnlim
     */
    public void addChannelStatusListener(ChannelStatusListener listener)throws RemoteException;
    
    
    /**
     * 
     * @author johnlim
     * @param listener
     * @throws RemoteException
     */    
    public void removeChannelStatusListener (ChannelStatusListener listener)throws RemoteException;

}
