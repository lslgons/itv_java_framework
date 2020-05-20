package com.cj.tvui.util;

import com.cj.tvui.Constants;
import com.cj.tvui.controller.SceneController;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;

public class GlobalImageManager {
    private static GlobalImageManager instance=null;
    public static GlobalImageManager getInstance() {
        if(instance == null) instance=new GlobalImageManager();
        return instance;
    }
    boolean isFlushed;
    ImagePool imgPool;
    private GlobalImageManager() {
        isFlushed=false;
        imgPool=new ImagePool();
        imgPool.setComponent(SceneController.getInstance().getRootScene());
    }

    public void preloadImages(String[] files) {
        for(int i=0;i<files.length;++i) {
            String file=files[i];
            loadImage(file);
        }
    }

    public Image loadImage(final String name) {
        if(isFlushed) {
//            imgPool=new ImagePool();
//            imgPool.setComponent(SceneController.getInstance().getRootScene());
            LOG.print("================ Global Image Pool flushed.... ");
            return null;
        }
        Image img;
        //1.Load Image from server
        if (name.startsWith("http://") || name.startsWith("https://")) {
            img = imgPool.getImage(name);
        } else {
            img = imgPool.getImage(Constants.DCL_HOST+name);
            if(img == null) {
                //20181010, Exception 처리
                try {
                    //2018-08-20, 서버 이미지 미존재시 로컬에서 가져옴
                    LOG.print(this, "Local Image Load!!!!");
                    File resFolder = new File("res");

                    if(resFolder.exists()) {
                        LOG.print(this, "Res Folder Exists!!!!");
                        File imgFile = new File("res"+File.separator+name);
                        if(imgFile.exists()) {
                            img = imgPool.getImage("res"+File.separator+name);
                        }
                    } else {
                        File imgFile = new File(name);
                        //2018-10-10, 파일 존재 시 이미지 가져옴
                        if(imgFile.exists()) {
                            img = imgPool.getImage(name);
                        }
                    }
                } catch(Exception e) {
                    LOG.print("Image not found : "+name);
                    return null;

                }

            }
            ImageObserver imgObs = new ImageObserver() {
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            };
            try {
                if(img.getWidth(imgObs) == -1 && img.getHeight(imgObs) == -1) {
                    //3.Exception, Can't load image
                    //throw new RuntimeException("Image not found");
                    LOG.print(this, "Image not found... : " + name);
                } else {
                    LOG.print(this, "Image Load Complete : "+ name);
                }
            } catch(NullPointerException e) {
                LOG.print(this, "Image not found... : " + name);
            }
        }


        return img;
    }


    public void flush() {
        LOG.print(this, "Flush all global Images");
        imgPool.flush();
        imgPool=null;
        isFlushed=true;
    }

    public void shutdown() {
        this.flush();
        instance=null;
    }
}
