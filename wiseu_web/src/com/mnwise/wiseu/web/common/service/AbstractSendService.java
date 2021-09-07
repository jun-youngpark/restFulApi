package com.mnwise.wiseu.web.common.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.campaign.dao.TemplateDao;
import com.mnwise.wiseu.web.channel.dao.EmTranDao;
import com.mnwise.wiseu.web.channel.dao.EmTranMmsDao;
import com.mnwise.wiseu.web.common.dao.DbInfoDao;
import com.mnwise.wiseu.web.common.model.ImageModelVo;
import com.mnwise.wiseu.web.common.model.MessageVo;
import com.mnwise.wiseu.web.common.util.DBConnectionUtil;
import com.mnwise.wiseu.web.ecare.dao.EcareTemplateDao;
import com.mnwise.wiseu.web.segment.dao.SegmentDao;
import com.mnwise.wiseu.web.segment.dao.SemanticDao;
import com.mnwise.wiseu.web.segment.model.DbInfoVo;
import com.mnwise.wiseu.web.segment.model.SemanticVo;
import com.mnwise.wiseu.web.template.dao.MobileContentsDao;

public class AbstractSendService extends BaseService {
    private final Logger log = LoggerFactory.getLogger(AbstractSendService.class);

    @Autowired private DbInfoDao dbInfoDao;
    @Autowired protected EcareTemplateDao ecareTemplateDao;
    @Autowired protected EmTranDao emTranDao;
    @Autowired protected EmTranMmsDao emTranMmsDao;
    @Autowired protected MobileContentsDao mobileContentsDao;
    @Autowired protected SegmentDao segmentDao;
    @Autowired protected SemanticDao semanticDao;
    @Autowired protected TemplateDao templateDao;

    private DbInfoVo dbInfoVo;
    private static boolean isdbinit = false;

    public int bulkLMSTRANDataInsert(final List<MessageVo> lmsList) {
        int row = 0;

        List<MessageVo> targetList = new ArrayList<>();
        if(StringUtil.isBlank(lmsList.get(0).getMmsSeq())) {
            final List<MessageVo> mmsSeqList = emTranMmsDao.getSequence(lmsList.get(0));
            for(int k = 0; k < lmsList.size(); k++) {
                MessageVo mv = lmsList.get(k);
                for(int j = 0; j < mmsSeqList.size(); j++) {
                    if(mv.getListSeq().equals(mmsSeqList.get(j).getListSeq())) {
                        mv.setMmsSeq(mmsSeqList.get(j).getMmsSeq());
                    }
                }
                targetList.add(mv);
            }

            for(int t = 0; t < targetList.size(); t++) {
                MessageVo mv = targetList.get(t);
                row += emTranDao.insertDBROLMS(mv);
            }
        } else {
            for(int i = 0; i < lmsList.size(); i++) {
                MessageVo mv = lmsList.get(i);
                row += emTranDao.insertDBROLMS(mv);
            }
        }

        return row;
    }

    public int bulkMMSTRANDataInsert(final List<MessageVo> mmsList) {
        int row = 0;

        List<MessageVo> targetList = new ArrayList<>();
        if(StringUtil.isBlank(mmsList.get(0).getMmsSeq())) {
            final List<MessageVo> mmsSeqList = emTranMmsDao.getSequence(mmsList.get(0));
            for(int k = 0; k < mmsList.size(); k++) {
                MessageVo mv = mmsList.get(k);
                for(int j = 0; j < mmsSeqList.size(); j++) {
                    if(mv.getListSeq().equals(mmsSeqList.get(j).getListSeq())) {
                        mv.setMmsSeq(mmsSeqList.get(j).getMmsSeq());
                    }
                }
                targetList.add(mv);
            }
            for(int t = 0; t < targetList.size(); t++) {
                MessageVo mv = targetList.get(t);
                row += emTranDao.insertDBROMMS(mv);
            }
        } else {
            for(int i = 0; i < mmsList.size(); i++) {
                MessageVo mv = mmsList.get(i);
                row += emTranDao.insertDBROMMS(mv);
            }
        }

        return row;
    }

    public String getImageURl(String serviceType, int serviceNo) {
        if("ec".equals(serviceType)) {
            return mobileContentsDao.getEcareKakaoImageUrl(serviceNo);
        } else {
            return mobileContentsDao.getCampaignKakaoImageUrl(serviceNo);
        }
    }

    public String getTargetQuery(String serviceType, int serviceNo) {
        if("ec".equals(serviceType)) {
            return segmentDao.getEcareTargetQuery(serviceNo);
        } else {
            return segmentDao.getCampaignTargetQuery(serviceNo);
        }
    }

    public ImageModelVo getTemplate(String serviceType, String channel, int serviceNo) {
        String template = "";
        ImageModelVo im = new ImageModelVo();

        if("ec".equals(serviceType)) {
            template = ecareTemplateDao.getEcareSendTemplate(serviceNo);
        } else {
            template = templateDao.getCampaignSendTemplate(serviceNo);
        }

        Hashtable<String, String> ht = new Hashtable<>();

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
        im.setTemplate(template);

        return im;
    }

    public String smsTemplate(Hashtable<String, String> ht, String template) {
        StringBuilder sb = new StringBuilder(template);
        Pattern pattern = null;
        Matcher matcher = null;
        for(int i = 1; i <= ht.size(); i++) {
            pattern = Pattern.compile("<%=\\(record.getString\\(" + i + "\\)\\)%>");
            matcher = pattern.matcher(sb.toString());
            while(matcher.find()) {
                sb.replace(matcher.start(), matcher.end(), "{$" + ht.get(String.valueOf(i)) + "$}");
                matcher = pattern.matcher(sb.toString());
            }
        }

        return sb.toString();
    }

    public ImageModelVo sendImageTemplate(String template) {
        ImageModelVo model = new ImageModelVo();
        Parser parser = null;
        NodeList list = null;
        ImageTag imageTag = null;
        String[] filePath = null;
        try {
            parser = new Parser(template);
            list = parser.extractAllNodesThatMatch(new HasAttributeFilter("img"));
            filePath = new String[list.size()];
            for(int i = 0; i < list.size(); i++) {
                imageTag = (ImageTag) list.elementAt(i);
                filePath[i] = super.mmsUploadPathRoot + imageTag.getAttribute("src");

                int imgSIndex = template.indexOf("<img");
                int imgEIndex = template.indexOf("/>");

                if(imgSIndex > -1) {
                    String temp = template.substring(imgSIndex, imgEIndex + 2);
                    template = template.replace(temp, "");
                }
            }

            model.setTemplate(template);
            model.setFilePath(filePath);

        } catch(Exception e) {
            log.error(e.getMessage());
        }

        return model;
    }

    public List<Hashtable<String, Object>> sendMessage(String serviceType, String targetQuery, String channel, int serviceNo) {
        Hashtable<String, String> ht = new Hashtable<>();

        List<SemanticVo> mappingList = semanticDao.getEcareSemanticList(serviceNo);

        for(int i = 0; i < mappingList.size(); i++) {
            ht.put(mappingList.get(i).getFieldSeq() + "", mappingList.get(i).getFieldDesc());
            if("S".equals(mappingList.get(i).getFieldKey()) || "E".equals(mappingList.get(i).getFieldKey())) {
                ht.put(mappingList.get(i).getFieldKey(), mappingList.get(i).getFieldDesc());
            }
        }

        return sendTargetData(targetQuery, ht);
    }

    public List<Hashtable<String, Object>> sendTargetData(String sendDataQuery, Hashtable<String, String> fieldTable) {
        dbinit();
        DBConnectionUtil dbConnectionUtil = new DBConnectionUtil(dbInfoVo.getDriverNm(), dbInfoVo.getDriverDsn(), dbInfoVo.getDbUserId(), dbInfoVo.getDbPassword());
        Connection conn = dbConnectionUtil.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Hashtable<String, Object>> list = null;

        try {
            pstmt = conn.prepareStatement(sendDataQuery);
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCnt = rsmd.getColumnCount();
            list = new ArrayList<>();
            rs.setFetchSize(100);

            while(rs.next()) {
                Hashtable<String, Object> ht = new Hashtable<>();
                for(int i = 1; i <= columnCnt; i++) {
                    ht.put(fieldTable.get(String.valueOf(i)), rs.getString(i));
                    if(rsmd.getColumnName(i).equals("CUSTOMER_TEL")) {
                        ht.put("S", rs.getString("CUSTOMER_TEL"));
                    } else if(rsmd.getColumnName(i).equals("CUSTOMER_EMAIL")) {
                        ht.put("E", rs.getString("CUSTOMER_EMAIL"));
                    }
                }
                list.add(ht);
            }
        } catch(Exception e) {
            log.error("[ERROR] : " + e.getMessage());
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(conn);
        }

        return list;
    }

    public void dbinit() {
        if(isdbinit) return;

        DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(1);
        if(dbInfoVo != null) {
            this.dbInfoVo = dbInfoVo;
            isdbinit = true;
        }else {
            log.error("NVDBINFO seq[1] is empty. Register wiseu database");
        }
    }
}
