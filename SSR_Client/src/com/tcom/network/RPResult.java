package com.tcom.network;

import org.json.simple.JSONObject;

import java.util.Map;

public interface RPResult {
    boolean isReady=false;
    void onSuccess(Map header, JSONObject data);
    void onFailed(int resultCode, String msg);
}
