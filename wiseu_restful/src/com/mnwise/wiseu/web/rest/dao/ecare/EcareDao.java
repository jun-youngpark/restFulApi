package com.mnwise.wiseu.web.rest.dao.ecare;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.dto.ReturnDto.CampaignDto;
import com.mnwise.wiseu.web.rest.dto.ReturnDto.EcareDto;
import com.mnwise.wiseu.web.rest.model.ecare.Ecare;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

@Repository
public class EcareDao extends BaseDao {

	/**
	 * svcid 조건으로 이케어 번호를 가져온다
	 */
    public Object  selectEcareNoBySvcId(String svcId) throws Exception {
		return selectOne("Ecare.selectEcareNoBySvcId", svcId);
    }

    /**
     * 이케어 정보를 가져온다.
     *
     * @param ecareNo
     * @return
     */
    public Ecare selectEcare(Ecare ecare) {
        return (Ecare) selectOne("Ecare.selectEcare", ecare);
    }

    /**
     * 이케어 수정
     *
     */
	public int updateEcare(Ecare ecare) {
		return update("Ecare.updateEcare", ecare);
	}

    /**
	 * 이케어 생성 (이케어번호 자동생성)
	 */
	public int insertEcareForFirst(Ecare ecare) {
		return insert("Ecare.insertEcareForFirst", ecare);
	}
	public int copyEcareForOmni(Ecare ecare) {
        return insert("Ecare.copyEcareForOmni", ecare);
    }

    /**
     * 새로운 이케어 번호를 가져온다.
     *
     * @return 새로운 이케어 번호(MAX + 1)
     */
    public int selectNextEcareNo() {
        return (int) selectOne("Ecare.selectNextEcareNo");
    }

	public int deleteEcare(Ecare ecare) {
		return update("Ecare.deleteEcare", ecare);
	}

	public List<Ecare> selectEcareList(Ecare ecare) {
		return (List<Ecare>) selectList("Ecare.selectEcareList", ecare);
	}

	public int selectEcareListCount(Ecare ecare) {
		return (int) selectOne("Ecare.selectEcareListCount", ecare);
	}

	public int updateState(Ecare ecare) {
		return (int) update("Ecare.updateState", ecare);
	}

	public EcareDto selectEcareOne(Ecare ecare) {
		return (EcareDto) selectOne("Ecare.selectEcareOne",ecare);
	}

	public List<EcareDto> getOmniList(Ecare ecare) {
		return (List<EcareDto>) selectList("Ecare.findOmniEcare", ecare);
	}


}