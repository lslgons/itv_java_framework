package com.tcom.ssr;


import com.tcom.platform.controller.KeyCode;
import com.tcom.scene.BaseScene;
import com.tcom.util.LOG;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.ArrayList;

public class SSRComponent extends BaseScene implements DataManager.DataReceivedListener {
    SSRContainer ssrContainer;
    DataManager dataManager;
    ArrayList renderList;
    ArrayList elementList;
    SSRElement activatedElement;

    public SSRComponent(SSRContainer container) {
        super();
        this.ssrContainer=container;
        //this.dataManager=new DataManager(this);
        this.renderList=new ArrayList();
        this.elementList = new ArrayList();

    }

    public void requestData(String uid) {
        this.dataManager=new DataManager(this, uid);
        //화면 구성
        this.dataManager.requestData(90, "");
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

    }

    public void onKeyDown(int keycode) {
        LOG.print("["+this.dataManager.getUid()+"] : onKeyDown => "+keycode);
        int action_index=-1;
        int key_mapping=0;
        switch(keycode) {

            case KeyCode.VK_UP:
                action_index=0;
                key_mapping=10;
                break;
            case KeyCode.VK_RIGHT:
                action_index=1;
                key_mapping=11;
                break;
            case KeyCode.VK_DOWN:
                action_index=2;
                key_mapping=12;
                break;
            case KeyCode.VK_LEFT:
                action_index=3;
                key_mapping=13;
                break;
            case KeyCode.VK_OK:
                action_index=4;
                key_mapping=14;
                break;
            case KeyCode.VK_BACK:
                action_index=5;
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
        repaint();
    }
}
