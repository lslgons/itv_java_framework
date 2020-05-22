package com.tcom.ssr;


import com.tcom.network.SSRConnector;
import com.tcom.network.SSRResponse;
import com.tcom.platform.controller.StbController;
import com.tcom.util.LOG;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataManager {

    JSONObject jsonData;

    private static DataManager instance;
    public static DataManager getInstance() {
        if(instance==null) instance = new DataManager();
        return instance;
    }

    private DataManager() {
        setDefaultData();
    }

    /**
     * 컨텍스트 정보 완전 초기화
     */
    public void setDefaultData() {
        SSRConfig config=SSRConfig.getInstance();
        StbController stb=StbController.getInstance();
        String defaultData = "{\"state\":{\"key\":[0,0,0],\"av_size\":[0,0,"+config.SCENE_WIDTH+","+config.SCENE_HEIGHT+"],\"vod_info\":\"\",\"dmc_code\":\""+stb.getDMCCode()+"\",\"so_code\":\""+stb.getSOCode()+"\",\"stb_id\":\""+stb.getSmartcardID()+"\"},\"uid\":\"\",\"context\":{},\"trigger_action\":99,\"trigger_target\":\"\",\"component\":{\"render\":[],\"element\":[]}}";
        JSONParser parser = new JSONParser();
        try {
            jsonData= (JSONObject) parser.parse(defaultData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 데이터 정보를 서버로부터 갱신
     */
    public void requestData(int action_type, int action_trigger) {
        SSRConnector.ssrRequest(this.jsonData, new SSRResponse() {
            public void onReceived(JSONObject response) {
                DataManager.this.jsonData=response;
            }

            public void onFailed(int status, String msg) {
                LOG.print(status+" error");
            }
        });
    }

}
