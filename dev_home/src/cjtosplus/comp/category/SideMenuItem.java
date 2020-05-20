package cjtosplus.comp.category;

import com.cj.tvui.ui.Widget;
import com.cj.tvui.util.LOG;

import java.awt.*;

/**
 * Created by user on 2017-11-01.
 */
public class SideMenuItem extends Widget {

    private String title;
    public SideMenuItem(String title) {
        this.title = title;
        this.setSize(300, 100);
        this.setLocation(0,0);
    }

    public String getTitle() {
        return this.title;
    }

    protected void onActivated() {

    }

    protected void onDeactivated() {

    }

    public void okKeyPressed() {
        LOG.print("SideMenuItem  OK Key");

    }

    public void paint(Graphics g) {
        g.setColor(Color.cyan);
        g.fillRect(0,0,300,100);
        if(isActivated()) {
            g.setColor(Color.red);
            g.drawRect(0,0,300,100);
        }


        g.setFont(new Font("Korean iTV SanSerifD", Font.PLAIN, 15));
        g.setColor(Color.black);
        g.drawString(this.title, 10,10);

        super.paint(g);
    }
}
