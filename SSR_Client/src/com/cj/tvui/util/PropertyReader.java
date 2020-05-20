package com.cj.tvui.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * 프로퍼티 Read/Write 유틸리티
 * @author daegon.kim
 * @since 2016-12-02.
 */
public class PropertyReader {
    /**
     * 해당 경로파일을 읽어 프로퍼티 리더객체로 반환
     * @param path
     * @return
     */
    public static PropertyReader readProperty(String path){
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PropertyReader reader = new PropertyReader(props);
        return reader;
    }

    /**
     * 바이트배열로 읽어 프로퍼티 리더객체로 반환
     * @param in
     * @return
     */
    public static PropertyReader readProperty(InputStream in) {
        Properties props = new Properties();
        try {
            props.load(in);
        } catch(IOException e1) {
        	e1.printStackTrace();
        }
        PropertyReader reader = new PropertyReader(props);
        return reader;
    }
    
    

    private Properties props = null;
    private PropertyReader(Properties p) {this.props = p;}
    public String read(final String key) {
        return props.getProperty(key);
    }
    
    public Set readKeys() {
    	return props.keySet();
    	
    }
}
