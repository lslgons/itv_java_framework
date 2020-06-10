package com.tcom.drawer;

import org.json.simple.JSONArray;

import java.awt.*;

public class DefinedColor {

    public static Color C_EDEDED = Color.decode("#ededed");
    //	public static Color C_FF3B5B = Color.decode("#ff3b5b");
    public static Color C_FF5B5B = Color.decode("#ff5b5b");
    public static Color C_EDDFD2 = Color.decode("#eddfd2");
    public static Color C_D6D4C1 = Color.decode("#d6d4c1");
    public static Color C_171717 = Color.decode("#171717");
    public static Color C_343434 = Color.decode("#343434");
    public static Color C_D2D2D2 = Color.decode("#d2d2d2");
    public static Color C_AAAAAA = Color.decode("#aaaaaa");

    public static Color C_AAAAAA_40 = decodeColor("#aaaaaa", 40);

    /** Color */
    public static Color C_000000 = Color.decode("#000000");
    public static Color C_FFFFFF = Color.decode("#ffffff");
    public static Color C_C84635 = Color.decode("#C84635");
    public static Color C_DDDDDD = Color.decode("#dddddd");
    public static Color C_FFB400 = Color.decode("#ffb400");
    public static Color C_666666 = Color.decode("#666666");
    public static Color C_FF5075 = Color.decode("#ff5075");
    public static Color C_5D43CF = Color.decode("#5d43cf");
    public static Color C_313131 = Color.decode("#313131");
    public static Color C_303030 = Color.decode("#303030");
    public static Color C_1C1C1C = Color.decode("#1c1c1c");
    public static Color C_111111 = Color.decode("#111111");
    public static Color C_999999 = Color.decode("#999999");
    public static Color C_888888 = Color.decode("#888888");
    public static Color C_555555 = Color.decode("#555555");
    public static Color C_4381CF = Color.decode("#4381cf");
    public static Color C_3C3C3C = Color.decode("#3c3c3c");
    public static Color C_333333 = Color.decode("#333333");
    public static Color C_1D1D1d = Color.decode("#1d1d1d");
    public static Color C_7C7C7C = Color.decode("#1d1d1d");
    public static Color C_FF5050 = Color.decode("#ff5050");
    public static Color C_EEEEEE = Color.decode("#eeeeee");
    public static Color C_444444 = Color.decode("#444444");
    public static Color C_E54040 = Color.decode("#e54040");
    public static Color C_222222 = Color.decode("#222222");
    public static Color C_F65073 = Color.decode("#f65073");
    public static Color C_6A6A6A = Color.decode("#6a6a6a");
    public static Color C_606060 = Color.decode("#606060");
    public static Color C_191919 = Color.decode("#191919");
    public static Color C_7F7F7F = Color.decode("#7f7f7f");
    public static Color C_FF7A19 = Color.decode("#ff7a19");
    public static Color C_FBE82C = Color.decode("#fbe82c");

    public static Color C_000000_95 = decodeColor("#000000", 95);
    public static Color C_000000_90 = decodeColor("#000000", 90);
    public static Color C_000000_80 = decodeColor("#000000", 80);
    public static Color C_000000_45 = decodeColor("#000000", 45);
    public static Color C_000000_30 = decodeColor("#000000", 30);
    public static Color C_333333_95 = decodeColor("#333333", 95);
    public static Color C_FFFFFF_20 = decodeColor("#FFFFFF", 20);
    public static Color C_FFFFFF_40 = decodeColor("#FFFFFF", 40);
    public static Color C_111111_90 = decodeColor("#111111", 90);
    public static Color C_303030_70 = decodeColor("#303030", 70);
    public static Color C_AAAAAA_30 = decodeColor("#AAAAAA", 30);


    /**
     * 16진수 컬러값과 투명도로 해당 Color를 리턴
     * @param nm
     * @param alpha
     * @return
     */
    public static Color decodeColor(String nm, int alpha) {
        Integer intval = Integer.decode(nm);
        int i = intval.intValue();
        Color color = new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF, (255 * alpha)/100);
        return color;
    }

    /**
     * 0-255 사이 컬러값을 통한 객체 리턴
     */
    public static Color decodeColor(int r, int g, int b, int a) {
        return new Color(r,g,b,a);
    }

    public static Color decodeColor(JSONArray obj) {
        if(obj==null) return null;
        return decodeColor(
                ((Long)obj.get(0)).intValue(),
                ((Long)obj.get(1)).intValue(),
                ((Long)obj.get(2)).intValue(),
                ((Long)obj.get(3)).intValue()
        );
    }
}
