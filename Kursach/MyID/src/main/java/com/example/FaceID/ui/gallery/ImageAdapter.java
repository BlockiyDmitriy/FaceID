package com.example.FaceID.ui.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.FaceID.R;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    public Integer[] mThumbIds = { R.drawable.pict1, R.drawable.pict2,
            R.drawable.pict3, R.drawable.pict4, R.drawable.pict5,
            R.drawable.pict6, R.drawable.pict7, R.drawable.pict2,
            R.drawable.pict3, R.drawable.pict4, R.drawable.pict5,
            R.drawable.pict6, R.drawable.pict2,
            R.drawable.pict3, R.drawable.pict4, R.drawable.pict5,
            R.drawable.pict6, R.drawable.pict2,
            R.drawable.pict3, R.drawable.pict4, R.drawable.pict5,
            R.drawable.pict6, };


    public ImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length; // длина массива
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, 300 ));

        return imageView;
    }
}
