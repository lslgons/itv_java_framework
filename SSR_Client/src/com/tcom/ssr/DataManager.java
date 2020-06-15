package com.tcom.ssr;


import com.tcom.network.SSRConnector;
import com.tcom.network.SSRResponse;
import com.tcom.platform.controller.KeyController;
import com.tcom.platform.controller.MediaController;
import com.tcom.platform.controller.StbController;
import com.tcom.util.LOG;
import com.tcom.util.StringUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.print.attribute.standard.Media;
import java.awt.*;

public class DataManager {
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
    JSONObject jsonData;
    private String uid;
    //Context : 데이터를 서버로 전달하기 위해 유지해야 함 (Immutable)
    private static JSONObject context;
    private JSONObject component;
    private StateManager stateManager;
    private JSONArray intervalArr;
    private DataReceivedListener _listener;
    private static JSONObject getContext() {
        if(context==null) context=new JSONObject();
        return context;
    }
    public DataManager(DataReceivedListener listener) {
        this(listener, "");
    }
    public DataManager(DataReceivedListener listener, final String defaultUid) {

        uid=defaultUid;
        stateManager=new StateManager();
        component=new JSONObject();
        component.put("render", new JSONArray());
        component.put("element", new JSONArray());
        intervalArr=null;
        this._listener=listener;
    }

    private void allocateData() {
        this.uid=(String) this.jsonData.get("uid");
        context= (JSONObject) this.jsonData.get("context");
        this.component= (JSONObject) this.jsonData.get("component");
        this.intervalArr=(JSONArray) this.jsonData.get("interval");
        stateManager.setJSONObject((JSONObject) this.jsonData.get("state"));
    }

    public String getUid() {
        return this.uid;
    }

    public String getActivatedElementName() {
        return (String)((JSONObject)DataManager.getContext().get("_"+this.uid)).get("_activated_element");

    }

    public void setActivatedElementName(final String el_name) {
//        JSONObject comp_context = (JSONObject)DataManager.getContext().get("_"+this.uid);
//        comp_context.put("_activated_element", el_name);
        this.setContextData("_activated_element", el_name);

    }

    public Object getContextData(String key) {
        return ((JSONObject)DataManager.getContext().get("_"+this.uid)).get(key);
    }

    public void setContextData(String key, Object value) {
        JSONObject comp_context = (JSONObject)DataManager.getContext().get("_"+this.uid);
        comp_context.put(key, value);
    }


    public void changeContainer(String containerName) {
        JSONObject reqData = new JSONObject();
        reqData.put("uid", containerName);
        reqData.put("context", DataManager.getContext());
        //reqData.put("component", this.component); //컴포넌트 정보는 전달할 필요없음
        reqData.put("state", this.stateManager.getJSONObject());
        reqData.put("trigger_action", new Integer(ACTION_TRIGGER_NONE));
        reqData.put("trigger_target", "");
        SSRConnector.containerRequest(reqData, new SSRResponse() {
            public void onReceived(JSONObject response) {
                DataManager.this.jsonData=response;
                allocateData();
                DataManager.this._listener.onDataReceived();
            }

            public void onFailed(int status, String msg) {
                LOG.print(status+" error");
            }
        });
    }

    /**
     * 데이터 정보를 서버로부터 갱신
     */
    public void requestData(int trigger_action, String trigger_target) {
        final JSONObject reqData = new JSONObject();
        reqData.put("uid", this.uid);
        reqData.put("context", DataManager.getContext());
        //reqData.put("component", this.component); //컴포넌트 정보는 전달할 필요없음
        reqData.put("state", this.stateManager.getJSONObject());
        reqData.put("trigger_action", new Integer(trigger_action));
        reqData.put("trigger_target", trigger_target);
        reqData.put("interval", this.intervalArr);
        SSRConnector.containerRequest(reqData, new SSRResponse() {
            public void onReceived(JSONObject response) {
                if(((Long)response.get("status")).intValue()==2000) {
                    //OK
                    DataManager.this.jsonData=response;
                    allocateData();
                    DataManager.this._listener.onDataReceived();
                } else if(((Long)response.get("status")).intValue()==3000) {
                    //Redirect
                    String newHost= (String) response.get("host");
                    LOG.print("Dev Mode, Redirect to "+newHost);
                    String[] hostAndPort=StringUtil.tokenize(newHost, ":");
                    SSRConfig.getInstance().SSR_HOST=hostAndPort[0];
                    if(hostAndPort.length==1) {
                        SSRConfig.getInstance().SSR_PORT=80;
                    } else {
                        SSRConfig.getInstance().SSR_PORT=Integer.parseInt(hostAndPort[1]);
                    }
                    SSRConnector.containerRequest(reqData, new SSRResponse() {
                        public void onReceived(JSONObject response) {
                            if(((Long)response.get("status")).intValue()==2000) {
                                //OK
                                DataManager.this.jsonData=response;
                                allocateData();
                                DataManager.this._listener.onDataReceived();
                            } else if(((Long)response.get("status")).intValue()==3000) {
                                LOG.print("Error : Duplicate Redirection...");
                            } else {
                                //TODO Error Except
                            }
                        }
                        public void onFailed(int status, String msg) {
                            LOG.print(status+" error");
                        }
                    });
                } else {
                    //TODO Error Except
                }
            }
            public void onFailed(int status, String msg) {
                LOG.print(status+" error");
            }
        });
    }

    public JSONArray getRenderData() {
        return (JSONArray) this.component.get("render");

    }

    public JSONArray getElementData() {
        return (JSONArray) this.component.get("element");
    }

    public JSONArray getIntervalData() {
        return this.intervalArr;
//        //return (JSONArray) this.component.get("interval");
//        JSONObject cContext= (JSONObject) DataManager.getContext().get("_"+this.uid);
//        return (JSONArray) cContext.get("_intervals");
    }

    class StateManager {
        JSONObject stateObj;

        public StateManager() {
            StbController stb=StbController.getInstance();
            KeyController key=KeyController.getInstance();
            MediaController media = MediaController.getInstance();
            this.stateObj=new JSONObject();
            this.stateObj.put("vod_info", "");
            JSONArray av_size = new JSONArray();
            Rectangle real_av_size = media.getCurrentVideoSize();
            av_size.add(new Integer((int)real_av_size.getX()));
            av_size.add(new Integer((int)real_av_size.getY()));
            av_size.add(new Integer((int)real_av_size.getWidth()));
            av_size.add(new Integer((int)real_av_size.getHeight()));
            this.stateObj.put("av_size", av_size);
            this.stateObj.put("dmc_code", stb.getDMCCode());
            this.stateObj.put("so_code", stb.getSOCode());
            this.stateObj.put("stb_id", stb.getSmartcardID());
            JSONArray enabled_key=new JSONArray();
            enabled_key.add(new Integer(key.isEnableBackKey()?1:0));
            enabled_key.add(new Integer(key.isEnableNumKey()?1:0));
            enabled_key.add(new Integer(key.isEnableHotKey()?1:0));
            this.stateObj.put("key", enabled_key);
            //TODO AppID : 어플리케이션 종류에 따른 ID부여 필요성

        }

        public void setJSONObject(JSONObject obj) {

            //TODO 설정된 정보에 따라 STB상태 갱신
            JSONArray old_av= (JSONArray) stateObj.get("av_size");
            JSONArray new_av= (JSONArray) obj.get("av_size");
            if(old_av.get(0)!=new_av.get(0) ||old_av.get(1)!=new_av.get(1)||old_av.get(2)!=new_av.get(2)||old_av.get(3)!=new_av.get(3)) {
                stateObj.put("av_size", new_av);
                MediaController.getInstance().changeVideoSize(((Long)new_av.get(0)).intValue(),((Long)new_av.get(1)).intValue(),((Long)new_av.get(2)).intValue(),((Long)new_av.get(3)).intValue());
            }
            JSONArray old_key=(JSONArray) stateObj.get("key");
            JSONArray new_key=(JSONArray) obj.get("key");
            if(old_key.get(0)!=new_key.get(0)) {
                stateObj.put("key", new_key);
                KeyController.getInstance().setEnableBackKey(((Long)new_key.get(0)).intValue()==1);
            }
            if(old_key.get(1)!=new_key.get(1)) {
                stateObj.put("key", new_key);
                KeyController.getInstance().setEnableNumKey(((Long)new_key.get(1)).intValue()==1);
            }
            if(old_key.get(2)!=new_key.get(2)) {
                stateObj.put("key", new_key);
                KeyController.getInstance().setEnableHotKey(((Long)new_key.get(2)).intValue()==1);
            }
        }

        public int[] getAVSize() {
            JSONArray _avSize= (JSONArray) stateObj.get("av_size");
            int[] avSize={((Integer) _avSize.get(0)).intValue(),
                    ((Integer) _avSize.get(1)).intValue(),
                    ((Integer) _avSize.get(2)).intValue(),
                    ((Integer) _avSize.get(3)).intValue()};
            return avSize;
        }
        public String getVODAssetID() {
            return (String) stateObj.get("vod_info");
        }
        public void flushVODAssetID() {
            stateObj.put("vod_info", "");
        }

        public JSONObject getJSONObject() {
            return this.stateObj;
        }
    }

    interface DataReceivedListener {
        void onDataReceived();
    }

}
