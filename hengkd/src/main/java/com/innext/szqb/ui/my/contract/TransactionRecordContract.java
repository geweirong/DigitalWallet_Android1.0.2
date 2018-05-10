package com.innext.szqb.ui.my.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.my.bean.TransactionRecordListBean;

import java.util.List;

/**
 * Created by hengxinyongli on 2017/2/16 0016.
 * 描述：
 */

public interface TransactionRecordContract {
    interface View extends BaseView {
        void recordSuccess(List<TransactionRecordListBean> itemBean,String transactionBeanLinkUrl);
        void loadRecordSuccess(List<TransactionRecordListBean> itemBean,String transactionBeanLinkUrl);
    }

    interface presenter {
        void recordRequest(String page, String pagesize);
        void getLoanRecordRequest(String userId,String pageNum,String pageSize);
    }
}
