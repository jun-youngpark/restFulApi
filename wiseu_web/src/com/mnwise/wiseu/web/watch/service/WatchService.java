package com.mnwise.wiseu.web.watch.service;

import static com.mnwise.common.util.StringUtil.CRLF;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.common.util.Util;
import com.mnwise.sw.tms.server.BseServer;
import com.mnwise.sw.tms.server.GenericServer;
import com.mnwise.sw.tms.server.ItsServer;
import com.mnwise.sw.tms.server.LtsServer;
import com.mnwise.sw.tms.server.MtsServer;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.watch.dao.StatusReqDao;
import com.mnwise.wiseu.web.watch.dao.StatusReqResultDao;
import com.mnwise.wiseu.web.watch.dao.SvcDivideDao;
import com.mnwise.wiseu.web.watch.dao.SvcMainDao;
import com.mnwise.wiseu.web.watch.dao.SvcMtsDao;
import com.mnwise.wiseu.web.watch.dao.SvrInfoDao;
import com.mnwise.wiseu.web.watch.model.EcareMonitorVO;
import com.mnwise.wiseu.web.watch.model.ServerConfig;
import com.mnwise.wiseu.web.watch.model.ServerList;
import com.mnwise.wiseu.web.watch.model.ServiceInfo;
import com.mnwise.wiseu.web.watch.model.ServiceTid;
import com.mnwise.wiseu.web.watch.model.SvcMain;
import com.mnwise.wiseu.web.watch.model.SvcMts;
import com.mnwise.wiseu.web.watch.model.SvrInfo;

/**
 * Watch Service
 */
@Service
public class WatchService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(WatchService.class);

    @Autowired private EcareDao ecareDao;
    @Autowired private StatusReqDao statusReqDao;
    @Autowired private StatusReqResultDao statusReqResultDao;
    @Autowired private SvcDivideDao svcDivideDao;
    @Autowired private SvrInfoDao svrInfoDao;
    @Autowired private SvcMainDao svcMainDao;
    @Autowired private SvcMtsDao svcMtsDao;

    private String m_sUserId;
    private List<ServerList> m_lServerList; // 서버 정보 목록 - get list server
    private List<ServiceInfo> m_lServiceList; // 서비스 정보 목록 - get list service
    private List<ServiceTid> m_lServiceInfo; // TID에 대한 서비스 정보 목록 - get info service TID
    private List<ServerConfig> m_lServerConfig; // comment, key, value

    /**
     * 로그인 - TMS 소켓 연결
     */
    public String connectTms(String userId, String passWd) {
        if(m_lServerList == null)
            m_lServerList = new ArrayList<>(); // ServerInfo(serverName, serverId[]
        if(m_lServiceList == null)
            m_lServiceList = new ArrayList<>(); // ServiceInfo
        if(m_lServerConfig == null)
            m_lServerConfig = new ArrayList<>();
        if(m_lServiceInfo == null)
            m_lServiceInfo = new ArrayList<>();

        return "+OK " + m_sUserId;
    }

    /**
     * 각 엔진 목록을 가져온다.
     */
    public List<ServerList> getListServer() {
        try {
            List<String> serverIdList = svrInfoDao.selectServerIdList();

            if(serverIdList == null)
                return null;

            m_lServerList.clear();

            for(int i = 0; i < serverIdList.size(); i++) {
                String serverName = serverIdList.get(i).substring(0, 3);

                String serverIds = "";
                int cnt = 0;
                while(true) {
                    if(!serverName.equals(serverIdList.get(i).substring(0, 3)))
                        break;

                    if(cnt > 0)
                        serverIds += ',';

                    serverIds += serverIdList.get(i);

                    i++;
                    cnt++;

                    if(i >= serverIdList.size())
                        break;
                }

                if(cnt != 0)
                    i--;

                ServerList serverList = new ServerList();
                serverList.setServerName(serverName.toLowerCase());
                serverList.setServerId(serverIds.split(","));
                m_lServerList.add(serverList);
            }

            serverIdList.clear();

        } catch(Exception e) {
            log.error(null, e);
            return null;
        }

        return m_lServerList;
    }

    /**
     * 서비스 목록을 가져온다.
     */
    public List<ServiceInfo> getListService(String client, String selectNum) {
        try {
            List<SvcMain> svcMainList = svcMainDao.selectServiceList(client, selectNum);

            if(svcMainList == null || svcMainList.size() == 0)
                return null;

            if(m_lServiceList == null)
                m_lServiceList = new ArrayList<>();

            m_lServiceList.clear();

            for(int i = 0; i < svcMainList.size(); i++) {
                SvcMain svcMain = svcMainList.get(i);

                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new SimpleDateFormat("yyyyMMddHHmm").parse(svcMain.getStartTm()));

                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setChannel(svcMain.getChannel());
                serviceInfo.setClient(svcMain.getClient());
                serviceInfo.setElapsedTime("0000");
                serviceInfo.setNo(Integer.parseInt(svcMain.getTId().substring(0, 15)));
                serviceInfo.setSendMode(svcMain.getSendMode());
                serviceInfo.setServiceName(svcMain.getServiceNm());
                serviceInfo.setServiceStatus(svcMain.getStatus());
                serviceInfo.setServiceType(svcMain.getServiceType());
                serviceInfo.setStartTime(date);
                serviceInfo.setTid(svcMain.getTId());
                serviceInfo.setUserId(svcMain.getUserId());

                if(svcMain.getTargetCnt() != 0) {
                    m_lServiceList.add(serviceInfo);
                }

            }
        } catch(Exception e) {
            log.error(null, e);
            return null;
        }

        return m_lServiceList;
    }

    /**
     * 서비스 상세정보를 얻는다. 객체직렬화
     */
    public List<ServiceTid> getInfoService(String tid) {
        try {
            List<SvcMain> svcMainList = svcMainDao.getInfoService_lts(tid);

            if(svcMainList == null || svcMainList.size() == 0)
                return null;

            if(m_lServiceInfo == null)
                m_lServiceInfo = new ArrayList<>();

            m_lServiceInfo.clear();

            SvcMain svcMain = svcMainList.get(0);

            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new SimpleDateFormat("yyyyMMddHHmm").parse(svcMain.getStartTm()));

            ServiceInfo serviceInfo = new ServiceInfo();
            serviceInfo.setChannel(svcMain.getChannel());
            serviceInfo.setClient(svcMain.getClient());
            serviceInfo.setElapsedTime("0000");
            serviceInfo.setNo(Integer.parseInt(svcMain.getTId().substring(0, 15)));
            serviceInfo.setSendMode(svcMain.getSendMode()); /* 발송모드[테스트(T), 실발송(R)] */
            serviceInfo.setServiceName(svcMain.getServiceNm()); /* 서비스명 */
            serviceInfo.setServiceStatus(svcMain.getServiceSts()); /* 클라이언트 종류 [EM, EC] */
            serviceInfo.setServiceType(svcMain.getServiceType()); /* 타입 [스케쥴(S), 리얼타임(R)] */
            serviceInfo.setStartTime(date);
            serviceInfo.setTid(svcMain.getTId());
            serviceInfo.setUserId(svcMain.getUserId());

            ServiceTid serviceTid = new ServiceTid();
            serviceTid.setServerName("common");
            serviceTid.setServiceName(svcMain.getServiceNm());
            serviceTid.setUserId(svcMain.getUserId());
            serviceTid.setServiceStatus(svcMain.getServiceSts());
            serviceTid.setSendMode(svcMain.getSendMode());
            m_lServiceInfo.add(serviceTid);

            serviceTid = new ServiceTid();
            serviceTid.setServerName("LTS");
            serviceTid.setServerId(svcMain.getServerId());
            serviceTid.setStartTime(date);
            serviceTid.setServiceStatus(svcMain.getServiceSts());
            serviceTid.setTargetCount(svcMain.getTargetCnt()); /* 대상자 건수 */
            serviceTid.setChannel(svcMain.getChannel()); /* 채널 [M, S, T, F] */
            serviceTid.setMtsNo(svcMain.getMtsCnt()); /* 작업 MTS 개수 */
            m_lServiceInfo.add(serviceTid);

            List<SvcMts> svcMtsList = svcMtsDao.getInfoService_mts(tid);

            if(svcMtsList == null || svcMtsList.size() == 0)
                return m_lServiceInfo;

            for(int i = 0; i < svcMtsList.size(); i++) {
                SvcMts svcMts = svcMtsList.get(i);

                serviceTid = new ServiceTid();

                date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new SimpleDateFormat("yyyyMMddHHmm").parse(svcMts.getStartTm()));

                serviceTid.setServerName(svcMts.getServerId().substring(0, 3));
                serviceTid.setServerId(svcMts.getServerId());
                serviceTid.setChannel(svcMain.getChannel());
                serviceTid.setStartTime(date);
                serviceTid.setServiceStatus(svcMts.getServiceSts()); /* 서비스 상태 */
                serviceTid.setTotalCount(svcMts.getTotCnt()); /* 전체건수 */
                serviceTid.setMadeCount(svcMts.getMadeCnt()); /* 생성건수 */
                serviceTid.setRetry(svcMts.getMaxRetry()); /* 재시도 횟수 */
                serviceTid.setSendCount(svcMts.getSendCnt()); /* 전송건수 */
                serviceTid.setSuccessCount(svcMts.getSuccessCnt()); /* 성공건수 */
                serviceTid.setUnknownUserCount(svcMts.getUnknownUserCnt()); /* unknown user 건수 */
                serviceTid.setUnknownHostCount(svcMts.getUnknownHostCnt()); /* unknown host 건수 */
                serviceTid.setSmtpExceptCount(svcMts.getSmtpExceptCnt()); /* smtp except 건수 */
                serviceTid.setNoRouteCount(svcMts.getNoRouteCnt()); /* no route 건수 */
                serviceTid.setRefusedCount(svcMts.getRefusedCnt()); /* refused 건수 */
                serviceTid.setEtcExceptCount(svcMts.getEtcExceptCnt()); /* etc except 건수 */
                serviceTid.setInvalidAddressCount(svcMts.getInvalidAddrCnt()); /* invalid address */
                serviceTid.setErrorMsg(svcMts.getErrMsg());
                m_lServiceInfo.add(serviceTid);
            }
        } catch(Exception e) {
            log.error(null, e);
            return null;
        }

        return m_lServiceInfo;
    }

    /**
     * serverId 에 해당하는 서버 정보를 얻는다.
     */
    public GenericServer getInfoServer(String serverId) {
        GenericServer genericServer = null;

        try {
            SvrInfo svrInfo = svrInfoDao.selectSvrInfoByPk(serverId);
            log.debug(svrInfo.toString());

            if(svrInfo == null) {
                return null;
            }

            if(serverId.startsWith("LTS")) {
                genericServer = new LtsServer(serverId, svrInfo.getStatus());
            } else if(serverId.startsWith("MTS")) {
                genericServer = new MtsServer(serverId, svrInfo.getStatus());
            } else if(serverId.startsWith("ITS")) {
                genericServer = new ItsServer(serverId, svrInfo.getStatus());
            } else if(serverId.startsWith("BSE")) {
                genericServer = new BseServer(serverId, svrInfo.getStatus());
            }

            if(isOnServer(svrInfo.getLastUpdateTm(), svrInfo.getCurTm()) == false) {
                genericServer.setServerStatus(0);
            } else {
                genericServer.setCpuRate(svrInfo.getCpuRate()); // CPU 사용률
                genericServer.setJobCount(svrInfo.getJobCnt()); // JOB 개수
                genericServer.setMaxMemory(svrInfo.getMaxMemory()); // 최대 메모리
                genericServer.setMaxQueueSize(svrInfo.getMaxQueueSize()); // 큐최대개수
                genericServer.setProcessThreadCount(svrInfo.getProcessThreadCnt()); // 처리쓰레드개수
                genericServer.setUsedMemory(svrInfo.getUsedMemory()); // 사용중인 메모리
                genericServer.setUsedQueueSize(svrInfo.getUsedQueueSize()); // 큐사용개수
                genericServer.setWorksThreadCount(svrInfo.getWorkThreadCnt()); // 작업쓰레드개수
                genericServer.setOpenFileDescriptorCount(svrInfo.getOpenfileDescCnt()); // 열린파일개수
            }

            genericServer.setConfig("server.root=d:/dev_dest/workspace/wiseu_server"); // dummy

            // 이제는 의미 없어서 값을 0으로 셋팅...
            String sExecInfo = "TMS_IP=0" + CRLF + "TMS_IP2=0" + CRLF + "WATCHDOG_IP=0" + CRLF + "WATCHDOG_PORT=0" + CRLF + "JVM=0" + CRLF + "XMX=0" + CRLF + "XMS=0" + CRLF;

            genericServer.setExecInfo(sExecInfo);

            String startTime = svrInfo.getStartTm();

            if(startTime != null) {
                Date date = new SimpleDateFormat("yyyyMMddHHmm").parse(startTime);
                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
                genericServer.setStartTime(startTime);
            }
        } catch(Exception e) {
            log.error(null, e);
            return null;
        }

        return genericServer;
    }

    private boolean isOnServer(String strLastUpdateTm, String strCurTm) {
        boolean bActServer = false;
        // String sCurTm = DateUtil.getNowDateTime();
        String sCurTm = strCurTm;

        if(sCurTm.equals(strLastUpdateTm))
            bActServer = true;
        else {
            if(sCurTm.substring(0, 10).equals(strLastUpdateTm.substring(0, 10))) {
                int nCurMin = Integer.parseInt(sCurTm.substring(10, 12));
                int nCurSs = Integer.parseInt(sCurTm.substring(12, 14));

                int nMin = Integer.parseInt(strLastUpdateTm.substring(10, 12));
                int nSs = Integer.parseInt(strLastUpdateTm.substring(12, 14));

                // 년월일시까지 동일하고 분초 비교해서 15초를 넘기지 않았으면 사용가능한 것으로 간주
                if((nCurMin - nMin == 0) && nCurSs - nSs < 16)
                    bActServer = true;
                else if((nCurMin - nMin == 1) && ((nCurSs + 60) - nSs < 16))
                    bActServer = true;
            }
        }

        return bActServer;
    }

    public List<ServerConfig> getConfig(String serverId) {
        try {
            log.info("get config " + serverId);
            m_lServerConfig.clear();

            SvrInfo svrInfo = svrInfoDao.selectSvrInfoByPk(serverId);

            String[] configLine = svrInfo.getConfigCont().split(CRLF);

            ServerConfig serverConfig = null;
            for(int i = 0; i < configLine.length; i++) {

                if(i == 0)
                    continue; // "?" 라인 붙어서 생략....

                if(serverConfig == null)
                    serverConfig = new ServerConfig();

                if(log.isDebugEnabled())
                    log.debug(configLine[i]);

                // comment
                if(configLine[i].startsWith("#")) {
                    serverConfig.setComment(configLine[i]);
                } else if(configLine[i].indexOf("=") > -1) {
                    int offset = configLine[i].indexOf("=");

                    serverConfig.setKey(configLine[i].substring(0, offset));
                    serverConfig.setValue(configLine[i].substring(offset + 1).trim());

                    m_lServerConfig.add(serverConfig);
                    serverConfig = null;
                }
            }
        } catch(Exception e) {
            log.error(null, e);
            return null;
        }

        return m_lServerConfig;
    }

    /**
     * Server ID 에 해당하는 config 내용을 가져옴
     */
    public String getConfigNew(String serverId) {
        try {
            log.info("get config " + serverId);
            m_lServerConfig.clear();

            SvrInfo svrInfo = svrInfoDao.selectSvrInfoByPk(serverId);

            return svrInfo.getConfigCont();
        } catch(Exception e) {
            log.error(null, e);
        }

        return null;
    }

    /**
     * 프로세스에서 엔진의 환경설정 값을 저장한다.
     */
    public void saveProcessConfig(String serverId, String config) throws Exception {
        svrInfoDao.updateConfigContByPk(serverId, config);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("reqKind", "SVR");
        paramMap.put("createTm", DateUtil.getNowDateTime());
        paramMap.put("taskId", serverId);
        paramMap.put("req", "CONFIG");
        paramMap.put("userId", "");
        statusReqDao.insertSvrStatusReq(paramMap);

        while(true) {
            Thread.sleep(3000);
            String sTemp = statusReqDao.retrieveSvrStatusReqResult(paramMap);

            if(sTemp.equals("1"))
                break;
        }
    }

    public boolean startServer(String serverId) throws Exception {
        SvrInfo svrInfo = svrInfoDao.selectSvrInfoByPk(serverId);
        Properties prop = Util.stringToProperties(svrInfo.getExecInfo());
        return startServer(prop.getProperty("WATCHDOG_IP").trim(), Integer.parseInt(prop.getProperty("WATCHDOG_PORT").trim()), serverId);
    }

    /**
     * 로그인 - WATCH DOG에 서버 실행 명령을 한다.
     */
    private boolean startServer(String strIp, int numPort, String strServerId) {
        Socket watchDogSocket = null;
        BufferedReader in = null;

        try {
            log.info("Connect to WatchDog from Watch : " + strIp + " " + numPort);
            // WiseWatch 접속
            watchDogSocket = new Socket(strIp, numPort);

            in = new BufferedReader(new InputStreamReader(watchDogSocket.getInputStream()));

            watchDogSocket.getOutputStream().write(("execute process " + strServerId).getBytes());
            watchDogSocket.getOutputStream().flush();

            String line = in.readLine().trim();

            if(line == null)
                return false;

            if(line.startsWith("-ERR"))
                return false;
            else if(line.startsWith("+OK"))
                return true;
        } catch(Exception e) {
            log.error(null, e);
            return false;
        } finally {
            IOUtil.closeQuietly(in);
            IOUtil.closeQuietly(watchDogSocket);
        }

        return true;
    }

    /**
     * serverId 에 해당하는 엔진을 정지시킨다. serverId: LTS1, MTS1 ...
     */
    public boolean stopServer(String serverId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("reqKind", "SVR");
        paramMap.put("createTm", DateUtil.getNowDateTime());
        paramMap.put("taskId", serverId);
        paramMap.put("req", "STOP");
        paramMap.put("userId", "");
        statusReqDao.insertSvrStatusReq(paramMap);

        while(true) {
            Thread.sleep(3000);
            String sTemp = statusReqDao.retrieveSvrStatusReqResult(paramMap);

            if(sTemp.equals("1"))
                break;
        }

        return true;
    }

    /**
     * 서비스 일시멈춤
     */
    public boolean pauseService(String tid) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("reqKind", "SVR");
        paramMap.put("createTm", DateUtil.getNowDateTime());
        paramMap.put("taskId", tid);
        paramMap.put("req", "SUSPEND");
        paramMap.put("userId", "");
        statusReqDao.insertSvrStatusReq(paramMap);

        //2019.11.18 분할발송작업중 여부
        boolean divideSvc = svcDivideDao.selectDivideStatusSuspend(tid) > 0 ? true : false;

        //분할발송
        int updateCnt = svcDivideDao.updateDivideStatusSuspend(tid);

        int errCnt = 0;
        for(int i = 0; ; i++) {
            Thread.sleep(3000);

            CaseInsensitiveMap smData = statusReqResultDao.retrieveSvcStatusReqResult(paramMap);

            int totCnt = ((java.math.BigDecimal) smData.get("TOT_CNT")).intValue();
            int cnt = ((java.math.BigDecimal) smData.get("CNT")).intValue();
            int mtsCnt = ((java.math.BigDecimal)smData.get("MTS_CNT")).intValue();

            if(totCnt == errCnt || (divideSvc && updateCnt < 1)) {
                paramMap.put("result", 3); // 1 : 처리완료, 3 : 기타오류
                paramMap.put("updateTm", DateUtil.getNowDateTime());
                statusReqDao.updateSvrStatusReq(paramMap);
                return false;
            } else if((totCnt == cnt || cnt == mtsCnt || cnt > 2) && (!divideSvc || updateCnt >= 1)) {
                paramMap.put("result", 1); // 1 : 처리완료, 3 : 기타오류
                paramMap.put("updateTm", DateUtil.getNowDateTime());
                statusReqDao.updateSvrStatusReq(paramMap);
                break;
            } else if(i > 10) {
                paramMap.put("result", 3); // 1 : 처리완료, 3 : 기타오류
                paramMap.put("updateTm", DateUtil.getNowDateTime());
                return false;
            }
        }

        return true;
    }

    /**
     * 서비스 재기동
     */
    public boolean restartService(String tid) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("reqKind", "SVR");
        paramMap.put("createTm", DateUtil.getNowDateTime());
        paramMap.put("taskId", tid);
        paramMap.put("req", "RESUME");
        paramMap.put("userId", "");
        statusReqDao.insertSvrStatusReq(paramMap);

        int errCnt = 0;
        for(int i = 0; ; i++) {
            Thread.sleep(3000);

            CaseInsensitiveMap smData = statusReqResultDao.retrieveSvcStatusReqResult(paramMap);

            int totCnt = ((java.math.BigDecimal) smData.get("TOT_CNT")).intValue();
            int cnt = ((java.math.BigDecimal) smData.get("CNT")).intValue();
            int mtsCnt = ((java.math.BigDecimal) smData.get("MTS_CNT")).intValue();

            if(totCnt == errCnt) {
                paramMap.put("result", 3);// 1 : 처리완료, 3 : 기타오류
                paramMap.put("updateTm", DateUtil.getNowDateTime());
                return false;
            } else if(totCnt == cnt || cnt == mtsCnt || cnt > 2) {
                paramMap.put("result", 1);// 1 : 처리완료, 3 : 기타오류
                paramMap.put("updateTm", DateUtil.getNowDateTime());
                statusReqDao.updateSvrStatusReq(paramMap);
                break;
            } else if(i > 10) {
                paramMap.put("result", 3);// 1 : 처리완료, 3 : 기타오류
                paramMap.put("updateTm", DateUtil.getNowDateTime());
                return false;
            }
        }

        return true;
    }

    /**
     * TID 에 해당하는 서비스 멈춤
     */
    public boolean stopService(String tid) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("reqKind", "SVR");
        paramMap.put("createTm", DateUtil.getNowDateTime());
        paramMap.put("taskId", tid);
        paramMap.put("req", "STOP");
        paramMap.put("userId", "");
        statusReqDao.insertSvrStatusReq(paramMap);

        //2019.11.18 분할발송작업중 여부
        boolean divideSvc = svcDivideDao.selectDivideStatusStop(tid) > 0 ? true : false;

        //2017.05.23 분할발송
        int updateCnt = svcDivideDao.updateDivideStatusStop(tid);

        int errCnt = 0;
        for(int i = 0; ; i++) {
            Thread.sleep(3000);

            CaseInsensitiveMap smData = statusReqResultDao.retrieveSvcStatusReqResult(paramMap);

            int totCnt = ((java.math.BigDecimal) smData.get("TOT_CNT")).intValue();
            int cnt = ((java.math.BigDecimal) smData.get("CNT")).intValue();
            int mtsCnt = ((java.math.BigDecimal) smData.get("MTS_CNT")).intValue();

            if(totCnt == errCnt || (divideSvc && updateCnt < 1)) {
                paramMap.put("result", 3);// 1 : 처리완료, 3 : 기타오류
                paramMap.put("updateTm", DateUtil.getNowDateTime());
                return false;
            } else if((totCnt == cnt || cnt == mtsCnt || cnt > 2) && (!divideSvc || updateCnt >= 1)) {
                paramMap.put("result", 1);// 1 : 처리완료, 3 : 기타오류
                paramMap.put("updateTm", DateUtil.getNowDateTime());
                statusReqDao.updateSvrStatusReq(paramMap);
                break;
            } else if(i > 10) {
                paramMap.put("result", 3);// 1 : 처리완료, 3 : 기타오류
                paramMap.put("updateTm", DateUtil.getNowDateTime());
                return false;
            }
        }

        return true;
    }

    /**
     * 서비스 삭제( S[일시정지],T[정지],O[에러],E[발송완료] 인 서비스만 삭제 가능하다.)
     */
    public boolean deleteService(String tid) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("reqKind", "SVR");
        paramMap.put("createTm", DateUtil.getNowDateTime());
        paramMap.put("taskId", tid);
        paramMap.put("req", "REMOVE");
        paramMap.put("userId", "");
        statusReqDao.insertSvrStatusReq(paramMap);

        //2019.11.18 분할발송작업중 여부
        boolean divideSvc = svcDivideDao.selectDivideStatusStop(tid) > 0 ? true : false;

        //2017.05.23 분할발송
        int updateCnt = svcDivideDao.updateDivideStatusStop(tid);

        for(int i = 0; ; ) {
            Thread.sleep(3000);

            CaseInsensitiveMap smData = statusReqResultDao.retrieveSvcStatusReqResult(paramMap);

            int totCnt = ((java.math.BigDecimal) smData.get("TOT_CNT")).intValue();
            int cnt = ((java.math.BigDecimal) smData.get("CNT")).intValue();
            int errCnt = ((java.math.BigDecimal) smData.get("ERR_CNT")).intValue();
            int mtsCnt = ((java.math.BigDecimal) smData.get("MTS_CNT")).intValue();

            if(totCnt == errCnt || (divideSvc && updateCnt < 1)) {
                paramMap.put("result", 3);// 1 : 처리완료, 3 : 기타오류
                paramMap.put("updateTm", DateUtil.getNowDateTime());
                return false;
            } else if((totCnt == cnt || cnt == mtsCnt || cnt > 2) && (!divideSvc || updateCnt >= 1)) {
                paramMap.put("result", 1);// 1 : 처리완료, 3 : 기타오류
                paramMap.put("updateTm", DateUtil.getNowDateTime());
                statusReqDao.updateSvrStatusReq(paramMap);
                svcMainDao.updateSvcDelYn(tid);
                break;
            } else if(i++ > 10) {
                paramMap.put("result", 3);// 1 : 처리완료, 3 : 기타오류
                paramMap.put("updateTm", DateUtil.getNowDateTime());
                return false;
            }
        }

        return true;
    }

    // LTS 모드상태(Active/StandBy 또는 Single) 모드 조회
    public String getPollingActStatus(String serverId) throws Exception {
        SvrInfo svrInfo = svrInfoDao.selectSvrInfoByPk(serverId);

        if(svrInfo != null) {
            // 서버가 종료상태인지 판단
            if(isOnServer(svrInfo.getLastUpdateTm(), svrInfo.getCurTm()) == false) {
                return "E";
            }

            return StringUtil.defaultString(svrInfo.getPollingActStatus());
        }

        return null;
    }

    /**
     * 사용자 아이디와 서비스 아이디로 ecare_no 를 조회
     *
     * @param userid 사용자 아이디
     * @param svcId 서비스 아이디
     * @return
     */
    public int getEcareMsgByEcareNo(String userId, String svcId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("svcId", svcId);
        return ecareDao.getEcareMsgByEcareNo(map);
    }

    /**
     * ecare 모니터 등록
     *
     * @param ecareMonitorVO
     * @return insert id
     */
    public List<EcareMonitorVO> getEcareMonitorStatusList2(String userId) {
        return ecareDao.getEcareMonitorStatusList2(userId);
    }
}
