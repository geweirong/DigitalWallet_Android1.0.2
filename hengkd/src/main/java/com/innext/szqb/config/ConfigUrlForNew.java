package com.innext.szqb.config;

import com.innext.szqb.BuildConfig;

/**
 * Created by hengxinyongli on 2017/9/12.
 * 新接口地址配置
 */
public class ConfigUrlForNew {
    public static final String NEW_BASE_URL= BuildConfig.BOOT_DOMAIN;//测试服务器地址
//    public static final String NEW_BASE_URL="http://172.30.2.170/hkd_boot/";//本地服务器地址
    /**
     * 获取首页数据
     */
    public static final String INDEX = NEW_BASE_URL + "index/show";
    public static final String NEW_INDEX = NEW_BASE_URL + "index/indexShow";
    /**
     * 获取首页服务费数据
     */
    public static final String GET_SERVICE = NEW_BASE_URL + "index/detail";
    /**
     * 首页活动接口
     */
    public static final String INDEX_ACTIVITY = NEW_BASE_URL + "index/activity";
//    public static final String INDEX_ACTIVITY = "http://172.30.2.123:8084/hkd_boot/" + "index/activity";
    /**
     * 验证借款接口
     */
    public static final String VERIFICATION_LOAN = NEW_BASE_URL + "loan/get-confirm-loan";
    /**
     * 申请借款
     */
    public static final String APPLY_LOAN = NEW_BASE_URL + "loan/commit_loan";
    /**
     * 借款下单接口
     */
    public static final String COMMIT_BORROW = NEW_BASE_URL + "loan/commit-borrow";
    /*
     *去借款
     */
    public static final String BORRW_DATA = NEW_BASE_URL + "loan/borrow-data";
    /*
     * 借款详情还款计划接口
     */
    public static final String PRPAYMENT_PLAN = NEW_BASE_URL + "assetRepayment/listAssetRepaymentPlan";
    /*
     * 获取预期还款计划列表
     */
    public static final String PRPAYMENT_LIST = NEW_BASE_URL + "loan/expect-repay-list";

    /**
     * 获取借款记录
     */
    public static final String GET_BORROW_RECORD = NEW_BASE_URL + "loan/AssetOrder";
    /**
     * 获取借款详情
     */
    public static final String GET_BORROWING_DETAIL = NEW_BASE_URL + "loan/orderListDetail";

    /*
     * 全部账单列表
     */
    public static final String GET_REPAYMENT = NEW_BASE_URL + "assetRepayment/listAssetRepayment";

    /*
     * 立即还款
     */
    public static final String REPAY_CHOOSE_MUTIL = NEW_BASE_URL + "repayment/repay-choose-mutil";

    /*
     *我的订单
     */
    public static final String MY_ORDER_LIST = NEW_BASE_URL + "store/my-order-list";
}
