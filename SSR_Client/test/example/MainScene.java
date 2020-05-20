package example;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import com.cj.tvui.Keys;
import com.cj.tvui.controller.KeyController;

import com.cj.tvui.ui.Scene;
import com.cj.tvui.util.LOG;


/**
 * Created by daegon.kim on 2016-12-02.
 */
public class MainScene extends Scene {
	
	List compList = new LinkedList();
	
	public class ItemComp extends Component {
		Color bgColor;
		String text;
		public ItemComp(Color bgColor, String txt) {
			this.bgColor = bgColor;
			this.text = txt;
		}
		public void paint(Graphics g) {
			//LOG.print(this, "paint...."+this.text);
			g.setColor(bgColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(0,0,0));
			g.drawString(this.text, 30, getHeight()/2);
			super.paint(g);
		}
		
		
	}
	
	int mFocusIndex = 1; //0,1,2,3,4,5
	
    public void onInit() {
        LOG.print(this, "Main Scene Init");
        //SceneController.getInstance().setStaticScene("cjtcom.StaticScene", null);
        setLayout(new GridLayout(3,3)); //Grid Layout
        
        compList = new LinkedList();
        compList.add(new ItemComp(new Color(255,255,255), "TV Framework Example"));
        compList.add(new ItemComp(new Color(177, 188, 206), "Network Test"));
        compList.add(new ItemComp(new Color(165, 180, 204), "Push Other Scene"));
        compList.add(new ItemComp(new Color(148, 170, 204), "Open Popup"));
        compList.add(new ItemComp(new Color(127, 154, 198), "Image"));
        compList.add(new ItemComp(new Color(111, 144, 198), "AV Size"));
        compList.add(new ItemComp(new Color(99, 132, 176), "Toggle Static Scene"));
        compList.add(new ItemComp(new Color(87, 121, 164), "Key Consume Test"));
        compList.add(new ItemComp(new Color(79, 110, 158), "VOD Test"));
        for(int i=0;i<compList.size();++i) {
        	add((Component)compList.get(i));
        }
        KeyController.getInstance().setEnableBackKey(true);

    }

    public void onShow() {
        LOG.print(this, "onShow");
        
        
    }

    public void onHide() {
        LOG.print(this, "onHide");
    }

    public void onDestroy() {
        LOG.print(this, "onDestroy");

    }

    public void onKeyDown(int keycode) {
        LOG.print(this, "Key Down :: " + keycode);
        int itemCount = getComponentCount();
        switch(keycode) {
        case Keys.VK_RIGHT:
        	if(mFocusIndex == (itemCount-1)) mFocusIndex = 1;
        	else ++mFocusIndex;
        	break;
        case Keys.VK_LEFT:
        	if(mFocusIndex ==1) mFocusIndex = (itemCount-1);
        	else --mFocusIndex;
        	break;
        case Keys.VK_UP:
        	if(mFocusIndex == 3) {
        		mFocusIndex += 3;
        	} else if(mFocusIndex == 6) {
        		mFocusIndex -= 3;
        	} else {
        		mFocusIndex -= 3;
            	if(mFocusIndex < 0) mFocusIndex += itemCount;
        	}
        
        	break;
        case Keys.VK_DOWN:
        	if(mFocusIndex == 3) {
        		mFocusIndex +=3;
        	} else if(mFocusIndex == 6) {
        		mFocusIndex -= 3;
        	} else {
        		mFocusIndex += 3;
            	if(mFocusIndex >= itemCount) mFocusIndex -= itemCount;
        	}
        	
        	break;
        case Keys.VK_OK:
        	switch(mFocusIndex) {
        	case 1:
        		openPopup("example.NetworkPopup", null);
        		break;
        	case 2:
        		pushScene("example.SecondScene", null);
        		break;
        	case 3:
        		openPopup("example.MainPopup", null);
        		break;
        	case 4:
        		pushScene("example.ImageScene", null);
        		break;
        	case 5:
        		pushScene("example.AVScene", null);
        		break;
        	case 6:
        		if(isSetStaticScene()) {
        			if(isShowStaticScene()) {
        				hideStaticScene();
        			} else {
        				showStaticScene();
        			}
        		} else {
        			setStaticScene("example.StaticScene", null);
        		}
        		
        		break;
        	case 7:
        		pushScene("example.KeyEventScene", null);
        		break;
        	case 8:
        		pushScene("example.VODScene", null);
        		
        		break;
    		default:
    			LOG.print(this, "No Action");
        	}
        	
        	break;
    	default:
    		break;
        }

    }
        
    
    
    

    public void onPaint(Graphics g) {
        Component[] comps = getComponents();
        if(comps.length != 0) {
        	Component curComp = comps[mFocusIndex];
        	int x = curComp.getX();
        	int y = curComp.getY();
            int compWidth = curComp.getWidth();
            int compHeight = curComp.getHeight();
            g.setColor(new Color(0,0,0)); //Black
            //LOG.print("Child Compoenent : "+x + " "+y+" "+compWidth+" "+compHeight);
//            Graphics2D g2d = (Graphics2D)g;
//            g2d.setStroke(new BasicStroke(3f));
            int offset = 1;
            g.drawRect(x+offset, y+offset, compWidth-offset*2, compHeight-offset*2);
            
            
        }
        
        
//        g.setColor(Color.blue);
//        g.drawRect(0,0,100,100);
//        g.fillRect(100,100,200,200);
    }

    public void timerWentOff() {
        //LOG.print(this, "timer");
    }

	public void onDataReceived(Object[] args) {
		// TODO Auto-generated method stub
		
	}
}
