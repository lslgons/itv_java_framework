package com.landman.util;

import com.cj.tvui.Constants;
import com.cj.tvui.controller.StbController;
import com.cj.tvui.dmc.interfaces.AVInterface;
import com.cj.tvui.dmc.interfaces.DisplayInterface;
import com.cj.tvui.dmc.interfaces.KeymapInterface;
import com.cj.tvui.dmc.interfaces.StbInterface;
import org.dvb.lang.DVBClassLoader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
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
            if(Constants.IS_EMUL) {
                obj = RemoteClassLoader.loadClass("com.cj.tvui.dmc.defaults."+target).newInstance();
            } else {
                obj = RemoteClassLoader.loadClass("com.cj.tvui.dmc."+Constants.DMC_NAME.toLowerCase()+"."+target).newInstance();
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
        LOG.print("load package : "+packagePath);
        LOG.print(klasses);
        if(klasses == null) klasses = new HashMap();
        if (Constants.USE_CLASS_CACHE) {
            k = (Class) klasses.get(packagePath);
        }
        if (k == null) {
            LOG.print("Class cache miss!!!");
            //load class from file
            try {
                Class cls = Class.forName(packagePath);
                if(Constants.USE_CLASS_CACHE) {
                    klasses.put(packagePath, cls);
                    LOG.print("######### Loaded Class ########## : "+ klasses.size());
                }

                return cls;
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
                System.out.println("Class not found in local...");
            }
            Class ucls = _loadClassFromUrl(packagePath);
            if(Constants.USE_CLASS_CACHE && ucls != null) {
                klasses.put(packagePath, ucls);
                LOG.print("######### Loaded Class ########## : "+ klasses.size());
            }


            return ucls;
        } else {
            LOG.print("Class cache hit!!");
            return k;
        }
    }

    public static void flushClassCache() {
        if(klasses!=null) {
            klasses.clear();
            klasses=null;
        }

    }

    private static Class _loadClassFromUrl(final String packagePath) {
        try {

            LOG.print("**************** Load class from URL");
            String u = Constants.DCL_HOST;
            URL[] url = {new URL(u)};
            if(!Constants.IS_EMUL) {
                DVBClassLoader urlClassLoader = DVBClassLoader.newInstance(url);
                Class cls = urlClassLoader.loadClass(packagePath);
                return cls;
            } else {
                URLClassLoader urlClassLoader = new URLClassLoader(url);
                Class cls = urlClassLoader.loadClass(packagePath);
                return cls;
            }
        } catch (MalformedURLException e1) {
            //e.printStackTrace();
            LOG.print("Class load malformedURLException");
            StbController.ClassLoadErrorListener listener = StbController.getInstance().getClassLoadErrorListener();
            if(listener !=null) {
                listener.onLoadError("MalformedURLException");
            }
        } catch(ClassNotFoundException e2) {
            //e.printStackTrace();
            LOG.print("Class load ClassNotFoundException");
            StbController.ClassLoadErrorListener listener = StbController.getInstance().getClassLoadErrorListener();
            if(listener !=null) {
                listener.onLoadError("ClassNotFoundException");
            }
        } catch(Exception e3) {
            LOG.print("Class load Unknown Exception");
            StbController.ClassLoadErrorListener listener = StbController.getInstance().getClassLoadErrorListener();
            if(listener !=null) {
                listener.onLoadError("UnknownException");
            }
        }
        return null;
    }


}
