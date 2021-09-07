package com.mnwise.wiseu.web.common.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.common.util.Mapper;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.Const.ServiceType;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.campaign.dao.CampaignDao;
import com.mnwise.wiseu.web.campaign.dao.SendResultDao;
import com.mnwise.wiseu.web.campaign.dao.TestGrpDao;
import com.mnwise.wiseu.web.campaign.dao.TestSendLogDao;
import com.mnwise.wiseu.web.campaign.dao.TestUserDao;
import com.mnwise.wiseu.web.campaign.dao.TestUserPoolDao;
import com.mnwise.wiseu.web.channel.dao.MzFtSendTranDao;
import com.mnwise.wiseu.web.channel.dao.MzSendTranDao;
import com.mnwise.wiseu.web.common.model.ImageModelVo;
import com.mnwise.wiseu.web.common.model.MessageVo;
import com.mnwise.wiseu.web.common.model.TestGrpVo;
import com.mnwise.wiseu.web.common.model.TestSendVo;
import com.mnwise.wiseu.web.common.model.TesterPoolVo;
import com.mnwise.wiseu.web.common.model.TesterVo;
import com.mnwise.wiseu.web.common.util.WiseuSerialNumber;
import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.ecare.dao.EcareSendResultDao;
import com.mnwise.wiseu.web.ecare.dao.TestEcareSendLogDao;

import net.sf.json.JSONObject;

/**
 * 테스트 발송 Service
 */
@Service
public class TesterService extends AbstractSendService {
    private static final Logger log = LoggerFactory.getLogger(TesterService.class);

    @Autowired private CampaignDao campaignDao;
    @Autowired private EcareDao ecareDao;
    @Autowired private EcareSendResultDao ecareSendResultDao;
    @Autowired private MzFtSendTranDao mzFtSendTranDao;
    @Autowired private MzSendTranDao mzSendTranDao;
    @Autowired private SendResultDao sendResultDao;
    @Autowired private TestEcareSendLogDao testEcareSendLogDao;
    @Autowired private TestGrpDao testGrpDao;
    @Autowired private TestSendLogDao testSendLogDao;
    @Autowired private TestUserDao testUserDao;
    @Autowired private TestUserPoolDao testUserPoolDao;

    @Value("${db.server}")
    private String dbServer;
    @Value("${import.upload.dir}")
    private String importDir;

    public List<TestSendVo> getTestSendList(TestSendVo testSendVo, int serviceNo, String serviceType) {
        PagingUtil.setPagingRowcount(testSendVo);
        List<TestSendVo> list = new ArrayList<>();
        testSendVo.setServiceNo(serviceNo);

        if(serviceType.equals("C")) {
            list = testSendLogDao.selectTestSend(testSendVo);
        } else if(serviceType.equals("E")) {
            list = testEcareSendLogDao.selectTestEcareSend(testSendVo);
        }

        return list;
    }

    public int getTestSendListCount(int serviceNo, String serviceType) {
        TestSendVo testSendVo = new TestSendVo();
        testSendVo.setServiceNo(serviceNo);
        int cnt = 0;
        if(serviceType.equals("C")) {
            cnt = testSendLogDao.getTestSendListCount(serviceNo);
        } else if(serviceType.equals("E")) {
            cnt = testEcareSendLogDao.getTestEcareSendListCount(serviceNo);
        }

        return cnt;
    }

    /**
     * 테스트 그룹 리스트를 가져온다
     *
     * @param userId 아이디
     * @return
     */
    public List<TestGrpVo> getTestGrpList(String userId) {
        return testGrpDao.getTestGrpList(userId);
    }

    /**
     * 그룹별 테스터 리스트를 가져온다.
     *
     * @param grpCd
     * @return
     */
    public List<TesterPoolVo> getTesterList(TesterPoolVo testPoolVo) {
        return testUserPoolDao.getTesterList(testPoolVo);
    }

    /**
     * 테스트 그룹 정보
     *
     * @param testGrpCd 테스트 그룹코드
     * @return
     */
    public TestGrpVo getTestGrpInfo(int testGrpCd, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("testGrpCd", testGrpCd);
        map.put("userId", userId);

        return testGrpDao.getTestGrpInfo(map);
    }

    /**
     * 그룹 저장
     *
     * @param testGrpVo
     * @return
     */
    public int insertTestGrp(TestGrpVo testGrpVo) {
        return testGrpDao.insertTestGrp(testGrpVo);
    }

    /**
     * 그룹 수정
     *
     * @param testGrpVo
     * @return
     */
    public int updateTestGrp(TestGrpVo testGrpVo) {
        return testGrpDao.updateTestGrp(testGrpVo);
    }

    /**
     * 그룹 삭제 (Active YN 을 N으로 )
     *
     * @param testGrpCd
     * @return
     */
    public int updateTestGrpActiveYn(int testGrpCd) {
        return testGrpDao.updateTestGrpActiveYn(testGrpCd);
    }

    /**
     * 테스터 추가
     *
     * @param testerPoolVo
     * @return
     */
    public int insertTester(TesterPoolVo testerPoolVo) {
        return testUserPoolDao.insertTester(testerPoolVo);
    }

    /**
     * 테스터 수정
     *
     * @param testerPoolVo
     * @return
     */
    public int updateTester(TesterPoolVo testerPoolVo) {
        return testUserPoolDao.updateTestUserPoolByPk(testerPoolVo);
    }

    /**
     * 테스터 삭제
     *
     * @param userId
     * @param seqNo[]
     * @return
     */
    public int deleteTester(String userId, int[] seqNo) {
        int row = 0;

        for(int i = 0; i < seqNo.length; i++) {
            row += testUserPoolDao.deleteTestUserPoolByPk(userId, seqNo[i]);
        }

        return row;
    }

    /**
     * 파일 올리기
     *
     * @param inputStream
     * @param testGrpCd 그룹 CD
     * @param userId 아이디
     * @return
     */
    public int insertFileUploadTester(InputStream inputStream, int testGrpCd, String userId) {
        String fileName = "tester_" + testGrpCd + "_" + DateUtil.dateToString("yyyyMMddhhmmss", new Date()) + ".csv";

        File fileUpload = new File(importDir + "/" + fileName);
        log.debug(importDir + "/" + fileName);

        List<TesterPoolVo> testerPoolList = null;

        int seqNo = testUserPoolDao.getTesterPoolMaxSeq(userId);
        BufferedReader in = null;
        try {
            FileUtil.forceMkdir(new File(fileUpload.getParent()));

            in = new BufferedReader(new InputStreamReader(inputStream,Charset.forName("EUC-KR")));

            EmailValidator emailValidator = EmailValidator.getInstance();

            String line;

            testerPoolList = new ArrayList<>();

            while((line = in.readLine()) != null) {
                String[] strArray = line.split(",");
                int cnt = strArray.length;
                int i = 0;

                // 이름, 이메일 내용이 없을 경우
                if(cnt < 2) {
                    return 1;
                }

                TesterPoolVo testerPoolVo = new TesterPoolVo();

                String testReceiverNm = strArray[i++];
                String testReceiverEmail = strArray[i++];
                String testReceiverTel = "";
                String testReceiverFax = "";
                if(cnt > 2) {
                    testReceiverTel = strArray[i++];

                    if(cnt == 4) {
                        testReceiverFax = strArray[i];
                    }
                }

                // 이메일 유효성 검사가 맞지 않으면
                if(!emailValidator.isValid(testReceiverEmail)) {
                    return 1;
                }

                testerPoolVo.setTestReceiverNm(testReceiverNm);
                testerPoolVo.setTestReceiverEmail(testReceiverEmail);
                testerPoolVo.setTestReceiverTel(testReceiverTel);
                testerPoolVo.setTestReceiverFax(testReceiverFax);
                testerPoolVo.setTestGrpCd(testGrpCd);
                testerPoolVo.setSeqNo(seqNo);
                testerPoolVo.setUserId(userId);
                // SECUDB DONE
                security.securityObject(testerPoolVo, "ENCRYPT");
                testerPoolList.add(testerPoolVo);
                seqNo++;
            }
        } catch(FileNotFoundException e) {
            log.error("saveFile() - File Not Found." + e);
        } catch(IOException e) {
            log.error("saveFile() - Error while saving file." + e);
        } finally {
            IOUtil.closeQuietly(in);
            try {
                // 생성된 csv 파일 삭제
                fileUpload.delete();
            } catch(Exception e2) {
                log.warn(null, e2);
            }
        }

        return testUserPoolDao.insertFileUploadTester(testerPoolList);
    }

    /**
     * 테스트 발송
     *
     * @param checkSeqNo 선택한 대상자의 SeqNO
     * @param no 서비스 번호 (캠페인번호, 이케어 번호)
     * @param serviceType 서비스 타입 (E 이케어 C 캠페인 S 설문)
     * @param userId 아이디
     * @return
     */
    public int insertTestUser(String checkSeqNo, int no, String serviceType, String userId) {
        StringTokenizer st = new StringTokenizer(checkSeqNo, ",");
        List<TesterVo> list = new ArrayList<>();

        while(st.hasMoreTokens()) {
            int seqNo = Integer.parseInt(st.nextToken());

            TesterVo testerVo = new TesterVo();
            testerVo.setSeqNo(seqNo);
            testerVo.setServiceNo(no);
            testerVo.setServiceType(serviceType);
            testerVo.setUserId(userId);

            list.add(testerVo);
        }

        TesterVo testerVo = new TesterVo();
        testerVo.setServiceNo(no);
        testerVo.setUserId(userId);
        testerVo.setServiceType(serviceType);

        // NVUSERTEST 에 기존에 테스트 발송된 동일 데이터가 있으면 삭제한다.
        testUserDao.deleteTestUser(testerVo);

        // NVUSERTEST 에 관련 정보를 넣는다.
        testUserDao.insertTestUser(list);

        if(serviceType.equals(ServiceType.CAMPAIGN)) {
            campaignDao.updateTestModeCampaign(no);
        } else if(serviceType.equals(ServiceType.ECARE)) {
            ecareDao.updateTestModeEcare(no);
        }

        // 각 모드별 테이블의 sending_mode 를 T로 업데이트 해준다
        return 0;
    }

    public int insertSendTestUser(String checkSeqNo, int serviceNo, String serviceType, String grpCd, String userId) {
        StringTokenizer st = new StringTokenizer(checkSeqNo, ",");
        List<Integer> seqNoList = new ArrayList<>();

        while(st.hasMoreTokens()) {
            seqNoList.add(Integer.parseInt(st.nextToken()));
        }

        List<TesterPoolVo> testUserList = new ArrayList<>();
        int resultCnt = 0;

        try {
            testUserList = testUserPoolDao.getTestSendList(seqNoList);
            if(serviceType.equals("E")) {
                serviceType = "ec";
            } else {
                serviceType = "em";
            }

            MessageVo messageVo = null;
            if(serviceType.equals("ec")) {
                messageVo = ecareDao.getEcareDetail(serviceNo);
            }else{
                messageVo = campaignDao.getCampaignDetail(serviceNo);
            }
            messageVo.setServiceNo(serviceNo + "");
            String startDt = DateUtil.dateToString("yyyyMMdd", new Date());
            String startTm = DateUtil.dateToString("HHmmss", new Date());
            messageVo.setResultSeq(Long.parseLong(startDt));
            messageVo.setStartDt(startDt);
            messageVo.setStartTm(startTm);

            String channel = messageVo.getChannel();
            ImageModelVo imVo = getTemplate(serviceType, channel, serviceNo);

            String template = imVo.getTemplate();
            String[] imagePath = imVo.getFilePath();

            Map<String, Object> map = new HashMap<>();
            map.put("serviceNo", serviceNo);
            map.put("date", startDt);

            if(serviceType.equals("ec")) {
                int ecareSendResultCnt = ecareSendResultDao.selectEcareSendResultCountByPk(map);

                if(ecareSendResultCnt == 0) {
                    ecareSendResultDao.insertEcareSendResultByMessage(messageVo);
                }
            }else{
                int campaignSendResultCnt = sendResultDao.selectSendResultCountByPk(map);

                if(campaignSendResultCnt == 0) {
                    sendResultDao.insertSendResultByMessage(messageVo);
                }
            }
            String friendtalkImagePath = "";
            JSONObject json = new JSONObject();
            JSONObject attachmentData = new JSONObject();
            String imageUrl = "";
            if("C".equals(channel)) {
                imageUrl = getImageURl(serviceType, serviceNo);
                if(StringUtil.isNotBlank(imageUrl)) {
                    json.put("img_url", imageUrl);
                    attachmentData.put("image", json);
                    friendtalkImagePath = mobileContentsDao.getFriendtalkImagePath(messageVo.getFileName1());
                }

            }
            String targetQuery = getTargetQuery(serviceType, serviceNo);
            if(dbServer.equalsIgnoreCase("DB2")) {
                targetQuery = targetQuery + " FETCH FIRST 1 ROWS ONLY ";
            } else if(dbServer.equalsIgnoreCase("MYSQL")) {
                targetQuery = targetQuery + " LIMIT 1 ";
            } else if(dbServer.equalsIgnoreCase("MSSQL")) {
                targetQuery = targetQuery.toUpperCase().replace("SELECT ", "SELECT TOP 1 ");
            } else if(dbServer.equalsIgnoreCase("ORACLE")) {
                targetQuery = targetQuery + " AND ROWNUM < 2 ";
            }

            List<Hashtable<String, Object>> sendMessageList = sendMessage(serviceType, targetQuery, channel, serviceNo);

            String tmplCd = messageVo.getTmplCd();
            String senderKey = messageVo.getSenderKey();
            String sendMessage = "";
            List<MessageVo> sendList = new ArrayList<>();

            for(int i = 0; i < testUserList.size(); i++) {
                if("A".equals(channel) || "C".equals(channel)) {
                    sendMessage = Mapper.mapping(template, sendMessageList.get(0), "#{", "}");
                } else {
                    sendMessage = Mapper.mapping(template, sendMessageList.get(0), "{$", "$}");
                }

                MessageVo mv = new MessageVo();
                mv.setSenderKey(senderKey);
                mv.setResultSeq(messageVo.getResultSeq());
                mv.setChannel(channel);
                mv.setReqDeptCd(grpCd);
                mv.setReqUsrId("testsend");
                mv.setSmsSndYn(messageVo.getSmsSndYn()); //수정
                mv.setCallBack(messageVo.getCallBack().replaceAll("-", ""));
                mv.setServiceType(serviceType);
                if(channel.equals(Channel.FRIENDTALK) || channel.equals(Channel.ALIMTALK)) {
                    mv.setSubject(messageVo.getFailbackSubject()); //수정
                    mv.setFileName1(friendtalkImagePath);
                    if(StringUtil.isNotBlank(imageUrl)) {
                        mv.setAttachment(attachmentData.toString());
                    }
                    if("A".equals(messageVo.getAdFlag())) {
                        mv.setAdFlag("Y");
                    } else {
                        mv.setAdFlag("N");
                    }
                }

                mv.setTmplCd(tmplCd);
                mv.setSn(WiseuSerialNumber.getSn("TEST", 1));
                mv.setSendMessage(sendMessage);
                mv.setPhoneNum(testUserList.get(i).getTestReceiverTel());
                mv.setServiceNo(serviceNo + "");
                if(channel.equals("T")) {
                    mv.setMmsSeq(emTranMmsDao.getEmTranMmsSequence() + "");
                    mv.setResultSeqPage(0);
                    mv.setListSeq(StringUtil.leftPad(String.valueOf(i + 1), 10, '0'));
                    mv.setSubject(messageVo.getSubject());
                    if(imagePath == null) {
                        mv.setFileCnt(0);
                    } else if(imagePath.length == 1) {
                        mv.setFileType("IMG");
                        mv.setFileName1(imagePath[0]);
                        if(StringUtil.isNotBlank(sendMessage)) {
                            mv.setFileCnt(2);
                        } else {
                            mv.setFileCnt(1);
                        }
                        mv.setServiceDep("ALL");
                    } else if(imagePath.length == 2) {
                        mv.setFileType("IMG");
                        mv.setFileName1(imagePath[0]);
                        mv.setFileName2(imagePath[1]);
                        mv.setServiceDep("ALL");
                        if(StringUtil.isNotBlank(sendMessage)) {
                            mv.setFileCnt(3);
                        } else {
                            mv.setFileCnt(2);
                        }
                    } else if(imagePath.length == 3) {
                        mv.setFileType("IMG");
                        mv.setFileName1(imagePath[0]);
                        mv.setFileName2(imagePath[1]);
                        mv.setFileName3(imagePath[2]);
                        mv.setServiceDep("ALL");
                        if(StringUtil.isNotBlank(sendMessage)) {
                            mv.setFileCnt(4);
                        } else {
                            mv.setFileCnt(3);
                        }
                    }
                }

                sendList.add(mv);

                if(sendList.size() == testUserList.size()) {
                    if("A".equals(channel)) {
                        resultCnt = mzSendTranDao.bulkAlimtalkDataInsert(sendList);
                    } else if("C".equals(channel)) {
                        resultCnt = mzFtSendTranDao.bulkFriendtalkDataInsert(sendList);
                    } else if("S".equals(channel)) {
                        resultCnt = emTranDao.bulkSMSDataInsert(sendList);
                    } else if("T".equals(channel)) {
                        if(imagePath == null) {
                            emTranMmsDao.bulkLMSDataInsert(sendList);
                            resultCnt = bulkLMSTRANDataInsert(sendList);
                        } else {
                            emTranMmsDao.bulkMMSDataInsert(sendList);
                            resultCnt = bulkMMSTRANDataInsert(sendList);
                        }
                    }
                }
            }
        } catch(Exception e) {
            log.error("### ERROR : TEST SEND ERROR...");
            log.error(e.getMessage());
            resultCnt = 0;
        }

        return resultCnt;
    }

}
