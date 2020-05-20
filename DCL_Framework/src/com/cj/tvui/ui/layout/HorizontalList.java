package com.cj.tvui.ui.layout;

/**
 * 가로형 List를 사용할 경우의 기본 클래스
 * @author daegon.kim (daegon.kim1@cj.net)
 * @since 2017-10-31
 */
public class HorizontalList extends GridList {
    public HorizontalList(int column, ListItemAdapter a) {
        super(column, 1, a);
    }
}
