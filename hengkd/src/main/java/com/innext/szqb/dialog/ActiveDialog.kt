package com.innext.szqb.dialog

import android.app.Activity
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.View
import com.innext.szqb.R
import com.innext.szqb.ext.loadActiveImage
import com.innext.szqb.ext.onClick
import com.innext.szqb.ui.main.WebViewActivity
import kotlinx.android.synthetic.main.dialog_active.view.*
import org.jetbrains.anko.startActivity

/**
 * @author      : yan
 * @date        : 2018/1/31 15:57
 * @description : 活动dialog，不需要show，在图片加载完成后自动show
 */
class ActiveDialog @JvmOverloads constructor(
        val activity: Activity, private val imageUrl: String?, private val linkedUrl: String?, themeResId: Int = R.style.activity_dialog
) : AlertDialog(activity, themeResId) {

    private val customerView: View = layoutInflater.inflate(R.layout.dialog_active, null)

    init {
        setView(customerView)
        initDialog()
        initListener()
    }

    private fun initDialog() {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        window.setLayout((dm.widthPixels * 0.8).toInt(), window.attributes.height)
    }

    private fun initListener() {
        if (imageUrl == null || linkedUrl == null) return
        customerView.apply {
            iv_content.onClick {
                activity.startActivity<WebViewActivity>("url" to linkedUrl)
                dismiss()
            }
            iv_close.onClick { dismiss() }
            iv_content.loadActiveImage(imageUrl, window.attributes.width) {
                show()
            }
        }
    }
}