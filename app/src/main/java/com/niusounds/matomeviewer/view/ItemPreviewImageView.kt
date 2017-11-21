package com.niusounds.matomeviewer.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

import com.android.volley.toolbox.NetworkImageView

class ItemPreviewImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : NetworkImageView(context, attrs, defStyle) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec) * 3 / 4, View.MeasureSpec.EXACTLY))
    }
}
