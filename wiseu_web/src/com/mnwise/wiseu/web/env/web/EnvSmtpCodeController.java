package com.mnwise.wiseu.web.env.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.env.model.TreeNode;
import com.mnwise.wiseu.web.env.model.TreeNodeState;
import com.mnwise.wiseu.web.env.model.json.SmtpCodeTreeEelement;
import com.mnwise.wiseu.web.env.service.EnvSmtpCodeService;

/**
 * SMTP 코드관리 Controller
 */
@Controller
public class EnvSmtpCodeController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EnvSmtpCodeController.class);

    @Autowired private EnvSmtpCodeService envSmtpCodeService;

    /**
     * - [환경설정>코드관리] 코드관리 <br/>
     * - URL : /env/smtpCode.do <br/>
     * - JSP : /env/smtpCode.jsp <br/>
     * 환경설정 - 코드 관리에서 코드 목록을 가져온다.
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/smtpCode.do", method={RequestMethod.GET, RequestMethod.POST})  // /env/smtpcode.do
    public ModelAndView list() throws Exception {
        try {
            Map<String, List<SmtpCodeTreeEelement>> returnData = new HashMap<>();
            returnData.put("smtpcodeList", envSmtpCodeService.selectSmtpCategoryList(null));
            return new ModelAndView("/env/smtpCode", returnData);  // /env/env_smtpcode
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [환경설정>코드관리] 코드관리 - 코드트리 JSON
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/smtpCodeTree.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ArrayList<TreeNode> smtpCodeTree(HttpServletRequest request) throws Exception {
        String root = ServletRequestUtils.getStringParameter(request, "root");
        root = root == null ? null : root.equalsIgnoreCase("source") ? null : root;
        List<SmtpCodeTreeEelement> groupList = envSmtpCodeService.selectSmtpCategoryList(root);
        ArrayList<TreeNode> treeList = new ArrayList<TreeNode>();
        TreeNodeState nodeState = new TreeNodeState();
        nodeState.setOpened(true);
        if(root ==null) {
            SmtpCodeTreeEelement[] element = new SmtpCodeTreeEelement[groupList.size()];
            for(int i = 0; i < element.length; i++) {
                element[i] = groupList.get(i);
                // 루트노드를 하나 만들어준다.
                treeList.add(new TreeNode(String.valueOf(element[i].getId()), element[i].getCategoryNm(), element[i].getParentId(), nodeState));
                List<SmtpCodeTreeEelement> childrenList = envSmtpCodeService.selectSmtpCategoryList(element[i].getId());
                for(SmtpCodeTreeEelement children: childrenList) {
                    treeList.add(new TreeNode(String.valueOf(children.getId()), children.getCategoryNm(), children.getParentId(),nodeState));
                }
            }
        } else {
                // 각 메뉴를 리스트에 넣어준다.
                for (SmtpCodeTreeEelement node: groupList) {
                    treeList.add(new TreeNode(String.valueOf(node.getId()), node.getCategoryNm(), node.getParentId()));
                }
        }
        return treeList;
    }


    /**
     * [환경설정>코드관리] 코드관리 - 특정 코드 카테고리의 코드 목록
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/smtpCodeList.do", method={RequestMethod.GET, RequestMethod.POST})  // /env/smtpcodelist.do
    public ModelAndView codeList(String categoryCd) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("/env/smtpCodeList");  // /env/env_smtpcodelist
            mav.addObject("codeList", envSmtpCodeService.selectCode(categoryCd));
            mav.addObject("categoryCd", categoryCd);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [환경설정>코드관리] 코드관리 - SMTP 코드 추가
     *
     * @param categoryCd
     * @param errorCd
     * @param errorDesc
     * @param spamYn
     * @return
     */
    @RequestMapping(value="/env/insertSmtpCodeList.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto insertSmtpCodeList(String categoryCd, String errorCd, String errorDesc, String spamYn) {
        try {
            int row = envSmtpCodeService.insertSmtpCodeList(categoryCd, errorCd, errorDesc, spamYn);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setRowCount(row);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [환경설정>코드관리] 코드관리 - SMTP 코드 삭제
     *
     * @param categoryCd
     * @param errorCd
     * @return
     */
    @RequestMapping(value="/env/deleteSmtpCodeList.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto deleteSmtpCodeList(String categoryCd, String errorCd) {
        try {
            int row = envSmtpCodeService.deleteSmtpCodeList(categoryCd, errorCd);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setRowCount(row);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
