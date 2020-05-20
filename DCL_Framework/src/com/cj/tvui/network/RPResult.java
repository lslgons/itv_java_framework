package com.cj.tvui.network;

import com.cj.tvui.ui.Scene;
import org.json.simple.JSONObject;

import java.util.Map;

public interface RPResult {
    boolean isReady=false;
    Map prepare(Scene currentScene);
    void onSuccess(Map header, JSONObject data);
    void onFailed(int resultCode, String msg);
    void complete(Scene currentScene);
}
