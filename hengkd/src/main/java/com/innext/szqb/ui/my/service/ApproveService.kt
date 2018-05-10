package com.innext.szqb.ui.my.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.innext.szqb.ui.my.bean.MoxieApproveStateBean
import com.innext.szqb.ui.my.contract.ApproveContract
import com.innext.szqb.ui.my.presenter.ApprovePresenter
import com.innext.szqb.util.common.LogUtils
import java.util.*

/**
 * Created by HX0010204NET on 2018/1/3.
 */
class ApproveService : Service(), ApproveContract.View {
    override fun showLoading(content: String?) {
    }

    override fun stopLoading() {
    }

    override fun showErrorMsg(msg: String?, type: String?) {

    }

    //    val TAG = "ApproveService"
    val binder = MyBinder()
    var ordernos = ""
    var presenter = ApprovePresenter()
    var mycallback: MyServiceCallback? = null
    /**
     * Timer实时更新数据的
     */
    private val mTimer = Timer()


    override fun onCreate() {
        presenter.init(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    /**
     * 接口回调成功
     */
    override fun getApproveStateSuccess(moxieApproveStateBean: MoxieApproveStateBean) {
        mycallback?.getState(moxieApproveStateBean)
    }

    /**
     *
     */
    var task: TimerTask = object : TimerTask() {
        override fun run() {
            presenter.getApproveState(ordernos)
        }
    }


    override fun onDestroy() {
        /**
         * 停止Timer
         */
        mTimer.cancel()
        super.onDestroy()
    }


    inner class MyBinder : Binder() {
        fun getService(): ApproveService {
            return this@ApproveService
        }

        fun getApproveState(orderno: String) {
            LogUtils.loge("shiquan" + "服务开始循环监听....ID: $orderno")
            ordernos = orderno
            mTimer.schedule(task, 3000, 5000)
        }
    }

    interface MyServiceCallback {
        fun getState(moxieApproveStateBean: MoxieApproveStateBean)
    }
}