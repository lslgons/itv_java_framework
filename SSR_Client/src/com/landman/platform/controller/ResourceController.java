package com.landman.platform.controller;

import com.cj.tvui.util.LOG;
import com.cj.tvui.util.PropertyReader;
import org.json.simple.JSONObject;

import javax.tv.xlet.XletContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Resource Controller
 * @deprecated
 * @author daegon.kim
 *
 */

public class ResourceController {
    private static ResourceController instance = null;
    public static ResourceController getInstance() {
        if(instance == null) instance = new ResourceController();
        return instance;
    }
    
    
    
    private Map stringPool;
    private XletContext xletCtx;

    private ResourceController(){
        LOG.print("Init ResourceController");
    }
    
    public XletContext getXletContext() {
    	return xletCtx;
    }
    
    public void setXletContext(XletContext xletCtx) {this.xletCtx = xletCtx;}
    
    
    
    /**
     * Property타입 스트링번들 적재
     * @param bundlePath
     */
    public void setStringBundle(String bundlePath) {
    	PropertyReader properties = PropertyReader.readProperty(bundlePath);
    	Set keys = properties.readKeys();
    	
    	stringPool = null;
    	stringPool = new HashMap();
    	
    	Iterator it = keys.iterator();
    	while(it.hasNext()) {
    		String key = (String) it.next();
    		stringPool.put(key, properties.read(key));
    	}
    }
    public void setStringBundle(JSONObject json) {
    	//TODO
    }
    
    /**
     * 적재된 번들 내 특정 키의 스트링 가져오기
     * @param key
     * @return
     */
    public String readString(final String key) {
    	if(stringPool == null) {
    		LOG.print(this, "String Bundle is not set.");
    		return null;
    	}
    	return (String) stringPool.get(key);
    }
    
}
