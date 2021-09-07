package com.mnwise.wiseu.web.env.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;

/**
 * 검증데이터 생성기 DAO 클래스
 */
@Repository
public class EnvMakeDataDao  extends BaseDao {
    /**
     * 환경설정 - 테스트발송관리 채널에 해당하는 서비스 리스트를 가져온다
     * @param status 
     *
     * @변경이력 :
     * @return
     */
    @SuppressWarnings("unchecked")
    public List selectServiceList(String channel, String status) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("channel", channel);
        dataMap.put("status", status);
        /* String[] subTypeArr = {"S", "R"};
         * dataMap.put("subType", subTypeArr); */
        List<Map<String, Object>> tmpMapList = selectList("MakeData.selectServiceList", dataMap);
        
        security.securityObjectList(tmpMapList,  "DECRYPT");
        return tmpMapList;
    }
    

    /**
     * 환경설정 - 테스트발송관리 선택한 서비스의 상세정보를 가져온다.
     * @param status 
     *
     * @param 
     */
    public Map<String, Object> getServiceInfo(String serviceNo, String serviceType, String status) {
        Map<String, Object> ecareMap = new HashMap<String, Object>();
        if(serviceType.equals("EC")) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("serviceNo", serviceNo);
            dataMap.put("status", status);
            
            ecareMap = (Map<String, Object>) selectOne("MakeData.getServiceInfo", dataMap);
        }
        
        security.securityObject(ecareMap,  "DECRYPT");
        return ecareMap;
    }

    /**
     * 환경설정 - 테스트발송관리 템플릿 메시지를 가져온다
     *
     * @param 
     */
    public String getTempleteMsg(String serviceNo, String serviceType) {
        String templeteMsg = "";
        if(serviceType.equals("EC")) {
            templeteMsg = (String) selectOne("MakeData.getTempleteMsg", serviceNo);
        }
        
        return templeteMsg;
    }
    
    /**
     * 환경설정 - 테스트발송관리 푸시의 팝업 메시지를 가져온다
     *
     * @param 
     */
    public String getPushPopMsg(String serviceNo, String serviceType) {
        String popMsg = "";
        if(serviceType.equals("EC")) {
            popMsg = (String) selectOne("MakeData.getPushPopMsg", serviceNo);
        }
        
        return popMsg;
    }

    /**
     * 환경설정 - 테스트발송관리 서비스에 해당하는 시맨틱 리스트를 가져온다
     *
     * @param 
     */
    public List<Map<String, Object>> getSemanticList(String segmentNo, String serviceType){
        List<Map<String, Object>> tmpMapList = new ArrayList<Map<String, Object>>();
        if(serviceType.equals("EC")) {
            tmpMapList = (List<Map<String, Object>>) selectList("MakeData.getSemanticList", segmentNo);
        }
        
        security.securityObjectList(tmpMapList,  "DECRYPT");
        return tmpMapList;
    }
    
    /**
     * 환경설정 - 테스트발송관리 만들어진 데이터를 발송 테이블에 Insert한다.
     *
     * @param 
     */
    public int insertMakeData(List<Map<String, Object>> nvMapList){
        int totalCnt = 0;
        int cnt = 0;
        try {
            Map<String, Object> nvMap = new HashMap<String, Object>();
            
            if(nvMapList.size() > 0) {
                for(int i=0; i < nvMapList.size(); i++) {
                    nvMap = nvMapList.get(i);

                    //DB 암복호화 적용
                    //이메일주소 정책으로 암호화
                    nvMap.put("receiver",security.encrypt((String) nvMap.get("receiver"), "ADDRcrypt"));
                    
                    String subType = nvMap.get("SUB_TYPE").toString();
                    if(subType.equals("S") || subType.equals("R")) {
                        cnt += update("MakeData.insertMakeBatchData", nvMap);
                    }else if(subType.equals("N") || subType == null) {
                        cnt += update("MakeData.insertMakeAcceptData", nvMap);
                    }
                    
                    
                    if(cnt == 50 || cnt == nvMapList.size()) {
                        totalCnt += cnt;
                        cnt = 0;
                    }
                }
            }
        } catch(Exception e) {
            totalCnt = 0;
            e.printStackTrace();
        }
        return totalCnt;
    }
    
    public List<String> getServiceNoForChannel(String channel){
        List<String> tmpMapList = new ArrayList<String>();
        tmpMapList = (List<String>) selectList("MakeData.getServiceNoForChannel", channel);
        
        security.securityObjectList(tmpMapList,  "DECRYPT");
        return tmpMapList;
    }
    
    public List<String> getServiceNoForFromTo(String channel, String serviceFrom, String serviceTo){
        Map<String, Object> tmpMap = new HashMap<String, Object>();
        tmpMap.put("channel", channel);
        tmpMap.put("serviceFrom", serviceFrom);
        tmpMap.put("serviceTo", serviceTo);
        
        List<String> tmpMapList = new ArrayList<String>();
        tmpMapList = (List<String>) selectList("MakeData.getServiceNoForFromTo", tmpMap);

        security.securityObjectList(tmpMapList,  "DECRYPT");
        return tmpMapList;
    }
    
    public List<String> getOmniChannelList(String serviceNo){
        List<String> tmpMapList = new ArrayList<String>();
        tmpMapList = (List<String>) selectList("MakeData.getOmniChannelList", serviceNo);
        
        security.securityObjectList(tmpMapList,  "DECRYPT");
        return tmpMapList;
    }


    /**
     * 보안 메일 커버 템플릿 조회
     * @param serviceNo
     * @param serviceType
     * @return
     */
    public String getCoverTemplateMsg(String serviceNo, String serviceType) {
        String templeteMsg = "";
        if(serviceType.equals("EC")) {
            templeteMsg = (String) selectOne("MakeData.getCoverTemplateMsg", serviceNo);
        }
        return templeteMsg;
    }
}
