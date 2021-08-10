package com.bitcom.openepark.util;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;


public class HttpUtils {
    public static String sendGet(String url, String param, int timeOut) throws Exception {
        String result = "";
        BufferedReader in = null;
        try {
            if (!"".equals(param)) {
                url = url + "?" + param;
            }
            URL realurl = new URL(url);

            URLConnection connection = realurl.openConnection();

            connection.setConnectTimeout(timeOut);
            connection.setReadTimeout(timeOut);
            connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();

            for (String str : map.keySet()) ;


            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result = result + line;
            }
        } catch (Exception e) {
            throw e;
        } finally {


            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                throw e2;
            }
        }
        return result;
    }


    public static String sendPost(String url, String param, String contentType) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();
        try {
            URL realurl = new URL(url);

            URLConnection conn = realurl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Accept", "text/plain,text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("user-agent", "mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Length", String.valueOf(param.getBytes().length));
            if (contentType != null && !"".equals(contentType)) {
                conn.setRequestProperty("Content-Type", contentType);
            }

            conn.setDoOutput(true);
            conn.setDoInput(true);

            out = new PrintWriter(conn.getOutputStream());

            out.print(param);

            out.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            throw e;
        } finally {


            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException iOException) {
            }
        }


        return result.toString();
    }


    public static void main(String[] args) throws Exception {
        System.out.println(Charset.defaultCharset());
        String a = "{\"method\":\"getLeaguerInfo\",\"params\":{\"parkCode\":\"191203\",\"plateNo\":\"粤A11111\",\"plateType\":\"02\"}}";


        String b = sendPost("http://topeasypark.com/thirdPark/synchronousTransferCustomData.do", a, "text/html");
        System.out.println(b);
        JSONObject jsonObj = JSONObject.parseObject(JSONObject.parse(b).toString());
        System.out.println(jsonObj);
    }
}



