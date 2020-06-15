package com.tcom.ssr.action;

import com.tcom.network.SSRConnector;
import com.tcom.util.LOG;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.tv.util.TVTimerWentOffEvent;
import javax.tv.util.TVTimerWentOffListener;

public class SSRInterval {
    JSONObject intervalObj;
    int intervalStep=0;
    IntervalTriggerListener listener;
    public interface IntervalTriggerListener {
        void onIntervalTriggered(String intervalID);
    }

    public SSRInterval(JSONObject obj, IntervalTriggerListener _listener) {
        this.intervalObj=obj;
        this.listener=_listener;
    }

    public String getIntervalID() {
        return (String)intervalObj.get("interval_id");
    }

    public int getIntervalPeriod() {
        return ((Long) intervalObj.get("interval_period")).intValue();
    }

    public int getIntervalSeq() {
        return ((Long)intervalObj.get("interval_sequence")).intValue();
    }

    public void setIntervalSeq(int seq) {
        intervalObj.put("interval_sequence", new Long(seq));
    }

    public int getIntervalMax() {
        return ((Long)intervalObj.get("max_sequence")).intValue();
    }

    public boolean isLoop() {
        return ((Boolean)intervalObj.get("loop")).booleanValue();
    }

    public boolean getEnable() {
        return ((Boolean)intervalObj.get("interval_activated")).booleanValue();
    }
    public void setEnable(boolean b) {
        intervalObj.put("interval_activated", new Boolean(b));
    }

    public void setObject(JSONObject intervalObj) {
        this.intervalObj=intervalObj;
    }
    public void timerWentOff() {
        intervalStep+=1;
        //LOG.print("Interval : "+this.intervalID+ ":: "+intervalStep);
        if(intervalStep>=getIntervalPeriod()) {
            intervalStep=0;
            if(getEnable()) {
                LOG.print("Interval Invoke : "+getIntervalID());
                int intervalSeq=getIntervalSeq();
                LOG.print("Interval Sequence : "+intervalSeq);
                intervalSeq+=1;
                if(intervalSeq>=getIntervalMax()) {
                    LOG.print("********** interval Seq to 0");
                    if(isLoop()) setIntervalSeq(0);
                    else setEnable(false);
                } else {
                    setIntervalSeq(intervalSeq);
                }
                this.listener.onIntervalTriggered(getIntervalID());
            } else {
                LOG.print("Interval disabled");
            }
        }

    }
}
