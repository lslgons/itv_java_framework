package com.tcom.platform.dmc.interfaces;

import java.awt.Rectangle;

/**
 * Created by daegon.kim on 2016-12-07.
 */
public interface AVInterface {
	public final static int AV_MODE_FULL=0;
	public final static int AV_MODE_PIP=1;

	public void resetVideoSize();
	public void changeVideoSize(Rectangle rect);
	public Rectangle getCurrentVideoSize();
	public void turnOnVideo();
	public void turnOffVideo();
}
