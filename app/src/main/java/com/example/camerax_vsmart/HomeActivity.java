package com.example.camerax_vsmart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnOpenCamera, btnOpenGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponents();
        initEvents();
    }

    private void initComponents() {
        btnOpenCamera = findViewById(R.id.btn_open_camera);
        btnOpenGallery = findViewById(R.id.btn_open_gallery);
    }

    private void initEvents() {
        btnOpenCamera.setOnClickListener(this);
        btnOpenGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_camera:
                switchToCameraActivity();
                break;
            case R.id.btn_open_gallery:
                switchToGallery();
                break;
        }
    }

    private void switchToGallery() {
        startActivity(new Intent(this, GalleryScreen.class));
    }

    private void switchToCameraActivity() {
        startActivity(new Intent(this, CameraActivity.class));
    }
}
