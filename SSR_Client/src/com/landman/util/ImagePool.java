package com.landman.util;

import com.cj.tvui.Constants;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * An image pool. This class caches images by name.
 */
public class ImagePool {
	private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
	private MediaTracker tracker;
	//private String uriBase;

	/* map from image filename to Image. */
	private Hashtable map = null;

	/**
	 * This constructor is required to call {@link #setComponent(Component)}
	 * before using the instance of this class.
	 * @param uriBase
	 */
	public ImagePool(/*String uriBase*/) {
		//this.uriBase = uriBase;
		this.map = new Hashtable();
	}

	/** Sets a {@link Component} instance, for which any image will be loaded. */
	public void setComponent(Component comp) {
		tracker = new MediaTracker(comp);
	}

	/**
	 * Returns an image which gets pixel data from the specified file
	 * relative to the URIBase with which this ImagePool is created.
	 *
	 * @param filename - image file name excluding URIBase specified in
	 *                  the constructor.
	 */
	public Image getImage(String filename) {
		if (filename == null || "".equals(filename)) { // Hashtable.get(null) throws NPE.
			return null;
		}

		if (filename.startsWith("http://")) {
			try {
				return getImage(filename, new URL(filename));
			} catch (MalformedURLException e) {
				return null;
			}
		}

		if (map == null) {
			return null;
		}
		/* FIXME: in fact, we have to cannonicalize the given pathname.
		   but, I don't mind. */
		Image image = (Image) map.get(filename);
		if (image != null) { /* Cache hit. */
			return image;
		}
		
		//LOG.debug(this, "getImage("+filename+")");

		//String url = uriBase + filename;
		String url = filename;
		if (Constants.IS_EMUL) {
			url = new File(url).getAbsolutePath();
		}

		/* To share the buffers used to read images. */
		synchronized (getClass()) {
			/* CAUTION: Each image must have its own imagedata.
			 * If imagedata changes, the corresponding image changes, too.
			 */
			image = toolkit.createImage(url);
			tracker.addImage(image, 0);
			try {
				tracker.waitForAll();
			} catch (InterruptedException e) {
				tracker.removeImage(image, 0);
				image = null;
			} finally {
				if (image != null) {
					tracker.removeImage(image, 0);
				}
				if (tracker.isErrorAny()) {
					image = null;
				}
			}
			if (image != null) {
				map.put(filename, image);
			}
			//map.put(filename, image);
			//tracker.addImage(image, 1);
		}
		return image;
	}

	public Image getImage2(String filename, URL url) {

		Image image = (Image) map.get(filename);
		if (image != null) { /* Cache hit. */
			return image;
		}

		/* To share the buffers used to read images. */
		synchronized (getClass()) {
			/* CAUTION: Each image must have its own imagedata.
			 * If imagedata changes, the corresponding image changes, too.
			 */
			image = toolkit.getImage(url);
			tracker.addImage(image, 1);
			try {
				tracker.waitForAll();
			} catch (InterruptedException e) {
				tracker.removeImage(image);
				image = null;
			} finally {
				if (image != null) {
					tracker.removeImage(image);
				}
				if (tracker.isErrorAny() || (image != null && image.getWidth(null) < 0)) {
					//Rs.debug(this,""+tracker.getErrorsAny());
					image.flush();
					image = null;
				}
			}
			if (image != null) {
				map.put(filename, image);
			}
		}
		return image;
	}

	public Image getImage(String filename, URL url) {
		if (filename == null) { // Hashtable.get(null) throws NPE.
			return null;
		}

		if (map == null) {
			return null;
		}
		/* FIXME: in fact, we have to cannonicalize the given pathname.
		   but, I don't mind. */
		Image image = (Image) map.get(filename);
		if (image != null) { /* Cache hit. */
			//Rs.print(this, "ImagePool getImage exist image return");
			return image;
		}

		/* To share the buffers used to read images. */
		synchronized (getClass()) {
			/* CAUTION: Each image must have its own imagedata.
			 * If imagedata changes, the corresponding image changes, too.
			 */
			byte[] byteChunk = byteImage(url, false);
			if (byteChunk == null) {
				return null;
			}
			image = toolkit.createImage(byteChunk);
			tracker.addImage(image, 1);
			try {
				tracker.waitForAll();
			} catch (InterruptedException e) {
				tracker.removeImage(image,1);
				image = null;
			} finally {
				tracker.removeImage(image,1);
				if (tracker.isErrorAny() || (image != null && image.getWidth(null) < 0)) {
					image.flush();
					image = null;
				}
				if (byteChunk != null) {
					byteChunk = null;
				}
			}
			if (image == null) {
				LOG.print("ImagePool getImage image null");
				return image;
			}
			map.put(filename, image);
			//tracker.addImage(image, 1);
		}
		return image;
	}

	// added by june
	public Image getImage(String filename, byte[] data) {
		if (filename == null || data == null || data.length <= 0) { // Hashtable.get(null) throws NPE.
			return null;
		}

		Image image = (Image) map.get(filename);
		if (image != null) { /* Cache hit. */
			//Rs.print("ImagePool getImage exist image return");
			return image;
		}

		/* To share the buffers used to read images. */
		synchronized (getClass()) {
			image = toolkit.createImage(data);
			if (image == null) {
				LOG.print("ImagePool getImage image null");
				return image;
			}
			map.put(filename, image);
			tracker.addImage(image, 1);
		}
		return image;
	}

	public Image getImage(byte[] data) {
		Image image = toolkit.createImage(data);
		return image;
	}

	public void flushImage(String filename) {
		if (filename == null) {
			return;
		}
		synchronized (getClass()) {
			Image image = (Image) map.get(filename);
			if (image != null) {
				tracker.removeImage(image);
				image.flush();
				image = null;
			}
			map.remove(filename);
		}
	}

	public void flushImage(Image img) {
		if (img == null) return;
		synchronized (getClass()) {
			if (map.containsValue(img)) {
				Iterator itr = map.entrySet().iterator();
				while (itr.hasNext()) {
					Entry entry = (Entry) itr.next();
					Image _img = (Image) entry.getValue();
					if (_img != null && _img.equals(img)) {
						String _key = entry.getKey().toString();
						tracker.removeImage(_img);
						LOG.debug(this, "flushImage(Image img) => key : " + _key);
						map.remove(_key);
						_img.flush();
						_img = null;
						return;
					}
				}
			}
		}
	}

	/**
	 * Flushes all images in this ImagePool.
	 */
	public void flush() {
		Enumeration keys = map.keys();
		Image _img;
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			_img = (Image) map.get(key);
			LOG.debug(this, "flush() => key : " + key);
			tracker.removeImage(_img);
			map.remove(key);
			_img.flush();
			_img = null;
		}
		map.clear();
		/*
		map = null;
		uriBase = null;
		if (tracker != null) {
			tracker = null;
		}
		*/
		//Runtime.getRuntime().gc();
		//System.gc();
	}

	/**
	 * Checks to see if all images in this ImagePool have
	 * finished loading. this method starts loading any images that are not
	 * yet being loaded.
	 * @return true if all images have finished loading, have been aborted,
	 *         or have encountered an error; false otherwise.
	 */
	public boolean checkAll() {
		return tracker.checkAll(true);
	}

	/**
	 * Waits for all images in this ImagePool.
	 */
	public void waitForAll() {
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

//	public Image loadImage(String url) {
//		try {
//			return loadImage(new URL(url));
//		} catch (MalformedURLException e) {
//			return null;
//		}
//	}
//
//	public Image loadImage(URL url) {
//		byte[] bytes = byteImage(url);
//		return (bytes == null) ? null : loadImage(bytes);
//	}

	public Image loadImage(byte[] data) {
		Image result = null;
		if (data != null) {
			synchronized (getClass()) {
				result = toolkit.createImage(data);
				if (result != null) {
					try {
						tracker.addImage(result, 1);
						tracker.waitForID(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						tracker.removeImage(result);
					}
				}
			}
		}
		return result;
	}

	public Image loadImage(Component comp, byte[] data) {
		Image result = null;
		if (data != null) {
			synchronized (getClass()) {
				result = toolkit.createImage(data);
				if (result != null) {
					MediaTracker tracker = new MediaTracker(comp);
					try {
						tracker.addImage(result, 1);
						tracker.waitForID(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						tracker.removeImage(result);
					}
				}
			}
		}
		return result;
	}
	//*
	public byte[] byteImage(String url) throws Exception {
		try {
			if (url == null || "".equals(url)) return null;
			return byteImage(new URL(url), true);
		} catch (MalformedURLException e) {
			return null;
		}
	}
	//*/
	private byte[] byteImage(URL url, boolean reTry) {
		//Rs.debug(this, "byteImage(URL " + url + ")");
		OutputStream bais = null;
		InputStream is = null;
		byte[] buf = null;

		//URLConnection urlc = null;
		HttpURLConnection urlc = null;

		long startTime = System.currentTimeMillis();
		try {

//			urlc = url.openConnection();
			urlc = (HttpURLConnection)url.openConnection();
			urlc.setRequestMethod("GET");

			//LOG.print(this, "ResponseCode : " + urlc.getResponseCode());

			if (urlc.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}

			//			urlc.setUseCaches(false);
			is = urlc.getInputStream();

			bais = new ByteArrayOutputStream();
			//is = url.openStream();
			buf = new byte[4096]; // Or whatever size you want to read in at a time.
			int len = -1;
			while ((len = is.read(buf)) > 0) {
				bais.write(buf, 0, len);
			}
			return ((ByteArrayOutputStream) bais).toByteArray();
//		} catch (SocketTimeoutException se) {  //SocketTimeoutException을 딜라이브(C&M) LG LSC-230모델에서는 지원하지 않음
//			if (reTry) {
//				Util.sleep(100);
//				return byteImage(url, false);
//			} else {
//				Rs.print(this, se);
//			}
//			return null;
		} catch (IOException e) {
			LOG.debug(this, "Failed while reading bytes from " + url.toExternalForm());// + ":" + e.getMessage());
			//Rs.print(e);
			e.printStackTrace();
			// Perform any other exception handling that's appropriate.
			return null;
		} finally {
			//LOG.print(this, " byteImage running time : " + (System.currentTimeMillis() - startTime));
			if (urlc != null) {
				try {
					urlc.disconnect();
				} catch (Exception e2) {
				}
				urlc = null;
			}
			if (buf != null) {
				buf = null;
			}
			try {
				if (is != null) is.close();
			} catch (Exception e) {
			} finally {
				is = null;
			}
			try {
				if (bais != null) {
					bais.flush();
					bais.close();
				}
			} catch (Exception e) {
			} finally {
				bais = null;
			}
		}
	}
	
	//daegon - get Image count
	public int getLoadedImageCount() {return map.size();}
	public Set getImagePath() {return map.keySet();}
}