package com.tcom.ssr.manager;

import com.tcom.platform.controller.KeyController;
import com.tcom.platform.controller.MediaController;
import com.tcom.platform.controller.StbController;
import com.tcom.ssr.SSRConfig;
import com.tcom.util.LOG;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;

public class StateManager {
    static JSONObject stateObj=null;

    public StateManager() {
        if(stateObj==null) {
            StbController stb=StbController.getInstance();
            KeyController key=KeyController.getInstance();
            MediaController media = MediaController.getInstance();
            JSONArray av_size = new JSONArray();

            Rectangle real_av_size = media.getCurrentVideoSize();

            LOG.print(real_av_size.toString());
            if(SSRConfig.getInstance().AV_SD_MODE) {
                av_size.add(new Integer((int)real_av_size.x*4/3));
                av_size.add(new Integer((int)real_av_size.y*4/3));
                av_size.add(new Integer((int)real_av_size.width*4/3));
                av_size.add(new Integer((int)real_av_size.height*4/3));
            } else {
                av_size.add(new Integer((int)real_av_size.x));
                av_size.add(new Integer((int)real_av_size.y));
                av_size.add(new Integer((int)real_av_size.width));
                av_size.add(new Integer((int)real_av_size.height));
            }

            JSONArray enabled_key=new JSONArray();
            enabled_key.add(new Integer(key.isEnableBackKey()?1:0));
            enabled_key.add(new Integer(key.isEnableNumKey()?1:0));
            enabled_key.add(new Integer(key.isEnableHotKey()?1:0));
            stateObj=new JSONObject();
            stateObj.put("vod_info", "");
            stateObj.put("av_size", av_size);
            stateObj.put("dmc_code", stb.getDMCCode());
            stateObj.put("so_code", stb.getSOCode());
            stateObj.put("stb_id", stb.getSmartcardID());
            stateObj.put("key", enabled_key);
        }

        //TODO AppID : 어플리케이션 종류에 따른 ID부여 필요성

    }

    public void setJSONObject(JSONObject obj) {

        //TODO 설정된 정보에 따라 STB상태 갱신
        JSONArray old_av= (JSONArray) stateObj.get("av_size");
        JSONArray new_av= (JSONArray) obj.get("av_size");
        if(old_av.get(0)!=new_av.get(0) ||old_av.get(1)!=new_av.get(1)||old_av.get(2)!=new_av.get(2)||old_av.get(3)!=new_av.get(3)) {
            LOG.print("Change Video requested, "+new_av.get(0)+","+new_av.get(1)+","+new_av.get(2)+","+new_av.get(3));
            stateObj.put("av_size", new_av);
            if(SSRConfig.getInstance().AV_SD_MODE) {
                //720사이즈 기준이므로 *0.75
                LOG.print("AV SD MODE : ");
                LOG.print("X :" + (int)(((Long)new_av.get(0)).intValue()*0.75));
                LOG.print("Y :" + (int)(((Long)new_av.get(1)).intValue()*0.75));
                LOG.print("W :" + (int)(((Long)new_av.get(2)).intValue()*0.75));
                LOG.print("H :" + (int)(((Long)new_av.get(3)).intValue()*0.75));
                MediaController.getInstance().changeVideoSize(
                        (int)(((Long)new_av.get(0)).intValue()*0.75),
                        (int)(((Long)new_av.get(1)).intValue()*0.75),
                        (int)(((Long)new_av.get(2)).intValue()*0.75),
                        (int)(((Long)new_av.get(3)).intValue()*0.75));
            } else {
                MediaController.getInstance().changeVideoSize(((Long)new_av.get(0)).intValue(),((Long)new_av.get(1)).intValue(),((Long)new_av.get(2)).intValue(),((Long)new_av.get(3)).intValue());
            }

        }

        //TODO VOD Play
        String newVodAssetId=(String) obj.get("vod_info");
        String oldVodAssetId=(String) stateObj.get("vod_info");
        if(newVodAssetId!=null && newVodAssetId.length()>0) {
            //Play VOD
            if(!oldVodAssetId.equalsIgnoreCase(newVodAssetId)) {
                //서로다른 VOD AssetID일 경우 재생
                MediaController.getInstance().startVOD(newVodAssetId);
            }
        } else {
            if(oldVodAssetId.length()>0) {
                //현재 재생중이므로 AV로 복귀
                MediaController.getInstance().stopVOD();
            }
        }


        JSONArray old_key=(JSONArray) stateObj.get("key");
        JSONArray new_key=(JSONArray) obj.get("key");
        if(old_key.get(0)!=new_key.get(0)) {
            stateObj.put("key", new_key);
            KeyController.getInstance().setEnableBackKey(((Long)new_key.get(0)).intValue()==1);
        }
        if(old_key.get(1)!=new_key.get(1)) {
            stateObj.put("key", new_key);
            KeyController.getInstance().setEnableNumKey(((Long)new_key.get(1)).intValue()==1);
        }
        if(old_key.get(2)!=new_key.get(2)) {
            stateObj.put("key", new_key);
            KeyController.getInstance().setEnableHotKey(((Long)new_key.get(2)).intValue()==1);
        }
    }

    public int[] getAVSize() {
        JSONArray _avSize= (JSONArray) stateObj.get("av_size");
        int[] avSize={((Integer) _avSize.get(0)).intValue(),
                ((Integer) _avSize.get(1)).intValue(),
                ((Integer) _avSize.get(2)).intValue(),
                ((Integer) _avSize.get(3)).intValue()};


        return avSize;
    }
    public String getVODAssetID() {
        return (String) stateObj.get("vod_info");
    }
    public void flushVODAssetID() {
        stateObj.put("vod_info", "");
    }

    public JSONObject getJSONObject() {
        return stateObj;
    }
}
