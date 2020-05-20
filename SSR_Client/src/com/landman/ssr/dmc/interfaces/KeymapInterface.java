package com.landman.ssr.dmc.interfaces;

/**
 * Created by daegon.kim on 2016-12-06.
 */
public interface KeymapInterface {
    public int keyMap(final int keycode);
    public Object getKeyListener();
    public Object getEventGroup(); /* For Havi */
    public void setEnableNumKey(boolean enable);
    public void setEnableHotKey(boolean enable);
    public void setEnableBackKey(boolean enable);
    public void setEnableTrickKey(boolean enable); //REW/Play&Pause/Stop/FF
    public void setEnableOkKey(boolean enable);
    public void setEnableArrowKey(boolean enable);
    
}
