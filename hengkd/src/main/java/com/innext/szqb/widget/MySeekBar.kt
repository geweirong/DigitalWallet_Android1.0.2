package com.innext.szqb.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.SeekBar

/**
 *  @author      : 楠GG
 *  @date        : 2017/11/28 21:53
 *  @description : 自定义seekbar方便设置是否可以触摸
 */
class MySeekBar: SeekBar {
    var isCanTouch = true

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isCanTouch) return false
        return super.onTouchEvent(event)
    }
}