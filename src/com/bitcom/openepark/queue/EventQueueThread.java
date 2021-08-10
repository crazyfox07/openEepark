package com.bitcom.openepark.queue;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.openepark.api.vo.RedisTask;
import com.bitcom.openepark.util.HttpUtils;
import org.apache.log4j.Logger;


public class EventQueueThread
        extends Thread {
    public static Logger logger = Logger.getLogger(EventQueueThread.class);

    private RedisQueue queue;

    private volatile boolean stop = false;

    public void init() {
        (new Thread(this)).start();
    }


    public void close() {
        System.out.println("***************Redis Queue is Stopping***************");
        this.stop = true;
        this.queue.close();
        interrupt();
        System.out.println("***************Redis Queue has Stopped***************");
    }


    public void run() {
        long currTime = System.currentTimeMillis();
        while (!this.stop) {
            String taskStr = this.queue.take();
            if (taskStr == null) {
                continue;
            }
            RedisTask task = (RedisTask) JSONObject.parseObject(taskStr, RedisTask.class);

            String postUrl = task.getPostUrl();
            String content = task.getContent();
            String res = "";
            try {
                res = HttpUtils.sendPost(postUrl, content, "text/plain;charset=utf-8");
                logger.info("【Task Send to Epark】#" + currTime + "# postUrl：" + postUrl + "taskID=" + task.getTaskId() + " retVal=" + res);
                if (res.contains("success")) {
                    this.queue.success(task);
                    continue;
                }
                logger.info("【Task Send to Epark】#" + currTime + "# task fail. taskId=" + task.getTaskId());
                this.queue.fail(task);
                if (logger.isDebugEnabled()) {
                    logger.debug("【take from queue】 task post to fail queue complete.");
                }
            } catch (Exception e) {
                logger.error("【Task Send to Epark】#" + currTime + "# task fail. epark Thread encounter a problem...." + e.getMessage());
                e.printStackTrace();

                try {
                    logger.error("【Task Send to Epark】waitting begin");
                    Thread.sleep(40000L);
                    this.queue.fail(task);
                    logger.error("【Task Send to Epark】waitting end");
                } catch (InterruptedException interruptedException) {
                }
            }
        }
    }


    public RedisQueue getQueue() {
        return this.queue;
    }


    public void setQueue(RedisQueue queue) {
        this.queue = queue;
    }


    public boolean isStop() {
        return this.stop;
    }


    public void setStop(boolean stop) {
        this.stop = stop;
    }
}



