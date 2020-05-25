package com.tcom.ssr;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;

public class SSRRender {
    final SSRComponent comp;
    final String type;
    final JSONArray arguments;

    public SSRRender(SSRComponent comp, JSONObject meta) {
        this.comp = comp;
        this.type = (String) meta.get("type");
        this.arguments= (JSONArray) meta.get("arguments");
    }

    public void draw(Graphics g) {
        JSONArray colorData = (JSONArray) arguments.get(4);
        if(colorData !=null) {
            Color c=new Color(((Long)colorData.get(0)).intValue(),
                    ((Long)colorData.get(1)).intValue(),
                    ((Long)colorData.get(2)).intValue(),
                    ((Long)colorData.get(3)).intValue());
            g.setColor(c);
        }

        if (type.equalsIgnoreCase("rect")) {
            //draw rect

            g.drawRect(
                    ((Long)arguments.get(0)).intValue(),
                    ((Long)arguments.get(1)).intValue(),
                    ((Long)arguments.get(2)).intValue(),
                    ((Long)arguments.get(3)).intValue()
            );

        } else if (type.equalsIgnoreCase(("fill"))) {

            g.fillRect(
                    ((Long)arguments.get(0)).intValue(),
                    ((Long)arguments.get(1)).intValue(),
                    ((Long)arguments.get(2)).intValue(),
                    ((Long)arguments.get(3)).intValue()
            );
        } else if (type.equalsIgnoreCase("image")) {
            //Image
            Image img=comp.loadImage((String)arguments.get(5));
            g.drawImage(img,
                    ((Long)arguments.get(0)).intValue(),
                    ((Long)arguments.get(1)).intValue(),
                    ((Long)arguments.get(2)).intValue(),
                    ((Long)arguments.get(3)).intValue(),
                    null
            );
        } else if (type.equalsIgnoreCase("text")) {
            //Text
            int x = ((Long)arguments.get(0)).intValue();
            int y = ((Long)arguments.get(1)).intValue();
            g.drawString((String)arguments.get(5),x,y);
        }
    }
}
