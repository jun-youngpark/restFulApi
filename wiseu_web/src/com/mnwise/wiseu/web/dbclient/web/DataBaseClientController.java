package com.mnwise.wiseu.web.dbclient.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.dbclient.model.DataBaseClientVo;
import com.mnwise.wiseu.web.dbclient.service.DataBaseClientService;

@Controller
public class DataBaseClientController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DataBaseClientController.class);

    @Autowired private DataBaseClientService dataBaseClientService;

    @RequestMapping(value="/dbclient/dbclient.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            List<DataBaseClientVo> dbClientList = dataBaseClientService.getDbinfo();

            if(dbClientList.size() == 0) {
                returnData.put("message", "<font color=\"blue\">☞ NVDBINFO 테이블에 데이터가 존재하지 않습니다.</font>");
            } else {
                returnData.put("dbClientList", dbClientList);
            }
            return new ModelAndView("/dbclient/dbclient", returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/dbclient/select.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView select(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();

            DataBaseClientVo dbClientVo = new DataBaseClientVo();
            dbClientVo.setDbms(ServletRequestUtils.getStringParameter(request, "dbms"));
            dbClientVo.setJdbcDriver(ServletRequestUtils.getStringParameter(request, "jdbcDriver"));
            dbClientVo.setJdbcUrl(ServletRequestUtils.getStringParameter(request, "jdbcUrl"));
            dbClientVo.setJdbcUserName(ServletRequestUtils.getStringParameter(request, "jdbcUserName"));
            dbClientVo.setJdbcPassWord(ServletRequestUtils.getStringParameter(request, "jdbcPassWord"));

            String dbms = ServletRequestUtils.getStringParameter(request, "dbms");
            String jdbcDriver = ServletRequestUtils.getStringParameter(request, "jdbcDriver");
            String jdbcUrl = ServletRequestUtils.getStringParameter(request, "jdbcUrl");
            String jdbcUserName = ServletRequestUtils.getStringParameter(request, "jdbcUserName");
            String jdbcPassWord = ServletRequestUtils.getStringParameter(request, "jdbcPassWord");
            String queryText = ServletRequestUtils.getStringParameter(request, "queryText").trim();
            String query = null;
            String textArray[] = queryText.split("\n");
            StringBuilder text = new StringBuilder();
            for(int i = 0; i < textArray.length; i++) {
                if(textArray[i].indexOf("--") < 0 && textArray[i].trim().length() != 0) {
                    text.append(textArray[i]).append("\n");
                }
            }
            query = text.toString();
            query = query.replace(";", "");
            String imsiQueryText = query.toUpperCase();
            dbClientVo.setQuery(query);
            String dist = null;
            int result = 0;
            if(imsiQueryText.length() > 6 && imsiQueryText.indexOf("SELECT") > -1 && imsiQueryText.indexOf("SELECT") < 6) {
                try {
                    List<String> columnList = dataBaseClientService.getResultMetaData(dbClientVo);
                    List<String> columnValue = dataBaseClientService.getResultData(dbClientVo);

                    if(columnList != null && columnValue != null) {
                        returnData.put("message", "<font style=\"color:blue;size:13px\">☞ 조회에 성공하였습니다.</font>");
                        returnData.put("columnList", columnList);
                        returnData.put("columnValue", columnValue);
                        returnData.put("columnCount", columnList.size());
                    } else {
                        returnData.put("message", "<font style=\"color:red;size:13px\">☞ 조회에 실패하였습니다.</font>");
                        returnData.put("error", dbClientVo.getErrMsg());
                    }

                } catch(Exception e) {
                    returnData.put("message", "<font style=\"color:red;size:13px\">☞ 조회에 실패하였습니다.</font>");
                    returnData.put("error", e);
                }
            } else if(imsiQueryText.length() > 6) {
                if(imsiQueryText.indexOf("INSERT") > -1) {
                    dist = "입력";
                } else if(imsiQueryText.indexOf("UPDATE") > -1 || imsiQueryText.indexOf("ALERT") > -1) {
                    dist = "수정";
                } else if(imsiQueryText.indexOf("DELETE") > -1 || imsiQueryText.indexOf("DROP") > -1 || imsiQueryText.indexOf("TRUNCATE") > -1) {
                    dist = "삭제";
                } else if(imsiQueryText.indexOf("CREATE") > -1) {
                    dist = "생성";
                } else {
                    dist = "기타 쿼리문";
                }
                try {
                    result = dataBaseClientService.setResultData(dbClientVo);
                    if(result < 0)
                        throw new Exception(dist + "에 실패하였습니다.");
                    returnData.put("message", "<font style=\"color:blue;size:13px\">☞" + dist + "에 성공하였습니다.</font>");
                } catch(Exception e) {
                    returnData.put("message", "<font style=\"color:red;size:13px\">☞" + dist + "에 실패하였습니다.</font>");
                    returnData.put("error", e);

                }
            }
            returnData.put("dbClientVo", dbClientVo);
            returnData.put("dbms", dbms);
            returnData.put("jdbcDriver", jdbcDriver);
            returnData.put("jdbcUrl", jdbcUrl);
            returnData.put("jdbcUserName", jdbcUserName);
            returnData.put("jdbcPassWord", jdbcPassWord);
            returnData.put("queryText", queryText);
            list(request, response);
            returnData.put("dbClientList", dataBaseClientService.getDbinfo());
            return new ModelAndView("/dbclient/dbclient", returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}