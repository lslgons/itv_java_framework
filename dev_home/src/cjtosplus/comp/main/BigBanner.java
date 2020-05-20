package cjtosplus.comp.main;

import com.cj.tvui.component.ItemCompEvent;
import com.cj.tvui.network.HttpConnect;
import com.cj.tvui.util.ImageUtil;

import java.awt.*;

/**
 * Created by user on 2017-10-31.
 */
public class BigBanner extends Banner implements Runnable{ // 20min add... // 아래쪽 배너

    private Thread thread = null;
    private int sleepTime = 2500*2;

    private Image[] bigBanner;

    private String[] bigBannerURL;

    private int bannerCnt = 0;
    private int bannerIndex = 0;

    private Image result;

    public BigBanner(int type, ItemCompEvent evt) {
        super(type, evt);
        // TODO Auto-generated constructor stub
    }
    public BigBanner(int type, Image focTop, Image focLeft, Image focRight, Image focBottom,ItemCompEvent evt) {
        super(type, focTop, focLeft, focRight, focBottom,evt);
        // TODO Auto-generated constructor stub
    }


    public Image onBannerChanged() {
        // TODO Auto-generated method stub
//			if(result != null) {
//				result.flush();
//			}

        result = bigBanner[bannerIndex];

        return result;
    }

    public void paint(Graphics g) {
//			super.paint(g);

        if(bigBanner == null) return;
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
        bigBannerURL = null;
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
        System.out.println("Start startBanner_01 thread.");
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stopBanner() {
        System.out.println("Stop startBanner_01 thread.");
        if (thread != null) {
            thread.interrupt();
        }
        thread = null;
    }

    public void setBannerImg() {
        bannerIndex = 0;
        flushImage();

        this.setBigBannerCnt(8);

        if (this.bannerCnt > 0) {
            bigBanner = new Image[bannerCnt];

            bigBannerURL = new String[bannerCnt];
            /////////Sample start////////////////////////////
            bigBannerURL[0] = "http://210.122.102.109:10080/ktbmt2/images/banner_big/20171019141016_H960_file_default960.png";
            bigBannerURL[1] = "http://210.122.102.109:10080/ktbmt2/images/banner_big/20171019141041_H960_file_default960.png";
            bigBannerURL[2] = "http://210.122.102.109:10080/ktbmt2/images/banner_big/20171019141208_H960_file_default960.png";
            bigBannerURL[3] = "http://210.122.102.109:10080/ktbmt2/images/banner_big/20171019141236_H960_file_default960.png";
            bigBannerURL[4] = "http://210.122.102.109:10080/ktbmt2/images/banner_big/20171019141317_H960_file_default960.png";
            bigBannerURL[5] = "http://210.122.102.109:10080/ktbmt2/images/banner_big/20171019141344_H960_file_default960.png";
            bigBannerURL[6] = "http://210.122.102.109:10080/ktbmt2/images/banner_big/20171019165626_H960_file_default960.png";
            bigBannerURL[7] = "http://210.122.102.109:10080/ktbmt2/images/banner_big/20171019171635_H960_file_default960.png";
            /////////Sample end////////////////////////////

            bannerIndex = 0;

            for (int i = 0; i < bigBanner.length; i++) {
                bigBanner[i] = ImageUtil.createImage(HttpConnect.ImgRequest(bigBannerURL[i]));
            }

        }

    }

    private void flushImage() {
        if (bigBanner != null) {
            for (int i = 0; i < bigBanner.length; i++) {
                if (bigBanner[i] != null) {
                    try {
                        bigBanner[i].flush();
                    } catch (Exception e) {
                    }
                    bigBanner[i] = null;
                }
            }
            bigBanner = null;
        }

        if(result != null) {
            result.flush();
        }
        result = null;
    }

    public int getBigBannerCnt() {
        return bannerCnt;
    }
    public void setBigBannerCnt(int bannerCnt) {
        this.bannerCnt = bannerCnt;
    }

}
