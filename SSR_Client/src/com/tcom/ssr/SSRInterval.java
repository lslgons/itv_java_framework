package com.tcom.ssr;

import com.tcom.network.SSRConnector;
import com.tcom.util.LOG;

import javax.tv.util.TVTimerWentOffEvent;
import javax.tv.util.TVTimerWentOffListener;

public class SSRInterval {
    String intervalID;
    int intervalPeriod;
    int intervalStep=0;
    boolean isEnabled=false;
    IntervalTriggerListener listener;
    public interface IntervalTriggerListener {
        void onIntervalTriggered(String intervalID);
    }

    public SSRInterval(String intervalID, int intervalPeriod, boolean isEnabled, IntervalTriggerListener _listener) {
        this.intervalID=intervalID;
        this.intervalPeriod=intervalPeriod;
        this.isEnabled=isEnabled;
        this.listener=_listener;
    }

    public String getIntervalID() {
        return intervalID;
    }

    public int getIntervalPeriod() {
        return intervalPeriod;
    }

    public void setEnable(boolean b) {
        this.isEnabled=b;
    }
    public void timerWentOff() {
        intervalStep+=1;
        LOG.print("Interval : "+this.intervalID+ ":: "+intervalStep);
        if(intervalStep>intervalPeriod) {
            intervalStep=0;
            if(isEnabled) {
                LOG.print("Interval Invoke : "+this.intervalID);
                this.listener.onIntervalTriggered(this.intervalID);
            } else {
                LOG.print("Interval disabled");
            }
        }

    }
}
