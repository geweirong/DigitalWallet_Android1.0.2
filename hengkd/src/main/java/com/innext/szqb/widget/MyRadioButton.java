package com.innext.szqb.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.innext.szqb.R;

public class MyRadioButton extends android.support.v7.widget.AppCompatRadioButton {

	private double mDrawableSize;// xml文件中设置的大小

    int drawableHeight = 0;
    int drawableWidth = 0;
    public MyRadioButton(Context context) {
        this(context, null, 0);  
    }  
  
    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);  
    }  
  
    public MyRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);  
        Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyRadioButton);
  
        int n = a.getIndexCount();  
        for (int i = 0; i < n; i++) {  
            int attr = a.getIndex(i);

            switch (attr) {
            case R.styleable.MyRadioButton_rbDrawableSize:
                mDrawableSize = a.getDimensionPixelSize(R.styleable.MyRadioButton_rbDrawableSize, 0);
                break;
            case R.styleable.MyRadioButton_rbDrawableTop:
                drawableTop = a.getDrawable(attr);
                break;  
            case R.styleable.MyRadioButton_rbDrawableBottom:
                drawableRight = a.getDrawable(attr);  
                break;  
            case R.styleable.MyRadioButton_rbDrawableRight:
                drawableBottom = a.getDrawable(attr);  
                break;  
            case R.styleable.MyRadioButton_rbDrawableLeft:
                drawableLeft = a.getDrawable(attr);  
                break;  
            default :  
                    break;  
            }  
        }  
        a.recycle();  
          
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);  
  
    }  
  
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left,
                                                        Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            getDrawableSize(left);
            left.setBounds(0, 0, drawableWidth, drawableHeight);
        }  
        if (right != null) {
            getDrawableSize(right);
            right.setBounds(0, 0, drawableWidth, drawableHeight);
        }  
        if (top != null) {
            getDrawableSize(top);
            top.setBounds(0, 0, drawableWidth, drawableHeight);
        }  
        if (bottom != null) {
            getDrawableSize(bottom);
            bottom.setBounds(0, 0, drawableWidth, drawableHeight);
        }  
        setCompoundDrawables(left, top, right, bottom);  
    }
    private void getDrawableSize(Drawable drawable){
        double height = drawable.getIntrinsicHeight();
        double width = drawable.getIntrinsicWidth();
        if(mDrawableSize == 0){
            drawableHeight = (int)height;
            drawableWidth = (int)width;
        }else{
            if( height >= width ){
                double offset = height/mDrawableSize;
                drawableHeight = (int)mDrawableSize;
                drawableWidth = (int)(width/offset);
            }else{
                double offset = width/mDrawableSize;
                drawableWidth = (int)mDrawableSize;
                drawableHeight = (int)(height/offset);
            }
        }
    }

}
