package com.tcom.platform.dmc.cjhv;

import java.awt.*;
import java.rmi.RemoteException;

import com.alticast.navsuite.core.ChannelController;
import com.alticast.navsuite.core.cj.CjChannelDatabase;
import com.castis.vod.control.VODEventListener;
import com.castis.vod.control.VODPlayer;
import com.tcom.platform.controller.KeyCode;
import com.tcom.platform.dmc.interfaces.AVInterface;
import com.tcom.platform.dmc.interfaces.VODInterface;
import com.tcom.ssr.SSRConfig;
import com.tcom.util.LOG;
import org.dvb.io.ixc.IxcRegistry;
import org.ocap.net.OcapLocator;

import javax.tv.service.selection.InsufficientResourcesException;
import javax.tv.service.selection.ServiceContext;
import javax.tv.service.selection.ServiceContextFactory;
import javax.tv.util.*;

public class AV implements AVInterface, VODInterface, VODEventListener {

	private int fx, fy, fw, fh;
	private java.awt.Rectangle fullRec, curRec;

	public AV() {
		javax.media.Player player = null;
		javax.tv.service.selection.ServiceContentHandler[] handlers  = null;
		javax.tv.media.AWTVideoSizeControl sizeCntr = null;
		java.awt.Rectangle curRect = null;

		// http://www.interactivetvweb.org/tutorials/ocap/jmf

		handlers  = javax.tv.service.selection.ServiceContextFactory.getInstance().getServiceContexts()[0].getServiceContentHandlers();
		LOG.print(this, "resizeAV - Rectangle : handlers.length " + handlers.length );
		for(int i=0; i < handlers.length ; i++) {
			if (handlers[i] instanceof javax.tv.service.selection.ServiceMediaHandler) {
				player = (javax.media.Player) handlers[i];
				LOG.print(this, "resizeAV - Rectangle : handlers["+i+ "] "+ handlers[i] );
			}
		}

		if(player == null){
			LOG.print(this, "resizeAV - Rectangle : player = null " + (player == null) );
			return;
		}

		//set the video size  switching full screen & pip screen
		sizeCntr = ( javax.tv.media.AWTVideoSizeControl ) player.getControl( "javax.tv.media.AWTVideoSizeControl" );
		curRect = new java.awt.Rectangle( sizeCntr.getSourceVideoSize() ); // 소스 비디오 사이즈

		this.fx = 0;
		this.fy = 0;
		this.fw = curRect.width;
		this.fh = curRect.height;

		LOG.print(this, "============================================");
		LOG.print(this, "Current Rect : "+ curRect.width+", "+curRect.height);
		LOG.print(this, "============================================");



		this.fullRec = new java.awt.Rectangle(this.fx, this.fy, this.fw, this.fh);
		this.curRec = null;


	}


	public void resetVideoSize() {
		changeVideoSize(fullRec);
	}

	public void changeVideoSize(Rectangle rect) {
		//CJHV는 960X540기준 사이즈 조절하므로 변환X
		LOG.print(this, "resizeAV - to : " + rect);
//		if (this.curMode == sizeMode) return;
		this.curRec=rect;


		javax.tv.service.selection.ServiceContext sc = null;
		javax.media.Player player = null;
		javax.tv.service.selection.ServiceContentHandler[] handlers  = null;
		javax.tv.media.AWTVideoSizeControl sizeCntr = null;
		java.awt.Rectangle NewBound = null;
		java.awt.Rectangle rec = null;
		javax.tv.media.AWTVideoSize VideoSize = null;
		javax.tv.media.AWTVideoSize CheckedSize = null;
		javax.tv.media.AWTVideoSize CurBound = null;
		try {

			sc = javax.tv.service.selection.ServiceContextFactory.getInstance().getServiceContexts()[0];

			// http://www.interactivetvweb.org/tutorials/ocap/jmf

			handlers  = sc.getServiceContentHandlers();
			LOG.print(this, "resizeAV - Rectangle : handlers.length " + handlers.length );
			for(int i=0; i < handlers.length ; i++) {
				if (handlers[i] instanceof javax.tv.service.selection.ServiceMediaHandler) {
					player = (javax.media.Player) handlers[i];
					LOG.print(this, "resizeAV - Rectangle : handlers["+i+ "] "+ handlers[i] );
				}
			}

			if(player == null){
				LOG.print(this, "resizeAV - Rectangle : player = null " + (player == null) );
				return;
			}


			sizeCntr = ( javax.tv.media.AWTVideoSizeControl ) player.getControl( "javax.tv.media.AWTVideoSizeControl" );
			NewBound = new java.awt.Rectangle( sizeCntr.getSourceVideoSize() ); // 소스 비디오 사이즈
			VideoSize = new javax.tv.media.AWTVideoSize( NewBound,  this.curRec); // 소스 비디오 사이즈를 풀 사이즈로 맞추려면 몇배로 늘려야하는가

//			Rs.print(this, "resizeAV - rec(curMode) width : " + rec.width + " / rec height : " + rec.height); // 풀 사이즈
//			Rs.print(this, "resizeAV - NewBound(SourceVideoSize) width : " + NewBound.width + " / NewBound height : " + NewBound.height); // 소스 비디오 사이즈
//			Rs.print(this, "resizeAV - VideoSize.getXScale : " + VideoSize.getXScale() + " / VideoSize.getYScale : " + VideoSize.getYScale()); // 소스 비디오 사이즈를 풀 사이즈로 맞추려면 몇배로 늘려야하는가
//			Rs.print(this, "resizeAV - VideoSize.width : " + VideoSize.getDestination().width + " / VideoSize.height : " + VideoSize.getDestination().height);

			if (curRec.width != 0 && curRec.height != 0) {
				LOG.print(this, "resizeAV -  : 1- 0 " );
				CheckedSize = sizeCntr.checkSize( VideoSize ); // 하드웨어가 지원하는 가장 근접한 사이즈 반환 VideoSize -> CheckedSize
				CurBound = sizeCntr.getSize(); // 현재 사이즈일거 같고
//				Rs.print(this, "resizeAV - CurBound width : " + CurBound.getDestination().width + " / CurBound height : " + CurBound.getDestination().height );
//				Rs.print(this, "resizeAV - CheckedSize width : " + CheckedSize.getDestination().width + " / CheckedSize height : " + CheckedSize.getDestination().height );
//				Rs.print(this, "resizeAV - CurBound.getXScale() : " + CurBound.getXScale() + " / CurBound.getYScale() : " + CurBound.getYScale() );
//				Rs.print(this, "resizeAV - CheckedSize.getXScale() : " + CheckedSize.getXScale() + " / CheckedSize.getYScale() : " + CheckedSize.getYScale() );
//				Rs.print(this, "resizeAV - !CurBound.equals( CheckedSize ) : " + !CurBound.equals( CheckedSize ));

				if ( !CurBound.equals( CheckedSize ) ) {
					LOG.print(this, "resizeAV -  : 1- 1 " );
					sizeCntr.setSize( CheckedSize );
				}
			} else {
				LOG.print(this, "resizeAV -  : 2- 0 " );
				sizeCntr.setSize( VideoSize );
			}

		} catch (Exception e) {
			LOG.print(this, "resizeAV Error :: " + e.getMessage());
		} finally {
			sc = null;
			player = null;
			if(handlers  != null){
				for(int i=0; i < handlers.length ; i++) {
					handlers[i] = null;
				}
				handlers  = null;
			}
			sizeCntr = null;
			NewBound = null;
			rec = null;
			VideoSize = null;
			CheckedSize = null;
			CurBound = null;
		}
		
	}



	public void turnOnVideo() {
		LOG.print(this, "Not Implemented");
	}

	public void turnOffVideo() {
		LOG.print(this, "Not Impleented");
	}

	public Rectangle getCurrentVideoSize() {
		return this.curRec;
	}


	//VOD Implements
	Rectangle vodRec=null;
	public static int VOD_STATUS = VOD_STOP;
	/**
	 * VOD assetId
	 */
	private String assetId;
	/**
	 * 반복재생 여부
	 */
	private boolean loop = false;
	public static int nodeGroupID = -1;

	public boolean isOpen = false;

	private VODPlayer vodPlayer = null;
	private VODEventListener listener = null;

	private ServiceContext serviceContext = null;

	/** play is Committing */
	protected boolean isCommitting = false;

	/** vod play 실패 여부 */
	protected boolean isVodFail = false;

	/**  */
	private static boolean isRetry = false;

	/**
	 * VOD 일시정지
	 */
	private int old_rate;
	private int old_status;

	// 전체 파일사이즈
	private long totalFileSize = 0;
	// 현재 파일 사이즈
	private long curFileSize = 0;
	// 전체 재생시간
	private long totalDurationTime = 0;

//	public int emulTime = 0;

	public static final Object lock = new Object();

	final boolean IS_TOGGLE_PLAY_PAUSE=true;

	/**
	 * VOD Rate
	 */
	private int rate;
	/**
	 * VOD 배속 배열
	 * com.castis.vod.control.VODPlayer setRate()
	 */
	private int[] rateArray =  { -64 , -32 , -16 , -8 , -4 , -2 , 1 , 2 , 4 , 8 , 16 , 32 , 64 };

	private double[]  mediaRatios = { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9 };

	/**
	 * 이전/다음 트랙 시 건너뛰는 시간(초단위)
	 */
	private int mediaTimeInterval = 15; //15 초

	/** ProgressBar Timer */
	protected TVTimerSpec tvTimerSpec = null;
	protected boolean bScheduled = false;
	protected ProgressTimerListener gListener = null;
	protected int BAR_REGULAR_TIME = 1000;
	private long secPerSize = 0;
	int progressIndex = 0;

	public void initVOD() {
		//VOD Constructor
		VOD_STATUS = VOD_STOP;
		this.isCommitting = false;

		this.tvTimerSpec = new TVTimerSpec();
		this.tvTimerSpec.setAbsolute(true);
		this.tvTimerSpec.setDelayTime(this.BAR_REGULAR_TIME);
		this.tvTimerSpec.setRepeat(true);
		this.gListener = new ProgressTimerListener();
	}

	public void receiveEvent(int vodEvent) throws RemoteException {

	}

	class ProgressTimerListener implements TVTimerWentOffListener {

		public synchronized void timerWentOff(TVTimerWentOffEvent e) {
			if (!AV.this.bScheduled) return;
			if (AV.this.vodPlayer == null || VOD_STATUS == VOD_PAUSE) return;
//			if (totalFileSize == 0)
			AV.this.totalFileSize = getTotalFileSize();
			AV.this.secPerSize = AV.this.totalFileSize / getTotPlayTime();

			try {
				LOG.print(this, "curFileSize : " + AV.this.curFileSize);
				LOG.print(this, "secPerSize : " + AV.this.secPerSize);
//				LOG.print(this, "getTotPlayTime() : " + getVPlayer().getDuration());
				LOG.print(this, "getMediaOffset() : " + getVPlayer().getMediaOffset());
				LOG.print(this, "getMediaRatio() : " + getMediaRatio() );

			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

			if (AV.this.progressIndex == 0) {
				AV.this.curFileSize = getMediaOffset();
			} else {
				if (VOD_STATUS == VOD_PLAY) {
					AV.this.curFileSize = AV.this.curFileSize + AV.this.secPerSize;
				} else {
					AV.this.curFileSize = getMediaOffset();
				}
			}
			if (++AV.this.progressIndex > 5) AV.this.progressIndex = 0;

		}
	}

	/** Runs a checking timer to inspect the current time every second. 진행바*/
	public synchronized void startTimer() {
		this.progressIndex = 0;
		TVTimerSpec actual;
		LOG.print(this, " startTimer bScheduled=" + this.bScheduled);
		if (this.bScheduled) {
			if (this.tvTimerSpec != null) {
				this.tvTimerSpec.removeTVTimerWentOffListener(this.gListener);
				TVTimer.getTimer().deschedule(this.tvTimerSpec);
			}
		}
		LOG.print(this, " tvTimerSpec bScheduled=" + this.tvTimerSpec);
		try {
			this.tvTimerSpec.addTVTimerWentOffListener(this.gListener);
			actual = TVTimer.getTimer().scheduleTimerSpec(this.tvTimerSpec);
			this.tvTimerSpec.setTime(actual.getTime());
			this.bScheduled = true;
		} catch (TVTimerScheduleFailedException e) {
		}
		LOG.print(this, " tvTimerSpec bScheduled222 " + this.tvTimerSpec);
	}

	/** Stops inspecting the current time every second. 진행바*/
	public synchronized void stopTimer() {
		LOG.print(this, " stopTimer bScheduled=" + this.bScheduled);
		if (!this.bScheduled) return;

		this.bScheduled = false;
		if (this.tvTimerSpec != null) {
			this.tvTimerSpec.removeTVTimerWentOffListener(this.gListener);
			TVTimer.getTimer().deschedule(this.tvTimerSpec);
		}
	}


	public void startVOD(String assetId, boolean loop) {
		this.loop=loop;
		LOG.debug(this, "startVOD  VOD_STATUS  ========================> " +  VOD_STATUS );
		LOG.debug(this, "startVOD (VOD_STATUS != S_STOP) ========================> " + (VOD_STATUS != VOD_STOP));
		LOG.debug(this, "startVOD  this.loop  ========================> " +  this.loop );

		if(VOD_STATUS != VOD_STOP) {

			LOG.debug(this, "startVOD  onlyVODStop(false)  ========================  "  );

			onlyVODStop(false); // 스톱을 걸어서 vod 가 종료되면,
		}else{
			initVOD();

			this.assetId = assetId;

			new Thread(new Runnable() {
				public void run() {
					LOG.debug(this, "startVOD  runVOD()  ========================  "  );
					runVOD();
				}
			}).start();
		}
		
	}


	private void runVOD() {
		String fileName = this.assetId+".mpg";

		LOG.debug(this, "================================= this.assetId >>>>>>>>>>>" + this.assetId );
		// 개발
		/**
		 if("CJOS20180710138".equals(this.assetId)) { // 0 번 메뉴

		 fileName= "M4567857.mpg"; // 로건 럭키 영화
		 this.assetId = "cjc|M4567857LSG865041201";
		 LOG.debug(this, "================================= logan lucky");
		 }else if("CJOS20180710238".equals(this.assetId)) { // 1 번 메뉴
		 fileName = "M4571094.mpg"; // 보험 사기 광고
		 this.assetId = "cjc|M4571094LSG870321001";
		 LOG.debug(this, "================================= insurance");
		 }else if("CJOS20180710145".equals(this.assetId)) { //2번 메뉴
		 fileName = "M4571094.mpg"; // 보험 사기 광고
		 this.assetId = "cjc|M4571094LSG870321001";
		 LOG.debug(this, "================================= insurance");
		 }else if("CJOS20180510096".equals(this.assetId)) { // 3번 메뉴
		 fileName= "M0020928.mpg"; // 나루토 애니
		 this.assetId = "cjc|M4562948LFO857277701";
		 LOG.debug(this, "================================= naruto");
		 }else if("".equals(this.assetId)) { // 4번 메뉴
		 // 재생 어셋이 없는거네
		 }else{
		 fileName = "M4571094.mpg"; // 보험 사기 광고
		 this.assetId = "cjc|M4571094LSG870321001";
		 LOG.debug(this, "================================= insurance");
		 }
		 */
		// QA
//		if("CJOS20180610078".equals(this.assetId) || "cjc|M4567857LSG865041201".equals(this.assetId) ) { // [오플] 스틸라 매그니피센트 메탈글리터 리퀴드 아이섀도 2개SET
//			fileName= "M4567857.mpg"; // 로건 럭키 영화
//			this.assetId = "cjc|M4567857LSG865041201";
//			LOG.debug(this, "================================= logan lucky");
//		}else if("CJOS20180410113".equals(this.assetId)  || "cjc|M4562948LFO857277701".equals(this.assetId) ) { // [오플] 한입떡갈비140g 10봉
//			fileName= "M0020928.mpg"; // 나루토 애니
//			this.assetId = "cjc|M4562948LFO857277701";
//			LOG.debug(this, "================================= naruto");
//		}else{
//			fileName = "M4571094.mpg"; // 보험 사기 광고
//			this.assetId = "cjc|M4571094LSG870321001";
//			LOG.debug(this, "================================= insurance");
//		}
		// TODO: 테스트 어셋 끝

		LOG.print(this, "play fileName : " + fileName );

		int returnValue = VODPlayer.VODRESULT_SUCCESS;

		//for CJHV 2015.05.09 : 어플리케이션이 이중으로 로딩되지 않도록 하는 처리
//		System.out.println("###############################################################");
//		System.out.println( "WRITE GLOBAL VALUE: true " );
//		System.out.println("###############################################################");
//		GlobalXletContext.getInstance().setProperty( SSRConfig.getInstance().APP_NAME, "true" );
//

		printStatus(VOD_STATUS);

		synchronized (lock) {
			// 전체 파일사이즈
			this.totalFileSize = 0;
			this.secPerSize = 0;
			// 현재 파일 사이즈
			this.curFileSize = 0;
			// 전체 재생시간
			this.totalDurationTime = 0;

			this.isOpen = false;

//			if (fileName == null || fileName.length() == 0) {
//				notifyVODEvent(false, true, "재생실패");
//				return;
//			}

			setApplicationName(fileName);

			LOG.print(this, "play STATUS "+ VOD_STATUS);

			LOG.print(this, "play:1-1");

			try {

				LOG.print(this, "play:1-2:fileName="+fileName+",nodeGroupID="+nodeGroupID);

				returnValue = getVPlayer().play(fileName, nodeGroupID, 0, 0); //for CJHV 2015.04.12	// 이어보기따윈 없으니 offset 은 0
				printResult(returnValue);
			} catch (RemoteException e) {
				LOG.print(e);
				this.isCommitting = false;
				notifyVODEvent(false, true, "재생오류");
				return;
			}

			if (returnValue == VODPlayer.VODRESULT_SUCCESS) {

				LOG.print(this, "TotalFileSize="+getTotalFileSize()+", TotPlayTime="+getTotPlayTime());

				LOG.print(this, "play:1-13");
				LOG.print(this, "[VODPlayer]thread sleep");

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					this.isCommitting = false;
					LOG.print(this, "[VODPlayer]S_STOP-----VOD_FAIL");
					LOG.print(e);
					notifyVODEvent(false, true, "재생오류");
					return;
				}

				try {
					reserveDevice();
				} catch (Exception e) {
					this.isCommitting = false;
					LOG.print(this, "[VODPlayer]S_STOP-----VOD_FAIL");
					LOG.print(e);
					notifyVODEvent(false, true, "재생오류");
					return;
				}

				VOD_STATUS = VOD_PLAY;
				startTimer();
				this.isOpen = true;
				this.isCommitting = false;
				this.rate = 1;

				LOG.print(this, "TotalFileSize="+getTotalFileSize()+", TotalDuration="+getTotLongPlayTime());

				notifyVODEvent(true, false, null);

				return;
			} else {
				if (returnValue == VODPlayer.VODRESULT_INVALID_FILE) {
					isRetry = true;
				}
				LOG.print(this, "VOD Retry isRetry " + isRetry);

				if (isRetry) {
					this.isCommitting = false;
					isRetry = false;
					notifyVODEvent(false, true, "재생오류");
					return;
				}
			}
		}

		if (!isRetry && returnValue != VODPlayer.VODRESULT_SUCCESS) {
			//VOD 재생 실패 시 리소스 정리 후 다시 플레이 시도
			LOG.print(this, "VOD Retry Strart");

			isRetry = true;

			try {
				Thread.sleep(500);
				absoluteStop();
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			runVOD();

			LOG.print(this, "VOD Retry End");
		}
	}

	public void stopVOD() {
		LOG.print(this, "[stopVOD]start VOD_STATUS=" + VOD_STATUS);
		synchronized (lock) {
			if (VOD_STATUS != VOD_STOP) {
				VOD_STATUS = VOD_STOP;
				stopTimer();
				this.isOpen = false;
				if (this.vodPlayer != null) {
					try {
						this.vodPlayer.stop();
					} catch (Throwable e) {
						LOG.print(e);
					}
				}
				releaseDevice();
				stopServiceContext();

				currentChannel();
			}
		}
		LOG.print(this, "[stopVOD]stop=" + VOD_STATUS);
	}

	public void currentChannel() {
		//1. Get ServiceContext
		LOG.print(this,"[start] getServiceContexts()");
		ServiceContextFactory factory = ServiceContextFactory.getInstance();
		try {
			ServiceContext[] serviceContexts = factory.getServiceContexts();
			LOG.print(this,"[end ] serviceContexts = factory.getServiceContexts()");
			if(serviceContexts.length > 0) {
				ServiceContext sc_1=serviceContexts[1];
				//2. getLocator
				OcapLocator currentChannelLocator[] = {(OcapLocator) ChannelController.getInstance().getCurrentChannel().getLocator()};
				sc_1.select(currentChannelLocator);
			}
			LOG.print("ServiceContexts length is 0");


		} catch (SecurityException e) {
			LOG.print("SecurityException:" + e);
			e.printStackTrace();

		} catch (Exception e) {
			LOG.print("Exception:" + e);
			e.printStackTrace();

		} finally {
			factory = null;
		}

	}

	public void destroyVOD(boolean tuneAV) {
		LOG.print(this, "[VODPlayer] destroyVOD tuneAV " + tuneAV);

		if(tuneAV){
			stopVOD();
		}else{
			absoluteStop();
		}

		this.isCommitting = false;
		this.curFileSize = 0; //sec
		this.isVodFail = false;


		if( this.tvTimerSpec != null) {
			this.tvTimerSpec.removeTVTimerWentOffListener(this.gListener);
			this.tvTimerSpec = null;
		}
		this.gListener = null;

		if( this.vodPlayer != null) {
			try {
				this.vodPlayer.removeVODEventListener(this);
			} catch (Throwable t) {
			}
			this.vodPlayer = null;
		}
		this.listener = null;

		this.assetId = null;

		this.loop = false;

		LOG.print(this, "[VODPlayer]destroy");
	}

	public void onlyVODStop(boolean tuneAV) {
		onlyVODStop();
		LOG.print(this, "[VODPlayer]releaseVOD "+tuneAV);
		if(tuneAV){
			LOG.debug(this, "onlyVODStop  stopVOD()  ========================  "  );
			stopVOD();
		}else{
			LOG.debug(this, "onlyVODStop  absoluteStop()  ========================  "  );
			absoluteStop();
		}
	}



	public void destroyVOD() {
		LOG.print(this, "[stopVOD]start VOD_STATUS=" + VOD_STATUS);
		synchronized (lock) {
			if (VOD_STATUS != VOD_STOP) {
				VOD_STATUS = VOD_STOP;
				stopTimer();
				this.isOpen = false;
				if (this.vodPlayer != null) {
					try {
						this.vodPlayer.stop();
					} catch (Throwable e) {
						LOG.print(e);
					}
				}

				releaseDevice();
				stopServiceContext();

				currentChannel();
			}
		}
		LOG.print(this, "[stopVOD]stop=" + VOD_STATUS);
		
	}

	/**
	 *  VOD stop을 하나, 채널이동(AV재생)은 없다.
	 */
	public void absoluteStop() {

		LOG.print(this, "[rewind]absoluteStop|start|" + VOD_STATUS);
		synchronized (lock) {
			VOD_STATUS = VOD_STOP;
			stopTimer();
			if (this.vodPlayer != null) {
				try {
					this.vodPlayer.stop();
				} catch (Throwable e) {
					LOG.print(e);
				}
			}

			releaseDevice();
			stopServiceContext();

		}
		LOG.print(this, "[rewind]absoluteStop|stop|" + VOD_STATUS);
	}

	public void vodKeyHandler(int keyCode) {
		LOG.debug(this, "called vodKeyHandler : key code = " + keyCode + ", key string = " + keyCode);

		long _seek = 0;

		switch (keyCode) {
			case KeyCode.VK_VOD_PLAY:
				if( IS_TOGGLE_PLAY_PAUSE ) {
					playAndPause();
				} else {
					play();
				}
				break;
			case 	KeyCode.VK_VOD_PAUSE:
				if( IS_TOGGLE_PLAY_PAUSE ) {
					playAndPause();
				} else {
					pause();
				}
				break;
			case KeyCode.VK_Y: // Rs.VK_VOD_PLAY:
				// 시작, 일시 정지
				playAndPause();
				break;
			case 	KeyCode.VK_VOD_STOP:
			case KeyCode.VK_G: //
				stopVOD();

				notifyVodPlayerStopEvent();
				break;
			case KeyCode.VK_VOD_TRACK_PREV:
				//이전 트랙

				_seek = Math.max(5, getCutPlayTime() - this.mediaTimeInterval);

				LOG.print(this, "VK_VOD_TRACK_PREV getCutPlayTime() " + getCutPlayTime() + " / _seek "+ _seek + " / setMediaRatio " + ((double) _seek / (double)getTotPlayTime()) );

				setMediaRatio((double) _seek / (double)getTotPlayTime());

				if ( this.rate == 0) {
					//일시 정지 중인경우 정상배속으로 변경
					VOD_STATUS = VOD_PLAY;
//					setRate( 1.0F );
					setRate(1);
				}
				break;
			case KeyCode.VK_VOD_TRACK_NEXT:
				//다음 트랙

				_seek = Math.min(getTotPlayTime() - 5, getCutPlayTime() + this.mediaTimeInterval);

				LOG.print(this, "VK_VOD_TRACK_NEXT getCutPlayTime()  :: " + getCutPlayTime() + " / _seek  :: "+ _seek + " / setMediaRatio  :: " + ((double) _seek / (double)getTotPlayTime()) + " /  this.rate :: " +  this.rate  );

				LOG.print(this,  " [before] getMediaRatio() " + getMediaRatio() );

				setMediaRatio((double) _seek / (double)getTotPlayTime());

				if ( this.rate == 0) {
					//일시 정지 중인경우 정상배속으로 변경
					VOD_STATUS = VOD_PLAY;
//					setRate( 1.0F );
					setRate(1);
				}

				LOG.print(this,  " [After] getMediaRatio() " + getMediaRatio() );

				break;
			case KeyCode.VK_R: //Rs.VK_VOD_REW:
				rewind();
				break;
			case KeyCode.VK_B: //Rs.VK_VOD_FF:
				fastFwd();
				break;
			case KeyCode.VK_0:
			case KeyCode.VK_1:
			case KeyCode.VK_2:
			case KeyCode.VK_3:
			case KeyCode.VK_4:
			case KeyCode.VK_5:
			case KeyCode.VK_6:
			case KeyCode.VK_7:
			case KeyCode.VK_8:
			case KeyCode.VK_9:
				setMediaRatio(this.mediaRatios[keyCode - KeyCode.VK_0]);
				break;
		}
		
	}

	/**
	 * controller 리스너 해지
	 */
	public void releaseDevice() {
		LOG.print(this, "[VODPlayer]releaseDevice");
		if (this.vodPlayer != null) {
			LOG.print(this, "[VODPlayer]releaseDevice vodPlayer is not null");
			try {
				this.vodPlayer.removeVODEventListener(this);
			} catch (RemoteException e) {
				LOG.print(e);
			}
			this.vodPlayer = null;
		} else {
			LOG.print(this, "[VODPlayer]releaseDevice vodPlayer is null");
		}
	}

	/**
	 * Player를 얻어와 controller리스너 등록
	 *
	 *            MediaLocator
	 * @throws Exception
	 */
	public void reserveDevice() throws Exception {
		try {
			LOG.print(this, "[reserveDevice]start");
			if (this.vodPlayer != null) {
				LOG.print(this, "[reserveDevice]vodController|" + this.vodPlayer);
				this.isCommitting = true;
				this.vodPlayer.addVODEventListener(this);

//				if (App.disposed) {
//					LOG.print(this, "[reserveDevice]App.disposed|"
//							+ App.disposed);
//					releaseDevice();
//				} else {
				{
					changeVODChannel();
					LOG.print(this, "[reserveDevice]vod sync start");
					this.isVodFail = false;
					this.isCommitting = false;
				}
			} else {
				LOG.print(this, "[reserveDevice]vod fail");
				this.isVodFail = true;
				this.isCommitting = false;
				throw new Exception();
			}
		} catch (Throwable e) {
			LOG.print(this, "[reserveDevice]exception");
			this.isVodFail = true;

			this.isCommitting = false;
			releaseDevice();
			throw new Exception();
		}
	}

	private void lookupVODIxc() {
		LOG.print(this, "lookupVODIxc");

		int i = 0;
		try {
			String loc = "/10000000/3031/" + VODPlayer.RMI_APP_NAME; //for CJHV 2015.04

			LOG.print(this, "loc = "+loc);
			LOG.print(this, "SceneManager.getInstance().xletContext = "+SSRConfig.getInstance().XLET_CONTEXT);

			while(this.vodPlayer == null ) {

				this.vodPlayer = (VODPlayer) IxcRegistry.lookup(SSRConfig.getInstance().XLET_CONTEXT, loc);
/*				if (vodPlayer != null) {
					LOG.print(this, "lookup fin");
					break;
				}*/

				LOG.print(this, "lookup cnt " + i );
				Thread.sleep(500L);

				i++;

				if( i > 10) break;
			}
		} catch (Exception e) {
			LOG.print(e);
		}

		try {
			getNodeGroupID();
			LOG.print(this, "nodeGroupID = "+ nodeGroupID);
		} catch (Exception e) {
			LOG.print(this, e);
		}
	}

	public static int getNodeGroupID() {
		if(nodeGroupID < 0) {
			nodeGroupID = ((CjChannelDatabase) CjChannelDatabase.getInstance()).getNodeGroup();
		}
		return nodeGroupID;
	}

	private VODPlayer getVPlayer() {

		if (this.vodPlayer == null) {
			lookupVODIxc();
		}

		LOG.print(this, "getVPlayer   (vodPlayer != null) " +  (this.vodPlayer != null));

		return this.vodPlayer;
	}

	public void setApplicationName(String fileName) {
		LOG.print(this, "setApplicationName fileName:" + fileName);
		LOG.print(this, "setApplicationName getVPlayer():" + getVPlayer());

		try {
			getVPlayer().setApplicationName("CJTOSPLUS");
			getVPlayer().setECMGroup(null);
		} catch (RemoteException e) {
			LOG.print(e);
		}
	}

	public void pauseVOD() {

		LOG.print(this, "[pauseVOD]status|" + VOD_STATUS);

		LOG.print(this, "pauseVOD:1-1");

		if (VOD_STATUS == VOD_PAUSE || VOD_STATUS == VOD_STOP) {
			LOG.print(this, "pauseVOD:1-2");
			return;
		}

		LOG.print(this, "pauseVOD:1-3");

		this.old_rate = this.rate;
		this.old_status = VOD_STATUS;

		LOG.print(this, "pauseVOD:1-4");

		synchronized (lock) {
			if (this.vodPlayer == null) {
				return;
			}
			LOG.print(this, "pauseVOD:1-5");
			VOD_STATUS = VOD_PAUSE;
			this.curFileSize = getMediaOffset();
			LOG.print(this, "pauseVOD:1-6");
			try {
				boolean isPause = setRate(VOD_PAUSE);

				if (isPause) {
					this.rate = 0;
				}

				LOG.print(this, "pauseVOD:1-7");
				LOG.print(this, "isPaused : " + isPause);
				LOG.print(this, "pauseVOD:1-8");
			} catch (Exception e) {
				LOG.print(e);
				this.rate = this.old_rate;
				VOD_STATUS = this.old_status;
				LOG.print(this, "[pauseVOD]Exception rollback");
			}
			LOG.print(this, "pauseVOD:1-9");
		}
		LOG.print(this, "pauseVOD:1-10");
	}

	public int getPlayRate() {
		LOG.print(this, "getPlayRate:playRate" + this.rate);
		return this.rate;
	}

	/**
	 * VOD Rewind.
	 */
	public void rewind() {
		LOG.print(this, "rewind" + VOD_STATUS);
		LOG.print(this, "playRate" + this.rate);

		if (this.vodPlayer == null)
			return;
		if (VOD_STATUS == VOD_STOP) {
			return;
		}
		synchronized (lock) {
			int _rateIndx = -1;
			try {
				if (VOD_STATUS == VOD_PAUSE) {
					_rateIndx = Math.max(0, indexOfRateArray(1) - 1);
				} else {
					if (Math.round(this.rate * 10) / 10 > 1) {
						//FF중일경우
						_rateIndx = Math.max(0, indexOfRateArray(1) - 1);
					} else {
						_rateIndx = Math.max(0, indexOfRateArray(this.rate) - 1);
					}
				}

				if (setRate(this.rateArray[_rateIndx])) {
					VOD_STATUS = VOD_REW;
					this.rate = this.rateArray[_rateIndx];
				}
			} catch (Exception e) {
			}
		}
	}

	public void fastFwd() {

		LOG.print(this, "fastFwd" + VOD_STATUS);
		LOG.print(this, "playRate" + this.rate);
		if (VOD_STATUS == VOD_STOP) {
			return;
		}

		if (this.vodPlayer == null)
			return;

		synchronized (lock) {
			int _rateIndx = -1;
			try {
				if (VOD_STATUS == VOD_PAUSE) {
					_rateIndx = Math.min(this.rateArray.length - 1, indexOfRateArray(1) + 1);
				} else {
					if (Math.round(this.rate * 10) / 10 < 1) {
						//REW중일 경우
						_rateIndx = Math.min(this.rateArray.length - 1, indexOfRateArray(1) + 1);
					} else {
						_rateIndx = Math.min(this.rateArray.length - 1, indexOfRateArray(this.rate) + 1);
					}
				}
				if (setRate(this.rateArray[_rateIndx])) {
					VOD_STATUS = VOD_FF;
					this.rate = this.rateArray[_rateIndx];
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * VOD Channel 로 전환 요청
	 *
	 * @throws RemoteException
	 */
	public void changeVODChannel() {
		LOG.print(this, "changeVODChannel");
//		 try {
//		 	getVPlayer().changeVODChannel(0, 0, Rs.WIDTH, Rs.HEIGHT);
//		 } catch (RemoteException e) {
//		 	LOG.print(e);
//		 }
		int frequency = getFrequency();
		int programNumber = getProgramNumber();
		int modulationFormat = 16;
		OcapLocator[] ls = null;
		try {

			ls = new OcapLocator[1];
			try {
				ls[0] = new OcapLocator(frequency, programNumber,modulationFormat);
			} catch (org.davic.net.InvalidLocatorException e) {
				LOG.print(e);
			}
			getServiceContext().select(ls);
		} catch (Exception e) {
			LOG.print(e);
		} finally {
			ls = null;
		}
		LOG.print(this, "changeVODChannel end");
	}

	/**
	 * VOD Channel Tunning 주파수 정보
	 *
	 * @return 주파수
	 * @throws RemoteException
	 */
	public int getFrequency() {
		int frequency = 0;
		LOG.print(this, "getFrequency");
		try {
			frequency = getVPlayer().getFrequency();
		} catch (RemoteException e) {
			LOG.print(e);
		}
		LOG.print(this, "getFrequency" + frequency);
		return frequency;
	}

	/**
	 * VOD Channel Tunning 프로그램 넘버
	 *
	 * @return 프로그램 넘버
	 * @throws RemoteException
	 */
	public int getProgramNumber() {
		int programNumber = 0;
		LOG.print(this, "getProgramNumber");
		try {
			programNumber = getVPlayer().getProgramNumber();
		} catch (RemoteException e) {
			LOG.print(e);
		}
		LOG.print(this, "getProgramNumber:" + programNumber);
		return programNumber;
	}

	/**
	 * 콘텐츠의 전체 File 크기 요청
	 *
	 * @return long byte 단위
	 * @throws RemoteException
	 */
	public long getTotalFileSize() {
		if (!this.isOpen || this.vodPlayer == null || VOD_STATUS == VOD_STOP) return 0;
		if (this.totalFileSize != 0) return this.totalFileSize;

		LOG.print(this, "getTotalFileSize");

		long fileSize = 0;
		try {
			fileSize = getVPlayer().getFileSize();
		} catch (RemoteException e) {
			LOG.print(e);
		}
		this.totalFileSize = fileSize;

		LOG.print(this, "fileSize=" + fileSize + " / secPerSize=" + this.secPerSize);
		return fileSize;
	}

	/**
	 * 현재 재생 지점의 파일 위치값 요청
	 *
	 * @return long byte 단위
	 * @throws RemoteException
	 */
	public long getMediaOffset() {
		if (!this.isOpen || this.vodPlayer == null || VOD_STATUS == VOD_STOP)  return 0;

		long duration = 0;
		try {
			duration = getVPlayer().getMediaOffset();
		} catch (RemoteException e) {
			LOG.print(e);
		}
		LOG.print(this, "getMediaOffset:duration=" + duration);
		return duration;
	}

	/**
	 * 현재 재생 지점의 비율을 요청
	 *
	 * @return double (0.0 ~ 1.0)
	 * @throws RemoteException
	 */
	public double getMediaRatio() {

		if (!this.isOpen || this.vodPlayer == null || VOD_STATUS == VOD_STOP) {
			return 0;
		}
		double duration = 0;
		LOG.print(this, "getMediaRatio:duration=" + duration);
		try {
			duration = getVPlayer().getMediaRatio();
		} catch (RemoteException e) {
			LOG.print(e);
		}
		LOG.print(this, "getMediaRatio:duration=" + duration);
		return duration;
	}

	// PLAY(0), PAUSE(1), FF(2,4,8,16,32,64) ,
	// REW(-2,-4,-8,-16,-32,-64)
	public boolean setRate(int rate) {
		LOG.print(this, "[setRate]rate|" + rate);
		boolean isSuccess = false;
		int preState = VOD_STATUS;
		LOG.print(this, "[setRate]VOD_STATUS|" + VOD_STATUS);


		try {
			isSuccess = getVPlayer().setRate(rate);
		} catch (RemoteException e) {
			LOG.print(e);
		} catch (Exception e) {
			LOG.print(e);
		}

		LOG.print(this, "[setRateisSuccess]" + isSuccess);
		if (isSuccess) {
			this.rate = rate;
			if (rate == VOD_PLAY) {
				VOD_STATUS = VOD_PLAY;
			} else if (rate == VOD_PAUSE) {
				VOD_STATUS = VOD_PAUSE;
			} else {
				if (rate < 0) {
					VOD_STATUS = VOD_REW;
				} else {
					VOD_STATUS = VOD_FF;
				}
			}
			notifyRateChangeEvent(rate);
		} else {
			VOD_STATUS = preState;
		}

		this.curFileSize = getMediaOffset();

		printStatus(VOD_STATUS);

		return isSuccess;
	}

	/**
	 * byte 단위의 파일 위치를 이용한 콘텐츠 탐색
	 * <p>
	 * PAUSE or FF or REW 상태일 때는 요청 실패
	 *
	 * @param offSet
	 *            byte 단위의 재생 파일 위치
	 * @return boolean 요청에 대한 결과
	 * @throws RemoteException
	 */
	public boolean setMediaOffset(long offSet) {

		LOG.print(this, "setMediaOffset:offset=" + offSet);
		if (VOD_STATUS == VOD_FF || VOD_STATUS == VOD_REW || VOD_STATUS == VOD_STOP
				|| VOD_STATUS == VOD_PAUSE) {
			if (!setRate(VOD_PLAY)) {
				LOG.print(this, "setMediaOffset:setRate is false");
				return false;
			}
		}

		if (offSet == 0) {
			try {
				return getVPlayer().setMediaOffset(0);
			} catch (RemoteException e) {
				LOG.print(e);
			}
		}

		if (offSet != 0) {
			try {
				this.totalFileSize = getTotalFileSize();
				this.totalDurationTime = getTotLongPlayTime();
				long curOffset = (long) (((float) offSet / this.totalDurationTime) * this.totalFileSize);
				LOG.print(this, "curOffset" + curOffset);
				return getVPlayer().setMediaOffset(curOffset);
			} catch (RemoteException e) {
				LOG.print(e);
				return false;
			}

		} else {
			try {
				return getVPlayer().setMediaOffset(0);
			} catch (RemoteException e) {
				LOG.print(e);
			}
		}
		return true;
	}

	/**
	 * 재생중인 콘텐츠의 비율을 이용한 콘텐츠 탐색
	 * <p>
	 * PAUSE or FF or REW 상태일 때는 요청 실패
	 *
	 * @param ratio
	 *            (0.0 ~ 1.0)
	 * @return boolean 요청에 대한 결과
	 * @throws RemoteException
	 */
	public boolean setMediaRatio(double ratio) {
		LOG.print(this, "setMediaRatio:ratio" + ratio);
		if (VOD_STATUS == VOD_FF || VOD_STATUS == VOD_REW || VOD_STATUS == VOD_STOP || VOD_STATUS == VOD_PAUSE) {
			if (!setRate(VOD_PLAY)) {
				LOG.print(this, "setMediaRatio:setRate is false");
				return false;
			}
		}

		boolean value = true;
		try {
			value = getVPlayer().setMediaRatio(ratio);
			notifyVODEvent(true, false, null);
		} catch (RemoteException e) {
			LOG.print(e);
		}
		LOG.print(this, "setMediaRatio:value=" + value);
		return value;
	}

	private void notifyVODEvent(final boolean played, final boolean exception, final String message) {
		if (this.listener != null) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					AV.this.listener.vodPlayerState(played, exception, message);
				}
			});
		}
	}

	private void notifyRateChangeEvent(final float rate) {
		if (this.listener != null) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					AV.this.listener.rateChangeEvent(rate);
				}
			});
		}
	}

	private void notifyVodPlayerEndEvent() {
		if (this.listener != null) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					AV.this.listener.vodPlayerEndEvent();

					LOG.print(this, "notifyVodPlayerEndEvent ::: loop ?? "+AV.this.loop);

					if(AV.this.loop) { // promotion 에서 vod 재생은 계속 반복된다.

						// promotion 에서 계속 vod 반복할때는 문제가 없는데,  promotion에서 full vod 로 재생후 , 끝까지 재생 완료해서 VODEVENT_END_OF_FILE  발생하는 경우에 문제가 있다.
						// 재생 완료하면 프로모션으로 되돌아가서 프로모션 영상을 다시 재생 시작하는데 의도치 않게 VODEVENT_END_OF_FILE 가 늦게 발생되어서 프로모션 영상을 중지하고 full vod 영상을 다시 loop 해버린다.
						// 이를 방지하기 위해 구분값으로 VOD_STATUS 로 구분 처리
						LOG.debug(this, "notifyVodPlayerEndEvent  VOD_STATUS  ========================> " +  VOD_STATUS );
						if( VOD_STATUS != VOD_STOP) {
							LOG.print(this, "loop true :: notifyVodPlayerEndEvent -> onlyVODStop -> startVODLoop");
							onlyVODStop(false);

							startVOD(AV.this.assetId, true);
						}

					}
				}
			});
		}
	}

	private void notifyVodPlayerStopEvent() {
		if (this.listener != null) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					AV.this.listener.vodPlayerStopEvent(AV.this.assetId);
				}
			});
		}
	}

	/**
	 * rateArray에서 해당 rate의 index 반환
	 * @param rate
	 * @return
	 */
	private int indexOfRateArray(int rate) {
		int indx = -1;
		for (int i = 0; i < this.rateArray.length; i++) {
			if (rate == this.rateArray[i]) {
				indx = i;
				break;
			}
		}
		return indx;
	}


//	/**
//	 *  배속을 설정한다.
//	 * @param r
//	 */
//	private void setRate( float r ) {
//
//	}

	/**
	 * VOD 재생/일시정지
	 */
	private void playAndPause() {
		if ( this.vodPlayer != null && this.isOpen ) {
			synchronized (lock) {
				try {
					switch (this.rate) {
						case 0:
							//일시정지중일 경우 재생
							setRate(1);
							break;
						case 1:
							//재생중일 경우 일시정지
							setRate(0);
							break;
						default:
							//배속변경중일 경우 정상재생
							setRate(1);
							break;
					}
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * VOD 재생
	 */
	private void play() {
		if ( this.vodPlayer != null && this.isOpen ) {
			synchronized (lock) {
				try {
					switch (this.rate) {
						case 0:
							//일시정지중일 경우 재생
							setRate(1);
							break;
						case 1:
							break;
						default:
							//배속변경중일 경우 정상재생
							setRate(1);
							break;
					}
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * VOD 일시정지
	 */
	private void pause() {
		if ( this.vodPlayer != null && this.isOpen ) {
			synchronized (lock) {
				try {
					switch (this.rate) {
						case 0:
							break;
						case 1:
							//재생중일 경우 일시정지
							setRate(0);
							break;
						default:
							//배속변경중일 경우 일시정지
							setRate(0);
							break;
					}
				} catch (Exception e) {
				}
			}
		}
	}

	public void forcedVODPause() {
		LOG.print(this, "[VODPlayer]forcedVODPause");
		synchronized (lock) {
			if ( this.vodPlayer != null && this.isOpen ) {
				setRate( 0 );
			}
		}
	}

	public void setSize(Rectangle rect) {

		javax.tv.service.selection.ServiceContext sc = null;
		javax.media.Player player = null;
		javax.tv.service.selection.ServiceContentHandler[] handlers  = null;
		javax.tv.media.AWTVideoSizeControl sizeCntr = null;
		java.awt.Rectangle NewBound = null;
		javax.tv.media.AWTVideoSize VideoSize = null;
		javax.tv.media.AWTVideoSize CheckedSize = null;
		javax.tv.media.AWTVideoSize CurBound = null;

		try {

				// http://www.interactivetvweb.org/tutorials/ocap
				sc = javax.tv.service.selection.ServiceContextFactory.getInstance().getServiceContexts()[0];
				handlers  = sc.getServiceContentHandlers();
				LOG.print(this, "resizeVOD - Rectangle : handlers.length " + handlers.length );
				for(int i=0; i < handlers.length ; i++) {
					if (handlers[i] instanceof javax.tv.service.selection.ServiceMediaHandler) {
						player = (javax.media.Player) handlers[i];
						LOG.print(this, "resizeVOD - Rectangle : handlers["+i+ "] "+ handlers[i] );
					}
				}

				if(player == null){
					LOG.print(this, "resizeVOD - Rectangle : player = null " + (player == null) );
					return;
				}
				//if(vodRec==null) vodRec=fullRec;
				vodRec=rect;
				sizeCntr = ( javax.tv.media.AWTVideoSizeControl ) player.getControl( "javax.tv.media.AWTVideoSizeControl" );
				NewBound = new java.awt.Rectangle( sizeCntr.getSourceVideoSize() ); // 소스 비디오 사이즈
				VideoSize = new javax.tv.media.AWTVideoSize( NewBound,  rect); // 소스 비디오 사이즈를 풀 사이즈로 맞추려면 몇배로 늘려야하는가

				if (rect.width != 0 && rect.height != 0) {
					LOG.print(this, "resizeVOD -  : 1- 0 " );
					CheckedSize = sizeCntr.checkSize( VideoSize ); // 하드웨어가 지원하는 가장 근접한 사이즈 반환 VideoSize -> CheckedSize
					CurBound = sizeCntr.getSize(); // 현재 사이즈일거 같고

					if ( !CurBound.equals( CheckedSize ) ) {
						LOG.print(this, "resizeVOD -  : 1- 1 " );
						sizeCntr.setSize( CheckedSize );
					}
				} else {
					LOG.print(this, "resizeVOD -  : 2- 0 " );
					sizeCntr.setSize( VideoSize );
				}


		} catch (Exception e) {
			LOG.print(this, "resizeVOD Error :: " + e.getMessage());
		} finally {
			sc = null;
			player = null;
			if(handlers  != null){
				for(int i=0; i < handlers.length ; i++) {
					handlers[i] = null;
				}
				handlers  = null;
			}
			sizeCntr = null;
			NewBound = null;
			VideoSize = null;
			CheckedSize = null;
			CurBound = null;
		}
	}


	public int getTotPlayTime() {
		if (!this.isOpen || this.vodPlayer == null || VOD_STATUS == VOD_STOP) return 0;

		if (this.totalDurationTime != 0) return (int) (this.totalDurationTime / 1000);

		LOG.print(this, "getTotLongPlayTime");
		try {
			this.totalDurationTime = getVPlayer().getDuration();
		} catch (RemoteException e) {
			LOG.print(e);
		}
		LOG.print(this, "totalDurationTime=" + this.totalDurationTime);
		if (this.totalDurationTime == 0) return 0;

		return (int) (this.totalDurationTime / 1000);
	}

	public long getTotLongPlayTime() {
		if (!this.isOpen || this.vodPlayer == null || VOD_STATUS == VOD_STOP) {
			return 0;
		}
		if (this.totalDurationTime != 0)
			return this.totalDurationTime;

		LOG.print(this, "getTotLongPlayTime");
		try {
			this.totalDurationTime = getVPlayer().getDuration();
		} catch (RemoteException e) {
			LOG.print(e);
		}
		LOG.print(this, "totalDurationTime=" + this.totalDurationTime);
		if (this.totalDurationTime == 0)
			return 0;
		return this.totalDurationTime;
	}

	public int getCutPlayTime() {
//		curFileSize = getMediaOffset();
//		totalFileSize = getTotalFileSize();

		LOG.print(this, "getCutPlayTime >> curFileSize " + this.curFileSize + " / totalFileSize  " + this.totalFileSize);

		if (this.curFileSize == 0 || this.totalFileSize == 0 || getTotPlayTime() == 0)  return 0;

/*		if (Rs.IS_EMUL)
			return emulTime;*/

		int curtime = (int) (((float) this.curFileSize / (float) this.totalFileSize) * getTotPlayTime());

/*		LOG.print(this,new java.text.SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new java.util.Date())
				+" // "+ "getTotPlayTime :  " + getTotPlayTime() + " / getCutPlayTime : "
				+ curtime + " / curFileSize : " + curFileSize + " / totalFileSize : " + totalFileSize
				+ " / getMediaRatio : " + getMediaRatio());*/

		return curtime;
	}

	public long getCurLongPlayTime() {
		/*		if (Rs.IS_EMUL)
		return emulTime * 1000;*/

		//	curFileSize = getMediaOffset();
		//	totalFileSize = getTotalFileSize();

		LOG.print(this, "getCurLongPlayTime >> curFileSize " + this.curFileSize + " / totalFileSize  " + this.totalFileSize);

		if (this.curFileSize == 0 || this.totalFileSize == 0 || getTotLongPlayTime() == 0) return 0;

		long curtime = (int) (((float) this.curFileSize / (float) this.totalFileSize) * getTotLongPlayTime());

		LOG.print(this, "getCurLongPlayTime : " + curtime);
		return curtime;
	}

	private ServiceContext getServiceContext() {
		LOG.print(this, "getServiceContext");

		if (this.serviceContext == null) {
			LOG.print(this, "serviceContext is null");
			ServiceContextFactory scf = ServiceContextFactory.getInstance();
			try {
				this.serviceContext = scf.createServiceContext();
			} catch (InsufficientResourcesException e) {
				LOG.print(e);
			} catch (SecurityException e) {
				LOG.print(e);
			}
			LOG.print(this, "serviceContext=" + this.serviceContext);
		}
		return this.serviceContext;
	}

	private void stopServiceContext() {
		LOG.print(this, "serviceContext stop ");
		if (this.serviceContext != null) {
			try {
				this.serviceContext.stop();
				this.serviceContext.destroy();
				this.serviceContext = null;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	/**
	 * VOD VOD_STATUS print
	 *
	 * @param STATUS
	 *
	 */
	public void printStatus(int STATUS) {
		switch (STATUS) {
			case VOD_PLAY:
				LOG.print(this, "[printStatus]STATUS = VOD_PLAY");
				break;
			case VOD_STOP:
				LOG.print(this, "[printStatus]STATUS = VOD_STOP");
				break;
			case VOD_PAUSE:
				LOG.print(this, "[printStatus]STATUS = VOD_PAUSE");
				break;
			case VOD_REW:
				LOG.print(this, "[printStatus]STATUS = VOD_REW");
				break;
			case VOD_FF:
				LOG.print(this, "[printStatus]STATUS = VOD_FF");
				break;
		}
	}

	public void printResult(int result) {
		switch (result) {
			case VODPlayer.VODRESULT_SUCCESS:
				LOG.print(this, "Result = PLAY_SUCCESS");
				break;
			case VODPlayer.VODRESULT_INVALID_PARAMETER:
				LOG.print(this, "Result = INVALID_PARAMETER");
				break;
			case VODPlayer.VODRESULT_INVALID_TSID:
				LOG.print(this, "Result = INVALID_TSID");
				break;
			case VODPlayer.VODRESULT_INVALID_FILE:
				LOG.print(this, "Result = INVALID_FILE");
				break;
			case VODPlayer.VODRESULT_MAX_SESSION_ERROR:
				LOG.print(this, "Result = MAX_SESSION_ERROR");
				break;
			case VODPlayer.VODRESULT_SERVER_ERROR:
				LOG.print(this, "Result = SERVER_ERROR");
				break;
			case VODPlayer.VODRESULT_INVALID_USER:
				LOG.print(this, "Result = INVALID_USER");
				break;
/*		case VODPlayer.VODRESULT_INVALID_SRM_ADDRESS:
			LOG.print(this, "Result = INVALID_SRM_ADDRESS");
			break;*/
			default:
				LOG.print(this, "Result = UNKNOWN [" + result + "]");
				break;
		}
	}

	public void printVodEvent(int evt) {
		String _str = "UNKNOWN";
		switch (evt) {
			case VODPlayer.VODEVENT_SESSION_ERROR:
				_str = "VODEVENT_SESSION_ERROR";
				break;
			case VODPlayer.VODEVENT_SESSION_NORMAL:
				_str = "VODEVENT_SESSION_NORMAL";
				break;
			case VODPlayer.VODEVENT_BEGIN_OF_FILE:
				_str = "VODEVENT_BEGIN_OF_FILE";
				break;
			case VODPlayer.VODEVENT_END_OF_FILE:
				_str = "VODEVENT_END_OF_FILE";
				break;
			case VODPlayer.VODEVENT_PAUSE_TIMEOUT:
				_str = "VODEVENT_PAUSE_TIMEOUT";
				break;
			case VODPlayer.VODEVENT_TUNNING_ERROR:
				_str = "VODEVENT_TUNNING_ERROR";
				break;
			case VODPlayer.VODEVENT_NEED_TUNE:
				_str = "VODEVENT_NEED_TUNE";
				break;
/*		case VODPlayer.VODEVENT_ADVERT_START:
			_str = "VODEVENT_ADVERT_START";
			break;
		case VODPlayer.VODEVENT_ADVERT_STOP:
			_str = "VODEVENT_ADVERT_STOP";
			break;*/
		}

		LOG.print(this, "VOD EVENT : " + _str +  " / VOD EVENT evt : " +  evt);
	}

	public String getResult(int result) {
		switch (result) {
			case 0:
				return "PLAY_SUCCESS";
			case 1:
				return "INVALID_PARAMETER";
			case 2:
				return "INVALID_TSID";
			case 3:
				return "INVALID_FILE";
			case 4:
				return "MAX_SESSION_ERROR";
			case 5:
				return "SERVER_ERROR";
			case 6:
				return "INVALID_USER";
			default:
				return "UNKNOWN";
		}
	}
	


	public int getPlayStatus() {
		return VOD_STATUS;
	}

	public float getRate() {
		return rate;
	}

	public void addVodEventListener(VODEventListener listener) {
		this.listener=listener;
		
	}

	public void removeVodEventListener(VODEventListener listener) {
		this.listener=null;
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void onlyVODStop() {
		this.onlyVODStop(true);
		
	}


}
