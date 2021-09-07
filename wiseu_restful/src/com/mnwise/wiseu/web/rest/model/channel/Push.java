package com.mnwise.wiseu.web.rest.model.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

/**
 * PUSH 채널 Vo
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Setter @Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Push  {
    private String svcType;
    private String pushAppId;
    private String pushMsgInfo;
    private String pushMenuId;
    private String pushPopImgUse;
    private String pushImgUrl;
    private String pushBigImgUse;
    private String pushBigImgUrl;
    private String pushWebUrl;
}
