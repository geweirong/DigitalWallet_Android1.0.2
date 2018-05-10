package com.innext.szqb.ui.mall.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.mall.bean.MallCheackAuthorBean;

/**
 * Created by HX0010637 on 2018/4/11.
 * 确认商城授权
 */

public interface MallContract {
    interface View extends BaseView{
        void getCheackAuthorSuccess(MallCheackAuthorBean mallAuthorBeanBaseResponse);
        void getAuthorSuccess();
    }
    interface Presenter {
        void getAuthorInfo();
        void getCheackAuthor();
    }
}
