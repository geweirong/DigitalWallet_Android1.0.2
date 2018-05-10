package com.innext.szqb.util.common

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.view.View
import com.innext.szqb.app.App
import com.innext.szqb.ext.realRecycle
import java.io.*

/**
 * @author      : yan
 * @date        : 2018/1/26 15:00
 * @description : 图片工具类
 */
object ImageUtil {

    private const val DEFAULT_SIZE = 1024 * 200

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     */
    fun copyFile(oldPath: String, newPath: String) {
        val fis = FileInputStream(oldPath)
        val fos = FileOutputStream(newPath)
        fos.use {
            val buffer = ByteArray(2048 * 4)
            var len: Int
            while (true) {
                len = fis.read(buffer)
                if (len != -1) {
                    it.write(buffer, 0, len)
                } else break
            }
            fis.close()
        }
    }

    /**
     * @param boundary  边界值
     */
    fun compressBitmap(path: String, inSampleSize: Int, file: File, boundary: Int = DEFAULT_SIZE) {
        val length = File(path).length()
        if (length < boundary) {
            copyFile(path, file.absolutePath)
        } else {
            compressBitmap2File(path, inSampleSize, file)
        }
    }

    /**
     * @param path  要压缩文件的路径
     * @param inSampleSize  倍率
     * @param file  压缩后的文件
     */
    private fun compressBitmap2File(path: String, inSampleSize: Int, file: File) {
        val options = BitmapFactory.Options()//new一个options
        options.inJustDecodeBounds = true//先设置为true，即片到内存，先获取图片的信息，比如长宽等信息

        BitmapFactory.decodeFile(path, options)
        //先获取图片格式
        val type = options.outMimeType
        val imgFormat: Bitmap.CompressFormat
        when (type) {
            "image/png" -> imgFormat = Bitmap.CompressFormat.PNG
            else -> imgFormat = Bitmap.CompressFormat.JPEG   //默认image/jpeg
        }
        options.inSampleSize = inSampleSize
        options.inJustDecodeBounds = false //设置为false，是要将图片以一定比例压缩后读入内存中
        val bitmap1: Bitmap? = BitmapFactory.decodeFile(path, options)   //这里得到的bitmap才是不为null

        val bitmap2: Bitmap = reviewPicRotate(bitmap1, path) ?: return

        val baos = ByteArrayOutputStream()
        // 把压缩后的数据存放到baos中
        bitmap2.compress(imgFormat, 80, baos)

        file.outputStream().use {
            it.write(baos.toByteArray())
        }
        bitmap1?.realRecycle()
        bitmap2.realRecycle()
    }

    /**
     * 获取图片文件的信息，是否旋转了90度，如果是则反转
     * @param bitmap 需要旋转的图片
     * @param path   图片的路径
     */
    private fun reviewPicRotate(bitmap: Bitmap?, path: String): Bitmap? {
        if (bitmap == null) return null
        var bitmap2 = bitmap
        val degree = getPicRotate(path)
        if (degree != 0) {
            val m = Matrix()
            val width = bitmap.width
            val height = bitmap.height
            m.setRotate(degree.toFloat()) // 旋转angle度
            bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true)// 从新生成图片
        }
        return bitmap2
    }

    /**
     * 读取图片文件旋转的角度
     * @param path 图片绝对路径
     * @return 图片旋转的角度
     */
    private fun getPicRotate(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

     fun savePicture(bm: Bitmap, fileNmae: String, view: View, context: Context): Boolean {
        try {
            val f = File(Environment.getExternalStorageDirectory().absolutePath, fileNmae + ".png")
            if (f.exists()) {
                f.delete()
            }
            val out = FileOutputStream(f)
            bm.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            view.post {
                //发送广播通知图库更行
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val uri = Uri.fromFile(f)
                intent.data = uri
                context.sendBroadcast(intent)
                App.hideLoading()
                ToastUtil.showToast("保存成功")
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }
}