package com.tcom.platform.dmc.cjhv;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Properties;

import com.tcom.network.TimeoutSocket;
import com.tcom.platform.controller.StbController;
import com.tcom.ssr.SSRConfig;
import com.tcom.util.LOG;
import com.tcom.util.PropertyReader;
import com.tcom.util.StringUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



// 앱 진입시 getSMSTag() 호출
//										- 호출 실패시 값 비움  로그인 제한 걸음 && soCode 은 EpgService 에서 getSoNumber() 호출로 사용
//										- 호출 성공시 EpgService 버전 존재 여부에 따라 - 존재시 requestBiz() 호출      - 성공시 sms tag , accessToken 획득 - soCode 는 getAccount() 호출로 사용
//																																		 - 실패시  값 비움  로그인 제한 걸음 && soCode 도 EpgService 에서 getSoNumber() 호출로 사용
//																								 	- 미존재시 requestCpn() 호출 - 성공시 sms tag 획득 -  soCode 는 EpgService 에서 getSoNumber() 호출로 사용
//																																		  - 실패시 값 비움 로그인 제한 걸음  && soCode 도 EpgService 에서 getSoNumber() 호출로 사용

public class CJHAPIManager {

    /**
     * RP 사용 설정
     */

    public boolean useSocket = true; // 뭘 쓰든 차이는 없다. socket 이냐 httpurlconnection 이냐 의 차이. 다만 timeout 적용하려면 true

    public String API_HOST;
    public int API_PORT;
    public String API_PATH;

    public String CPN_API_HOST;
    public String CPN_API_PATH;

    public String X_CLIENT_APP_KEY;
    public long token_expire_time = -1L;
    public String accessToken = null; // 초기 값은 null.  통신 시도후 실패시 empty 로 처리

    private String sms_tag = null; // 초기 값은 null.  통신 시도후 실패시 empty 로 처리

    //서비스 로그인시 받아오는 값 필드 추가 YoungMin 2019.01.18
    public String soCode = null; // "43"

    private String charSet = "UTF-8";
	public static final String CRLF = "\r\n";
    private int CONN_TIMEOUT = 20 * 1000;

    private static CJHAPIManager instance;

    public static synchronized CJHAPIManager getInstance() {
        if (instance == null) {
            instance = new CJHAPIManager();
        }
        return instance;
    }

    private CJHAPIManager() {
		/**
		 * 보낸사람:성민석님[성민석님][minsuk.sung@cj.net]
		 보낸시간:2019년 2월 26일 화요일 오전 10:45:17
		 받는사람:서성옥님
		 참조:정지훈님 [Jeong JiHoon]; 강민석님 [Kang-Minseok]; 한현진님 [Han HyunJin]
		 제목:RE: [CJ ENM] 오쇼핑플러스 Biz 시스템 연동

		 안녕하세요 서성옥님,

		 CJ라이브 클라이언트키 공유 드립니다.

		 QHAKTE8SNC1MNCBV0B26DRTGDC
		 */
		PropertyReader prop=SSRConfig.getInstance().getPropertyReader();
		this.X_CLIENT_APP_KEY = prop.read("X_CLIENT_APP_KEY");

		this.API_HOST = prop.read("CJH_API_HOST");
		this.API_PORT = Integer.parseInt(prop.read("CJH_API_PORT"));
		this.API_PATH = prop.read("CJH_API_PATH");

		this.CPN_API_HOST = prop.read("CPN_API_HOST");
		this.CPN_API_PATH = prop.read("CPN_API_PATH");

		this.CONN_TIMEOUT = Integer.parseInt(prop.read("CONN_TIMEOUT"));

		LOG.print("============= CJHAPIManager prob =============");
		LOG.print("API_HOST::" + this.API_HOST);
		LOG.print("API_PORT::" + this.API_PORT);
		LOG.print("API_PATH::" + this.API_PATH);
		LOG.print("");
		LOG.print("CPN_API_HOST::" + this.CPN_API_HOST);
		LOG.print("CPN_API_PATH::" + this.CPN_API_PATH);
		LOG.print("=====================================");

    }


    public void destroy() {
        instance = null;
    }


	// SMS TAG #################################################################################
    /*
	정지훈님 [Jeong JiHoon] 02-11 월 오후 15:07:24 보냄

	안녕하세요. 서성옥님. 정지훈입니다.
	저희가 알래스카 UI가 적용된 STB의 경우에만 Biz 쪽에서 정보를 받아오는데요.

	오쇼핑플러스의 경우에는 RP서버 통신하는 내용이 없기 때문에, 상품 연동은
	분기치실 필요는 없고, sms_tag 얻어오는 부분만 분기치면 될 것 같습니다.

	EPG ixc의 EpgService#gerVersion으로 UI버전을 체크를 해보신 뒤에
	Exception이 떨어진다면 구 쿠폰서버를 통한 sms_tag를 획득하시고,
	버전정보가 리턴이 된다면 Biz 서버를 통해 sms_tag를 획득하시면 될 것 같습니다.
     */
	// #######################################################################################
    public String getSMSTag() throws Exception {
    	LOG.debug(this, " getSMSTag() ");
		if (SSRConfig.getInstance().IS_EMUL) { // YoungMin add 2018.09.10
			return this.sms_tag = "1234567890";
		}

        if(this.accessToken == null) refeshToken();
        return this.sms_tag;
    }

    public boolean isTokenObtained() {
        if (this.accessToken == null || this.accessToken.length() <= 0) {
            LOG.print(this, "accessToken not found");
            this.accessToken="";
            return false;
        }
        long curTime = System.currentTimeMillis();
        if (curTime > this.token_expire_time) {
            LOG.print(this, "accessToken expired");
            this.accessToken="";
            return false;
        }
        return true;
    }

    // SMS TAG (Biz 서버) #################################################################################
    /**
     * 토큰 값 갱신
     * accountId가 sms_tag
     * accessToken이 토큰값
     * @return
     * @throws Exception
     */
    public void refeshToken() throws Exception {
    	LOG.debug(this, " refeshToken() ");
        JSONParser parser;
        SimpleDateFormat sdf;
        String accountId = null;
        String res = null;
        JSONObject jObj;
        SSRConfig config = SSRConfig.getInstance();
        try {
        	if( config.IS_ALASKA_UI==1 ) {
        		res = requestBiz(); // 알래스카 UI O
        	}else if(config.IS_ALASKA_UI==0){
        		res = requestCpn(); // 알래스카 UI X
        	}
            print(res);

            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            parser = new JSONParser();
            jObj = (JSONObject) parser.parse(res);

            this.accessToken = StringUtil.retStringValue(jObj, "accessToken");
            accountId = StringUtil.retStringValue(jObj, "accountId");
            if(accountId.equalsIgnoreCase("null")) accountId = ""; // String 문자 "null" 로 내려올 가능성도 있으므로 체크하여 blank 로 변경
            this.sms_tag = accountId;

            if( config.IS_ALASKA_UI==1 ) {
            	config.ALASKA_BIZ_TAG = accountId;
            }else if(config.IS_ALASKA_UI==0){
            	config.ALASKA_CPN_TAG = accountId;
            }
            this.token_expire_time = (sdf.parse(StringUtil.retStringValue(jObj, "expiresAt"))).getTime();
        } catch (Exception e) {
            this.accessToken = "";
            this.sms_tag = "";
            this.token_expire_time = -1;
            LOG.print(this, "Refresh accessToken Exception ======= " + e.getMessage());
            throw e;
        } finally {
        	parser = null;
        	sdf = null;
        	res = null;
        	jObj = null;
        	accountId = null;
        }

    }

    //daegon.kim - Use Library (2018.09.12)
    public String requestBiz() throws Exception {
    	LOG.debug(this, " requestBiz() ");

    	String rslt = null;
    	StringBuffer sb = null;
    	String postData = null;
    	byte[] postDataBytes = null;
    	String s_url = null;
    	URL url = null;
    	HttpURLConnection conn = null;
    	BufferedReader in = null;
    	String inputLine = null;
		StbController stbController=StbController.getInstance();
    	try {
            // 2018.09.12 - hyeokjun, - param 생성 방법 변경 (Map -> String)
            postData = new StringBuffer("grantType=password&username=").append(stbController.getSubscribeID())
            		.append("&password=").append(stbController.getMacAddr()).append("&authDomain=CJHV").toString();

    		if(this.useSocket) {

       			sb = new StringBuffer();
    			sb.append("POST ").append(this.API_PATH).append("/token").append(" HTTP/1.0").append(CRLF);
    			sb.append("X-Client-App-Key: ").append(this.X_CLIENT_APP_KEY).append(CRLF);
    			sb.append("Content-Type: application/x-www-form-urlencoded; charset=utf-8").append(CRLF);
    			sb.append("Content-Length: " + postData.length()).append(CRLF);
    			sb.append(CRLF);
    			sb.append(postData);

    			rslt =  execute("POST", this.API_HOST,this.API_PORT, sb.toString());

    		}else{
                postDataBytes = postData.getBytes(this.charSet);

                s_url = new StringBuffer("http://").append(this.API_HOST).append(":").append(this.API_PORT).append(this.API_PATH).append("/token").toString();

                url = new URL(s_url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("X-Client-App-Key", this.X_CLIENT_APP_KEY);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

                LOG.print("============= Host Info =============");
                LOG.print("Parameter::" + postData);
                LOG.print("URL::" + s_url);
                LOG.print("X-Client-App-Key::" + conn.getRequestProperty("X-Client-App-Key"));
                LOG.print("Content-Type::" + conn.getRequestProperty("Content-Type"));
                LOG.print("=====================================");

                conn.getOutputStream().write(postDataBytes); //POST호출

                LOG.print("Result Code : "+conn.getResponseCode());
                if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    LOG.print("Response Error");
                }

                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                sb = new StringBuffer();
                while((inputLine = in.readLine()) != null) {
                	sb.append(inputLine);
                }
                rslt = sb.toString();
    		}

            LOG.print("Response : "+rslt);
            // 응답예시 - json 타입을 반환
//            {
//        	  "accountId" : "TB92801811301658031",
//        	  "accessToken" : "00USPV3DB6U3OQRRGM81CI6TL0",
//        	  "expiresAt" : "2019-02-12T15:08:04"
//        	}
            //
    	}catch(Exception e) {
    		LOG.print("requestBiz Exception " + e.getMessage());
    		throw e;
    	}finally{
    		if(in != null) {
    			in.close();
    			in = null;
    		}
    		if(conn != null) {
    			conn.disconnect();
    			conn = null;
    		}
        	sb = null;
        	postData = null;
        	postDataBytes = null;
        	s_url = null;
        	url = null;
        	inputLine = null;
    	}
    	LOG.print("requestBiz End");
        return rslt;
    }


	// SMS TAG (구 쿠폰서버) - 인증키 불필요, accessToken 값 반환 없음.  #################################################################################
    public String requestCpn() throws Exception {
    	LOG.debug(this, " requestCpn() ");

    	JSONObject jo = new JSONObject();
    	String rslt = null;
    	StringBuffer sb = null;
    	String postData = null;
    	byte[] postDataBytes = null;
    	String s_url = null;
    	URL url = null;
    	HttpURLConnection conn = null;
    	BufferedReader in = null;
    	String inputLine = null;
    	try {
            postData = new StringBuffer("out_type=txt&card_id=&client=IVP&subscriber_id=").append(StbController.getInstance().getSubscribeID()).toString();
            if(this.useSocket){
    			sb = new StringBuffer();
    			sb.append("POST ").append(this.CPN_API_PATH).append(" HTTP/1.0").append(CRLF);
    			sb.append("Content-Type: application/x-www-form-urlencoded; charset=utf-8").append(CRLF);
    			sb.append("Content-Length: " + postData.length()).append(CRLF);
    			sb.append(CRLF);
    			sb.append(postData);

    			rslt =  execute("POST", this.CPN_API_HOST,80, sb.toString());

            }else{
            	postDataBytes = postData.getBytes(this.charSet);

                s_url = "http://"+this.CPN_API_HOST+this.CPN_API_PATH;

                url = new URL(s_url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                LOG.print("============= Host Info =============");
                LOG.print("Parameter::" + postData);
                LOG.print("URL::" + s_url);
                LOG.print("=====================================");

                conn.getOutputStream().write(postDataBytes); //POST호출

                LOG.print("Result Code : "+conn.getResponseCode());
                if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    LOG.print("Response Error");
                }else{
                    in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    sb = new StringBuffer();
                    while((inputLine = in.readLine()) != null) {
                    	sb.append(inputLine);
                    }
                    rslt = sb.toString();
                }
            }

            jo.put("accountId", "");
        	jo.put("accessToken", "");
        	jo.put("expiresAt",  "2149-01-01T00:00:00"); // 구 쿠폰 서버를 통해서 accessToken 을 얻을순 없다.

            LOG.print("Response : "+rslt);  //  문자열 반환   | 구분자로 잘라서 맨 앞이 0 이어야 성공 데이터.    코드|메시지|sms_tag   ex) 0|OK|110002691284

            String[] result = StringUtil.tokenize(rslt, "|");
            if(result != null && result.length == 3 && "0".equals(result[0])) {
            	jo.put("accountId", result[2]);
            }
    	}catch(Exception e) {
    		LOG.print("requestToken Exception " + e.getMessage());
    		throw e;
    	}finally{
    		if(in != null) {
    			in.close();
    			in = null;
    		}
    		if(conn != null) {
    			conn.disconnect();
    			conn = null;
    		}
        	sb = null;
        	postData = null;
        	postDataBytes = null;
        	s_url = null;
        	url = null;
        	inputLine = null;
    	}

        LOG.print("requestCpn End");
        return jo.toJSONString();
    }

	// BIZ INFO #################################################################################
    public void getAccount() throws Exception { // 인증토큰으로 CJH의 정보 조회 2019.01.17 YoungMin.

    	LOG.debug(this, " getAccount() ");

    	String rslt = null;
    	StringBuffer sb = null;
    	String s_url = null;
    	URL url = null;
    	HttpURLConnection conn = null;
    	BufferedReader in = null;
    	String inputLine = null;
        JSONParser parser = null;
        JSONObject jObj = null;
        JSONObject domain = null;
        try {

        	if(this.useSocket){
    			sb = new StringBuffer();
    			sb.append("GET ").append(this.API_PATH).append("/info?accessToken=").append(this.accessToken).append(" HTTP/1.0").append(CRLF);
    			sb.append("X-Client-App-Key: ").append(this.X_CLIENT_APP_KEY).append(CRLF);
    			sb.append(CRLF);

    			rslt =  execute("GET", this.API_HOST,this.API_PORT, sb.toString());
        	}else{
        		s_url = new StringBuffer("http://").append(this.API_HOST).append(":").append(this.API_PORT).append(this.API_PATH).append("/info?accessToken=").append(this.accessToken).toString();

                url = new URL(s_url);

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-Client-App-Key", this.X_CLIENT_APP_KEY);

                LOG.print("============= Host Info =============");
                LOG.print("URL::" + s_url);
                LOG.print("X-Client-App-Key::" + conn.getRequestProperty("X-Client-App-Key"));
                LOG.print("=====================================");

                LOG.print("Result Code : "+conn.getResponseCode());
                if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    LOG.print("Response Error");
                	this.soCode = "";
                }else{
                    in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    sb = new StringBuffer();
                    while((inputLine = in.readLine()) != null) {
                    	sb.append(inputLine);
                    }
                    rslt = sb.toString();

                }
        	}


            LOG.print("Response : "+rslt);

            parser = new JSONParser();
            jObj = (JSONObject) parser.parse(rslt);
//            this.idStatus = (String) jObj.get("status"); // 서비스 계정의 상태.
//            LOG.print(this,"idStatus Value is [" +this.idStatus +"]");

            domain = (JSONObject)jObj.get("domain");
            if(domain != null) {
            	this.soCode = StringUtil.retStringValue(domain, "soCode"); // "43" //JCN SO Code : 65

                LOG.print(this,"soCode Value is [" +this.soCode +"]");

//            	this.spCode = Util.retStringValue(domain, "spCode"); // "CJHV"
//            	this.soName = Util.retStringValue(domain, "soName"); // "(주)양천방송"
//             	LOG.print(this,"spCode Value is [" +this.spCode +"]");
//             	LOG.print(this,"soName Value is [" +this.soName +"]");
            }else{
            	this.soCode = "";
            }

        }  catch (Exception e) {
        	this.soCode = "";

            LOG.print(this, "getAccount Exception" + e.getMessage());
            throw e;
        } finally {
    		if(in != null) {
    			in.close();
    			in = null;
    		}
    		if(conn != null) {
    			conn.disconnect();
    			conn = null;
    		}
        	sb = null;
        	s_url = null;
        	url = null;
        	inputLine = null;
        	rslt = null;
            parser = null;
            jObj = null;
        }

        LOG.print("getAccount End");
    }

    public String getSoCode () throws Exception { // SoCode 받아오는 부분 추가. YoungMin 2019.01.18
    	if(this.soCode == null) {
    		getAccount();
    	}
    	return this.soCode;
    }

    private void print(String retStr) {
        print(retStr, !false);
    }

    private void print(String retStr, boolean bForce) {
        if (retStr == null) {
            LOG.print("RpManager", "print(), RES is NULL !!");
            return;
        } else {
            if (bForce) {
                LOG.print("RpManager", "print(), RES/" + retStr + "\n");
                return;
            } else {
                if (retStr.length() > 350) {
                    LOG.print("RpManager", "print(), RES/Long retStr, len = " + retStr.length());
                } else {
                    LOG.print("RpManager", "print(), RES/" + retStr);
                }
                return;
            }
        }
    }

	public  String execute(String TC_METHOD, String TC_HOST, int TC_PORT,  String header) throws Exception {

		LOG.print(this, "execute(), TC_METHOD : " + TC_METHOD);
		LOG.print(this, "execute(), TC_HOST : " + TC_HOST);
		LOG.print(this, "execute(), TC_PORT : " + TC_PORT);
		LOG.print(this, "execute(), HEADER : " + header);


		Socket socket = null;
		PrintStream ps = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		BufferedOutputStream bos = null;
		String ret;
		try {

			/**
			 * Connection Timeout 설정모드
			 */
			socket = TimeoutSocket.getSocket(TC_HOST, TC_PORT, this.CONN_TIMEOUT);

			try {
				socket.setSoTimeout(this.CONN_TIMEOUT);
			} catch (NoSuchMethodError nme) {
			} catch (Exception e) {
			}

			LOG.print("http://"+TC_HOST+":"+TC_PORT);
			LOG.print("header:["+header+"]");

			ps = new PrintStream(socket.getOutputStream(), false);

			ps.print(header);
			ps.flush();

			bis = new BufferedInputStream(socket.getInputStream());
			baos = new ByteArrayOutputStream();
			bos = new BufferedOutputStream(baos);
			int c;
			byte[] buf = new byte[2048];
			while ((c = bis.read(buf)) != -1) {
				bos.write(buf, 0, c);
			}
			bos.flush();
			ret = baos.toString(this.charSet);
			// 서버에서 결과물은 utf-8 로 읽는다.
			if (ret == null || ret.length() == 0) {
				LOG.print(this, "execute ret is null");
				return null;
			} else {

				if (ret.indexOf('{') > -1) {
					return ret.substring(ret.indexOf('{'));
				} else {
					String[] result = StringUtil.tokenize(ret, CRLF);
					if(result != null && result.length > 0) {
						return result[result.length-1].trim();
					}else{
						return "";
					}
				}
			}
		} catch (Exception e) {
			LOG.print(e);
			throw e;
		} finally {
			ret = null;
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					LOG.print(e);
				}
			}
			bos = null;

			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {
					LOG.print(e);
				}
			}
			baos = null;

			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					LOG.print(e);
				}
			}
			bis = null;

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
			socket = null;

			LOG.print("execute(), connect END socket = " + socket + ", ps = " + ps + ", bis = " + bis + ", baos = " + baos + ", bos = " + bos);
		}
	}
}
