package com.landman.platform.dmc.interfaces;



/**
 * VOD 재생 인터페이스
 *
 * <pre>
 * 플렛폼 별로 VOD재생을 위한 Class는 cjtosplus.app.vod.VODManagerImpl Class를 생성하여
 * VODManager를 Implements 해야함.
 * VODManagerImpl Class는 Singleton으로 생성
 *
 * VODManagerImpl에서는 실제 플렛폼별 VOD 재생기능 및 Player관련 이벤트처리를 구현하고
 * 실제 화면UI에서는 VODManager에 정의된 메소드들을 이용하여 VOD컨트롤함.
 *
 * 플렛폼별 VOD 이벤트는 VODManagerImpl에서 재가공하여 VODEventListener로 등록된 화면UI로 전달함.
 * </pre>
 */
public interface VODInterface {
	/**
	 * VOD이벤트 리스너 interface
	 * @author mutant97
	 *
	 */
	
	public interface VODEventListener {
		/**
		 * 배속 변경시 처리할 작업 수행
		 * @param rate
		 */
		public void rateChangeEvent(float rate);

		/**
		 * VOD player의 종료 상태시 처리할 작업 수행
		 */
		public void vodPlayerEndEvent();

		/**
		 * VOD player의 상태를 전달받아 작업 수행
		 *
		 * @param played
		 *            true면 vod play중, false면 그 반대
		 * @param exception
		 *            true면 에러, false면 그 반대
		 * @param message
		 *            exception이 true일 경우 에러 메시지
		 */
		public void vodPlayerState(boolean played, boolean exception, String message);

		/**
		 * VOD player의 STOP 상태시 처리할 작업 수행
		 * @param assetid
		 */
		public void vodPlayerStopEvent(String assetid);
	}
	
	
	
	/** 재생중 */
	public static final int VOD_PLAY = 1;
	/** 일시정지 */
	public static final int VOD_PAUSE = 0;
	/** 정지 */
	public static final int VOD_STOP = -1;
	/** 되감기 */
	public static final int VOD_REW = 2;
	/** 빨리감기 */
	public static final int VOD_FF = -2;

	public static final String VOD_ERROR_1 = "authorizeViewX responseParam IS NULL!!!";
	public static final String VOD_ERROR_2 = "unicastService IS NULL!!!";
	public static final String VOD_ERROR_0 = "VOD Stopped cause unexpected error";
	public static final String VOD_ERROR_3 = "error while preparing skylife vod service";
	
	/** Event Process Type **/
	public static final int EVENT_CLOSE = 0;
	public static final int EVENT_RETRY = 1;
	public static final int EVENT_POPUP = 2;
	
	
	/**
	 * VOD 재생요청
	 * @param assetId
	 */
	public void startVOD(String assetId);

	/**
	 * 재생중인 VOD를 종료하고 리소스를 정리.
	 */
	public void destroyVOD();

	
	
	/**
	 * 리모컨 VOD키 이벤트핸들러
	 */
	public void vodKeyHandler(int keyCode);

	/**
	 * VOD 전체 재생시간 반환
	 * @return 초단위
	 */
	public int getTotPlayTime();

	/**
	 * VOD 전체 재생시간 반환
	 * @return 1/1000 초 단위
	 */
	public long getTotLongPlayTime();

	/**
	 * VOD 현재 재생 시간 반환
	 * @return 초단위
	 */
	public int getCutPlayTime();

	/**
	 * VOD 현재 재생 시간 반환
	 * @return 1/1000 초단위
	 */
	public long getCurLongPlayTime();

	/**
	 * VOD 현재 재생상태 반환
	 * @return
	 */
	public int getPalyStatus();

	/**
	 * VOD 현재 재생속도 반환
	 * @return
	 */
	public float getRate();

	/**
	 * VOD 이벤트 수신 리스너 등록
	 * @param listener
	 */
	public void addVodEventListener(VODEventListener listener);

	/**
	 * VOD 이벤트 수신 리스너 해제
	 * @param listener
	 */
	public void removeVodEventListener(VODEventListener listener);

	public void destroy();

	/**
	 * 재생중인 VOD를 중지하고 관련 리소스 정리
	 * <pre>
	 * 기본적으로 화면 이동은 하지 않음.
	 * KDMC향 App에서 VOD재생 중 핫키로 VOD가 종료 되는 경우 정상적으로 리소스 정리가 되지 않는 현상때문에 별도 추가
	 * 타 플렛폼에서는 필요에따라 구현
	 * </pre>
	 */
	public void onlyVODStop();

	/**
	 * 현재 배속설정과 상관없이 강제로 pause 처리
	 */
	public void forcedVODPause();
}
