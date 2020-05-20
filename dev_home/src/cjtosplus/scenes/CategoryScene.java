package cjtosplus.scenes;

import cjtosplus.comp.category.GridItem;
import cjtosplus.comp.category.SideMenuItem;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.ui.Widget;
import com.cj.tvui.ui.layout.GridList;
import com.cj.tvui.ui.layout.ListItemAdapter;
import com.cj.tvui.ui.layout.VerticalList;
import com.cj.tvui.util.LOG;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by user on 2017-10-31.
 */
public class CategoryScene extends Scene {
    VerticalList menuList;
    GridList productList;

    class ProductItemAdapter implements ListItemAdapter {
        LinkedList items;

        public ProductItemAdapter() {
            items = new LinkedList();
            for(int i=0;i<10;++i) {
                GridItem item = new GridItem("Grid Item "+i);
                items.add(i, item);
            }
        }

        public int totalCount() {
            return items.size();
        }

        public Widget item(int index) {
            return (Widget) items.get(index);
        }

        public int width() {
            return 200;
        }

        public int height() {
            return 200;
        }

        public int padding() {
            return 15;
        }
    }

    class MenuItemAdapter implements ListItemAdapter {
        LinkedList items = new LinkedList();

        public MenuItemAdapter() {
            SideMenuItem item1 = new SideMenuItem("Box 1");
            item1.setBackground(Color.blue);
            items.add(0, item1);
            SideMenuItem item2 = new SideMenuItem("Box 2");
            item2.setBackground(Color.black);
            items.add(1, item2);
            SideMenuItem item3 = new SideMenuItem("Box 3");
            item3.setBackground(Color.yellow);
            items.add(2, item3);
            SideMenuItem item4 = new SideMenuItem("Box 4");
            item3.setBackground(Color.yellow);
            items.add(3, item4);
            SideMenuItem item5 = new SideMenuItem("Box 5");
            item3.setBackground(Color.yellow);
            items.add(4, item5);
        }

        public int totalCount() {
            return items.size();
        }

        public Widget item(int i) {
            return (Widget)items.get(i);
        }

        public int width() {
            return 300;
        }

        public int height() {
            return 100;
        }

        public int padding() {
            return 10;
        }
    }

    public void onInit() {
        LOG.print(this, "CategoryScene onInit");
        menuList = new VerticalList(3, new MenuItemAdapter());
        productList = new GridList(2,2, new ProductItemAdapter());
//        productList = new VerticalList(3, new MenuItemAdapter());
        productList.setLocation(300, productList.getY());
        this.add(menuList);
        this.add(productList);
//        LOG.print(this, menuList.getWidth()+", "+menuList.getHeight());
//        LOG.print(this, menuList.getMaxPage()+", "+menuList.getCurrentPage());
        setWidgetActivate(productList);
        menuList.setFocusTraversal(Widget.AT_WIDGET_RIGHT, productList);
        productList.setFocusTraversal(Widget.AT_WIDGET_LEFT, menuList);

    }

    public void onDataReceived(Object[] objects) {

    }

    public void onShow() {

    }

    public void onHide() {

    }

    public void onDestroy() {

    }

    public void onKeyDown(int i) {
        LOG.print(this, "onKeyDown : "+i);

    }

    public void onPaint(Graphics graphics) {

    }

    public void timerWentOff() {

    }
}
