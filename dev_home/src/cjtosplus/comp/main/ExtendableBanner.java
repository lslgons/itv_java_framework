package cjtosplus.comp.main;

import com.cj.tvui.component.ItemCompEvent;

import java.awt.*;

/**
 * Created by user on 2017-10-31.
 */
public class ExtendableBanner extends Banner { // 20min add... // 커지는 배너.

    public ExtendableBanner(int type, ItemCompEvent evt) {
        super(type, evt);
        // TODO Auto-generated constructor stub
    }
    public ExtendableBanner(int type, Image focTop, Image focLeft, Image focRight, Image focBottom, ItemCompEvent evt) {
        super(type, focTop, focLeft, focRight, focBottom,evt);
        // TODO Auto-generated constructor stub
    }


    public Image onBannerChanged() {
        // TODO Auto-generated method stub
        return null;
    }

    public void paint(Graphics g) {
        super.paint(g);
//			g.setColor(new Color(0,255,0));
//    		g.drawRect(0, 0, getWidth()-10, getHeight()-10);
    }

//        public void okKeyPressed() {
//        	System.out.println("1999999999999999999999-----bannerComp.. is...");
//        }

}
