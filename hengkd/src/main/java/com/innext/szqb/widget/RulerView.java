package com.innext.szqb.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * 刻度尺
 *
 */
public class RulerView extends View {

    private int defaultLineHeight = 10; //竖线高度   单位dp

    private int numberLineHeight = 16;  //带文字竖线高度 单位dp

    private int intervalLine = 5;  //间隔多少根线显示数字

    private int centerLineHeight = 40;  //中心线高度 单位dp

    private int centerNumber;     //中心数字

    private float lineWidth = 0.5f;     //竖线宽度  单位dp
    private Paint linePaint;    //刻度线画笔
    private Paint textPaint;    //文字画笔
    private Paint centerLinePaint;  //中心线画笔
    private String centerColor = "#004192"; //中心线颜色
    private float centerWidth = 0.5f;   //中心线宽度  单位dp
    private int interval = 10; //间隔距离
    private int intervalNumber = 20; //间隔数字
    private String lineColor = "#aaaab9";   //线颜色
    private int textSize = 9;   //文字大小 单位sp
    private String textColor = "#5f5f5f";   //文字颜色

    private int centerLineX;

    private int baseLineY;//底线位置

    private Rect textRect;//文字位置，用于使文字左右居中线显示

    private int offset; //偏移量 所有的滚动全靠这个值

    private int minNumber = 0;       //最小滚动值
    private int maxNumber = 1000;       //最大滚动值
    private int maxRuler = (maxNumber - minNumber) / intervalNumber; //总刻度数
    private GestureDetector mDetector;  //手势对象

    private Scroller mScroller;     //滚动对象

    private Rect mRect;     //所画刻度的总宽高

    private boolean isUpAndEnd;//松开并滚动完毕 用于判断抛后停止时使刻度滚动到线的位置

    private ScrollListening mScrollListening;   //回调

    private int oldScrollX; //用于抛后计算滚动值的

    private int oldCenterNumber;    //上一个值

    public RulerView(Context context) {
        this(context, null);
    }

    public RulerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        interval = dpToPx(getContext(), interval);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.parseColor(lineColor));
        linePaint.setStrokeWidth(dpToPx(getContext(), lineWidth));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor(textColor));
        textPaint.setTextSize(spToPx(getContext(), textSize));

        centerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerLinePaint.setColor(Color.parseColor(centerColor));
        centerLinePaint.setStrokeWidth(dpToPx(getContext(), centerWidth));

        textRect = new Rect();
        mRect = new Rect();
        mScroller = new Scroller(getContext());
        mDetector = new GestureDetector(getContext(), mListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, mRect.bottom, getWidth(), mRect.bottom, linePaint);//底线
        for (int i = 0; i <= maxRuler; i++) {   //画所有的线
            String text = (minNumber + intervalNumber * i) + "";//当前线的数字值
            if (Integer.parseInt(text) % (intervalLine*intervalNumber) == 0) {       //值是否为整百，整百则画数字
                canvas.drawLine(interval * i + offset, baseLineY, interval * i + offset, baseLineY - dpToPx(getContext(), numberLineHeight), linePaint);
                textPaint.getTextBounds(text, 0, text.length(), textRect);
                canvas.drawText(text, interval * i - textRect.width() / 2 + offset, baseLineY - dpToPx(getContext(), numberLineHeight) - textRect.height(), textPaint);
            } else {
                canvas.drawLine(interval * i + offset, baseLineY, interval * i + offset, baseLineY - dpToPx(getContext(), defaultLineHeight), linePaint);
            }
        }
        canvas.drawLine(centerLineX, baseLineY - dpToPx(getContext(), centerLineHeight), centerLineX, baseLineY, centerLinePaint);//中心线

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerLineX = getWidth() / 2;
//        offset = centerLineX;
        maxRuler = (maxNumber - minNumber) / intervalNumber;
        centerNumber = maxNumber;
        offset = centerLineX-maxRuler*interval;
        centerNumber = minNumber;
        baseLineY = getHeight()-dpToPx(getContext(), lineWidth);
        mRect.set(left, baseLineY - dpToPx(getContext(), numberLineHeight), maxRuler * interval, baseLineY);
    }

//    /**
//     * 滚动到输入的数字
//     *
//     * @param number
//     */
//    public void scrollToNumber(int number) {
//        if (centerNumber==number){
//            return;
//        }
//        if (number < minNumber) {
//            number = minNumber;
//        } else if (number > maxNumber) {
//            number = maxNumber;
//        }
//        int offsetX;
//        if (number>centerNumber){
//            offsetX = (number - centerNumber) / intervalNumber * interval;
//        }else{
//            offsetX = -(centerNumber-number) / intervalNumber * interval;
//        }
//
//        mScroller.startScroll(mScroller.getStartX(), 0, mScroller.getStartX() - offsetX,
//                0, 500);
//        postInvalidate();
//
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        if (!mScroller.computeScrollOffset()&& event.getAction() == MotionEvent.ACTION_UP) {//当松开并且没有滚动时
            //scroller();
            scrollToHundred();
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {//滚动进行中
            if (oldScrollX == 0) {
                oldScrollX = mScroller.getStartX();
            }
            offset += mScroller.getCurrX() - oldScrollX;
            oldScrollX = mScroller.getCurrX();
            if (offset > centerLineX) {
                offset = centerLineX;
            } else if (interval * maxRuler + offset < centerLineX) {
                offset = centerLineX - interval * maxRuler;
            }
            invalidate();
        } else {
            oldScrollX = 0;
            if (isUpAndEnd) {//是抛动作完成的
                scrollToHundred();
                //scroller();
            }

        }
        centerNumber = minNumber + Math.round((centerLineX - offset) / interval) * intervalNumber;
        if (oldCenterNumber != centerNumber) {
            if (mScrollListening != null) {
                boolean isHundred;
                if (centerNumber % (intervalNumber * intervalLine) == 0
                        ||centerNumber == minNumber
                        || centerNumber == maxNumber) {
                    isHundred = true;
                }  else {
                    isHundred = false;
                }
                mScrollListening.onCurrentNumber(centerNumber + "", isHundred);
            }
            oldCenterNumber = centerNumber;
        }
        super.computeScroll();
    }
    GestureDetector.OnGestureListener mListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mScroller.computeScrollOffset()){
               mScroller.abortAnimation();
            }
            offset += -(int) distanceX;
            if (offset > centerLineX) {
                offset = centerLineX;
            } else if (interval * maxRuler + offset < centerLineX) {
                offset = centerLineX - interval * maxRuler;
            }
            invalidate();
            return false;
        }

        @Override


        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (centerNumber==minNumber&&velocityX>0){
                return false;
            }else if (centerNumber==maxNumber&&velocityX<0){
                return false;
            }
            if (offset < centerLineX && interval * maxRuler + offset > centerLineX) {
                isUpAndEnd = true;
                mScroller.fling(0, 0, (int) velocityX, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
                invalidate();
            }
            return false;
        }
    };
    /**
     * 滚动到线的位置
     */
    private void scroller() {
        int upOffset = (offset - centerLineX) % interval;
        if (upOffset != 0) {
            isUpAndEnd = false;
            if (upOffset > interval / 2) {
                mScroller.startScroll(mScroller.getStartX(), 0, mScroller.getStartX() + interval - upOffset,
                        0, 500);
            } else {
                mScroller.startScroll(mScroller.getStartX(), 0, mScroller.getStartX() - upOffset,
                        0, 500);
            }
            invalidate();
        }
    }
    /**
     * 滚动到整百数字位
     *
     * @param
     */
    public void scrollToHundred() {
        int offsetX = 0;
        int upOffset = (centerLineX - offset) % interval; //两根线之间的偏移量。
        centerNumber = minNumber + Math.round((centerLineX - offset) / interval) * intervalNumber; //当前中心线显示的数字
        int remainder = centerNumber % (intervalLine*intervalNumber); //余数
        int number; //需要滚动到的位置
        if (remainder < 60) {
            offsetX += -upOffset;
            number = centerNumber - remainder;
        } else {
            offsetX -= upOffset;
            number = centerNumber + (intervalLine*intervalNumber - remainder);

        }
        if (number < minNumber) {
            number = minNumber;
        } else if (number > maxNumber) {
            number = maxNumber;
        }
        offsetX += (number - centerNumber) / intervalNumber * interval;
        if (offsetX != 0) {
            isUpAndEnd = false;
            mScroller.startScroll(mScroller.getStartX(), 0, mScroller.getStartX() - offsetX,
                    0, 500);
            invalidate();
        }
    }


    public void setOnScrollListening(ScrollListening listening) {
        mScrollListening = listening;
    }

    public int dpToPx(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale * dp + 0.5f);
    }

    public int spToPx(Context context, int sp) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;

        return (int) (sp * scale + 0.5f);
    }

    public interface ScrollListening {
        /**
         * @param number    表示当前的数字
         * @param isHundred 是否为整百或最大最小值
         */
        void onCurrentNumber(String number, boolean isHundred);
    }

    public int getMinNumber() {
        return minNumber;
    }

    public void setMinNumber(int minNumber) {
        if (this.minNumber == minNumber){
            return;
        }
        this.minNumber = minNumber;
        maxRuler = (maxNumber - minNumber) / intervalNumber;
        centerNumber = minNumber;
        offset = centerLineX;
        invalidate();
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        if (this.maxNumber ==maxNumber){
            return;
        }
        maxRuler = (maxNumber - minNumber) / intervalNumber;
        this.maxNumber = maxNumber;
        centerNumber = maxNumber;
        offset = centerLineX-maxRuler*interval;
        invalidate();
    }

    public int getMaxRuler() {
        return maxRuler;
    }

    public int getIntervalNumber() {
        return intervalNumber;
    }

    public void setIntervalNumber(int intervalNumber) {
        this.intervalNumber = intervalNumber;
        maxRuler = (maxNumber - minNumber) / intervalNumber;
        scrollToHundred();
    }
}
