package com.cj.tvui.ui.layout;

/**
 * 세로형 List를 사용할 경우의 기본 클래스
 * @author daegon.kim (daegon.kim1@cj.net)
 * @since 2017-10-31
 */
public class VerticalList extends GridList {
    public VerticalList(int row, ListItemAdapter a) {
        super(1, row, a);
    }
}
