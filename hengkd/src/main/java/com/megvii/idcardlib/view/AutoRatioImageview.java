package com.megvii.idcardlib.view;


import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by binghezhouke on 14-1-2.
 */
public class AutoRatioImageview extends android.support.v7.widget.AppCompatImageView {
    private float mRatio = -1;
    private int mPrefer = 0;

    public AutoRatioImageview(Context context) {
        super(context);
    }

    public AutoRatioImageview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoRatioImageview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (mRatio < 0) {
            //this case means the ration is auto ratio
            if (getDrawable() == null) {
                //no image settled, invoke super onMeasure
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else {

                int drawableWidth = getDrawable().getIntrinsicWidth();
                int drawableHeight = getDrawable().getIntrinsicHeight();
                if (mPrefer == 0) {
                    // consider width

                    setMeasuredDimension(viewWidth,
                            viewWidth * drawableHeight / drawableWidth);
                } else {
                    setMeasuredDimension(viewHeight * drawableWidth / drawableHeight, viewHeight);
                }
            }
        } else {
            // this view is fixed ratio
            if (mPrefer == 0) {
                // consider view width
                setMeasuredDimension(viewWidth,
                        (int) (viewWidth * mRatio));
            } else {
                setMeasuredDimension((int) (viewHeight / mRatio), viewWidth);
            }
        }

    }
}
