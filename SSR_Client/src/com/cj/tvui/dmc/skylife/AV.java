package com.cj.tvui.dmc.skylife;

import java.awt.Rectangle;
import java.io.IOException;

import javax.media.ConnectionErrorEvent;
import javax.media.Controller;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DeallocateEvent;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoDataSourceException;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RateChangeEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.StartEvent;
import javax.media.StopEvent;
import javax.media.Time;
import javax.media.protocol.DataSource;
import javax.tv.media.AWTVideoSize;
import javax.tv.media.AWTVideoSizeControl;
import javax.tv.service.selection.ServiceContext;
import javax.tv.service.selection.ServiceContextFactory;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.StbController;
import com.cj.tvui.dmc.interfaces.AVInterface;
import com.cj.tvui.dmc.interfaces.VODInterface;
import com.cj.tvui.util.DateUtil;
import com.cj.tvui.util.LOG;
import com.cj.tvui.util.StringUtil;

import kt.alticast.interactive.service.vod.ParamMap;
import kt.alticast.interactive.service.vod.UnicastService;
import kt.alticast.interactive.service.vod.client.UnicastServiceClientStub;

public class AV implements AVInterface, VODInterface{
	
	ServiceContext sc = ServiceContextFactory.getInstance().getServiceContexts()[0];
	Player avPlayer = (Player) sc.getServiceContentHandlers()[0];
	AWTVideoSizeControl sizeCntr = ( AWTVideoSizeControl ) avPlayer.getControl( "javax.tv.media.AWTVideoSizeControl" );

	public void changeVideoSize(Rectangle rect) {
		// TODO Auto-generated method stub
		try {
			Rectangle NewBound = new Rectangle( sizeCntr.getSourceVideoSize() );
			AWTVideoSize VideoSize = new AWTVideoSize( NewBound,  rect);

			if (rect.width != 0 && rect.height != 0) {
				AWTVideoSize CheckedSize = sizeCntr.checkSize( VideoSize );
				AWTVideoSize CurBound = sizeCntr.getSize();
				if ( !CurBound.equals( CheckedSize ) ) {
					sizeCntr.setSize( CheckedSize );
				}
			} else {
				sizeCntr.setSize( VideoSize );
			}

		} catch (Exception e) {
			LOG.print(this, "resizeAV Error :: " + e.getMessage());
		}

	}

	public void turnOnVideo() {
		try {
			com.kt.navsuite.core.ChannelController.getInstance().resume();
		} catch (NoSuchMethodError nm) {
			LOG.print(this, "tuneOnAV NoSuchMethodError");
			try {
                avPlayer.start();
			} catch (Exception e) {
				LOG.print(this, "tuneOnAV Error - 2");
				LOG.print(e);
			}
		} catch (Exception e) {
			LOG.print(this, "tuneOnAV Error - 1");
			LOG.print(e);
		}
	}

	public void turnOffVideo() {
		try {
			com.kt.navsuite.core.ChannelController.getInstance().pause();
		} catch (NoSuchMethodError nm) {
			LOG.print(this, "tuneOnAV NoSuchMethodError");
			try {
                avPlayer.stop();
			} catch (Exception e) {
				LOG.print(this, "tuneOnAV Error - 2");
				LOG.print(e);
			}
		} catch (Exception e) {
			LOG.print(this, "tuneOnAV Error - 1");
			LOG.print(e);
		}
		
	}

	public Rectangle getCurrentVideoSize() {
		// TODO Auto-generated method stub
		return new Rectangle( sizeCntr.getSourceVideoSize() );
	}
	
	
	/*
	 * VOD Interface Implementation
	 * 
	 */



    public static int vodStatus = VOD_STOP;

    /**
     * 에러 발생시 플레이어 종료
     */
    private final int CLOSE = 0;
    /**
     * 에러 발생시 URL2로 플레이어 실행
     */
    private final int RETRY = 1;
    /**
     * 에러 발생시 팝업 실행
     */
    private final int POPUP = 2;


    /**
     * VOD 실행시 인증에 필요한 파라메터를 넣을 Map
     */
    private ParamMap requestParam;
    /**
     * VOD 인증 후 넘어오는 파라메터가 들어 있는 Map
     */
    private ParamMap responseParam;
    /**
     * VOD 인증에 필요한 Stub
     */
    private UnicastService unicastService;
    /**
     * Player를 만들기 위한 MediaLocator
     */
    private MediaLocator mediaLocator;


    /**
     * VOD assetId
     */
    private String assetId;
    /**
     * VOD서버 URL1
     */
    private String vod_Server1;
    /**
     * VOD서버 URL2
     */
    private String vod_Server2;
    /**
     * 현재 시도중인 VODURL
     */
    private String vodUrl;
    /**
     * Play 티켓 ID
     */
    private String ticket_ID;

    /**
     * VOD Start 여부에 대한 Flag값
     */
    private boolean started;
    /**
     * 플레이되고 있는 VOD의 총 실행 시간
     */
    private int totalMediaTime;
    /**
     * 실행 시간
     */
    private int offsetTime = 0;
    /**
     * VOD Rate
     */
    private float rate;
    /**
     * 이전/다음 트랙 시 건너뛰는 시간(초단위)
     */
    private int mediaTimeInterval = 15; //5 * 60;
    /**
     * VOD 배속 배열
     */
    private float[] rateArray = { -64.0F, -32.0F, -16.0F, -8.0F, -4.0F, -2.0F, 1.0F, 2.0F, 4.0F, 8.0F, 16.0F, 32.0F, 64.0F };

    private Player vodPlayer;



    private VODEventListener listener = null;



	/**
	 * VOD 플레이어 준비, Play는 아님
	 */
	public void startVOD(String assetId) {
        this.assetId = assetId;

        this.offsetTime = 0;

        if ( requestParam == null ) {
            requestParam = new ParamMap();
        }

        final String said = StbController.getInstance().getSubscribeID();

        requestParam.clear();
        requestParam.put( "AUTH_TYPE", "VOD" );
        requestParam.put( "SA_ID", said);
        requestParam.put( "BUYING_DATE", DateUtil.getCurrentTime14() );
        requestParam.put( "CONTS_ID", this.assetId );

        try {
            if ( unicastService == null ) {
                unicastService = new UnicastServiceClientStub( UnicastService.class );
            }

            if ( unicastService != null ) {
                responseParam = unicastService.authorizeViewX( "VOD", said, requestParam );

                if (responseParam != null) {
                    vod_Server1 = responseParam.get( "VOD_SERVER1" );
                    if (StringUtil.isEmpty(vod_Server1)) {
                        vod_Server1 = null;
                    }
                    vod_Server2 = responseParam.get( "VOD_SERVER2" );
                    if (StringUtil.isEmpty(vod_Server2)) {
                        vod_Server2 = null;
                    }

                    //AV 정지
                    turnOffVideo();

                    vodServerConnect(vod_Server1);
                } else {
                    LOG.print(this, "authorizeViewX responseParam IS NULL!!!");
                    if (listener != null) {
                        listener.vodPlayerState(false, true, VOD_ERROR_3);
                    }
                }
            } else {
                LOG.print(this, "unicastService IS NULL!!!");
                if (listener != null) {
                    listener.vodPlayerState(false, true, VOD_ERROR_2);
                }
            }
        } catch (Exception e) {
            LOG.print(e);
            if (listener != null) {
                listener.vodPlayerState(false, true, VOD_ERROR_3);
            }
        }
    }
	
	
	/**
	 * VOD 서버에 연결하고, Player를 생성한다
	 * @param vodUrl
	 */
	private void vodServerConnect(String vodUrl) {
        this.vodUrl = vodUrl;
        ticket_ID = responseParam.get( "TICKET_ID" ) + "X:";

        LOG.print(this, "VODURL : " + this.vodUrl);
        LOG.print(this, "ticket_ID : " + this.ticket_ID);
        LOG.print(this, "offsetTime " + offsetTime);

        if ( vodUrl != null && vodUrl.trim().length() > 4 ) {
            mediaLocator = null;
            String mediaLocatorUrl = vodUrl + ";;" + ticket_ID;
            if (offsetTime > 0) {
                mediaLocatorUrl = mediaLocatorUrl + "@" + offsetTime * 1000;
            }
            LOG.print(this, "mediaLocatorUrl : " + mediaLocatorUrl);
            mediaLocator = new MediaLocator( mediaLocatorUrl );
            if ( mediaLocator == null ) {
                LOG.print( this, "Locator is null" );
            }
            try {
                if ( vodPlayer != null ) {
                    clearPlayer();
                    LOG.print( this, "ClearPlayer VODPlayer" );
                }

                DataSource playingDataSource;
                try {
                    playingDataSource = Manager.createDataSource( mediaLocator );
                    vodPlayer = Manager.createPlayer( playingDataSource );
                    LOG.print( this, "create playingDataSource VODPlayer" );
                } catch (NoDataSourceException e) {
                    LOG.print(this, e);
                    vodPlayer = Manager.createPlayer( mediaLocator );
                }
                LOG.print( this, "create VODPlayer" );
                vodPlayer.addControllerListener( vodControllerListener );
                vodPlayer.realize();
                LOG.print( this, " player realize" );
            } catch ( NoPlayerException e ) {
                LOG.print(this, e);
                vodRetryProcess();
            } catch ( IOException e ) {
                LOG.print(this, e);
                vodRetryProcess();
            } catch ( Exception e ) {
                LOG.print(this, e);
                vodRetryProcess();
            }
        }
        else {
            LOG.print(this, "VODURL is Empty!!! Retry...");
            vodRetryProcess();
        }
    }
	
	/**
	 * VOD Player 초기화
	 */
	private synchronized void clearPlayer() {
        if (vodPlayer != null) {
            try {
                vodPlayer.removeControllerListener(vodControllerListener);
                vodPlayer.stop();
                if ( vodPlayer.getState() != Controller.Started ) {
                    vodPlayer.deallocate(); // stop() 를 먼져 호출 해야 된다.
                }
                if ( vodPlayer.getState() != Controller.Unrealized ) {
                    vodPlayer.close();
                }
            } catch (Exception e) {
                vodPlayer.stop();
                vodPlayer.deallocate();
                vodPlayer.close();
            }
            vodPlayer = null;
        }
        vodStatus = VOD_STOP;
    }
	
	/**
	 * VOD 생성도중 에러가 발생할 경우 다른 경로로 VOD를 실행시키거나 팝업을 띄운다.
	 */
	private void vodRetryProcess() {
		if ( vodPlayer != null )
			offsetTime = (int)vodPlayer.getMediaTime().getSeconds();

		clearPlayer();

		String url = null;
		if ( vodUrl != null && vodUrl.equalsIgnoreCase( vod_Server1 ) ) {
			url = vod_Server2;
		}

		if ( url != null ) {
			vodServerConnect( url );
		}
		else {
			if (listener != null) {
				listener.vodPlayerState(false, true, VOD_ERROR_0);
			}
		}
		url = null;
	}
	
	
	

	public void destroyVOD() {
        clearPlayer();

        //AV 재생
        try {
            turnOnVideo();
        } catch (Exception e) {
            LOG.print(e);
        }

        unicastService = null;
        mediaLocator = null;

        if ( requestParam != null ) {
            requestParam.clear();
            requestParam = null;
        }

        if ( responseParam != null ) {
            responseParam.clear();
            responseParam = null;
        }

        ticket_ID = null;
        vod_Server1 = null;
        vod_Server2 = null;

        vodUrl = null;
        vodPlayer = null;
    }

	public void vodKeyHandler(int keyCode) {
        int _rateIndx = -1;
        long _seek = 0;
        switch (keyCode) {
            case Keys.VK_VOD_PLAY:
                // 시작, 일시 정지
                playAndPause();
                break;
            case Keys.VK_VOD_STOP:
                //정지
                stop();
                break;
            case Keys.VK_VOD_REW:
                //REW
                LOG.print(this, "current rate : " + rate );
                if (rate == 0) {
                    //일시 정지 중
                    _rateIndx = Math.max(0, indexOfRateArray(1.0F) - 1);
                } else {
                    if (Math.round(rate * 10) / 10 > 1) {
                        //FF중일경우
                        _rateIndx = Math.max(0, indexOfRateArray(1.0F) - 1);
                    } else {
                        _rateIndx = Math.max(0, indexOfRateArray(rate) - 1);
                    }
                }
                vodStatus = VOD_REW;
                this.setRate( rateArray[_rateIndx] );
                break;
            case Keys.VK_VOD_FF:
                //FF
                LOG.print(this, "current rate : " + rate );
                if (rate == 0) {
                    //일시 정지 중
                    _rateIndx = Math.min(rateArray.length - 1, indexOfRateArray(1.0F) + 1);
                } else {
                    if (Math.round(rate * 10) / 10 < 1) {
                        //REW중일 경우
                        _rateIndx = Math.min(rateArray.length - 1, indexOfRateArray(1.0F) + 1);
                    } else {
                        _rateIndx = Math.min(rateArray.length - 1, indexOfRateArray(rate) + 1);
                    }
                }
                vodStatus = VOD_FF;
                this.setRate( rateArray[_rateIndx] );
                break;
            case Keys.VK_VOD_TRACK_PREV:
                //이전 트랙
                _seek = (long)Math.max(5, vodPlayer.getMediaTime().getSeconds() - mediaTimeInterval);
                vodPlayer.setMediaTime( new Time(_seek * Time.ONE_SECOND ) );

                if (true || rate == 0) {
                    //일시 정지 중인경우 정상배속으로 변경
                    vodStatus = VOD_PLAY;
                    this.setRate( 1.0F );
                }
                break;
            case Keys.VK_VOD_TRACK_NEXT:
                //다음 트랙
                _seek = (long)Math.min(vodPlayer.getDuration().getSeconds() - 5, vodPlayer.getMediaTime().getSeconds() + mediaTimeInterval);
                vodPlayer.setMediaTime( new Time(_seek * Time.ONE_SECOND ) );

                if (true || rate == 0) {
                    //일시 정지 중인경우 정상배속으로 변경
                    vodStatus = VOD_PLAY;
                    this.setRate( 1.0F );
                }
        }
    }
	
	/**
	 * VOD 재생/일시정지
	 */
	private void playAndPause() {
        if ( vodPlayer != null && started ) {
            float _rate = vodPlayer.getRate();
            if (_rate == 0.0F) {
                //일시정지중일 경우 재생
                rate = 1.0F;
                vodStatus = VOD_PLAY;
                vodPlayer.setRate( rate );
            }
            else if (_rate == 1.0F) {
                //재생중일 경우 일시정지
                rate = 0.0F;
                vodStatus = VOD_PAUSE;
                vodPlayer.setRate( rate );
            }
            else {
                //배속변경중일 경우 정상재생
                rate = 1.0F;
                vodStatus = VOD_PLAY;
                vodPlayer.setRate( rate );
            }
        }
    }
	
	/**
	 * 정지
	 */
	private void stop() {
        if ( vodPlayer != null ) {
            LOG.print( this, "Stop " + vodPlayer.getState() );
            // rate == 0 일경우 stop() 를 호출하면 Socket Exception 이 발생한다.
            if ( vodPlayer.getState() == 600 ) {
                if ( vodPlayer.getRate() != 1.0F ) {
                    rate = 1.0F;
                    vodPlayer.setRate( rate );
                }
                vodPlayer.setMediaTime( new Time( 0L ) );
            }
            vodStatus = VOD_STOP;
            vodPlayer.stop();
        }
    }
	
	/**
	 *  배속을 설정한다.
	 * @param r
	 */
	private void setRate( float r ) {
        if ( vodPlayer != null && started ) {
            LOG.print( this, "NextRate : " + r );
            if ( vodPlayer.getState() != 600 ) {
                vodPlayer.start();
            }
            rate = r;
            vodPlayer.setRate( rate );
            LOG.print( this, "player.setRate(rate)" );
        }
    }
	
	/**
	 * rateArray에서 해당 rate의 index 반환
	 * @param rate
	 * @return
	 */
	private int indexOfRateArray(float rate) {
        int indx = -1;
        for (int i = 0; i < rateArray.length; i++) {
            if (rate == rateArray[i]) {
                indx = i;
                break;
            }
        }
        return indx;
    }

	public int getTotPlayTime() {
		return totalMediaTime;
	}

	public long getTotLongPlayTime() {
		return totalMediaTime * 1000;
	}

	public int getCutPlayTime() {
		if (vodPlayer != null) {
			return (int)vodPlayer.getMediaTime().getSeconds();
		}
		return 0;
	}

	public long getCurLongPlayTime() {
		if (vodPlayer != null) {
			return (long)vodPlayer.getMediaTime().getSeconds() * 1000L;
		}
		return 0;
	}

	public int getPalyStatus() {
		return vodStatus;
	}

	public float getRate() {
		return this.rate;
	}

	public void addVodEventListener(VODEventListener listener) {
		this.listener = listener;
	}

	public void removeVodEventListener(VODEventListener listener) {
		this.listener = null;
	}

	public void destroy() {
		destroyVOD();
		
	}

	public void onlyVODStop() {
		// TODO Auto-generated method stub
		
	}

	public void forcedVODPause() {
		// TODO Auto-generated method stub
		
	}



	//VodControllerListener Implements
    private ControllerListener vodControllerListener = new ControllerListener() {

        public void controllerUpdate(ControllerEvent event) {

            LOG.print(this, "controllerUpdate : " + event);
            if ( event.getSource() == vodPlayer ) {
                if ( vodPlayer == null ) {
                    LOG.print( this, "vodPlayer is null" );
                    started = false;
                    return;
                }
                else if ( event instanceof ConnectionErrorEvent ) {
                    LOG.print(this, "ConnectionErrorEvent");

                    started = false;
                    totalMediaTime = 0;

                    ConnectionErrorEvent cee = ( ConnectionErrorEvent ) event;
                    String ecode = cee.getMessage();

                    LOG.print( this, "ConnectionErrorEvent Error Code : " + ecode );


                    int process = EVENT_CLOSE;
                    if("Tune is failed".equals(ecode.trim())){
                        process = EVENT_RETRY;
                    }
                    try {
                        process = errorProcess(Integer.parseInt(ecode));
                    } catch (NumberFormatException e) {
                        LOG.print(e);
                        if("Tune is failed".equals(ecode.trim())){
                            process = EVENT_RETRY;
                        }
                    } catch ( Exception e ) {
                        LOG.print(e);
                        process = EVENT_CLOSE;
                    }
                    if("Tune is failed".equals(ecode.trim())){
                        process = EVENT_RETRY;
                    }

                    switch (process) {
                        case EVENT_RETRY:
                            vodRetryProcess();
                            break;
                        case EVENT_POPUP:
                            if (listener != null) {
                                listener.vodPlayerState(false, true, VOD_ERROR_0);
                            }
                            clearPlayer();
                            break;
                        case EVENT_CLOSE:
                        default:
                            LOG.print( this, "ConnectionErrorEvent ErrorProcess Close" );
                            clearPlayer();
                            break;
                    }
                }
                else if ( event instanceof RealizeCompleteEvent ) {
                    LOG.print( this, " RealizeCompleteEvent" );
                    vodPlayer.prefetch();
                }
                else if ( event instanceof PrefetchCompleteEvent ) {
                    LOG.print( this, " PrefetchCompleteEvent" );
                    totalMediaTime = ( int ) vodPlayer.getDuration().getSeconds();
                    LOG.print( this, " TotMediaTime " + totalMediaTime );
                    if ( vodPlayer.getTargetState() != Controller.Started ) {
                        vodPlayer.start();
                    }
                }
                else if ( event instanceof StartEvent ) {
                    LOG.print( this, " StartEvent" );
                    rate = 1.0F;
                    started = true;
                    vodStatus = VOD_PLAY;
                    if (listener != null) {
                        listener.vodPlayerState(started, false, null);
                    }
                }
                else if ( event instanceof RateChangeEvent ) {
                    LOG.print( this, " RateChangeEvent" );
                    float _rate = vodPlayer.getRate();
                    LOG.print( this, " RateChangeEvent - PreRate : " + rate + "CurRate : " +  _rate);
                    rate = _rate;
                    if ( _rate == 1.0F ) {
                        vodStatus = VOD_PLAY;
                        started = true;
                    } else if (_rate == 0.0F) {
                        vodStatus = VOD_PAUSE;
                    } else if (_rate < 0.0F) {
                        vodStatus = VOD_REW;
                    } else if (_rate > 1.0F) {
                        vodStatus = VOD_FF;
                    }
                    if (listener != null) {
                        listener.rateChangeEvent( vodPlayer.getRate() );
                    }

                }

                else if ( event instanceof DeallocateEvent ) {
                    LOG.print( this, " DeallocateEvent" );
                    started = false;
                }
                else if ( event instanceof ControllerClosedEvent ) {
                    LOG.print( this, " ControllerClosedEvent" );
                    started = false;
                    clearPlayer();
                }
                else if ( event instanceof EndOfMediaEvent ) {
                    LOG.print( this, " EndOfMediaEvent" );
                    started = false;
                    if ( listener != null ) {
                        listener.vodPlayerEndEvent();
                    }
                    clearPlayer();
                }
                else if ( event instanceof StopEvent ) {
                    LOG.print( this, " StopEvent" );
                    vodStatus = VOD_STOP;
                    if (vodPlayer != null) {
                        if ( listener != null ) {
                            listener.vodPlayerStopEvent(assetId);
                        }
                    }
                }
            }


        }

        /**
         * 에러 코드에 따라 다음 프로세스 결정한다.
         * @param errorcode
         * @return
         */
        private int errorProcess( int errorcode ) {
            switch ( errorcode ) {
                case 40040:
                case 40042:
                case 40043:
                case 40048:
                case 40054:
                case 40055:
                case 40056:
                case 41040:
                case 42000:
                    return EVENT_CLOSE;
                case 50000:
                    return EVENT_POPUP;
                default:
                    return EVENT_RETRY;
            }
        }
    };

}
