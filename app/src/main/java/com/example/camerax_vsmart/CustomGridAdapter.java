package com.example.camerax_vsmart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.example.camerax_vsmart.Utils.DebugLog;
import com.example.camerax_vsmart.Utils.ImageOverlayView;
import com.example.camerax_vsmart.Utils.StylingOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stfalcon.frescoimageviewer.ImageViewer;

public class CustomGridAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mImages;
    private LayoutInflater mInflater;
    private ImageOverlayView mImageOverlayView;
    private StylingOptions mStyleOptions;

    CustomGridAdapter(Context context, String[] images) {
        this.mContext = context;
        this.mImages = images;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        mStyleOptions = new StylingOptions();
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_item, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.displayedImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        initDrawee(holder.imageView, position);

        return convertView;
    }

    static class ViewHolder {
        SimpleDraweeView imageView;
    }

    private void initDrawee(SimpleDraweeView drawee, final int startPosition) {
        drawee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugLog.d("[Custom Grid] " + startPosition + " draweeView clicked");
                showImage(startPosition);
            }
        });
        DebugLog.d("[Custom Grid] images array length: " + mImages.length);
        Glide.with(mContext).load(mImages[startPosition]).into(drawee);
        //drawee.setImageURI(mImages[startPosition]);
    }

    private void showImage(int startPosition) {
        ImageViewer.Builder builder = new ImageViewer.Builder<>(mContext, mImages)
                .setStartPosition(startPosition)
                .setOnDismissListener(getDismissListener());

        builder.hideStatusBar(mStyleOptions.get(StylingOptions.Property.HIDE_STATUS_BAR));
        builder.allowSwipeToDismiss(mStyleOptions.get(StylingOptions.Property.SWIPE_TO_DISMISS));
        builder.allowZooming(mStyleOptions.get(StylingOptions.Property.ZOOMING));

        if (mStyleOptions.get(StylingOptions.Property.SHOW_OVERLAY)) {
            mImageOverlayView = new ImageOverlayView(mContext);
            builder.setOverlayView(mImageOverlayView);
            builder.setImageChangeListener(getImageChangeListener());
        }

        builder.show();
    }

    private ImageViewer.OnImageChangeListener getImageChangeListener() {
        return new ImageViewer.OnImageChangeListener() {
            @Override
            public void onImageChange(int position) {
                // TODO
                DebugLog.d("[Custom Grid] ImageViewer.onImageChange...");
                String url = mImages[position];
                mImageOverlayView.setShareText(url);
            }
        };
    }

    private ImageViewer.OnDismissListener getDismissListener() {
        return new ImageViewer.OnDismissListener() {
            @Override
            public void onDismiss() {
                // TODO
                DebugLog.d("[Custom Grid] ImageViewer.onDismiss...");
            }
        };
    }
}
