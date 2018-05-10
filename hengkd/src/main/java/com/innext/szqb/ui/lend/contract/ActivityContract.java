package com.innext.szqb.ui.lend.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.lend.bean.ActivityBean;
import com.innext.szqb.ui.lend.bean.IndexActivityBean;
import com.innext.szqb.ui.my.bean.Item;

/**
 * Created by hengxinyongli on 2017/3/31 0031.
 */

public interface ActivityContract {
    interface View extends BaseView{
        void loadActivitySuccess(ActivityBean result);
        void loadCheckUpdateSuccess(Item result);
        void onGetIndexActivitySuccess(IndexActivityBean result);
    }
    interface Presenter{
        void loadActivity();
        void checkUpdate(String channel);
        void downloadApk(String downloadUrl, String savedPath);
        void getIndexActivity();
    }
}
