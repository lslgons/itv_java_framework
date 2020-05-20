package com.cj.tvui.network;

import com.cj.tvui.controller.SceneController;
import com.cj.tvui.util.LOG;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * RP통신 모듈
 * @author daegon.kim
 *
 */

public class RPConnector2 {
	

	public final class RP_METHOD {
		public static final String RP_GET = "GET";
		public static final String RP_POST = "POST";
		public static final String RP_PUT = "PUT";
		public static final String RP_DELETE = "DELETE";
	};
	
	
	
	private static class RPRequestThread extends Thread {
		Object mutex = new Object();
		URL url;
		String payload;
		String method;
		RPResponse res;
		boolean isCompleted = false;
		
		public RPRequestThread(URL url, String payload, String method, RPResponse res) {
			this.url = url;
			this.payload = payload;
			this.res = res;
			this.method = method;
		}
		
		public boolean isCompleted() {
			synchronized(mutex) {
				return this.isCompleted;
			}
			
		}
		
		public void run() {
			LOG.print(this, "Network Thread Start");
			Vector returnValue = new Vector();
			String[] result = null;
			HttpURLConnection conn;
			try {
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				//2018-10-10, Request Timeout 설정
				conn.setRequestMethod(method);
				if(staticHeader == null) {
					staticHeader = new HashMap();
					//set Default Header
					staticHeader.put("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
				}
				
				//Set Header to Connector
				Iterator it = staticHeader.keySet().iterator();
				while(it.hasNext()) {
					String headerKey = (String)it.next();
					String headerValue = (String)staticHeader.get(headerKey);
					conn.setRequestProperty(headerKey, headerValue);
				}
				
				OutputStreamWriter osw = null;
				try {
					osw = new OutputStreamWriter(conn.getOutputStream());
				} catch(ConnectException e) {
					res.onFailed(999, "Connection Refused");
					synchronized(mutex) {
						isCompleted = true;
					}
					
					return;
				}
	            
	            osw.write(payload);
	            osw.flush();
	            
	            int resCode = conn.getResponseCode();

	            //daegon.kim, 20180927 - Response 헤더정보 가져오기
                Map respHeader = conn.getHeaderFields();
                Set entrySet = respHeader.entrySet();
                Iterator entrySetIterator = entrySet.iterator();
                Map returnMap = new HashMap();
                while(entrySetIterator.hasNext()) {
                    Map.Entry entry= (Map.Entry) entrySetIterator.next();
                    returnMap.put(entry.getKey(), entry.getValue());
                }
                //End of code

	            if (resCode != HttpURLConnection.HTTP_OK) {
	            	res.onFailed(resCode, "Unexpected Error");
	            	synchronized(mutex) {
						isCompleted = true;
					}
	            	return;
	            }
	            
	            String tempString;
	            BufferedReader bufferedreader = null;
	            
	            try {
	            	bufferedreader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	            } catch(FileNotFoundException e) {
	            	res.onFailed(conn.getResponseCode(), "File not found");
	            	returnValue.removeAllElements();
	            	synchronized(mutex) {
						isCompleted = true;
					}
	            	return;
	            }
	            
	            String tempRead;
	            StringBuffer stringbuffer = new StringBuffer();

	            while ((tempRead = bufferedreader.readLine()) != null) {
	                stringbuffer.append(tempRead);
	            }
	            tempString = stringbuffer.toString();
	            res.onReceived(200, returnMap, tempString);
	            
	            bufferedreader.close();
	            returnValue.removeAllElements();

	            
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			synchronized(mutex) {
				isCompleted = true;
			}
			

		}
	}
	
	
	/* Thread Pool */
	
	private static class ThreadQueue extends Thread {
		public Object mutex = new Object();
		private final int MAX_THREAD = 4;
		private RPRequestThread[] innerThread = new RPRequestThread[MAX_THREAD];
		private boolean stopFlag = false;
		private boolean isCompleted = false;
		LinkedList queue = new LinkedList();
		
		public void run() {
			//super.run();
			while(true) {
				synchronized(mutex) {
					if(stopFlag) break;
				}
//				LOG.print(this, "Activated Thread : " + Thread.activeCount());
//				LOG.print(this, "Thread Pool : " + queue.size());
				for(int i=0;i<MAX_THREAD;++i) {
					if(queue.size() > 0) {
						if(innerThread[i] != null && innerThread[i].isCompleted) {
							LOG.print(this, "exists completed thread, destroy it.");
							innerThread[i].interrupt();
							innerThread[i] = null;
							LOG.print(this, "Create new RP Request Thread");
							RpInfo info = (RpInfo) queue.removeFirst();
							innerThread[i] = new RPRequestThread(info.url, info.payload, info.method, info.response);
							innerThread[i].start();
						} else if(innerThread[i] == null) {
							LOG.print(this, "Create new RP Request Thread");
							RpInfo info = (RpInfo) queue.removeFirst();
							innerThread[i] = new RPRequestThread(info.url, info.payload, info.method, info.response);
							innerThread[i].start();
						}
					} else {
						//Kill all RP Request Thread
						if(innerThread[i] != null) {
							innerThread[i].interrupt();
							innerThread[i] = null;
						}
						
					}
					
				}
//				synchronized(mutex) {
//					try {
//						sleep(100);
//					} catch(InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
				
			}
			synchronized(mutex) {
				this.isCompleted = true;
			}
			
		
			
		
		}
		
		public void stopAll() {
			synchronized(mutex) {
				this.stopFlag = true;
			}
			
		}
		
		public void execute(RpInfo info) {
			synchronized(mutex) {
				this.queue.addFirst(info);
			}
		}
		
		public int getWaitingCount() {
			synchronized(mutex) {
				return queue.size();
			}
		}
		
	}
	
	private static class RpInfo {
		public URL url = null;
		public String method=null;
		public String payload=null;
		public RPResponse response=null;
		
	}
 	
	
	
	//static ExecutorService executorService = Executors.newCachedThreadPool();
	static ThreadQueue thQueue = null;
	//Header Info
	private static Map staticHeader = null;
	
	public static void setHeader(Map newHeader) {
		staticHeader = newHeader;
	}

    /**
     * New Request
     * @param host
     * @param port
     * @param uri
     * @param method
     * @param payload
     * @param response
     */
    static JSONParser parser=new JSONParser();
    public static void callJSON(final String host, final int port, final String uri, final String method, final Map payload, final RPResult result) {
        Map header=result.prepare(SceneController.getInstance().getCurrentScene());
        RPConnector2.setHeader(header);
        request(host, port, uri, method, payload, new RPResponse() {
            public void onReceived(int code, Map respHeader, Object response) {
                try {
                    String data = (String)response;
                    LOG.print(data);
                    if(data.substring(0,1).toCharArray()[0]=='\uFEFF') data=data.substring(1);
                    JSONObject obj = (JSONObject) parser.parse(data);
                    result.onSuccess(respHeader, obj);
                } catch (ParseException e) {
                    e.printStackTrace();
                    result.onFailed(9999, "Error parse JSON");
                } finally {
                    result.complete(SceneController.getInstance().getCurrentScene());
                }
            }

            public void onFailed(int code, Object response) {
                result.onFailed(code, (String) response);
                result.complete(SceneController.getInstance().getCurrentScene());
            }
        });
    }


	
	/* 80 Port */
	public static void request(final String host, final String uri, final String method, final Map payload, RPResponse response) {
		request(host, 80, uri, method, payload, response);
	}

    public static int request(final String host, final int port, final String uri, final String method, final Map payload, RPResponse response) {
        return request(host, port, uri, method, payload, response, 30*1000);
    }

    public static int request(final String host, final int port, final String uri, final String method, final Map payload, RPResponse response, int timeout) {
        return request(host, port, uri, method, null, payload, response, timeout);
    }
	
	//public static void request(final String host, final int port, final String uri, final String method, final Map payload, RPResponse response) {
    public static int request(final String host, final int port, final String uri, final String method, final Map header, final Map payload, RPResponse response, int timeout) {
	    if(header !=null) RPConnector.setHeader(header);
		StringBuffer urlBuf = new StringBuffer();
		if(host.startsWith("http://")) {
			urlBuf.append(host).append(":").append(String.valueOf(port));
		} else {
			urlBuf.append("http://").append(host).append(":").append(String.valueOf(port));
		}
		if(uri.startsWith("/")) {
			urlBuf.append(uri);
		} else {
			urlBuf.append("/").append(uri);
		}
		try {
			URL url = new URL(urlBuf.toString());
			StringBuffer payloadBuf = new StringBuffer();
			if(payload != null) {
				Iterator it = payload.keySet().iterator();
				while(it.hasNext()) {
				    //중복키를 대응, space로 들어오는 부분  trim처리
					String key = (String)it.next();
					String trimmed_key=key.trim();
					String value = (String)payload.get(key);
					payloadBuf.append(trimmed_key+"="+value);
					if(it.hasNext()) payloadBuf.append("&");
				}
				LOG.print("Payload : " + payloadBuf.toString());
			}
			if(thQueue == null) {
				thQueue = new ThreadQueue();
				thQueue.start();
			}
			RpInfo info = new RpInfo();
			info.url = url;
			info.method = method;
			info.payload = payloadBuf.toString();
			info.response = response;
			thQueue.execute(info);
//			RPRequest rp = new RPRequest(url, payloadBuf.toString(), method, response);
//			executorService.execute(rp);
//			LOG.print("RP Thread Count : "+ ((ThreadPoolExecutor)executorService).getPoolSize());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //Thread Queue는 반환값 없음
		return -1;
	}
	
	/**
	 * ThreadPool은 메인스레드가 종료되도 끝나지 않으므로 destroyXlet에서 반드시 호출해야 함
	 */
	public static void shutdown() {
		LOG.print("RPConnector", "RP Network Thread Pool Destroy...");
		if(thQueue != null) {
			synchronized(thQueue.mutex) {
				thQueue.stopAll();
				thQueue.interrupt();
				thQueue = null;
				
			}
		}
		
		
//		executorService.shutdown();
//		LOG.print("RP Thread Count : "+ ((ThreadPoolExecutor)executorService).getPoolSize());
	}
	
}
