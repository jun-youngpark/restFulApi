package org.springframework.web.servlet.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

public class MobileListJsonView extends AbstractView {
    private static final String DEFAULT_JSON_CONTENT_TYPE = "text/plain; charset=ISO-8859-1";

    public MobileListJsonView() {
        super();
        setContentType(DEFAULT_JSON_CONTENT_TYPE);
    }

    @SuppressWarnings("rawtypes")
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONArray ja = JSONArray.fromObject(model.get("list"));
        response.getWriter().write(ja.toString());
    }
}