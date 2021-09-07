package com.mnwise.wiseu.web.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.multipart.MultipartFile;

import com.mnwise.common.io.IOUtil;

public class HttpUtils {

    public static final String sendGetRequest(String url) throws IOException {
        final HttpGet get = new HttpGet(url);

        final HttpClient client = new DefaultHttpClient();
        final HttpResponse response = client.execute(get);

        final StringBuilder sb = new StringBuilder();
        BufferedReader in = null;
        String tmp;
        try {
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while((tmp = in.readLine()) != null) {
                sb.append(tmp);
            }
        } catch(IOException e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(in);
        }
        return sb.toString();
    }

    public static final String sendPostRequest(String url, List<NameValuePair> params) throws IOException {
        final HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        final HttpClient client = new DefaultHttpClient();
        final HttpResponse response = client.execute(post);

        final StringBuilder sb = new StringBuilder();
        BufferedReader in = null;
        String tmp;
        try {
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while((tmp = in.readLine()) != null) {
                sb.append(tmp);
            }
        } catch(IOException e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(in);
        }
        return sb.toString();
    }

    public static final String sendPostRequest(String url, String jsonData) throws IOException {
        final HttpPost post = new HttpPost(url);
        StringEntity input = new StringEntity(jsonData, "UTF-8");
        post.setEntity(input);

        final HttpClient client = new DefaultHttpClient();
        final HttpResponse response = client.execute(post);

        final StringBuilder sb = new StringBuilder();
        BufferedReader in = null;
        String tmp;
        try {
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while((tmp = in.readLine()) != null) {
                sb.append(tmp);
            }
        } catch(IOException e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(in);
        }
        return sb.toString();
    }
    public static final String sendPostandFileRequest(String url, List<NameValuePair> params, Map<String,MultipartFile> multipartFile) throws IOException {
        final HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE , null , Charset.forName("UTF-8")); // HttpMultipartMode
        //string value 전송
        for (int i = 0; i < params.size(); i++) {
             BasicNameValuePair param = (BasicNameValuePair)params.get(i);
             StringBody strBody = new StringBody(param.getValue(), Charset.forName("UTF-8"));
             mpEntity.addPart(param.getName(), strBody);
        }
        //file value 전송
        Iterator<Map.Entry<String,MultipartFile>> entries = multipartFile.entrySet().iterator();
        while(entries.hasNext()){
            Entry<String,MultipartFile> entry = (Entry<String,MultipartFile>)entries.next();
            mpEntity.addPart( entry.getKey(), new InputStreamBody(entry.getValue().getInputStream(),entry.getValue().getOriginalFilename()));
        }
        post.setEntity( mpEntity );

        final HttpClient client = new DefaultHttpClient();
        final HttpResponse response = client.execute(post);
        final StringBuilder sb = new StringBuilder();
        BufferedReader in = null;
        String tmp;
        try {
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while((tmp = in.readLine()) != null) {
                sb.append(tmp);
            }
        } catch(IOException e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(in);
        }
        return sb.toString();
    }

    public static final String sendPostRequest(String url, List<NameValuePair> params, List<MultipartFile> multiFileList) throws IOException {
        final HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE , null , Charset.forName("UTF-8")); // HttpMultipartMode
        //string value 전송
        for (int i = 0; i < params.size(); i++) {
             BasicNameValuePair param = (BasicNameValuePair)params.get(i);
             StringBody strBody = new StringBody(param.getValue(), Charset.forName("UTF-8"));
             mpEntity.addPart(param.getName(), strBody);
        }
        //file value 전송
        for(int j=0 ; j<multiFileList.size(); j++) {
            MultipartFile multipartFile = multiFileList.get(j);
            if(multipartFile.getName().startsWith("attachment")) {
                mpEntity.addPart("attachment"+(j+1), new InputStreamBody(multipartFile.getInputStream(), multipartFile.getOriginalFilename()));
            }else {
                mpEntity.addPart(multipartFile.getName(), new InputStreamBody(multipartFile.getInputStream(), multipartFile.getOriginalFilename()));
            }
        }
        post.setEntity(mpEntity);

        final HttpClient client = new DefaultHttpClient();
        final HttpResponse response = client.execute(post);
        final StringBuilder sb = new StringBuilder();
        BufferedReader in = null;
        String tmp;
        try {
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while((tmp = in.readLine()) != null) {
                sb.append(tmp);
            }
        } catch(IOException e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(in);
        }
        return sb.toString();
    }
}
