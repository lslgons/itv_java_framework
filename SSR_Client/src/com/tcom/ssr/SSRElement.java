package com.tcom.ssr;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.ArrayList;

public class SSRElement {
    final SSRComponent comp;
    final String name;
    ArrayList actions;
    ArrayList onactivatedrender;
    ArrayList ondeactivatedrender;

    public SSRElement(SSRComponent comp, JSONObject meta) {
        this.comp = comp;
        this.name = (String) meta.get("name");
        JSONArray actionData = (JSONArray) meta.get("action");
        actions = new ArrayList();
        for (int i=0;i<actionData.size();++i) {
            JSONObject a_data = (JSONObject) actionData.get(i);
            if(a_data != null) {
                SSRAction action=new SSRAction(((Long)a_data.get("type")).intValue(), (JSONArray) a_data.get("arguments"));
                actions.add(action);
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

    }

    public void doAction(int action_index) {

    }

    public void draw(Graphics g, boolean isActivated) {
        for(int i=0;i<ondeactivatedrender.size();++i) {
            SSRRender render = (SSRRender) ondeactivatedrender.get(i);
            render.draw(g);
        }
        if(isActivated) {
            for(int j=0;j<onactivatedrender.size();++j) {
                SSRRender render = (SSRRender) onactivatedrender.get(j);
                render.draw(g);
            }
        }
    }


    static class SSRAction {
        int type;
        JSONArray arguments;
        public SSRAction(int type, JSONArray arguments) {
            this.type=type;
            this.arguments=arguments;
        }
    }
}
