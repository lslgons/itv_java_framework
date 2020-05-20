package cjtosplus.comp.main;

import com.cj.tvui.component.ItemCompEvent;
import com.cj.tvui.network.HttpConnect;
import com.cj.tvui.util.ImageUtil;

import java.awt.*;

/**
 * Created by user on 2017-10-31.
 */
public class SmallBanner extends Banner implements Runnable { // 20min add... // 위쪽배너
    private Thread thread = null;
    private int sleepTime = 2500*2;

    private Image[] smallBanner;

    private String[] smallBannerURL;

    private int bannerCnt = 0;
    private int bannerIndex = 0;

    private Image result;

    public SmallBanner(int type, ItemCompEvent evt) {
        super(type, evt);
        // TODO Auto-generated constructor stub
    }
    public SmallBanner(int type, Image focTop, Image focLeft, Image focRight, Image focBottom,ItemCompEvent evt) {
        super(type, focTop, focLeft, focRight, focBottom,evt);
        // TODO Auto-generated constructor stub
    }


    public Image onBannerChanged() {
        // TODO Auto-generated method stub
        result = smallBanner[bannerIndex];

        return result;
    }

    public void paint(Graphics g) {
        if(smallBanner == null) return;
        try {
            g.drawImage(onBannerChanged(), 0, 0, super.getWidth(), super.getHeight(), this);
//				g.drawImage(bigBanner[bannerIndex], 0, 0, super.getWidth(), super.getHeight(), this);
        } catch (Exception e) {
            System.out.println( "PAINT: " + e.getMessage() );
        }

        super.paint(g);
    }

//        public void okKeyPressed() {
//        	System.out.println("1999999999999999999999-----bannerComp.. is...");
//        }

    public void destroy() {
        if(this.thread != null) {
            thread = null;
        }
        flushImage();
        smallBannerURL = null;
    }

    public void run() {
        // TODO Auto-generated method stub
        try{
            while(true) {
                Thread.yield();
                if(sleepTime <= 0) {
                    break;
                }

                bannerIndex++;

                if(bannerIndex == bannerCnt) {
                    bannerIndex =0;
                }

                repaint();
                System.out.println("("+bannerIndex+") bannerIndex Thread is Here... ");

                Thread.sleep(sleepTime);
            }

        }catch(InterruptedException e) {
            System.out.println("intterpt trigger thread.");
        }

    }

    public void startBanner() {
        System.out.println("Start startBanner_02 thread.");
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stopBanner() {
        System.out.println("Stop startBanner_02 thread.");
        if (thread != null) {
            thread.interrupt();
        }
        thread = null;
    }

    public void setBannerImg() {
        bannerIndex = 0;
        flushImage();

        this.setSmallBannerCnt(3);

        if (this.bannerCnt > 0) {
            smallBanner = new Image[bannerCnt];

            smallBannerURL = new String[bannerCnt];
            /////////Sample start////////////////////////////
            smallBannerURL[0] = "http://210.122.102.109:10080/ktbmt2/images/banner_small/20170228154856_H960_file_default960.png";
            smallBannerURL[1] = "http://210.122.102.109:10080/ktbmt2/images/banner_small/20170728103004_H960_file_default960.png";
            smallBannerURL[2] = "http://210.122.102.109:10080/ktbmt2/images/banner_small/20170926161809_H960_file_default960.png";
            /////////Sample end////////////////////////////

            bannerIndex = 0;

            for (int i = 0; i < smallBanner.length; i++) {
                smallBanner[i] = ImageUtil.createImage(HttpConnect.ImgRequest(smallBannerURL[i]));
            }

        }

    }

    private void flushImage() {
        if (smallBanner != null) {
            for (int i = 0; i < smallBanner.length; i++) {
                if (smallBanner[i] != null) {
                    try {
                        smallBanner[i].flush();
                    } catch (Exception e) {
                    }
                    smallBanner[i] = null;
                }
            }
            smallBanner = null;
        }

        if(result != null) {
            result.flush();
        }
        result = null;
    }

    public int getSmallBannerCnt() {
        return bannerCnt;
    }
    public void setSmallBannerCnt(int bannerCnt) {
        this.bannerCnt = bannerCnt;
    }



}