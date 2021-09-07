package com.mnwise.wiseu.web.template.service;

import static com.mnwise.wiseu.web.common.util.KakaoRestApiUtil.FAIL_CODE;
import static com.mnwise.wiseu.web.common.util.KakaoRestApiUtil.SUCCESS_CODE;
import static com.mnwise.wiseu.web.common.util.KakaoRestApiUtil.callRestApi;
import static com.mnwise.wiseu.web.common.util.KakaoRestApiUtil.callRestFileSendApi;
import static com.mnwise.wiseu.web.common.util.KakaoRestApiUtil.createSubApiUrl;
import static com.mnwise.wiseu.web.common.util.KakaoRestApiUtil.parseSingleBrandTalkResponse;
import static com.mnwise.wiseu.web.common.util.PropertyUtil.getProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.common.util.KakaoRestApiUtil;
import com.mnwise.wiseu.web.template.dao.MobileContentsDao;
import com.mnwise.wiseu.web.template.model.BrandtalkSingleResponse;
import com.mnwise.wiseu.web.template.model.BrandtalkTemplateVo;
import com.mnwise.wiseu.web.template.model.KakaoButton;
import com.mnwise.wiseu.web.template.model.MobileVo;
import com.mnwise.wiseu.web.template.util.KakaoButtonUtils;
import com.mnwise.wiseu.web.template.util.TemplateFileUtil;
@Service
public class BrandtalkTemplateService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(BrandtalkTemplateService.class);

    @Autowired private MobileContentsDao mobileContentsDao;

    /**
     * <p>
     * 브랜드톡 템플릿 발송을 위한 파라미터 생성
     * </p>
     *
     * @param BrandtalkTemplateVo
     * @return List
     */
    private List<NameValuePair> createParams(BrandtalkTemplateVo brandtalkTemplateVo) throws Exception {
        List<NameValuePair>  params = new ArrayList<>();
        if(StringUtil.isNotEmpty(brandtalkTemplateVo.getSenderKey()) || StringUtil.isNotEmpty(brandtalkTemplateVo.getName())
            || StringUtil.isNotEmpty(brandtalkTemplateVo.getContent())) {
            params.add(new BasicNameValuePair("senderKey", brandtalkTemplateVo.getSenderKey()));
            params.add(new BasicNameValuePair("name", brandtalkTemplateVo.getName()));
            params.add(new BasicNameValuePair("content", brandtalkTemplateVo.getContent()));
            params.add(new BasicNameValuePair("unsubscribeContent", brandtalkTemplateVo.getUnsubscribeContent()));
            params.add(new BasicNameValuePair("imageLink", brandtalkTemplateVo.getImageLink()));
            params.add(new BasicNameValuePair("messageType", brandtalkTemplateVo.getMessageType()));
            params.add(new BasicNameValuePair("contentType", brandtalkTemplateVo.getContentType()));

            if(StringUtil.isNotEmpty(brandtalkTemplateVo.getCode())) {
                params.add(new BasicNameValuePair("code", brandtalkTemplateVo.getCode()));
            }
            if(brandtalkTemplateVo.getButtons()!=null) {
                List<NameValuePair> list = new ArrayList<>();
                for(KakaoButton button : brandtalkTemplateVo.getButtons()) {
                    int ordering = button.getOrdering();
                    int index = ordering - 1;
                    list.add(new BasicNameValuePair("buttons[" + index + "].ordering", String.valueOf(ordering)));
                    String linkType = button.getLinkType();
                    list.add(new BasicNameValuePair("buttons[" + index + "].linkType", linkType));
                    if(!KakaoButtonUtils.ADD_CHANNEL.equalsIgnoreCase(linkType)) {
                        list.add(new BasicNameValuePair("buttons[" + index + "].name", button.getName()));
                    }
                    if(KakaoButtonUtils.WEB_LINK.equals(linkType)) {
                        list.add(new BasicNameValuePair("buttons[" + index + "].linkMo", button.getLinkMo()));
                        String linkPc = button.getLinkPc();
                        if(linkPc != null) {
                            list.add(new BasicNameValuePair("buttons[" + index + "].linkPc", button.getLinkPc()));
                        }
                    } else if(KakaoButtonUtils.APP_LINK.equals(linkType)) {
                        list.add(new BasicNameValuePair("buttons[" + index + "].linkIos", button.getLinkIos()));
                        list.add(new BasicNameValuePair("buttons[" + index + "].linkAndroid", button.getLinkAnd()));
                    }
                }
                for(NameValuePair pair : list) {
                    params.add(pair);
                }
            }
        }else {
            throw new Exception("Invalid Parameter");
        }
        return params;
    }
    /**
     * <p>
     * 브랜드톡 템플릿을 새로 생성한다.
     * </p>
     *
     * @param jsonParam 카카오 REST API에 사용할 파라미터
     * @return String
     */
    public String createBrandTalkTemplate(BrandtalkTemplateVo brandtalkTemplateVo) throws Exception{
        try {
            String message= " " ;
            Map<String, MultipartFile> multipartFileMap = new HashMap<>();
            multipartFileMap.put("image", brandtalkTemplateVo.getBrandTalkImageFile());
            final String jsonData = callRestFileSendApi(getProperty("kakao.biz.api.brandtalk.template.create"),createParams(brandtalkTemplateVo), multipartFileMap);
            if(StringUtil.isEmpty(jsonData)) {
                log.error("Kakao Template Response is empty");
                throw new Exception("Response is empty");
            } else {
                BrandtalkSingleResponse response = parseSingleBrandTalkResponse(jsonData);
                String code = response.getCode();

                if(SUCCESS_CODE.equals(code)) {
                    brandtalkTemplateVo.setContsNo(mobileContentsDao.selectNextContsNo());
                    brandtalkTemplateVo.setTime();
                    //파일 복사
                    Map<String, String> FileUploadMap = TemplateFileUtil.saveFile(brandtalkTemplateVo.getBrandTalkImageFile().getInputStream(),brandtalkTemplateVo.getBrandTalkImageFile().getOriginalFilename()
                        ,brandtalkTemplateVo.getCreateDt()
                        ,brandtalkTemplateVo.getContsNo(), "on", super.mmsUploadPathRoot, super.mmsUploadPath);
                    //brandtalkTemplateVo 테이블 입력 값 설정
                    brandtalkTemplateVo.setFile(FileUploadMap);
                    brandtalkTemplateVo.setFileType(Const.Channel.BRANDTALK);
                    BrandtalkTemplateVo data = response.getData();
                    brandtalkTemplateVo.setCode(data.getCode());
                    brandtalkTemplateVo.setKakaoTmplStatus(data.getStatus());
                    brandtalkTemplateVo.setKakaoButtons(brandtalkTemplateVo.convertButtonsToJson());
                    brandtalkTemplateVo.setContentType(data.getContentType());

                    mobileContentsDao.insertBrandTalkContents(brandtalkTemplateVo);
                    log.info("Kakao Template is created. Sender Key: " + brandtalkTemplateVo.getSenderKey());
                    return SUCCESS_CODE;
                } else {
                    message =  getErrorMsg(response);
                    log.error("Kakao Template is not created. Sender Key: " + brandtalkTemplateVo.getSenderKey());
                    log.error("Result Code: " + code + ", Message: " + message);
                    throw new Exception("[" + code + "] BrandTalk Template is not created. " + message);
                }
            }
        } catch(Exception e) {
            log.error("while requesting kakao template create, exception occurred. " + e.getMessage());
            throw new Exception("BrandTalk Template is not created. " + e.getMessage());
        }
    }

    /**
     * <p>
     * 브랜드톡 템플릿을 단건 조회 REST API
     * </p>
     *
     * @param jsonParam 카카오 REST API에 사용할 파라미터
     * @return String
     */
    public String getBrandTalkTemplate(BrandtalkTemplateVo brandtalkTemplateVo) throws Exception{
        try {
            String message= "" ;
            String url =createSubApiUrl(getProperty("kakao.biz.api.brandtalk.template"), brandtalkTemplateVo.getCode());
            String jsonData =callRestApi(url);    //get방식
            if(StringUtil.isEmpty(jsonData)) {
                log.error("Kakao Template Response is empty");
                return FAIL_CODE + ":Response is empty";
            } else {
                BrandtalkSingleResponse response = KakaoRestApiUtil.parseSingleBrandTalkResponse(jsonData);
                String code = response.getCode();
                if(KakaoRestApiUtil.SUCCESS_CODE.equals(code)) {
                    BrandtalkTemplateVo data = response.getData();
                    brandtalkTemplateVo.setKakaoTmplStatus(data.getStatus());
                    return SUCCESS_CODE;
                } else {
                   return code + ":" + message;
                }
            }
        }catch(final Exception e) {
            log.error("while requesting kakao template create, exception occurred. " + e.getMessage());
            return FAIL_CODE + ":" + e.getMessage();
        }
    }

    /**
     * <p>
     * 브랜드톡 템플릿 카운트
     * </p>
     *
     * @param FileType = B
     * @return int
     */
    public int getBrandTalkTemplateTotalCount(BrandtalkTemplateVo brandtalkTemplateVo) throws Exception{
        return mobileContentsDao.selectBrandTalkContentListCount(brandtalkTemplateVo);
    }
    /**
     * <p>
     * 브랜드톡 템플릿 리스트 불러오기
     * </p>
     *
     * @param FileType = B
     * @return List
     */
    public List<BrandtalkTemplateVo> getBrandTalkTemplateList(BrandtalkTemplateVo brandtalkTemplateVo) throws Exception {
        PagingUtil.setPagingRowcount(brandtalkTemplateVo);
        return mobileContentsDao.selectBrandTalkContentList(brandtalkTemplateVo);
    }
    /**
     * <p>
     * 브랜드톡 템플릿 리스트에서 선택한 템플릿을 조회한다.
     * </p>
     * @param BrandtalkTemplateVo
     * @return BrandtalkTemplateVo
     */
    public BrandtalkTemplateVo findBrandtalkTemplateInfo(BrandtalkTemplateVo brandtalkTemplateVo) {
        return mobileContentsDao.selectBrandTalkContentInfo(brandtalkTemplateVo);
    }

    /** <p>
     *  선택한 브랜드톡 템플릿 템플릿을 수정한다.
     *  </p>
     * @param BrandtalkTemplateVo
     * @return String
     */
    public String updateBrandTalkTemplate(BrandtalkTemplateVo brandtalkTemplateVo) throws Exception {
        try {
            if(!templateStatusCheck(brandtalkTemplateVo)){  //true 정상
                throw new Exception("It can be modified only when the template status is standby (R).");
            }
            String message= "" ;
            Map<String, MultipartFile> multipartFileMap = new HashMap<>();
            multipartFileMap.put("image", brandtalkTemplateVo.getBrandTalkImageFile());
            String url =createSubApiUrl(getProperty("kakao.biz.api.brandtalk.template.update"), brandtalkTemplateVo.getCode());
            String jsonData =callRestFileSendApi(url, createParams(brandtalkTemplateVo), multipartFileMap);    //POST방식
            if(StringUtil.isEmpty(jsonData)) {
                throw new Exception("Response is empty");
            } else {
                BrandtalkSingleResponse response = KakaoRestApiUtil.parseSingleBrandTalkResponse(jsonData);
                String code = response.getCode();
                if(KakaoRestApiUtil.SUCCESS_CODE.equals(code)) {
                    BrandtalkTemplateVo data = response.getData();
                    brandtalkTemplateVo.setKakaoButtons(brandtalkTemplateVo.convertButtonsToJson());
                    brandtalkTemplateVo.setTime();
                    //파일 복사
                    Map<String, String> FileUploadMap = TemplateFileUtil.saveFile(brandtalkTemplateVo.getBrandTalkImageFile().getInputStream()
                        ,brandtalkTemplateVo.getBrandTalkImageFile().getOriginalFilename()
                        ,brandtalkTemplateVo.getCreateDt()
                        ,brandtalkTemplateVo.getContsNo(), "on", super.mmsUploadPathRoot, super.mmsUploadPath);
                    brandtalkTemplateVo.setFile(FileUploadMap);
                    brandtalkTemplateVo.setFileType(Const.Channel.BRANDTALK);
                    brandtalkTemplateVo.setKakaoTmplStatus(data.getStatus());
                    brandtalkTemplateVo.setContentType(data.getContentType());
                    mobileContentsDao.updateBrandTalkTemplate(brandtalkTemplateVo);

                    return SUCCESS_CODE;
                } else {
                    message = getErrorMsg(response);
                   log.error("Kakao Template is not created. Sender Key: " + brandtalkTemplateVo.getSenderKey());
                   throw new Exception("Result Code: " + code + ", Message: " + message);
                }
            }
        }catch(final Exception e) {
            log.error("while requesting kakao template create, exception occurred. " + e.getMessage());
            throw new Exception("Message: " + e.getMessage());
        }
    }

    /**
     * <p>
     * 선택한 브랜드톡 템플릿 템플릿을 삭제한다.
     * </p>
     * @param BrandtalkTemplateVo
     * @return String
     */
    public String deleteBrandTalkTemplate(BrandtalkTemplateVo brandtalkTemplateVo) throws Exception{
        try {
            if(!templateStatusCheck(brandtalkTemplateVo)){  //true 정상
                return FAIL_CODE + ":It can be modified only when the template status is standby (R).";
            }
            String message= " " ;
            String url =createSubApiUrl(getProperty("kakao.biz.api.brandtalk.template.delete"), brandtalkTemplateVo.getCode());
            String jsonData =callRestApi(url, new ArrayList<NameValuePair>());    //post방식
            if(StringUtil.isEmpty(jsonData)) {
                log.error("Kakao Template Response is empty");
                return FAIL_CODE + ":Response is empty";
            } else {
                BrandtalkSingleResponse response = KakaoRestApiUtil.parseSingleBrandTalkResponse(jsonData);
                String code = response.getCode();
                if(KakaoRestApiUtil.SUCCESS_CODE.equals(code)) {
                    mobileContentsDao.updateUseYnToN(brandtalkTemplateVo);
                    return SUCCESS_CODE;
                } else {
                   message = getErrorMsg(response);
                   log.error("Kakao Template is not created. Sender Key: " + brandtalkTemplateVo.getSenderKey());
                   log.error("Result Code: " + code + ", Message: " + message);
                   return code + ":" + message;
                }
            }
        }catch(final Exception e) {
            log.error("while requesting kakao template create, exception occurred. " + e.getMessage());
            return FAIL_CODE + ":" + e.getMessage();
        }
    }

    /**
     * 템플릿 등록/수정/삭제 오류 메세지 출력.
     *
     * @param BrandtalkSingleResponse
     * @return String
     */
    public String getErrorMsg(BrandtalkSingleResponse response) {
        String message = "";
        if(response.getMessage() instanceof String) {
             message =  (String)response.getMessage();
        }else if(response.getMessage() instanceof String[]) {
             message =  Arrays.toString((String[])response.getMessage());
        }else {
            List<?> list = new ArrayList<>();
            if (response.getMessage().getClass().isArray()) {
                list = Arrays.asList((Object[])response.getMessage());
            } else if (response.getMessage() instanceof Collection) {
                list = new ArrayList<>((Collection<?>)response.getMessage());
            }
            for(Object object : list) {
                message += object +"\r\n";
           }
        }
        return message;
    }
        /**
         * 선택한 브랜드톡 템플릿이 '대기'상태인지 체크한다.
         *
         * @param BrandtalkTemplateVo
         * @return boolean
         */
    public boolean templateStatusCheck(BrandtalkTemplateVo brandtalkTemplateVo) {
        boolean result = false;

        String url =createSubApiUrl(getProperty("kakao.biz.api.brandtalk.template"), brandtalkTemplateVo.getCode());
        String jsonData= callRestApi(url);    //get방식
        if(!StringUtil.isEmpty(jsonData)) {
            BrandtalkSingleResponse response = KakaoRestApiUtil.parseSingleBrandTalkResponse(jsonData);
            if(KakaoRestApiUtil.SUCCESS_CODE.equals(response.getCode())) {
                BrandtalkTemplateVo data = response.getData();
                result = Const.KakaoTemplateStatus.READY.equalsIgnoreCase(data.getStatus());
            }
        }
        return result;
    }

}
