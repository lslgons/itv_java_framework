package com.landman.util;

/**
 * Created by daegon.kim on 2016-12-05.
 */

import com.cj.tvui.Constants;

import java.util.ArrayList;

/**
 * 로그 출력
 * @param o
 * @param p
 */
public class LOG {

    public static final void debug(Object o, Object p) {
        if (Constants.ENABLE_LOG) {
            try {
                String s = o.getClass().getName();
                System.out.println(LOG_PREFIX() + new String(("{" + s.substring(s.lastIndexOf('.') + 1) + "} " + p).getBytes(), "UTF-8"));
            } catch (Exception e) {

            }
        }
    }
    /**
     * 로그 Prefix 문자열 반환
     * @return
     */
    public static String LOG_PREFIX(){
        return "[" + Constants.APP_NAME + "] ";
    }

    /**
     * 로그 출력
     * @param o
     * @param p
     */
    public static final void print(Object o, Object p) {
        if (Constants.ENABLE_LOG) {
            String s = o.getClass().getName();
            print("{" + s.substring(s.lastIndexOf('.') + 1) + "} " + p);
        }
    }

    /**
     * 로그 출력
     * @param str
     */
    public static final void print(String str) {
        if (Constants.ENABLE_LOG) {
            try {
                System.out.println(LOG_PREFIX() + new String(str.getBytes(), "UTF-8"));
            } catch (Exception e) {
            }
        }
    }

    /**
     * 로그 출력
     * @param o
     */
    public static final void print(Object o) {
        if (Constants.ENABLE_LOG) {
            if (o != null) {
                print(o.toString());
            } else {
                print("null");
            }
        }
    }

    /**
     * 로그 출력
     * @param o
     */
    public static final void print(Object[] o) {
        if (Constants.ENABLE_LOG) {
            if (o != null && o.length > 0) {
                for (int i = 0; i < o.length; i++) {
                    print(i + " " + o[i]);
                }
            } else {
                print("null or empty array");
            }
        }
    }

    /**
     * 로그 출력
     * @param e
     */
    public static final void print(Throwable e) {
        System.out.println(LOG_PREFIX() + "ERROR");
        e.printStackTrace();
    }

    /**
     * 로그 출력
     * @param list
     */
    public static final void print(ArrayList list) {
        if (Constants.ENABLE_LOG) {
            print("list size = " + list.size());
            for (int i = 0; i < list.size(); i++) {
                print(i + " = " + list.get(i));
            }
        }
    }

}




