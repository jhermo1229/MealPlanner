package com.foodies.mealplanner.util;

public class AppUtils {

    public String encodeBase64(String data) {
        String encodeData = android.util.Base64.encodeToString(data.getBytes(), android.util.Base64.DEFAULT);
        return encodeData;
    }

    public String decodeBase64(String data) {
        byte[] decodedBytes = android.util.Base64.decode(data, android.util.Base64.DEFAULT);
        return new String(decodedBytes);
    }
}
