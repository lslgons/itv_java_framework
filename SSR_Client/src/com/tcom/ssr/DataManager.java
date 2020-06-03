package com.tcom.ssr;


import com.tcom.network.SSRConnector;
import com.tcom.network.SSRResponse;
import com.tcom.platform.controller.KeyController;
import com.tcom.platform.controller.MediaController;
import com.tcom.platform.controller.StbController;
import com.tcom.util.LOG;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

    JSONObject jsonData;

    private String uid;
    //Context : 데이터를 서버로 전달하기 위해 유지해야 함 (Immutable)
    private JSONObject context;
    private JSONObject component;
    private StateManager stateManager;
    private DataReceivedListener _listener;

    public DataManager(DataReceivedListener listener) {
        this(listener, "");
    }
    public DataManager(DataReceivedListener listener, final String defaultUid) {

        uid=defaultUid;
        stateManager=new StateManager();
        context=new JSONObject();
        component=new JSONObject();
        component.put("render", new JSONArray());
        component.put("element", new JSONArray());
        this._listener=listener;
    }

    private void allocateData() {
        this.uid=(String) this.jsonData.get("uid");
        this.context= (JSONObject) this.jsonData.get("context");
        this.component= (JSONObject) this.jsonData.get("component");
        stateManager.setJSONObject((JSONObject) this.jsonData.get("state"));
    }

    public String getUid() {
        return this.uid;
    }

    public String getActivatedElementName() {
        return (String)((JSONObject)this.context.get("_"+this.uid)).get("_activated_element");

    }

    public void setActivatedElementName(final String el_name) {
        JSONObject comp_context = (JSONObject)this.context.get("_"+this.uid);
        comp_context.put("_activated_element", el_name);

    }



    /**
     * 데이터 정보를 서버로부터 갱신
     */
    public void requestData(int trigger_action, String trigger_target) {
        JSONObject reqData = new JSONObject();
        reqData.put("uid", this.uid);
        reqData.put("context", this.context);
        //reqData.put("component", this.component); //컴포넌트 정보는 전달할 필요없음
        reqData.put("state", this.stateManager.getJSONObject());
        reqData.put("trigger_action", new Integer(trigger_action));
        reqData.put("trigger_target", trigger_target);
        SSRConnector.ssrRequest(reqData, new SSRResponse() {
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

    public JSONArray getRenderData() {
        return (JSONArray) this.component.get("render");

    }

    public JSONArray getElementData() {
        return (JSONArray) this.component.get("element");
    }

    public JSONArray getIntervalData() {
        //return (JSONArray) this.component.get("interval");
        JSONObject cContext= (JSONObject) this.context.get("_"+this.uid);
        return (JSONArray) cContext.get("_intervals");
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
            this.stateObj=obj;
            //TODO 설정된 정보에 따라 STB상태 갱신
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
