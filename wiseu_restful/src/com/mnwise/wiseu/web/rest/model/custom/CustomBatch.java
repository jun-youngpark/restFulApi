package com.mnwise.wiseu.web.rest.model.custom;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.rest.model.Groups;
import com.mnwise.wiseu.web.rest.model.ecare.Batch;

import lombok.Getter;
import lombok.Setter;

/**
 * 고객사별 스케줄 테이블 커스터마이징 필드 추가
 */
@Setter
@Getter
public class CustomBatch extends Batch  {
	/*@NotBlank
	private String reqSeq;
	*/
	@NotNull(groups = {Groups.create.class})
	private String omniReceiver;
	
	private String batchSeq;
	
	public String getSeq() {
		return StringUtil.defaultIfBlank(seq, UUID.randomUUID().toString());
	}
}
