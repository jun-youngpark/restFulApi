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
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.editor.model.SmsVo;
import com.mnwise.wiseu.web.editor.service.EditorSmsService;

/**
 * SMS 저작기 구현중
 *
 * - 20100825 : SMS 저작기의 템플릿 검색을 내용에서 찾기로 변경
 */
@Controller
public class EditorSmsController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EditorSmsController.class);

    @Autowired private EditorSmsService editorSmsService;

    /**
     * [이케어>이케어 등록>메시지 작성] SMS 템플릿 리스트
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editor/smsContsList.do", method = {RequestMethod.GET, RequestMethod.POST})  // /editor/sms_contents_list.do
    public ModelAndView list(HttpServletRequest request, SmsVo smsVo) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            smsVo.setUserVo(getLoginUserVo());
            PagingUtil.setPagingInfo(request, smsVo);

            int smsContentsListTotalCount = editorSmsService.selectEditorSmsContentsListMax(smsVo);

            // 컨텐츠 총 건수다. 사용되지는 않지만 보여줘야 할지도 모르니 값만 가지고 있는다.
            returnData.put("smsContentsListTotalCount", String.valueOf(smsContentsListTotalCount));
            returnData.put("smsContentsList", editorSmsService.selectEditorSmsContentsList(smsVo));
            returnData.put("contsTxt", smsVo.getContsTxt());
            returnData.put("userId", smsVo.getUserVo().getUserId());

            PagingUtil.transferPagingInfo(request, smsContentsListTotalCount);

            return new ModelAndView("editor/smsContsList", returnData);  // editor/sms_contents_editor
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
