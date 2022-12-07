package com.foodies.mealplanner.util;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Utility for base 64 and date
 * @author herje
 * @version 1
 */
public class AppUtils {

    public String encodeBase64(String data) {
        String encodeData = android.util.Base64.encodeToString(data.getBytes(), android.util.Base64.DEFAULT);
        return encodeData;
    }

    public String decodeBase64(String data) {
        Log.d("DECODE", data);
        byte[] decodedBytes = android.util.Base64.decode(data, android.util.Base64.DEFAULT);
        return new String(decodedBytes);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String dateFormatter(LocalDate date){

        String formattedDate = date.format(DateTimeFormatter
                .ofLocalizedDate(FormatStyle.MEDIUM));

        return formattedDate;
    }
}
