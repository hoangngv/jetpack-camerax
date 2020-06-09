package com.example.camerax_vsmart.Utils;

public class AppConstants {
    public class Common{
        public static final int COMMON_REQUEST_PERMISSION_CODE = 9999;
        public static final String IMG_NAME = "imgName";
    }

    public class CameraConfig{
        public static final int REQUEST_CAMERA_PERMISSION = 100;

        public static final int STATE_PREVIEW = 0;
        public static final int STATE_WAITING_LOCK = 1;
        public static final int STATE_WAITING_PRECAPTURE = 2;
        public static final int STATE_WAITING_NON_PRECAPTURE = 3;
        public static final int STATE_PICTURE_TAKEN = 4;

        public static final int MODE_FLASH_AUTO = 5;
        public static final int MODE_FLASH_OFF = 6;
        public static final int MODE_FLASH_ON = 7;
    }

    public class RatioFrame {
        public static final int RATIO_56x29 = 1;
        public static final int RATIO_76x30 = 2;
        public static final int RATIO_70x20 = 3;
        public static final int RATIO_0 = 0;
    }
}
