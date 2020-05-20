package cjtosplus.comp.main;

import com.cj.tvui.component.ItemCompEvent;
import com.cj.tvui.ui.Widget;

import java.awt.*;

/**
 * Created by user on 2017-10-31.
 */
/* Buttons */
public class Button extends Widget {
    private ItemCompEvent evt = null; // ok Event 처리.

    private boolean isSelected = false; //Selected 여부.

    private String text = null;
    private Color color = null;

    private Image iconImg = null;  // 사용할 아이콘 이미지.
    private Image norImg = null; // 노멀 이미지.
    private Image focImg = null; // 포커스 이미지.
    private Image selImg = null; // 셀렉트 이미지.

    private Image focTop = null; // 포커스의 분리
    private Image focLeft = null;
    private Image focRight= null;
    private Image focBottom = null;

    private int btnType = 0; // 0: default;

    public Button(int width, int height, String text, Image icon) {
        setBounds(0, 0, width, height);
        this.text = text;
        this.iconImg = icon;
    }

    public Button(int width, int height, String text, Image icon, Image nor, Image foc, Image sel, int btnType, ItemCompEvent e) {
        setBounds(0, 0, width, height);
        this.text = text;
        this.iconImg = icon;
        this.norImg = nor;
        this.focImg = foc;
        this.selImg = sel;
        this.evt = e;
    }

    public Button(int width, int height, String text, Image icon, Image nor,Image focTop, Image focLeft, Image focRight, Image focBottom, Image sel,  int btnType, ItemCompEvent e) {
        setBounds(0, 0, width, height);
        this.text = text;
        this.iconImg = icon;
        this.norImg = nor;
        this.focTop = focTop;
        this.focLeft = focLeft;
        this.focRight = focRight;
        this.focBottom = focBottom;
        this.selImg = sel;
        this.btnType =btnType;
        this.evt = e;
    }

    public Button(int width, int height, String text, Color color, Image icon, Image nor,Image focTop, Image focLeft, Image focRight, Image focBottom, Image sel, int btnType, ItemCompEvent e) {
        setBounds(0, 0, width, height);
        this.text = text;
        this.color = color;
        this.iconImg = icon;
        this.norImg = nor;
        this.focTop = focTop;
        this.focLeft = focLeft;
        this.focRight = focRight;
        this.focBottom = focBottom;
        this.selImg = sel;
        this.btnType =btnType;
        this.evt = e;
    }


    public void onActivated() {

    }


    public void onDeactivated() {

    }


    public void okKeyPressed() {
        evt.doAction();
    }

    public void paint(Graphics g) {
        g.setFont(new Font("Korean iTV SanSerifD", Font.PLAIN, 15));

        if(this.isSelected) {
            g.drawImage(this.getSelImg(), 0, 0,  getWidth(), getHeight(), this);
            if(this.btnType == 0) {
                g.drawImage(this.getIconImg(), 24, 5, 32, 32, this);
            }else if(this.btnType == 1) {
                g.drawImage(this.getIconImg(), 24-6, 5, 32, 32, this);
            }else if(this.btnType == 2) {
                g.drawImage(this.getIconImg(), 24+6, 5, 32, 32, this);
            }
            if(this.color == null) {
                g.setColor(new Color(255,255,255));
            }else {
                g.setColor(this.color);
            }

            if (this.btnType == 0) {
                g.drawString(text, 60 - 5, 27);
            } else if (this.btnType == 1) {
                g.drawString(text, 60 - 5 - 6, 27);
            } else if (this.btnType == 2) {
                g.drawString(text, 60 - 5 + 6+6, 27);
            }
            return;
        }

        if(this.isActivated()) { // 포커스
            g.drawImage(this.getNorImg(), 0, 0,  getWidth(), getHeight(), this);
            g.drawImage(this.getSelImg(),0,0, getWidth(), getHeight(), this);

            if(this.btnType == 0) {
                g.drawImage(this.getIconImg(), 24, 5, 32, 32, this);
            }else if(this.btnType == 1) {
                g.drawImage(this.getIconImg(), 24-6, 5, 32, 32, this);
            }else if(this.btnType == 2) {
                g.drawImage(this.getIconImg(), 24+6, 5, 32, 32, this);
            }

            if(this.color == null) {
                g.setColor(new Color(255,255,255));
            }else {
                g.setColor(this.color);
            }

            if (this.btnType == 0) {
                g.drawString(text, 60 - 5, 27);
            } else if (this.btnType == 1) {
                g.drawString(text, 60 - 5 - 6, 27);
            } else if (this.btnType == 2) {
                g.drawString(text, 60 - 5 + 6+6, 27);
            }

            if(this.getFocImg() != null) { // focImg 존재
                g.drawImage(this.getFocImg(), 0, 0,  getWidth(), getHeight(), this);
            }else { // 4개의 focImg Default.
                g.drawImage(this.focTop, 0, 0, getWidth(), 4, this);	// top
                g.drawImage(this.focLeft, 0, 0 + 4, 4, getHeight() - 4 - 4, this);	// left
                g.drawImage(this.focRight, 0 + getWidth() - 4, 0 + 4, 4, getHeight()  - 4 - 4, this);	// right
                g.drawImage(this.focBottom, 0, 0 + getHeight()  - 4, getWidth(), 4, this);	// bottom
            }

        }else { // 노멀
            g.drawImage(this.getNorImg(), 0, 0,  getWidth(), getHeight(), this);

            if(this.color == null) {
                g.setColor(new Color(255,255,255));
            }else {
                g.setColor(this.color);
            }

            if (this.btnType == 0) {
                g.drawString(text, 60 - 5, 27);
            } else if (this.btnType == 1) {
                g.drawString(text, 60 - 5 - 6, 27);
            } else if (this.btnType == 2) {
                g.drawString(text, 60 - 5 + 6 + 6, 27);
            }

            if (this.btnType == 0) {
                g.drawImage(this.getIconImg(), 24, 5, 32, 32, this);
            } else if (this.btnType == 1) {
                g.drawImage(this.getIconImg(), 24 - 6, 5, 32, 32, this);
            } else if (this.btnType == 2) {
                g.drawImage(this.getIconImg(), 24 + 6, 5, 32, 32, this);
            }

        }

    }

    public Image getIconImg() {
        return iconImg;
    }

    public Image getNorImg() {
        return norImg;
    }

    public Image getFocImg() {
        return focImg;
    }

    public Image getSelImg() {
        return selImg;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void destroy() {
        if (getIconImg() != null) {
            this.iconImg.flush();
            iconImg = null;
        }

        if (getNorImg() != null) {
            this.norImg.flush();
            norImg = null;
        }

        if (getFocImg() != null) {
            this.focImg.flush();
            focImg = null;
        }

        if (getSelImg() != null) {
            this.selImg.flush();
            selImg = null;
        }

        if (this.focTop != null) {
            this.focTop.flush();
            focTop = null;
        }

        if (this.focLeft != null) {
            this.focLeft.flush();
            focLeft = null;
        }

        if (this.focRight != null) {
            this.focRight.flush();
            focRight = null;
        }

        if (this.focBottom != null) {
            this.focBottom.flush();
            focBottom = null;
        }

        if (this.color != null) {
            this.color = null;
        }

    }

}
