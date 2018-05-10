package com.innext.szqb.ui.authentication.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.authentication.bean.GetWorkInfoBean;

import java.util.Map;

/**
 * Created by hengxinyongli at 2017/2/17 0017
 */
public interface WorkDetailContract {
    interface View extends BaseView{
        void getWorkInfoSuccess(GetWorkInfoBean.ItemBean result);
        void saveWorkInfoSuccess();
    }
    interface Presenter {
        void getWorkInfo();
        void saveWorkInfo(Map<String,String> params);
    }
}
