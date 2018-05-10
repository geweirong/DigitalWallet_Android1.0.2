package com.innext.szqb.util.common

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import android.widget.TextView
import com.innext.szqb.config.Constant
import com.innext.szqb.util.Tool
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.io.IOException
import java.util.*

/**
 * @author      : HX0010239ANDROID
 * @date        : 2017/11/23 11:11
 * @description : 常用工具类
 */
object CommonUtil {

    /**
     * 设置textview的text、顶部image
     */
    fun setTextAndTopImage(context: Context, tv: TextView, text: String?, imgId: Int) {
        tv.text = text
        val drawable = context.resources.getDrawable(imgId)
        //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        tv.setCompoundDrawables(null, drawable, null, null)
    }

    /**
     * 安装apk
     */
    fun installApk(context: Context, apkPath: String?) {
        if (apkPath.isNullOrEmpty()) return

        val file = File(apkPath)
        val intent = Intent(Intent.ACTION_VIEW)

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            val apkUri = FileProvider.getUriForFile(context, Constant.FILE_PROVIDER_AUTHORITY, file)
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }

        context.startActivity(intent)
    }

    /**
     * 进入qq客服
     */
    fun enterQQClient(context: Context) {
        //使用随机qq跳转
        val i = Random().nextInt(3)
        val qqUrl = Constant.QQ_LIST[i]
        if (!Tool.isBlank(qqUrl)) {
            val qq_url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqUrl
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(qq_url)))
            } catch (e: Exception) {
                ToastUtil.showToast("请确认安装了QQ客户端")
            }
        }
    }

    /**
     * 压缩图片2
     */
    fun compressImage2(imagePath: String, savedPath: String,  block: (String) -> Unit) {
        Observable.create(Observable.OnSubscribe<String> { subscriber ->
            if (File(imagePath).exists()) { //图片是否存在
                ImageUtil.compressBitmap(imagePath, 2, File(savedPath))
                subscriber.onNext(savedPath)
                subscriber.onCompleted()
            } else {
                subscriber.onError(IOException("图片保存失败"))
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ filePath ->
                    block(filePath)
                }) { throwable -> ToastUtil.showToast(throwable.message) }
    }

    /**
     * 压缩图片
     */
    fun compressImage(context: Context, imagePath: String, block: (String) -> Unit) {
        Observable.create(Observable.OnSubscribe<String> { subscriber ->
            val savedPath = createCompressFilePath(context)
            if (File(imagePath).exists()) { //图片是否存在
                ImageUtil.compressBitmap(imagePath, 2, File(savedPath))
                subscriber.onNext(savedPath)
                subscriber.onCompleted()
            } else {
                subscriber.onError(IOException("图片保存失败"))
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ filePath ->
                    block(filePath)
                }) { throwable -> ToastUtil.showToast(throwable.message) }
    }

    /**
     * 保存图片
     */
    private fun saveBitmap(filePath: String, bitmap: Bitmap) =
        File(filePath).outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }

    fun createImageFile(context: Context) = createFile(context, "/image/")

    fun createCompressFilePath(context: Context) = createFile(context, "/upload/")

    /**
     * 创建文件
     */
    private fun createFile(context: Context, postName: String): String {
        //文件路径
        val path = getBaseDir(context) + postName
        val file = File(path)
        if (!file.exists())
            file.mkdirs()
        //文件名称
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        return path + fileName
    }

    private fun getBaseDir(context: Context) =
            Environment.getExternalStorageDirectory().toString() + "/" + context.applicationInfo.packageName

    /**
     * 获取唯一id
     */
    private fun getUniquePseudoID(): String {
        var serial: String? = null

        val szDevIDShort = "35" +
                Build.BOARD.length % 10 + Build.BRAND.length % 10 +
                Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +
                Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
                Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +
                Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +
                Build.TAGS.length % 10 + Build.TYPE.length % 10 +
                Build.USER.length % 10 //13 位

        try {
            serial = android.os.Build::class.java.getField("SERIAL").get(null).toString()
            //API>=9 使用serial号
            return UUID(szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (exception: Exception) {
            //serial需要一个初始化
            serial = "serial" // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return UUID(szDevIDShort.hashCode().toLong(), serial!!.hashCode().toLong()).toString()

    }
}