package com.tcom.ssr.action;

import org.json.simple.JSONArray;

public class SSRAction {
    final int type;
    final JSONArray arguments;
    public SSRAction(int type, JSONArray arguments) {
        this.type=type;
        this.arguments=arguments;
    }

    public int getType() {
        return type;
    }

    public JSONArray getArguments() {
        return arguments;
    }
}
