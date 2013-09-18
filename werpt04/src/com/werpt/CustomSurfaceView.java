package com.werpt;

import com.werpt.util.ServiceData;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class CustomSurfaceView extends SurfaceView {
	
	private Context context;

	public CustomSurfaceView(Context context) {
        super(context);
        this.context = context;
    }
    
    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	int width = ServiceData.getScreenWidthOrHeight("width", this.context);
    	int height = ServiceData.getScreenWidthOrHeight("height", this.context);
    	setMeasuredDimension(width, height);
    }

}
