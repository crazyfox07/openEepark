package com.bitcom.openepark.util;

import com.bitcom.openepark.common.util.PropertiesUtil;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;


public class FileUploadUtil {
    public static Map<String, Date> uploadMultiFiles(MultipartRequest request, String path) throws Exception {
        Map<String, Date> resMap = new HashMap<>();
        String savePath = PropertiesUtil.getProperty("pic_upload_path") + path + "/";
        File dirFile = new File(savePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        String fileName = "";
        Map<String, MultipartFile> fileMap = request.getFileMap();
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            MultipartFile mFile = entry.getValue();
            if (mFile.isEmpty()) {
                continue;
            }
            fileName = mFile.getOriginalFilename();
            mFile.transferTo(new File(savePath + fileName));
            resMap.put(path + "/" + fileName, new Date());
        }
        return resMap;
    }
}



