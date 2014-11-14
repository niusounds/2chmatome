package com.niusounds.matomeviewer.view;

import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

public class ItemPreviewImageView extends NetworkImageView {
    public ItemPreviewImageView(Context context) {
        super(context);
    }

    public ItemPreviewImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemPreviewImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) * 3 / 4, MeasureSpec.EXACTLY));
    }
}
