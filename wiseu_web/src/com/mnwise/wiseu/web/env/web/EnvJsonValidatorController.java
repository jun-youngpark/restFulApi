package com.mnwise.wiseu.web.env.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.env.service.EnvJsonValidatorService;

/** 전문 검증 화면
 */
@Controller
public class EnvJsonValidatorController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EnvJsonValidatorController.class);

    @Autowired private EnvJsonValidatorService envJsonValidatorService;
   

    @RequestMapping(value="/env/envJsonVaildator.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView view(HttpServletRequest request) throws Exception {
        Map<String, Object> returnData = new HashMap<>();
        String json = ServletRequestUtils.getStringParameter(request, "json", "");
        String cmd = ServletRequestUtils.getStringParameter(request, "cmd", "");
        String seq = ServletRequestUtils.getStringParameter(request, "seq", "");
        if("seq".equals(cmd)) {
            json = envJsonValidatorService.getJonmun(seq);
            if(json == null) {
                json = "";
                returnData.put("searchMsg", "데이터가 없습니다.");
            }
        }
        Map<String, Object> jsonReport = null;
        if(!json.isEmpty()) {
            jsonReport = validate(json.replaceAll("&quot;", "\""));
        }
        returnData.put("json", json);
        returnData.put("seq", seq);
        returnData.put("jsonReport", jsonReport);
        
        return new ModelAndView("/env/env_json_vaildator", returnData);
    }
    public Map<String, Object> validate(String json) throws IOException, ParseException {
        Map<String, Object> returnJson = new  HashMap<String, Object>();

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser;
        String fmtJson = "";
        try {
            json = json.replaceAll(System.getProperty("line.separator"), "");
            parser = factory.createParser(json);
            JsonNode jsonObj = mapper.readTree(parser);
            // json 문자열 fomatting
            fmtJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
            returnJson.put("json", fmtJson);
        } catch(JsonParseException e) { //문법 에러 검출
            String errMsg = e.getMessage();
            //에러 위치 저장
            String errPoint = errMsg.substring(errMsg.indexOf("column: "), errMsg.length());
            int errColumn = Integer.parseInt(errPoint.replaceAll("[^0-9]", ""));
            String str = json.substring(errColumn < 20 ? 0 : errColumn - 20
                    , errColumn + 20 < json.length() ? errColumn + 20 : json.length());

            returnJson.put("errMsg",errMsg);
            returnJson.put("errColumn",errColumn);
            returnJson.put("errWord",errMsg.substring(0, errMsg.indexOf("[Source:")));
            returnJson.put("errPoint", str);
        }
        
        return returnJson;
        

    }
}
