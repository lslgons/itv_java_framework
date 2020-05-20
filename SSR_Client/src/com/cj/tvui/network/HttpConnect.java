package com.cj.tvui.network;


import java.io.*;
import java.net.*;
import java.util.Vector;

import com.cj.tvui.util.LOG;

/**
 * Http 통신 클래스
 */
public class HttpConnect {
	public interface HttpResponse {
		void onSuccess(int code, String resp);
		void onFail(int code, String resp);
	}
    
    public synchronized static boolean isSrvCheck(String urlPath, String port, String fileName) {
    	boolean result = false;
    	try {
            URL url = null;
            if (urlPath == null) {
                url = new URL("http://10.119.160.232/http_test/sample.txt");
            } else {
            	if(fileName.startsWith("/")) {
            		url = new URL("http://" + urlPath + ":" + port + fileName);
            	} else {
            		url = new URL("http://" + urlPath + ":" + port + "/" + fileName);
            	}
                
                System.out.println(urlPath.trim() + "urlPath.Trim() +20min..."+"["+url+"]");
            } 

            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
			if (conn.getResponseCode() == 200 || conn.getResponseCode() == 402) {
				System.out.println("conn.getResponseCode()  value is : ["+conn.getResponseCode()+"]"); 
				result=true;
			} else {
				System.out.println("conn.getResponseCode()  value is : ["+conn.getResponseCode()+"]"); 
				result=false;
			}
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    	return result;
    }
    
    public synchronized static String[] respDataHttp(String urlPath, String port, String fileName, HttpResponse delegator) {
//    	HttpConnect.respDataHttp("10.119.160.232","80","/http_test/sample.txt","|");
    	Vector returnValue = new Vector();
        String[] result = null;
        try {
            URL url = null;
//            url = new URL("http://" + Reference.URL_MAIN + ":" + Reference.PORT + urlPath);
            if (urlPath == null) {
                url = new URL("http://10.119.160.232/http_test/sample.txt");
            } else {
            	if(fileName.startsWith("/")) {
            		url = new URL("http://" + urlPath + ":" + port + fileName);
            	} else {
            		url = new URL("http://" + urlPath + ":" + port +"/"+ fileName);
            	}
                
                //System.out.println(urlPath.trim() + "urlPath.Trim() +20min..."+"["+url+"]");
                LOG.print(url.toString());
            }
            
            if(!isSrvCheck(urlPath, port, fileName)) { // 서버 체크.
            	System.out.println("isSrvCheck bad...");
            	return null;
            }else {
            	System.out.println("isSrvCheck Good...");
            }

            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
                  
            String param = URLEncoder.encode("mac_num") + "=" + URLEncoder.encode("12301293");   //  4958
//            param += "&" + URLEncoder.encode("page_num") + "=" + URLEncoder.encode(pageNum/*"00000131671"*/);   //  00000131671

            OutputStreamWriter osw = null;
            osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(param);
            osw.flush();

            String tempString;
            BufferedReader bufferedreader = null;
            bufferedreader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//             bufferedreader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "8859_1"));
//          bufferedreader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "KSC5601"));

            String tempRead;
            String storeString;
            StringBuffer stringbuffer = new StringBuffer();

            while ((tempRead = bufferedreader.readLine()) != null) {
                stringbuffer.append(tempRead);
            }
            tempString = stringbuffer.toString();
// Split
//            StringTokenizer token = new StringTokenizer(tempString, delim);
//            while (token.hasMoreTokens()) {
//                storeString = token.nextToken();
//                returnValue.addElement(storeString);
////                System.out.println("PostRequestEventList..First.storeString==================================" + storeString);         
//            }
            
//End of Splilt
            delegator.onSuccess(200, tempString);
            bufferedreader.close();

            result = new String[returnValue.size()];
            for (int i = 0; i < returnValue.size(); i++) {
                result[i] = (String) returnValue.get(i);
                System.out.println(i + " number (" + result[i] + ") value is... 20min Test sample.txt  Info value...");
            }
            returnValue.removeAllElements();

        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }
    
    public synchronized static HttpURLConnection Connect(String url) {
        URL RemoteURL = null;
        HttpURLConnection HttpConnection = null;
        try {
            RemoteURL = new URL(url);
            HttpConnection = (HttpURLConnection) RemoteURL.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
            HttpConnection = null;
        }
        RemoteURL = null;

        return HttpConnection;
    }
    
    public synchronized static byte[] ImgRequest(String url) {
        HttpURLConnection HttpConnection = Connect(url);
        if (HttpConnection == null)
            return null;

        byte[] Msg = null;
        InputStream InStream = null;
        BufferedInputStream BuffStream = null;
        ByteArrayOutputStream ByteStream = null;
        try {
            InStream = HttpConnection.getInputStream();
            BuffStream = new BufferedInputStream(InStream);

            byte[] Buffer = new byte[1024];
            ByteStream = new ByteArrayOutputStream(1024);

            int ImgByteLenght = -1;
            while ((ImgByteLenght = BuffStream.read(Buffer)) != -1)
                ByteStream.write(Buffer, 0, ImgByteLenght);

            Msg = ByteStream.toByteArray();

            ByteStream.close();
            InStream.close();
            BuffStream.close();
            HttpConnection.disconnect();

            ByteStream = null;
            Buffer = null;
            InStream = null;
            BuffStream = null;
            HttpConnection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("Http...................." + tempcnt++);
//        System.out.println("Image byte value [" + Msg.length +"]" +"--------------------------");
        return Msg;
    }
      
}
