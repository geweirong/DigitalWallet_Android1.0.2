package com.innext.szqb.util.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by closetou on 2017/5/22.
 */

public class FileUtils {


    private static final String JPEG_FILE_PREFIX = "IMG_";

    private static final String JPEG_FILE_SUFFIX = ".jpg";

    public static File createTmpFile(Context context) throws IOException {
        File dir = null;
        if(TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            if (!dir.exists()) {
                Log.e("IMG","mTmpFIle----------");
                dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera");
                Log.e("IMG","mTmpFIle000000000");
                if (!dir.exists()) {
                    Log.e("IMG","mTmpFIlewwwwwqqqqq");
                    dir = getCacheDirectory(context, true);
                    Log.e("IMG","mTmpFIleeeeeeeeeee");
                }
            }
        }else{
            dir = getCacheDirectory(context, true);
        }
        Log.e("IMG","mTmpFIle===========");
        return File.createTempFile(JPEG_FILE_PREFIX, JPEG_FILE_SUFFIX, dir);
    }

    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        } catch (IncompatibleClassChangeError e) { // (sh)it happens too (Issue #989)
            externalStorageState = "";
        }
        if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }


    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }
        return appCacheDir;
    }

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";


    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 获取文件大小
     */
    public static long getFile(File file) throws Exception{
        if (file == null){
            return 0;
        }
        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        Log.e("<---7777---->","文件大小："+size);
        return size;
    }
}
