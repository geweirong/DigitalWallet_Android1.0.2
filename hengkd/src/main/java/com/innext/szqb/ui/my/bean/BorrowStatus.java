package com.innext.szqb.ui.my.bean;

/**
 * Created by HX0010637 on 2018/5/7.
 */

public class BorrowStatus {
    /**
     * (0:待初审(待机审);
     * -3:初审驳回;
     * 1:初审通过;
     * -4:复审驳回;
     * 20:复审通过,待放款;
     * -5:放款驳回;
     * 22:放款中;
     * -10:放款失败;
     * 21已放款，还款中;
     * 23:部分还款;
     * 30:已还款;
     * -11:已逾期;
     * -20:已坏账，
     * 34逾期已还款；
     *
     * 30,34:已还款 可点击
     * -11,21：待还款
     */

    /*
     * 借款详情是否显示还款按钮，是否可点击选中借款
     */
    public static Boolean isShowRepay(int borrowStatus){
        return (!(-11 == borrowStatus || 21 == borrowStatus));
    }
}
