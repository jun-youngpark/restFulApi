package com.mnwise.wiseu.web.env.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.env.dao.EnvMakeDataDao;

@Service
public class EnvMakeDataService extends BaseService {
    
    @Autowired private EnvMakeDataDao envMakeDataDao;

    public void setEnvMakeDataDao(EnvMakeDataDao envMakeDataDao) {
        this.envMakeDataDao = envMakeDataDao;
    }
    /**
     * 선택한 채널에 해당하는 서비스 리스트를 가져온다.
     * @param status 
     *
     * @변경이력 :
     * @return
     */
    public List<Map<String, Object>> channelServiceList(String channel, String status) {
        return envMakeDataDao.selectServiceList(channel, status);
    }
    
    /**
     * 선택한 서비스의 상세정보를 가져온다.
     * @param status 
     *
     * @변경이력 :
     * @return
     */
    public Map<String, Object> getServiceInfo(String serviceNo, String serviceType, String status) {
        return envMakeDataDao.getServiceInfo(serviceNo, serviceType, status);
    }

    /**
     * 선택한 템플릿의 텍스트를 가져온다.
     *
     * @변경이력 :
     * @return
     */
    public String getTempleteMsg(String serviceNo, String serviceType) {
        return envMakeDataDao.getTempleteMsg(serviceNo, serviceType);
    }

    /**
     * 푸시의 팝업 텍스트를 가져온다.
     *
     * @변경이력 :
     * @return
     */
    public String getPushPopMsg(String serviceNo, String serviceType) {
        return envMakeDataDao.getPushPopMsg(serviceNo, serviceType);
    }

    /**
     * 선택한 서비스에 맵핑되는 시맨틱을 가져온다.
     *
     * @변경이력 :
     * @return
     */
    public List<Map<String, Object>> getSemanticList(String segmentNo, String serviceType){
        return envMakeDataDao.getSemanticList(segmentNo, serviceType);
    }
    
    /**
     * 만들어진 데이터를 발송을 위한 테이블에 Insert한다.
     *
     * @변경이력 :
     * @return
     */
    public int insertMakeData(List<Map<String, Object>> nvMapList) {
        return envMakeDataDao.insertMakeData(nvMapList);
    }
    
    /**
     * 선택한 채널의 서비스 중 기동중인 서비스를 모두 가져온다.
     *
     * @변경이력 :
     * @return
     */
    public List<String> getServiceNoForChannel(String channel){
        return envMakeDataDao.getServiceNoForChannel(channel);
    }
    
    public List<String> getServiceNoForFromTo(String channel, String serviceFrom, String serviceTo){
        return envMakeDataDao.getServiceNoForFromTo(channel, serviceFrom, serviceTo);
    }

    
    public List<String> getOmniChannelList(String serviceNo){
        return envMakeDataDao.getOmniChannelList(serviceNo);
    }
    
    /**
     * 보안 메일 커버 템플릿 조회
     * @param serviceNo
     * @param serviceType
     * @return
     */
    public String getCoverTemplateMsg(String serviceNo, String serviceType) {
        return envMakeDataDao.getCoverTemplateMsg(serviceNo, serviceType);
    }
}
