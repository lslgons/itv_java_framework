package com.tcom.util;


import com.tcom.platform.controller.StbController;
import com.tcom.platform.dmc.interfaces.AVInterface;
import com.tcom.platform.dmc.interfaces.DisplayInterface;
import com.tcom.platform.dmc.interfaces.KeymapInterface;
import com.tcom.platform.dmc.interfaces.StbInterface;
import com.tcom.ssr.SSRConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * 클래스를 동적으로 호출하기 위한 모듈, 한번 불린 클래스형은 Cache로 저장되어 다시 호출할 경우 재사용 됨
 *
 * @author daegon.kim
 * @since 2016-11-24
 */
public final class RemoteClassLoader {

    private static HashMap klasses=new HashMap();

    public static AVInterface loadAVInterface() {
    	return (AVInterface) _loadIntfFromString("AV");
    }

    public static DisplayInterface loadDisplayInterface() {
        return (DisplayInterface)_loadIntfFromString("Display");
    }
    public static KeymapInterface loadKeymapInterface() {
        return (KeymapInterface) _loadIntfFromString("Keymap");
    }
    public static StbInterface loadStbInterface() {

        return (StbInterface)_loadIntfFromString("Stb");
    }

    private static Object _loadIntfFromString(final String target) {
        Object obj = null;
        try {
            if(SSRConfig.getInstance().IS_EMUL) {
                obj = RemoteClassLoader.loadClass("com.tcom.platform.dmc.pc."+target).newInstance();
            } else {
                obj = RemoteClassLoader.loadClass("com.tcom.platform.dmc."+SSRConfig.getInstance().DMC_NAME.toLowerCase()+"."+target).newInstance();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Object newInstance(final String packagePath, Class[]argsClass, Object[]argsObject) {

        Object obj = null;
        if (argsClass == null) {
            try {
                obj=loadClass(packagePath).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Constructor ctor = loadClass(packagePath).getDeclaredConstructor(argsClass);
                obj = ctor.newInstance(argsObject);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                LOG.print("Class load NoSuchMethodException");
                StbController.ClassLoadErrorListener listener = StbController.getInstance().getClassLoadErrorListener();
                if(listener !=null) {
                    listener.onLoadError("NoSuchMethodException");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                LOG.print("Class load IllegalAccessException");
                StbController.ClassLoadErrorListener listener = StbController.getInstance().getClassLoadErrorListener();
                if(listener !=null) {
                    listener.onLoadError("IllegalAccessException");
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
                LOG.print("Class load InstantiationException");
                StbController.ClassLoadErrorListener listener = StbController.getInstance().getClassLoadErrorListener();
                if(listener !=null) {
                    listener.onLoadError("InstantiationException");
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                LOG.print("Class load InvocationTargetException");
                StbController.ClassLoadErrorListener listener = StbController.getInstance().getClassLoadErrorListener();
                if(listener !=null) {
                    listener.onLoadError("InvocationTargetException");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                LOG.print("Class load NullPointerException");
                StbController.ClassLoadErrorListener listener = StbController.getInstance().getClassLoadErrorListener();
                if(listener !=null) {
                    listener.onLoadError("NullPointerException");
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOG.print("Class load UnknownException");
                StbController.ClassLoadErrorListener listener = StbController.getInstance().getClassLoadErrorListener();
                if(listener !=null) {
                    listener.onLoadError("UnknownException");
                }
            }
        }

        return obj;
    }


    public static Class loadClass(final String packagePath) {
        Class k = null;
        LOG.print("load Class : "+packagePath);
        //LOG.print(klasses);
        try {
            k = Class.forName(packagePath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        if(klasses == null) klasses = new HashMap();
//
//        k = (Class) klasses.get(packagePath);
//        if (k == null) {
//            try {
//                Class cls = Class.forName(packagePath);
//                klasses.put(packagePath, cls);
//                LOG.print("######### Loaded Class ########## : "+ klasses.size());
//                return cls;
//            } catch (ClassNotFoundException e) {
//                //e.printStackTrace();
//                System.out.println("Class not found in local...");
//
//            }
//            return null;
//        } else return k;
        return k;



    }

    public static void flushClassCache() {
        if(klasses!=null) {
            klasses.clear();
            klasses=null;
        }

    }

//    private static Class _loadClassFromUrl(final String packagePath) {
//        try {
//
//            LOG.print("**************** Load class from URL");
//            String u = Constants.DCL_HOST;
//            URL[] url = {new URL(u)};
//            if(!Constants.IS_EMUL) {
//                DVBClassLoader urlClassLoader = DVBClassLoader.newInstance(url);
//                Class cls = urlClassLoader.loadClass(packagePath);
//                return cls;
//            } else {
//                URLClassLoader urlClassLoader = new URLClassLoader(url);
//                Class cls = urlClassLoader.loadClass(packagePath);
//                return cls;
//            }
//        } catch (MalformedURLException e1) {
//            //e.printStackTrace();
//            LOG.print("Class load malformedURLException");
//            StbController.ClassLoadErrorListener listener = StbController.getInstance().getClassLoadErrorListener();
//            if(listener !=null) {
//                listener.onLoadError("MalformedURLException");
//            }
//        } catch(ClassNotFoundException e2) {
//            //e.printStackTrace();
//            LOG.print("Class load ClassNotFoundException");
//            StbController.ClassLoadErrorListener listener = StbController.getInstance().getClassLoadErrorListener();
//            if(listener !=null) {
//                listener.onLoadError("ClassNotFoundException");
//            }
//        } catch(Exception e3) {
//            LOG.print("Class load Unknown Exception");
//            StbController.ClassLoadErrorListener listener = StbController.getInstance().getClassLoadErrorListener();
//            if(listener !=null) {
//                listener.onLoadError("UnknownException");
//            }
//        }
//        return null;
//    }


}
