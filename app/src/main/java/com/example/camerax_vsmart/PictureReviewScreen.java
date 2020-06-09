package com.example.camerax_vsmart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.camerax_vsmart.Utils.AppConstants;
import com.example.camerax_vsmart.Utils.AppUtils;
import com.example.camerax_vsmart.Utils.DebugLog;

import androidx.appcompat.app.AppCompatActivity;

public class PictureReviewScreen extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_review);
        initComponents();
        displayImage();
    }

    private void initComponents() {
        mImageView = findViewById(R.id.iv_preview_photo);
    }

    private void displayImage() {
        Intent intent = getIntent();
        String picName = intent.getStringExtra(AppConstants.Common.IMG_NAME);
        Bitmap bitmap = BitmapFactory.decodeFile(AppUtils.getFileDir(picName));

        DebugLog.d(AppUtils.getFileDir(picName));
        mImageView.setImageBitmap(bitmap);
        DebugLog.d("Displaying image");
    }
}