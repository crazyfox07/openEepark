package com.bitcom.openepark.api.vo;

import com.bitcom.openepark.util.HttpUtils;
import org.apache.log4j.Logger;

public class HttpTask
        implements Runnable {
    private Logger logger = Logger.getLogger(HttpTask.class);

    private String url;

    private String requestBody;
    private HttpTaskManager taskManager;

    public HttpTask() {
    }

    public HttpTask(String url, String requestBody) {
        this.url = url;
        this.requestBody = requestBody;
    }


    public void run() {
        try {
            String res = HttpUtils.sendPost(this.url, this.requestBody, "text/plain;charset=utf-8");
            this.taskManager.results.offer(res);
        } catch (Exception e) {
            this.logger.error("【请求停车场服务失败】:Exception" + e);
        }
    }


    public String getUrl() {
        return this.url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getRequestBody() {
        return this.requestBody;
    }


    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }


    public HttpTaskManager getTaskManager() {
        return this.taskManager;
    }


    public void setTaskManager(HttpTaskManager taskManager) {
        this.taskManager = taskManager;
    }
}



