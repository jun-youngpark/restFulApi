package com.mnwise.wiseu.web.rest.model.ecare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.rest.model.BaseVo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

/**
 * NVECMSCHEDULE 테이블 모델 클래스
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Setter @Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class EcmSchedule extends BaseVo {
    private int ecmScheduleNo;
    private String ecmScheduleNm;
    private String cycleCd;
    private String sendStartDt;
    private String sendEndDt;
    private String invokeTm;
    private String invokeEveryMin;
    private int day;
    private int scheWeeknumber;
    private String weekday;
    private String startTm;
    private String endTm;
    private String termMin;
    private String monthOpt;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private int newEcmScheduleNo;



    /////////////////////////////////////////////////////////////////
    // 멤버함수
    public String getStartTmToDateStr() {
        if(StringUtil.isNotBlank(startTm)) {
            startTm = startTm.substring(0, 2) + ":" + startTm.substring(2, 4);
        }
        return startTm;
    }
    public String getEndTmToDateStr() {
        if(StringUtil.isNotBlank(endTm)) {
            endTm = endTm.substring(0, 2) + ":" + endTm.substring(2, 4);
        }
        return endTm;
    }

}
