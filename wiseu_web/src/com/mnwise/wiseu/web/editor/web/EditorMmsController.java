package com.mnwise.wiseu.web.editor.web;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.editor.model.MmsVo;
import com.mnwise.wiseu.web.editor.service.EditorMmsService;

/**
 * MMS 저작기 구현중
 */
@Controller
public class EditorMmsController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EditorMmsController.class);

    @Autowired private EditorMmsService editorMmsService;

    /**
     * [이케어>이케어 등록>메시지 작성] LMS/MMS 템플릿/이미지 리스트
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/editor/mmsContsList.do", "/editor/frtContsList.do"}, method = {RequestMethod.GET, RequestMethod.POST})  // /editor/mms_contents_list.do
    public ModelAndView list(HttpServletRequest request, MmsVo mmsVo) throws Exception {
        try {
            mmsVo.setUserVo(getLoginUserVo());
            PagingUtil.setPagingInfo(request, mmsVo);
            int mmsContentsListTotalCount = editorMmsService.selectEditorMmsContentsListMax(mmsVo);

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("mmsContentsListTotalCount", String.valueOf(mmsContentsListTotalCount));
            returnData.put("mmsContentsList", editorMmsService.selectEditorMmsContentsList(mmsVo));
            returnData.put("fileType", mmsVo.getFileType());
            returnData.put("userId", mmsVo.getUserVo().getUserId());
            returnData.put("contsTxt", mmsVo.getContsTxt());

            PagingUtil.transferPagingInfo(request, mmsContentsListTotalCount);

            String view = Channel.FRIENDTALK.equals(mmsVo.getFileType()) ? "/editor/frtContsList" : "editor/mmsContsList";  // /editor/frtalk_image_editor, editor/mms_contents_editor
            return new ModelAndView(view, returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/editor/selectEditorMmsContentsFilePreviewPath.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public String selectEditorMmsContentsFilePreviewPath(String filePath) {
        try {
            return editorMmsService.selectEditorMmsContentsFilePreviewPath(filePath);
        } catch(Exception e) {
            log.error(null, e);
        }

        return null;
    }
}
