package org.springframework.web.servlet.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class JsonView extends AbstractView {
    private static final String DEFAULT_JSON_CONTENT_TYPE = "text/plain; charset=ISO-8859-1";

    public JsonView() {
        super();
        setContentType(DEFAULT_JSON_CONTENT_TYPE);
    }

    @SuppressWarnings("rawtypes")
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObject = JSONObject.fromObject(model);
        response.getWriter().write(jsonObject.toString());
    }
}