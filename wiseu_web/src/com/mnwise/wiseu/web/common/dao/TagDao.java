package com.mnwise.wiseu.web.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.common.util.CollectionUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.Tag;

/**
 * 태그 테이블(NVCAMPTAG, NVECAREMSGTAG, NVCONTENTSTAG, NVSEGMENTTAG, NVMOBILECONTENTSTAG) 공통 DAO 클래스
 */
@Repository
public class TagDao extends BaseDao {
    public int insertTag(String tableName, String tagNm) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tableName", tableName);
        paramMap.put("tagNm", tagNm);
        return insert("Tag.insertTag", paramMap);
    }

    public int updateTagByPk(String tableName, int tagNo, String tagNm) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tableName", tableName);
        paramMap.put("tagNo", tagNo);
        paramMap.put("tagNm", tagNm);
        return update("Tag.updateTagByPk", paramMap);
    }

    public int deleteTagByPk(String tableName, int tagNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tableName", tableName);
        paramMap.put("tagNo", tagNo);
        return delete("Tag.deleteTagByPk", paramMap);
    }

    public Tag selectTagByPk(String tableName, int tagNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tableName", tableName);
        paramMap.put("tagNo", tagNo);
        return (Tag) selectOne("Tag.selectTagByPk", paramMap);
    }

    public String selectTagNmByPk(String tableName, int tagNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tableName", tableName);
        paramMap.put("tagNo", tagNo);
        return (String) selectOne("Tag.selectTagNmByPk", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<Integer> selectTagNo(String tableName, String tagNm) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tableName", tableName);
        paramMap.put("tagNm", tagNm);
        return (List<Integer>) selectList("Tag.selectTagNo", paramMap);
    }

    public int selectTagNoWithInsert(String tableName, String tagNm) {
        List<Integer> tagNoList = selectTagNo(tableName, tagNm);

        if(CollectionUtil.isEmpty(tagNoList)) {
            insertTag(tableName, tagNm);
            tagNoList = selectTagNo(tableName, tagNm);
        }

        return CollectionUtil.isEmpty(tagNoList) ? 1 : tagNoList.get(0).intValue();
    }

    public int selectNextTagNo(String tableName) {
        return (Integer) selectOne("Tag.selectNextTagNo", tableName);
    }

    /**
     * 태그리스트 가져오기
     *
     * @param onMenu
     * @param userVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Tag> getTagList(String onMenu, UserVo userVo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("onMenu", onMenu);
        paramMap.put("userVo", userVo);
        return (List<Tag>) selectList("Tag.selectTagList", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<Tag> selectTagCloud(Map<String, Object> map) {
        return selectList("Tag.selectTagCloudList", map);
    }

}