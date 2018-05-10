package com.innext.szqb.util.crypt

import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * @author : yan
 * @date : 2018/1/10 15:54
 * @description : AES加密,注意此文件尽量不要移动位置，避免修改c代码
 */
object AESCrypt {
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"
    private const val ALGORITHM = "AES"
    private var KEY: String

    init {
        System.loadLibrary("native-aes")
        KEY = getAesKey()
    }

    private external fun getAesKey(): String

    /**
     * AES 加密
     * @param input 要加密的内容
     * @param key   秘钥
     * @return      加密后的字节数组
     */
    @JvmOverloads
    fun aesEncrypt(input: String, key: String = KEY): String? {
        try {
            //1.创建cipher对象
            val cipher = Cipher.getInstance(TRANSFORMATION)
            //2.初始化cipher对象
            val keySpec = SecretKeySpec(key.toByteArray(), ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            //3.加密
            val bytes = cipher.doFinal(input.toByteArray())
            //返回16进制字符串
            return parseByte2HexStr(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * AES 解密
     * @param input 要解密的16进制字符串
     * @param key   秘钥
     * @return      解密后的字符串
     */
    @JvmOverloads
    fun aesDecrypt(input: String?, key: String = KEY): String? {
        if (input == null) return null
        try {
            //先将16进制字符串转成字节数组
            val inputBytes = parseHexStr2Byte(input)
            //1.创建cipher对象
            val cipher = Cipher.getInstance(TRANSFORMATION)
            //2.初始化cipher对象
            val keySpec = SecretKeySpec(key.toByteArray(), ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            //3.解密
            if (inputBytes != null) {
                val bytes = cipher.doFinal(inputBytes)
                return String(bytes, Charset.defaultCharset())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** ********************************进制转换***************************************  */

    /**
     * 将二进制转换成16进制
     */
    private fun parseByte2HexStr(buf: ByteArray): String {
        val sb = StringBuffer()
        for (i in buf.indices) {
            var hex = Integer.toHexString(buf[i].toInt() and 0xFF)
            if (hex.length == 1) {
                hex = '0' + hex
            }
            sb.append(hex.toUpperCase())
        }
        return sb.toString()
    }

    /**
     * 将16进制转换为二进制
     */
    private fun parseHexStr2Byte(hexStr: String): ByteArray? {
        if (hexStr.isEmpty()) return null
        val result = ByteArray(hexStr.length / 2)
        for (i in 0 until hexStr.length / 2) {
            val high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16)
            val low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16)
            result[i] = (high * 16 + low).toByte()
        }
        return result
    }

}
