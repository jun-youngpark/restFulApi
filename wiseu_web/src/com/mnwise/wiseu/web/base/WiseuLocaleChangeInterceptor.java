package com.mnwise.wiseu.web.base;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.WebUtils;

import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.admin.model.AdminSessionVo;

/**
 * Dao, Serivce 단에서도 한글을 사용하고 있어서 language를 꺼내 올 수 있도록 LocaleChangeInterceptor에 getLanguage, getLocale을 추가함.
 * 
 * @변경이력 :
 */
@Component
public class WiseuLocaleChangeInterceptor extends LocaleChangeInterceptor {
    private String language = Locale.getDefault().getLanguage();

    public String getLanguage() {
        return language;
    }

    public Locale getLocale() {
        return new Locale(language);
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        String newLocale = request.getParameter(super.getParamName());

        AdminSessionVo adminSessionVo = (AdminSessionVo) WebUtils.getSessionAttribute(request, "adminSessionVo");
        if(adminSessionVo != null) {
            UserVo userVo = adminSessionVo.getUserVo();
            newLocale = userVo.getLanguage();
        } else if(newLocale == null) {
            newLocale = language;
        }

        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if(localeResolver == null)
            throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
        LocaleEditor localeEditor = new LocaleEditor();
        localeEditor.setAsText(newLocale);
        localeResolver.setLocale(request, response, (Locale) localeEditor.getValue());
        language = newLocale;
        return true;
    }
}
