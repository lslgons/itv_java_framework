package example;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.KeyController;
import com.cj.tvui.network.StbServer;
import com.cj.tvui.network.StbServer.ClientMessageReceiver;
import com.cj.tvui.ui.Scene;
import com.cj.tvui.ui.Widget;
import com.cj.tvui.util.LOG;


/**
 * Widget활용 Scene Example
 * @author daegon.kim
 *
 */
public class MainScene2 extends Scene {

	List compList = new LinkedList();
	
	interface ItemCompEvent {
		public void doAction();
	}
	
	public class ItemComp extends Widget {
		Color bgColor;
		String text;
		boolean isActivated = false;
		ItemCompEvent evt = null;
		public ItemComp(Color bgColor, String txt, ItemCompEvent e) {
			this.bgColor = bgColor;
			this.text = txt;
			this.evt = e;
		}
		public void paint(Graphics g) {
			g.setColor(bgColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(0,0,0));
			g.drawString(this.text, 30, getHeight()/2);
			if(isActivated) {
				int offset = 1;
				g.drawRect(0, 0, getWidth()-offset*2, getHeight()-offset*2);
			}
			super.paint(g);
		}
		public void onActivated() {
			LOG.print(this, "["+text+"]onActivated");
			this.isActivated = true;
		}
		public void onDeactivated() {
			LOG.print(this, "["+text+"]onDeactivated");
			this.isActivated = false;
		}
		public void okKeyPressed() {
			evt.doAction();
		}
	}
	
	//StbServerMessageReceiver
	ClientMessageReceiver receiver = null;
	
	public void onInit() {
		LOG.print(this, "Main Scene 2 Init");
		setLayout(new GridLayout(3,3));
		
		compList = new LinkedList();
		compList.add(new ItemComp(new Color(255,255,255), "TV Framework Example", null));
        compList.add(new ItemComp(new Color(177, 188, 206), "Network Test", new ItemCompEvent() {
			
			public void doAction() {
				openPopup("example.NetworkPopup", null);
			}
		}));
        compList.add(new ItemComp(new Color(165, 180, 204), "Push Other Scene", new ItemCompEvent() {
			
			public void doAction() {
				pushScene("example.SecondScene", null);
				
			}
		}));
        compList.add(new ItemComp(new Color(148, 170, 204), "Open Popup", new ItemCompEvent() {
			
			public void doAction() {
				openPopup("example.MainPopup", null);
				
			}
		}));
        compList.add(new ItemComp(new Color(127, 154, 198), "Image", new ItemCompEvent() {
			
			public void doAction() {
				pushScene("example.ImageScene", null);
				
			}
		}));
        compList.add(new ItemComp(new Color(111, 144, 198), "AV Size", new ItemCompEvent() {
			
			public void doAction() {
				pushScene("example.AVScene", null);
				
			}
		}));
        compList.add(new ItemComp(new Color(99, 132, 176), "Toggle Static Scene", new ItemCompEvent() {
			
			public void doAction() {
				if(isSetStaticScene()) {
        			if(isShowStaticScene()) {
        				hideStaticScene();
        			} else {
        				showStaticScene();
        			}
        		} else {
        			setStaticScene("example.StaticScene", null);
        		}
				
			}
		}));
        compList.add(new ItemComp(new Color(87, 121, 164), "Key Consume Test", new ItemCompEvent() {
			
			public void doAction() {
				pushScene("example.KeyEventScene", null);
			}
		}));
        compList.add(new ItemComp(new Color(79, 110, 158), "VOD Test", new ItemCompEvent() {
			
			public void doAction() {
				pushScene("example.VODScene", null);
			}
		}));
        for(int i=0;i<compList.size();++i) {
        	add((Component)compList.get(i));
        }
        KeyController.getInstance().setEnableBackKey(true);
        setWidgetActivate((Widget)compList.get(1));
        //Set Focus Traversal
        for(int i=1;i<compList.size();++i) {
        	ItemComp comp = (ItemComp)compList.get(i);
        	if(i>=1 && i<compList.size()) {
        		if (i == 1) {
        			comp.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget)compList.get(8));
            		comp.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget)compList.get(i+1));
        		} else if(i == 8) {
        			comp.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget)compList.get(i-1));
            		comp.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget)compList.get(1));
        		} else {
        			comp.setFocusTraversal(Widget.AT_WIDGET_LEFT, (Widget)compList.get(i-1));
            		comp.setFocusTraversal(Widget.AT_WIDGET_RIGHT, (Widget)compList.get(i+1));
        		}
        		if (i==1 || i==2) {
        			comp.setFocusTraversal(Widget.AT_WIDGET_TOP, (Widget)compList.get(i+6));
            		comp.setFocusTraversal(Widget.AT_WIDGET_BOTTOM, (Widget)compList.get(i+3));
        		} else if(i == 3) {
        			comp.setFocusTraversal(Widget.AT_WIDGET_TOP, (Widget)compList.get(6));
            		comp.setFocusTraversal(Widget.AT_WIDGET_BOTTOM, (Widget)compList.get(6));
        		} else if(i == 6) {
        			comp.setFocusTraversal(Widget.AT_WIDGET_TOP, (Widget)compList.get(3));
            		comp.setFocusTraversal(Widget.AT_WIDGET_BOTTOM, (Widget)compList.get(3));
        		} else if(i==7 || i==8) {
        			comp.setFocusTraversal(Widget.AT_WIDGET_TOP, (Widget)compList.get(i-3));
            		comp.setFocusTraversal(Widget.AT_WIDGET_BOTTOM, (Widget)compList.get(i-6));
        		} else {
        			comp.setFocusTraversal(Widget.AT_WIDGET_TOP, (Widget)compList.get(i-3));
            		comp.setFocusTraversal(Widget.AT_WIDGET_BOTTOM, (Widget)compList.get(i+3));
        		}
        		
        	}
        }
        LOG.print(this, "Register Receiver");
        receiver = new ClientMessageReceiver() {
    		
    		public void messageReceived(String msg) {
    			LOG.print(this, msg);
    			openPopup("example.MessageReceivePopup", new String[]{msg});
    			
    		}
    	};
        StbServer.getInstance().registerReceiver(receiver);

	}

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub

	}

	public void onShow() {
		// TODO Auto-generated method stub

	}

	public void onHide() {
		// TODO Auto-generated method stub

	}

	public void onDestroy() {
		//StbServer 내 Register 수행 후에는 반드시 unregister를 해야 함
		StbServer.getInstance().unregisterReceiver(receiver);

	}

	public void onKeyDown(int keycode) {
		// TODO Auto-generated method stub

	}

	public void onPaint(Graphics g) {
		// TODO Auto-generated method stub

	}

	public void timerWentOff() {
		// TODO Auto-generated method stub

	}

}
