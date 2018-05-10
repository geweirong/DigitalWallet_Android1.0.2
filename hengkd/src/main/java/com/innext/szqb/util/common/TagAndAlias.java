package com.innext.szqb.util.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.innext.szqb.app.App;
import com.innext.szqb.config.Constant;
import com.innext.szqb.util.view.ViewUtil;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by HX0010637 on 2018/5/8.
 */

public class TagAndAlias {
    /**
     * 设置标签与别名
     */
    public static void setTagAndAlias(Context context,String userid) {
        /**
         *这里设置了别名，在这里获取的用户登录的信息
         *并且此时已经获取了用户的userId,然后就可以用用户的userId来设置别名了
         **/
        //false状态为未设置标签与别名成功
        Set<String> tags = new HashSet<String>();
        //这里可以设置你要推送的人，一般是用户uid 不为空在设置进去 可同时添加多个  SpUtil.getString(Constant.CACHE_TAG_UID))
        if (!TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_UID))){
            tags.add(SpUtil.getString(userid + "_" +ViewUtil.getDeviceId(App.getContext())));//设置tag
            //上下文、别名【Sting行】、标签【Set型】、回调
            Log.e("别名1：","别名1："+userid + "_" +ViewUtil.getDeviceId(App.getContext()));
            JPushInterface.setAliasAndTags(context, userid + "_" +ViewUtil.getDeviceId(App.getContext()), tags,
                    mAliasCallback);
        }else {
            tags.add(SpUtil.getString(ViewUtil.getDeviceId(App.getContext())));//设置tag
            //上下文、别名【Sting行】、标签【Set型】、回调
            Log.e("别名2：","别名2："+userid + "_" +ViewUtil.getDeviceId(App.getContext()));
            JPushInterface.setAliasAndTags(context, ViewUtil.getDeviceId(App.getContext()), tags,
                    mAliasCallback);
        }
    }
    public static final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    //这里可以往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    //UserUtils.saveTagAlias(getHoldingActivity(), true);
                    logs = "Set tag and alias success极光推送别名设置成功";
                    Log.e("TAG", logs);
                    break;
                case 6002:
                    //极低的可能设置失败 我设置过几百回 出现3次失败 不放心的话可以失败后继续调用上面那个方面 重连3次即可 记得return 不要进入死循环了...
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.极光推送别名设置失败，60秒后重试";
                    Log.e("TAG", logs);
                    break;
                default:
                    logs = "极光推送设置失败，Failed with errorCode = " + code;
                    Log.e("TAG", logs);
                    break;
            }
        }
    };
}
