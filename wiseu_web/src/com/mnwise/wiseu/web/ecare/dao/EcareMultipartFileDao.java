package com.mnwise.wiseu.web.ecare.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.editor.model.MultipartFileVo;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * NVECAREMULTIPARTFILE 테이블 DAO 클래스
 */
@Repository
public class EcareMultipartFileDao extends BaseDao {
    public int insertEcareMultipartFile(MultipartFileVo multipartFile) {
        return insert("EcareMultipartFile.insertEcareMultipartFile", multipartFile);
    }

    public int deleteEcareMultipartFile(int ecareNo) {
        return delete("EcareMultipartFile.deleteEcareMultipartFile", ecareNo);
    }
    public int selectEditorEcareMultipartFileCount(int ecareNo) {
        return (int) selectOne("EcareMultipartFile.selectEditorEcareMultipartFileCount", ecareNo);
    }

    public int deleteEditorEcareMultipartfile(MultipartFileVo multipartFile) {
        return delete("EcareMultipartFile.deleteEditorEcareMultipartFile", multipartFile);
    }

    @SuppressWarnings("unchecked")
    public List<MultipartFileVo> selectEditorEcareMultipartFile(int ecareNo) {
        return selectList("EcareMultipartFile.selectEditorEcareMultipartFile", ecareNo);
    }

    public MultipartFileVo selectEditorEcareMultipartFileOne(MultipartFileVo multipartFile) {
        return (MultipartFileVo) selectOne("EcareMultipartFile.selectEditorEcareMultipartFileOne", multipartFile);
    }

    public int selectEditorEcareMultipartFileMax(int ecareNo) {
        return (Integer) selectOne("EcareMultipartFile.selectEditorEcareMultipartFileMax", ecareNo);
    }
}