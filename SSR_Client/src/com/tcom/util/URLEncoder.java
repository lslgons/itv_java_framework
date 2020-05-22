package com.tcom.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

public class URLEncoder {
    static BitSet dontNeedEncoding;
    static final int caseDiff = ('a' - 'A');

    static {
    	dontNeedEncoding = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            dontNeedEncoding.set(i);
        }
        dontNeedEncoding.set(' ');
        dontNeedEncoding.set('-');
        dontNeedEncoding.set('_');
        dontNeedEncoding.set('.');
        dontNeedEncoding.set('*');
    }

    private URLEncoder() { }

    public static String encode(String s) {

        String str = null;

        try {
            str = encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        return str;
    }

    public static String encode(String s, String enc) throws UnsupportedEncodingException {

        boolean needToChange = false;
        boolean wroteUnencodedChar = false;
        int maxBytesPerChar = 10;

        LOG.print("encode target string : " +s);
        if(s == null) {
        	//들어오는 값이 없을경우 빈 스트링객체 전달
        	return "";
		}
        StringBuffer out = new StringBuffer(s.length());
        ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);
        OutputStreamWriter writer = new OutputStreamWriter(buf, enc);

        for (int i = 0; i < s.length(); i++) {
        	int c = s.charAt(i);
			if (dontNeedEncoding.get(c)) {
				if (c == ' ') {
					c = '+';
					needToChange = true;
				}
				out.append((char)c);
				wroteUnencodedChar = true;
			}
			else {
				try {
					if (wroteUnencodedChar) {
						writer = new OutputStreamWriter(buf, enc);
						wroteUnencodedChar = false;
					}

					writer.write(c);

					if (c >= 0xD800 && c <= 0xDBFF) {
						if ( (i+1) < s.length()) {
							int d = s.charAt(i+1);
							if (d >= 0xDC00 && d <= 0xDFFF) {
								writer.write(d);
								i++;
							}
						}
					}
					writer.flush();
				} catch (IOException e) {
					buf.reset();
					continue;
				}

				byte[] ba = buf.toByteArray();
				for (int j = 0; j < ba.length; j++) {
					out.append('%');
					char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
					if (Character.isLetter(ch)) {
						ch -= caseDiff;
					}
					out.append(ch);

					ch = Character.forDigit(ba[j] & 0xF, 16);
					if (Character.isLetter(ch)) {
						ch -= caseDiff;
					}
					out.append(ch);
				}
				buf.reset();
				needToChange = true;
			}
        }
        return (needToChange? out.toString() : s);
    }
}
