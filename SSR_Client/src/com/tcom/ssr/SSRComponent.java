package com.tcom.ssr;


import com.tcom.platform.controller.KeyCode;
import com.tcom.scene.BaseScene;
import com.tcom.ssr.action.SSRAction;
import com.tcom.ssr.action.SSRInterval;
import com.tcom.ssr.element.SSRElement;
import com.tcom.ssr.element.SSRInputElement;
import com.tcom.ssr.element.SSRSelectElement;
import com.tcom.ssr.manager.DataManager;
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
    final private int componentMode;
    private boolean isIntervalProcessing;


    public SSRComponent(SSRContainer container, int componentMode) {
        super();
        this.ssrContainer=container;
        this.renderList=new ArrayList();
        this.elementList = new ArrayList();
        this.intervalList = new ArrayList();
        this.componentMode=componentMode;
        isIntervalProcessing=true;
        setBounds(0,0,960,540);
        if(componentMode==SSRConstant.COMPONENT_MODE_LOADING) {
            this.requestData("");
        }

    }

    public void requestData(String uid) {
        this.dataManager=new DataManager(this, uid, this.componentMode==SSRConstant.COMPONENT_MODE_LOADING);
        //화면 구성
        if(uid==null) this.dataManager.requestData(SSRConstant.ACTION_TRIGGER_INIT, "");
        else this.dataManager.requestData(SSRConstant.ACTION_TRIGGER_INIT, uid);
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

    public void processAction(int action_key, int action_index, SSRAction action) {
        processAction(action_key, action_index, action, activatedElement.getName());
    }

    public void processAction(int action_key, int action_index, SSRAction action, String elementName) {
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
        LOG.print("process Action... : "+action.getType());
        switch(action.getType()) {
            case SSRConstant.FORMAT_ACTION_TYPE_ACTIVATE:
                String target= (String) action.getArguments().get(0);
                SSRElement targetEl=findElementByName(target);
                if(targetEl==null) {
                    LOG.print("There is no target...");
                    return;
                } else {
                    this.dataManager.setActivatedElementName(target);
                    this.activatedElement=targetEl;
                    repaint();
                }
                break;
            case SSRConstant.FORMAT_ACTION_TYPE_REFRESH:
                //Refresh
                this.dataManager.requestData(action_key, elementName);
                break;
            case SSRConstant.FORMAT_ACTION_TYPE_COMPONENT:
                //Clear Interval
                LOG.print("Component change to "+action.getArguments().get(0));
                this.intervalList.clear();

                try {
                    int removeContextData = ((Long) action.getArguments().get(1)).intValue();
                    if(removeContextData==1) {
                        //Clear data
                        this.dataManager.removeComponentData();
                    }
                } catch(IndexOutOfBoundsException e) {
                    LOG.print("No option data");
                }

                this.dataManager.changeContainer((String) action.getArguments().get(0));
                break;
            case SSRConstant.FORMAT_ACTION_TYPE_OVERLAY:
                
                this.ssrContainer.enableOverlay((String) action.getArguments().get(0));
                break;
            case SSRConstant.FORMAT_ACTION_TYPE_CLOSE:
                //Context data 삭제
                try {
                    int removeContextData = ((Long) action.getArguments().get(0)).intValue();
                    if(removeContextData==1) {
                        //Clear data
                        this.dataManager.removeComponentData();
                    }
                } catch(IndexOutOfBoundsException e) {
                    LOG.print("No option data");
                }
                if(this.componentMode==SSRConstant.COMPONENT_MODE_OVERLAY) {
                    //TODO 오버레이 종료
                    this.ssrContainer.disableOverlay();
                } else if(this.componentMode==SSRConstant.COMPONENT_MODE_NORMAL) {
                    //TODO 어플리케이션 종료
                    MainXlet.closeApp();
                } else {
                    //TODO default
                }
                break;
            case SSRConstant.FORMAT_ACTION_TYPE_PROPAGATE:
                SSRElement targetElement = findElementByName((String) action.getArguments().get(0));
                SSRAction action1=targetElement.getAction(action_index);
                this.processAction(action_key, action_index, action1);
                //repaint();
                break;
            case SSRConstant.FORMAT_ACTION_TYPE_PROPAGATE_ACTIVATED:
                SSRAction action2=activatedElement.getAction(action_index);
                this.processAction(action_key, action_index, action2);
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
                key_mapping=SSRConstant.ACTION_TRIGGER_UP;
                break;
            case KeyCode.VK_RIGHT:
                action_index=1;
                key_mapping=SSRConstant.ACTION_TRIGGER_RIGHT;
                break;
            case KeyCode.VK_DOWN:
                action_index=2;
                key_mapping=SSRConstant.ACTION_TRIGGER_DOWN;
                break;
            case KeyCode.VK_LEFT:
                action_index=3;
                key_mapping=SSRConstant.ACTION_TRIGGER_LEFT;
                break;
            case KeyCode.VK_OK:
                action_index=4;
                key_mapping=SSRConstant.ACTION_TRIGGER_OK;
                break;
            case KeyCode.VK_BACK:
                action_index=5;
                key_mapping=SSRConstant.ACTION_TRIGGER_BACK;
                break;
            default:
                break;
        }

        switch(activatedElement.getType()) {
            case SSRElement.ELEMENT_TYPE_NORMAL:
                if(action_index>-1) {
                    processAction(key_mapping, action_index, (SSRAction) activatedElement.getAction(action_index));
                }

                break;
            case SSRElement.ELEMENT_TYPE_INPUT:
            case SSRElement.ELEMENT_TYPE_PASSWORD_INPUT:
                SSRInputElement input = (SSRInputElement) activatedElement;
                if(keycode>=KeyCode.VK_0 && keycode<=KeyCode.VK_9) {
                    if(input.getInputData().length()==input.getMaxInputSize()) {
                        SSRAction onMaxInputAction=input.getOnMaxInputAction();
                        processAction(key_mapping, action_index, onMaxInputAction);
                    } else {
                        input.setInputData(input.getInputData()+(keycode-48));
                        //입력수가 최대치에 도달하면 Action 수행
                        if(input.getInputData().length()==input.getMaxInputSize()) {
                            SSRAction onMaxInputAction=input.getOnMaxInputAction();
                            processAction(key_mapping, action_index, onMaxInputAction);
                        }
                    }

                } else if(keycode==KeyCode.VK_LEFT) {
                    String inputData = input.getInputData();
                    if (inputData.length()>0) {
                        input.setInputData(inputData.substring(0, inputData.length()-1));
                    } else if(inputData.length()==0) {
                        processAction(key_mapping, action_index, input.getOnInitInputAction());
                    }
                } else {
                    if(action_index>-1) {
                        processAction(key_mapping, action_index, (SSRAction) activatedElement.getAction(action_index));
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
                            processAction(key_mapping, action_index, select.getOnSelectFirst());
                        } else {
                            select.setSelectedIndex(selected_index-1);
                        }
                    } else if(keycode==KeyCode.VK_RIGHT) {
                        int selected_index=select.getSelectedIndex();
                        if(selected_index==select.getSelectableValues().length-1) {
                            processAction(key_mapping, action_index, select.getOnSelectEnd());
                        } else {
                            select.setSelectedIndex(selected_index+1);
                        }

                    } else {
                        if(action_index>-1) {
                            processAction(key_mapping, action_index, (SSRAction) activatedElement.getAction(action_index));
                        }
                    }
                } else {
                    if(keycode==KeyCode.VK_UP) {
                        int selected_index=select.getSelectedIndex();
                        if(selected_index==0) {
                            processAction(key_mapping, action_index, select.getOnSelectFirst());
                        } else {
                            select.setSelectedIndex(selected_index-1);
                        }
                    } else if(keycode==KeyCode.VK_DOWN) {
                        int selected_index=select.getSelectedIndex();
                        if(selected_index==select.getSelectableValues().length-1) {
                            processAction(key_mapping, action_index, select.getOnSelectEnd());
                        } else {
                            select.setSelectedIndex(selected_index+1);
                        }

                    } else {
                        if(action_index>-1) {
                            processAction(key_mapping, action_index, (SSRAction) activatedElement.getAction(action_index));
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

    public void stopInterval() {
        this.isIntervalProcessing=false;
    }

    public void startInterval() {
        for(int i=0;i<intervalList.size();++i) {
            ((SSRInterval)intervalList.get(i)).resetStep();
        }
        this.isIntervalProcessing=true;
    }

    public boolean isIntervalProcessing() {
        return this.isIntervalProcessing;
    }

    public void timerWentOff() {
        if(isIntervalProcessing) {
            for(int i=0;i<intervalList.size();++i) {
                ((SSRInterval)intervalList.get(i)).timerWentOff();
            }
        }

    }

    public void onDataRequestStart() {
        LOG.print(this, "onDataRequestStart");
        if(componentMode!=SSRConstant.COMPONENT_MODE_LOADING) this.ssrContainer.showLoading(true);
    }

    public void onDataRequestComplete() {
        if(componentMode!=SSRConstant.COMPONENT_MODE_LOADING) this.ssrContainer.showLoading(false);
    }

    public void onDataRequestFailed() {
        if(componentMode!=SSRConstant.COMPONENT_MODE_LOADING) this.ssrContainer.showLoading(false);
    }

    public void refreshComponent() {
        this.dataManager.requestData(SSRConstant.ACTION_TRIGGER_NONE, this.dataManager.getUid());
    }

    //Interface
    public void onDataReceived(int status) {
        LOG.print("onDataReceived, Refresh Frame");
        if(status==2100) {
            //Dismiss overlay
            this.ssrContainer.disableOverlay();
            //Refresh Main component
            //Refresh
            this.ssrContainer.mainComponent.refreshComponent();
        } else if(status==2000) {
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
                        ssrInterval.setObject(interval);
                    }
                }
                if(!isAllocated) {
                    intervalList.add(new SSRInterval(interval, this));
                }

            }

            if(componentMode!=SSRConstant.COMPONENT_MODE_LOADING) this.ssrContainer.showLoading(false);
            repaint();
        }

    }

    public void onIntervalTriggered(SSRInterval interval) {
        processAction(SSRConstant.ACTION_TRIGGER_INTERVAL, 21, interval.getAction(), interval.getIntervalID());
        //this.dataManager.requestData(SSRConstant.ACTION_TRIGGER_INTERVAL, interval.getIntervalID());
    }

}
