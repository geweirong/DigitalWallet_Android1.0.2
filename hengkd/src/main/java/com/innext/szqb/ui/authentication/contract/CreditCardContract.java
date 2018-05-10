package com.innext.szqb.ui.authentication.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.authentication.bean.CreditCardBean;
import com.innext.szqb.ui.authentication.bean.EmailInfo;

import java.util.List;

/**
 * Created by HX0010637 on 2018/5/2.
 */

public interface CreditCardContract {
    interface View extends BaseView {
      void getEmailSuccess(List<EmailInfo> list);
      void getSaveCreditCardSuccess();
    }
    interface Presenter {
     void getEmailInfo();
     void getSaveCredit(String taskId);
    }
}
