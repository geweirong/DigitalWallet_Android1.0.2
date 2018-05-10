package com.innext.szqb.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.innext.szqb.app.App;
import com.innext.szqb.config.Constant;
import com.innext.szqb.ui.my.service.UploadPOIService;
import com.innext.szqb.util.view.ViewUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

public class ServiceUtil {

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos = activityManager.getRunningServices(Constant.RETRIVE_SERVICE_COUNT);

        if(null == serviceInfos || serviceInfos.size() < 1) {
            return false;
        }
        for(int i = 0; i < serviceInfos.size(); i++) {
            if(serviceInfos.get(i).service.getClassName().contains(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 启动service
     * @param context
     */
    public static void invokeTimerPOIService(Context context){
        if (App.getConfig().getLoginStatus()){
            Intent startIntent = new Intent(App.getContext(), UploadPOIService.class);
            startIntent.putExtra("isLogin", App.getConfig().getLoginStatus());
            context.startService(startIntent);
        }
    }

    /**
     * 取消service
     * @param context
     */
    public static void cancelAlarmManager(Context context){
        if (!App.getConfig().getLoginStatus()){
            Intent intent = new Intent(App.getContext(), UploadPOIService.class);
            intent.putExtra("isLogin", App.getConfig().getLoginStatus());
            context.startService(intent);
        }
    }

    /**
     * 设置标签与别名
     */
    public static void setTagAndAlias(Context context) {
        /**
         *这里设置了别名，在这里获取的用户登录的信息
         *并且此时已经获取了用户的userId,然后就可以用用户的userId来设置别名了
         **/
        //false状态为未设置标签与别名成功
        Set<String> tags = new HashSet<String>();
        //这里可以设置你要推送的人，一般是用户uid 不为空在设置进去 可同时添加多个
        if (!TextUtils.isEmpty(Constant.USER_ID)){
            tags.add(Constant.USER_ID);//设置tag
        }
        //上下文、别名【Sting行】、标签【Set型】、回调
//        JPushInterface.setAlias(context,Integer.parseInt(Constant.USER_ID) ,Constant.USER_ID + "_" + ViewUtil.getDeviceId(App.getContext()));
    }
}
