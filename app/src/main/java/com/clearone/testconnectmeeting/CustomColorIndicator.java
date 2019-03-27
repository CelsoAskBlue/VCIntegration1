package com.clearone.testconnectmeeting;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by laura on 28/04/2017.
 */

public class CustomColorIndicator extends FrameLayout implements View.OnClickListener{
    public interface OnCustomColorIndicatorListener
    {
        public void onColorSelected(View v, int color);
    }

    int _tint = 0xff00ff00;
    OnCustomColorIndicatorListener _listener;

    public CustomColorIndicator(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomColorIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadAttributes(context, attrs);
        init(context);
    }

    public CustomColorIndicator(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttributes(context, attrs);
        init(context);
    }
    @TargetApi(21)
    public CustomColorIndicator(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadAttributes(context, attrs);
        init(context);
    }
    void init(Context context)
    {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_custom_color_indicator, this);
        this.setOnClickListener(this);
        ImageView v = (ImageView) findViewById(R.id.color_indicator_inner_circle);
        v.setColorFilter(_tint);
    }

    private void loadAttributes( Context context, AttributeSet attrs)
    {
        TypedArray attributeArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomColorIndicator);
        _tint = attributeArray.getColor(R.styleable.CustomColorIndicator_inner_tint, 0xffffff);
        attributeArray.recycle();
    }

    public void setOnCustomColorIndicatorListener(OnCustomColorIndicatorListener listener)
    {
        _listener = listener;
    }

    public int getInnerTintColor()
    {
        return _tint;
    }

    @Override
    public void onClick(View v) {
        if(_listener != null)
            _listener.onColorSelected(this, _tint);
    }
}
