package com.tcom.ssr.manager;


import com.tcom.network.SSRConnector;
import com.tcom.network.SSRResponse;
import com.tcom.platform.controller.KeyController;
import com.tcom.platform.controller.MediaController;
import com.tcom.platform.controller.StbController;
import com.tcom.ssr.SSRConfig;
import com.tcom.ssr.SSRConstant;
import com.tcom.util.LOG;
import com.tcom.util.StringUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;

public class DataManager {

    JSONObject jsonData;
    private String uid;
    private boolean loadingComponent;
    //Context : 데이터를 서버로 전달하기 위해 유지해야 함 (Immutable)
    private static JSONObject context;
    private JSONObject loadingContext;
    private JSONObject component;
    private StateManager stateManager;
    private JSONArray intervalArr;
    private DataReceivedListener _listener;
    private JSONObject getContext() {
        if(loadingComponent) {
            if(loadingContext==null) this.loadingContext=new JSONObject();
            return this.loadingContext;
        }
        else {
            if(DataManager.context==null) DataManager.context=new JSONObject();
            return DataManager.context;
        }
//        if(context==null) context=new JSONObject();
//        return context;
    }
    public DataManager(DataReceivedListener listener) {
        this(listener, "");
    }

    public DataManager(DataReceivedListener listener, final String defaultUid) {
        this(listener, defaultUid, false);
    }

    public DataManager(DataReceivedListener listener, final String defaultUid, boolean loadingComponent) {

        uid=defaultUid;
        stateManager=new StateManager();
        component=new JSONObject();
        component.put("render", new JSONArray());
        component.put("element", new JSONArray());
        intervalArr=new JSONArray();
        this.loadingComponent=loadingComponent;
        this._listener=listener;
    }

    private void allocateData() {
        this.uid=(String) this.jsonData.get("uid");
        if(this.loadingComponent) {
            loadingContext=(JSONObject) this.jsonData.get("context");
        } else {
            context= (JSONObject) this.jsonData.get("context");
        }

        this.component= (JSONObject) this.jsonData.get("component");
        this.intervalArr=(JSONArray) this.jsonData.get("interval");
        stateManager.setJSONObject((JSONObject) this.jsonData.get("state"));
    }

    public String getUid() {
        return this.uid;
    }

    public String getActivatedElementName() {
        return (String)((JSONObject)getContext().get("_"+this.uid)).get("_activated_element");
    }

    public void setActivatedElementName(final String el_name) {
//        JSONObject comp_context = (JSONObject)DataManager.getContext().get("_"+this.uid);
//        comp_context.put("_activated_element", el_name);
        this.setContextData("_activated_element", el_name);

    }

    public Object getContextData(String key) {
        return ((JSONObject)getContext().get("_"+this.uid)).get(key);
    }

    public void setContextData(String key, Object value) {
        JSONObject comp_context = (JSONObject)getContext().get("_"+this.uid);
        comp_context.put(key, value);
    }

    public void removeComponentData() {
        ((JSONObject)getContext()).remove("_"+this.uid);
    }

    public void changeContainer(String containerName) {
        JSONObject reqData = new JSONObject();
        reqData.put("uid", containerName);
        reqData.put("context", getContext());
        //reqData.put("component", this.component); //컴포넌트 정보는 전달할 필요없음
        reqData.put("state", this.stateManager.getJSONObject());
        reqData.put("trigger_action", new Integer(SSRConstant.ACTION_TRIGGER_NONE));
        reqData.put("trigger_target", "");
        _listener.onDataRequestStart();
        SSRConnector.containerRequest(reqData, new SSRResponse() {
            public void onReceived(JSONObject response) {
                DataManager.this.jsonData=response;
                allocateData();
                DataManager.this._listener.onDataReceived(((Long)response.get("status")).intValue());
            }

            public void onFailed(int status, String msg) {
                _listener.onDataRequestFailed();
                LOG.print(status+" error");
            }
        });
    }

    /**
     * 데이터 정보를 서버로부터 갱신
     */
    public void requestData(int trigger_action, String trigger_target) {
        _listener.onDataRequestStart();
        final JSONObject reqData = new JSONObject();
        reqData.put("uid", this.uid);
        reqData.put("context", getContext());
        //reqData.put("component", this.component); //컴포넌트 정보는 전달할 필요없음
        reqData.put("state", this.stateManager.getJSONObject());
        reqData.put("trigger_action", new Integer(trigger_action));
        reqData.put("trigger_target", trigger_target);
        if(this.intervalArr!=null && this.intervalArr.size()>0) {
            reqData.put("interval", this.intervalArr);
        }

        String uri="container/";
        if(this.loadingComponent) uri="loading/";
        SSRConnector.ssrRequest(uri, reqData, new SSRResponse() {
            public void onReceived(JSONObject response) {
                if(((Long)response.get("status")).intValue()==2000) {
                    //OK
                    DataManager.this.jsonData=response;
                    allocateData();
                    DataManager.this._listener.onDataReceived(2000);
                } else if(((Long)response.get("status")).intValue()==2100) {
                    //Dismiss overlay and refresh
                    DataManager.this.jsonData=response;
                    allocateData();
                    DataManager.this._listener.onDataReceived(2100);

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
                                DataManager.this._listener.onDataReceived(2000);
                            } else if(((Long)response.get("status")).intValue()==3000) {
                                LOG.print("Error : Duplicate Redirection...");
                            } else {
                                //TODO Error Except
                                _listener.onDataRequestFailed();
                            }
                        }
                        public void onFailed(int status, String msg)
                        {
                            LOG.print(status+" error");
                            _listener.onDataRequestFailed();
                        }
                    });
                } else {
                    //TODO Error Except
                }
            }
            public void onFailed(int status, String msg) {
                _listener.onDataRequestFailed();
                LOG.print(status+" error");
            }
        });
        _listener.onDataRequestComplete();
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
    public interface DataReceivedListener {
        void onDataRequestStart();
        void onDataRequestComplete();
        void onDataRequestFailed();
        void onDataReceived(int status);
    }

}
