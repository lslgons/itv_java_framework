package com.tcom.network;

import org.json.simple.JSONObject;

public interface SSRResponse {
    void onReceived(JSONObject response);
    void onFailed(int status, String msg);
}
