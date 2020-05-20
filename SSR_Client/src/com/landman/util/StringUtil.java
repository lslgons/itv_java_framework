package com.landman.util;

import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * 스트링 값 가공, 편집 유틸리티
 */
public class StringUtil {

    /** 말 줄임표. */
    public static final String ELLIPSIS = "..";
    private static final String EMPTY_STRING = "";

    /**
     * 문자열 길이 반환
     * @param src
     * @param fm
     * @return
     */
    public static int getStringWidth(String src, FontMetrics fm) {
        return fm.stringWidth(src);
    }

    /**
     * 말줄임 표시
     * @param src
     * @param fm
     * @param width
     * @return
     */
    public static String shorten(String src, FontMetrics fm, int width) {
        return shorten(src,fm,width,ELLIPSIS);
    }

    /**
     * 말줄임 표시
     * @param src
     * @param fm
     * @param width
     * @param ellipsis
     * @return
     */
    public static String shorten(String src, FontMetrics fm, int width,String ellipsis) {
        if (src == null || src.length() <= 0) {
            return EMPTY_STRING;
        }
        if (fm.stringWidth(src) <= width) {
            return src;
        }
        int ew = fm.stringWidth(ellipsis);
        int len;
        do {
            len = src.length();
            if (len <= 1) {
                return (width >= ew) ? ellipsis : EMPTY_STRING;
            }
            src = src.substring(0, len - 1);
        } while (fm.stringWidth(src) + ew > width);
        return src + ellipsis;
    }
    /**
     * 긴 한줄의 text를 여러줄로 나눈다.
     * @param src 원본 String
     * @param fm 그릴 string의 font metrics
     * @param width 줄바꿈 기준폭
     * @param byWord 단어 기준 줄바꿈 여부
     */
    public static String[] split(String src, FontMetrics fm, int width, boolean byWord) {
        if (src == null) {
            return new String[0];
        }
        Vector lines = null;
        try {
            lines = new Vector();
            StringBuffer strbuf = new StringBuffer();
            for (int i = 0; i < src.length(); i++) {
                if (src.charAt(i) == '\n') {
                    addLineAfterWrap(lines, strbuf.toString(), fm, width, byWord);
                    strbuf = new StringBuffer();
                } else {
                    strbuf.append(src.charAt(i));
                }
            }
            addLineAfterWrap(lines, strbuf.toString(), fm, width, byWord);

            String[] dummy = new String[lines.size()];
            for (int i = 0; i < lines.size(); i++) {
                dummy[i] = (String) lines.elementAt(i);
            }
            return dummy;
        } finally {
            if (lines != null) {
                lines.clear();
                lines = null;
            }
        }
    }

    private static void addLineAfterWrap(Vector lines, String string, FontMetrics fm, int lineWidth, boolean byWord) {
        StringBuffer org = new StringBuffer(string);
        StringBuffer str = new StringBuffer();
        try {
            int firstIndex = 0;
            int lastBlank = -1;
            int fw;
            char c;
            int strlen = org.length();
            for (int i = 0; i < strlen; i++) {
                c = org.charAt(i);
                str = str.append(c);
                fw = fm.stringWidth(str.toString());

                if (byWord && (c == ' ')) {//if (byWord && (c == '\t' || c == ' ' || c == '\n')) {
                    // 이 부분을 커멘트 처리하지 않으면, 빈칸 기준으로 나눈다.
                    lastBlank = i;
                }
                if (fw > lineWidth) {
                    if (lastBlank >= firstIndex) {
                        // to the next line
                        lines.addElement(string.substring(firstIndex, lastBlank));
                        firstIndex = lastBlank + 1;
                        str = new StringBuffer(string.substring(firstIndex, i + 1));
                    } else if (i > 0) {
                        // split word
                        lines.addElement(string.substring(firstIndex, i));
                        firstIndex = i;
                        str = new StringBuffer();
                        // 이 조건문도 마찬가지
                        if (c != '\t' && c != ' ') {
                            str.append(c);
                        }
                    }
                }
            }
            String lastStr = string.substring(firstIndex, strlen);
            if (fm.stringWidth(lastStr) > lineWidth) {
                lines.addElement(string.substring(firstIndex, strlen - 1));
                lines.addElement(string.substring(strlen - 1));
            } else {
                lines.addElement(lastStr);
            }
        } finally {
            org = null;
            str = null;
        }
    }

    public static String[] tokenize(String str, String delim) {
        Vector v = null;
        try {
            v = new Vector();
            String s = str;
            String[] ret = null;
            if (s != null && s.length() > 0) {
                int p;
                while ((p = s.indexOf(delim)) != -1) {
                    if (p == 0) {
                        v.addElement("");
                    } else {
                        v.addElement(s.substring(0, p));
                    }
                    s = s.substring(p + 1);
                }
                v.add(s);
                ret = new String[v.size()];
                ret = (String[]) v.toArray(ret);
            }
            return ret;
        } finally {
            if (v != null) {
                v.clear();
                v = null;
            }
        }
    }

    public static String[] tokenize(String str, int size) {
        Vector v = null;
        try {
            v = new Vector();
            String s = str;
            String[] ret = null;
            if (s != null && s.length() > 0) {
                while (s.length() > size) {
                    v.addElement(s.substring(0, size));
                    s = s.substring(size);
                }
                v.add(s);
                ret = new String[v.size()];
                ret = (String[]) v.toArray(ret);
            }
            return ret;
        } finally {
            if (v != null) {
                v.clear();
                v = null;
            }
        }
    }
    
    /**
     * 만나이 계산
     * @param date
     * @return
     */
    public static int getMAge(Date date) {
        int m_year = 0;
        try {
            String i_date = StringUtil.formatDate(date, "yyyyMMdd");
            String m_date = DateUtil.getDateString("yyyyMMdd");
            m_year = Integer.parseInt(m_date.substring(0, 4), 10) - Integer.parseInt(i_date.substring(0, 4), 10);
            if (Integer.parseInt(m_date.substring(4, 8), 10) < Integer.parseInt(i_date.substring(4, 8), 10)) {
                m_year -= 1;
            }
        } catch (Exception e) {
        }
        return m_year;
    }


    public static String formatDate(Date date, String pattern) {
        if (date == null) return "";
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String formatDate(String dateStr, String pattern) {
        if (dateStr == null || "".equals(dateStr)) return "";
        return formatDate(DateUtil.getDate(dateStr), pattern);
    }

    public static int getDiffDays(Date fromDate, Date toDate) {
        double d_remain_time = toDate.getTime() - fromDate.getTime();
        return (int) (d_remain_time / 24 / 60 / 60 / 1000);
    }

    /** 날짜를 formatting (8.18)
     * type 1 : 2007.01.01
     * type 2 : 2007/01/01
     * type 3 : 01/01
     * type 4 : 1.01
     * */
    public static String getDateString2(String date, int type) {
        String reDate = date;
        if (date.length() == 8) {
            try {
                switch (type) {
                    case 1:
                        reDate = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6);
                        break;
                    case 2:
                        reDate = date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6);
                        break;
                    case 3:
                        reDate = date.substring(4, 6) + "/" + date.substring(6);
                        break;
                    case 4:
                        reDate = Integer.parseInt(date.substring(4, 6)) + "/" + Integer.parseInt(date.substring(6));
                        break;
                }
            } catch (Exception e) {
            }
        }
        return reDate;
    }

    /**
     * 현재 시간 스케줄인지 반환
     * @param startDt
     * @param endDt
     * @param pattern
     * @return
     */
    public static boolean isCurrentSchedule(String startDt, String endDt, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return isCurrentSchedule(dateFormat.parse(startDt), dateFormat.parse(endDt));
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 현재 시간 스케줄인지 반환
     * @param startDt
     * @param endDt
     * @return
     */
    public static boolean isCurrentSchedule(Date startDt, Date endDt) {
        long cur_time = (new Date()).getTime(), start_time = startDt.getTime(), end_time = endDt.getTime();

        if (start_time >= end_time) return false;
        return (cur_time >= start_time && cur_time < end_time);
    }

    public static String addStr(String s, String s2) {
        if (s == null || s.length() == 0) {
            return s;
        } else {
            return s + s2;
        }
    }

    /**
     * 입력받은 date 문자열을 tagPattern 으로 변환
     * @param date - 날짜 문자열
     * @param orgPattern - 원본 날짜 포멧
     * @param tagPattern - 리턴할 날짜 포멧
     * @return
     */
    public static String formatDate(String date, String orgPattern, String tagPattern) {
        String rslt = "";
        try {
            if (!isEmpty(date) && !isEmpty(orgPattern) && !isEmpty(tagPattern)) {
                rslt = formatDate(new SimpleDateFormat(orgPattern).parse(date), tagPattern);
            }
        } catch (ParseException e) {
        }
        return rslt;
    }

    /** 돈에 , 찍기 */
    public static String format(long number) {
        if (number < 0) {
            return '-' + format(-number);
        } else if (number < 1000) {
            return "" + number;
        } else {
            return format(number / 1000) + ',' + ((number % 1000 + 1000) + "").substring(1);
        }
    }

    public static String format(String str) {
        if (str == null) {
            return "";
        }
        try {
            if (str.indexOf(".") == -1) {
                return format(Long.parseLong(str));
            } else {
                return format(Long.parseLong(str.substring(0, str.indexOf(".")))) + str.substring(str.indexOf("."));
            }
        } catch (Exception e) {
            if (str.indexOf(",") != -1) { // ,를 갖고 있다면 이미 x,xxx 형식일 수 있다 - Yi, Kooksun
                return str;
            }
            return "0";
        }
    }

    /** 가격등을 포맷대로 반환 */
    public static String formatNumber(int val, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(val);
    }

    /** 20060409 => 2006.04.09 */
    public static String formatDate(String s) {
        String str = "";
        if (s != null && s.length() == 8) {
            str = s.substring(0, 4) + '.' + s.substring(4, 6) + '.' + s.substring(6);
        } else {
            str = s;
        }
        return str;
    }

    /** Password String을 *로 숨김 */
    public static String password(String string) {
        if (string == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            sb.append("●");
        }
        return sb.toString();
    }

    /** card 번호 세번째, 네번째를  *로 숨김 */
    public static String hiddenCardNum(String cardNum) {
        if(cardNum == null || "".equals(cardNum)) return "";
        if(cardNum.length() == 16) return getCardNumberFormat(cardNum);
        String tmpCardNum = "";
        String[] tmp = tokenize(cardNum, "-");
        String pass = "";
        if (tmp != null && tmp.length == 4) {
            if (tmp[2] != null && tmp[2].length() == 4) {
                for (int i = 0; i < tmp.length; i++) {
                    if (i > 1) {
                        pass = "";
                        for (int j = 0; j < tmp[i].length(); j++) {
                            pass += "●";
                        }
                        tmp[i] = pass;
                    }
                }
                for (int i = 0; i < tmp.length; i++) {
                    tmpCardNum += tmp[i] + "-";
                }
                tmpCardNum = tmpCardNum.substring(0, tmpCardNum.length() - 1);
            }
        }
        return tmpCardNum;
    }
    /**
     * card 번호 두번째, 네번째를  *로 숨김
     * @param cardNum
     * @return
     */
    public static String hiddenCardNum2(String cardNum) {
        if(cardNum == null || "".equals(cardNum)) return "";
        if(cardNum.length() == 16) return getCardNumberFormat2(cardNum);
        String tmpCardNum = "";
        String[] tmp = tokenize(cardNum, 4);
        String pass = "";
        if (tmp != null && tmp.length == 4) {
            if (tmp[1] != null && tmp[1].length() == 4) {
                for (int i = 0; i < tmp.length; i++) {
                    if (i == 1 || i > 2) {
                        pass = "";
                        for (int j = 0; j < tmp[i].length(); j++) {
                            pass += "●";
                        }
                        tmp[i] = pass;
                    }
                }
                for (int i = 0; i < tmp.length; i++) {
                    tmpCardNum += tmp[i] + "-";
                }
                tmpCardNum = tmpCardNum.substring(0, tmpCardNum.length() - 1);
            }
        }
        return tmpCardNum;
    }

    /** String의 값이 뒷자리부터 delete */
    public static String delete(String s) {
        if (s == null) return "";

        int len = s.length();
        if (len > 0) {
            return s.substring(0, len - 1);
        }
        return s;
    }

    public static long delete(long s) {
        return s / 10;
    }

    public static int indexOf(Object[] array, Object item, int error) {
        if (array == null || item == null) {
            return error;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && array[i].equals(item)) {
                return i;
            }
        }
        return error;
    }

    public static int indexOf(int[] array, int item, int error) {
        if (array == null) {
            return error;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == item) {
                return i;
            }
        }
        return error;
    }

    /** 각 string에서 하이픈 빼기
     * jek */
    public static String makeNoHyphenString(String s) {
        if (s == null) return "";
        if (s.indexOf("-") == -1) return s;

        String[] s1 = tokenize(s, "-");
        String ret = "";
        for (int i = 0; i < s1.length; i++) {
            ret = ret + s1[i];
        }

        return ret;
    }

    /**
     * key code를 key string으로 변환하는 함수
     * @param keyType
     * @return key string
     */
    public static String toKeyTypeStr(int keyType) {
        switch (keyType) {
            case KeyEvent.KEY_PRESSED:
                return "pressed";
            case KeyEvent.KEY_RELEASED:
                return "released";
            case KeyEvent.KEY_TYPED:
                return "typed";
        }
        return "Unknown";
    }

    public static String replaceNull(String tmp) {
        if (tmp == null) {
            tmp = "";
        }
        return tmp;
    }

    public static String replaceEmpty(String tmp, String defVal) {
        if (isEmpty(tmp)) {
            return defVal;
        }
        return tmp;
    }

    public static int parseInt(String tmp) {
        int result = 0;
        if (tmp != null) {
            try {
                result = Integer.parseInt(tmp);
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * ServiceUsage 용 현재 시간 얻어온다.
     * @param currentTime long
     * @return String
     */
    public static String getCurrentTime(long currentTime) {
        SimpleDateFormat simleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simleDateFormat.format(new Date(currentTime));
    }

    public static boolean isNotEmpty(String name) {
        return !isEmpty(name);
    }

    public static boolean isEmpty(String name) {
        return name == null || "".equals(name) || "".equals(name.trim());
    }

    public static String getCardNumberFormat(String cardNo) {
        if (cardNo == null) return "";
        String result = "";
        if (cardNo.length() == 16) {
            result += cardNo.substring(0, 4);
            result += "-";
            result += "●●●●";
            result += "-";
            result += "●●●●";
            result += "-";
            result += cardNo.substring(12, 16);
        } else {
            result = cardNo;
        }
        return result;
    }

    public static String getCardNumberFormat2(String cardNo) {
        if (cardNo == null) return "";
        String result = "";
        if (cardNo.length() == 16) {
            result += cardNo.substring(0, 4);
            result += "-";
            result += "●●●●";
            result += "-";
            result += cardNo.substring(8, 12);
            result += "-";
            result += "●●●●";
        } else {
            result = cardNo;
        }
        return result;
    }

    public static String getAcntNumberFormat(String cardNo) {
        if (cardNo == null) return "";
        String result = "";
        if (cardNo.length() == 16) {
            result += "●●●●";
            result += "-";
            result += "●●●●";
            result += "-";
            result += cardNo.substring(8, 12);
            result += "-";
            result += cardNo.substring(12, 16);
        } else if (cardNo.length() >8) {
            result += "●●●●";
            result += "-";
            result += "●●●●";
            result += "-";
            result += cardNo.substring(8, cardNo.length());
        } else {
            result = cardNo;
        }
        return result;
    }

    public static String getPhoneNumberFormat(String phoneNo) {
        if (phoneNo == null) return "";
        String result = "";
        if (phoneNo.length() >4 ) {
            result += phoneNo.substring(0,phoneNo.length()-4);
            result += "●●●●";
        } else {
            result = phoneNo;
        }
        return result;
    }

    /**
     * 두 날짜사이의 차이를 반환
     * @param begin
     * @param end
     * @return
     * @throws Exception
     */
    public static long getDiffOfDate(String begin, String end, String pattern) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date beginDate = dateFormat.parse(begin);
        Date endDate = dateFormat.parse(end);
        return endDate.getTime() - beginDate.getTime();
    }

    public static long getDiffOfDate(String end, String pattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Date endDate = dateFormat.parse(end);
            return endDate.getTime() - new Date().getTime();
        } catch (ParseException pe) {
            return -1;
        }
    }

    public static String zeroPrefix(int arg0, int size) {
        return zeroPrefix(String.valueOf(arg0), size);
    }
    public static String zeroPrefix(String arg0, int size) {
        String result = arg0;
        if (result.length() >= size) return result;
        result = "0000000000000" + result;
        return result.substring(result.length() - size);
    }

    public static String getDiffOfDateToTimeStr(String end, String pattern) {
        try {
            double d_remain_time = (double) getDiffOfDate(end, pattern) / 60 / 60 / 1000;
            if (d_remain_time < 0) {
                return "--:--:--";
            }
            int hh = 0;
            int mm = 0;
            int ss = 0;
            if (d_remain_time > 0) {
                hh = (int) (d_remain_time); // 시간
                d_remain_time = (d_remain_time - hh) * 60;
                mm = (int) (d_remain_time); // 분
                d_remain_time = (d_remain_time - mm) * 60;
                ss = (int) (d_remain_time); // 초
            }
            return zeroPrefix(hh, 2) + ":" + zeroPrefix(mm, 2) + ":" + zeroPrefix(ss, 2);
        } catch (Exception e) {
            return "--:--:--";
        }
    }

    /**
     * 쿠폰리스트에서 쿠폰의 남은유효기간(시간)을 가져오기위해 사용
     * 만료 날짜와 현재시간의 차이를 반환
     * @param end
     * @param pattern
     * @return
     * @throws Exception
     */
    public static String getDiffOfDateToTimeStr2(String end, String pattern) {
        try {
            if ("yyyyMMdd".equals(pattern)) {
                end = end + "232359";
                pattern = "yyyyMMddHHmmss";
            }
            double d_remain_time = (double) getDiffOfDate(end, pattern) / 60 / 60 / 1000;
            if (d_remain_time < 0) {
                return "사용기간지남";
            }
            int day = 0;
            int hh = 0;
            day = (int)(d_remain_time / 24);
            hh = (int)(d_remain_time - (day * 24));
            if(day != 0) {
                if(day > 99) {
                    DecimalFormat df = new DecimalFormat("###,###");
                    String dfStr = df.format(day);
                    return dfStr+"일";
                } else {
                    return day+"일 "+hh+"시간";
                }
            } else {
                return hh+"시간";

            }

        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 문자열에서 해당문자를 원하는 문자로 변경한다.
     * @param org :	문자열
     * @param var :	변환할 문자열
     * @param tgt :	변환될 문자열
     * @return : 변환된	문자열
     */
    public static String replaceString(String org, String var, String tgt) {
        if (org == null) return "";
        StringBuffer temp = new StringBuffer(1024);
        int end = 0;
        int begin = 0;

        while (true) {
            end = org.indexOf(var, begin);
            if (end == -1) {
                end = org.length();
                temp.append(org.substring(begin, end));
                break;
            }
            temp.append(org.substring(begin, end)).append(tgt);
            begin = end + var.length();
        }
        return temp.toString();
    }

    public static String replaceStringTail(String org, String tgt) {
        if (org == null) return "";
        if (tgt == null) return org;

        int oLen = org.length(), tLen = tgt.length();
        if (oLen == 0 || tLen == 0 ) return org;
        if (oLen < tLen) {
            return org.substring(0, oLen - 1) + tgt;
        } else {
            return org.substring(0, oLen - tLen) + tgt;
        }
    }

    /**
     * 문자열 뒷부분을 치환
     * @param org
     * @param tgt
     * @return
     */
    public static String trimStringTail(String org, String tgt) {
        if (org == null) return "";
        if (tgt == null) return org;

        int oLen = org.length(), tLen = tgt.length();
        if (oLen == 0 || tLen == 0 ) return org;
        if (oLen < tLen) {
            //원본문자열이 치환될 문자열 보다 짧을 경우
            return org.substring(0, oLen - 1) + tgt;
        } else {
            return org.substring(0, oLen - tLen) + tgt;
        }
    }

    public static int inset(String string, String tokenized, String delim) {
        return inset(string, tokenize(tokenized, delim));
    }

    public static int inset(String string, String[] tokenized) {
        if (string == null || "".equals(string) || tokenized == null || tokenized.length == 0) return -1;
        for (int i = 0; i < tokenized.length; i++) {
            if (string.equals(tokenized[i])) return i;
        }
        return -1;
    }

    public static String capitalize(String str)
    {
        int strLen;
        if(str == null || (strLen = str.length()) == 0)
            return str;
        else
            return (new StringBuffer(strLen)).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    /** 휴대번호 앞자리 번호 */
    static private String[] mobile0Num = new String[] { "010", "011", "016", "017", "018", "019" };
    public static boolean checkMobile0(String hp0) {
        if (isEmpty(hp0)) return false;
        for (int i = 0; i < mobile0Num.length; i++) {
            if (mobile0Num[i].equals(hp0)) {
                return true;
            }
        }
        return false;
    }
    public static boolean checkMobile(String[] mobile) {
        if ((mobile[0].length() < 3 || StringUtil.checkMobile0(mobile[0])) == false) {
            return false;

        } else if (mobile[1].length() < 3) {
            return false;

        } else if (mobile[2].length() < 4) {
            return false;
        }

        return true;
    }

    public static int retIntValue(JSONObject src, Object key) {
        if (src == null || key == null) {
            return 0;
        }
        Object o = src.get(key);
        if (o != null && o instanceof String) {
            return StringUtil.isEmpty(String.valueOf(o)) ? 0 : new Integer(String.valueOf(o)).intValue();
        }
        return (o != null && !"".equals(o)) ? ((Number) o).intValue() : 0;
    }

    public static long retLongValue(JSONObject src, Object key) {
        if (src == null || key == null) {
            return 0;
        }

        Object o = src.get(key);
        if (o != null && o instanceof String) {
            return StringUtil.isEmpty(String.valueOf(o)) ? 0 : new Long(String.valueOf(String.valueOf(o))).longValue();
        }
        return o != null && !"".equals(o) ? ((Number) o).longValue() : 0;
    }

    public static Integer retIntegerValue(JSONObject src, Object key) {
        if (src == null || key == null) {
            return new Integer(0);
        }

        Object o = src.get(key);
        if (o != null && o instanceof String) {
            return StringUtil.isEmpty(String.valueOf(o)) ? new Integer(0) : new Integer(String.valueOf(o));
        }
        return o != null && !"".equals(o) ? new Integer(((Number) o).intValue()) : new Integer(0);
    }

    public static String retStringValue(JSONObject src, Object key) {
        if (src == null || key == null) {
            return "";
        }

        Object o = src.get(key);
        if (o == null) {
            return "";
        } else {
            if (o instanceof String) {
                return (String) o;
            } else {
                return String.valueOf(o);
            }
        }
    }

    public static boolean retBooleanValue(JSONObject src, Object key) {
        if (src == null || key == null) {
            return false;
        }

        Object o = src.get(key);
        return o != null ? ((Boolean) o).booleanValue() : false;
    }

    /**
     * 초를 mm:ss 으로 변환
     * @param sec
     * @return
     */
    public static String formatVodTime(int sec) {
        String retStr = "00:00";
        int m = 0, s = 0;
        if (sec > 0) {
            m = (int)Math.floor((sec) / 60D);
            s = sec % 60;
            retStr = zeroPrefix(m, 2) + ":" + zeroPrefix(s, 2);
        }
        return retStr;
    }


}
