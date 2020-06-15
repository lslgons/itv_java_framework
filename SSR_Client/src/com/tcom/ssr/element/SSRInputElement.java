package com.tcom.ssr.element;

import com.tcom.ssr.SSRComponent;
import com.tcom.ssr.SSRRender;
import com.tcom.ssr.action.SSRAction;
import org.json.simple.JSONObject;

import java.awt.*;

public class SSRInputElement extends SSRElement{

    private boolean isPasswordField=false;

    public SSRInputElement(SSRComponent comp, JSONObject meta) {
        super(comp, meta);
    }

    public void draw(Graphics g, boolean isActivated) {
        super.draw(g, isActivated);

        for(int i=0;i<featured.size();++i) {
            if(((SSRRender) featured.get(i)).getType().equalsIgnoreCase("text")) {
                if(isPasswordField) {
                    String masked_content="";
                    for(int j=0; j<getInputData().length(); ++j) {
                        masked_content+="●";
                    }
                    ((SSRRender) featured.get(i)).drawText(g, masked_content);
                } else {
                    ((SSRRender) featured.get(i)).drawText(g, getInputData());
                }
            }
        }
    }

    public void setPasswordField(boolean b) {
        isPasswordField=b;
    }

    public int getMaxInputSize() {
        return ((Long) comp.getContextData("_input_max_num_"+this.getName())).intValue();
    }

    public String getInputData() {
        return (String) comp.getContextData("_input_data_"+this.getName());
    }

    public void setInputData(String value) {
        comp.setContextData("_input_data_"+this.getName(), value);
    }

    public SSRAction getOnMaxInputAction() {
        return (SSRAction) actions.get(22);
    }

    public SSRAction getOnInitInputAction() {
        return (SSRAction) actions.get(23);
    }
}
