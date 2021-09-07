package com.mnwise.wiseu.web.env.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.env.service.EnvDomainService;

/**
 * 도메인 관리 Controller
 */
@Controller
public class EnvDomainController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EnvDomainController.class);

    @Autowired private EnvDomainService envDomainService;

    private static final String etcDomain = "ZZZ.DOMAIN";

    /**
     * - [환경설정>도메인 관리] 도메인 관리 <br/>
     * - URL : /env/domain.do <br/>
     * - JSP : /env/domain.jsp <br/>
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/domain.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView view() throws Exception {
        try {
            Map<String, String> returnData = new HashMap<>();
            List<String> domainList = envDomainService.getDomainList();
            StringBuffer sbDomainList = new StringBuffer();
            for(int i = 0; i < domainList.size(); i++) {
                String domainNm = domainList.get(i);
                // 기타 도메인을 제거한다.
                if(domainNm.equals(etcDomain)) {
                    continue;
                }
                sbDomainList.append(domainNm).append(",");
            }
            returnData.put("domainList", sbDomainList.toString().toLowerCase());
            return new ModelAndView("/env/domain", returnData);  // /env/env_domain
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [환경설정>도메인 관리] 도메인 관리 - 저장<br/>
     *
     * @param paramMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/updateDomain.do", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto update(@RequestParam("domains") String domains) throws Exception {
        try {
            if(StringUtil.isNotEmpty(domains)) {
                envDomainService.deleteAllDomain();

                String[] saDomain = domains.trim().split(",");
                // 중복 도메인을 제거한다.
                TreeSet<String> tSet = new TreeSet<String>();
                for(int i = 0; i < saDomain.length; i++) {
                    tSet.add(saDomain[i]);
                }

                Iterator<String> it = tSet.iterator();
                while(it.hasNext()) {
                    envDomainService.insertTopDomain(StringUtil.upperCase(it.next()));
                }
                // 기타 도메인을 추가한다.
                envDomainService.insertTopDomain(etcDomain);
            }
            return new ResultDto(ResultDto.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
