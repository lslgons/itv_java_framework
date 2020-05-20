package com.cj.tvui.ui;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import com.cj.tvui.Keys;
import com.cj.tvui.util.LOG;

/**
 * 포커싱 가능한 컴포넌트 클래스
 * @author daegon.kim (daegon.kim1@cj.net)
 * @since 2017-12-05
 */
public abstract class Widget extends Container {


    private boolean isActivated = false;

    /**
     * Widget 활성화, 포커싱 이벤트 발생
     */
    public void activate() {
        isActivated = true;
        onActivated();
    }

    /**
     * Widget 비활성화, 포커싱 해제 이벤트 발생
     */
    public void deactivate() {
        isActivated = false;
        onDeactivated();
    }

    /**
     * 활성화 여부
     * @return true면 활성화됨
     */
    public boolean isActivated() {
        return this.isActivated;
    }
	/**
	 * 포커스 활성화되었을 경우
	 */
	protected abstract void onActivated();
	/**
	 * 포커스 비활성화되었을 경우
	 */
	protected abstract void onDeactivated();
	/**
	 * Key 이벤트
	 * 설정된 Focus Traversal에 따라 포커스 이동함...
	 * Focus Traversal 정책 변경이 필요하다면 오버라이드 실시...
	 * @param keycode 발생된 키 이벤트
	 */
	public void onKeyDown(int keycode) {
		Scene parent = (Scene)getParent();
		switch(keycode) {
		case Keys.VK_UP:
			if(topComp != null) {
				parent.setWidgetActivate(topComp);
			}
			break;
		case Keys.VK_RIGHT:
			if(rightComp != null) {
				parent.setWidgetActivate(rightComp);
			}
			break;
		case Keys.VK_DOWN:
			if(bottomComp != null) {
				parent.setWidgetActivate(bottomComp);
			}
			break;
			
		case Keys.VK_LEFT:
			if(leftComp != null) {
				parent.setWidgetActivate(leftComp);
			}
			break;
		case Keys.VK_OK:
			this.okKeyPressed();
			break;
		}
	}

    /**
     * 확인키 발생 시, 일반적으로 확인키만을 사용하게 되나 방향키에 대한
     * 이벤트는 onKeyDown을 오버라이드하여 사용
     */
	public abstract void okKeyPressed(); 

	
	public final static int AT_WIDGET_TOP = 10;
	public final static int AT_WIDGET_RIGHT = 11;
	public final static int AT_WIDGET_BOTTOM = 12;
	public final static int AT_WIDGET_LEFT = 13;
	
	protected Widget topComp=null;
	protected Widget rightComp=null;
	protected Widget bottomComp=null;
	protected Widget leftComp=null;

	
	public Widget() {
		//LOG.print(this, "===========>>> Widget Constructor");
	}

    /**
     * 현재 객체로부터 키이벤트 발생 시 상하좌우 특정 위젯으로 이동할 것인지 설정
     * @param where 위치
     * @param target 대상Widget
     */
	public void setFocusTraversal(int where, Widget target) {
		switch(where) {
		case AT_WIDGET_TOP:
			topComp = target;
			break;
		case AT_WIDGET_RIGHT:
			rightComp = target;
			break;
		case AT_WIDGET_BOTTOM:
			bottomComp = target;
			break;
		case AT_WIDGET_LEFT:
			leftComp = target;
			break;
		default:
			LOG.print(this, "Invalid Type");
		}
	}
	/**
	 * 한번에 전체 Focus Traversal 설정
	 * top, right, bottom, left
	 */
	public void setFocusTraversal(Widget top, Widget right, Widget bottom, Widget left) {
		topComp = top;
		rightComp = right;
		bottomComp = bottom;
		leftComp = left;
	}
	
	

	
}
