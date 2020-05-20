package com.landman.util;

import com.cj.tvui.Constants;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * zip파일 압축 유틸리티
 */
public class ZipParser {
	String fileName;
	
	Map stringData;
	Map byteData;
	
	public ZipParser(String fileName, boolean isRemote) {
		this.fileName = fileName;
		stringData = new HashMap();
		byteData = new HashMap();
		
		if (isRemote) {
			parseZipFileInHost(this.fileName);
		} else {
			parseZipFile(this.fileName);
		}
		
	}
	
	public Set getStringFileName() {
		return stringData.keySet();
	}
	
	public Set getByteFileName() {
		return byteData.keySet();
	}
	
	public String getString(String filename) {
		return (String)stringData.get(filename);
	}
	
	public byte[] getBytes(String filename) {
		return (byte[])byteData.get(filename);
	}
	
	
	/**
	 * In Host
	 */
	synchronized private void parseZipFileInHost(String address) {

        
        InputStream is = null;
        try {
            System.out.println("File Path : "+Constants.DCL_HOST+address);
            URL url = new URL(Constants.DCL_HOST+address);
            URLConnection urlConnection = url.openConnection();
            is = urlConnection.getInputStream();
            //byte[] buffer = new byte[1024];
            //int readBytes;
//            while ((readBytes = is.read(buffer)) != -1) {
//                fos.write(buffer, 0, readBytes);
//            }
        
    		//Hashtable ret = new Hashtable();
    		byte[] buf;
    		ZipEntry ze;
    		ByteArrayOutputStream baos = null;
    		ZipInputStream zis = null;
    		try {
    			baos = new ByteArrayOutputStream();
//    			if (file == null) {
//    				//return ret;
//    				return;
//    			}
    			zis = new ZipInputStream(is);
    			buf = new byte[1024]; // 1K
    			int r;
    			while ((ze = zis.getNextEntry()) != null) {
    				baos.reset();
    				while ((r = zis.read(buf)) != -1) {
    					baos.write(buf, 0, r);
    				}
    				baos.flush();

    				if (ze.getName().endsWith(".json") || ze.getName().endsWith(".txt")) {
    					//ret.put(ze.getName(), baos.toString("UTF-8"));
    					stringData.put(ze.getName(), baos.toString("UTF-8"));
    				} else {
    					//ret.put(ze.getName(), baos.toByteArray());
    					byteData.put(ze.getName(), baos.toByteArray());
    				}
    			}
    		} catch (Exception e) {
    			LOG.print(this, "parseZipFile(" + fileName + "), " + e.toString());
    		} finally {
    			try {
    				zis.close();
    			} catch (Exception ee) {
    			}
//    			try {
//    				fis.close();
//    			} catch (Exception ee) {
//    			}
    			try {
    				baos.close();
    			} catch (Exception ee) {
    			}
    			zis = null;
    			//fis = null;
    			baos = null;
    			ze = null;
    			buf = null;
    		}
    		//return ret;
    	    
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
//                if (fos != null) {
//                    fos.close();
//                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
	}
	
	
	/**
	 * zip파일 파싱
	 * @param fileName
	 * @return
	 */
	synchronized private void parseZipFile(String fileName) {
		//Hashtable ret = new Hashtable();
		byte[] buf;
		ZipEntry ze;
		ByteArrayOutputStream baos = null;
		FileInputStream fis = null;
		ZipInputStream zis = null;
		try {
			baos = new ByteArrayOutputStream();

			File file = new File(fileName);
//			if (file == null) {
//				//return ret;
//				return;
//			}

			fis = new FileInputStream(file);
			zis = new ZipInputStream(fis);
			buf = new byte[1024]; // 1K
			int r;
			while ((ze = zis.getNextEntry()) != null) {
				baos.reset();
				while ((r = zis.read(buf)) != -1) {
					baos.write(buf, 0, r);
				}
				baos.flush();

				if (ze.getName().endsWith(".json") || ze.getName().endsWith(".txt")) {
					//ret.put(ze.getName(), baos.toString("UTF-8"));
					stringData.put(ze.getName(), baos.toString("UTF-8"));
				} else {
					//ret.put(ze.getName(), baos.toByteArray());
					byteData.put(ze.getName(), baos.toByteArray());
				}
			}
		} catch (Exception e) {
			LOG.print(this, "parseZipFile(" + fileName + "), " + e.toString());
		} finally {
			try {
				zis.close();
			} catch (Exception ee) {
			}
			try {
				fis.close();
			} catch (Exception ee) {
			}
			try {
				baos.close();
			} catch (Exception ee) {
			}
			zis = null;
			fis = null;
			baos = null;
			ze = null;
			buf = null;
		}
		//return ret;
	}

}
