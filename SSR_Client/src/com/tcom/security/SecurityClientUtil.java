package com.tcom.security;

import com.tcom.ssr.SSRConfig;
import com.tcom.util.LOG;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SecurityClientUtil {
	private static SecurityClientUtil securityClientUtil = null;

	private final String keyDefault = "#CJOshoppingPLUS@T-Commerce$2016";
	private final int keyByteLength = 32;
	private final int keyURLStartPoint = 33;
	private final int keyURLLength = 256;

	private final Rijndael rijndael = new Rijndael();

	private byte[] keyByte = null;

	public static SecurityClientUtil getInstance() {
		if (securityClientUtil == null) {
			securityClientUtil = new SecurityClientUtil();
		}

		return securityClientUtil;
	}

	private SecurityClientUtil() {
		this.keyByte = null;
		setKey(SSRConfig.getInstance().CONTEXT_KEY, SSRConfig.getInstance().CONTEXT_SALT);
	}

	public void setKey(String key, String salt) {
		setKey(null, -1, key, salt);
	}

	public void setKey(String urlHost, int portHost, String key, String salt) {
		byte[] byteKey = null;

		LOG.print(this, "[SecurityClientUtil:setKey] START : urlHost=" + urlHost + "/portHost=" + portHost + "/key=" + key + "/salt=" + salt);

		byteKey = getKeyFromString(key, salt);

		if ((urlHost != null) &&
				(0 < urlHost.trim().length()) &&
				(portHost > 80) &&
				(portHost < 30000)) {
			LOG.print(this, "[SecurityClientUtil:setKey] Get Key From URL : " + urlHost + ":" + portHost);
			byteKey = getKeyFromURL(urlHost, portHost, byteKey);
		}

		if ((byteKey != null) &&
				(this.keyByteLength == byteKey.length)) {
			LOG.print(this, "[SecurityClientUtil:setKey] Valid Key");

			this.rijndael.makeKey(byteKey, (this.keyByteLength*8));

			this.keyByte = new byte[this.keyByteLength];
			System.arraycopy(byteKey, 0, this.keyByte, 0, this.keyByteLength);
		}

		LOG.print(this, "[SecurityClientUtil:setKey] END");
		try {
			LOG.print(this, "++++++>" + new String(this.keyByte, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
			LOG.print(e);
		}
	}

	private byte[] getKeyFromString(String key, String salt) {
		LOG.print(this, "[SecurityClientUtil:getKeyFromString] START : key=" + key + "/salt=" + salt);

		String strData = null;

		byte[] byteData = null;
		byte[] byteTemp = null;

		strData = ((key==null)?"":key)
				+ ((salt==null)?"":salt)
				+ this.keyDefault;

		LOG.print(this, "[SecurityClientUtil:getKeyFromString] KEY DATA : " + strData);

		try {
			byteTemp = strData.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			//e1.printStackTrace();
			LOG.print(e1);

			strData = this.keyDefault;
			LOG.print(this, "[SecurityClientUtil:getKeyFromString] CHANGE KEY : " + strData);
			try {
				byteTemp = strData.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				byteTemp = null;
				LOG.print(this, "[SecurityClientUtil:getKeyFromString] Making Key Byte is Failed!");

				//e.printStackTrace();
				LOG.print(e);
			}
		}

		if (byteTemp != null) {
			LOG.print(this, "[SecurityClientUtil:getKeyFromString] KEY LENGTH : " + byteTemp.length);

			byteData = new byte[this.keyByteLength];
			System.arraycopy(
					byteTemp,
					0,
					byteData,
					0,
					this.keyByteLength);
		}

		return byteData;
	}

	private byte[] getKeyFromURL(String urlHost, int portHost, byte[] key) {
		SSLSocket socket = null;
		byte[] encodedPublicKey = null;
		byte[] byteData = null;
		int iPoint = 0;

		LOG.print(this, "[SecurityClientUtil:getKeyFromURL] START : urlHost=" + urlHost + "/portHost=" + portHost + "/key=" + ((key==null)?"NULL":String.valueOf(key.length)));

		if ((urlHost == null) ||
				(1 > urlHost.trim().length()) ||
				(portHost < 80) ||
				(portHost > 30000)) {
			LOG.print(this, "[SecurityClientUtil:getKeyFromURL] Invalid URL = " + urlHost + ":" + portHost);

			return key;
		}

		LOG.print(this, "[SecurityClientUtil:getKeyFromURL] Getting Public Key = " + urlHost + ":" + portHost);

		try {
			socket = (SSLSocket)((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket(urlHost, portHost);
			socket.startHandshake();
			encodedPublicKey = (((X509Certificate)(socket.getSession()).getPeerCertificates()[0]).getPublicKey()).getEncoded();
		} catch (UnknownHostException e1) {
			encodedPublicKey = null;
			LOG.print(this, "[SecurityClientUtil:getKeyFromURL] Getting Public Key is Failed : Unknown Host - " + e1.getMessage());
			//e1.printStackTrace();
		}  catch (SSLPeerUnverifiedException e1) {
			encodedPublicKey = null;
			LOG.print(this, "[SecurityClientUtil:getKeyFromURL] Getting Public Key is Failed : Failed Getting Certificate - " + e1.getMessage());
			//e1.printStackTrace();
		} catch (IOException e1) {
			encodedPublicKey = null;
			LOG.print(this, "[SecurityClientUtil:getKeyFromURL] Getting Public Key is Failed : Failed Create Socket - " + e1.getMessage());
			//e1.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					//e.printStackTrace();
				} finally {
					socket = null;
				}
			}
		}

		if ((encodedPublicKey == null) ||
				(encodedPublicKey.length < (this.keyURLLength + this.keyURLStartPoint))) {
			LOG.print(this, "[SecurityClientUtil:getKeyFromURL] Invalid Public Key");

			return key;
		}

		if (key != null) {
			LOG.print(this, "[SecurityClientUtil:getKeyFromURL] Making Key Point");

			for (int i=0; i<key.length; i++) {
				iPoint += key[i];
			}

			LOG.print(this, "[SecurityClientUtil:getKeyFromURL] Change Point with Key : " + iPoint);

			iPoint %= this.keyURLLength;

			LOG.print(this, "[SecurityClientUtil:getKeyFromURL] Change Point with Valid Length : " + iPoint);

			if (iPoint > (this.keyURLLength - this.keyByteLength)) {
				iPoint = (this.keyURLLength - this.keyByteLength);
			} else if (iPoint < 0) {
				iPoint *= (-1);
			}

			iPoint += this.keyURLStartPoint;

			LOG.print(this, "[SecurityClientUtil:getKeyFromURL] Latest Point : " + iPoint);
		}

		byteData = new byte[this.keyByteLength];
		System.arraycopy(encodedPublicKey, iPoint, byteData, 0, this.keyByteLength);

		LOG.print(this, "[SecurityClientUtil:getKeyFromURL] END : " + byteData.length);

		return byteData;
	}

	public String Encrypt(String urlHost, int portHost, String key, String salt, String data) {
		this.setKey(urlHost, portHost, key, salt);
		return this.Encrypt(data);
	}

	public String Encrypt(String data) {
		if(SSRConfig.getInstance().CONTEXT_ENCRYPT) {
			return Run(data, true);
		} else {
			return data;
		}
	}

	public String Decrypt(String urlHost, int portHost, String key, String salt, String data) {
		this.setKey(urlHost, portHost, key, salt);
		return this.Decrypt(data);
	}

	public String Decrypt(String data) {
		if(SSRConfig.getInstance().CONTEXT_ENCRYPT) {
			return Run(data, false);
		} else {
			return data;
		}
	}

	public String Run(String data, boolean encFlag) {
		byte[] byteResult = null;
		byte[] byteData = null;
		byte[] byteTemp = null;
		byte[] byteTempResult = null;
		int encSize = 0;
		int dataSize = 0;
		int iBreak = 0;

		try {
//			if (Rs.IS_EMUL) {
//				LOG.print(this, "[SecurityClientUtil:Run] START : data=" + data + "||");
//			}

			if ((this.keyByte == null) ||
					(data == null) ||
					(1 > data.trim().length())) {
//				if (Rs.IS_EMUL) {
//					LOG.print(this, "[SecurityClientUtil:Run] Invalid Data : " + data);
//				}
				return data;
			}

			if (encFlag) {
				try {
					byteData = data.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					LOG.print(this, "[SecurityClientUtil:Run] Getting Data Byte is Failed : " + e.getMessage());
					//e.printStackTrace();
					return data;
				}
			} else {
				try {
					byteData = Base64.decode(data);
				} catch (Exception e) {
					//e.printStackTrace();
					return data;
				}
			}

			dataSize = byteData.length;
			iBreak = EncSize(dataSize);
			encSize = (iBreak/16);

//			if (Rs.IS_EMUL) {
//				LOG.print(this, "[SecurityClientUtil:Run] Data Size : " + dataSize + " / Encrypt Byte Size : " + encSize);
//			}


			byteResult = new byte[iBreak];

			for (int i=0; i<encSize; i++) {
				byteTemp = new byte[16];
				byteTempResult = new byte[16];

				for (int j=0; j<16; j++) {
					if((16 * i + j) > (dataSize - 1)) {
						byteTemp[j] = 0;
					} else {
						byteTemp[j] = byteData[16 * i + j];
					}
				}
				if (encFlag) {
					this.rijndael.encrypt(byteTemp, byteTempResult);
				} else {
					this.rijndael.decrypt(byteTemp, byteTempResult);
				}

				for(int j=0; j<16; j++) {
					byteResult[16 * i + j] = byteTempResult[j];

					if (((byteTempResult[j] == 0) ||
							(byteTempResult[j] == 10)) &&
							(iBreak > (16 * i + j))) {
						iBreak = (16 * i + j);
//						if (Rs.IS_EMUL) {
//							LOG.print(this, "[SecurityClientUtil:Run] BREAK : " + iBreak + "/" + byteTempResult[j]);
//						}
					}
				}
			}

			if (encFlag) {
//				if (Rs.IS_EMUL) {
//					LOG.print(this, "[SecurityClientUtil:Run] END : [" + Base64.encode(byteResult) + "]");
//				}
				return Base64.encode(byteResult);
			} else {
				byteTempResult = new byte[iBreak];
				System.arraycopy(byteResult, 0, byteTempResult, 0, iBreak);
				String result = null;
				try {
					result = new String(byteTempResult, "UTF-8");
//					if (Rs.IS_EMUL) {
//						LOG.print(this, "[SecurityClientUtil:Run] END : [" + result + "]");
//					}
				} catch (UnsupportedEncodingException e) {
					result = new String(byteTempResult);
					LOG.print(this, "[SecurityClientUtil:Run-UnsupportedEncodingException] END : [" + result + "]");
				}
				return result;
			}
		} finally {
			byteResult = null;
			byteData = null;
			byteTemp = null;
			byteTempResult = null;
		}
	}

	public int EncSize(int i) {
        int j = i / 16;
        int k = i % 16;
        if(k > 0)
            j++;
        i = j * 16;
        return i;
    }

//	public static void main(String[] args) {
//		try {
//			String plain_text = "한글";
//			String cipher_test = SecurityClientUtil.getInstance().Encrypt(plain_text);
//			System.out.println("plain_text :: " + plain_text);
//			System.out.println("cipher_test :: " + cipher_test);
//
//			String js_text = "XQKsni1m1laQIw==";
//			String js_plain_text = SecurityClientUtil.getInstance().Decrypt(js_text);
//			System.out.println("js_text :: " +  js_text);
//			System.out.println("js_plain_text :: " +  js_plain_text);
//
//			System.out.println(">>>>>>>>>>>  " + plain_text.equals(js_plain_text));
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
}
