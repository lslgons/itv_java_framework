/*
 * Main.java
 *
 * Created on 2005년 5월 11일 (수), 오후 1:21
 */

/**
 *
 * @author gyuh
 */

import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;
import javax.tv.xlet.Xlet;
import javax.tv.xlet.XletContext;

import org.havi.ui.ScreenProxy;

import com.cj.tvui.Constants;
import com.cj.tvui.util.PropertyReader;

import de.twonkyvision.havi.CUContext;
import de.twonkyvision.havi.CUSecurityManager;
import de.twonkyvision.havi.Display;
import de.twonkyvision.havi.DisplayFrame;
import de.twonkyvision.havi.DisplayProxy;
import de.twonkyvision.havi.InfoSecurityManager;
import de.twonkyvision.havi.JarClassLoader;

public class Main
    implements DisplayProxy, XletContext
{
    private static class XletStarter
        implements Runnable
    {

        public void run()
        {
            try
            {
                boolean flag = !initialized;
//                Trace._log_out_low("1");
                if(flag)
                {
//                    Trace._log_out_low("2");
                    Main.info("Initializing " + Main.cname);
                    Main.xlet.initXlet(ctx);
                    initialized = true;
                    Thread.sleep(100L);
                }
//                Trace._log_out_low("3");
                Main.info("Starting " + Main.cname);
//                Trace._log_out_low("4");
                Main.xlet.startXlet();
//                Trace._log_out_low("5");
                Main.info(Main.cname + " started");
                if(flag && Main.waitForInput)
                {
                    Main.info("Press <ENTER> to terminate Xlet");
                    return;
                }
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
                Main.exit("Failed to start Xlet - abort", 7);
            }
        }

        private boolean initialized;
        private XletContext ctx;

        XletStarter(XletContext xletcontext)
        {
            ctx = xletcontext;
        }
    }

    private static class CloseListener extends WindowAdapter
        implements Runnable
    {

        void terminate()
        {
            if(!terminating)
            {
                terminating = true;
                (new Thread(Main.xletGroup, this, "Xlet-Shutdown")).start();
            }
        }

        public void windowClosed(WindowEvent windowevent)
        {
            terminate();
        }

        public void run()
        {
            try
            {
                Main.info("Destroying " + Main.cname);
                Main.xlet.destroyXlet(true);
                Thread.sleep(2000L);
            }
            catch(Throwable throwable)
            {
                Main.error("destroyXlet failed: ");
                throwable.printStackTrace();
            }
            System.exit(0);
        }

        private boolean terminating;

        CloseListener()
        {
        }
    }


    public Main()
    {
    }

    public static void main(String args[])
    {
label0:
        {
    		//OcDownLoader.loadDa();
    	
    		//OCDownloader Property
//	    	InputStream configInput = new Object().getClass().getResourceAsStream("/config.properties");
//	    	if(configInput == null) {
//	    		BufferedReader br;
//				try {
//					br = new BufferedReader(new InputStreamReader(new Object().getClass().getResourceAsStream("/host.conf"), "UTF-8"));
//					String host = br.readLine();
//					System.out.println("host : "+host);
//					URL url = new URL(host+"config.properties");
//					configInput = url.openStream();
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//	    	}
//	    	PropertyReader properties = PropertyReader.readProperty(configInput);
//            PropertyReader properties = PropertyReader.readProperty("config.properties");
//	        String host=properties.read("META_OC_DOWNLOAD_URL");
//	        String dest=properties.read("META_OC_DOWNLOAD_DEST_DIR");
//	        String files = properties.read("META_OC_DOWNLOAD_FILES");
//	        if(host != null && dest != null) {
//                OcDownLoader.loadDaFile(host, dest, files.trim().split(","));
//            }


            System.out.println("Xlet Launcher 1.0 build 1 - 2002-02-08\n(c) 2001 by TwonkyVision GmbH - http://www.twonkyvision.de/");
            cname = "Xlet";
//            Trace._log_out_low("");
            args = new String[1];

//          

            //args[0] = properties.getProperty("initialclass");
			//args[0] = "cjtosplus.app.App";
            args[0] = "com.cj.tvui.MainXlet";
            //args[0] = "com.cjtmall.MainXlet";
            //properties = null;

            String s = null;
            try
            {
                int i = 0;
                for(int j = args.length; i < j; i++)
                {
                    String s1 = args[i];
                    System.out.println("args[1] :" + s1);
                    if(s1.equals("-s"))
                    {
                        useSM = false;
                        continue;
                    }
                    if(s1.equals("+s"))
                    {
                        useSM = true;
                        continue;
                    }
                    if(s1.equals("-jar"))
                    {
                        if(++i == j)
                        {
                            System.out.println("  Argument: [-s|+s] [<fully qualified class>] [[-jar ]<jar>]\n  Main starts an Xlet by loading and executing a class that\n  implements the interface javax.tv.xlet.Xlet.\n  Instead of a class a jar-archive can be specified. If it does not\n  follow option '-jar', then it must have the extension .jar.\n  If it is not accessible in the file system, then the access happens\n  via URL.\n  The archive must either contain a class Xlet or a fully qualified\n  class has to be specified additionally.\n  On Java 1.1 is a security manager installed, what can be inhibited\n  with option '-s'. On Java 2 it is not installed, but can be forced\n  with option '+s'. In the latter case is a java.security.policy file\n  required. If the Xlet is not inside an archive, then the security\n  manager is deactivated in any case.\n  Set system property havi.display=<classname> to override the default\n  display handler.\n  Note: HTML embedded Xlets are not supported.");
                            exit("Option -jar not followed by archive", 1);
                        }
                        s = args[i];
                        continue;
                    }
                    if(s1.toLowerCase().endsWith(".jar"))
                    {
                        if(s == null)
                        {
                            s = s1;
                        } else
                        {
                            System.out.println("  Argument: [-s|+s] [<fully qualified class>] [[-jar ]<jar>]\n  Main starts an Xlet by loading and executing a class that\n  implements the interface javax.tv.xlet.Xlet.\n  Instead of a class a jar-archive can be specified. If it does not\n  follow option '-jar', then it must have the extension .jar.\n  If it is not accessible in the file system, then the access happens\n  via URL.\n  The archive must either contain a class Xlet or a fully qualified\n  class has to be specified additionally.\n  On Java 1.1 is a security manager installed, what can be inhibited\n  with option '-s'. On Java 2 it is not installed, but can be forced\n  with option '+s'. In the latter case is a java.security.policy file\n  required. If the Xlet is not inside an archive, then the security\n  manager is deactivated in any case.\n  Set system property havi.display=<classname> to override the default\n  display handler.\n  Note: HTML embedded Xlets are not supported.");
                            exit("Only one archive supported", 2);
                        }
                        continue;
                    }
                    if(cname == "Xlet")
                    {
                    	System.out.println("Cname2 : " + cname);
                        cname = s1;
                    } else
                    {
                    	System.out.println("Cname3 : " + cname);
                    	System.out.println("  Argument: [-s|+s] [<fully qualified class>] [[-jar ]<jar>]\n  Main starts an Xlet by loading and executing a class that\n  implements the interface javax.tv.xlet.Xlet.\n  Instead of a class a jar-archive can be specified. If it does not\n  follow option '-jar', then it must have the extension .jar.\n  If it is not accessible in the file system, then the access happens\n  via URL.\n  The archive must either contain a class Xlet or a fully qualified\n  class has to be specified additionally.\n  On Java 1.1 is a security manager installed, what can be inhibited\n  with option '-s'. On Java 2 it is not installed, but can be forced\n  with option '+s'. In the latter case is a java.security.policy file\n  required. If the Xlet is not inside an archive, then the security\n  manager is deactivated in any case.\n  Set system property havi.display=<classname> to override the default\n  display handler.\n  Note: HTML embedded Xlets are not supported.");
                        exit("Unrecognized parameter: " + s1, 3);
                    }
                }

                Class class1;
                if(s != null)
                {
                    Object obj;
                    try
                    {
                        obj = new FileInputStream(s);
                    }
                    catch(FileNotFoundException filenotfoundexception)
                    {
                        try
                        {
                            obj = (new URL(s)).openStream();
                        }
                        catch(Exception exception)
                        {
                            throw filenotfoundexception;
                        }

                    }
                    classloader = new JarClassLoader((InputStream)obj);
                    ((InputStream)obj).close();
                    class1 = classloader.loadClass(cname);
                    if(class1 == null)
                        exit(cname + " not found in " + s, 4);
                    CUContext.createContext(InfoSecurityManager.getLoader(), Thread.currentThread().getThreadGroup());
                } else
                {
                    class1 = Class.forName(cname);
                    useSM = false;
                }
                Main Main = new Main();
                ScreenProxy.setDisplayProxy(Main);
                Object obj1 = class1.newInstance();
                if(!(obj1 instanceof Xlet))
                    exit("Class does not implement javax.tv.xlet.Xlet.", 5);
                info("Security Manager " + (useSM ? "enabled" : "disabled"));
                if(useSM)
                {
                    Toolkit.getDefaultToolkit();
                    System.setSecurityManager(new CUSecurityManager());
                }
                xletGroup = new ThreadGroup("Xlet");
                xletGroup.setMaxPriority(4);
                CUContext.createContext(classloader, xletGroup);
                xlet = (Xlet)obj1;
                xletStarter = new XletStarter(Main);
                closeListener = new CloseListener();
                (new Thread(xletGroup, xletStarter, "Xlet-Start")).start();
                //Check Memory Usage
                //MemoryUsageAnalyzer.getInstance().runMemoryUsage();
                
                
                /*
                try
                {
                    int k = System.in.read();
                    waitForInput = false;
                    if(k >= 0)
                    {
                        closeListener.terminate();
                        return;
                    }
                }
                catch(Exception exception1)
                {
                    waitForInput = false;
                    return;
                }
                */
                break label0;
            }
            catch(Throwable throwable)
            {
                if(cname == "Xlet" && s == null)
                	System.out.println("  Argument: [-s|+s] [<fully qualified class>] [[-jar ]<jar>]\n  Main starts an Xlet by loading and executing a class that\n  implements the interface javax.tv.xlet.Xlet.\n  Instead of a class a jar-archive can be specified. If it does not\n  follow option '-jar', then it must have the extension .jar.\n  If it is not accessible in the file system, then the access happens\n  via URL.\n  The archive must either contain a class Xlet or a fully qualified\n  class has to be specified additionally.\n  On Java 1.1 is a security manager installed, what can be inhibited\n  with option '-s'. On Java 2 it is not installed, but can be forced\n  with option '+s'. In the latter case is a java.security.policy file\n  required. If the Xlet is not inside an archive, then the security\n  manager is deactivated in any case.\n  Set system property havi.display=<classname> to override the default\n  display handler.\n  Note: HTML embedded Xlets are not supported.");
                throwable.printStackTrace();
                exit("Unrecoverable error - aborting", 6);
            }
        }
    }

    public void notifyDestroyed()
    {
        info(cname + " has uninstalled itself - exiting");
      //Check Memory Usage
        //MemoryUsageAnalyzer.getInstance().stopMemoryUsage();
        System.exit(0);
    }

    public void notifyPaused()
    {
        info("Xlet paused");
    }

    public Object getXletProperty(String s)
    {
        info("Property request for key: " + s);
        return System.getProperty(s);
        /**
        SecurityException securityexception;
        securityexception;
        return null;
         */
    }

    public void resumeRequest()
    {
        info("Resume request - restarting " + cname);
        (new Thread(xletGroup, xletStarter, "Xlet-Resume")).start();
    }

    public Display getDisplay()
    {
        Object obj = null;
        String s = System.getProperty("havi.display");
        if(s != null)
            try
            {
                obj = Class.forName(s).newInstance();
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        if(obj == null)
            obj = new DisplayFrame();
        if(obj != null && (obj instanceof Window))
            ((Window)obj).addWindowListener(closeListener);
        return (Display)obj;
    }

    private static void exit(String s, int i)
    {
        error(s);
        System.exit(i);
    }

    private static void error(String s)
    {
        System.err.println("ERROR: Main: " + s);
    }

    private static void info(String s)
    {
    	System.out.println("Main: " + s);
    }

    private static boolean useSM;
    private static ThreadGroup xletGroup;
    private static ClassLoader classloader;
    private static String cname;
    private static Xlet xlet;
    private static XletStarter xletStarter;
    private static CloseListener closeListener;
    private static boolean waitForInput = true;

    static
    {
        try
        {
            Class.forName("java.security.Permission");
        }
        catch(Throwable throwable)
        {
            useSM = true;
        }
    }

}
