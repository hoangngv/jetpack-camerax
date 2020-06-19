package com.example.camerax_vsmart;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.camerax_vsmart.Utils.AppUtils;
import com.example.camerax_vsmart.Utils.DebugLog;
import com.example.camerax_vsmart.Utils.ImageOverlayView;
import com.example.camerax_vsmart.Utils.StylingOptions;

import androidx.appcompat.app.AppCompatActivity;

public class GalleryScreen extends AppCompatActivity {

    private static final int[] ids = new int[]{
            R.id.firstImage, R.id.secondImage,
            R.id.thirdImage, R.id.fourthImage,
            R.id.fifthImage, R.id.sixthImage,
            R.id.seventhImage, R.id.eighthImage,
            R.id.ninethImage
    };

    protected String[] images;

    private ImageOverlayView overlayView;
    private StylingOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        DebugLog.d("[Capture Photo] Review photo activity launched");
        images = AppUtils.getLocalImageUri();
        DebugLog.d("[Custom Grid] images array length: " + images.length);
        final GridView gridView = (GridView) findViewById(R.id.gridView);
        CustomGridAdapter adapter = new CustomGridAdapter(this, images);
        gridView.setAdapter(adapter);
    }

//    protected void showPicker(int startPosition) {
//        ImageViewer.Builder builder = new ImageViewer.Builder<>(this, images)
//                .setStartPosition(startPosition)
//                .setOnDismissListener(getDismissListener());
//
//        builder.hideStatusBar(options.get(StylingOptions.Property.HIDE_STATUS_BAR));
//        builder.allowSwipeToDismiss(options.get(StylingOptions.Property.SWIPE_TO_DISMISS));
//        builder.allowZooming(options.get(StylingOptions.Property.ZOOMING));
//
//        if (options.get(StylingOptions.Property.SHOW_OVERLAY)) {
//            overlayView = new ImageOverlayView(this);
//            builder.setOverlayView(overlayView);
//            builder.setImageChangeListener(getImageChangeListener());
//        }
//
//        builder.show();
//    }
//
//    private ImageViewer.OnImageChangeListener getImageChangeListener() {
//        return new ImageViewer.OnImageChangeListener() {
//            @Override
//            public void onImageChange(int position) {
//                // TODO
//                DebugLog.d("onImageChange");
//                String url = images[position];
//                overlayView.setShareText(url);
//            }
//        };
//    }
//
//    private ImageViewer.OnDismissListener getDismissListener() {
//        return new ImageViewer.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                // TODO
//                DebugLog.d("onDismiss");
//            }
//        };
//    }
//
//    private void initView() {
//        options = new StylingOptions();
//
//        images = AppUtils.getLocalImageUri();
//        int imgQuantity = images.length;
//
//        for (int i = 0; i < (imgQuantity < 9 ? imgQuantity : 9); i++) {
//            SimpleDraweeView drawee = (SimpleDraweeView) findViewById(ids[i]);
//            initDrawee(drawee, i);
//        }
//    }
//
//    private void initDrawee(SimpleDraweeView drawee, final int startPosition) {
//        drawee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPicker(startPosition);
//            }
//        });
//        drawee.setImageURI(images[startPosition]);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}