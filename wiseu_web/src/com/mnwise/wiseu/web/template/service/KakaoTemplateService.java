package com.mnwise.wiseu.web.template.service;

import static com.mnwise.wiseu.web.common.util.ExcelUtils.cellReader;
import static com.mnwise.wiseu.web.common.util.ExcelUtils.rowToArray;
import static com.mnwise.wiseu.web.common.util.KakaoRestApiUtil.FAIL_CODE;
import static com.mnwise.wiseu.web.common.util.KakaoRestApiUtil.SUCCESS_CODE;
import static com.mnwise.wiseu.web.common.util.KakaoRestApiUtil.callRestApi;
import static com.mnwise.wiseu.web.common.util.KakaoRestApiUtil.createParameters;
import static com.mnwise.wiseu.web.common.util.KakaoRestApiUtil.getTmplMsgType;
import static com.mnwise.wiseu.web.common.util.KakaoRestApiUtil.parseSingleResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import com.csvreader.CsvWriter;
import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.common.util.JsonUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.admin.model.AdminSessionVo;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.common.dao.CdMstDao;
import com.mnwise.wiseu.web.common.service.AbstractTemplateService;
import com.mnwise.wiseu.web.common.util.PropertyUtil;
import com.mnwise.wiseu.web.template.dao.KakaoProfileDao;
import com.mnwise.wiseu.web.template.model.KakaoButton;
import com.mnwise.wiseu.web.template.model.KakaoProfile;
import com.mnwise.wiseu.web.template.model.KakaoQuickReply;
import com.mnwise.wiseu.web.template.model.KakaoSingleResponse;
import com.mnwise.wiseu.web.template.model.KakaoTemplateVo;
import com.mnwise.wiseu.web.template.model.MobileVo;
import com.mnwise.wiseu.web.template.util.KakaoButtonUtils;

@Service
public class KakaoTemplateService extends AbstractTemplateService {
    private static final Logger log = LoggerFactory.getLogger(KakaoTemplateService.class);
    //[파일 업로드]템플릿 정보 시작 번호
    private final static int HEADER_ROWNUM = 4;
    //[파일업로드]템플릿 정보 필수 입력 값 인덱스 번호
    private final static int[] requiredIndex = {0 , 1 , 2 , 3 , 9 , 10};
    @Autowired private CdMstDao cdMstDao;
    @Autowired private KakaoProfileDao kakaoProfileDao;
    /**
     * 선택한 알림톡 템플릿에 코멘트를 추가
     * 파일 첨부하여 코멘트 기능 추가
     *
     * @param jsonParam 카카오 REST API에 사용할 파라미터
     * @return
     */
    public String addAlimtalkTemplateComment(String jsonParam , List<MultipartFile> multipartFileList) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> param = (Map<String, String>) JsonUtil.toObject(jsonParam, Map.class);

            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            AdminSessionVo adminSessionVo = (AdminSessionVo) WebUtils.getSessionAttribute(request, "adminSessionVo");

            UserVo userVo = adminSessionVo.getUserVo();

            int contsNo = Integer.parseInt(param.get("contsNo"));
            if(StringUtil.isNotBlank(String.valueOf(contsNo))) {
                String requestUserId = userVo.getUserId();

                MobileVo mobileVo = new MobileVo();
                mobileVo.setContsNo(contsNo);
                mobileVo.setUserId(requestUserId);

                MobileVo vo = findAlimtalkTemplateInfo(mobileVo);

                if(requestUserId.equals(vo.getUserId())) {
                    String senderKey = vo.getKakaoSenderKey();
                    String tmplCd = vo.getKakaoTmplCd();

                    String comment = param.get("comment");

                    if(StringUtil.isNotBlank(comment)) {
                        List<NameValuePair> params = createParameters(senderKey, "S", tmplCd, "", "");
                        params.add(new BasicNameValuePair("comment", comment));
                        String jsonData = callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.comment_file"), params, multipartFileList);
                        if(StringUtil.isBlank(jsonData)) {
                            log.error("Kakao Template Response is empty");
                            return FAIL_CODE + ":Response is empty";
                        } else {
                            KakaoSingleResponse kakaoResponse = parseSingleResponse(jsonData);
                            String code = kakaoResponse.getCode();

                            if(SUCCESS_CODE.equals(code)) {
                                log.info("Kakao Template Comment Submit is completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                                return SUCCESS_CODE;
                            } else {
                                String message = kakaoResponse.getMessage();
                                log.error("Kakao Template Comment Submit is not completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                                log.error("Result Code: " + code + ", Message: " + message);
                                return code + ":" + message;
                            }
                        }
                    }
                }
            }
            return FAIL_CODE + ":Invalid Parameter";
        } catch(Exception e) {
            String message = e.getMessage();
            log.error("while requesting kakao template commnet add, exception occurred. " + message);
            return FAIL_CODE + ":" + message;
        }
    }

    /**
     * 선택한 알림톡 템플릿에 코멘트를 추가한다.
     *
     * @param jsonParam 카카오 REST API에 사용할 파라미터
     * @return
     */
    public String addAlimtalkTemplateComment(String jsonParam) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> param = (Map<String, String>) JsonUtil.toObject(jsonParam, Map.class);

            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            AdminSessionVo adminSessionVo = (AdminSessionVo) WebUtils.getSessionAttribute(request, "adminSessionVo");

            UserVo userVo = adminSessionVo.getUserVo();

            int contsNo = Integer.parseInt(param.get("contsNo"));
            if(StringUtil.isNotBlank(String.valueOf(contsNo))) {
                String requestUserId = userVo.getUserId();

                MobileVo mobileVo = new MobileVo();
                mobileVo.setContsNo(contsNo);
                mobileVo.setUserId(requestUserId);

                MobileVo vo = findAlimtalkTemplateInfo(mobileVo);

                if(requestUserId.equals(vo.getUserId())) {
                    String senderKey = vo.getKakaoSenderKey();
                    String tmplCd = vo.getKakaoTmplCd();

                    String comment = param.get("comment");

                    if(StringUtil.isNotBlank(comment)) {
                        List<NameValuePair> params = createParameters(senderKey, "S", tmplCd, "", "");
                        params.add(new BasicNameValuePair("comment", comment));

                        String jsonData = callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.comment"), params);

                        if(StringUtil.isBlank(jsonData)) {
                            log.error("Kakao Template Response is empty");
                            return FAIL_CODE + ":Response is empty";
                        } else {
                            KakaoSingleResponse kakaoResponse = parseSingleResponse(jsonData);
                            String code = kakaoResponse.getCode();

                            if(SUCCESS_CODE.equals(code)) {
                                log.info("Kakao Template Comment Submit is completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                                return SUCCESS_CODE;
                            } else {
                                String message = kakaoResponse.getMessage();
                                log.error("Kakao Template Comment Submit is not completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                                log.error("Result Code: " + code + ", Message: " + message);
                                return code + ":" + message;
                            }
                        }
                    }
                }
            }
            return FAIL_CODE + ":Invalid Parameter";
        } catch(Exception e) {
            String message = e.getMessage();
            log.error("while requesting kakao template commnet add, exception occurred. " + message);
            return FAIL_CODE + ":" + message;
        }
    }

    /**
     * 알림톡 템플릿을 새로 생성한다.
     *
     * @param paramMap 카카오 REST API에 사용할 파라미터
     * @return
     */
    public void createAlimtalkTemplate(Map<String, String> paramMap, String userId, List<MultipartFile> multipartFileList) throws Exception {
        String senderKey = paramMap.get("kakaoSenderKey");
        String tmplCd = paramMap.get("kakaoTmplCd");
        String tmplNm = paramMap.get("contsNm");
        String tmplContent = paramMap.get("content");

        if(StringUtil.isNotBlank(senderKey) && StringUtil.isNotBlank(tmplCd) && StringUtil.isNotBlank(tmplNm) && StringUtil.isNotBlank(tmplContent)) {
            List<NameValuePair> params = createParameters(senderKey, "S", tmplCd, tmplNm, tmplContent);
            String json = paramMap.get("buttons");
            if(StringUtil.isNotBlank(json)) {
                List<NameValuePair> paramList = KakaoButtonUtils.convertButtonToHttpParamList(JsonUtil.toList(json, KakaoButton.class));
                for(NameValuePair pair : paramList) {
                    params.add(pair);
                }
            }

            String kakaoQuickReplies = paramMap.get("kakaoQuickReplies");
            if(StringUtil.isNotBlank(kakaoQuickReplies)) {
                List<NameValuePair> paramList = KakaoButtonUtils.convertButtonToHttpParamList(JsonUtil.toList(kakaoQuickReplies, KakaoQuickReply.class));
                for(NameValuePair pair : paramList) {
                    params.add(pair);
                }
            }

            String categoryCd = paramMap.get("categoryCd");
            params.add(new BasicNameValuePair("categoryCode", categoryCd));
            String kakaoSecurityYn = paramMap.get("kakaoSecurityYn");
            params.add(new BasicNameValuePair("securityFlag", String.valueOf("Y".equals(kakaoSecurityYn))));
            String kakaoEmType = paramMap.get("kakaoEmType");
            params.add(new BasicNameValuePair("templateEmphasizeType", kakaoEmType));
            String kakaoEmTitle = paramMap.get("kakaoEmTitle");
            String kakaoEmSubtitle = paramMap.get("kakaoEmSubtitle");

            if ("TEXT".equals(kakaoEmType)) {
                params.add(new BasicNameValuePair("templateTitle", kakaoEmTitle));
                params.add(new BasicNameValuePair("templateSubtitle", kakaoEmSubtitle));
            }

            String kakaoTmplMsgType = paramMap.get("kakaoTmplMsgType");
            params.add(new BasicNameValuePair("templateMessageType", kakaoTmplMsgType));
            String kakaoTmplEx = paramMap.get("kakaoTmplEx");
            String kakaoTmplAd = paramMap.get("kakaoTmplAd");

            if ("EX".equals(kakaoTmplMsgType) || "MI".equals(kakaoTmplMsgType)) {
                params.add(new BasicNameValuePair("templateExtra", kakaoTmplEx));
            }
            if ("AD".equals(kakaoTmplMsgType) || "MI".equals(kakaoTmplMsgType)) {
                params.add(new BasicNameValuePair("templateAd", kakaoTmplAd));
            }
            String jsonData ;
            if(multipartFileList.size()>0) {
                jsonData = callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.createWithImage"), params , multipartFileList);
            }else {
                jsonData = callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.create"), params);
            }

            if(StringUtil.isBlank(jsonData)) {
                throw new Exception("[" + tmplCd + "] Kakao Template Response is empty");
            } else {
                KakaoSingleResponse response = parseSingleResponse(jsonData);
                String code = response.getCode();

                if(SUCCESS_CODE.equals(code)) {
                    MobileVo mobileVo = new MobileVo();
                    mobileVo.setUserId(userId);
                    mobileVo.setFileType(Channel.ALIMTALK);
                    mobileVo.setAuthType(paramMap.get("authType"));
                    mobileVo.setKakaoSenderKey(senderKey);
                    mobileVo.setKakaoTmplCd(tmplCd);
                    mobileVo.setContsNm(StringUtil.escapeXss(tmplNm));
                    mobileVo.setCategoryCd(categoryCd);
                    mobileVo.setContsDesc(StringUtil.escapeXss(paramMap.get("contsDesc")));
                    mobileVo.setContsTxt(StringUtil.escapeXss(tmplContent));

                    String date = DateUtil.dateToString("yyyMMddHHmmss", new Date());
                    mobileVo.setCreateDt(date.substring(0, 8));
                    mobileVo.setCreateTm(date.substring(8));
                    mobileVo.setContsNo(selectNextContsNo());

                    KakaoTemplateVo data = response.getData();
                    mobileVo.setKakaoInspStatus(data.getInspectionStatus());
                    mobileVo.setKakaoTmplStatus(data.getStatus());
                    mobileVo.setKakaoButtons(data.convertButtonsToJson());
                    mobileVo.setKakaoTmplMsgType(kakaoTmplMsgType);
                    if ("AD".equals(kakaoTmplMsgType) || "MI".equals(kakaoTmplMsgType)) {
                        mobileVo.setKakaoTmplAd(kakaoTmplAd);
                    }
                    if ("EX".equals(kakaoTmplMsgType) || "MI".equals(kakaoTmplMsgType)) {
                        mobileVo.setKakaoTmplEx(kakaoTmplEx);
                    }
                    mobileVo.setKakaoSecurityYn(kakaoSecurityYn);
                    mobileVo.setKakaoEmType(kakaoEmType);
                    if (Const.KkoEmType.TEXT.equals(kakaoEmType)) {
                        mobileVo.setKakaoEmTitle(kakaoEmTitle);
                        mobileVo.setKakaoEmSubtitle(kakaoEmSubtitle);
                    }
                    mobileVo.setKakaoQuickReplies(kakaoQuickReplies);
                    mobileVo.setImageUrl(data.getTemplateImageUrl());
                    mobileVo.setFilePreviewName(paramMap.get("fileNm"));
                    mobileVo.setFilePreviewPath(paramMap.get("filePreviewPath"));
                    mobileVo.setFilePath(paramMap.get("filePath"));

                    insertMobileContent(mobileVo);
                    log.info("Kakao Template is created. Sender Key: " + senderKey + " Template Code: " + tmplCd);
                } else {
                    String message = response.getMessage();
                    log.error("Kakao Template is not created. Sender Key: " + senderKey + " Template Code: " + tmplCd);
                    log.error("Result Code: " + code + ", Message: " + message);
                    throw new Exception("[" + tmplCd + "] " + message + "(" + code + ")");
                }
            }
        } else {
            throw new Exception("[" + tmplCd + "] Invalid Parameter.");
        }
    }

    /**
     * 선택한 알림톡 템플릿을 삭제한다.<br>
     * 템플릿 검수상태가 승인(APR) 이 아닌 경우에만 삭제 가능.
     *
     * @param contsNoArr 검수 요청할 템플릿 번호 배열
     * @return
     */
    public void deleteAlimtalkTemplate(int[] contsNoArr, String userId) throws Exception {
        if(contsNoArr == null || contsNoArr.length == 0) {
            throw new Exception("Invalid Parameter.");
        }

        for(int contsNo : contsNoArr) {
            MobileVo paramVo = new MobileVo();
            paramVo.setContsNo(contsNo);
            paramVo.setUserId(userId);
            MobileVo mobileVo = findAlimtalkTemplateInfo(paramVo);

            String senderKey = mobileVo.getKakaoSenderKey();
            String tmplCd = mobileVo.getKakaoTmplCd();

            List<NameValuePair> params = createParameters(senderKey, "S", tmplCd, "", "");
            String jsonData = callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.delete"), params);

            if(StringUtil.isBlank(jsonData)) {
                throw new Exception("[" + tmplCd + "] Kakao Template Response is empty");
            } else {
                KakaoSingleResponse response = parseSingleResponse(jsonData);
                String code = response.getCode();

                if(SUCCESS_CODE.equals(code)) {
                    log.info("Kakao Template Delete is completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                    MobileVo vo = new MobileVo();
                    vo.setKakaoSenderKey(senderKey);
                    vo.setKakaoTmplCd(tmplCd);

                    int cnt = deleteAlimtalkTemplate(vo);
                    log.info("Alimtalk Template(" + cnt + ") Deleted.");
                } else {
                    log.error("Kakao Template Delete is not completed. Result Code: " + code + ", Message: " + response.getMessage() + ", Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                    throw new Exception("[" + tmplCd + "] " + response.getMessage() + "(" + code + ")");
                }
            }
        }
    }

    /**
     * 선택한 템플릿을 카카오에 검수요청한다.
     *
     * @param contsNoArr 검수 요청할 템플릿 번호 배열
     * @return
     */
    public void inspectAlimtalkTemplate(int[] contsNoArr, String userId) throws Exception {
        for(int contsNo : contsNoArr) {
            MobileVo paramVo = new MobileVo();
            paramVo.setContsNo(contsNo);
            paramVo.setUserId(userId);
            MobileVo mobileVo = findAlimtalkTemplateInfo(paramVo);

            String senderKey = mobileVo.getKakaoSenderKey();
            String tmplCd = mobileVo.getKakaoTmplCd();
            List<NameValuePair> params = createParameters(senderKey, "S", tmplCd, "", "");
            String jsonData = callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.request"), params);

            if(StringUtil.isBlank(jsonData)) {
                throw new Exception("[" + tmplCd + "] Kakao Template Response is empty");
            } else {
                KakaoSingleResponse response = parseSingleResponse(jsonData);
                String code = response.getCode();

                if(SUCCESS_CODE.equals(code)) {
                    log.info("Kakao Template Request is completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                } else {
                    log.error("Kakao Template Request is not completed. Result Code: " + code + ", Message: " + response.getMessage() + ", Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                    throw new Exception("[" + tmplCd + "] " + response.getMessage() + "(" + code + ")");
                }
            }
        }
    }

    /**
     * 선택한 템플릿을 카카오에 검수요청 취소한다.
     *
     * @param contsNoArr 검수 요청 취소할 템플릿 번호 배열
     * @return
     */
    public void cancleInspectAlimtalkTemplate(int[] contsNoArr, String userId) throws Exception {
        for(int contsNo : contsNoArr) {
            MobileVo paramVo = new MobileVo();
            paramVo.setUserId(userId);
            paramVo.setContsNo(contsNo);
            MobileVo mobileVo = findAlimtalkTemplateInfo(paramVo);

            String senderKey = mobileVo.getKakaoSenderKey();
            String tmplCd = mobileVo.getKakaoTmplCd();
            List<NameValuePair> params = createParameters(senderKey, "S", tmplCd, "", "");
            String jsonData = callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.cancelRequest"), params);

            if(StringUtil.isBlank(jsonData)) {
                throw new Exception("[" + tmplCd + "] Kakao Template Response is empty");
            } else {
                KakaoSingleResponse response = parseSingleResponse(jsonData);
                String code = response.getCode();

                if(SUCCESS_CODE.equals(code)) {
                    log.info("Kakao Template Cancel Request is completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                } else {
                    log.error("Kakao Template Cancel Request is not completed. Result Code: " + code + ", Message: " + response.getMessage() + ", Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                    throw new Exception("[" + tmplCd + "] " + response.getMessage() + "(" + code + ")");
                }
            }
        }
    }

    /**
     * 검수가 안 된 모든 템플릿을 카카오에 검수요청한다.
     *
     * @return
     */
    public void inspectAllAlimtalkTemplate(String userId) throws Exception {
        List<MobileVo> list = findRegStatusTemplate(userId);

        for(MobileVo mobileVo : list) {
            String senderKey = mobileVo.getKakaoSenderKey();
            String tmplCd = mobileVo.getKakaoTmplCd();
            List<NameValuePair> params = createParameters(senderKey, "S", tmplCd, "", "");
            String jsonData = callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.request"), params);

            if(StringUtil.isBlank(jsonData)) {
                throw new Exception("[" + tmplCd + "] Kakao Template Response is empty");
            } else {
                KakaoSingleResponse response = parseSingleResponse(jsonData);
                String code = response.getCode();

                if(SUCCESS_CODE.equals(code)) {
                    log.info("Kakao Template Request is completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                } else {
                    log.error("Kakao Template Request is not completed. Result Code: " + code + ", Message: " + response.getMessage() + ", Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                    throw new Exception("[" + tmplCd + "] " + response.getMessage() + "(" + code + ")");
                }
            }
        }
    }

    /**
     * 특정 알림톡 템플릿을 카카오에 업데이트 요청한다.<br>
     *
     * - 업데이트 가능한 항목 : 템플릿 제목, 템플릿 내용, 템플릿 코드
     *
     * @param templateNo 삭제할 템플릿 번호
     * @return
     */
    public void updateAlimtalkTemplate(Map<String, String> paramMap, UserVo userVo,List<MultipartFile> multipartFileList) throws Exception {
        int contsNo = Integer.parseInt(paramMap.get("contsNo"));
        if(StringUtil.isNotBlank(String.valueOf(contsNo))) {
            MobileVo mobileVo = new MobileVo();
            mobileVo.setContsNo(contsNo);
            mobileVo.setUserId(userVo.getUserId());

            MobileVo vo = findAlimtalkTemplateInfo(mobileVo);

            String loginUserId = userVo.getUserId();
            String loginUserType = userVo.getUserTypeCd();
            String loginUserGrp = userVo.getGrpCd();

            // 사용자 타입이 관리자('A')이거나 같은 부서 매니저('M')이거나 템플릿을 등록한 사용자 ID와 동일한 경우만 템플릿 수정이 가능
            if("A".equals(loginUserType) || "M".equals(loginUserType) && loginUserGrp.equals(vo.getGrpCd()) || loginUserId.equals(vo.getUserId())) {
                String senderKey = vo.getKakaoSenderKey();
                String tmplCd = vo.getKakaoTmplCd();

                String newContsNm = paramMap.get("contsNm");
                String newContent = paramMap.get("content");
                String newTmplCd = paramMap.get("kakaoTmplCd");
                String categoryCd = paramMap.get("categoryCd");
                String contsDesc = paramMap.get("contsDesc");
                String authType = paramMap.get("authType");
                String useYn = paramMap.get("useYn");
                String modifyPossible = paramMap.get("modifyPossible");
                String newKakaoTmplMsgType = paramMap.get("kakaoTmplMsgType");
                String newKakaoTmplEx = paramMap.get("kakaoTmplEx");
                String newKakaoTmplAd = paramMap.get("kakaoTmplAd");
                String newKakaoEmType = paramMap.get("kakaoEmType");
                String newKakaoEmTitle = paramMap.get("kakaoEmTitle");
                String newKakaoEmSubtitle = paramMap.get("kakaoEmSubtitle");
                String newKakaoSecurityYn = paramMap.get("kakaoSecurityYn");

                if("N".equals(modifyPossible)) {
                    MobileVo paramVo = new MobileVo();
                    paramVo.setContsNo(contsNo);
                    paramVo.setCategoryCd(categoryCd);
                    paramVo.setContsDesc(contsDesc);
                    paramVo.setAuthType(authType);
                    paramVo.setUseYn(useYn);

                    int cnt = updateKakaoTemplateAuthType(paramVo);

                    log.info("Alimtalk TemplateAuthType(" + cnt + ") Updated.");
                    return;
                }

                if(StringUtil.isNotBlank(newTmplCd) && StringUtil.isNotBlank(newContsNm) && StringUtil.isNotBlank(newContent)) {
                    List<NameValuePair> params = createParameters(senderKey, "S", tmplCd, "", "");
                    params.add(new BasicNameValuePair("newSenderKey", senderKey));
                    params.add(new BasicNameValuePair("newTemplateCode", newTmplCd));
                    params.add(new BasicNameValuePair("newTemplateName", newContsNm));
                    params.add(new BasicNameValuePair("newTemplateContent", newContent));
                    params.add(new BasicNameValuePair("newCategoryCode", categoryCd));
                    params.add(new BasicNameValuePair("newTemplateMessageType", newKakaoTmplMsgType));

                    if ("EX".equals(newKakaoTmplMsgType) || "MI".equals(newKakaoTmplMsgType)) {
                        params.add(new BasicNameValuePair("newTemplateExtra", newKakaoTmplEx));
                    }
                    if ("AD".equals(newKakaoTmplMsgType) || "MI".equals(newKakaoTmplMsgType)) {
                        params.add(new BasicNameValuePair("newTemplateAd", newKakaoTmplAd));
                    }

                    params.add(new BasicNameValuePair("newTemplateEmphasizeType", newKakaoEmType));
                    if ("TEXT".equals(newKakaoEmType)) {
                        params.add(new BasicNameValuePair("newTemplateTitle", newKakaoEmTitle));
                        params.add(new BasicNameValuePair("newTemplateSubtitle", newKakaoEmSubtitle));
                    }

                    params.add(new BasicNameValuePair("securityFlag", String.valueOf("Y".equals(newKakaoSecurityYn))));
                    String buttons = paramMap.get("buttons");
                    if (StringUtil.isNotBlank(buttons)) {
                        List<NameValuePair> paramList = KakaoButtonUtils.convertButtonToHttpParamList(JsonUtil.toList(buttons, KakaoButton.class));
                        for(NameValuePair pair : paramList) {
                            params.add(pair);
                        }
                    }
                    String kakaoQuickReplies = paramMap.get("kakaoQuickReplies");
                    if (StringUtil.isNotBlank(kakaoQuickReplies)) {
                        List<NameValuePair> quickReplyList = KakaoButtonUtils.convertButtonToHttpParamList(JsonUtil.toList(kakaoQuickReplies, KakaoQuickReply.class));
                        for(NameValuePair pair : quickReplyList) {
                            params.add(pair);
                        }
                    }
                    String jsonData ;
                    if(multipartFileList.size()>0) {
                        jsonData = callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.updateWithImage"), params , multipartFileList);
                    }else {
                        jsonData = callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.update"), params);
                    }

                    if(StringUtil.isBlank(jsonData)) {
                        throw new Exception("[" + tmplCd + "] Kakao Template Response is empty");
                    } else {
                        KakaoSingleResponse kakaoResponse = parseSingleResponse(jsonData);
                        String code = kakaoResponse.getCode();

                        if(SUCCESS_CODE.equals(code)) {
                            log.info("Kakao Template Update is completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                            KakaoTemplateVo templateVo = kakaoResponse.getData();

                            MobileVo paramVo = new MobileVo();
                            paramVo.setContsNo(contsNo);
                            paramVo.setKakaoSenderKey(senderKey);
                            paramVo.setNewKakaoSenderKey(templateVo.getSenderKey());
                            paramVo.setKakaoTmplCd(tmplCd);
                            paramVo.setNewKakaoTmplCd(templateVo.getTemplateCode());
                            paramVo.setContsNm(templateVo.getTemplateName());
                            paramVo.setContsTxt(templateVo.getTemplateContent());
                            paramVo.setKakaoInspStatus(templateVo.getInspectionStatus());
                            paramVo.setKakaoTmplStatus(templateVo.getStatus());
                            paramVo.setCategoryCd(templateVo.getCategoryCode());
                            paramVo.setContsDesc(contsDesc);
                            paramVo.setAuthType(authType);
                            paramVo.setUseYn(useYn);
                            paramVo.setKakaoButtons(templateVo.convertButtonsToJson());
                            paramVo.setKakaoTmplMsgType(templateVo.getTemplateMessageType());
                            paramVo.setKakaoTmplAd(templateVo.getTemplateAd());
                            paramVo.setKakaoTmplEx(templateVo.getTemplateExtra());
                            paramVo.setKakaoEmType(templateVo.getTemplateEmphasizeType());
                            paramVo.setKakaoEmTitle(templateVo.getTemplateTitle());
                            paramVo.setKakaoEmSubtitle(templateVo.getTemplateSubtitle());
                            paramVo.setKakaoSecurityYn("true".equals(templateVo.getSecurityFlag()) ? "Y" : "N");
                            paramVo.setKakaoQuickReplies(templateVo.convertQuickRepliesToJson());
                            paramVo.setImageUrl(templateVo.getTemplateImageUrl());
                            paramVo.setFilePreviewName(paramMap.get("fileNm"));
                            paramVo.setFilePreviewPath(paramMap.get("filePreviewPath"));
                            paramVo.setFilePath(paramMap.get("filePath"));

                            int cnt = updateKakaoTemplate(paramVo);
                            log.info("Alimtalk Template(" + cnt + ") Updated.");
                            return;
                        } else {
                            String message = kakaoResponse.getMessage();
                            log.error("Kakao Template Update is not completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                            log.error("Result Code: " + code + ", Message: " + message);
                            throw new Exception("[" + tmplCd + "] " + message + "(" + code + ")");
                        }
                    }
                }
            } else {
                throw new Exception("You don't have permission to change it.");
            }
        } else {
            throw new Exception("Invalid Parameter.");
        }
    }

    /**
     * 알림톡 템플릿 파일을 한 Row 씩 읽어와서 알림톡 REST API 서버에 전송하고 NVMOBILECONTENTS 테이블에 INSERT
     *
     * @param userId
     * @param authType
     * @param contsDesc
     * @throws Exception
     */
    public void createAlimtalkTemplate(String userId, String authType, String contsDesc, String fileName) throws Exception {
        final String uploadFilePath = StringUtil.escapeFilePath(getImportedFilePath(fileName));

        final String errFileNm = this.importDir + "/uploadError_"+System.currentTimeMillis()+".csv";
        final CsvWriter csvWriter = new CsvWriter(errFileNm, ',', Charset.forName("euc-kr"));
        Workbook workbook = null;

        try {
            workbook = WorkbookFactory.create(new FileInputStream(uploadFilePath));
            workbook.setMissingCellPolicy(MissingCellPolicy.CREATE_NULL_AS_BLANK);
            for(Sheet sheet : workbook) {
                //만일 마지막 RowNum이 1004보다 크다면, 업로드 하고자 하는 갯수가 1000개가 넘음
                if(sheet.getLastRowNum() >= 1004) {
                    throw new Exception("1000개 이상의 템플릿을 한번에 업로드 하실 수 없습니다.");
                }
                int rowNo=0;
                for(Row row : sheet) {
                    try {
                            rowNo = row.getRowNum();
                            if(HEADER_ROWNUM >= rowNo || StringUtils.isBlank(row.getCell(0).getStringCellValue())){
                               continue;
                            }
                            createAlimtalkTemplate(createKakaoMap(row , authType, contsDesc , userId), userId, new ArrayList<MultipartFile>());
                     }catch(Exception e1) {
                         csvWriter.writeRecord((rowToArray(row)));
                         log.error("[Kakao Template File Upload Error] line Number:{} {}", rowNo , e1);
                     }
                }
        }
        } catch(Exception e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(workbook);
            csvWriter.close();
            final File uploadFile = new File(uploadFilePath);
            if(uploadFile.exists()) {
                uploadFile.delete();
            }
        }
    }


    /** 파일 업로드시 필요한 data를 엑셀에서 읽어 map에 설정
     * @param String[] data
     * @return Map
     */
    private Map<String, String> createKakaoMap(Row row, String authType, String contsDesc, String userId) throws Exception{
        Map<String, String> paramMap= new HashMap<String, String>();
        paramMap.put("authType", authType);
        paramMap.put("contsDesc", contsDesc);
        String yellowId = cellReader(row.getCell(0));
        String kakaoTmplCd = cellReader(row.getCell(1));
        kakaoTmplCd = StringUtil.deleteWhitespace(kakaoTmplCd); //공백제거
        String contsNm = cellReader(row.getCell(2));
        String kakaoTmplMsgType = cellReader(row.getCell(3));
        String content = cellReader(row.getCell(4));
        String kakaoSecurityYn = "TRUE".equals(cellReader(row.getCell(9)))?"Y":"N";
        //필수값 체크
        if(StringUtils.isBlank(kakaoTmplCd)
            || StringUtils.isBlank(contsNm) || StringUtils.isBlank(kakaoTmplMsgType)
            ||StringUtils.isBlank(content)) {
            throw new Exception("Kakao Template required Error");
        }
        String kakaoEmTitle = cellReader(row.getCell(7));
        String kakaoEmSubtitle = cellReader(row.getCell(8));

        KakaoProfile profile = kakaoProfileDao.selectKakaoProfileByYellowId(yellowId, userId);
        if(profile == null || StringUtils.isBlank(profile.getKakaoYellowId())) {
            throw new Exception("Kakao channel id does not exist." + row.getCell(0).getStringCellValue());
        }
        paramMap.put("kakaoSenderKey", profile.getKakaoSenderKey()); //발신프로필키 (필수)
        paramMap.put("kakaoTmplCd", kakaoTmplCd);    //템플릿 코드 (필수)
        paramMap.put("contsNm",contsNm);    //템플릿명 (필수)
        paramMap.put("kakaoTmplMsgType",getTmplMsgType(kakaoTmplMsgType));//템플릿 유형 (필수)
        paramMap.put("content",content);        //템플릿 내용 (필수)
        paramMap.put("kakaoTmplEx",cellReader(row.getCell(5)));    //부가정보
        paramMap.put("kakaoTmplAd",cellReader(row.getCell(6)));    //광고성메시지
        if(StringUtil.isNotBlank(kakaoEmTitle)  || StringUtil.isNotBlank(kakaoEmSubtitle)) {
            paramMap.put("kakaoEmType",Const.KkoEmType.TEXT);    //강조표기 타이틀
            paramMap.put("kakaoEmTitle",kakaoEmTitle);    //강조표기 타이틀
            paramMap.put("kakaoEmSubtitle",kakaoEmSubtitle);    //강조 표기 보조 문구
        }else {
            paramMap.put("kakaoEmType",Const.KkoEmType.NONE);    //강조표기 타이틀
        }
        paramMap.put("kakaoSecurityYn",kakaoSecurityYn);    //보안템플릿 여부 (필수) Y,N
        paramMap.put("categoryCode",cellReader(row.getCell(10)));    //카테고리 코드 (필수)
        paramMap.put("buttons",cellReader(row.getCell(11)));    //버튼 타입
        paramMap.put("kakaoQuickReplies",cellReader(row.getCell(12)));    //바로가기 연결

        return paramMap;
    }



    /**
     * 알림톡 템플릿 개수 확인
     *
     * @param mobileVo
     * @return
     */
    public int getAlimtalkTemplateTotalCount(MobileVo mobileVo) {
        return mobileContentsDao.getAlimtalkContentTotalCount(mobileVo);
    }

    /**
     * 알림톡 템플릿 목록 조회
     *
     * @param mobileVo
     * @return
     */
    public List<MobileVo> getAlimtalkTemplateList(MobileVo mobileVo) {
        PagingUtil.setPagingRowcount(mobileVo);
        return mobileContentsDao.getAlimtalkTemplateList(mobileVo);
    }

    /**
     * 알림톡 템플릿 리스트에서 선택한 템플릿을 조회한다.
     *
     * @param mobileVo
     * @return
     */
    public List<MobileVo> findAlimtalkTemplates(MobileVo mobileVo) {
        return mobileContentsDao.findAlimtalkTemplates(mobileVo);
    }

    /**
     * 알림톡 템플릿 검수 상태 코드와 코드명이 담긴 Map 객체 반환
     *
     * @return
     */
    public CaseInsensitiveMap findKakaoTemplateInspStatusNameMap() {
        return cdMstDao.findKakaoTemplateInspStatusNameMap();
    }

    /**
     * 알림톡 템플릿 상태 코드와 코드명이 담긴 Map 객체 반환
     *
     * @return
     */
    public CaseInsensitiveMap findKakaoTemplateStatusNameMap() {
        return cdMstDao.findKakaoTemplateStatusNameMap();
    }

    /**
     * 알림톡 템플릿 코멘트 상태 코드와 코드명이 담긴 Map 객체 반환
     *
     * @return
     */
    public CaseInsensitiveMap findKakaoTemplateCommentStatusNameMap() {
        return cdMstDao.findKakaoTemplateCommentStatusNameMap();
    }

    public List<CaseInsensitiveMap> findAlimtalkTemplateCategoryGroupList() {
        return cdMstDao.selectAlimtalkTemplateCategoryGroupList();
    }

    public List<CaseInsensitiveMap> findAlimtalkTemplateCategoryCdList(String categoryGroup) {
        return cdMstDao.selectAlimtalkTemplateCategoryCdList(categoryGroup);
    }

    public void saveFile(Map<String, String> returnMap, InputStream in, String fileName , String previewMode) {
        String preViewUrl = null;
        File uploadFile = null;
        String toDate = DateUtil.getNowDateTime("yyyyMMdd");
        /* file 경로 설정 */
        // templateUploadDir/템플릿번호/생성날짜
        uploadFile = new File(
            super.mmsUploadPathRoot + "/" + super.mmsUploadPath + "/" + toDate + "/"  + fileName);

        if(log.isDebugEnabled())
            log.debug("[uploadFile path] " + uploadFile.getPath());

        /* file 경로 설정 */
        if(previewMode != null) {
            preViewUrl = super.mmsUploadPath + "/" + toDate + "/"  + fileName;
            returnMap.put("filePreviewPath", preViewUrl);
            returnMap.put("filePath", uploadFile.getPath());
        } else {
            returnMap.put("filePath", uploadFile.getPath());
        }
        returnMap.put("fileNm", uploadFile.getName());
        FileOutputStream out = null;
        try {
            FileUtil.forceMkdir(new File(uploadFile.getParent()));
            out = new FileOutputStream(uploadFile);
            // 파일 복사
            IOUtil.copyLarge(in, out);
        } catch(FileNotFoundException e) {
            log.error("saveFile() - File Not Found." + e);
            returnMap.put("isFail", "true");
        } catch(Exception e) {
            log.error("saveFile() - Error while saving file." + e);
            returnMap.put("isFail", "true");
        } finally {
            IOUtil.closeQuietly(in);
            IOUtil.closeQuietly(out);
        }
    }

    public String replacePreviewPath(String filePath) {
        return filePath.replaceAll(super.mmsUploadPathRoot + "/" + super.mmsUploadPath , super.mmsUploadPath);
    }

}
