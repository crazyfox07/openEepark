package com.bitcom.openepark.api.vo;

public class RedisTask {
    private String taskId;
    private String postUrl;
    private String content;

    public String getTaskId() {
        return this.taskId;
    }


    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    public String getPostUrl() {
        return this.postUrl;
    }


    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }


    public String getContent() {
        return this.content;
    }


    public void setContent(String content) {
        this.content = content;
    }
}



