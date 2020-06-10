package com.tcom.ssr;


import com.tcom.platform.controller.KeyCode;
import com.tcom.scene.BaseScene;
import com.tcom.ssr.action.SSRAction;
import com.tcom.ssr.action.SSRInterval;
import com.tcom.ssr.element.SSRElement;
import com.tcom.ssr.element.SSRInputElement;
import com.tcom.ssr.element.SSRSelectElement;
import com.tcom.util.LOG;
import com.tcom.xlet.MainXlet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.ArrayList;

public class SSRComponent extends BaseScene implements DataManager.DataReceivedListener, SSRInterval.IntervalTriggerListener {
    private SSRContainer ssrContainer;
    private DataManager dataManager;
    private ArrayList renderList;
    private ArrayList elementList;
    private ArrayList intervalList;
    private SSRElement activatedElement;
    final private boolean isOverlay;

//    TVTimerSpec timerSpec;

    public SSRComponent(SSRContainer container, boolean isOverlay) {
        super();
        this.ssrContainer=container;
        this.renderList=new ArrayList();
        this.elementList = new ArrayList();
        this.intervalList = new ArrayList();
        this.isOverlay=isOverlay;
        setBounds(0,0,960,540);

    }

    public void requestData(String uid) {
        this.dataManager=new DataManager(this, uid);
        //화면 구성
        if(uid==null) this.dataManager.requestData(DataManager.ACTION_TRIGGER_NONE, "");
        else this.dataManager.requestData(DataManager.ACTION_TRIGGER_NONE, uid);
    }

    /**
     * 화면 갱신
     */
    public void invalidate() {
        //TODO
        this.renderList.clear();
        this.elementList.clear();
        this.intervalList.clear();
        repaint();
    }

    public void onInit() {

    }

    public void onShow() {

    }

    public void onHide() {

    }

    public void onDestroy() {

    }

    public void processAction(int action_key, SSRAction action) {
        /**
         * FORMAT_ACTION_TYPE_ACTIVATE=0 #엘리먼트 액션 활성화
         * FORMAT_ACTION_TYPE_REFRESH=1  #서버 액션 렌더 요청 -> 렌더링 재수행
         * FORMAT_ACTION_TYPE_COMPONENT=2 #다른 컴포넌트로의 이동
         * FORMAT_ACTION_TYPE_OVERLAY=3 #오버레이 활성화
         * FORMAT_ACTION_TYPE_CLOSE=4 #메인 컴포넌트의 경우 앱 종료, 오버레이의 경우 오버레이 사라짐
         */
        if(action==null) {
            LOG.print("No action defined");
            return;
        }
        switch(action.getType()) {
            case DataManager.FORMAT_ACTION_TYPE_ACTIVATE:
                String target= (String) action.getArguments().get(0);
                this.dataManager.setActivatedElementName(target);
                this.activatedElement=findElementByName(target);
                repaint();
                break;
            case DataManager.FORMAT_ACTION_TYPE_REFRESH:
                //Refresh
                this.dataManager.requestData(action_key, activatedElement.getName());
                break;
            case DataManager.FORMAT_ACTION_TYPE_COMPONENT:
                //Clear Interval
                LOG.print("Component change to "+action.getArguments().get(0));
                this.intervalList.clear();
                this.dataManager.changeContainer((String) action.getArguments().get(0));
                break;
            case DataManager.FORMAT_ACTION_TYPE_OVERLAY:
                this.ssrContainer.enableOverlay((String) action.getArguments().get(0));
                break;
            case DataManager.FORMAT_ACTION_TYPE_CLOSE:
                if(this.isOverlay) {
                    //TODO 오버레이 종료
                    this.ssrContainer.disableOverlay();
                } else {
                    //TODO 어플리케이션 종료
                    MainXlet.closeApp();
                }
                break;

            default:
                LOG.print("Unknown Action");
        }
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

        switch(activatedElement.getType()) {
            case SSRElement.ELEMENT_TYPE_NORMAL:
                if(action_index>-1) {
                    processAction(key_mapping, (SSRAction) activatedElement.getAction(action_index));
                }

                break;
            case SSRElement.ELEMENT_TYPE_INPUT:
            case SSRElement.ELEMENT_TYPE_PASSWORD_INPUT:
                SSRInputElement input = (SSRInputElement) activatedElement;
                if(keycode>=KeyCode.VK_0 && keycode<=KeyCode.VK_9) {
                    if(input.getInputData().length()==input.getMaxInputSize()) {
                        SSRAction onMaxInputAction=input.getOnMaxInputAction();
                        processAction(key_mapping, onMaxInputAction);
                    } else {
                        input.setInputData(input.getInputData()+(keycode-48));
                        //입력수가 최대치에 도달하면 Action 수행
                        if(input.getInputData().length()==input.getMaxInputSize()) {
                            SSRAction onMaxInputAction=input.getOnMaxInputAction();
                            processAction(key_mapping, onMaxInputAction);
                        }
                    }

                } else if(keycode==KeyCode.VK_LEFT) {
                    String inputData = input.getInputData();
                    if (inputData.length()>0) {
                        input.setInputData(inputData.substring(0, inputData.length()-1));
                    } else if(inputData.length()==0) {
                        processAction(key_mapping, input.getOnInitInputAction());
                    }
                } else {
                    if(action_index>-1) {
                        processAction(key_mapping, (SSRAction) activatedElement.getAction(action_index));
                    }
                }
                repaint();
                break;
            case SSRElement.ELEMENT_TYPE_SELECT:
                SSRSelectElement select = (SSRSelectElement) activatedElement;
                if(select.isHorizontalSelection()) {
                    //Horizontal selection
                    if(keycode==KeyCode.VK_LEFT) {
                        int selected_index=select.getSelectedIndex();
                        if(selected_index==0) {
                            processAction(key_mapping, select.getOnSelectFirst());
                        } else {
                            select.setSelectedIndex(selected_index-1);
                        }
                    } else if(keycode==KeyCode.VK_RIGHT) {
                        int selected_index=select.getSelectedIndex();
                        if(selected_index==select.getSelectableValues().length-1) {
                            processAction(key_mapping, select.getOnSelectEnd());
                        } else {
                            select.setSelectedIndex(selected_index+1);
                        }

                    } else {
                        if(action_index>-1) {
                            processAction(key_mapping, (SSRAction) activatedElement.getAction(action_index));
                        }
                    }
                } else {
                    if(keycode==KeyCode.VK_UP) {
                        int selected_index=select.getSelectedIndex();
                        if(selected_index==0) {
                            processAction(key_mapping, select.getOnSelectFirst());
                        } else {
                            select.setSelectedIndex(selected_index-1);
                        }
                    } else if(keycode==KeyCode.VK_DOWN) {
                        int selected_index=select.getSelectedIndex();
                        if(selected_index==select.getSelectableValues().length-1) {
                            processAction(key_mapping, select.getOnSelectEnd());
                        } else {
                            select.setSelectedIndex(selected_index+1);
                        }

                    } else {
                        if(action_index>-1) {
                            processAction(key_mapping, (SSRAction) activatedElement.getAction(action_index));
                        }
                    }
                }

                repaint();
                break;
            case SSRElement.ELEMENT_TYPE_SCROLL:
                break;
            default:
                LOG.print("Unknown element");
        }

    }

    public Object getContextData(String key) {
        return this.dataManager.getContextData(key);
    }

    public void setContextData(String key, Object value) {
        this.dataManager.setContextData(key, value);
    }

    public SSRElement findElementByName(String name) {
        for(int i=0;i<this.elementList.size(); ++i) {
            SSRElement el= (SSRElement) this.elementList.get(i);
            if(el.getName().equalsIgnoreCase(name)) return el;
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
            element.draw(g, this.dataManager.getActivatedElementName().equalsIgnoreCase(element.getName()));
        }

    }

    public void timerWentOff() {
        for(int i=0;i<intervalList.size();++i) {
            ((SSRInterval)intervalList.get(i)).timerWentOff();
        }
    }

    //Interface
    public void onDataReceived() {
        LOG.print("onDataReceived, Refresh Frame");
        this.renderList.clear();
        this.elementList.clear();
        JSONArray renderData = this.dataManager.getRenderData();
        for(int i=0;i<renderData.size();++i) {
            SSRRender render = new SSRRender(this, (JSONObject) renderData.get(i));
            this.renderList.add(render);
        }
        JSONArray elementData = this.dataManager.getElementData();
        for(int j=0;j<elementData.size();++j) {
            int elementType = ((Long) ((JSONObject)elementData.get(j)).get("type")).intValue();
            switch(elementType) {
                case SSRElement.ELEMENT_TYPE_NORMAL:
                    SSRElement element = new SSRElement(this, (JSONObject) elementData.get(j));
                    this.elementList.add(element);
                    if(this.dataManager.getActivatedElementName().equalsIgnoreCase(element.getName())) this.activatedElement=element;
                    break;
                case SSRElement.ELEMENT_TYPE_INPUT:
                case SSRElement.ELEMENT_TYPE_PASSWORD_INPUT:
                    SSRInputElement inputElement = new SSRInputElement(this, (JSONObject) elementData.get(j));
                    this.elementList.add(inputElement);
                    if(elementType==SSRElement.ELEMENT_TYPE_PASSWORD_INPUT) inputElement.setPasswordField(true);
                    if(this.dataManager.getActivatedElementName().equalsIgnoreCase(inputElement.getName())) this.activatedElement=inputElement;
                    break;
                case SSRElement.ELEMENT_TYPE_SELECT:
                    SSRSelectElement selectElement = new SSRSelectElement(this, (JSONObject) elementData.get(j));
                    this.elementList.add(selectElement);
                    if(this.dataManager.getActivatedElementName().equalsIgnoreCase(selectElement.getName())) this.activatedElement=selectElement;
                    break;


            }

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
