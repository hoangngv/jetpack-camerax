package com.example.camerax_vsmart.Utils;

import android.os.Environment;

import java.io.File;

public class AppUtils {
    public static String getFileDir(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory(), "CAMERA-VSMART");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                DebugLog.d("Can't create file path");
            }
        }
        return file.getAbsolutePath() + "/" + fileName;
    }
}
