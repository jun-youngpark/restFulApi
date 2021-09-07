package org.springframework.web.servlet.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TreeJsonView extends AbstractView {
    private static final String DEFAULT_JSON_CONTENT_TYPE = "text/plain; charset=ISO-8859-1";

    public TreeJsonView() {
        super();
        setContentType(DEFAULT_JSON_CONTENT_TYPE);
    }

    @SuppressWarnings("rawtypes")
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONArray jsonArray = null;
        JSONObject[] jo = (JSONObject[]) model.get("JsonArr");
        List groupList = (List) model.get("groupList");
        if(jo == null) {
            jsonArray = JSONArray.fromObject(groupList);
        } else {
            jsonArray = JSONArray.fromObject(jo);
        }
        response.getWriter().write(jsonArray.toString());
    }
}