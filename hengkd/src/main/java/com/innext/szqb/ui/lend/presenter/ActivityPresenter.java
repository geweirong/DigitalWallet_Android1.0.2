package com.innext.szqb.ui.lend.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.lend.bean.ActivityBean;
import com.innext.szqb.ui.lend.bean.IndexActivityBean;
import com.innext.szqb.ui.lend.contract.ActivityContract;
import com.innext.szqb.ui.my.bean.Item;
import com.innext.szqb.ui.my.bean.VersionUpdateBean;
import com.orhanobut.logger.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hengxinyongli on 2017/3/31 0031.
 */

public class ActivityPresenter extends BasePresenter<ActivityContract.View> implements ActivityContract.Presenter {

    private static final int TYPE_DOWNLOAD = 10;
    private static final String INDEX_ACTIVITY_PARAM = "newYear";

    @Override
    public void loadActivity() {
        toSubscribe(HttpManager.getApi().getActivityData(), new HttpSubscriber<ActivityBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(ActivityBean activityBean) {
                if (activityBean!=null){
                    mView.loadActivitySuccess(activityBean);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }

    @Override
    public void checkUpdate(String channel) {
        toSubscribe(HttpManager.getApi().checkVersion(channel), new HttpSubscriber<VersionUpdateBean>() {

            @Override
            protected void _onStart() {
            }

            @Override
            protected void _onNext(VersionUpdateBean bean) {
                if (bean != null) {
                    Item item = bean.getItem();
                    if (item != null && "1".equals(item.isUpdate())) {
                        mView.loadCheckUpdateSuccess(item);
                    }
                }
            }

            @Override
            protected void _onError(String message) {

            }

            @Override
            protected void _onCompleted() {

            }
        });
    }

    @Override
    public void downloadApk(String downloadUrl, final String savedPath) {
        OkHttpClient okHttpClient = HttpManager.getOkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    InputStream is = response.body().byteStream();
                    //为了方便就不动态申请权限了,直接将文件放到CacheDir()中
                    File file = new File(savedPath);
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.e("----------error:" + e.getMessage());
                }
            }
        });
    }

    @Override
    public void getIndexActivity() {
        toSubscribe(HttpManager.getApi().getIndexActivity(INDEX_ACTIVITY_PARAM), new HttpSubscriber<IndexActivityBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(IndexActivityBean bean) {
                if (bean != null) mView.onGetIndexActivitySuccess(bean);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
