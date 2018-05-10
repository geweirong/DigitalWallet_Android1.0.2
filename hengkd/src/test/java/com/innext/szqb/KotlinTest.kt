package com.innext.szqb

import android.graphics.Color
import com.innext.szqb.util.common.ImageUtil
import com.innext.szqb.util.crypt.AESCrypt
import com.innext.szqb.util.view.RichTextUtil
import org.junit.Test

/**
 * @author      : yan
 * @date        : 2018/1/3 14:28
 * @description : todo
 */
class KotlinTest {
    @Test
    fun testTime() {
        val str = "2017-01-01 10:00:00"
//        println(str.getFriendMonth())

        RichTextUtil.setRichText(
                "123" to Color.RED,
                "yyie" to Color.BLACK,
                "jfidaji" to Color.BLUE
        )
    }

    @Test
    fun testCrypt() {
        val content = "123456"
        val key = "1234567812345678"

        val encrypt = AESCrypt.aesEncrypt(content, key)
        println("AES加密后：" + encrypt)
        val decrypt = AESCrypt.aesDecrypt(encrypt, key)
        println("AES解密后：" + decrypt)
    }

    @Test
    fun testCopyFile() {
        val oldPath = "C:\\Users\\HX0010239ANDROID\\Desktop\\HXYL\\HKD_Android\\hengkd\\src\\test\\java\\com\\innext\\hkd\\KotlinTest.kt"
        val newPath = "C:\\Users\\HX0010239ANDROID\\Desktop\\Copy.kt"
        ImageUtil.copyFile(oldPath, newPath)
    }

}