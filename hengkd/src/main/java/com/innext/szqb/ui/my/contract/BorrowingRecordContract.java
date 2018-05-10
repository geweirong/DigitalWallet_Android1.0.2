package com.innext.szqb.ui.my.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.my.bean.BorrowingRecordBean;

/**
 * Created by hengxinyongli on 2017/8/28.
 */

public interface BorrowingRecordContract {
    interface View extends BaseView {
        void getBorrowingDetail(BorrowingRecordBean itemBean);
    }

    interface presenter {
        void getBorrowingDetail(String poolId);
    }
}
