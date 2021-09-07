package com.mnwise.wiseu.web.env.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.env.model.SendBlockDateVo;
import com.mnwise.wiseu.web.env.service.EnvBlockDateService;

/**
 * 공휴일 관리 Controller
 */
@Controller
public class EnvBlockDateController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EnvBlockDateController.class);

    @Autowired private EnvBlockDateService envBlockDateService;

    /**
     * - [환경설정>공휴일 관리] 공휴일 관리 - NVSENDBLOCKDATE 테이블에서 휴일 목록을 가져와 캘린터에 출력<br/>
     * - URL : /env/blockDate.do <br/>
     * - JSP : /env/blockDate.jsp <br/>
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/blockDate.do", method={RequestMethod.GET, RequestMethod.POST})  // /env/blockdate.do
    public ModelAndView list(String currentYear) throws Exception {
        try {
            if(StringUtil.isBlank(currentYear)) {
                currentYear = DateUtil.getNowDateTime("yyyy");
            }

            @SuppressWarnings("unchecked")
            List<SendBlockDateVo> restDays = envBlockDateService.selectRestDays();
            String restDay = "";
            for(int i = 0; i < restDays.size(); i++) {
                restDay += "[" + ((SendBlockDateVo) restDays.get(i)).getChkYear() + "," + ((SendBlockDateVo) restDays.get(i)).getBlockDt().substring(0, 2) + ","
                    + ((SendBlockDateVo) restDays.get(i)).getBlockDt().substring(2, 4) + "]";
                if(i != restDays.size() - 1)
                    restDay += ",";
            }

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("restDay", restDay);
            returnData.put("currentYear", currentYear);

            return new ModelAndView("/env/blockDate", returnData);  // /env/env_blockdate
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * NVSENDBLOCKDATE 테이블에 주어진 일자를 휴일 등록가능여부를 확인한다
     *
     * @param chkYear
     * @param blockDt
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/confirmRestDays.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto confirmRestDays(String chkYear, String blockDt) {
        try {
            boolean isOk = envBlockDateService.confirmRestDays(chkYear, blockDt);  // 공휴일 등록가능 여부
            return isOk ? new ResultDto(ResultDto.OK) : new ResultDto(ResultDto.FAIL);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * NVSENDBLOCKDATE 테이블에 주어진 일자를 휴일로 등록
     *
     * @param chkYear
     * @param blockDt
     * @throws Exception
     */
    @RequestMapping(value="/env/insertRestDays.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto insertRestDays(String chkYear, String blockDt) {
        try {
            envBlockDateService.insertRestDays(chkYear, blockDt);
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * NVSENDBLOCKDATE 테이블에서 등록된 휴일을 삭제
     *
     * @param chkYear
     * @param blockDt
     * @throws Exception
     */
    @RequestMapping(value="/env/deleteRestDays.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto deleteRestDays(String chkYear, String blockDt) {
        try {
            envBlockDateService.deleteRestDays(chkYear, blockDt);
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * NVSENDBLOCKDATEINFO 테이블에서 주어진 년도의 주말발송제한 정보를 가져온다
     *
     * @param year
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/selectWeekendBlockDateInfo.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public String selectWeekendBlockDateInfo(int year) throws Exception {
        try {
            return envBlockDateService.selectRegistInfo(year);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
