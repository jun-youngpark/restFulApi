package com.mnwise.wiseu.web.template.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.template.model.ContentVo;

/**
 * NVCONTENTS 테이블 DAO 클래스
 */
@Repository
public class ContentsDao extends BaseDao {
    public int insertContents(ContentVo contents) {
        return insert("Contents.insertContents", contents);
    }

    public int updateContentsByPk(ContentVo contents) {
        return update("Contents.updateContentsByPk", contents);
    }

    public int updateTemplate(ContentVo contents) {
        return update("Contents.updateTemplate", contents);
    }

    public int deleteContentsByPk(int contsNo) {
        return delete("Contents.deleteContentsByPk", contsNo);
    }

    /**
     * 등록한 템플릿을 삭제한다.
     *
     * @param contentVo
     */
    public int deleteTemplate(ContentVo contentVo) {
        return delete("Contents.deleteTemplate", contentVo);
    }

    public ContentVo selectContentsByPk(int contsNo) {
        return (ContentVo) selectOne("Contents.selectContentsByPk", contsNo);
    }

    public ContentVo selectTemplateInfo(int contsNo) {
        ContentVo tmp = (ContentVo) selectOne("Contents.selectTemplateInfo", contsNo);
        security.securityObject(tmp , "DECRYPT");
        return tmp;
    }

    public int selectNextContsNo() {
        return (Integer) selectOne("Contents.selectNextContsNo");
    }

    /**
     * 템플릿 전체 건수를 가져온다.
     *
     * @param contentVo
     * @return
     */
    public int selectTemplateTotalCount(ContentVo contentVo) {
        Integer count = (Integer) selectOne("Contents.selectTotalCount", contentVo);
        return (count == null) ? 0 : count;
    }

    /**
     * 템플릿 리스트를 가져온다.
     *
     * @param contentVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ContentVo> selectTemplateList(ContentVo contentVo) {
        List<ContentVo> tmp = selectList("Contents.selectTemplateList", contentVo);
        security.securityObjectList(tmp , "DECRYPT");
        return tmp;
    }
}