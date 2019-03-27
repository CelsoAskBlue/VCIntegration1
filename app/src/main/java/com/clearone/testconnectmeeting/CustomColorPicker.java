package com.clearone.testconnectmeeting;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by laura on 27/04/2017.
 */

public class CustomColorPicker extends LinearLayout implements  CustomColorIndicator.OnCustomColorIndicatorListener {

    CustomColorIndicator.OnCustomColorIndicatorListener _listener;
    View _lastSelected = null;
    public CustomColorPicker(Context context) {
        super(context);
    }

    public CustomColorPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomColorPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public CustomColorPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    void init(Context context)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        inflater.inflate(R.layout.layout_custom_color_picker, this);
        setAsChildrenListener();
        int numColors = getNumColors();
        double d = Math.random();
        int selectedChildColor = (int)Math.round( (numColors-1)*d);
        CustomColorIndicator v = findColorSelector(selectedChildColor);
        if(v != null)
        {
            _lastSelected = v;
            v.setSelected(true);
            onColorSelected(v, v.getInnerTintColor());
        }
    }

    public void setOnCustomColorIndicatorListener(CustomColorIndicator.OnCustomColorIndicatorListener listener)
    {
        _listener = listener;
    }

    void setAsChildrenListener()
    {
        int nChildren = getChildCount();
        for(int i = 0; i < nChildren; i++)
        {
            View child = getChildAt(i);
            if(child != null && child instanceof  CustomColorIndicator)
            {
                ((CustomColorIndicator)child).setOnCustomColorIndicatorListener(this);
            }
        }
    }

    int getNumColors()
    {
        int res = 0;
        int nChildren = getChildCount();
        for(int i = 0; i < nChildren; i++)
        {
            View child = getChildAt(i);
            if(child != null && child instanceof  CustomColorIndicator)
            {
                res++;
            }
        }
        return res;
    }

    CustomColorIndicator findColorSelector(int pos)
    {
        CustomColorIndicator res = null;
        int nChildren = getChildCount();
        int colorCount = 0;
        for(int i = 0; i < nChildren; i++)
        {
            View child = getChildAt(i);
            if(child != null && child instanceof  CustomColorIndicator)
            {
                if(colorCount == pos) {
                    res = (CustomColorIndicator) child;
                    break;
                }
                colorCount ++;
            }
        }
        return res;
    }

    public int getColorSelected()
    {
        int res = Color.argb(255, 0,255,0);
        if(_lastSelected != null)
            res = ((CustomColorIndicator)_lastSelected).getInnerTintColor();
        return res;
    }

    @Override
    public void onColorSelected(View v, int color) {
        CustomColorIndicator indicator = (CustomColorIndicator)v;
        if(_listener != null)
            _listener.onColorSelected(v, color);
        if(_lastSelected != null)
            _lastSelected.setSelected(false);
        _lastSelected = v;
        v.setSelected(true);
    }
}
