package com.cj.tvui.component;

import java.awt.*;

/**
 * Created by user on 2017-10-31.
 */
public class ImagePanel extends Component {
    Image image = null;
    Rectangle rect = null;

    public ImagePanel(Image _img, Rectangle _rect) {
        this.image = _img;
        this.rect = _rect;
        setBounds(this.rect);
    }

    public ImagePanel(Image _img, int x, int y, int w, int h) {
        this(_img, new Rectangle(x,y,w,h));
    }


    public void paint(Graphics g) {
        g.drawImage(this.image, 0, 0, getWidth(), getHeight(), this);
        super.paint(g);
    }
}
