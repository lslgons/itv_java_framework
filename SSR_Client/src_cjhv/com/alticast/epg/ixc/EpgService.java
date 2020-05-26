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
 * <code>EpgService</code>
 * 
 * @version e391fdcc93bcd5a3dac615ec602704b8458ac9c7
 * @date 207/08/04
 * @author tklee
 * @since 2014. 9. 24.
 */
public interface EpgService extends Remote {
    /**
     * Smart Card ID 의 변경 여부를 notify 받고자 할때 사용하는 mask 값.<br>
     */
    public static final int MASK_CARD_ID = 0x01;
    /**
     * Subscriber ID 의 변경 여부를 notify 받고자 할때 사용하는 mask 값.<br>
     */
    public static final int MASK_SUBS_ID = 0x02;
    /**
     * SO Code 의 변경 여부를 notify 받고자 할때 사용하는 mask 값.<br>
     */
    public static final int MASK_SO_CODE = 0x04;
    /**
     * Region Bytes 의 변경 여부를 notify 받고자 할때 사용하는 mask 값.<br>
     */
    public static final int MASK_REGION_BYTE = 0x08;
    /**
     * CAS ID 의 변경 여부를 notify 받고자 할때 사용하는 mask 값.<br>
     */
    public static final int MASK_CAS_ID = 0x10;
    /**
     * 시청연령제한 설정값의 변경 여부를 notify 받고자 할때 사용하는 mask 값.<br>
     */
    public static final int MASK_PR = 0x20;
    
    /**
     * Cug Bytes 의 변경 여부를 notify 받고자 할때 사용하는 mask 값.<br>
     */
    public static final int MASK_CUG_GROUPIDS = 0x11;

    /**
     * Standby, 독립형 어플실행중에 해당하는 상태.<br>
     */
    public static final int EPG_PAUSE = 1;
    /**
     * 채널시정중인 상태.<br>
     */
    public static final int EPG_TV_WATCHING = 2;
    /**
     * VOD시청중인 상태.<br>
     */
    public static final int VOD_WATCHING = 3;
    /**
     * EPG 홈메뉴 실행중인 상태.<br>
     */
    public static final int EPG_MENU = 4;
    /**
     * VOD 홈메뉴 실행중인 상태.<br>
     */
    public static final int VOD_MENU = 5;

    /**
     * EPG Organization ID.<br>
     */
    public static final String EPG_OID = "10000000";
    /**
     * EPG Application ID.<br>
     */
    public static final String EPG_APP_ID = "3030";
    /**
     * EPG RemoteApplicationName.
     */
    public static final String RMI_APP_NAME = "EpgService";

    /**
     * SO를 구분하기 위해 사용하는 LVCT 로 전달되는 ID 값.<br>
     */
    public int mapId = -1;

    /** The Constant MAP_ID_NAME. */
    public static final String[] MAP_ID_NAME = { "양천", // 0
        "북인천", // 1
        "해운대", // 2
        "중부산", // 3
        "경남", // 4
        "가야", // 5
        "아름", // 6
        "푸른", // 7
        "남인천", // 8
        "중앙", // 9
        "금정", // 10
        "영남", // 11
        "충남", // 12
        "부천", // 13
        "은평", // 14
        "대구", // 15
        "영동", // 16
        "아라", // 17
        "신라", // 18
    };

    /**
     * EPG 의 상태 전환을 요청한다. Monitor App 만 사용해야 한다.<br>
     * 
     * @param state
     *            전환해야할 state
     * @param sourceid
     *            state 전환시 채널 전환이 필요한 경우, 해당 채널의 source ID. 채널 전환이 필요 없을 경우, 0
     * @throws RemoteException
     */
    public void EPG_changeState(int state, int sourceid) throws RemoteException;

    /**
     * EPG의 현재 state 를 얻어온다.<br>
     */
    public int EPG_getState() throws RemoteException;

    /**
     * 특정 package 에 대한 가입 여부를 확인 한다.<br>
     * 
     * @param packageID
     * @return 가입되어 있는 경우 true
     * @throws RemoteException
     */
    public boolean isPackageSubscribed(int packageID) throws RemoteException;

    /**
     * 현재 셋팅된 시청연령 제한 값을 확인 한다.<br>
     * 
     * @return "0" - All<br>
     *         "7" - 7세<br>
     *         "12" - 12세<br>
     *         "15" - 15세<br>
     *         "19" - 19세<br>
     * @throws RemoteException
     */
    public String getCurrentParentalRating() throws RemoteException;

    /**
     * Smart Card ID 를 리턴한다.<br>
     * 0, null, 혹은 음수인 경우는 사용해서는 안된다.<br>
     * 
     * @return
     * @throws RemoteException
     */
    public String getSmartCardID() throws RemoteException;

    /**
     * Subscriber ID 를 리턴한다.<br>
     * 0, null, 혹은 음수인 경우는 사용해서는 안된다.<br>
     * 
     * @return
     * @throws RemoteException
     */
    public String getSubscriberID() throws RemoteException;

    /**
     * CAS ID 를 리턴한다.<br>
     * 0, null, 혹은 음수인 경우는 사용해서는 안된다.<br>
     * 
     * @return
     * @throws RemoteException
     */
    public String getCASID() throws RemoteException;

    /**
     * Super CAS ID 를 리턴한다.<br>
     * 0, null, 혹은 음수인 경우는 사용해서는 안된다.<br>
     * 
     * @return
     * @throws RemoteException
     */
    public String getSuperCASID() throws RemoteException;

    /**
     * Region Bytes 를 리턴한다.<br>
     * 
     * @return
     * @throws RemoteException
     */
    public byte[] getRegionBits() throws RemoteException;

    /**
     * 특정 범위의 Region Bytes 를 리턴한다.<br>
     * 
     * @param from
     *            시작 index
     * @param to
     *            끝 index
     * @return
     * @throws RemoteException
     */
    public byte[] getRegionBits(int from, int to) throws RemoteException;

    /**
     * SO Code 를 리턴한다.<br>
     * 0, 혹은 음수인 경우는 사용해서는 안된다.<br>
     * 
     * @return 가야: 40<br>
     *         경남/마산: 41<br>
     *         강원: 42<br>
     *         양천: 43<br>
     *         중부산: 44<br>
     *         해운대: 45<br>
     *         북인천: 46<br>
     *         푸른:48<br>
     *         남인천: 49<br>
     *         영남: 50<br>
     *         충남: 51<br>
     *         중앙: 52<br>
     *         금정: 53<br>
     *         부천/김포: 54<br>
     *         은평: 55<br>
     *         영동: 56<br>
     *         동구/수성(대구): 57<br>
     *         아라(순천): 58<br>
     *         신라: 59<br>
     *         나라: 61<br>
     *         영서: 62<br>
     *         전북: 63<br>
     *         호남: 64<br>
     * @throws RemoteException
     */
    public int getSoNumber() throws RemoteException;

    /**
     * 비밀번호가 정확한지 확인한다.<br>
     * 단, 사용자가 입력한 비밀번호가 4자리 인지는 개별 어플에서 미리 체크해야 한다. ("0" 과 "0000" 모두 integer 로는 '0' 이지만, "0"은 올바른 비밀번호가 아니기 때문)<br>
     * 
     * @param pin
     * @return
     * @throws RemoteException
     */
    public boolean checkPinCode(int pin) throws RemoteException;

    /**
     * 구매비밀번호가 정확한지 확인한다.<br>
     * 단, 사용자가 입력한 비밀번호가 4자리 인지는 개별 어플에서 미리 체크해야 한다. ("0" 과 "0000" 모두 integer 로는 '0' 이지만, "0"은 올바른 비밀번호가 아니기 때문)<br>
     * 
     * @param pin
     * @return
     * @throws RemoteException
     */
    public boolean checkPurchasePinCode(int pin) throws RemoteException;

    /**
     * {@link CardInfoUpdateListener}를 등록 한다.<br>
     * Listener 를 등록한 어플은, 종료될때 반드시 {@link #removeCardInfoUpdateLIstener(CardInfoUpdateListener)}를 호출해야 한다.<br>
     * 
     * @param listener
     *            등록할 listener
     * @param mask
     *            notify 받고자 하는 정보의 mask 값. {@link #MASK_CARD_ID} | {@link #MASK_SUBS_ID} | {@link #MASK_SO_CODE} | {@link #MASK_REGION_BYTE} | {@link #MASK_CAS_ID} | {@link #MASK_PR}
     * @throws RemoteException
     */
    public void addCardInfoUpdateListener(CardInfoUpdateListener listener, int mask) throws RemoteException;

    /**
     * {@link CardInfoUpdateListener}를 제거 한다.<br>
     * {@link #addCardInfoUpdateListener(CardInfoUpdateListener, int)}로 listener 를 등록한 어플은, 종료될때 반드시 호출해야 한다.<br>
     * 
     * @param listener
     *            제거할 listener
     * @throws RemoteException
     */
    public void removeCardInfoUpdateLIstener(CardInfoUpdateListener listener) throws RemoteException;

    /**
     * Display iTV I-Frame which is stored at FFS. Must be called by only Monitor Application.<br>
     * 
     * @throws RemoteException
     */
    public void displayLoadingIframe() throws RemoteException;

    /**
     * {@link EPGAppStateNotificationListener}를 등록 한다.<br>
     */
    public void addStateChangeListener(EPGAppStateNotificationListener listener) throws RemoteException;

    /**
     * {@link EPGAppStateNotificationListener}를 제거 한다.<br>
     */
    public void removeStateChangeListener(EPGAppStateNotificationListener listener) throws RemoteException;

    /**
     * EPG가 LVCT에서 parsing 한 MAP ID 를 얻어온다.<br>
     * EPG도 MAP ID를 모를 경우(section filter 아직 올라오지 않은 경우나, parsing 시 에러가 난 경우)는 -1을 리턴한다.<br>
     * 리턴값이 -1이면, 추후에 다시 물어봐야 한다.<br>
     * 
     * @return
     * @throws RemoteException
     */
    public int getMapId() throws RemoteException;

    /**
     * cas에서 수신한 cug groud id를 전달한다
     * @return cugGroupIds
     * @throws RemoteException
     */
    public String getCugGroupIds() throws RemoteException;
    
    
    /**
     * EPG 버전 정보를 전달한다
     * 버전 형식 - major.minor.revision
     * 
     * Alaska EPG인경우 EPG 버전 전달. Alaska EPG가 아닌경우, 본 method 가 지원되지 않기 때문에 NoSuchMethodError 를 catch 하여야 함.
     * 
     * @return
     * @throws RemoteException
     */
    public String getVersion() throws RemoteException;
    
    //=============================================================================
    /**
     * node group id를 전달한다
     * 
     * @return node group id
     * @throws RemoteException
     */
    public int getNodeGroupId()  throws RemoteException;
    
    
	/**
	 * node group id를 설정한다
	 * 
	 * @param id
	 * @throws RemoteException
	 */
    public void setNodeGroupId(int id) throws RemoteException;
    
    
}

