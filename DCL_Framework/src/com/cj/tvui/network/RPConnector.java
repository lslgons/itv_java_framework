package com.cj.tvui.network;

import java.io.*;
import java.net.Socket;
import java.util.*;

import com.cj.tvui.Constants;
import com.cj.tvui.controller.SceneController;
import com.cj.tvui.util.LOG;
import com.cj.tvui.util.StringUtil;
import com.cj.tvui.util.URLEncoder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * RP통신 모듈
 * @author daegon.kim
 *
 */

public class RPConnector {

	public final class RP_METHOD {
		public static final String RP_GET = "GET";
		public static final String RP_POST = "POST";
		public static final String RP_PUT = "PUT";
		public static final String RP_DELETE = "DELETE";
	}


	static Object mutex=new Object();

	static String persistentHost=null;
	static int persistentPort=-1;
	public static void setPersistentHost(String host, int port) {
	    persistentHost=host;
	    persistentPort=port;
    }

    public static boolean isSetPersistentHost() {
	    if(persistentHost==null || persistentPort<0) return false;
	    return true;
    }

	private static class SocketThread extends Thread {
        final static int SO_TIMEOUT=60*1000;
        final static String CRLF="\r\n";
        final static int BUF_SIZE=2048;
        boolean connect;
        boolean isCompleted;
	    Socket socket;
	    PrintStream ps;
        BufferedReader br;
        String ret;
        String method;
        String host;
        int port;
        String uri;
        String formData;
        int connectTimeout;
        Map header;
        RPResponse response;
        int readState; //0:Response State, 1:Header Read, 2:Body Read, 4:End of Stream

        ////////////////////////////
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream bos = null;



        public SocketThread(String method, String host, int port, String uri, String formData, int timeout, RPResponse response) {
            this.method=method;
            this.host = host;
            this.port = port;
            this.connectTimeout=timeout;
            this.uri=uri;
            this.formData=formData;
            connect=false;
            header=null;
            this.response=response;
            isCompleted=false;
            readState=0;
        }
        public void setHeader(Map header) {
            this.header=header;
        }
        public boolean isCompleted() {
            return this.isCompleted;
        }


        public void run() {
            try {
                socket = TimeoutSocket.getSocket(host, port, connectTimeout);
                try {
                    socket.setSoTimeout(SO_TIMEOUT);
                    socket.setKeepAlive(true); //idle connection 상태 유지
                } catch(Exception e) {

                }

                connect=true;
                if(!uri.startsWith("/")) uri="/"+uri;
                if(method==RP_METHOD.RP_GET && (formData !=null&&formData.length()>0)) {
                    LOG.print("Connect to http://"+host+":"+port+"/"+uri+"?"+formData);

                } else {
                    LOG.print("Connect to http://"+host+":"+port+"/"+uri);
                    LOG.print("param : "+formData);
                }


                ps=new PrintStream(socket.getOutputStream(), false);
                StringBuffer sBuf=new StringBuffer();
                if(method==RP_METHOD.RP_GET && (formData!=null&&formData.length()>0)) {
                    sBuf.append(method+" "+uri+"?"+formData+" HTTP/1.0").append(CRLF);
                } else {
                    sBuf.append(method+" "+uri+" HTTP/1.0").append(CRLF);
                }
                sBuf.append("Host: "+host+":"+port).append(CRLF);
                sBuf.append("Connection: close").append(CRLF); //Connection Keep Alive하게되면 2~5초 지연이 생김
                if(method == RP_METHOD.RP_POST) {
                    sBuf.append("Content-Length: ").append(Integer.toString(formData.getBytes().length)).append(CRLF);
                    sBuf.append("Content-Type: application/x-www-form-urlencoded; charset=utf-8").append(CRLF);

                }

                //sBuf.append("Content-Type: application/json; charset=utf-8").append(CRLF); //JSON 데이타 전용
                if(header != null) {
                    //추가 헤더필드 설정값이 있을경우 추가 함
                    Iterator it = header.keySet().iterator();
                    while(it.hasNext()) {
                        String headerKey = (String)it.next();
                        String headerValue = (String)header.get(headerKey);
                        sBuf.append(headerKey+": ").append(headerValue).append(CRLF);
                    }
                }
                sBuf.append(CRLF);
                if(method == RP_METHOD.RP_POST) {
                    if(formData != null && formData.length()>0) {
                        sBuf.append(formData);
                    }
                }

                ps.print(sBuf.toString());
                LOG.print("request to ");
                LOG.print(CRLF+sBuf.toString());
                ps.flush();


                //br=new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"), BUF_SIZE);

                bis = new BufferedInputStream(socket.getInputStream());
                baos = new ByteArrayOutputStream();
                bos = new BufferedOutputStream(baos);
                int c;
                byte[] buf = new byte[2048];
                while ((c = bis.read(buf)) != -1) {
                    bos.write(buf, 0, c);
                }
                bos.flush();

                String rawBody = baos.toString("UTF-8");
                //System.out.println(new String(baos.toByteArray()));
                String tempRead;
                //StringBuffer ssBuf=new StringBuffer();
                boolean isChunked=false; //"transfer-Encoding: chunked일 경우 응답데이터를 해당 길이만큼 쪼개서 받아야 함
                //int chunkSize=-1; //덩어리 크기 (chunked일 경우)
                int contentLength=-1;
                Map responseHeader = new HashMap();
                StringBuffer bodyBuf = new StringBuffer();

                BufferedReader br=new BufferedReader(new StringReader(rawBody));

                //boolean isEndOfStream=false;
                //Thread.sleep(400);
                //long timeout_ts=System.currentTimeMillis();

                LOG.print("Read response header");
                while(readState<2) {
                    tempRead=br.readLine();

                    if(tempRead == null) readState=3;
                    LOG.print(tempRead);

                    switch(readState) {
                        case 0:
                            //Response Code
                            int code=Integer.valueOf(tempRead.substring(9,12)).intValue();
                            if(code !=200) {
                                //HTTP Error
                                response.onFailed(code, "HTTP Error");
                                readState=3;
                            } else {
                                readState=1;
                            }
                            break;
                        case 1:
                            //Header Read
                            if(tempRead.length() >0) {
                                int delimIdx = tempRead.indexOf(":"); // headerName:headerValue
                                String headerName = tempRead.substring(0, delimIdx);
                                String headerValue = tempRead.substring(delimIdx + 1).trim();
                                if(headerName.indexOf("Transfer-Encoding")>-1&&headerValue.indexOf("chunked")>-1) {
                                    isChunked=true;
                                } else if(headerName.indexOf("Content-Length")>-1) {
                                    contentLength=Integer.parseInt(headerValue);
                                }
                                //2018-10-12, Response 헤더정보는 List형태로 넘겨야 함
                                List valueList = (List) responseHeader.get(headerName);
                                if(valueList == null) {
                                    valueList = new LinkedList();
                                }
                                valueList.add(headerValue);
                                responseHeader.put(headerName, valueList);
                            } else {
                                readState=2;
                            }
                            break;
                    }
                }

                if(readState == 2) {
                    if(isChunked) {
                        //chucked mode
                        while(contentLength!=0) {
                            //UTF-8인코딩 중 오류발생 문제.... 일단 예외처리
                            try {
                                contentLength=Integer.parseInt(br.readLine(), 16);
                            } catch(IOException e) {
                                LOG.print("IOException...");
                            } catch(Exception e) {
                                LOG.print("Buffered Reader Unknown Exception");
                            }

                            if(contentLength != 0) {
                                try {
                                    bodyBuf.append(br.readLine());
                                } catch(IOException e) {
                                    LOG.print("IOException...");
                                } catch(Exception e) {
                                    LOG.print("Buffered Reader Unknown Exception");
                                }

                            }
                        }
                    } else {
                        //content-length
                        try {
                            bodyBuf.append(br.readLine());
                        } catch(IOException e) {
                            LOG.print("IOException...");
                        } catch(Exception e) {
                            LOG.print("Buffered Reader Unknown Exception");
                        }
                    }
                }


                String body = bodyBuf.toString();
                LOG.print("Body Data : ");
                LOG.print(body);
                if(body.length()>0) {
                    response.onReceived(200, responseHeader, body);
                } else {
                    response.onFailed(405, "null data");
                }
//                if (ret == null || ret.length() == 0) {
//                    LOG.print(this, "execute ret is null");
//                    response.onFailed(991, "value is nothing");
//                }

            } catch(InterruptedException ie) {
                LOG.print("Socket Interupped!!!");
            }
//            } catch(SocketTimeoutException ste) {
//                LOG.print("Socket Timeout Exception...");
//                if(response != null) {
//                    response.onFailed(988, "Socket Timeout");
//                }
//
//            }
            catch (Exception e) {
                LOG.print("################# Socket Exception ##################");
                e.printStackTrace();
                if(response != null) {
                    response.onFailed(990, "Connection Error");

                }
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        LOG.print(e);
                    }
                }
                ps = null;

                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        LOG.print(e);
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception e) {
                    }
                }
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e) {
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (Exception e) {
                    }
                }
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (Exception e) {
                    }
                }
                if(header !=null) {
                    header.clear();
                    header=null;
                }

                br = null;
                bis=null;
                bos=null;
                baos=null;
                socket = null;
                ret=null;
                isCompleted=true;
            }
            super.run();

        }
    }
	
	/* deprecated */
	public static void setHeader(Map newHeader) {
		//staticHeader = newHeader;

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
        RPConnector.setHeader(header);
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


    final static int MAX_SOCK_COUNT=4;
    static SocketThread[] st=new SocketThread[MAX_SOCK_COUNT];

    public static void cancel(final int index) {
        if(index >= MAX_SOCK_COUNT) {
            LOG.print("Invalid socket index.");
        } else {
            if(st[index]!=null) {
                st[index].interrupt();
                st[index]=null;
            }
        }

    }


    public static int request(final String host, final int port, final String uri, final String method, final Map header, final Map payload, RPResponse response, int timeout) {
        StringBuffer payloadBuf = new StringBuffer();
        if(payload != null) {
            Iterator it = payload.keySet().iterator();
            while (it.hasNext()) {
                //중복키를 대응, space로 들어오는 부분  trim처리
                String key = (String) it.next();
                String trimmed_key = key.trim();
                String value = (String) payload.get(key);
                //20181102, 들어오는 Body 데이터를 URL Encoding 처리함
//                payloadBuf.append(trimmed_key + "=" + value);
                try {
                    payloadBuf.append(trimmed_key + "=" + URLEncoder.encode(value, "UTF-8"));
                } catch(UnsupportedEncodingException e) {
                    LOG.print("UnsupportedEncoding Error");
                }

                if (it.hasNext()) payloadBuf.append("&");
            }
            LOG.print("Payload : " + payloadBuf.toString());
        }
        int index=-1;
        for(int i=0;i<MAX_SOCK_COUNT;++i) {
            if(st[i] != null) {
                if(st[i].isCompleted()) {
                    st[i].interrupt();
                    st[i]=null;
                    st[i]=new SocketThread(method, host, port, uri, payloadBuf.toString(), timeout, response);
                    if(header != null) st[i].setHeader(header);
                    st[i].start();
                    index=i;
                    break;
                }
            } else {
                st[i]=new SocketThread(method, host, port, uri, payloadBuf.toString(), timeout, response);
                if(header != null) st[i].setHeader(header);
                st[i].start();
                index=i;
                break;
            }
        }

        return index;

    }


    public static int request(final String host, final int port, final String uri, final String method, final Map payload, RPResponse response, int timeout) {
        return request(host, port, uri, method, null, payload, response, timeout);
    }

    public static int request(final String host, final int port, final String uri, final String method, final Map payload, RPResponse response) {
        return request(host, port, uri, method, payload, response, 30*1000);
    }

	/* 80 Port */
	public static int request(final String host, final String uri, final String method, final Map payload, RPResponse response) {
		return request(host, 80, uri, method, payload, response);
	}

	public static int request(final String uri, String method, Map payload, RPResponse response) throws Exception {
	    if(isSetPersistentHost()) {
            return request(persistentHost, persistentPort, uri, method, payload, response);
        } else {
	        LOG.print("Error : RP Host is not set...");
	        throw new Exception();
        }
    }

	
	/**
	 * ThreadPool은 메인스레드가 종료되도 끝나지 않으므로 destroyXlet에서 반드시 호출해야 함
     *
	 */
	public static void shutdown() {
		LOG.print("RP Network Thread Pool Destroy...");
		for(int i=0;i<MAX_SOCK_COUNT;++i) {
            if(st[i] != null) {
                st[i].interrupt();
                st[i]=null;
            }
        }
        st=null;

	}


	public static void main(String[] argv) {
	    final long cur_time=System.currentTimeMillis();
        Constants.IS_EMUL=true;
        Constants.ENABLE_LOG=true;
	    LOG.print("Start Request");
	    RPConnector.request("210.122.101.156", 11080, "/cjlive/skylife/acap/trigger/trigger960.json", RP_METHOD.RP_GET, null, new RPResponse() {
        //RPConnector.request("210.122.102.171", 80, "/cjtosplus/appRp/getOnairProductEx.jsp", RP_METHOD.RP_POST, null, new RPResponse() {
        //RPConnector.request("216.58.221.4", 80, "/", RP_METHOD.RP_GET, null, new RPResponse() {

            public void onReceived(int code, Map respHeader, Object response) {
                LOG.print("Message Received");
                LOG.print(response);
                LOG.print("+++"+(System.currentTimeMillis()-cur_time));
            }

            public void onFailed(int code, Object response) {
                LOG.print("+++"+(System.currentTimeMillis()-cur_time));
            }
        });
	    RPConnector.shutdown();
    }
	
}
