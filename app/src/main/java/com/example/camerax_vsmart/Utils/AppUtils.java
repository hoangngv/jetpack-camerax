package com.example.camerax_vsmart.Utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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

    private static final String VSMART_DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/CAMERA-VSMART";

    public static String[] getLocalImageUri() {
        ArrayList<String> imagePaths = new ArrayList<String>();
        File[] files = new File(VSMART_DIRECTORY).listFiles();
        for (File file : files) {
            if (file.isFile()){
                imagePaths.add("file://" + file.getAbsolutePath());
                DebugLog.d("AppUtils: " + "file://" + file.getAbsolutePath());
            }
        }
        String[] sortedPaths = imagePaths.toArray(new String[imagePaths.size()]);
        Arrays.sort(sortedPaths, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
        return sortedPaths;
    }
}
