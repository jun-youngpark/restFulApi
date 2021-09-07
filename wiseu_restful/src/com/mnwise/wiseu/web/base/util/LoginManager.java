package com.mnwise.wiseu.web.base.util;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class LoginManager implements HttpSessionBindingListener {

    private static LoginManager loginManager = null;
    private static Hashtable<HttpSession, String> loginUsers = new Hashtable<>();

    /**
     * 동일 계정 로그인 하게 되면 이전 세션은 종료시킨다.
     * 
     * @return
     */
    public static synchronized LoginManager getInstance() {
        if(loginManager == null) {
            loginManager = new LoginManager();
        }
        return loginManager;
    }

    /**
     * 해당 아이디를 사용 중인지 확인한다.
     * 
     * @param userID
     * @return
     */
    public boolean isUsing(String userID) {
        return loginUsers.containsValue(userID);
    }

    /**
     * 입력받은 아이디를 해시테이블에서 삭제.
     * 
     * @param userId
     */
    public void removeSession(String userId) {
        Enumeration<HttpSession> e = loginUsers.keys();
        HttpSession session = null;
        while(e.hasMoreElements()) {
            session = e.nextElement();
            if(loginUsers.get(session).equals(userId)) {
                session.invalidate();
            }
        }
    }

    /**
     * 로그인한 사용자 계정을 세션에 저장한다.
     * 
     * @param session
     * @param userId
     */
    public void setSession(HttpSession session, String userId) {
        session.setAttribute(userId, this);
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        loginUsers.put(event.getSession(), event.getName());
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        loginUsers.remove(event.getSession());

    }
}
