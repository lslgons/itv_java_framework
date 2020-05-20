package com.cj.tvui.ui.layout;

import com.cj.tvui.Keys;
import com.cj.tvui.ui.Widget;
import com.cj.tvui.util.LOG;

import java.awt.*;

/**
 * Grid List 를 사용하는 기본 클래스
 * @author daegon.kim (daegon.kim1@cj.net)
 * @since 2017-10-31
 */
public class GridList extends AbstractList {
    public GridList(int column, int row, ListItemAdapter a) {
        super(column, row, a);
    }

    protected void onActivated() {
        this.activatedWidget.activate();
        repaint();
    }
    protected void onDeactivated() {
        this.activatedWidget.deactivate();
        repaint();

    }
    public void okKeyPressed() {
        this.activatedWidget.okKeyPressed();

    }

    public void onKeyDown(int keycode) {
        LOG.print(this, "List onKeyPress : "+keycode);
        LOG.print(this, "focusRowInPage : "+focusRowInPage);
        LOG.print(this, "List focusColumnInpage : "+focusColumnInPage);
        if(keycode == Keys.VK_UP) {
            if(focusRowInPage == 0) {
                //상키를 통한 이전페이지, column값은 유지
                if(currentPage != 1){
                    focusRowInPage = row-1;
                    this.goPreviousPage();
                }
                return;
            }
            //그 외의 경우 포커스 이동해야 함
            focusRowInPage -=1;
        } else if(keycode == Keys.VK_DOWN) {
            if(focusRowInPage == row-1) {
                //하키를 통한 다음페이지, column값은 유지
                if(currentPage != maxPage) {
                    focusRowInPage = 0;
                    if(this.goNextPage()) {

                    } else {
                        super.onKeyDown(keycode);
                    }
                }
                return;
            }
            //그 외의 경우 포커스 이동해야 함
            focusRowInPage +=1;
        } else if(keycode == Keys.VK_RIGHT) {
            if(focusColumnInPage == column-1) {
                //끝자락에 있을 경우
                if(focusRowInPage == row-1) {
                    //마지막 열

                    //우측에 포커싱 Traversal위젯이 존재할 경우
                    if(rightComp != null) {
                        super.onKeyDown(keycode);
                    } else {
                        focusColumnInPage=0;
                        focusRowInPage=0;
                        this.goNextPage();
                    }
                    return;
                } else {
                    //우측에 포커싱 Traversal위젯이 존재할 경우
                    if(rightComp != null) {
                        super.onKeyDown(keycode);
                        return;
                    } else {
                        //그 외는 열+1, 행=0
                        focusRowInPage+=1;
                        focusColumnInPage=0;
                    }

                }


            } else {
                //끝자락이 아닐경우
                focusColumnInPage+=1;
            }
        } else if(keycode == Keys.VK_LEFT) {
            if (focusColumnInPage == 0) {
                //처음에 있을 경우
                if (focusRowInPage == 0) {
                    //첫째열

                    //좌측에 포커싱 Traversal위젯이 존재할 경우
                    if (leftComp != null) {
                        super.onKeyDown(keycode);
                        return;
                    } else {
                        focusColumnInPage = column - 1;
                        focusRowInPage = row - 1;
                        this.goPreviousPage();
                    }
                    return;
                } else {
                    //좌측에 포커싱 Traversal위젯이 존재할 경우
                    if (leftComp != null) {
                        super.onKeyDown(keycode);
                        return;
                    } else {
                        //그 외는 열=-1, 행=last
                        focusRowInPage -= 1;
                        focusColumnInPage = column - 1;
                    }

                }
            } else {
                //처음이 아닐경우
                focusColumnInPage -= 1;
            }
        }

        if(isActivated()) {
            LOG.print(this, "row : "+focusRowInPage);
            LOG.print(this, "column : "+focusColumnInPage);
            int index = (currentPage-1)*(row*column)+column*focusRowInPage+focusColumnInPage;
            Widget item = getWidget(index);
            if (item != null) {
                swapItemActivate(item);
            }

        }

//        if(keycode == Keys.VK_OK) {
//            this.activatedWidget.okKeyPressed();
//        }
        //super.onKeyDown(keycode);
    }

    public void onAdapterChanged() {
        int width = (adapter.width()+adapter.padding()*2)*column;
        int height = (adapter.height()+adapter.padding()*2)*(row+1);
        this.setSize(width, height);
    }

    protected int maxPage() {
        int maxPage = (adapter.totalCount()/(row*column))+1;
        return maxPage;
    }

    protected void onPageChanged(int page) {
        LOG.print(this, "Page Changed : "+ page);

        removeAll();
        repaint();
        for(int i=0; i<row; ++i) {

            int line = row*(page-1)+i;
            for (int j=0;j<column; ++j) {
                int position=column*line+j;
                LOG.print(this, "Position : "+position);
                if(position > adapter.totalCount()-1) {
                    break;
                }
                Widget item =adapter.item(position);
                item.setBounds(adapter.padding()+j*(adapter.height()+adapter.padding()), adapter.padding()+i*(adapter.height()+adapter.padding()), adapter.width(), adapter.height());
                this.add(item);
                item.setFocusTraversal(getWidget(position-row), getWidget(position+1), getWidget(position+row), getWidget(position-1));
            }
        }
        int index = (page-1)*(row*column)+column*focusRowInPage+focusColumnInPage;
        Widget _item = getWidget(index);
        if(_item != null) {
            swapItemActivate(_item);
        }

        repaint();
    }
    public void paint(Graphics g) {
        super.paint(g);
    }
}
