package com.mnwise.wiseu.web.editor.web;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.editor.service.EditorCampaignService;
import com.mnwise.wiseu.web.editor.service.EditorEcareService;

@Controller
public class EditorAjaxController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EditorAjaxController.class);

    @Autowired private EditorCampaignService editorCampaignService;
    @Autowired private EditorEcareService editorEcareService;

    @RequestMapping(value="/editor/updateEditorCampaignTemplate.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto updateEditorCampaignTemplate(int no, int segmentNo, String template, String seg, String handlerType) {
        try {
            int rowCount = editorCampaignService.updateEditorCampaignTemplate(no, segmentNo, template, seg, handlerType, null);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setRowCount(rowCount);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    @RequestMapping(value="/editor/deleteEditorCampaignTemplate.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto deleteEditorCampaignTemplate(int no, String seg) {
        try {
            int rowCount = editorCampaignService.deleteEditorCampaignTemplate(no, seg);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setRowCount(rowCount);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    @RequestMapping(value="/editor/updateEditorEcareTemplate.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto updateEditorEcareTemplate(int no, int segmentNo, String template, String serviceType, String handlerType) {
        try {
            List<TemplateVo> templateVos = new LinkedList<TemplateVo>();
            TemplateVo templateVo = new TemplateVo();
            templateVo.setTemplate(template);
            templateVos.add(templateVo);

            int value = editorEcareService.updateEditorEcareTemplate(no, templateVos);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(String.valueOf(value));
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    @RequestMapping(value="/editor/deleteEditorEcareTemplate.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto deleteEditorEcareTemplate(int no, String seg) {
        try {
            int rowCount = editorEcareService.deleteEditorEcareTemplate(no, seg);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setRowCount(rowCount);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
