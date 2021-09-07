package com.mnwise.wiseu.web.report.service.campaign;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.ZipUtil;
import com.mnwise.wiseu.web.campaign.dao.ApplicationDao;
import com.mnwise.wiseu.web.campaign.dao.CampaignDao;
import com.mnwise.wiseu.web.campaign.dao.ScenarioDao;
import com.mnwise.wiseu.web.campaign.dao.SendResultDao;
import com.mnwise.wiseu.web.campaign.dao.TemplateDao;
import com.mnwise.wiseu.web.editor.model.HandlerVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignSendResultVo;
import com.mnwise.wiseu.web.report.model.campaign.ScenarioInfoVo;

/**
 * 캠페인 공통 서비스
 */
@Service
public class CampaignCommonService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(CampaignCommonService.class);

    @Autowired private ApplicationDao applicationDao;
    @Autowired private CampaignDao campaignDao;
    @Autowired private ScenarioDao scenarioDao;
    @Autowired private SendResultDao sendResultDao;
    @Autowired private TemplateDao templateDao;

    @Value("${report.env.data.tmp}")
    private String filePath;

    public int getMaxCampaignNo(ScenarioInfoVo scenarioInfoVo) {
        return campaignDao.getMaxCampaignNo(scenarioInfoVo);
    }

    public ScenarioInfoVo selectScenarioInfo(int campaignNo) {
        return scenarioDao.selectScenarioInfo(campaignNo);
    }

    /**
     * 캠페인 요약정보 nvsendresult 값의 정보를 보여준다
     */
    public CampaignSendResultVo selectSendResult(ScenarioInfoVo scenarioInfoVo) {
        return sendResultDao.selectSendResult(scenarioInfoVo);
    }

    /**
     * 캠페인별 템플릿 목록 조회
     *
     * @param campaignNo 캠페인 번호
     * @return
     */
    public List<TemplateVo> getTemplateList(int campaignNo) {
        return templateDao.getTemplateList(campaignNo);
    }

    /**
     * 템플릿 조회
     *
     * @param templateVo 템플릿 정보
     * @return
     */
    public TemplateVo getTemplate(TemplateVo templateVo) {
        // 수신확인 태그를 제거하기 위해서 핸들러 타입이 G(groovy)/S(ASE) 인지 알기 위해 쿼리한다.
        HandlerVo handlerVo = applicationDao.selectEditorCampaignHandler(templateVo.getCampaignNo());
        templateVo = templateDao.getTemplate(templateVo);
        if(templateVo != null) {
            templateVo.setHandlerType(handlerVo.getType());
        }

        return templateVo;
    }

    /**
     * 오픈 고객 리스트 다운로드 여러개의 캠페인을 동시에 다운로드 할 수 있으므로 파일 생성후 zip으로 압축하고 생성된 파일명을 리턴한다.
     *
     * @param campaignNo 캠페인 번호
     * @return
     */
    public String makeCsvReceiptList(int[] campaignNo) {
        BufferedOutputStream out = null;
        String zipPath = null;
        String[] files = null;
        String[] names = null;

        try {
            files = new String[campaignNo.length];
            names = new String[campaignNo.length];

            String path = null;
            String name = null;

            for(int i = 0; i < campaignNo.length; i++) {
                name = "open_cust_" + campaignNo[i] + "_" + DateUtil.dateToString("yyyyMMddhhmmss", new Date()) + ".csv";
                path = filePath + "/" + name;
                FileUtil.forceMkdir(new File(filePath));
                out = new BufferedOutputStream(new FileOutputStream(new File(path)));
                makeCsvReceiptList(out, campaignNo[i]);

                files[i] = path;
                names[i] = name;

                out.flush();
            }

            zipPath = filePath + "/open_cust_" + DateUtil.dateToString("yyyyMMddhhmmss", new Date()) + ".zip";
            ZipUtil.compressZip(files, names, zipPath);
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            try {
                // 생성된 csv 파일 삭제
                for(int i = 0; i < files.length; i++) {
                    new File(files[i]).delete();
                }
            } catch(Exception e2) {
                log.warn(null, e2);
            }
        }

        return zipPath;
    }

    /**
     * 오픈 고객 리스트 다운로드. 오픈 고객 리스트가 많을 경우 스프링을 이용하면 메모리 에러가 나기 때문에 jdbc로 커서에 붙어서 데이타를 생성함 (1000만건 테스트 완료)
     *
     * @param campaignNo 캠페인 번호
     * @return
     */
    public void makeCsvReceiptList(OutputStream out, int campaignNo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            // KSM 본발송만 거르도록 조건 추가
            StringBuffer sql = new StringBuffer();
            //SECUDB NONE
            //NVRECEIPT : CUSTOMER_NM, CUSTOMER_EMAIL
            sql.append("\n  SELECT X.CAMPAIGN_NO                                     ");
            sql.append("\n       ,X.RESULT_SEQ                                      ");
            sql.append("\n       ,X.LIST_SEQ                                        ");
            sql.append("\n       ,X.RECORD_SEQ                                      ");
            sql.append("\n       ,X.CUSTOMER_ID                                     ");
            sql.append("\n       ,X.CUSTOMER_NM                                     ");
            sql.append("\n       ,X.CUSTOMER_EMAIL                                  ");
            sql.append("\n       ,X.OPEN_DT                                         ");
            sql.append("\n       ,X.OPEN_TM                                         ");
            sql.append("\n       ,X.READING_DT                                      ");
            sql.append("\n       ,X.READING_TM                                      ");
            sql.append("\n       ,X.READING_DURATION                                ");
            sql.append("\n       ,X.VALID_CNT                                       ");
            sql.append("\n   FROM NVRECEIPT X, NVSENDRESULT SR                      ");
            sql.append("\n   WHERE X.RESULT_SEQ = SR.RESULT_SEQ                         ");
            sql.append("\n   AND X.CAMPAIGN_NO = ?                                     ");
            sql.append("\n   AND SR.RESEND_STS IS NULL                                    ");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, campaignNo);
            rs = pstmt.executeQuery();

            String cusId;
            while(rs.next()) {
                out.write(printObjectToBytes(rs.getString("campaign_no")));
                out.write(',');
                // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에 '''를
                // 붙여준다.
                cusId = rs.getString("customer_id");
                if(cusId.trim().startsWith("=")) {
                    cusId = "'" + cusId;
                }
                out.write(printObjectToBytes(cusId));
                out.write(',');
                out.write(printObjectToBytes(rs.getString("open_dt")));
                out.write(',');
                out.write(printObjectToBytes(rs.getString("open_tm")));
                out.write('\n');
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(conn);
        }
    }

    private byte[] printObjectToBytes(Object value) {
        return (value == null) ? "".getBytes() : value.toString().getBytes();
    }
}
