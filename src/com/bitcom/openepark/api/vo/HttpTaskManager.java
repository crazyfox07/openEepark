package com.bitcom.openepark.api.vo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class HttpTaskManager
        implements Runnable {
    private Logger logger = Logger.getLogger(getClass());

    public BlockingQueue<String> results = new ArrayBlockingQueue<>(200);
    private String[][] task = (String[][]) null;
    private int taskLength = 0;
    private long beginTimeMillis = 0L;


    public void run() {
        this.beginTimeMillis = System.currentTimeMillis();
        for (String[] entry : this.task) {
            if (entry[0] != null && !entry[0].equals("")) {
                HttpTask task = new HttpTask(entry[0], entry[1]);
                task.setTaskManager(this);
                (new Thread(task)).start();
            }
        }
    }

    public String[] getResult(long timeOut) {
        boolean continueFlag = true;
        while (continueFlag) {
            try {
                Thread.sleep(20L);
            } catch (InterruptedException interruptedException) {
            }


            long nowTimeMillis = System.currentTimeMillis();
            if (nowTimeMillis - this.beginTimeMillis > timeOut || this.taskLength == this.results.size()) {
                if (this.taskLength == 0) {
                    this.logger.info("HTTP no task,invalid request");
                }
                continueFlag = false;
            }
        }


        String[] res = new String[this.results.size()];
        this.results.toArray((Object[]) res);
        return res;
    }

    public void setTask(String[][] task) {
        for (String[] entry : task) {
            if (entry[0] != null && !entry[0].equals("")) {
                this.taskLength++;
            }
        }
        this.task = task;
    }
}



