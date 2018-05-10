//
// Created by HX0010239ANDROID on 2018/1/18.
//

#include <jni.h>
jstring Java_com_innext_szqb_util_crypt_AESCrypt_getAesKey(JNIEnv *env, jobject obj) {
    char *key = "hxyl!@#$%^&*()_+";
    return (*env)->NewStringUTF(env, key);
}
