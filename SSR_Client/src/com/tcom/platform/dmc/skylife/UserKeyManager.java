package com.tcom.platform.dmc.skylife;

import java.awt.event.KeyEvent;


import com.tcom.platform.controller.KeyController;
import com.tcom.ssr.SSRConfig;
import com.tcom.util.LOG;
import org.davic.resources.ResourceClient;
import org.davic.resources.ResourceProxy;
import org.dvb.event.EventManager;
import org.dvb.event.UserEvent;
import org.dvb.event.UserEventListener;
import org.dvb.event.UserEventRepository;
import org.havi.ui.event.HRcEvent;



/**
 * UserEvent로 받아야 하는 key들을 관리하는 클래스
 */
public class UserKeyManager implements ResourceClient {
	private UserEventRepository numRep;
	private boolean numEnabled = false;
	private UserEventAdapter numAdapter;

	private UserEventRepository prevRep;
	private boolean prevEnabled = false;
	private UserEventAdapter prevAdapter;

//	private static class Instance {
//		private static final UserKeyManager instance = new UserKeyManager();
//	}

	private static UserKeyManager instance;
	/**
	 * Singleton instance.
	 */
	public static UserKeyManager getInstance() {
		if (instance == null) {
			instance = new UserKeyManager();
		}
		return instance;
	}

	/**
	 * Constructor.
	 */
	private UserKeyManager() {
		numAdapter = new UserEventAdapter();
		numRep = new UserEventRepository("NumericKeyRepository");
		numRep.addAllNumericKeys();

		prevAdapter = new UserEventAdapter();
		prevRep = new UserEventRepository("PreviousKeyRepository");
		prevRep.addKey(HRcEvent.VK_F1); //for 'back' key, Skylife F1 Key

		LOG.print(this, "create UserKeyManager");
	}

	public void destroy() {
		LOG.print(this, "UserKeyManager destroy");
		releaseNumericKeys();
		releasePrevKey();
		numRep.removeAllNumericKeys();
		prevRep.removeKey(HRcEvent.VK_F1);

		numRep = null;
		numAdapter = null;

		prevRep = null;
		prevAdapter = null;

		instance = null;
	}

	/**
	 * Gets the UserEventRepository.
	 * @return UserEventRepository
	 */
	public UserEventRepository getNumRepository() {
		return this.numRep;
	}

	boolean addNumericKeys = false;

	/**
	 * Repository에 등록된 키들의 Event를 받는다. - '숫자'키
	 */
	public void reserveNumericKeys() {
		if (!numEnabled) {
			LOG.debug(this, "reserveNumericKeys() called");
			//EventManager.getInstance().addUserEventListener(numAdapter, numRep); //Shard 하게 등록
			if (SSRConfig.getInstance().IS_EMUL) {
				addNumericKeys = EventManager.getInstance().addUserEventListener(numAdapter, this, numRep); //Shard 하게 등록
			} else {
				EventManager.getInstance().addUserEventListener(numAdapter, this, numRep); //Shard 하게 등록
			}
			numEnabled = true;
		}
	}

	/**
	 * Key Event를 받지 않는다. - '숫자' 키
	 */
	public void releaseNumericKeys() {
		if (numEnabled) {
			LOG.debug(this, "releaseNumericKeys() called");
			EventManager.getInstance().removeUserEventListener(numAdapter);
			numEnabled = false;
			if (SSRConfig.getInstance().IS_EMUL) {
				addNumericKeys = false;
			}
		}
	}

	public boolean isReservedNumericKeys() {
		if (SSRConfig.getInstance().IS_EMUL) {
			return addNumericKeys && numEnabled;
		} else {
			return numEnabled;
		}
	}

	boolean addPrevKey = false;
	/**
	 * Repository에 등록된 키들의 Event를 받는다. - '이전으로' 키
	*/
	public void reservePrevKey() {
		LOG.print(this, "reservePrevKey ...........");
		if (!prevEnabled) {
			if (SSRConfig.getInstance().IS_EMUL) {
				addPrevKey = EventManager.getInstance().addUserEventListener(prevAdapter, this, prevRep); //exclusive 하게 등록
			} else {
				EventManager.getInstance().addUserEventListener(prevAdapter, this, prevRep); //exclusive 하게 등록
			}
			prevEnabled = true;
		}
	}

	/**
	 * Key Event를 받지 않는다. - '이전으로' 키
	 */
	public void releasePrevKey() {
		LOG.print(this, "releasePrevKey");
		if (prevEnabled) {
			EventManager.getInstance().removeUserEventListener(prevAdapter);
			prevEnabled = false;
			if (SSRConfig.getInstance().IS_EMUL) {
				addPrevKey = false;
			}
		}
	}

	public boolean isReservedPrevKeys() {
		if (SSRConfig.getInstance().IS_EMUL) {
			return addPrevKey && prevEnabled;
		} else {
			return prevEnabled;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.davic.resources.ResourceClient#notifyRelease(org.davic.resources.ResourceProxy)
	 */

	public void notifyRelease(ResourceProxy arg0) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.davic.resources.ResourceClient#release(org.davic.resources.ResourceProxy)
	 */
	/*
	* 다른 어플에서 강제로 뺏어가려고 할때 콜백 되는 메소드
	*/
	public void release(ResourceProxy arg0) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.davic.resources.ResourceClient#requestRelease(org.davic.resources.ResourceProxy,
	 *      java.lang.Object)
	 */

	/* 다른 어플이 exclusive하게 키를 뺏어가려고 하면
	 * 콜백되는 메소드
	 */
	public boolean requestRelease(ResourceProxy arg0, Object arg1) {
		LOG.print("-requestRelease----------------" + arg0);
		return false; //
	}

	//--------------------------------------------------------------------------
	// UserEventListener Implementation
	//--------------------------------------------------------------------------

	public class UserEventAdapter implements UserEventListener {

		public void userEventReceived(UserEvent evt) {
			LOG.print(this, "@@@@@@ userEventReceived() code=" + evt.getCode() + ", type=" + evt.getType());
			LOG.print(this, "@@@@@@ userEventReceived() " + evt.toString());

			int code = evt.getCode();
			switch (evt.getType()) {
			case KeyEvent.KEY_PRESSED:
				switch (code) {
				/*Extended key events*/
				case HRcEvent.VK_0:
				case HRcEvent.VK_1:
				case HRcEvent.VK_2:
				case HRcEvent.VK_3:
				case HRcEvent.VK_4:
				case HRcEvent.VK_5:
				case HRcEvent.VK_6:
				case HRcEvent.VK_7:
				case HRcEvent.VK_8:
				case HRcEvent.VK_9:
					//SceneManager.getInstance().handleKeyEvent(evt);
                    KeyController.getInstance().keyProcess(evt.getCode());
					break;
				case HRcEvent.VK_F1:
					//SceneManager.getInstance().handleKeyEvent(evt);
                    KeyController.getInstance().keyProcess(evt.getCode());
					break;
				default:
					break;
				}
			}
		}
	}
}
