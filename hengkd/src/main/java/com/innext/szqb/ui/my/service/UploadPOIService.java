package com.innext.szqb.ui.my.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.innext.szqb.config.Constant;
import com.innext.szqb.ui.my.contract.UploadPOIContract;
import com.innext.szqb.ui.my.presenter.UploadPOIPresenter;
import com.innext.szqb.util.common.LogUtils;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 上传定位信息
 */
public class UploadPOIService extends Service implements UploadPOIContract.View,AMapLocationListener {
    private final String TAG = "UploadPOIService";
    private UploadPOIPresenter mUploadPOIPresenter;
    private Subscription mSubscription;
    private boolean mIsLogin;//若是没有登录,则不上传

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mIsLogin = intent.getBooleanExtra("isLogin",false);
        LogUtils.loge(TAG+"进来了onStartCommand  mIsLogin="+mIsLogin);
        if (mIsLogin){
            uploadLocation();
        }else{
            stopSelf();
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void uploadLocation() {
        registerGaoDeLocation();
        if (mUploadPOIPresenter == null) {
            mUploadPOIPresenter = new UploadPOIPresenter();
            mUploadPOIPresenter.init(this);
        }
        if (mSubscription == null || mSubscription.isUnsubscribed()) {
            LogUtils.loge(TAG+"创建循环上传位置");
            mSubscription = Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    while (mIsLogin) {
                        subscriber.onNext("");
                        try {
                            Thread.sleep(Constant.INTERVAL_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            LogUtils.loge(TAG+"服务结束了");
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!mIsLogin) {
                                LogUtils.loge(TAG+"服务取消了");
                                stopSelf();
                            } else {
                                LogUtils.loge(TAG+"onError");
                                uploadLocation();
                            }
                        }

                        @Override
                        public void onNext(String s) {
                        openGps(new OnPosChanged() {
                            public void changed(AMapLocation location) {
                                if (location.getErrorCode() == 0) {
                                    Date date = new Date(location.getTime());
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    mUploadPOIPresenter.uploadPOI(String.valueOf(location.getLongitude()),
                                            String.valueOf(location.getLatitude()),
                                            location.getAddress(),
                                            String.valueOf(format.format(date)));
                                } else {
                                    Logger.e(location.getErrorInfo());
                                }
                            }
                        });
                        }
                    });
        }else{
            LogUtils.loge(TAG+"继续循环上传位置"+mIsLogin);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.loge(TAG+"进来了onDestroy"+mIsLogin);
        if (mIsLogin) {
            Intent intent = new Intent(this, UploadPOIService.class);
            intent.putExtra("isLogin",mIsLogin);
            startService(intent);
        }else{
            if (mSubscription != null && !mSubscription.isUnsubscribed()) {
                mSubscription.unsubscribe();
            }
            if (mUploadPOIPresenter!=null){
                mUploadPOIPresenter.onDestroy();
            }
            System.exit(0);
        }
    }
    @Override
    public void uploadPOISuccess() {
        LogUtils.loge(TAG+"定位上传成功");
    }

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {
        LogUtils.loge(TAG+"定位上传结束");
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        LogUtils.loge(TAG+"定位上传失败:" + msg);
    }


    /*********************** 高德地图定位 *****************************/

    public  AMapLocationClient mLocationClient;

    private OnPosChanged onPosChanged;

    public void registerGaoDeLocation() {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            //设置定位监听
            mLocationClient.setLocationListener(this);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            setLocOption(5000);
        }
    }
    /***********
     * 打开GPS
     * @param listener
     */
    public  void openGps(OnPosChanged listener)
    {
        onPosChanged = listener;
        setLocOption(5000);
        mLocationClient.startLocation();
    }
    public void setLocOption(int time){
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);// 设置定位模式
        option.setInterval(time);// 设置发起定位请求的间隔时间为5000ms
        option.setNeedAddress(true);// 返回的定位结果包含地址信息
//		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        //设置为高精度定位模式
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(option);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            mLocationClient.stopLocation();
            if(onPosChanged!=null){
                onPosChanged.changed(amapLocation);
            }
        }
    }

    public interface OnPosChanged
    {
        void changed(AMapLocation location);
    }

}
