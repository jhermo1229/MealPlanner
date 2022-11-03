package com.example.mealplanner;

public class AppUtils {

    public String EncodeBase64(String data) {
        String encodeData = android.util.Base64.encodeToString(data.getBytes(), android.util.Base64.DEFAULT);
        return encodeData;
    }

    public String DecodeBase64(String data) {
        byte[] decodedBytes = android.util.Base64.decode(data, android.util.Base64.DEFAULT);
        return new String(decodedBytes);
    }
}
