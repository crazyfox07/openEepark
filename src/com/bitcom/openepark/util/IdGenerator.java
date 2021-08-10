package com.bitcom.openepark.util;

import java.util.UUID;


public class IdGenerator {
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}



