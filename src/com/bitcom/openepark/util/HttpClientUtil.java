package com.bitcom.openepark.util;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class HttpClientUtil {
    public static final String CHARSET_UTF_8 = "utf-8";
    public static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=utf-8";
    public static final String CONTENT_TYPE_FORM_URL = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON_URL = "application/json;charset=utf-8";
    private static PoolingHttpClientConnectionManager pool;
    private static RequestConfig requestConfig;

    static {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, (TrustStrategy) new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslsf)
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .build();
            pool = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            pool.setMaxTotal(200);

            pool.setDefaultMaxPerRoute(2);

            int socketTimeout = 10000;
            int connectTimeout = 10000;
            int connectionRequestTimeout = 10000;


            requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


        requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000).setConnectionRequestTimeout(50000).build();
    }


    public static CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager((HttpClientConnectionManager) pool).setDefaultRequestConfig(requestConfig).setRetryHandler((HttpRequestRetryHandler) new DefaultHttpRequestRetryHandler(0, false)).build();

        return httpClient;
    }


    private static String sendHttpPost(HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;

        String responseContent = null;

        try {
            httpClient = getHttpClient();

            httpPost.setConfig(requestConfig);

            response = httpClient.execute((HttpUriRequest) httpPost);

            HttpEntity entity = response.getEntity();


            if (response.getStatusLine().getStatusCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + response
                        .getStatusLine().getStatusCode());
            }

            if (200 == response.getStatusLine().getStatusCode()) {
                responseContent = EntityUtils.toString(entity, "utf-8");
                EntityUtils.consume(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }


    private static String sendHttpGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;

        String responseContent = null;

        try {
            httpClient = getHttpClient();

            httpGet.setConfig(requestConfig);

            response = httpClient.execute((HttpUriRequest) httpGet);

            HttpEntity entity = response.getEntity();


            if (response.getStatusLine().getStatusCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + response
                        .getStatusLine().getStatusCode());
            }

            if (200 == response.getStatusLine().getStatusCode()) {
                responseContent = EntityUtils.toString(entity, "utf-8");
                EntityUtils.consume(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }


    public static String sendHttpPost(String httpUrl) {
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpPost(httpPost);
    }


    public static String sendHttpGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpGet(httpGet);
    }


    public static String sendHttpPost(String httpUrl, Map<String, String> maps, List<File> fileLists) {
        HttpPost httpPost = new HttpPost(httpUrl);
        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
        if (maps != null) {
            for (String key : maps.keySet()) {
                meBuilder.addPart(key, (ContentBody) new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
            }
        }
        if (fileLists != null) {
            for (File file : fileLists) {
                FileBody fileBody = new FileBody(file);
                meBuilder.addPart("files", (ContentBody) fileBody);
            }
        }
        HttpEntity reqEntity = meBuilder.build();
        httpPost.setEntity(reqEntity);
        return sendHttpPost(httpPost);
    }


    public static String sendHttpPost(String httpUrl, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(httpUrl);

        try {
            List<NameValuePair> nvps = new ArrayList<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(nvps, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }


    public static String sendHttpPostJson(String httpUrl, String paramsJson) {
        HttpPost httpPost = new HttpPost(httpUrl);

        try {
            if (paramsJson != null && paramsJson.trim().length() > 0) {
                StringEntity stringEntity = new StringEntity(paramsJson, "UTF-8");
                stringEntity.setContentType("application/json;charset=utf-8");
                httpPost.setEntity((HttpEntity) stringEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }


    public static String sendHttpPostXml(String httpUrl, String paramsXml) {
        HttpPost httpPost = new HttpPost(httpUrl);

        try {
            if (paramsXml != null && paramsXml.trim().length() > 0) {
                StringEntity stringEntity = new StringEntity(paramsXml, "UTF-8");
                stringEntity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity((HttpEntity) stringEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }


    public static String convertStringParamter(Map parameterMap) {
        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator<String> iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String) parameterMap.get(key);
                } else {
                    value = "";
                }
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
        }
        return parameterBuffer.toString();
    }


    public static void main(String[] args) throws Exception {
        System.out.println(sendHttpGet("http://www.baidu.com"));
    }
}



