package com.mnwise.wiseu.web.channel.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.channel.model.PushVo;
import com.mnwise.wiseu.web.channel.service.PushService;
import com.mnwise.wiseu.web.env.model.TreeNode;
import com.mnwise.wiseu.web.env.model.TreeNodeState;

/**
 * 환경 설정 - PUSH Controller
 */
@Controller
public class PushController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(PushController.class);

    @Autowired private PushService pushService;

    /**
     * - [환경설정>PUSH 앱 관리] PUSH 앱 관리 <br/>
     * - URL : /env/pushSetUp.do <br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/pushSetUp.do", method = {RequestMethod.GET , RequestMethod.POST})  // /env/env_push_setting.do
    public ModelAndView list(@RequestParam(defaultValue="") String type, @RequestParam(defaultValue="") String workType, HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("/env/pushSetUp");  // /env/env_push_setting
            Map<String,Object> param = new HashMap<>();
            if(StringUtil.isNotBlank(type)) {
                UserVo userVo = getLoginUserVo();
                int result = -1;

                // setting MSG_TYPE info
                if(type.indexOf("msg") > -1) {
                    try {
                        param.put("cd", StringUtil.defaultIfBlank(request.getParameter("CD"), ""));
                         param.put("cd_desc", StringUtil.defaultIfBlank(request.getParameter("CD_DESC"), ""));
                        param.put("use_yn", StringUtil.defaultIfBlank(request.getParameter("USE_YN"), "false").equals("true")?"y":"n");
                        param.put("curr_date",  DateUtil.getNowDateTime());
                        param.put("updateType",  workType);
                        param.put("lang", userVo.getLanguage());
                        result = pushService.updatePushMsgType(param);
                    } catch(Exception e) {
                        log.error(e.getMessage(), e);
                        result = -2;
                    }
                    if(log.isDebugEnabled()) {
                        log.debug(workType + " MSGTYPE info [" + result + "]. cd:" + param.get("cd"));
                    }
                    if(result < 1) {
                        log.error(workType + " ERROR [" + result + "]. cd:" + param.get("cd"));
                    }
                } else {  // setting PUSH APP info
                    param.put("pushAppId", StringUtil.defaultIfBlank(request.getParameter("PUSH_APP_ID"), ""));
                    param.put("pushAppNm", StringUtil.defaultIfBlank(request.getParameter("PUSH_APP_NM"), ""));
                    param.put("useYn", StringUtil.defaultIfBlank(request.getParameter("USE_YN"), "false").equals("true")?"Y":"N");
                    param.put("useTestMode", StringUtil.defaultIfBlank(request.getParameter("USE_TEST_MODE"), "false").equals("true")?"Y":"N");
                    param.put("currDts",  DateUtil.getNowDateTime());
                    param.put("updateType",  workType);
                    param.put("userId", userVo.getUserId());

                    try {
                        result = pushService.updatePushApp(param);
                    } catch(Exception e) {
                        log.error(e.getMessage(), e);
                        result = -2;
                    }

                    if(log.isDebugEnabled()) {
                        log.debug(workType + " push info [" + result + "]. appId:" + param.get("pushAppId"));
                    }
                    if(result < 1) {
                        log.error(workType + " ERROR [" + result + "]. appId:" + param.get("pushAppId"));
                    }
                }
                mav.addObject("workType", workType);
                mav.addObject("resultCnt", result);
            }
            mav.addObject("pushAppList", pushService.selectPushAppList("N"));
            mav.addObject("msgTypeList", pushService.selectPushMsgTypeList(false));

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 환경설정 - PUSH설정 의 코드 관리에서 코드 tree를 json형태로 만든다.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/codeTree/env_push_setting.json", method = RequestMethod.GET)
    @ResponseBody public ArrayList<TreeNode> codeTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String root = ServletRequestUtils.getStringParameter(request, "root");
            root = root == null ? null : root.equalsIgnoreCase("source") ? null : root;
            // 1. code list select
            List<CaseInsensitiveMap> groupList = pushService.selectCodeGroup(root);
            ArrayList<TreeNode> treeList = new ArrayList<TreeNode>();
            TreeNodeState nodeState = new TreeNodeState();
            nodeState.setOpened(true);
            if(root ==null) {
                CaseInsensitiveMap[] element = new CaseInsensitiveMap[groupList.size()];
                for(int i = 0; i < element.length; i++) {
                    element[i] = groupList.get(i);
                    // 루트노드를 하나 만들어준다.
                    treeList.add(new TreeNode(String.valueOf(element[i].get("cd_cat")),String.valueOf(element[i].get("cd_desc")), "#", nodeState));
                    List<CaseInsensitiveMap> childrenList = pushService.selectCodeGroup(String.valueOf(element[i].get("cd_cat")));
                    //자식노드 입력
                    for(CaseInsensitiveMap map: childrenList) {
                        treeList.add(new TreeNode(String.valueOf(map.get("cd_cat")), String.valueOf(map.get("cd_desc")), String.valueOf(map.get("par_cd_cat")), nodeState));
                    }
                }
            } else {
                    for(CaseInsensitiveMap map: groupList) {
                        treeList.add(new TreeNode(String.valueOf(map.get("cd_cat")), String.valueOf(map.get("cd_desc")), String.valueOf(map.get("par_cd_cat")), nodeState));
                    }
            }
            return treeList;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/env/selectPushAppList.json", method = RequestMethod.POST)
    @ResponseBody public List<CaseInsensitiveMap> selectPushAppList(String useOnly) {
        try {
            List<CaseInsensitiveMap> list = pushService.selectPushAppList(useOnly);
            return list;
        } catch (Exception e) {
            log.error(null, e);
        }

        return null;
    }

    @RequestMapping(value="/env/updatePushServiceInfo.json", method = RequestMethod.POST)
    @ResponseBody public int updatePushServiceInfo(int no, String svcType, String pushAppId, String pushMsgInfo, String pushMenuId,
        String pushPopImgUse, String pushImgUrl, String pushBigImgUse, String pushBigImgUrl, String pushWebUrl) throws Exception {

        try {
            return pushService.updatePushServiceInfo(
                svcType, no, pushAppId, pushMsgInfo, pushMenuId, pushPopImgUse,
                pushImgUrl, pushBigImgUse, pushBigImgUrl, pushWebUrl
            );
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [환경설정>PUSH 설정] 코드 관리 트리 클릭시 코드 목록 조회
     *
     * @param cdCat
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/selectPushCodeList.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public Map<String, Object> selectCodeList(String cdCat) throws Exception {
        try {
            return pushService.selectCodeList(cdCat);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
