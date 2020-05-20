package com.landman.platform.dmc.interfaces;

import java.awt.Rectangle;

/**
 * Created by daegon.kim on 2016-12-07.
 */
public interface AVInterface {
	public void changeVideoSize(Rectangle rect);
	public Rectangle getCurrentVideoSize();
	public void turnOnVideo();
	public void turnOffVideo();
}
