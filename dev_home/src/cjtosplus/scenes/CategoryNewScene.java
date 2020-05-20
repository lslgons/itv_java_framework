package cjtosplus.scenes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.util.LinkedList;
import java.util.List;

import com.cj.tvui.Keys;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.ui.Widget;
import com.cj.tvui.ui.layout.GridList;
import com.cj.tvui.ui.layout.ListItemAdapter;
import com.cj.tvui.ui.layout.VerticalList;

import cjtosplus.comp.Util;
import cjtosplus.comp.category.GridItem;
import cjtosplus.comp.category.GridItemExtend;
import cjtosplus.comp.category.SideMenuItem;
import cjtosplus.comp.category.SideMenuItemExtend;
import cjtosplus.comp.main.Button;
import cjtosplus.scenes.CategoryScene.MenuItemAdapter;
import cjtosplus.scenes.CategoryScene.ProductItemAdapter;

public class CategoryNewScene  extends Scene{
	private VerticalList menuList;
	private GridList productList;
	
//	/** 메인배경 */
//	private Image main_bg;
//	/** 카테고리 목록 배경 */
//	private Image main_list_bg;
//	/** 상품 기본이미지(315x145) */
//	private Image default_315x145;
	
//	/** 상품이미지 frame */
//	private Image image_frame_315;
	
	/** 상품혜택가 화살표  */
	private Image main_arrow_price;
	
	private String currentMenuName;
	
	
	
	public void onInit() {
		// TODO Auto-generated method stub
		System.out.println("CategoryNewScene is onInit..");
//		 menuList = new VerticalList(3, new MenuItemAdapter());
		menuList = new VerticalList(10, new MenuItemAdapter()); // 10개인 경우 한페이지인 경우 버그 있음. 다음페이지로 넘어가며, 아무 메뉴도 안나옴..생성하는 파람 보다. 한개씩 더 생성되는 것 같음. 추후 확인요망.
		menuList.setLocation(50+4+2+2, 34+2+2+2+1);

		this.add(menuList);
		
		productList = new GridList(2,2, new ProductItemAdapter());
//		productList.setLocation(300, productList.getY());
//		productList.setLocation(204+100, productList.getY());
		productList.setLocation(204+100, 40-2);
//		productList.setSize(720, 480);
		productList.setSize(960, 540);
		this.add(productList);

	
		
		menuList.setFocusTraversal(Widget.AT_WIDGET_RIGHT, productList);
        productList.setFocusTraversal(Widget.AT_WIDGET_LEFT, menuList);
		
    	setWidgetActivate(menuList);
//    	productList.setVisible(false);
		 
		loadInitImage();
		
//        addBackgroundImage("main_arrow_price", loadImage("main_icon_noti.png", true), 0,200,100,100);
//        addBackgroundImage("image_frame_315", loadImage("main_arrow_price.png", true), 0,0,100,100);
        
        addBackgroundImage("main_list_bg", loadImage("main_list_bg.png", true), 59, 44, 145, 428); //	/** 카테고리 목록 배경 */
        addBackgroundImage("main_bg", loadImage("main_bg.png", true), 0, 0, 960, 540); //	/** 메인배경 */
        
        
	}
	
	public void onShow() {
		// TODO Auto-generated method stub
		System.out.println("CategoryNewScene is onShow..");
		setWidgetActivate(menuList); // GridItemExtend가 focus가 사라지지 않는 증상이 있다. 문의예정.
		
	}

	public void onHide() {
		// TODO Auto-generated method stub
		System.out.println("CategoryNewScene is onHide..");
		
	}

	public void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("CategoryNewScene is onDestroy..");
		
	}


	public void onDataReceived(Object[] arg0) {
		// TODO Auto-generated method stub
		
	}


	public void onKeyDown(int keyCode) {
		// TODO Auto-generated method stub
		switch(keyCode) {
		case Keys.VK_BACK:
		case 8: // backSpace key For emul...
			popScene(); // 이전으로 처리. :맨위에 있는 scene을 stack에서 한개 빼고 나오게 한다.
			break;
		case Keys.VK_OK:
//			System.out.println("188888888888 ok");
//			this.menuList.getWidget(arg0)
			System.out.println("["+this.menuList.getCurrentPage() +"]this.menuList.getCurrentPage()");
//			this.menuList.
			
//			Button btn02 = (Button)compList.get(1);
			
//			MenuItemAdapter menuItemAdapter =(MenuItemAdapter)((List) menuList).get(1);
//			System.out.println("["+menuItemAdapter.item(0).toString()+"]");
			
//			 VerticalList temp = (VerticalList)menuList.get
			
			System.out.println("!!!!!!!!!!!"+ this.menuList.getWidget(0).getHeight()+"--------");
			
//			this.menuList.getcu
			
			SideMenuItemExtend temp =(SideMenuItemExtend) this.menuList.getWidget(0);
			temp.getTitle();
			System.out.println("----------- getTitle name..." +temp.getTitle());
			
			System.out.println("----------- isFocusable name..."+menuList.getWidget(0).isActivated()+"----");
			
			
			for(int i=0; i<10; i++) {
				if(menuList.getWidget(i).isActivated()) {
					SideMenuItemExtend temp02 =(SideMenuItemExtend) this.menuList.getWidget(i);
					currentMenuName = temp02.getTitle();
					
					temp02.setSelected(true);
					
					 setWidgetActivate(productList);
				}
			}
//			System.out.println("currentMenuName" +currentMenuName);
			
			if(currentMenuName.trim().equals("패션")) {
				System.out.println("========== 패션..");
			}else if(currentMenuName.trim().equals("레포츠")) {
				System.out.println("========== 레포츠..");
			}else if(currentMenuName.trim().equals("뷰티")) {
				System.out.println("========== 뷰티..");
			}else if(currentMenuName.trim().equals("리빙")) {
				System.out.println("========== 리빙..");
			}else if(currentMenuName.trim().equals("주방")) {
				System.out.println("========== 주방..");
			}else if(currentMenuName.trim().equals("식품")) {
				System.out.println("========== 식품..");
			}else if(currentMenuName.trim().equals("가전")) {
				System.out.println("========== 가전..");
			}else if(currentMenuName.trim().equals("기타")) {
				System.out.println("========== 기타..");
			}else if(currentMenuName.trim().equals("고객센터")) {
				System.out.println("========== 고객센터..");
			}else if(currentMenuName.trim().equals("마이존")) {
				System.out.println("========== 마이존..");
			}
			
			break;
			
		case Keys.VK_UP:
		case Keys.VK_DOWN:
//			for(int i=0; i<10; i++) {
//				if(menuList.getWidget(i).isActivated()) {
//					SideMenuItemExtend temp02 =(SideMenuItemExtend) this.menuList.getWidget(i);
//					currentMenuName = temp02.getTitle();
//					
//					temp02.setSelected(false);
//				}
//			}
			break;
			
		case Keys.VK_RIGHT:
			for(int i=0; i<10; i++) {
				if(menuList.getWidget(i).isActivated()) { // menuList에 포커스가 있으면, selected로 변경시켜준다.
					SideMenuItemExtend temp02 =(SideMenuItemExtend) this.menuList.getWidget(i);
					currentMenuName = temp02.getTitle();
					
					temp02.setSelected(true);
				}
			}
			break;
		case Keys.VK_LEFT:
			for(int i=0; i<10; i++) {
				if(!menuList.getWidget(i).isActivated()) {// menuList에 포커스가 없으면, Selected를 풀어준다.
					SideMenuItemExtend temp02 =(SideMenuItemExtend) this.menuList.getWidget(i);
					currentMenuName = temp02.getTitle();
					
					temp02.setSelected(false);
				}
			}
			break;
			
		}
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
//		g.drawImage(image_frame_315, 0+200, 10, 100, 100, this);
//		g.drawImage(main_arrow_price, 0, 400, 100, 100, this);
	
	}

	public void timerWentOff() {
		// TODO Auto-generated method stub
		
	}
	
	private void loadInitImage() {
//		image_frame_315 = loadImage("image_frame_315.png", true);
		main_arrow_price = loadImage("main_arrow_price.png", true);
	}
	
	 class MenuItemAdapter implements ListItemAdapter {
	        private LinkedList items = new LinkedList();

	        public MenuItemAdapter() {
//	            SideMenuItem item1 = new SideMenuItem("Box 1");
//	            item1.setBackground(Color.blue);
//	            items.add(0, item1);
//	            SideMenuItem item2 = new SideMenuItem("Box 2");
//	            item2.setBackground(Color.black);
//	            items.add(1, item2);
//	            SideMenuItem item3 = new SideMenuItem("Box 3");
//	            item3.setBackground(Color.yellow);
//	            items.add(2, item3);
//	            SideMenuItem item4 = new SideMenuItem("Box 4");
//	            item3.setBackground(Color.yellow);
//	            items.add(3, item4);
//	            SideMenuItem item5 = new SideMenuItem("Box 5");
//	            item3.setBackground(Color.yellow);
//	            items.add(4, item5);
	        	
	        	 SideMenuItemExtend item1 = new SideMenuItemExtend("패션",
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_fashion.png",true),
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_fashion_f.png",true),
		    			 83-24,
		    			 53-9,
		    			 145,
		    			 42,
		    			loadImage("common_popup_foc_02.png", true),
		 				loadImage("common_popup_foc_01.png", true),
		 				loadImage("common_popup_foc_03.png", true),
		 				loadImage("common_popup_foc_02.png", true) );
		            item1.setBackground(Color.blue);
		            items.add(0, item1);
		            
	            SideMenuItemExtend item2 = new SideMenuItemExtend("레포츠",
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_sports.png",true),
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_sports_f.png",true),
		    			 83-24,
		    			 53-9,
		    			 145,
		    			 42,
		    			loadImage("common_popup_foc_02.png", true),
		 				loadImage("common_popup_foc_01.png", true),
		 				loadImage("common_popup_foc_03.png", true),
		 				loadImage("common_popup_foc_02.png", true) );
		            item2.setBackground(Color.black);
		            items.add(1, item2);
		            
	            SideMenuItemExtend item3 = new SideMenuItemExtend("뷰티",
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_beauty.png",true),
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_beauty_f.png",true),
		    			 83-24,
		    			 53-9,
		    			 145,
		    			 42,
		    			loadImage("common_popup_foc_02.png", true),
		 				loadImage("common_popup_foc_01.png", true),
		 				loadImage("common_popup_foc_03.png", true),
		 				loadImage("common_popup_foc_02.png", true) );
		            item3.setBackground(Color.yellow);
		            items.add(2, item3);
		            
	            SideMenuItemExtend item4 = new SideMenuItemExtend("리빙",
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_living.png",true),
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_living_f.png",true),
		    			 83-24,
		    			 53-9,
		    			 145,
		    			 42,
		    			loadImage("common_popup_foc_02.png", true),
		 				loadImage("common_popup_foc_01.png", true),
		 				loadImage("common_popup_foc_03.png", true),
		 				loadImage("common_popup_foc_02.png", true) );
	            item3.setBackground(Color.yellow);
	            items.add(3, item4);
		            
	            SideMenuItemExtend item5 = new SideMenuItemExtend("주방",
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_kitchen.png",true),
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_kitchen_f.png",true),
		    			 83-24,
		    			 53-9,
		    			 145,
		    			 42,
		    			loadImage("common_popup_foc_02.png", true),
		 				loadImage("common_popup_foc_01.png", true),
		 				loadImage("common_popup_foc_03.png", true),
		 				loadImage("common_popup_foc_02.png", true) );
	            item3.setBackground(Color.yellow);
	            items.add(4, item5);
	            
	            SideMenuItemExtend item6 = new SideMenuItemExtend("식품",
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_food.png",true),
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_food_f.png",true),
		    			 83-24,
		    			 53-9,
		    			 145,
		    			 42,
		    			loadImage("common_popup_foc_02.png", true),
		 				loadImage("common_popup_foc_01.png", true),
		 				loadImage("common_popup_foc_03.png", true),
		 				loadImage("common_popup_foc_02.png", true) );
	            item6.setBackground(Color.yellow);
	            items.add(5, item6);
	            
	            SideMenuItemExtend item7 = new SideMenuItemExtend("가전",
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_electronics.png",true),
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_electronics_f.png",true),
		    			 83-24,
		    			 53-9,
		    			 145,
		    			 42,
		    			loadImage("common_popup_foc_02.png", true),
		 				loadImage("common_popup_foc_01.png", true),
		 				loadImage("common_popup_foc_03.png", true),
		 				loadImage("common_popup_foc_02.png", true) );
	            item7.setBackground(Color.yellow);
	            items.add(6, item7);
	            
	            SideMenuItemExtend item8 = new SideMenuItemExtend("기타",
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_other.png",true),
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_other_f.png",true),
		    			 83-24,
		    			 53-9,
		    			 145,
		    			 42,
		    			loadImage("common_popup_foc_02.png", true),
		 				loadImage("common_popup_foc_01.png", true),
		 				loadImage("common_popup_foc_03.png", true),
		 				loadImage("common_popup_foc_02.png", true) );
	            item8.setBackground(Color.yellow);
	            items.add(7, item8);
	            
	            SideMenuItemExtend item9 = new SideMenuItemExtend("고객센터",
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_customers.png",true),
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_customers_f.png",true),
		    			 83-24,
		    			 53-9,
		    			 145,
		    			 42,
		    			loadImage("common_popup_foc_02.png", true),
		 				loadImage("common_popup_foc_01.png", true),
		 				loadImage("common_popup_foc_03.png", true),
		 				loadImage("common_popup_foc_02.png", true) );
	            item9.setBackground(Color.yellow);
	            items.add(8, item9);
	            
	            SideMenuItemExtend item10 = new SideMenuItemExtend("마이존",
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_myzone.png",true),
		    			 loadImage("http://210.122.102.109:10080/ktbmt2/images/category/menu_myzone_f.png",true),
		    			 83-24,
		    			 53-9,
		    			 145,
		    			 42,
		    			loadImage("common_popup_foc_02.png", true),
		 				loadImage("common_popup_foc_01.png", true),
		 				loadImage("common_popup_foc_03.png", true),
		 				loadImage("common_popup_foc_02.png", true) );
	            item10.setBackground(Color.yellow);
	            items.add(9, item10);
	            
//	            SideMenuItemExtend item11 = new SideMenuItemExtend("테스트",
//		    			 loadImage("main_icon_big.png",true),
//		    			 loadImage("main_btn_nor.png",true),
//		    			 83-24,
//		    			 53-9,
//		    			 145,
//		    			 42,
//		    			loadImage("common_popup_foc_02.png", true),
//		 				loadImage("common_popup_foc_01.png", true),
//		 				loadImage("common_popup_foc_03.png", true),
//		 				loadImage("common_popup_foc_02.png", true) );
//	            item11.setBackground(Color.yellow);
//	            items.add(10, item11);
	            
	            
	        }

	        public int totalCount() {
	            return items.size();
	        }

	        public Widget item(int i) {
	            return (Widget)items.get(i);
	        }

	        public int width() {
	            return 145;
	        }

	        public int height() {
	            return 42;
	        }

	        public int padding() {
	            return 1;
	        }
	        
	    }
	 
	class ProductItemAdapter implements ListItemAdapter {
		LinkedList items;
		

		public ProductItemAdapter() {
			items = new LinkedList();
			for (int i = 0; i < 10; ++i) {
//				GridItem item = new GridItem("Grid Item " + i);
//				items.add(i, item);
//				GridItemExtend item = new GridItemExtend("GridItemExtend Item " + i);
				GridItemExtend item = new GridItemExtend("GridItemExtend Item " + i, 0, 0, 220, 200,  loadImage("main_arrow_price.png", true)); //widht가 220이 넘어가면, comp가 겹친다.
//				GridItemExtend item = new GridItemExtend("GridItemExtend Item " + i, 0, 0, 100+300, 100+100+100);
//				GridItemExtend item = new GridItemExtend("GridItemExtend Item " + i, 0, 0, 100, 50);
//				GridItemExtend item = new GridItemExtend("GridItemExtend Item " + i, 0, 0, 100*2, 50*2);
//				GridItemExtend item = new GridItemExtend("GridItemExtend Item " + i, 0, 0, 100*3, 50*3); 
//				GridItemExtend item = new GridItemExtend("GridItemExtend Item " + i, 0, 0, 100*4, 50*4);
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
//			return 200;
//			return 349+200;
			return 220+100; //width와 height 가 같은 정사각형 일 경우에만 겹치지 않고, 직사각형인 경우는 레이아웃이 겹치는 현상이 발생한다.
		}

		public int height() {
//			return 200;
//			return 200+10;
			return 114+10+10+50+10+20;
		}

		public int padding() {
			return 10;
		}
	}

}
