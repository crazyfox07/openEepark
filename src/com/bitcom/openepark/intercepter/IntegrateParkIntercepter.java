package com.bitcom.openepark.intercepter;

import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class IntegrateParkIntercepter
        extends HandlerInterceptorAdapter {
    private Logger logger = Logger.getLogger(getClass());


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");


        String domainId = request.getParameter("domainId");
        String timeStamp = request.getParameter("timeStamp");
        String sign = request.getParameter("sign");
        String content = request.getParameter("content");
        if (domainId == null || timeStamp == null || sign == null || content == null) {
            PrintWriter out = new PrintWriter((OutputStream) response.getOutputStream());
            out.print("{\"result\":\":fail\",\"msg\":\"parameters incomplete.\"}");
            out.close();
            this.logger.error("【拦截异常】：parameters incomplete. IP:" + request.getRemoteAddr());
            return false;
        }

        return true;
    }
}



