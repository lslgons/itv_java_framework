package com.tcom.ssr;

public class SSRConstant {
    final public static int ACTION_TRIGGER_NUM_0=0;
    final public static int ACTION_TRIGGER_NUM_1=1;
    final public static int ACTION_TRIGGER_NUM_2=2;
    final public static int ACTION_TRIGGER_NUM_3=3;
    final public static int ACTION_TRIGGER_NUM_4=4;
    final public static int ACTION_TRIGGER_NUM_5=5;
    final public static int ACTION_TRIGGER_NUM_6=6;
    final public static int ACTION_TRIGGER_NUM_7=7;
    final public static int ACTION_TRIGGER_NUM_8=8;
    final public static int ACTION_TRIGGER_NUM_9=9;

    final public static int ACTION_TRIGGER_COLOR_RED=20;
    final public static int ACTION_TRIGGER_COLOR_GREEN=21;
    final public static int ACTION_TRIGGER_COLOR_YELLOW=22;
    final public static int ACTION_TRIGGER_COLOR_BLUE=23;
    final public static int ACTION_TRIGGER_UP=10;
    final public static int ACTION_TRIGGER_RIGHT=11;
    final public static int ACTION_TRIGGER_DOWN=12;
    final public static int ACTION_TRIGGER_LEFT=13;
    final public static int ACTION_TRIGGER_OK=14;
    final public static int ACTION_TRIGGER_BACK=15;
    final public static int ACTION_TRIGGER_EXIT=16;

    final public static int ACTION_TRIGGER_INTERVAL=90;
    final public static int ACTION_TRIGGER_INIT=91;

    final public static int ACTION_TRIGGER_NONE=99;

    final public static int FORMAT_ACTION_TYPE_ACTIVATE=0; //엘리먼트 액션 활성화
    final public static int FORMAT_ACTION_TYPE_REFRESH=1;  //서버 액션 렌더 요청 -> 렌더링 재수행
    final public static int FORMAT_ACTION_TYPE_COMPONENT=2; //다른 컴포넌트로의 이동
    final public static int FORMAT_ACTION_TYPE_OVERLAY=3; //오버레이 활성화
    //TODO CLOSE액션이라 하더라도 CALLBACK수행/앱 종료 알림을 위해 서버에 CLOSE ACTION Event 전달, 이후 닫힌 컴포넌트의 컨텍스트 정보는 서버에서 제거 함
    final public static int FORMAT_ACTION_TYPE_CLOSE=4; //메인 컴포넌트의 경우 앱 종료, 오버레이의 경우 오버레이 사라짐
    //final public static int FORMAT_ACTION_TYPE_CALLBACK=5; //TODO 오버레이 종료 후 메인 컴포넌트 콜백 요청
    final public static int FORMAT_ACTION_TYPE_PROPAGATE=6; // 다른 엘리먼트의 이벤트로 전이시킴
    final public static int FORMAT_ACTION_TYPE_PROPAGATE_ACTIVATED=7; // 현재 활성화된 엘리먼트로 이벤트 전이시킴

    final public static int COMPONENT_MODE_NORMAL=0;
    final public static int COMPONENT_MODE_OVERLAY=1;
    final public static int COMPONENT_MODE_LOADING=2;

}
