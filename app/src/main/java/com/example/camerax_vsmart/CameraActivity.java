package com.example.camerax_vsmart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.LifecycleOwner;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camerax_vsmart.Utils.AppConstants;
import com.example.camerax_vsmart.Utils.AppUtils;
import com.example.camerax_vsmart.Utils.DebugLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DATE_FORMAT = "yyMMdd_kkmmss";
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    TextureView textureView;
    private ImageButton btnBack, btnCapture;
    private ImageView ivRectFrame;
    private TextView tvUserWarning;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initComponents();
        initEvents();

        if(allPermissionsGranted()){
            startCamera(); //start camera if permission has been granted by user
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void initComponents(){
        textureView = findViewById(R.id.tv_camera_preview);
        btnBack = findViewById(R.id.btn_back);
        btnCapture = findViewById(R.id.btn_capture);
        ivRectFrame = findViewById(R.id.iv_frame_camera);
        tvUserWarning = findViewById(R.id.tv_user_warning);
        //displayDynamicView(tvUserWarning, 100, 300);
    }

    private void initEvents(){
        btnBack.setOnClickListener(this);
    }

    private void startCamera() {

        CameraX.unbindAll();

        Rational aspectRatio = new Rational (textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight()); //size of the screen

        DebugLog.d("[Capture Photo]: (width, height) = ("+ screen.getWidth() + ", " + screen.getHeight() + ")");

        PreviewConfig pConfig = new PreviewConfig.Builder().setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();
        Preview preview = new Preview(pConfig);

        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
                    //to update the surface texture we have to destroy it first then re-add it
                    @Override
                    public void onUpdated(Preview.PreviewOutput output){
                        ViewGroup parent = (ViewGroup) textureView.getParent();
                        parent.removeView(textureView);
                        parent.addView(textureView, 0);

                        textureView.setSurfaceTexture(output.getSurfaceTexture());
                        updateTransform();
                    }
                });

        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder()
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();

        final ImageCapture imageCapture = new ImageCapture(imageCaptureConfig);

        btnCapture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DebugLog.d("[Capture Photo] Capture button pressed");
                Calendar calendar = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

                final String fileName = "IMG_" + dateFormat.format(calendar.getTime()) + ".jpg";
                final String filePath = AppUtils.getFileDir(fileName);
                File file = new File(filePath);

                imageCapture.takePicture(file, new ImageCapture.OnImageSavedListener() {

                    @Override
                    public void onImageSaved(@NonNull File file) {
                        DebugLog.d("[Capture Photo] Run into onImageSaved");
                        DebugLog.d("[Capture Photo] Image saved at: " + filePath);
                        String msg = "Photo saved at " + file.getAbsolutePath();
                        Toast.makeText(getBaseContext(), msg,Toast.LENGTH_LONG).show();

                        DebugLog.d("[Canvas drawing] File size before being compressed: " + file.length());

                        //writeTextOntoImage(filePath, "(Long: X, Lat: Y)", 120, Color.GREEN, 200, 200);
                        //reviewPicture(fileName);

                        DebugLog.d("[Capture Photo] Image is saved successfully");
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        String msg = "Pic capture failed : " + message;
                        Toast.makeText(getBaseContext(), msg,Toast.LENGTH_LONG).show();
                        if(cause != null){
                            cause.printStackTrace();
                        }
                    }
                });

                imageCapture.takePicture(new ImageCapture.OnImageCapturedListener() {
                    @Override
                    public void onCaptureSuccess(ImageProxy image, int rotationDegrees) {
                        super.onCaptureSuccess(image, rotationDegrees);
                        DebugLog.d("[Capture Photo] Capture Success");
                    }

                    @Override
                    public void onError(ImageCapture.UseCaseError useCaseError, String message, @Nullable Throwable cause) {
                        super.onError(useCaseError, message, cause);
                    }
                });
            }
        });

        //bind to lifecycle
        CameraX.bindToLifecycle((LifecycleOwner)this, preview, imageCapture);
    }

    private void reviewPicture(String filePath) {
        if (filePath != null) {
            DebugLog.d("[Capture Photo] Prepare to review photo");
            Intent intent = new Intent(this, GalleryScreen.class);
            intent.putExtra(AppConstants.Common.IMG_NAME, filePath);
            startActivity(intent);
            //finish();
        } else {
            Toast.makeText(this, R.string.unable_open_photo, Toast.LENGTH_SHORT).show();
        }
        //finish();
    }

    private void updateTransform(){
        Matrix mx = new Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        float cX = w / 2f;
        float cY = h / 2f;

        int rotationDgr;
        int rotation = (int)textureView.getRotation();

        switch(rotation){
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        mx.postRotate((float)rotationDgr, cX, cY);
        textureView.setTransform(mx);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startCamera();
            } else{
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    private void writeTextOntoImage(String imagePath, String text, int textSize, int textColor, int posX, int posY) {
        DebugLog.d("[Capture Photo] Writing text onto image");

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        try {
            bitmap = modifyOrientation(bitmap, imagePath);
            DebugLog.d("Modified orientation");
        } catch (IOException e) {
            DebugLog.d("Error in modifyOrientation: " + e);
        }

        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        bitmap.recycle();

        // overwrite
        File file = new File(imagePath);
        if (file.exists())
            file.delete();

        try {
            DebugLog.d("Run into Canvas");
            FileOutputStream out = new FileOutputStream(file);

            Canvas canvas = new Canvas(mutableBitmap);
            Paint paint = new Paint();
            paint.setColor(textColor);
            paint.setTextSize(textSize);
            paint.setStyle(Paint.Style.FILL);

            canvas.drawBitmap(mutableBitmap, 0, 0, paint);
            canvas.drawText(text, posX, posY, paint);

            // compress image
            int MAX_IMAGE_SIZE = 1000 * 1024;
            int streamLength = MAX_IMAGE_SIZE;
            int compressQuality = 100;
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();

            while (streamLength >= MAX_IMAGE_SIZE && compressQuality > 60) {

                try {
                    bmpStream.flush(); //to avoid out of memory error
                    bmpStream.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                compressQuality -= 10;
                mutableBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);

                streamLength = bmpStream.size();

                DebugLog.d("Compressed Image Size: " + streamLength);
            }

            DebugLog.d("[Canvas drawing] File size after being compressed: " + bmpStream.size());

            out.write(bmpStream.toByteArray());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Bitmap modifyOrientation(Bitmap bitmap, String imageAbsolutePath) throws IOException {
        ExifInterface ei = new ExifInterface(imageAbsolutePath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        DebugLog.d("Orientation identified: " + orientation);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    private static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            DebugLog.d("notification pushed up");
            hiddenStatusBar();
        } else {
            DebugLog.d("notification pulled down");
        }
        super.onWindowFocusChanged(hasFocus);
    }

    private void hiddenStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onClick(View v) {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // display text view at specified location
    private void displayDynamicView(View view, float x, float y){
        view.setX(x);
        view.setY(y);
    }
}