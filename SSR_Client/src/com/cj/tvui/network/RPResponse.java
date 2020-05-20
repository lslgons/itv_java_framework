package com.cj.tvui.network;

import java.util.Map;

public interface RPResponse {
	void onReceived(int code, Map respHeader, Object response);
	void onFailed(int code, Object response);
}
