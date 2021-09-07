package com.mnwise.wiseu.web.common.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.campaign.dao.CampaignDao;
import com.mnwise.wiseu.web.channel.dao.MzFtSendTranDao;
import com.mnwise.wiseu.web.channel.dao.MzSendTranDao;
import com.mnwise.wiseu.web.common.model.ImageModelVo;
import com.mnwise.wiseu.web.common.model.MessageVo;
import com.mnwise.wiseu.web.common.util.WiseuSerialNumber;
import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.ecare.dao.EcareSendResultDao;
import com.mnwise.wiseu.web.segment.model.SemanticVo;
import com.mnwise.wiseu.web.template.dao.KakaoProfileDao;
import com.mnwise.wiseu.web.template.model.KakaoProfile;
import com.mnwise.wiseu.web.template.model.MobileVo;

import net.sf.json.JSONObject;

@Service
public class SendMessageService extends AbstractSendService {
    private final Logger log = LoggerFactory.getLogger(SendMessageService.class);

    @Autowired private CampaignDao campaignDao;
    @Autowired private EcareDao ecareDao;
    @Autowired private EcareSendResultDao ecareSendResultDao;
    @Autowired private KakaoProfileDao kakaoProfileDao;
    @Autowired private MzFtSendTranDao mzFtSendTranDao;
    @Autowired private MzSendTranDao mzSendTranDao;

    public int bulkAlimtalkDataImport(List<MessageVo> arrList) {
        return mzSendTranDao.bulkAlimtalkDataInsert(arrList);
    }

    public int bulkFriendtalkDataImport(List<MessageVo> arrList) {
        return mzFtSendTranDao.bulkFriendtalkDataInsert(arrList);
    }

    public int bulkLMSkDataImport(List<MessageVo> arrList) {
        emTranMmsDao.bulkLMSDataInsert(arrList);
        return bulkLMSTRANDataInsert(arrList);
    }

    public int bulkMMSDataImport(List<MessageVo> arrList) {
        emTranMmsDao.bulkMMSDataInsert(arrList);
        return bulkMMSTRANDataInsert(arrList);
    }

    public int bulkSMSDataImport(List<MessageVo> arrList) {
        return emTranDao.bulkSMSDataInsert(arrList);
    }

    public MessageVo getCampaignDetail(int serviceNo) {
        return campaignDao.getCampaignDetail(serviceNo);
    }

    public MessageVo getEcareDetail(int serviceNo) {
        return ecareDao.getEcareDetail(serviceNo);
    }

    public String getFriendtalkImagePath(String serviceNo) {
        return mobileContentsDao.getFriendtalkImagePath(serviceNo);
    }

    public ImageModelVo getTemplate(String sendOnce, String serviceType, String channel, int serviceNo, String template) {
        ImageModelVo im = new ImageModelVo();

        Hashtable<String, String> ht = new Hashtable<>();

        if("O".equals(sendOnce)) {
            // 단건발송일 때
            if("S".equals(channel)) {
                template = template.replace("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>", "");
                template = template.replace("<%record=context.get(\"record\")%>", "");
            } else if("T".equals(channel)) {
                template = template.replace("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>", "");
                template = template.replace("<%record=context.get(\"record\")%>", "");
                if(template.contains("<img")) {
                    im = sendImageTemplate(template);
                    template = im.getTemplate();
                }
            } else if("A".equals(channel) || "C".equals(channel)) {
                template = template.replace("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>", "");
            }
        } else {
            if("ec".equals(serviceType)) {
                template = ecareTemplateDao.getEcareSendTemplate(serviceNo);
            } else {
                template = templateDao.getCampaignSendTemplate(serviceNo);
            }

            List<SemanticVo> mappingList = semanticDao.getEcareSemanticList(serviceNo);

            for(int i = 0; i < mappingList.size(); i++) {
                ht.put(mappingList.get(i).getFieldSeq() + "", mappingList.get(i).getFieldDesc());
                if("S".equals(mappingList.get(i).getFieldKey()) || "E".equals(mappingList.get(i).getFieldKey())) {
                    ht.put(mappingList.get(i).getFieldKey(), mappingList.get(i).getFieldDesc());
                }
            }
            if("S".equals(channel)) {
                template = template.replace("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>", "");
                template = template.replace("<%record=context.get(\"record\")%>", "");
                template = smsTemplate(ht, template);
            } else if("T".equals(channel)) {
                template = template.replace("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>", "");
                template = template.replace("<%record=context.get(\"record\")%>", "");
                if(template.contains("<img")) {
                    im = sendImageTemplate(template);
                    template = im.getTemplate();
                }
                template = smsTemplate(ht, template);
            } else if("C".equals(channel)) {
                template = template.replace("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>", "");
            }
        }
        im.setTemplate(template);

        return im;
    }

    public void insertNVECARESENDRESULT(MessageVo messageVo) {
        ecareSendResultDao.insertEcareSendResultByMessage(messageVo);
    }

    public List<KakaoProfile> selectKakaoProfileList(String userId) {
        return kakaoProfileDao.selectKakaoProfileList(userId);
    }

    public int selectNVECARESENDRESULT(Map<String, Object> map) {
        return ecareSendResultDao.selectEcareSendResultCountByPk(map);
    }

    // 단건발송 데이터 처리
    public int sendMessageOnce(String channel, int serviceNo, String send_dtm, String callBack, String phoneNum, String sendDt, String sendTm, String subject, String template, String senderKey,
        String tmplCd, String smsSndYn, String contsNo, String adFlag, UserVo userVo) {

        String serviceType = "ec";
        ImageModelVo imVo = getTemplate("O", serviceType, channel, serviceNo, template);
        template = imVo.getTemplate();
        String[] imagePath = imVo.getFilePath();
        String imgPath = "";
        String attachment = "";

        JSONObject json = new JSONObject();
        if("C".equals(channel) && !"".equals(contsNo.trim())) {
            MobileVo moVo = mobileContentsDao.getKakaoImageUrl(contsNo);
            json.put("img_url", moVo.getImageUrl());
            imgPath = moVo.getDetourFilePath();
            attachment = "{ \"image\" : " + json.toString() + " }";
        }

        MessageVo mobileVo = getEcareDetail(serviceNo);
        mobileVo.setServiceNo(serviceNo + "");
        mobileVo.setResultSeq(Long.parseLong(sendDt));
        mobileVo.setStartDt(sendDt);
        mobileVo.setStartTm(sendTm);

        Map<String, Object> map = new HashMap<>();
        map.put("serviceNo", serviceNo);
        map.put("date", sendDt);

        int ecareSendResultCnt = selectNVECARESENDRESULT(map);

        if(ecareSendResultCnt == 0) {
            insertNVECARESENDRESULT(mobileVo);
        }

        String sendMessage = template;

        int resultCnt = 0;

        try {
            MessageVo mv = new MessageVo();
            mv.setSenderKey(senderKey);
            mv.setResultSeq(mobileVo.getResultSeq());
            mv.setChannel(channel);
            mv.setReqDeptCd(userVo.getGrpCd());
            mv.setReqUsrId(userVo.getUserId());
            mv.setSmsSndYn(smsSndYn); //수정
            mv.setCallBack(callBack);

            if(channel.equals("C") || channel.equals("A")) {
                mv.setSubject(subject); //수정
                mv.setAttachment(attachment);
                mv.setFileName1(imgPath);
                mv.setAdFlag(adFlag);
            }

            String reqDtm = "Now".equals(send_dtm) ? DateUtil.getNowDateTime() : sendDt + sendTm;
            mv.setReqDtm(reqDtm);
            mv.setTmplCd(tmplCd);
            mv.setSn(WiseuSerialNumber.getSn(serviceType, 1));
            mv.setSendMessage(sendMessage);
            mv.setPhoneNum(phoneNum);
            mv.setServiceNo(serviceNo + "");
            mv.setServiceType(serviceType);

            if(channel.equals("T")) {
                mv.setMmsSeq(emTranMmsDao.getEmTranMmsSequence() + "");
                mv.setResultSeqPage(0);
                mv.setListSeq("0000000001");
                mv.setSubject(subject);
                mv.setFileCnt(0);
                if(imagePath != null) {
                    mv.setFileCnt((imagePath.length));
                    if(imagePath.length == 1) {
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
            }

            List<MessageVo> sendList = new ArrayList<>();
            sendList.add(mv);

            if("A".equals(channel)) {
                resultCnt = bulkAlimtalkDataImport(sendList);
            } else if("C".equals(channel)) {
                resultCnt = bulkFriendtalkDataImport(sendList);
            } else if("S".equals(channel)) {
                resultCnt = bulkSMSDataImport(sendList);
            } else if("T".equals(channel)) {
                if(imagePath != null) {
                    resultCnt = bulkMMSDataImport(sendList);
                } else {
                    resultCnt = bulkLMSkDataImport(sendList);
                }
            }

        } catch(Exception e) {
            log.error("### ERROR : SEND ONCE FAIL ERROR...");
            log.error(e.getMessage());
            resultCnt = 0;
        }

        return resultCnt;
    }
}