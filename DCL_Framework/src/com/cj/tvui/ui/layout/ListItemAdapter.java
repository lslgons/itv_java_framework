package com.cj.tvui.ui.layout;

import com.cj.tvui.ui.Widget;

/**
 * 리스트 사용 시 데이터를 중재하느 Adapter 인터페이스
 * @author daegon.kim (daegon.kim1@cj.net)
 * @since 2017-10-31.
 */
public interface ListItemAdapter {
    public int totalCount();
    public Widget item(int index);
    public int width();
    public int height();
    public int padding();

}
