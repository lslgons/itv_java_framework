package com.castis.vod.control;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * VOD 재생을 위한 인터페이스
 *
 *
 * @version 1.4
 * @author castis
 * @since 1.0
 *
 */
public interface VODPlayer extends Remote {
    /** RMI APP NAME */
    public static final String RMI_APP_NAME = "VODPLAYER";

    /** Play 요청 성공 */
    public static final int VODRESULT_SUCCESS = 0;

    /** play 요청시 입력값이 빠져있거나 적절하지 않은 값 */
    public static final int VODRESULT_INVALID_PARAMETER = 1;

    /** 서버에서 요청한 TSID 를 찾을 수 없음 */
    public static final int VODRESULT_INVALID_TSID = 2;

    /** 서버에서 요청한 File 이 존재하지 않거나 유효하지 않은 File */
    public static final int VODRESULT_INVALID_FILE = 3;

    /** 최대 연결 세션을 초과 */
    public static final int VODRESULT_MAX_SESSION_ERROR = 4;

    /** 서버 장애 */
    public static final int VODRESULT_SERVER_ERROR = 5;

    /** 사용자 인증 실패 */
    public static final int VODRESULT_INVALID_USER = 6;

    /** 세션 에러 VOD Event */
    public static final int VODEVENT_SESSION_ERROR = -1;

    /** 세션 정상 VOD Event */
    public static final int VODEVENT_SESSION_NORMAL = 0;

    /** 파일의 처음으로 도달했을 때 VOD Event */
    public static final int VODEVENT_BEGIN_OF_FILE = 1;

    /** 파일의 끝에 도달했을 때 VOD Event */
    public static final int VODEVENT_END_OF_FILE = 2;

    /** 서버에서 정의한 pause timeout 시간에 도달했을 때 VOD Event */
    public static final int VODEVENT_PAUSE_TIMEOUT = 3;

    /** VOD 채널 전환 오류시 발행하는 VOD Event */
    public static final int VODEVENT_TUNNING_ERROR = 4;

    /** 세션 장애로 재튜닝이 필요할 때 VOD Event */
    public static final int VODEVENT_NEED_TUNE = 5;

    /**
     * 컨텐츠 재생 요청
     * <p>
     * 처음부터 재생은 offset : 0
     * <p>
     * 이어보기 재생은 offset : 위치값
     *
     * @param fileName
     *            컨텐츠 파일
     * @param TSList
     *            TS ID 값들
     * @param offset
     *            파일의 위치값 (단위:byte)
     * @param requestID
     *            RequestID
     * @param priorityCriteria
     *            사업자가 정한 Service Priority Criteria
     * @return int 요청에 대한 결과
     * @throws RemoteException
     */
    public int play(String fileName, int[] TSList, long offset, int requestID, int priorityCriteria) throws RemoteException;

    /**
     * 컨텐츠 재생 요청
     * <p>
     * 처음부터 재생은 offset : 0
     * <p>
     * 이어보기 재생은 offset : 위치값
     *
     * @param fileName
     *            컨텐츠 파일
     * @param TSList
     *            TS ID 값들
     * @param offset
     *            파일의 위치값 (단위:byte)
     * @param requestID
     *            RequestID
     * @return int 요청에 대한 결과
     * @throws RemoteException
     */
    public int play(String fileName, int[] TSList, long offset, int requestID) throws RemoteException;

    /**
     * 컨텐츠 재생 요청
     * <p>
     * 처음부터 재생은 offset : 0
     * <p>
     * 이어보기 재생은 offset : 위치값
     *
     * @param fileName
     *            컨텐츠 파일
     * @param nodeGroupID
     *            NodeGroupID 값
     * @param offset
     *            파일의 위치값 (단위:byte)
     * @param requestID
     *            RequestID
     * @return int 요청에 대한 결과
     * @throws RemoteException
     */
    public int play(String fileName, int nodeGroupID, long offset, int requestID) throws RemoteException;

    /**
     * 컨텐츠 재생 요청
     * <p>
     * 처음부터 재생은 offset : 0
     * <p>
     * 이어보기 재생은 offset : 위치값
     *
     * @param fileName
     *            컨텐츠 파일
     * @param nodegroupID
     *            NodeGroupID 값
     * @param offset
     *            파일의 위치값 (단위:byte)
     * @param requestID
     *            RequestID
     * @param priorityCriteria
     *            사업자가 정한 Service Priority Criteria
     * @return int 요청에 대한 결과
     * @throws RemoteException
     */
    public int play(String fileName, int nodegroupID, long offset, int requestID, int priorityCriteria) throws RemoteException;

    /**
     * 서버 로깅에 필요한 정보
     * <p>
     * 컨텐츠 재생 요청전에 호출해야 함
     *
     * @param name
     *            VODPlayer 를 사용하는 Application Name
     * @throws RemoteException
     */
    public void setApplicationName(String name) throws RemoteException;

    /**
     * Encryption 을 위한 ECMGroup 값 전달
     * <p>
     * 컨텐츠 재생 요청전에 호출해야 함
     *
     * @param ecm
     *            값이 유효하지 않을경우, Encryption 처리하지 않음
     * @throws RemoteException
     */
    public void setECMGroup(byte[] ecmgroup) throws RemoteException;

    /**
     * VODEvent 을 등록
     *
     * @param eventListener
     * @throws RemoteException
     */
    public void addVODEventListener(VODEventListener eventListener) throws RemoteException;

    /**
     * VODEvent 을 해지
     *
     * @param eventListener
     * @throws RemoteException
     */
    public void removeVODEventListener(VODEventListener eventListener) throws RemoteException;

    /**
     * VOD Channel 로 전환 요청
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @throws RemoteException
     */
    public void changeVODChannel(int x, int y, int width, int height) throws RemoteException;

    /**
     * 컨텐츠 배속 변경
     *
     * @param rate
     *            PLAY(1), PAUSE(0), FF(2,4,8,16,32,64) ,
     *            REW(-2,-4,-8,-16,-32,-64)
     * @return 요청에 대한 결과
     * @throws RemoteException
     */
    public boolean setRate(int rate) throws RemoteException;

    /**
     * byte 단위의 파일 위치를 이용한 컨텐츠 탐색
     * <p>
     * PAUSE or FF or REW 상태일 때는 요청 실패
     *
     * @param offset
     *            byte 단위의 재생 파일 위치
     * @return boolean 요청에 대한 결과
     * @throws RemoteException
     */
    public boolean setMediaOffset(long offset) throws RemoteException;

    /**
     * 재생중인 컨텐츠의 비율을 이용한 컨텐츠 탐색
     * <p>
     * PAUSE or FF or REW 상태일 때는 요청 실패
     *
     * @param ratio
     *            (0.0 ~ 1.0)
     * @return boolean 요청에 대한 결과
     * @throws RemoteException
     */
    public boolean setMediaRatio(double ratio) throws RemoteException;

    /**
     * 컨텐츠 재생 종료
     *
     * @throws RemoteException
     */
    public void stop() throws RemoteException;

    /**
     * 컨텐츠의 전체 시간 요청
     *
     * @return int 1/1000 초 단위
     * @throws RemoteException
     */
    public int getDuration() throws RemoteException;

    /**
     * 컨텐츠의 전체 File 크기 요청
     *
     * @return long byte 단위
     * @throws RemoteException
     */
    public long getFileSize() throws RemoteException;

    /**
     * 현재 재생 지점의 파일 위치값 요청
     *
     * @return long byte 단위
     * @throws RemoteException
     */
    public long getMediaOffset() throws RemoteException;

    /**
     * 현재 재생 지점의 비율을 요청
     *
     * @return double (0.0 ~ 1.0)
     * @throws RemoteException
     */
    public double getMediaRatio() throws RemoteException;

    /**
     * VOD Channel Tunning 주파수 정보
     *
     * @return 주파수
     * @throws RemoteException
     */
    public int getFrequency() throws RemoteException;

    /**
     * VOD Channel Tunning 프로그램 넘버
     *
     * @return 프로그램 넘버
     * @throws RemoteException
     */
    public int getProgramNumber() throws RemoteException;

}
