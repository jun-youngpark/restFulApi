package com.mnwise.wiseu.web.rest.service.Contents;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.JsonUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.util.KakaoButtonUtils;
import com.mnwise.wiseu.web.base.util.KakaoRestApiUtil;
import com.mnwise.wiseu.web.base.util.MultipartFileUtil;
import com.mnwise.wiseu.web.base.util.PropertyUtil;
import com.mnwise.wiseu.web.rest.dao.contents.ContentsDao;
import com.mnwise.wiseu.web.rest.dto.DataMap;
import com.mnwise.wiseu.web.rest.exception.RestException;
import com.mnwise.wiseu.web.rest.model.contents.KakaoButton;
import com.mnwise.wiseu.web.rest.model.contents.KakaoQuickReply;
import com.mnwise.wiseu.web.rest.model.contents.KakaoSingleResponse;
import com.mnwise.wiseu.web.rest.model.contents.KakaoTemplateVo;
import com.mnwise.wiseu.web.rest.model.contents.Mappable;
import com.mnwise.wiseu.web.rest.model.custom.CustomContents;
import com.mnwise.wiseu.web.rest.parent.BaseService;

import org.json.simple.JSONObject;

@Service
public class ContentsService extends BaseService {
	 private static final Logger log = LoggerFactory.getLogger(ContentsService.class);

	@Autowired private ContentsDao templateDao;

    private final long MMS_IMAGE_MAX_SIZE = 40960;
	/**
	 * 템플릿 등록
	 */
    public JSONObject insertTemplate(CustomContents customContents) throws Exception{
    	DataMap dataMap = new DataMap();
        customContents.setCreateDt(DateUtil.getNowDateTime("yyyyMMdd"));
        customContents.setCreateTm(DateUtil.getNowDateTime("HHmmss"));
    	if("M".equals(customContents.getTemplateType())) { // 메일
    		File file = new File(customContents.getFileUrlName());
    		String oriFileName = file.getName();
            customContents.setContsNo(Integer.parseInt(templateDao.selectNextContsNo()));
            customContents.setFileType(oriFileName.toLowerCase().endsWith("html") || oriFileName.toLowerCase().endsWith("htm") ? "H" : oriFileName.toLowerCase().endsWith("zip") ? "H" : "I");
            customContents.setFileName(oriFileName);
            customContents.setFilePath(file.getPath());
    		templateDao.insertTemplateMail(customContents);
		}else if("DTI".indexOf(customContents.getTemplateType()) != -1) { // 단문, 장문, 이미지
            customContents.setContsNo(Integer.parseInt(templateDao.selectNextMobileContsNo()));
            customContents.setTemplate(StringUtil.escapeXss(customContents.getTemplate()));
            if(customContents.getFileUrlName() != null) {
            	// 파일을 업로드 한 거니깐 파일을 저장하고 등록하자.
                File originalFile = new File(customContents.getFileUrlName());
                long fileSize = originalFile.length() ;
                // 40kbyte 이상 이미지가 올려지지 않도록 변경
                if(fileSize >= this.MMS_IMAGE_MAX_SIZE) {
                    throw new RestException("template.mobiletemplate.alert.size.over");
                } else if(fileSize == 0) {
                    throw new RestException("파일 사이즈가 0입니다.");
                }
                customContents.setFileSize(String.valueOf(fileSize));
                //customContents.setFileSize(fileSize);
                String fileName = originalFile.getName();
                /* file 경로 설정 */
                customContents.setFilePath(originalFile.getPath());
                customContents.setFileName(fileName);
                customContents.setFilePreviewName(fileName);
                customContents.setFilePreviewPath(originalFile.getPath());
            }
            templateDao.insertMobileContents(customContents);
        	dataMap.put("contsNo", customContents.getContsNo());
        } else if("C".equals(customContents.getTemplateType())) {
            customContents.setContsNo(Integer.parseInt(templateDao.selectNextMobileContsNo()));
            File originalFile = new File(customContents.getFileUrlName());
            String fileName = originalFile.getName();

            if((originalFile.length() > 0)) {
            	customContents.setFileName(fileName);
            	customContents.setFilePath(originalFile.getPath());
            	customContents.setFilePreviewName(fileName);
                customContents.setFilePreviewPath(originalFile.getPath());
            }

            String jsonData = this.friendTalkImgUpload(originalFile.getPath(), fileName);
            String code = "888";
            String url = "";
            String message = "파일을 선택하세요";

            if(StringUtil.isBlank(jsonData)) {
                throw new RestException("Kakao Friendtalk Image Upload Response is empty");
            } else {
                JSONParser jsonParser = new JSONParser();
                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) jsonParser.parse(jsonData);
                url = (String) jsonObject.get("image");
                message = (String) jsonObject.get("message");
                code = (String) jsonObject.get("code");
                if("200".equals(code)) {
                	customContents.setImageUrl(url);
                	customContents.setImageLink(customContents.getImageLink());
                    log.info("Kakao ImageUpload is success. code: " + code + "/ message: " + message + "/ url: " + url);
                } else {
                    throw new RestException(message);
                }
            }
            if(customContents.getDetourFilePath() != null) { // 실패시 MMS 이미지
                File detourFile = new File(customContents.getDetourFilePath());
	            // MMS 최대 BYTE 사이즈 이상의 이미지가 올려지지 않도록 변경
	            long fileSize = detourFile.length();
	            if(fileSize >= this.MMS_IMAGE_MAX_SIZE) {
	                throw new RestException("파일 사이즈가 제한 사이즈를 초과합니다.");
	            }
	            if((fileSize > 0)) {
	            	customContents.setDetourFileName(detourFile.getName());
	            	customContents.setDetourFilePath(detourFile.getPath());
	            	customContents.setDetourPreviewName(detourFile.getName());
	                customContents.setDetourPreviewPath(detourFile.getPath());
	            } else {
	                throw new RestException("MMS 파일 사이즈가 0입니다.");
	            }
            }
			templateDao.insertMobileContents(customContents);
        }else if("A".equals(customContents.getTemplateType())) {
            customContents.setContsNo(Integer.parseInt(templateDao.selectNextMobileContsNo()));
			templateDao.insertMobileContents(createAlimtalkTemplate(customContents));
		}else {
			throw new RestException("TemplateType not maching.");
        }
    	return dataMap.put("contsNo", customContents.getContsNo()).build();

    }
	/**
	 * 템플릿 수정
	 */
    public JSONObject updateTemplate(CustomContents customContents) throws Exception{
    	if("M".equals(customContents.getTemplateType())) {
    		if(customContents.getFileUrlName() == null) {
                templateDao.updateTemplateMail(customContents);
            } else {
            	File htmlFile = new File((customContents.getFileUrlName()));
                String oriFileName = htmlFile.getName();
            	if(htmlFile.length() == 0) {
        			throw new RestException("파일 사이즈가 0입니다.");
                }
                customContents.setFileType(oriFileName.toLowerCase().endsWith("html") || oriFileName.toLowerCase().endsWith("htm") ? "H" : oriFileName.toLowerCase().endsWith("zip") ? "H" : "I");
                customContents.setFileName(oriFileName);
        		templateDao.updateTemplateMail(customContents);
            }
		}else if("DTI".indexOf(customContents.getTemplateType()) != -1) { // 단문, 장문, 이미지
			customContents.setFileType(customContents.getTemplateType());
            if(customContents.getFileUrlName() != null) {
                File originalFile = new File(customContents.getFileUrlName());
                long fileSize = originalFile.length() ;
                // 40kbyte 이상 이미지가 올려지지 않도록 변경
                if(fileSize >= this.MMS_IMAGE_MAX_SIZE) {
                    throw new RestException("template.mobiletemplate.alert.size.over");
                } else if(fileSize == 0) {
                    throw new RestException("파일 사이즈가 0입니다.");
                }
                customContents.setFileSize(String.valueOf(fileSize));
                String fileName = originalFile.getName();
                /* file 경로 설정 */
                customContents.setFilePath(originalFile.getPath());
                customContents.setFileName(fileName);
                customContents.setFilePreviewName(fileName);
                customContents.setFilePreviewPath(originalFile.getPath());
            }
            System.out.println(customContents.toStringJson());
            templateDao.updateMobileTemplate(customContents);
		}else if("C".equals(customContents.getTemplateType())) {


            if(customContents.getFileUrlName() != null) {
            	File originalFile = new File((customContents.getFileUrlName()));
	            String jsonData = this.friendTalkImgUpload(customContents.getFileUrlName(), originalFile.getName());
	            String code = "888";
	            String url = "";
	            String message = "파일을 선택하세요";

	            if(StringUtil.isBlank(jsonData)) {
	                throw new RestException("Kakao Friendtalk Image Upload Response is empty");
	            } else {
	                JSONParser jsonParser = new JSONParser();
	                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) jsonParser.parse(jsonData);
	                url = (String) jsonObject.get("image");
	                message = (String) jsonObject.get("message");
	                code = (String) jsonObject.get("code");
	                if("200".equals(code)) {
	                	customContents.setImageUrl(url);
	                	customContents.setImageLink(customContents.getImageLink());
	                    log.info("Kakao ImageUpload is success. code: " + code + "/ message: " + message + "/ url: " + url);
	                } else {
	                    throw new RestException(message);
	                }
	            }

                String fileName = originalFile.getName();

            	customContents.setFileName(fileName);
            	customContents.setFilePath(customContents.getFileUrlName());
            	customContents.setFilePreviewName(fileName);
            	customContents.setFilePreviewPath(customContents.getFileUrlName());
            }

            if(customContents.getDetourFilePath() != null) { // 실패시 MMS 이미지
                File detourFile = new File(customContents.getDetourFilePath());
	            // MMS 최대 BYTE 사이즈 이상의 이미지가 올려지지 않도록 변경
	            long fileSize = detourFile.length();
	            if(fileSize >= this.MMS_IMAGE_MAX_SIZE) {
	                throw new RestException("파일 사이즈가 제한 사이즈를 초과합니다.");
	            }
	            if((fileSize > 0)) {
	            	customContents.setDetourFileName(detourFile.getName());
	            	customContents.setDetourFilePath(detourFile.getPath());
	            	customContents.setDetourPreviewName(detourFile.getName());
	                customContents.setDetourPreviewPath(detourFile.getPath());
	            } else {
	                throw new RestException("MMS 파일 사이즈가 0입니다.");
	            }
            }
            templateDao.updateMobileTemplate(customContents);
		}else if("A".equals(customContents.getTemplateType())) {
			updateAlimtalkTemplate(customContents);
		}else {
			throw new RestException("TemplateType not maching.");
		}
    	return new DataMap()
    			  .build();

    }

	/**
	 * 템플릿 삭제
	 */
    public JSONObject deleteTemplate(CustomContents customContents) throws Exception{
    	if("M".equals(customContents.getTemplateType())) {
    		templateDao.deleteContentsByPk(customContents);
		}else if("DTI".indexOf(customContents.getTemplateType()) != -1) { // 단문, 장문, 이미지
			templateDao.deleteMobileContentsByPk(customContents);
		}else if("A".equals(customContents.getTemplateType())) {
			deleteAlimtalkTemplate(customContents);
		}else {
		}
    	templateDao.insertTemplateMail(customContents);
    	return new DataMap()
    			  .build();

    }

	/**
	 * 템플릿 목록 조회
	 */
    public JSONObject list(CustomContents customContents) throws Exception{
    	int count = 0;

    	List<CustomContents> list = new ArrayList<CustomContents>();
    	if("M".equals(customContents.getTemplateType())) {
   		 count = templateDao.selectTemplateCount(customContents);
    		list = templateDao.selectTemplateMail(customContents);
		}else if("DTI".indexOf(customContents.getTemplateType()) != -1) { // 단문, 장문, 이미지
			 count = templateDao.selectMobileTemplateCount(customContents);
			list = templateDao.selectTemplateMassage(customContents);
		}else if("A".equals(customContents.getTemplateType())) {
			list = templateDao.selectTemplateAlimTalk(customContents);
			 count = templateDao.selectAilmTalkTemplateCount(customContents);
		}else {
		}
    	return new DataMap()
      		    .put("count", count)
    			.put("list", list)
    			  .build();

    }
    public String selectNextContsNo() throws Exception{
    	return templateDao.selectNextContsNo();
    }
    public String friendTalkImgUpload(String filePath, String fileName) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        File file = new File(filePath);
        String image = new String();
        FileInputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            in = new FileInputStream(file);
        } catch(FileNotFoundException e) {
            log.error("Exception position : FileUtil - fileToString(File file)");
        }

        int len = 0;
        byte[] buf = new byte[1024];
        try {
            while((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }

            byte[] fileArray = out.toByteArray();
            image = new String(Base64.encodeBase64(fileArray));
        } catch(IOException e) {
            log.error("Exception position : FileUtil - fileToString(File file)");
        } finally {
            IOUtil.closeQuietly(in);
            IOUtil.closeQuietly(out);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("image", image);
        jsonObject.put("name", fileName);
        jsonObject.put("sndDtm", sdf.format(new Date()));
        System.out.println( jsonObject.toString());
        String jsonData = KakaoRestApiUtil.callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.uploadImage"), jsonObject.toString());


    	return jsonData;
    }

    public CustomContents createAlimtalkTemplate(CustomContents customContents) throws Exception {
        String senderKey = customContents.getKakaoSenderKey();
        String tmplCd = customContents.getTemplateCode();
        String tmplNm = customContents.getTemplateName();
        String tmplContent = customContents.getTemplate();

        if(StringUtil.isNotBlank(senderKey) && StringUtil.isNotBlank(tmplCd) && StringUtil.isNotBlank(tmplNm) && StringUtil.isNotBlank(tmplContent)) {
            List<NameValuePair> params = KakaoRestApiUtil.createParameters(senderKey, "S", tmplCd, tmplNm, tmplContent);
            if(customContents.getButton() != null) {
	            String buttonJson = customContents.getButton().toString();
	            if(StringUtil.isNotBlank(buttonJson)) {
	            	List<Mappable> mappableList = JsonUtil.toList(buttonJson, KakaoButton.class);
	                List<NameValuePair> paramList = KakaoButtonUtils.convertButtonToHttpParamList(mappableList);
	                for(NameValuePair pair : paramList) {
	                    params.add(pair);
	                }
	            }
            }
            String kakaoQuickReplies = "";
            if(customContents.getKakaoQuickReplies() != null) {
	            kakaoQuickReplies = customContents.getKakaoQuickReplies().toString();//바로연결
	            if(StringUtil.isNotBlank(kakaoQuickReplies)) {
	            	List<Mappable> mappableList = JsonUtil.toList(kakaoQuickReplies, KakaoQuickReply.class);
	                List<NameValuePair> paramList = KakaoButtonUtils.convertButtonToHttpParamList(mappableList);
	                for(NameValuePair pair : paramList) {
	                    params.add(pair);
	                }
	            }
            }
            params.add(new BasicNameValuePair("categoryCode", customContents.getCategoryCd()));
            String kakaoSecurityYn = customContents.getKakaoSecurityYn();
            params.add(new BasicNameValuePair("securityFlag", String.valueOf("Y".equals(kakaoSecurityYn))));
            String kakaoEmType = customContents.getKakaoEmType();
            params.add(new BasicNameValuePair("templateEmphasizeType", kakaoEmType));
            String kakaoEmTitle = customContents.getKakaoEmTitle();
            String kakaoEmSubtitle = customContents.getKakaoEmSubtitle();
            List<MultipartFile> multipartFileList = new ArrayList<MultipartFile>();

            if ("TEXT".equals(kakaoEmType)) {
                params.add(new BasicNameValuePair("templateTitle", kakaoEmTitle));
                params.add(new BasicNameValuePair("templateSubtitle", kakaoEmSubtitle));
            }else if ("IMAGE".equals(kakaoEmType)) {
                if(StringUtils.isNotBlank(customContents.getFileUrlName())) {  //카카오 강조유형 이미지인 경우 기존 이미지 활용
                	MultipartFile multipartFile = MultipartFileUtil.fileToMultipartFileConvert(customContents.getFileUrlName(), "image");
                    if(multipartFile != null && multipartFile.getSize() > 0) {
                        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                        customContents.setFileName(fileName);
                        multipartFileList.add(multipartFile);
                    }
                }
            }

            String kakaoTmplMsgType = customContents.getKakaoTmplMsgType();
            params.add(new BasicNameValuePair("templateMessageType", kakaoTmplMsgType));
            String kakaoTmplEx = customContents.getKakaoTmplEx();
            String kakaoTmplAd = customContents.getKakaoTmplAd();

            if ("EX".equals(kakaoTmplMsgType) || "MI".equals(kakaoTmplMsgType)) {
                params.add(new BasicNameValuePair("templateExtra", kakaoTmplEx));
            }
            if ("AD".equals(kakaoTmplMsgType) || "MI".equals(kakaoTmplMsgType)) {
                params.add(new BasicNameValuePair("templateAd", kakaoTmplAd));
            }
            String jsonData ;

            if(multipartFileList.size()>0) {
                jsonData = KakaoRestApiUtil.callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.createWithImage"), params , multipartFileList);
            }else {
                jsonData = KakaoRestApiUtil.callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.create"), params);
            }

            if(StringUtil.isBlank(jsonData)) {
                throw new RestException("[" + tmplCd + "] Kakao Template Response is empty");
            } else {
                KakaoSingleResponse response = KakaoRestApiUtil.parseSingleResponse(jsonData);
                String code = response.getCode();

                if("200".equals(code)) {
                    KakaoTemplateVo data = response.getData();
                    customContents.setFileType("A");
                    customContents.setKakaoInspStatus(data.getInspectionStatus());
                    customContents.setKakaoTmplStatus(data.getStatus());
                    customContents.setKakaoButtons(data.convertButtonsToJson());
                    customContents.setImageUrl(data.getTemplateImageUrl());
                    customContents.setFilePreviewName(customContents.getFileName());
                    customContents.setFilePreviewPath(customContents.getFileUrlName());
                    customContents.setFilePath(customContents.getFileUrlName());

                    log.info("Kakao Template is created. Sender Key: " + senderKey + " Template Code: " + tmplCd);
                    return customContents;
                } else {
                    String message = response.getMessage();
                    log.error("Kakao Template is not created. Sender Key: " + senderKey + " Template Code: " + tmplCd);
                    log.error("Result Code: " + code + ", Message: " + message);
                    throw new RestException("[" + tmplCd + "] " + message + "(" + code + ")");
                }
            }
        } else {
            throw new RestException("[" + tmplCd + "] Invalid Parameter.");
        }
    }

    public void updateAlimtalkTemplate(CustomContents customContents) throws Exception {
        if(StringUtil.isNotBlank(String.valueOf(customContents.getContsNo()))) {
        	CustomContents vo = templateDao.findAlimtalkTemplateInfo(customContents);
            // 사용자 타입이 관리자('A')이거나 같은 부서 매니저('M')이거나 템플릿을 등록한 사용자 ID와 동일한 경우만 템플릿 수정이 가능
            String senderKey = vo.getKakaoSenderKey();
            String tmplCd = vo.getKakaoTmplCd();

            String newContsNm = customContents.getTemplateName();
            String newContent = customContents.getTemplate();
            String newTmplCd = customContents.getTemplateCode();
            String categoryCd = customContents.getCategoryCd();
            String newKakaoTmplMsgType = vo.getKakaoTmplMsgType();
            String newKakaoTmplEx = customContents.getKakaoTmplEx();
            String newKakaoTmplAd = customContents.getKakaoTmplAd();
            String newKakaoEmType = customContents.getKakaoEmType();
            String newKakaoEmTitle = customContents.getKakaoEmTitle();
            String newKakaoEmSubtitle = customContents.getKakaoEmSubtitle();
            String newKakaoSecurityYn = customContents.getKakaoSecurityYn();

            if(StringUtil.isNotBlank(newTmplCd) && StringUtil.isNotBlank(newContsNm) && StringUtil.isNotBlank(newContent)) {
                List<NameValuePair> params = KakaoRestApiUtil.createParameters(senderKey, "S", tmplCd, "", "");
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
                List<MultipartFile> multipartFileList = new ArrayList<MultipartFile>();
                params.add(new BasicNameValuePair("newTemplateEmphasizeType", newKakaoEmType));
                if ("TEXT".equals(newKakaoEmType)) {
                    params.add(new BasicNameValuePair("newTemplateTitle", newKakaoEmTitle));
                    params.add(new BasicNameValuePair("newTemplateSubtitle", newKakaoEmSubtitle));
                }else if ("IMAGE".equals(newKakaoEmType)) {
                    if(StringUtils.isNotBlank(customContents.getFileUrlName())) {  //카카오 강조유형 이미지인 경우 기존 이미지 활용
                    	MultipartFile multipartFile = MultipartFileUtil.fileToMultipartFileConvert(customContents.getFileUrlName(), "image");
                        if(multipartFile != null && multipartFile.getSize() > 0) {
                            String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                            customContents.setFileName(fileName);
                            multipartFileList.add(multipartFile);
                        }
                    }
                }

                params.add(new BasicNameValuePair("securityFlag", String.valueOf("Y".equals(newKakaoSecurityYn))));
                String buttons = customContents.getButton();
                if (StringUtil.isNotBlank(buttons)) {
                    List<NameValuePair> paramList = KakaoButtonUtils.convertButtonToHttpParamList(JsonUtil.toList(buttons, KakaoButton.class));
                    for(NameValuePair pair : paramList) {
                        params.add(pair);
                    }
                }
                String kakaoQuickReplies = customContents.getKakaoQuickReplies();
                if (StringUtil.isNotBlank(kakaoQuickReplies)) {
                    List<NameValuePair> quickReplyList = KakaoButtonUtils.convertButtonToHttpParamList(JsonUtil.toList(kakaoQuickReplies, KakaoQuickReply.class));
                    for(NameValuePair pair : quickReplyList) {
                        params.add(pair);
                    }
                }

                String jsonData ;
                if(multipartFileList.size()>0) {
                    jsonData = KakaoRestApiUtil.callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.updateWithImage"), params , multipartFileList);
                }else {
                    jsonData = KakaoRestApiUtil.callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.update"), params);
                }

                if(StringUtil.isBlank(jsonData)) {
                    throw new RestException("[" + tmplCd + "] Kakao Template Response is empty");
                } else {
                    KakaoSingleResponse kakaoResponse = KakaoRestApiUtil.parseSingleResponse(jsonData);
                    String code = kakaoResponse.getCode();

                    if("200".equals(code)) {
                        log.info("Kakao Template Update is completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);

                        customContents.setKakaoSenderKey(vo.getKakaoSenderKey());
                        customContents.setKakaoTmplCd(vo.getKakaoTmplCd());
                        customContents.setFilePreviewName(customContents.getFileName());
                        customContents.setFilePreviewPath(customContents.getFileUrlName());
                        customContents.setFilePath(customContents.getFileUrlName());

                        int cnt = templateDao.updateKakaoTemplate(customContents);
                        log.info("Alimtalk Template(" + cnt + ") Updated.");
                        return;
                    } else {
                        String message = kakaoResponse.getMessage();
                        log.error("Kakao Template Update is not completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                        log.error("Result Code: " + code + ", Message: " + message);
                        throw new RestException("[" + tmplCd + "] " + message + "(" + code + ")");
                    }
                }
            }

        } else {
            throw new RestException("Invalid Parameter.");
        }
    }

    public void deleteAlimtalkTemplate(CustomContents customContents) throws Exception {

    	CustomContents vo = templateDao.findAlimtalkTemplateInfo(customContents);

        String senderKey = vo.getKakaoSenderKey();
        String tmplCd = vo.getKakaoTmplCd();
        if(StringUtils.isBlank(senderKey)) {
        	throw new RestException("서비스가 존재하지 않습니다.");
        }

        List<NameValuePair> params = KakaoRestApiUtil.createParameters(senderKey, "S", tmplCd, "", "");
        String jsonData = KakaoRestApiUtil.callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.delete"), params);

        if(StringUtil.isBlank(jsonData)) {
            throw new RestException("[" + tmplCd + "] Kakao Template Response is empty");
        } else {
            KakaoSingleResponse response = KakaoRestApiUtil.parseSingleResponse(jsonData);
            String code = response.getCode();

            if("200".equals(code)) {
                log.info("Kakao Template Delete is completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                customContents.setKakaoSenderKey(senderKey);
                customContents.setKakaoTmplCd(tmplCd);

                int cnt = templateDao.deleteAlimtalkTemplate(customContents);
                log.info("Alimtalk Template(" + cnt + ") Deleted.");
            } else {
                log.error("Kakao Template Delete is not completed. Result Code: " + code + ", Message: " + response.getMessage() + ", Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                throw new RestException("[" + tmplCd + "] " + response.getMessage() + "(" + code + ")");
            }
        }
    }

    /**
     * 선택한 템플릿을 카카오에 검수요청한다.
     *
     * @param contsNoArr 검수 요청할 템플릿 번호 배열
     * @return
     */
    public JSONObject inspectAlimtalkTemplate(CustomContents customContents) throws Exception {
    	CustomContents vo = templateDao.findAlimtalkTemplateInfo(customContents);

        String senderKey = vo.getKakaoSenderKey();
        String tmplCd = vo.getKakaoTmplCd();
System.out.println(senderKey);
        List<NameValuePair> params = KakaoRestApiUtil.createParameters(senderKey, "S", tmplCd, "", "");
        String jsonData =KakaoRestApiUtil.callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.request"), params);

        if(StringUtil.isBlank(jsonData)) {
            throw new RestException("[" + tmplCd + "] Kakao Template Response is empty");
        } else {
            KakaoSingleResponse response = KakaoRestApiUtil.parseSingleResponse(jsonData);
            String code = response.getCode();

            if("200".equals(code)) {
                log.info("Kakao Template Request is completed. Sender Key: " + senderKey + ", Template Code: " + tmplCd);
            } else {
                log.error("Kakao Template Request is not completed. Result Code: " + code + ", Message: " + response.getMessage() + ", Sender Key: " + senderKey + ", Template Code: " + tmplCd);
                throw new RestException("[" + tmplCd + "] " + response.getMessage() + "(" + code + ")");
            }
        }
    	return new DataMap()
  			  .build();
    }
    public JSONObject selectAlimtalkInspect(CustomContents customContents) throws Exception {
    	String kakaoInspStatus = templateDao.selectAlimtalkInspect(customContents);

    	return new DataMap()
    		  .put("kakaoInspStatus", kakaoInspStatus)
  			  .build();
    }

    public JSONObject selectTemplateCount(CustomContents customContents) throws Exception {
    	int count = 0;
    	if("M".equals(customContents.getTemplateType())) {
    		 count = templateDao.selectTemplateCount(customContents);
		}else if("DTIC".indexOf(customContents.getTemplateType()) != -1) { // 단문, 장문, 이미지
			 count = templateDao.selectMobileTemplateCount(customContents);
		}else if ("A".equals(customContents.getTemplateType())) {
			 count = templateDao.selectAilmTalkTemplateCount(customContents);
		}
    	return new DataMap()
    		  .put("count", count)
  			  .build();
    }

}