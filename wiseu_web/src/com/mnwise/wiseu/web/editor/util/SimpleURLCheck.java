package com.mnwise.wiseu.web.editor.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.util.PatternMatchUtils;

public class SimpleURLCheck {
    public static int getHttpStatusCode(String urlString) {
        int statusCode = 0;
        int timeOut = 5000;
        String URLSTRING = urlString.toUpperCase();
        String[] allowProtocol = {
            "HTTP://*", "HTTPS://*"
        };
        if(PatternMatchUtils.simpleMatch(allowProtocol, URLSTRING)) {
            try {
                URL url = new URL(urlString);
                URLConnection connection = url.openConnection();
                if(connection instanceof HttpURLConnection) {
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    httpConnection.connect();
                    httpConnection.setConnectTimeout(timeOut);
                    int response = httpConnection.getResponseCode();
                    return response;
                }
            } catch(IOException e) {
                return 404;
            }
        } else {
            return 404;
        }

        return statusCode;
    }
}
