package com.mnwise.wiseu.web.rest.model.common;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class ApiLog {
    private String seq;
    private String regType;
    private String interfaceId;
    private String msg;
    private String inVal;
    private String dtlMsg;

}
