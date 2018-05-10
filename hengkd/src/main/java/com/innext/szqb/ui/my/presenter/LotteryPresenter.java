package com.innext.szqb.ui.my.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.my.bean.Lottery;
import com.innext.szqb.ui.my.contract.LotteryContract;


/**
 * Created by hengxinyongli on 2017/2/15 0015.
 * 描述：抽奖码逻辑层
 */

public class LotteryPresenter extends BasePresenter<LotteryContract.View> implements LotteryContract.presenter {

    @Override
    public void lotteryRequest(String phone, String page, String pageSize) {
        toSubscribe(HttpManager.getApi().lotteryRequest(phone, page, pageSize), new HttpSubscriber<Lottery>() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(Lottery lottery) {
                if (lottery.getItem() != null) {
                    mView.lotterySuccess(lottery.getItem());
                } else {
                    mView.showErrorMsg("数据获取失败，请重新获取", null);
                }
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
