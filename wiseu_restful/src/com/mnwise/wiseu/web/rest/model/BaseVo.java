package com.mnwise.wiseu.web.rest.model;

import javax.validation.constraints.NotNull;
import static com.mnwise.wiseu.web.rest.model.Groups.list;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseVo {

	protected String language ="ko";

	@Range(min = 0, max =30,groups = {list.class})
	@NotNull(groups = {list.class})
	protected Integer limit;
	@Range(min = 0, max =1000,groups = {list.class})
    @NotNull(groups = {list.class})
	protected Integer nowPage;
	@NotNull(groups = {list.class})
	protected String searchStartDt;
	@NotNull(groups = {list.class})
	protected String searchEndDt;

   public String toStringJson() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE, true, false, true , null);
    }

   public String toString() {
	   return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
   }

}
