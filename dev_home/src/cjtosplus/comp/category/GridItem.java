package cjtosplus.comp.category;

import com.cj.tvui.ui.Widget;
import com.cj.tvui.util.LOG;

import java.awt.*;

/**
 * Created by user on 2017-11-01.
 */
public class GridItem extends Widget {

    private String title;
    public GridItem(String title) {
        this.title = title;
        this.setSize(200, 200);
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
        LOG.print("GridItem  OK Key");

    }

    public void paint(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(0,0,200,200);
        if(isActivated()) {
            g.setColor(Color.green);
            g.drawRect(0,0,298,98);
        }


        g.setFont(new Font("Korean iTV SanSerifD", Font.PLAIN, 15));
        g.setColor(Color.black);
        g.drawString(this.title, 30,30);

        super.paint(g);
    }
}
