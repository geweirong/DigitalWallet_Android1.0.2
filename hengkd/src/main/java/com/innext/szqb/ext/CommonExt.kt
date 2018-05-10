package com.innext.szqb.ext

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/**
 * Created by HX0010204NET on 2018/1/17.
 */

fun View.onClick(onClickListener: View.OnClickListener) {
    this.setOnClickListener(onClickListener)
}

fun View.onClick(block: () -> Unit) {
    this.setOnClickListener { block() }
}

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun Bitmap.realRecycle() {
    if (this.isRecycled.not()) this.recycle()
}

/**
 * 加载活动图片
 */
fun ImageView.loadActiveImage(url: String, width: Int, completeBlock: () -> Unit) {
    Glide.with(context)
            .load(url)
            .fitCenter()
            .override(width, (width * 1.25).toInt())//宽高比例
            .listener(object : RequestListener<String, GlideDrawable> {
                override fun onException(e: Exception, model: String, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                    completeBlock()
                    return false
                }
            })
            .into(this)
}
