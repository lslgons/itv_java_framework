package cjtosplus.comp.main;

import java.awt.*;

/**
 * Created by user on 2017-10-31.
 */
/* Flow text */
public class FlowTextBar extends Component implements Runnable {
    private Thread thread = null;
    private int sleepTime = 200;
    private int movePixel = 2;   // 이동거리.
//		private int movePixel = 10;

    private int xp;
    private int yp;
    private int height;
    private int width;
    private String flowText;
    private Font font;
    private Color color;

    private int flowTextXp = 0;
    private int flowTextYp = 16; //기준선
    private int flowTextWidth = 0;

    private int textGap = 0; // 첫번째 text와 꼬리를 무는 Text와의 간격.

    private boolean oneTimeProcess = false;

    public FlowTextBar() {
        super();
        // TODO Auto-generated constructor stub
    }

    public FlowTextBar(int x, int y, int w, int h, String text, Font font, Color color) {
        this.xp = x;
        this.yp = y;
        this.width = w;
        this.height = h;
        this.flowText = text;
        this.font = font;
        this.color = color;

        setBounds(x, y, w, h);

        this.flowTextXp = 10; //
    }

    public void paint(Graphics g) {
        g.setFont(this.font);
        g.setColor(this.color);
//	        System.out.println("ticker paint is here..........");
        g.drawString(this.flowText, flowTextXp, flowTextYp); // ù Text

        if (!oneTimeProcess) {
            flowTextWidth = g.getFontMetrics().stringWidth(flowText);
            oneTimeProcess = true;
        }

        if (flowTextWidth + flowTextXp < width) {
            g.drawString(this.flowText, flowTextWidth +flowTextXp + getTextGap(), flowTextYp); //������ ���� ��2�� Text
        }

			/*
			System.out.println("flowTextWidth["+ flowTextWidth +"]");
			System.out.println("flowTextXp["+ flowTextXp +"]");
			int temp =flowTextWidth + flowTextXp;
			System.out.println("flowTextWidth + flowTextXp["+temp+"]");
			System.out.println("width["+ width +"]");*/

    }

    public int getTextGap() {
        return textGap;
    }

    public void setTextGap(int textGap) {
        this.textGap = textGap;
    }

    public void destroy() {
        this.flowText = null;
        this.font = null;
        this.color = null;

        if (this.thread != null) {
            thread = null;
        }
    }


    public void run() {
        // TODO Auto-generated method stub
        try {
            while (true) {
                Thread.yield();
                if (sleepTime <= 0) {
                    break;
                }

                flowTextXp -= movePixel;                  // 이동 픽셀 단위.

//	                if(flowTextXp < 0) {
//	                	flowTextXp = this.height;
//	                }

//	                if(flowTextXp+this.flowTextWidth < 0) { // 맨마지막 char 까지 보여준다.
//	                	flowTextXp = this.height;
//	                }

                if (flowTextWidth + flowTextXp  <= 0) { // 초기화시켜준다.
                    flowTextXp = 0;
                }

//	                System.out.println("yPos value " + yPos);
                repaint();
//	                System.out.println("Ticker is Live...");

                Thread.sleep(sleepTime);
            }
        } catch (InterruptedException e) {
            System.out.println("intterpt trigger thread.");
        }

    }

    public void startTicker() {
        System.out.println("Start startTicker thread.");
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stopTicker() {
        System.out.println("Stop startTicker thread.");
        if (thread != null) {
            thread.interrupt();
        }
        thread = null;
    }

}
