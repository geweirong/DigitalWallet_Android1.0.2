package com.innext.szqb.ui.authentication.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.authentication.bean.PicListBean;
import com.innext.szqb.ui.authentication.bean.SelectPicBean;

/**
 * Created by hengxinyongli on 2017/2/21 0021.
 */

public interface UpLoadPictureContract {
    interface View extends BaseView{
        void getPicListSuccess(PicListBean data);
        void uploadPicSuccess(SelectPicBean info);
    }
    interface Presenter {
        void getPicList(String type);
        void uploadPic(SelectPicBean info, Integer type);
    }
}
