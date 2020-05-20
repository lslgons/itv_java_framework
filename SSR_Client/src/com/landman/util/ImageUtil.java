package com.landman.util;

import java.awt.*;

public class ImageUtil {
	private final static Component component = new Component() {
	};
	private final static MediaTracker tracker = new MediaTracker(component);
    private final static Toolkit TOOLKIT = Toolkit.getDefaultToolkit();
	
	public static Image createImage(byte[] imageData) {
        return createImage(imageData, 0, imageData.length);
    }

    public static Image createImage(byte[] imageData, int offset, int length) {
        Image image = null;
        try {
            image = TOOLKIT.createImage(imageData, offset, length);
            if (image == null) {
                return null;
            }
            return image;
        } finally {
            synchronized (tracker) {
                tracker.addImage(image, 0);
                try {
                    tracker.waitForID(0, 5000);
                } catch (InterruptedException e) {
                    System.out.println("INTERRUPTED while loading Image..20min");
                }
                tracker.statusID(0, false);
                tracker.removeImage(image, 0);
            }
        }
    }
}
