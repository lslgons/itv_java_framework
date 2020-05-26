package com.tcom.platform.dmc.cjhv;

import com.tcom.platform.controller.KeyCode;
import com.tcom.platform.controller.KeyController;
import com.tcom.util.LOG;
import org.davic.resources.ResourceClient;
import org.davic.resources.ResourceProxy;
import org.dvb.event.EventManager;
import org.dvb.event.UserEvent;
import org.dvb.event.UserEventListener;
import org.dvb.event.UserEventRepository;
import org.havi.ui.event.HRcEvent;
import org.ocap.ui.event.OCRcEvent;

import java.awt.event.KeyEvent;

public class UserKeyManager implements ResourceClient {

    private UserEventRepository numRep;
    private boolean numEnabled = false;
    private UserEventAdapter numAdapter;

    private UserEventRepository arrowRep;
    private boolean arrowEnabled = false;
    private UserEventAdapter arrowAdapter;

    private UserEventRepository trickRep;
    private boolean trickEnabled = false;
    private UserEventAdapter trickAdapter;

    private UserEventRepository prevRep;
    private boolean prevEnabled = false;
    private UserEventAdapter prevAdapter;

    private UserEventRepository hotRep;
    private boolean hotEnabled = false;
    private UserEventAdapter hotAdapter;

    /**
     * Singleton instance.
     */
    private static UserKeyManager instance;

    public static UserKeyManager getInstance() {
        if (instance == null) {
            instance = new UserKeyManager();
        }
        return instance;
    }

    /**
     * Constructor.
     */
    UserKeyManager() {
        this.numAdapter = new UserEventAdapter();
        this.numRep = new UserEventRepository("NumericKeyRepository");
        this.numRep.addAllNumericKeys();

        this.arrowAdapter = new UserEventAdapter();
        this.arrowRep = new UserEventRepository("ArrowKeyRepository");
        this.arrowRep.addAllArrowKeys();
        this.arrowRep.addKey(KeyCode.VK_OK);

        this.trickAdapter = new UserEventAdapter();
        this.trickRep = new UserEventRepository("TrickKeyRepository");
        this.trickRep.addKey(KeyCode.VK_R);
        this.trickRep.addKey(KeyCode.VK_B);
        this.trickRep.addKey(KeyCode.VK_Y);
        this.trickRep.addKey(KeyCode.VK_G);

        this.prevAdapter = new UserEventAdapter();
        this.prevRep = new UserEventRepository("PreviousKeyRepository");
        this.prevRep.addKey(OCRcEvent.VK_LAST); //for 'back' key
        this.prevRep.addKey(OCRcEvent.VK_EXIT); //for 'exit' key

        this.hotAdapter = new UserEventAdapter();
        this.hotRep = new UserEventRepository("HotKeyRepository");
        this.hotRep.addKey(KeyCode.VK_R);
        this.hotRep.addKey(KeyCode.VK_Y);
        this.hotRep.addKey(KeyCode.VK_G);
        this.hotRep.addKey(KeyCode.VK_B);
        LOG.print(this, "create UserKeyManager");
    }


    public void destroy() {
        LOG.print(this, "UserKeyManager destroy");

        if(this.numRep!=null) this.numRep.removeAllNumericKeys();
        if(this.prevRep!=null) this.prevRep.removeKey(OCRcEvent.VK_LAST);
        if(this.hotRep!=null) this.hotRep.removeKey(KeyCode.VK_R);
        if(this.hotRep!=null) this.hotRep.removeKey(KeyCode.VK_Y);
        if(this.hotRep!=null) this.hotRep.removeKey(KeyCode.VK_G);
        if(this.hotRep!=null) this.hotRep.removeKey(KeyCode.VK_B);

        releaseNumericKeys();
        releasePrevKey();
        releaseHotKey();
        releaseArrowKeys();
        releaseTrickKeys();

        this.numRep = null;
        this.numAdapter = null;

        this.prevRep = null;
        this.prevAdapter = null;

        this.hotRep = null;
        this.hotAdapter = null;

        this.arrowRep=null;
        this.arrowAdapter=null;

        this.trickRep=null;
        this.trickAdapter=null;

        instance = null;
    }

    /**
     * Repository에 등록된 키들의 Event를 받는다. - '숫자'키
     */
    public void reserveNumericKeys() {
        if (!this.numEnabled) {
            LOG.debug(this, "reserveNumericKeys() called");
            //EventManager.getInstance().addUserEventListener(numAdapter, numRep); //Shard 하게 등록
            EventManager.getInstance().addUserEventListener(this.numAdapter, this, this.numRep); //Shard 하게 등록
            this.numEnabled = true;
        }
    }

    /**
     * Key Event를 받지 않는다. - '숫자' 키
     */
    public void releaseNumericKeys() {
        if (this.numEnabled) {
            LOG.debug(this, "releaseNumericKeys() called");
            EventManager.getInstance().removeUserEventListener(this.numAdapter);
            this.numEnabled = false;
        }
    }

    public boolean isReservedNumericKeys() {
        return this.numEnabled;
    }

    /**
     * Repository에 등록된 키들의 Event를 받는다. - '숫자'키
     */
    public void reserveArrowKeys() {
        if (!this.arrowEnabled) {
            LOG.debug(this, "reserveArrowKeys() called");
            //EventManager.getInstance().addUserEventListener(numAdapter, numRep); //Shard 하게 등록
            EventManager.getInstance().addUserEventListener(this.arrowAdapter, this, this.arrowRep); //Shard 하게 등록
            this.arrowEnabled = true;
        }
    }

    /**
     * Key Event를 받지 않는다. - '숫자' 키
     */
    public void releaseArrowKeys() {
        if (this.arrowEnabled) {
            LOG.debug(this, "releaseArrowKeys() called");
            EventManager.getInstance().removeUserEventListener(this.arrowAdapter);
            this.arrowEnabled = false;
        }
    }

    public boolean isReservedArrowKeys() {
        return this.arrowEnabled;
    }

    /**
     * Repository에 등록된 키들의 Event를 받는다. - '숫자'키
     */
    public void reserveTrickKeys() {
        if (!this.trickEnabled) {
            LOG.debug(this, "reserveTrickKeys() called");
            //EventManager.getInstance().addUserEventListener(numAdapter, numRep); //Shard 하게 등록
            EventManager.getInstance().addUserEventListener(this.trickAdapter, this, this.trickRep); //Shard 하게 등록
            this.trickEnabled = true;
        }
    }

    /**
     * Key Event를 받지 않는다. - '숫자' 키
     */
    public void releaseTrickKeys() {
        if (this.trickEnabled) {
            LOG.debug(this, "releaseTrickKeys() called");
            EventManager.getInstance().removeUserEventListener(this.trickAdapter);
            this.trickEnabled = false;
        }
    }

    public boolean isReservedTrickKeys() {
        return this.trickEnabled;
    }

    /**
     * Repository에 등록된 키들의 Event를 받는다. - '이전으로' 키
     */
    public void reservePrevKey() {
        LOG.print(this, "reservePrevKey ...........");
        if (!this.prevEnabled) {
            EventManager.getInstance().addUserEventListener(this.prevAdapter, this, this.prevRep); //exclusive 하게 등록
            this.prevEnabled = true;
        }
    }

    /**
     * Key Event를 받지 않는다. - '이전으로' 키
     */
    public void releasePrevKey() {
        LOG.print(this, "releasePrevKey");
        if (this.prevEnabled) {
            EventManager.getInstance().removeUserEventListener(this.prevAdapter);
            this.prevEnabled = false;
        }
    }

    public boolean isReservedPrevKeys() {
        return this.prevEnabled;
    }

    public void reserveHotKey() {
        LOG.print(this, "reserveHotKey ...........");
        if (!this.hotEnabled) {
            EventManager.getInstance().addUserEventListener(this.hotAdapter, this, this.hotRep); //exclusive 하게 등록
            this.hotEnabled = true;
        }
    }

    public void releaseHotKey() {
        LOG.print(this, "releaseHotKey");
        if (this.hotEnabled) {
            EventManager.getInstance().removeUserEventListener(this.hotAdapter);
            this.hotEnabled = false;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.davic.resources.ResourceClient#notifyRelease(org.davic.resources.ResourceProxy)
     */

    public void notifyRelease(ResourceProxy arg0) {}

    /*
     * (non-Javadoc)
     *
     * @see org.davic.resources.ResourceClient#release(org.davic.resources.ResourceProxy)
     */
    /*
     * 다른 어플에서 강제로 뺏어가려고 할때 콜백 되는 메소드
     */
    public void release(ResourceProxy arg0) {}

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
                case KeyEvent.KEY_PRESSED: //401
                    //case KeyEvent.KEY_RELEASED:
                    //case KeyEvent.KEY_TYPED:
                    KeyController.getInstance().keyProcess(evt.getCode());
//                    switch (code) {
//                        /*Extended key events*/
//                        case HRcEvent.VK_0:
//                        case HRcEvent.VK_1:
//                        case HRcEvent.VK_2:
//                        case HRcEvent.VK_3:
//                        case HRcEvent.VK_4:
//                        case HRcEvent.VK_5:
//                        case HRcEvent.VK_6:
//                        case HRcEvent.VK_7:
//                        case HRcEvent.VK_8:
//                        case HRcEvent.VK_9:
//
//                        case OCRcEvent.VK_LAST:
//                        case OCRcEvent.VK_EXIT:
//
//                        case KeyCode.VK_R:
//                        case KeyCode.VK_Y:
//                        case KeyCode.VK_G:
//                        case KeyCode.VK_B:
//
//                        default:
//                            break;
//                    }
            }
        }
    }
}
