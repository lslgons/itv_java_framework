package com.cj.tvui.ui.layout;

import com.cj.tvui.ui.Widget;

/**
 * 리스트 기본 추상클래스, 커스텀 리스트 사용 시 사용 가능 함
 * @author daegon.kim (daegon.kim1@cj.net)
 * @since 2017-11-01
 */
abstract class AbstractList extends Widget {
    protected ListItemAdapter adapter = null;
    protected Widget activatedWidget = null;
    protected int maxPage = 1;
    protected int currentPage = 1;
    protected int focusRowInPage = 0;
    protected int focusColumnInPage = 0;
    protected int column;
    protected int row;
    boolean isVisibleScroll = false;
    boolean isCircularFocus = false;



    public AbstractList(int column, int row, ListItemAdapter a) {
        this.adapter = a;
        this.focusRowInPage = 0;
        this.focusColumnInPage = 0;
        this.activatedWidget = this.adapter.item(0);
        this.maxPage = 1;
        this.currentPage = 1;
        this.column = column;
        this.row = row;
        this.maxPage = maxPage();

        onAdapterChanged();
        onPageChanged(currentPage);
    }

    protected void swapItemActivate(Widget _after) {
        if(this.activatedWidget != null) this.activatedWidget.deactivate();
        _after.activate();
        this.activatedWidget = _after;
    }



    public void setAdapter(ListItemAdapter _a) {
        this.adapter = _a;
        onAdapterChanged();
        this.maxPage = maxPage();
        this.currentPage = 1;
        onPageChanged(currentPage);
    }


    protected abstract void onAdapterChanged();
    protected abstract int maxPage();
    protected abstract void onPageChanged(int page);

    public Widget getWidget(int index) {
        try {
            return adapter.item(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (Exception e) {
            return null;
        }

    }

    public int getMaxPage() {
        return maxPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setVisibleScroll(boolean v) {
        this.isVisibleScroll = v;
    }

    public boolean isVisibleScroll() {
        return this.isVisibleScroll;
    }

    public boolean isCircularFocus() {
        return isCircularFocus;
    }

    public void setCircularFocus(boolean circularFocus) {
        isCircularFocus = circularFocus;
    }

    public boolean goNextPage() {
        currentPage +=1;
        if(maxPage < currentPage) {
            if(isCircularFocus) {
                currentPage = 1;
            } else {
                currentPage = maxPage;
                return false;
            }
        }
        this.onPageChanged(currentPage);
        return true;

    }

    public boolean goPreviousPage() {
        currentPage -=1;
        if(currentPage < 1) {
            if(isCircularFocus) {
                currentPage = maxPage;
            } else {
                currentPage =1;
                return false;
            }
        }
        this.onPageChanged(currentPage);
        return true;

    }

}
