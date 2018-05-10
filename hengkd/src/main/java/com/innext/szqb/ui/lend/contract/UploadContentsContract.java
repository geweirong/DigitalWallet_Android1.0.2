package com.innext.szqb.ui.lend.contract;/**
 * Created by hengxinyongli on 2017/2/15 0015.
 */

import com.innext.szqb.base.BaseView;

/**
 * Created by hengxinyongli at 2017/2/15 0015
 */
public interface UploadContentsContract  {
    interface View extends BaseView {
        //type:1短信，2app，3通讯录
        void uploadSuccess(String type);
    }
    interface Presenter {
        void toUploadContents(String type,String data);
    }
}
