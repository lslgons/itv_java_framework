package com.cj.tvui.network;

import com.cj.tvui.controller.SceneController;
import com.cj.tvui.util.LOG;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.*;

/**
 * RP통신 모듈
 * @author daegon.kim
 *
 */

public class RPConnector3 {
	

	public final class RP_METHOD {
		public static final String RP_GET = "GET";
		public static final String RP_POST = "POST";
		public static final String RP_PUT = "PUT";
		public static final String RP_DELETE = "DELETE";
	}


	static Object mutex=new Object();

	private static class SocketThread extends Thread {
        final static int SO_TIMEOUT=30*1000;
        final static String CRLF="\r\n";
        final static int BUF_SIZE=4096;
        boolean connect;
        boolean isCompleted;
	    Socket socket;
	    PrintStream ps;
	    //BufferedInputStream bis;
        //ByteArrayOutputStream baos = null;
        //BufferedOutputStream bos = null;
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
                //socket = new Socket(host, port);
                try {
                    socket.setSoTimeout(SO_TIMEOUT);
                } catch(Exception e) {

                }

                connect=true;
                if(!uri.startsWith("/")) uri="/"+uri;
                if(method== RP_METHOD.RP_GET && (formData !=null&&formData.length()>0)) {
                    LOG.print("Connect to http://"+host+":"+port+"/"+uri+"?"+formData);

                } else {
                    LOG.print("Connect to http://"+host+":"+port+"/"+uri);
                    LOG.print("param : "+formData);
                }


                ps=new PrintStream(socket.getOutputStream(), false);
                StringBuffer sBuf=new StringBuffer();
                if(method== RP_METHOD.RP_GET && (formData!=null&&formData.length()>0)) {
                    sBuf.append(method+" "+uri+"?"+formData+" HTTP/1.1").append(CRLF);
                } else {
                    sBuf.append(method+" "+uri+" HTTP/1.1").append(CRLF);
                }
                sBuf.append("Host: "+host+":"+port).append(CRLF);
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
                    if(formData != null && formData.length()>0) sBuf.append(formData);
                }

                ps.print(sBuf.toString());
                LOG.print("request to ");
                LOG.print(CRLF+sBuf.toString());
                ps.flush();


                //bis = new BufferedInputStream(socket.getInputStream());
                //baos = new ByteArrayOutputStream();
                //bos = new BufferedOutputStream(baos);
                //int c;
                //byte[] buf = new byte[BUF_SIZE];


                //Logic 3
                //BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"), BUF_SIZE);
                br=new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"), BUF_SIZE);

                String tempRead;
                StringBuffer ssBuf=new StringBuffer();
//                while ((tempRead = bufferedReader.readLine()) != null) {
//                    ssBuf.append(tempRead+CRLF);
//                }

                boolean isEndOfStream=false;

                long timeout_ts=System.currentTimeMillis();
                while(!isEndOfStream) {
                    //LOG.print("readLine!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    if(System.currentTimeMillis()-timeout_ts > 30*1000) {
                        LOG.print("Connection timeout");
                        response.onFailed(995, "Connection Timeout...");
                        throw new Exception();
                    }

                    //tempRead=bufferedReader.readLine();
                    tempRead=br.readLine();
                    if(tempRead == null) isEndOfStream=true;
                    else {
                        ssBuf.append(tempRead+CRLF);
                    }
                }

                //Logic 3

                  //Logic 2

//                //데이터 수신받는 시간 대기
//                long wait_timeout=0;
//                while(true) {
//                    if(bis.available()>0) break;
//                    Thread.sleep(100);
//                    wait_timeout+=100;
//                    LOG.print("Wait for response..... : "+wait_timeout);
//                    if(wait_timeout>30*1000) {
//                        LOG.print("Response Timeout...");
//                        response.onFailed(995, "Connection Timeout...");
//                        throw new Exception();
//                    }
//                }
//
//                Thread.sleep(100);
//
//                while(bis.available()>0) {
//                    c=bis.read(buf);
//                    bos.write(buf, 0, c);
//                    //if(bis.available()<=0) break;
//                }
                //Logic 2

                //Logic 1
//                while ((c = bis.read(buf)) != -1) {
//                    LOG.print("readLine!!!!!!!!!!!!!!!!!");
//                    bos.write(buf, 0, c);
//                }
                //Logic 1


                //bos.flush();
                //ret = baos.toString("UTF-8"); //헤더포함 데이터
                ret=ssBuf.toString();
                LOG.print("response to");
                LOG.print(CRLF+ret);
                Map responseHeader = new HashMap();
                StringReader sr = null;
                BufferedReader _br = null;


                String line;
                boolean isChunked=false; //"transfer-Encoding: chunked일 경우 응답데이터를 해당 길이만큼 쪼개서 받아야 함
                int chunkSize=0; //덩어리 크기 (chunked일 경우)
                StringBuffer bodyBuf=new StringBuffer();





                try {
                    sr = new StringReader(ret);
                    _br = new BufferedReader(sr);
                    String responseCodeLine=_br.readLine(); // Response Code
                    int code=Integer.valueOf(responseCodeLine.substring(9,12)).intValue();
                    if(code !=200) {
                        //HTTP Error
                        response.onFailed(code, "HTTP Error");
                        return;
                    }
                    boolean isBodyStart=false;

                    boolean isBodyEnd=false;


                    //while ((line = br.readLine()) != null) {
                    while(!isBodyEnd) {
//                        LOG.print(this, line + "<<");
                        line=_br.readLine();
                        if(line == null) {
                            isBodyEnd=true;
                            break;
                        }

                        if(line != null && line.length()==0) isBodyStart=true; //줄넘김, 바디 시작지점

                        if (!isBodyStart) {
                            int delimIdx = line.indexOf(":"); // headerName:headerValue
                            String headerName = line.substring(0, delimIdx);
                            String headerValue = line.substring(delimIdx + 1).trim();
                            if(headerName.indexOf("Transfer-Encoding")>-1&&headerValue.indexOf("chunked")>-1) {
                                isChunked=true;
                            }
                            //2018-10-12, Response 헤더정보는 List형태로 넘겨야 함
                            List valueList = (List) responseHeader.get(headerName);
                            if(valueList == null) {
                                valueList = new LinkedList();
                            }
                            valueList.add(headerValue);
                            responseHeader.put(headerName, valueList);
                        } else {
                            // Body 영역
                            if(line.length()>0) {
                                if(isChunked) {
                                    if(chunkSize==0) {
                                        chunkSize=Integer.parseInt(line, 16);
                                    } else {
                                        bodyBuf.append(line);
                                        chunkSize=0;
                                    }
                                } else {
                                    if(line.length()>0) {
                                        bodyBuf.append(line);
                                    }
                                }
                            }

                        }

                    }


                } catch(Exception e) {
                    LOG.print("Data Parsing Exception...");
                } finally {
                    if (sr != null) {
                        try {
                            sr.close();
                        } catch (Exception e) {
                        }
                    }
                    sr = null;
                    if (_br != null) {
                        try {
                            _br.close();
                        } catch (Exception e) {
                        }
                    }
                    _br = null;
                }


                String body = bodyBuf.toString();
                if(body.length()>0) {
                    response.onReceived(200, responseHeader, body);
                } else {
                    response.onFailed(405, "null data");
                }


                if (ret == null || ret.length() == 0) {
                    LOG.print(this, "execute ret is null");
                    response.onFailed(991, "value is nothing");
                }
            } catch(InterruptedException ie) {
                LOG.print("Socket Interupped!!!");
            } catch (Exception e) {
                LOG.print("################# Socket Exception ##################");
                if(response != null) {
                    response.onFailed(990, "Connection Error");
                }
            } finally {
//                if (bos != null) {
//                    try {
//                        bos.close();
//                    } catch (IOException e) {
//                        LOG.print(e);
//                    }
//                }
//                bos = null;
//
//                if (baos != null) {
//                    try {
//                        baos.close();
//                    } catch (Exception e) {
//                        LOG.print(e);
//                    }
//                }
//                baos = null;
//
//                if (bis != null) {
//                    try {
//                        bis.close();
//                    } catch (IOException e) {
//                        LOG.print(e);
//                    }
//                }
//                bis = null;

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
                br = null;
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
        RPConnector3.setHeader(header);
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
                payloadBuf.append(trimmed_key + "=" + value);
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
	}
	
}
