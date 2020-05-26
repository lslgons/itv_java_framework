/* =================================================================
 * Copyright (c) 2006 -2010: NDS Ltd.
 * P R O P R I E T A R Y C O N F I D E N T I A L
 * The copyright of this document is vested in NDS Ltd. without
 * whose prior written permission its contents must not be published,adapted
 * or reproduced in any form or disclosed or issued to any third party.
 * =================================================================
 */
package com.alticast.epg.ixc;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * EPG - VOD 간 IXC API를 정의 하고 있다.<br>
 */
public interface VodBaseApi extends Remote {
	/**
	 * <code>lookup</code> 시 사용할 remote object name.<br>
	 */
	public static final String REMOTE_OBJECT_NAME = "VodBaseApi";

	/**
	 * 최신영화관 메뉴에서 EPG홈메뉴 1depth(focus 는 최신영화관에)로 돌아가도록 요청.<br>
	 * 
	 * @throws RemoteException
	 */
	public void backToNewVod() throws RemoteException;

	/**
	 * VOD 다시보기 메뉴에서 EPG홈메뉴 1depth(focus 는 VOD 다시보기)로 돌아가도록 요청.<br>
	 * 
	 * @throws RemoteException
	 */
	public void backToVodMenu() throws RemoteException;

	/**
	 * VOD 마이컨텐츠 메뉴에서 EPG홈메뉴 1depth(focus 는 마이컨텐츠)로 돌아가도록 요청.<br>
	 * 
	 * @throws RemoteException
	 */
	public void backToMyConent() throws RemoteException;

	/**
	 * 이용안내 메뉴에서 EPG홈메뉴 안내 및 설정 2depth(focus 는 이용안내)로 돌아가도록 요청.<br>
	 * 
	 * @throws RemoteException
	 */
	public void backToHelpMenu() throws RemoteException;

	/**
     * VOD 채널 컨텐츠 상세정보에서 VOD 채널 영화선택 화면으로 돌아가도록 요청.<br>
     * VOD 채널 영화 선택 화면(EPG)에서 사용자가 영화를 선택하여 상세화면(VOD)으로 이동한 다음, 이전 혹은 종료 키등에 의해 상세화면이 종료되는 경우 호출한다.<br>
     * 이때, EPG는 영화 선택 화면에서 assetID 의 포스터에 포커스를 표시 한다.<br>
     * 
     * @param assetId
     *            상세정보가 표시된 VOD asset ID
     * @throws RemoteException
     */
	public void backToVodChannel(String assetId) throws RemoteException;

	/**
     * 검색결과에서 VOD로 간다음, 되돌아 올때 호출함.
     * 
     * @throws RemoteException
     */
	public void backToSearchMenu() throws RemoteException;

	/**
     * 데쉬보드-보유쿠폰목록박스에서 VOD로 간다음, 되돌아 올때 호출함.
     * 
     * @throws RemoteException
     */
	public void backToDashBoard() throws RemoteException;

	/**
	* 우리동네VOD 메뉴에서 EPG홈메뉴 1depth(focus 는 우리동네VOD 메뉴에)로 돌아가도록 요청.<br>
	*/
	public void backToMyTownMenu() throws RemoteException;

	/**
	 * VOD시청중, 좌 방향 키를 눌러 즐겨찾기 퀵메뉴를 띄워야 할때 호출한다.<br>
	 * 
	 * @throws RemoteException
	 */
	public void launchFavoriteMenu() throws RemoteException;

	/**
	 * VOD시청중, 상 방향 키를 눌러 검색 트리거를 띄워야 할때 호출한다.<br>
	 * 
	 * @throws RemoteException
	 */
	public void launchSearchMenu() throws RemoteException;

	/**
	 * VOD시청중/VOD메뉴에서 검색어입력 팝업을 띄워야 할때 호출한다.<br>
	 * 
	 * @throws RemoteException
	 */
	public void launchSearchInputPopup() throws RemoteException;

	/**
	 * VOD시청중/VOD메뉴에서 검색결과 팝업을 띄워야 할때 호출한다.<br>
	 * 
	 * @throws RemoteException
	 */
	public void launchSearchResultPopup(String keyword) throws RemoteException;

	/**
	 * VOD시청중, 하 방향 키를 눌러 인기컨텐츠 트리거를 띄워야 할때 호출한다.<br>
	 * 
	 * @throws RemoteException
	 */
	public void launchPopularContent() throws RemoteException;

	/**
	 * VOD 메뉴에서 쿠폰메뉴(쿠폰샵)으로 이동할때 호출한다.<br>
	 * 
	 * @throws RemoteException
	 */
	public void launchCouponMenu() throws RemoteException;

	/**
	 * VOD 메뉴(마이컨텐츠) 에서 정기구매 메뉴로 이동할때 호출한다.<br>
	 * 
	 * @throws RemoteException
	 */
	public void launchVipCoinMenu() throws RemoteException;

	/**
	 *  VOD 메뉴(공지사항) 에서 비밀번호설정 팝업으로 이동할때 호출한다.<br>
	 * 
	 * @throws RemoteException
	 */
	public void launchPinChangePopup() throws RemoteException;

	/**
     * EPG가 요청한 VOD play 종료 요청에 대한 처리 결과를 리턴한다.<br>
     * 
     * @param stop
     *            VOD가 정상 종료 되는 경우 true, 사용자가 종료확인을 취소한 경우 false
     * @throws RemoteException
     */
	public void notifyVodPlayStop(boolean stop) throws RemoteException;

	/**
     * 다른 어플의 요청에 의해 VOD 화면이 뜨거나, 해당 화면이 사라질때 호출한다.<br>
     * 
     * @param visible
     *            true: 다른 어플의 요청에 의해 VOD 화면이 뜰때<br>
     *            false: 다른 어플의 요청에 의해 뜬 VOD화면이 사라질때<br>
     * @throws RemoteException
     */
	public void notifyVodLaunching(boolean visible) throws RemoteException;

	/**
     * AV상태의 전환을 EPG에게 알려준다.<br>
     * 
     * @param play
     *            true : AV -> VOD Watching 으로 가는 경우<br>
     *            false : VOD Watching -> AV 로 가는 경우
     * @throws RemoteException
     */
	public void notifyVodWatching(boolean play) throws RemoteException;

	/**
     * EPG에서 특정 VOD 링크로 이동한 다음, 다시 이전 상태로 되돌아 갈때 호출한다.<br>
     * 다만, 이용안내, 검색결과 등, 해당 링크를 위한 전용 API가 있을 경우는 거기에 해당하는 backToXXXX()를 사용하여야 한다.<br>
     * 
     * @param epgStateId
     *            EPG가 VOD 링크를 요청할 당시 넘겨준 id 값
     * @throws RemoteException
     */
	public void backToEpgState(int epgStateId) throws RemoteException;

	/**
     * VOD 에서 쿠폰이 사용된 경우, 호출된다.<br>
     * EPG 는 자체적으로 cache 하고 있는 쿠폰 잔액을 지우고, 필요한 경우 쿠폰서버로 부터 다시 받아온다.<br>
     * 
     * @throws RemoteException
     */
	public void invalidateCouponCache() throws RemoteException;
	
	/**
     * VOD 에서 TV팝업을 통해 월정액 상품이 구매된 경우, 호출된다.<br>
     * EPG 는 서버로 부터 관련 정보를 다시 받아온다.<br>
     * 
     * @throws RemoteException
     */
	public void invalidateBonusSubscription() throws RemoteException;

}
