package com.innext.szqb.util.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.innext.szqb.R
import com.innext.szqb.config.Constant
import com.innext.szqb.dialog.AlertFragmentDialog
import com.innext.szqb.dialog.VerifyCodeDialog
import com.innext.szqb.ui.main.WebViewActivity
import com.innext.szqb.ui.my.bean.CopyTextBean
import com.innext.szqb.util.ConvertUtil
import com.innext.szqb.util.view.RichTextUtil
import org.jetbrains.anko.startActivity

/**
 * @author      : HX0010239ANDROID
 * @date        : 2017/11/20 11:53
 * @description : 对话框工具类
 */
object DialogUtil {

    /**
     * 显示打电话对话框
     */
    fun showCallDialog(activity: FragmentActivity, oldTel: String) {
        val newTel1 = oldTel.replace("-", "").trim()
        AlertFragmentDialog.Builder(activity)
                .setTitle("温馨提示")
                .setContent(RichTextUtil.setCallDialogRichText(oldTel))
                .setLeftBtnText("取消")
                .setRightBtnText("确定")
                .setRightCallBack {
                    //跳转拨打客服电话页面
                    val uri = Uri.parse("tel:" + newTel1)
                    val intent = Intent(Intent.ACTION_DIAL, uri)
                    activity.startActivity(intent)
                }
                .setCancel(true)
                .build()
    }
        fun copy2Clipboard(context: Context?, text: String?) {
            if (context == null || text == null) {
                return
            }
            if (Build.VERSION.SDK_INT >= 11) {
                val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText(null, text)
                clipboardManager.primaryClip = clipData
                ToastUtil.showToast("复制到剪贴板")
            } else {
                val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
                clipboardManager.text = text
            }
        }
    /**
     * 获取 会员dialog
     */
    fun showVipDialog(activity: AppCompatActivity,url:String): AlertFragmentDialog =
        AlertFragmentDialog.Builder(activity)
                .setTitle("温馨提示")
                .setContent(" 恒快贷采用会员制，您将支付28元会员服务费；加入会员，您将享有：\n" +
                        "1. 无砍头息，年化36%以下利率;\n" +
                        "2. 会员借款利率折扣优惠;\n" +
                        "3. 会员费换积分可在积分抽奖使用;")
                .setLeftBtnText("再考虑下")
                .setLeftCallBack {  }
                .setRightBtnText("确认支付")
                .setRightCallBack {
                    //跳转绑卡、未绑卡界面
                    activity.startActivity<WebViewActivity>("url" to url,Constant.CACHE_TAG_UID to true,"type" to "get")
                }.setCancel(true)
                .build()

    /**
     * 显示验证码
     */
    fun showVerifyCodeDialog(context: Context, isCancel: Boolean, imagePath: String,
                             rightClickCallBack: VerifyCodeDialog.RightClickCallBack,
                             changeCodeClickCallBack: VerifyCodeDialog.ChangeCodeClickCallBack) {
        VerifyCodeDialog.Builder(context as FragmentActivity)
                .setCancel(isCancel)
                .setTitle(context.getString(R.string.verifycode_title))
                .setContent(context.getString(R.string.verifycode_content))
                .setImagePath(imagePath)
                .setLeftBtnText(context.getString(R.string.verifycode_leftBtnText))
                .setLeftCallBack { }
                .setRightBtnText(context.getString(R.string.verifycode_rightBtnText))
                .setRightCallBack(rightClickCallBack)
                .setChangeCodeCallBack(changeCodeClickCallBack)
                .build()
    }
}