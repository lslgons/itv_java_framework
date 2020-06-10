package com.tcom.ssr.element;

import com.tcom.ssr.SSRComponent;
import com.tcom.ssr.SSRRender;
import com.tcom.ssr.action.SSRAction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;

public class SSRSelectElement extends SSRElement{
    final String[] selectableValues;
    public SSRSelectElement(SSRComponent comp, JSONObject meta) {
        super(comp, meta);

        JSONArray values= (JSONArray) comp.getContextData("_select_values_"+this.getName());
        selectableValues=new String[values.size()];
        for(int i=0;i<values.size();++i) {
            selectableValues[i]= (String) values.get(i);
        }
    }

    public void draw(Graphics g, boolean isActivated) {
        super.draw(g, isActivated);

        for(int i=0;i<featured.size();++i) {
            SSRRender render = (SSRRender) featured.get(i);
            if(render.getType().equalsIgnoreCase("text")) {
                ((SSRRender) featured.get(i)).drawText(g, selectableValues[getSelectedIndex()]);
            }

        }
    }

    public boolean isHorizontalSelection() {
        return ((Long) comp.getContextData("_select_direction_"+this.getName())).intValue()==400;
    }

    public String[] getSelectableValues() {
        return selectableValues;
    }

    public int getSelectedIndex() {
        return ((Long) comp.getContextData("_selected_index_"+this.getName())).intValue();
    }

    public void setSelectedIndex(int index) {
        comp.setContextData("_selected_index_"+this.getName(), new Long(index));
    }

    public SSRAction getOnSelectFirst() {
        return (SSRAction) actions.get(21);
    }

    public SSRAction getOnSelectEnd() {
        return (SSRAction) actions.get(22);
    }
}
