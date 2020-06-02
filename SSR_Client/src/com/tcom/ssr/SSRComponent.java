package com.tcom.ssr;


import com.tcom.platform.controller.KeyCode;
import com.tcom.scene.BaseScene;
import com.tcom.util.LOG;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.tv.util.TVTimer;
import javax.tv.util.TVTimerScheduleFailedException;
import javax.tv.util.TVTimerSpec;
import javax.tv.util.TVTimerWentOffListener;
import java.awt.*;
import java.util.ArrayList;

public class SSRComponent extends BaseScene implements DataManager.DataReceivedListener, SSRInterval.IntervalTriggerListener {
    SSRContainer ssrContainer;
    DataManager dataManager;
    ArrayList renderList;
    ArrayList elementList;
    ArrayList intervalList;
    SSRElement activatedElement;

//    TVTimerSpec timerSpec;

    public SSRComponent(SSRContainer container) {
        super();
        this.ssrContainer=container;
        //this.dataManager=new DataManager(this);
        this.renderList=new ArrayList();
        this.elementList = new ArrayList();
        this.intervalList = new ArrayList();

//        timerSpec = new TVTimerSpec();
//        timerSpec.setAbsolute(true);
//        timerSpec.setDelayTime(TIMER_DELAY);
//        timerSpec.setRepeat(true);
//        try {
//            TVTimerSpec actual = TVTimer.getTimer().scheduleTimerSpec(this.timerSpec);
//            this.timerSpec.setTime(actual.getTime());
//        } catch (TVTimerScheduleFailedException e) {
//            e.printStackTrace();
//        }

    }

    public void requestData(String uid) {
        this.dataManager=new DataManager(this, uid);
        //화면 구성
        this.dataManager.requestData(DataManager.ACTION_TRIGGER_NONE, "");
    }

    /**
     * 화면 갱신
     */
    public void invalidate() {
        //TODO
        this.renderList.clear();
        this.elementList.clear();
    }

    public void onInit() {

    }

    public void onShow() {

    }

    public void onHide() {

    }

    public void onDestroy() {
//        for(int i=0;i<intervalList.size();++i) {
//            timerSpec.removeTVTimerWentOffListener((TVTimerWentOffListener) intervalList.get(i));
//        }
//        TVTimer.getTimer().deschedule(this.timerSpec);
    }

    public void onKeyDown(int keycode) {
        LOG.print("["+this.dataManager.getUid()+"] : onKeyDown => "+keycode);
        int action_index=-1;
        int key_mapping=0;
        switch(keycode) {

            case KeyCode.VK_UP:
                action_index=0;
                key_mapping=DataManager.ACTION_TRIGGER_UP;
                break;
            case KeyCode.VK_RIGHT:
                action_index=1;
                key_mapping=DataManager.ACTION_TRIGGER_RIGHT;
                break;
            case KeyCode.VK_DOWN:
                action_index=2;
                key_mapping=DataManager.ACTION_TRIGGER_DOWN;
                break;
            case KeyCode.VK_LEFT:
                action_index=3;
                key_mapping=DataManager.ACTION_TRIGGER_LEFT;
                break;
            case KeyCode.VK_OK:
                action_index=4;
                key_mapping=DataManager.ACTION_TRIGGER_OK;
                break;
            case KeyCode.VK_BACK:
                action_index=5;
                key_mapping=DataManager.ACTION_TRIGGER_BACK;
                break;
            default:
                break;


        }
        if(action_index>-1) {
            SSRElement.SSRAction action= (SSRElement.SSRAction) activatedElement.actions.get(action_index);
            /**
             * FORMAT_ACTION_TYPE_ACTIVATE=0 #엘리먼트 액션 활성화
             * FORMAT_ACTION_TYPE_REFRESH=1  #서버 액션 렌더 요청 -> 렌더링 재수행
             * FORMAT_ACTION_TYPE_COMPONENT=2 #다른 컴포넌트로의 이동
             * FORMAT_ACTION_TYPE_OVERLAY=3 #오버레이 활성화
             * FORMAT_ACTION_TYPE_CLOSE=4 #메인 컴포넌트의 경우 앱 종료, 오버레이의 경우 오버레이 사라짐
             */
            if(action.type==0) {
                String target= (String) action.arguments.get(0);
                this.dataManager.setActivatedElementName(target);
                this.activatedElement=findElementByName(target);
                repaint();

            } else if(action.type==1) {
                //Refresh

                this.dataManager.requestData(key_mapping, activatedElement.name);
            }
        }
    }

    public SSRElement findElementByName(String name) {
        for(int i=0;i<this.elementList.size(); ++i) {
            SSRElement el= (SSRElement) this.elementList.get(i);
            if(el.name.equalsIgnoreCase(name)) return el;
        }
        return null;
    }

    public void onPaint(Graphics g) {
        //1. DrawRender
        for(int i=0;i<this.renderList.size(); ++i) {
            SSRRender render = (SSRRender) this.renderList.get(i);
            render.draw(g);
        }
        //2. Draw Element
        for(int j=0;j<this.elementList.size(); ++j) {
            SSRElement element = (SSRElement) this.elementList.get(j);
            element.draw(g, this.dataManager.getActivatedElementName().equalsIgnoreCase(element.name));
        }

    }

    public void timerWentOff() {
        for(int i=0;i<intervalList.size();++i) {
            ((SSRInterval)intervalList.get(i)).timerWentOff();
        }
    }

    //Interface
    public void onDataReceived() {
        this.renderList.clear();
        this.elementList.clear();
        JSONArray renderData = this.dataManager.getRenderData();
        for(int i=0;i<renderData.size();++i) {
            SSRRender render = new SSRRender(this, (JSONObject) renderData.get(i));
            this.renderList.add(render);
        }
        JSONArray elementData = this.dataManager.getElementData();
        for(int j=0;j<elementData.size();++j) {
            SSRElement element = new SSRElement(this, (JSONObject) elementData.get(j));
            this.elementList.add(element);
            if(this.dataManager.getActivatedElementName().equalsIgnoreCase(element.name)) this.activatedElement=element;
        }

        JSONArray intervals = this.dataManager.getIntervalData();
        for(int k=0;k<intervals.size(); ++k) {
            JSONObject interval = (JSONObject) intervals.get(k);
            String interval_id=(String)interval.get("interval_id");
            boolean isAllocated=false;
            for(int l=0; l<intervalList.size(); ++l) {
                SSRInterval ssrInterval= (SSRInterval) intervalList.get(l);
                if(interval_id.equals(ssrInterval.getIntervalID())) {
                    isAllocated=true;
                    ssrInterval.setEnable(((Boolean)interval.get("interval_activated")).booleanValue());
                }
            }
            if(!isAllocated) {
                int period = ((Long) interval.get("interval_period")).intValue();
                boolean enabled=((Boolean)interval.get("interval_activated")).booleanValue();
                SSRInterval ssrInterval = new SSRInterval(interval_id, period, enabled, this);
                intervalList.add(ssrInterval);
            }

        }


        repaint();
    }

    public void onIntervalTriggered(String intervalID) {
        this.dataManager.requestData(DataManager.ACTION_TRIGGER_INTERVAL, intervalID);
    }
}
