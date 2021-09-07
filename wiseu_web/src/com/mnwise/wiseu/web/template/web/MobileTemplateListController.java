package com.mnwise.wiseu.web.template.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.MobileListJsonView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.upload.resolver.AjaxFileUploadMultipartResolver;
import com.mnwise.wiseu.web.template.model.MobileVo;
import com.mnwise.wiseu.web.template.service.MobileTemplateService;

@Controller
public class MobileTemplateListController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(MobileTemplateListController.class);

    @Autowired private MobileTemplateService mobileTemplateService;
    @Autowired private AjaxFileUploadMultipartResolver customMultipartResolver;

    @Value("${mms.image.max.size:20480}")
    private long mmsImageMaxSize;

    /**
     * [템플릿>모바일 템플릿 리스트] 모바일 템플릿 리스트
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/template/mobileTemplateList.do", method={RequestMethod.GET, RequestMethod.POST})  // /template/mobileTemplate.do
    public ModelAndView list(@RequestParam(defaultValue="-1") int tagNo, String fileType, HttpServletRequest request) throws Exception {
        try {
            MobileVo mobileVo = new MobileVo();
            mobileVo.setUserVo(getLoginUserVo());
            mobileVo.setTagNo(tagNo);
            mobileVo.setFileType(fileType);

            PagingUtil.setPagingInfo(request, mobileVo);

            ModelAndView mav = new ModelAndView("template/mobileTemplateList");  // template/mobile_list
            final int totalCount = mobileTemplateService.getMobileContentTotalCount(mobileVo);
            if(totalCount > 0) {
                mobileVo.setListCnt(getListCount(mobileVo, totalCount));
                mobileVo.setTotalRowCnt(totalCount);
                mav.addObject("templateList", mobileTemplateService.getMobileList(mobileVo));
            }

            PagingUtil.transferPagingInfo(request, totalCount);

            mav.addObject("fileType", mobileVo.getFileType());
            mav.addObject("searchWord", mobileVo.getSearchWord());
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 현재 페이지에 출력할 건수를 구하는 역할
     *
     * @param mobileVo
     * @param totalCount
     * @return
     */
    private int getListCount(MobileVo mobileVo, final int totalCount) {
        final int perPage = mobileVo.getCountPerPage();
        final int lastRow = perPage * mobileVo.getNowPage();

        if(lastRow <= totalCount) {
            return perPage;
        } else {
            return (totalCount + perPage) % lastRow;
        }
    }

    /**
     * [템플릿>모바일 템플릿 리스트] 모바일 템플릿 내용 조회
     *
     * @param contsNo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/template/mobileTemplateView.do", method={RequestMethod.GET, RequestMethod.POST})  // /template/viewMobileTemplate.do
    public ModelAndView view(int contsNo, HttpServletRequest request) throws Exception {
        try {
            MobileVo mobileVo = new MobileVo();
            mobileVo.setContsNo(contsNo);
            mobileVo = mobileTemplateService.getMobileContentsInfo(contsNo);

            // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
            if(isInvalidAccess(mobileVo.getGrpCd(), mobileVo.getUserId(), mobileVo.getAuthType())) {
                return new ModelAndView("");
            }

            ModelAndView mav = new ModelAndView("template/mobileTemplateView");  // template/mobile_view
            mav.addObject("mobileVo", mobileVo);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [템플릿>모바일 템플릿 리스트] 모바일 템플릿 삭제
     *
     * @param contsNo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/template/deleteMobileTemplate.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView delete(int contsNo, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            // User Type Code가 '관리자(A)'일 경우 템플릿 삭제
            if(Const.USER_ROLE_ADMIN.equals(userVo.getUserTypeCd())) {
                mobileTemplateService.deleteMobileContents(contsNo);
            } else {
                MobileVo originalVo = mobileTemplateService.getMobileContentsInfo(contsNo);

                // 세션에 저장된 계정과 템플릿 등록 계정이 동일할 경우 템플릿 삭제
                if(userVo.getUserId().equals(originalVo.getUserId())) {
                    mobileTemplateService.deleteMobileContents(contsNo);
                }
            }

            return new ModelAndView(new RedirectView("/template/mobileTemplateList.do"));
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 파일유효성 검사
     *
     * @param fileEx
     * @param fileType
     * @param request
     * @return
     */
    @RequestMapping(value="/template/validateFile.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto fileCheck(String fileEx, String fileType, HttpServletRequest request) {
        try {
            String message = mobileTemplateService.fileCheck(fileEx, fileType);

            if(StringUtil.startsWith(message, "true")) {
                return new ResultDto(ResultDto.OK);
            }

            ResultDto resultDto = new ResultDto(ResultDto.FAIL);
            if(StringUtil.startsWith(message, "false|")) {
                resultDto.setMessage(message.substring(6));
            }
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [템플릿>모바일 템플릿 리스트>내용 조회] 모바일 템플릿 저장
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/template/updateMobileTemplate.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView update(HttpServletRequest request) throws Exception {
        try {
            MultipartHttpServletRequest multipartRequest = customMultipartResolver.resolveMultipart(request);

            // 20151005 parameter XSS 처리
            String tagNm = ServletRequestUtils.getStringParameter(multipartRequest, "tagNm");
            String contsNm = ServletRequestUtils.getStringParameter(multipartRequest, "contsNm");
            String contsDesc = ServletRequestUtils.getStringParameter(multipartRequest, "contsDesc");
            int contsNo = ServletRequestUtils.getIntParameter(multipartRequest, "contsNo");

            MobileVo mobileVo = new MobileVo();
            mobileVo.setTagNm(StringUtil.escapeXss(tagNm));
            mobileVo.setContsNm(StringUtil.escapeXss(contsNm));
            mobileVo.setContsDesc(StringUtil.escapeXss(contsDesc));
            mobileVo.setFileType(ServletRequestUtils.getStringParameter(multipartRequest, "mobile_type"));
            mobileVo.setAuthType(ServletRequestUtils.getStringParameter(multipartRequest, "authType"));

            String nowDateTime = DateUtil.getNowDateTime();
            mobileVo.setCreateDt(nowDateTime.substring(0, 8));
            mobileVo.setCreateTm(nowDateTime.substring(8, 14));
            mobileVo.setContsNo(contsNo);
            mobileVo.setContsTxt("");

            ModelAndView mav = new ModelAndView("template/mobileUpload");
            mav.addObject("contsNo", contsNo);
            boolean isFail = false;
            if(mobileVo.getFileType().equals("I") || mobileVo.getFileType().equals("S")) {
                // 파일을 업로드 한 거니깐 파일을 저장하고 등록하자.
                MultipartFile originalFile = multipartRequest.getFile("fileName");
                // 미리보기 파일 숨김
                MultipartFile previewFile = multipartRequest.getFile("fileName");

                if((originalFile.getSize() > 0)) {
                    // 20kbyte 이상 이미지가 올려지지 않도록 변경
                    if(originalFile.getSize() >= this.mmsImageMaxSize) {
                        mav.addObject("isFail", "true");
                        mav.addObject("imgFileUploadErro", "파일 사이즈가 제한 사이즈를 초과합니다.");
                        return mav;
                    } else if(originalFile.getSize() == 0) {
                        mav.addObject("isFail", "true");
                        mav.addObject("imgFileUploadErro", "파일 사이즈가 0입니다.");
                        return mav;
                    }

                    mobileVo.setFileSize(originalFile.getSize());

                    String fileName = originalFile.getOriginalFilename();
                    // String previewFileName = previewFile.getOriginalFilename();
                    Map<String, String> originalFileUploadMap = mobileTemplateService.saveFile(originalFile.getInputStream(), fileName, mobileVo, null);

                    Map<String, String> previewFileUploadMap = mobileTemplateService.saveFile(previewFile.getInputStream(), fileName, mobileVo, "preview");

                    // 오류여부 체크
                    if((originalFileUploadMap.get("isFail") != null)) {
                        isFail = true;
                    } else {
                        mobileVo.setFileName(originalFileUploadMap.get("fileNm"));
                        mobileVo.setFilePath(originalFileUploadMap.get("filePath"));
                        mobileVo.setFilePreviewName(previewFileUploadMap.get("fileNm"));
                        mobileVo.setFilePreviewPath(previewFileUploadMap.get("filePath"));
                    }
                }
            } else if(mobileVo.getFileType().equals("C")) {
                mobileVo.setImageLink(ServletRequestUtils.getStringParameter(multipartRequest, "friendtalkImageUrlLink"));

                MultipartFile originalFile = multipartRequest.getFile("fileName2");
                // 미리보기 파일 숨김
                MultipartFile previewFile = multipartRequest.getFile("fileName2");

                if((originalFile.getSize() > 0)) {
                    mobileVo.setImageUrl(ServletRequestUtils.getStringParameter(multipartRequest, "friendtalkImageUrl"));

                    String fileName = originalFile.getOriginalFilename();
                    // String previewFileName = previewFile.getOriginalFilename();
                    Map<String, String> originalFileUploadMap = mobileTemplateService.saveFile(originalFile.getInputStream(), fileName, mobileVo, null);
                    Map<String, String> previewFileUploadMap = mobileTemplateService.saveFile(previewFile.getInputStream(), fileName, mobileVo, "preview");
                    // 오류여부 체크
                    if((originalFileUploadMap.get("isFail") != null)) {
                        isFail = true;
                    } else {
                        mobileVo.setFileName(originalFileUploadMap.get("fileNm"));
                        mobileVo.setFilePath(originalFileUploadMap.get("filePath"));
                        mobileVo.setFilePreviewName(previewFileUploadMap.get("fileNm"));
                        mobileVo.setFilePreviewPath(previewFileUploadMap.get("filePath"));
                    }
                }
                originalFile = multipartRequest.getFile("fileName3");
                // 미리보기 파일 숨김
                previewFile = multipartRequest.getFile("fileName3");
                // MMS 최대 BYTE 사이즈 이상의 이미지가 올려지지 않도록 변경
                if(originalFile.getSize() >= this.mmsImageMaxSize) {
                    mav.addObject("isFail", "true");
                    mav.addObject("imgFileUploadErro", "파일 사이즈가 제한 사이즈를 초과합니다.");
                    return mav;
                }
                if((originalFile.getSize() > 0)) {
                    String fileName = originalFile.getOriginalFilename();
                    Map<String, String> originalFileUploadMap = mobileTemplateService.saveFile(originalFile.getInputStream(), fileName, mobileVo, null);
                    Map<String, String> previewFileUploadMap = mobileTemplateService.saveFile(previewFile.getInputStream(), fileName, mobileVo, "preview");
                    // 오류여부 체크
                    if((originalFileUploadMap.get("isFail") != null)) {
                        isFail = true;
                    } else {
                        mobileVo.setDetourFileName(originalFileUploadMap.get("fileNm"));
                        mobileVo.setDetourFilePath(originalFileUploadMap.get("filePath"));
                        mobileVo.setDetourPreviewName(previewFileUploadMap.get("fileNm"));
                        mobileVo.setDetourPreviewPath(previewFileUploadMap.get("filePath"));
                    }
                }
            } else {
                // 여긴 그냥 텍스트저장 할때 들어 오겠네...
                // 20151005 parameter XSS 처리
                String contsTxt = ServletRequestUtils.getStringParameter(multipartRequest, "txta_conts");
                mobileVo.setContsTxt(StringUtil.escapeXss(contsTxt));
            }

            if(isFail) {
                mav.addObject("isFail", "true");
            } else {
                mobileTemplateService.modifyMobileContents(mobileVo);
            }

            return mav;
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    public ModelAndView editor(HttpServletRequest request) throws Exception {
        Map<String, List<?>> returnData = new HashMap<>();
        int page = ServletRequestUtils.getIntParameter(request, "page").intValue();
        Integer start = new Integer((page - 1) * 6);
        Integer end = new Integer((page - 1) * 6 + 6);
        Map<String, Object> param = new HashMap<>();
        param.put("start", start);
        param.put("end", end);
        List<MobileVo> list = mobileTemplateService.getMobileList(param);
        returnData.put("list", list);
        return new ModelAndView(new MobileListJsonView(), returnData);
    }
}
