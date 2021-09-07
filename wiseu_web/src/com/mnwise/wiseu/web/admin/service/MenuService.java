package com.mnwise.wiseu.web.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.account.dao.MenuDao;
import com.mnwise.wiseu.web.account.dao.MenuLangDao;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.common.model.MenuVo;
import com.mnwise.wiseu.web.common.util.StringCharsetConverter;

/**
 * 메뉴 컨트롤에 관련된 데이타를 저장하고 있으며 spring에서 싱글턴으로 로딩한다.
 * NVMENU_LANG 테이블에 언어별 서브메뉴명을 관리하여 서브메뉴를 언어별로 가져오도록 변경
 */
@Service
public class MenuService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(MenuService.class);

    @Autowired private MenuDao menuDao;
    @Autowired private MenuLangDao menuLangDao;

    @Value("${dbEnc}")
    private String dbEnc;
    @Value("${viewEnc}")
    private String viewEnc;

    private List<MenuVo> topMenuList;
    private List<MenuVo> subMenuList;
    private List<String> langList;
    private Map<String, List<MenuVo>> subMenuListMap;
    private Map<String, CaseInsensitiveMap> menuNameMap = new HashMap<>();

    public List<MenuVo> getTopMenuList() {
        return topMenuList;
    }

    public List<MenuVo> getSubMenuList(String language) {
        return subMenuListMap.get(language);
    }

    private void preInit() throws Exception {
        langList = menuLangDao.getLangList();
    }

    public void setDbEnc(String dbEnc) {
        this.dbEnc = dbEnc;
    }

    public void setViewEnc(String viewEnc) {
        this.viewEnc = viewEnc;
    }

    @PostConstruct
    public void init() throws Exception {
        preInit();

        if(topMenuList == null) {
            topMenuList = menuDao.getTopMenuList();

            if(log.isInfoEnabled()) {
                log.info("top menu loading...");
            }
        }

        if(subMenuList == null) {
            subMenuListMap = new HashMap<>();
            for(String lang : langList) {
                subMenuList = menuDao.getSubMenuList(lang);
                subMenuListMap.put(lang, subMenuList);
                menuNameMap.put(lang, menuLangDao.selectMenuNames(lang));
            }

            if(log.isInfoEnabled()) {
                log.info("sub menu loading...");
            }
        }

        StringCharsetConverter.init(dbEnc, viewEnc);
    }

    public Map<String, CaseInsensitiveMap> getMenuNameMap() {
        return menuNameMap;
    }
}
