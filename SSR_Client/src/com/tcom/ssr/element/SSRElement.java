package com.tcom.ssr.element;

import com.tcom.ssr.SSRComponent;
import com.tcom.ssr.SSRRender;
import com.tcom.ssr.action.SSRAction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.ArrayList;

public class SSRElement {

    final public static int ELEMENT_TYPE_NORMAL=0;
    final public static int ELEMENT_TYPE_INPUT=1;
    final public static int ELEMENT_TYPE_PASSWORD_INPUT=2;
    final public static int ELEMENT_TYPE_SCROLL=3;
    final public static int ELEMENT_TYPE_SELECT=4;

    final SSRComponent comp;
    final String name;
    final int type;
    ArrayList actions;
    ArrayList onactivatedrender;
    ArrayList ondeactivatedrender;
    ArrayList featured;

    public SSRElement(SSRComponent comp, JSONObject meta) {
        this.comp = comp;
        this.name = (String) meta.get("name");
        this.type = ((Long) meta.get("type")).intValue();
        JSONArray actionData = (JSONArray) meta.get("action");
        actions = new ArrayList();
        for (int i=0;i<actionData.size();++i) {
            JSONObject a_data = (JSONObject) actionData.get(i);
            if(a_data != null) {
                actions.add(new SSRAction(((Long)a_data.get("type")).intValue(), (JSONArray) a_data.get("arguments")));
            } else {
                actions.add(null);
            }


        }
        JSONArray onActivateData = (JSONArray) meta.get("onactivated");
        onactivatedrender=new ArrayList();
        for (int j=0;j<onActivateData.size();++j) {
            SSRRender render = new SSRRender(comp, (JSONObject) onActivateData.get(j));
            onactivatedrender.add(render);
        }
        JSONArray onDeactivateData = (JSONArray) meta.get("ondeactivated");
        ondeactivatedrender=new ArrayList();
        for (int k=0;k<onDeactivateData.size();++k) {
            SSRRender render = new SSRRender(comp, (JSONObject) onDeactivateData.get(k));
            ondeactivatedrender.add(render);
        }

        JSONArray featuredData = (JSONArray) meta.get("featured");
        featured=new ArrayList();
        if(featuredData!=null) {
            for (int k=0;k<featuredData.size();++k) {
                SSRRender render = new SSRRender(comp, (JSONObject) featuredData.get(k));
                featured.add(render);
            }
        }

    }

    public void draw(Graphics g, boolean isActivated) {
        for(int i=0;i<ondeactivatedrender.size();++i) {
            ((SSRRender) ondeactivatedrender.get(i)).draw(g);
        }
        if(isActivated) {
            for(int j=0;j<onactivatedrender.size();++j) {
                ((SSRRender) onactivatedrender.get(j)).draw(g);
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public SSRAction getAction(int index) {
        return (SSRAction) actions.get(index);
    }



}
