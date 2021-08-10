package com.bitcom.openepark.common.util;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


public class PropertiesUtil {
    private static Logger log = Logger.getLogger(PropertiesUtil.class);

    private static Properties properties;

    static {
        InputStream input = PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties");
        properties = new Properties();
        try {
            properties.load(input);
        } catch (Exception e) {
            log.error("加载配置文件出错", e);
            e.printStackTrace();
        }
    }


    public static String getProperty(String key) {
        if (properties != null) {
            return properties.getProperty(key).trim();
        }
        return null;
    }
}



