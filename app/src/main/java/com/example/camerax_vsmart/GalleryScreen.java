package com.example.camerax_vsmart;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.camerax_vsmart.Utils.AppUtils;
import com.example.camerax_vsmart.Utils.DebugLog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stfalcon.frescoimageviewer.ImageViewer;

import androidx.appcompat.app.AppCompatActivity;

public class GalleryScreen extends AppCompatActivity {

    private static final int[] ids = new int[]{
            R.id.firstImage, R.id.secondImage,
            R.id.thirdImage, R.id.fourthImage,
            R.id.fifthImage, R.id.sixthImage,
            R.id.seventhImage, R.id.eighthImage,
            R.id.ninethImage
    };

    protected String[] posters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        DebugLog.d("[Capture Photo] Review photo activity launched");
        initView();
    }

    protected void showPicker(int startPosition) {
        new ImageViewer.Builder<>(this, posters)
                .setStartPosition(startPosition)
                .show();
    }

    private void initView() {
        posters = AppUtils.getLocalImageUri();
        int imgQuantity = posters.length;
        for (int i = 0; i < imgQuantity; i++) {
            Log.d("POSTERS", posters[i]);
        }

        for (int i = 0; i < (imgQuantity < 9 ? imgQuantity : 9); i++) {
            SimpleDraweeView drawee = (SimpleDraweeView) findViewById(ids[i]);
            initDrawee(drawee, i);
        }
    }

    private void initDrawee(SimpleDraweeView drawee, final int startPosition) {
        drawee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker(startPosition);
            }
        });
        drawee.setImageURI(posters[startPosition]);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}