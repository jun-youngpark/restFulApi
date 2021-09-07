package com.mnwise.wiseu.web.rest.parent;


import java.util.Optional;

import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.model.common.TraceInfo;
import com.mnwise.wiseu.web.rest.model.ecare.Ecare;

public class BaseService {

	protected boolean isScheduleOrScheduleMin(Ecare ecare) {
        return !ecare.getSubType().equals(Const.SubType.NREALTIME);
    }

	protected boolean isSchedule(Ecare ecare) {
        return ecare.getSubType().equals(Const.SubType.SCHDULE);
    }

	protected boolean isScheduleMin(Ecare ecare) {
        return ecare.getSubType().equals(Const.SubType.SCHDULE_M);
    }

	protected boolean isNrealTime(Ecare ecare) {
        return ecare.getSubType().equals(Const.SubType.NREALTIME);
    }

	protected TraceInfo makeTraceInfo(int serviceNo) {
		return TraceInfo.builder()
				.traceType("TRACE")
				.termType("1")
				.serviceNo(serviceNo)
				.build();
	}

	protected void isNullException(Object value, String errorMsg) throws Exception {
		Optional
		.ofNullable(value)
		.orElseThrow(() -> new Exception(errorMsg));
	}

}

