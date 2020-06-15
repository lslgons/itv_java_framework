package com.tcom.ssr;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import com.tcom.drawer.DefinedColor;
import com.tcom.drawer.Drawer;
import com.tcom.drawer.StringDrawer;
import com.tcom.util.LOG;
import com.tcom.util.StringUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;

public class SSRRender {
    final SSRComponent comp;
    final String type;
    final JSONArray arguments;

    public String getType() {
        return this.type;
    }

    public SSRRender(SSRComponent comp, JSONObject meta) {
        this.comp = comp;
        this.type = (String) meta.get("type");
        this.arguments= (JSONArray) meta.get("arguments");
    }

    public void drawText(Graphics g) {
        this.drawText(g, (String) arguments.get(5));
    }

    public void drawText(Graphics g, String text) {
        Color c= DefinedColor.decodeColor((JSONArray) arguments.get(4));
        //Text
        int x = ((Long)arguments.get(0)).intValue();
        int y = ((Long)arguments.get(1)).intValue();
        String font_align=(String)arguments.get(6);
        int font_size = ((Long)arguments.get(7)).intValue();
        String[] contents=StringUtil.tokenize(text, "\\n");
        if(contents==null) return;

        if(font_align.equals("left")) {
            StringDrawer.drawStringLeft(g, contents,
                    ((Long)arguments.get(0)).intValue(),
                    ((Long)arguments.get(1)).intValue(),
                    //((Long)arguments.get(2)).intValue(),
                    ((Long)arguments.get(3)).intValue(),
                    Drawer.getFont(font_size, Font.PLAIN), c);
        } else if(font_align.equals("right")) {

        } else {
            for(int i=0;i<contents.length;++i) {
                String content=contents[i];
                if(i!=0) content=contents[i].substring(2);
                StringDrawer.drawStringCenter(g, content,
                        ((Long)arguments.get(0)).intValue(),
                        ((Long)arguments.get(1)).intValue(),
                        ((Long)arguments.get(2)).intValue(),
                        ((Long)arguments.get(3)).intValue()*(i+1) + 5*(i),
                        Drawer.getFont(font_size, Font.PLAIN), c);
            }
        }
    }

    public void drawRect(Graphics g) {
        Drawer.drawFocus(g,((Long)arguments.get(0)).intValue(),((Long)arguments.get(1)).intValue(),
                ((Long)arguments.get(2)).intValue(),((Long)arguments.get(3)).intValue(),((Long)arguments.get(5)).intValue(),
                DefinedColor.decodeColor((JSONArray) arguments.get(4)));
    }

    public void fillRect(Graphics g) {
        Drawer.drawFillRect(g,((Long)arguments.get(0)).intValue(),((Long)arguments.get(1)).intValue(),
                ((Long)arguments.get(2)).intValue(),((Long)arguments.get(3)).intValue(),
                DefinedColor.decodeColor((JSONArray) arguments.get(4)));
    }

    public void drawImage(Graphics g) {
        Drawer.drawImage(g, comp.loadImage((String)arguments.get(5)),
                ((Long)arguments.get(0)).intValue(),
                ((Long)arguments.get(1)).intValue(),
                ((Long)arguments.get(2)).intValue(),
                ((Long)arguments.get(3)).intValue(),
                null);
    }

    public void fillCircle(Graphics g) {
        Drawer.drawFillCircle(g,((Long)arguments.get(0)).intValue(),((Long)arguments.get(1)).intValue(),
                ((Long)arguments.get(2)).intValue(),((Long)arguments.get(3)).intValue(),
                DefinedColor.decodeColor((JSONArray) arguments.get(4)));
    }

    public void draw(Graphics g) {
        if (type.equalsIgnoreCase("rect")) {
            drawRect(g);
        } else if (type.equalsIgnoreCase("fill")) {
            fillRect(g);
        } else if (type.equalsIgnoreCase("image")) {
            drawImage(g);
        } else if (type.equalsIgnoreCase("text")) {
            drawText(g);
        } else if(type.equalsIgnoreCase("circle")) {
            fillCircle(g);
        }
    }
}
