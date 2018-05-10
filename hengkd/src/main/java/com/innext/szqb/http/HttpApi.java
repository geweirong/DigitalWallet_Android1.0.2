package com.innext.szqb.http;

import com.innext.szqb.BuildConfig;
import com.innext.szqb.bean.BaseResponse;
import com.innext.szqb.config.ConfigUrlForNew;
import com.innext.szqb.ui.authentication.bean.BankInfoBean;
import com.innext.szqb.ui.authentication.bean.CreditCardBean;
import com.innext.szqb.ui.authentication.bean.GetBankListBean;
import com.innext.szqb.ui.authentication.bean.GetPicListBean;
import com.innext.szqb.ui.authentication.bean.GetRelationBean;
import com.innext.szqb.ui.authentication.bean.GetWorkInfoBean;
import com.innext.szqb.ui.authentication.bean.ImageDataBean;
import com.innext.szqb.ui.authentication.bean.PerfectInfoBean;
import com.innext.szqb.ui.authentication.bean.PersonalInformationRequestBean;
import com.innext.szqb.ui.authentication.bean.SaveInfoBean;
import com.innext.szqb.ui.lend.bean.ActivityBean;
import com.innext.szqb.ui.lend.bean.AgreeStateBean;
import com.innext.szqb.ui.lend.bean.ConfirmLoanBean;
import com.innext.szqb.ui.lend.bean.ConfirmLoanResponseBean;
import com.innext.szqb.ui.lend.bean.ExpenseDetailBean;
import com.innext.szqb.ui.lend.bean.HomeInfoResponseBean;
import com.innext.szqb.ui.lend.bean.IndexActivityBean;
import com.innext.szqb.ui.login.bean.LoginBean;
import com.innext.szqb.ui.login.bean.RegisterBean;
import com.innext.szqb.ui.login.bean.RegisterCodeBean;
import com.innext.szqb.ui.mall.bean.MallCheackAuthorBean;
import com.innext.szqb.ui.my.bean.AllScoreBean;
import com.innext.szqb.ui.my.bean.BorrowingRecordBean;
import com.innext.szqb.ui.my.bean.CBResponse;
import com.innext.szqb.ui.my.bean.InvitationDataInfo;
import com.innext.szqb.ui.my.bean.Lottery;
import com.innext.szqb.ui.my.bean.MoreBean;
import com.innext.szqb.ui.my.bean.MoxieApproveBean;
import com.innext.szqb.ui.my.bean.MoxieApproveStateBean;
import com.innext.szqb.ui.my.bean.MyOrderBean;
import com.innext.szqb.ui.my.bean.QueryVipStateBean;
import com.innext.szqb.ui.my.bean.ScoreRecordBean;
import com.innext.szqb.ui.my.bean.TransactionBean;
import com.innext.szqb.ui.my.bean.VersionUpdateBean;
import com.innext.szqb.ui.repayment.bean.RepayMentInfoBean;
import com.innext.szqb.ui.repayment.bean.RepaymentBean;
import com.innext.szqb.ui.repayment.bean.RepaymentDetailBean;
import com.innext.szqb.ui.repayment.bean.RepaymentPlanBean;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * retrofit接口请求地址
 */
public interface HttpApi {
    //所有需要的泛型 添加：BaseResponse<UserInfo>
    //邀请好友赚积分
    String URL_SCORE_LOTTERY = BuildConfig.API_DOMAIN + "gotoInviteGetGift";

    //征信报告url
    String URL_CREDIT_REPORT = BuildConfig.API_DOMAIN + "integralPrizeShow/goIntegralDraw";;

    /**
     * 上传用户信息
     *
     * @param device_id
     * @param installed_time
     * @param uid
     * @param username
     * @param net_type
     * @param identifyID
     * @param appMarket
     * @return
     */
    @FormUrlEncoded
    @POST("credit-app/device-report")
    Observable<BaseResponse> deviceReport(@Field("device_id") String device_id,
                                          @Field("installed_time") String installed_time,
                                          @Field("uid") String uid,
                                          @Field("username") String username,
                                          @Field("net_type") String net_type,
                                          @Field("identifyID") String identifyID,
                                          @Field("appMarket") String appMarket);

    /*******************************  借款模块 start ***********************/

    /**
     * 首页新接口
     */
    @GET(ConfigUrlForNew.NEW_INDEX)
    Observable<BaseResponse<HomeInfoResponseBean>> getinfo();

    /**
     * 修改状态
     */
    @FormUrlEncoded
    @POST("credit-card/updateAssetOrderPool")
    Observable<BaseResponse> updateAssetOrder(@Field("userId") Integer userid);

    //首页借款费用详情
    @GET(ConfigUrlForNew.GET_SERVICE)
    Observable<BaseResponse<List<ExpenseDetailBean>>> loadExpenseDetail(@Query("moneyAmount") String money,
                                                                        @Query("periods") String day);

    /**
     * 首页活动弹窗
     */
    @GET("ad/getAd")
    Observable<BaseResponse<ActivityBean>> getActivityData();

    /**
     * 验证借款/loan/get-confirm-loan
     */
//    @FormUrlEncoded
//    @POST("credit-loan/get-confirm-loan")
//    Observable<BaseResponse<ConfirmLoanResponseBean>> toLoan(@Field("money") String money,
    @GET(ConfigUrlForNew.VERIFICATION_LOAN)
    Observable<BaseResponse<ConfirmLoanResponseBean>> toLoan(@Query("money") String money,
                                                             @Query("periods") String period,
                                                             @Query("loan_periods") String loan_perioes);


    /**
     * 首页借款被拒绝后调用的接口
     */
    @FormUrlEncoded
    @POST("credit-loan/confirm-failed-loan")
    Observable<BaseResponse> confirmFailed(@Field("id") String id);

    /**
     * 首页活动
     */
    @GET(ConfigUrlForNew.INDEX_ACTIVITY)
    Observable<BaseResponse<IndexActivityBean>> getIndexActivity(@Query("type") String type);
    /**
     * 借款下单接口
     */
    @FormUrlEncoded
    @POST(ConfigUrlForNew.COMMIT_BORROW)
    Observable<BaseResponse> applyLoan(@Field("borrowAmount") String money,//借款金额：单位为分
                                       @Field("payPassword") String pay_password,//支付密码
                                       @Field("usageIndex") String usageIndex, // 借款用途索引
                                       @Field("productNo") String productNo);  // 金融产品编号
    /*
     *去借款接口
     */
    @GET(ConfigUrlForNew.BORRW_DATA)
    Observable<BaseResponse<ConfirmLoanBean>> borrowData();

    /*
     * 获取预期还款计划列表
     */
    @GET(ConfigUrlForNew.PRPAYMENT_LIST)
    Observable<BaseResponse<RepaymentPlanBean>> getPlanList(@Query("borrowAmount") String borrowAmount,
                                                             @Query("productNo") String productNo);
    /*
     * 借款详情还款计划接口
     */
     @GET(ConfigUrlForNew.PRPAYMENT_PLAN)
     Observable<BaseResponse<RepaymentPlanBean>> getPlan(@Query("assetOrderId") String assetOrderId);

     /*
      * 获取同盾信用卡邮箱
      */
     @GET("userCreditCard/rentCreditCard")
     Observable<BaseResponse<CreditCardBean>> getEmailInfo();
    /**
     * 上传短信 app列表 联系人  type:1短信，2app，3通讯录
     */
    @FormUrlEncoded
    @POST("credit-info/up-load-contents")
    Observable<BaseResponse> upLoadContents(@Field("type") String type,
                                            @Field("data") String data);
    /*******************************  借款模块 end ***********************/

    /*******************************  还款模块 start ***********************/
    /**
     * 还款页面接口
     */
    @GET("credit-loan/get-my-loan")
    Observable<BaseResponse<RepaymentBean>> getMyLoan();

    /**
     * 修改同意收取报告费状态接口
     */
    @GET("credit-card/modify-agree-state")
    Observable<BaseResponse<AgreeStateBean>> getStateArgee();
    /*******************************  还款模块 end ***********************/

    /*******************************  登录模块 start ***********************/
    /**
     * 登录
     */
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-user/login")
    Observable<BaseResponse<LoginBean>> login(@Field("username") String username,
                                              @Field("password") String password);

    /**
     * 退出登录
     */
    @GET("credit-user/logout")
    Observable<BaseResponse> loginOut();

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("credit-user/register")
    Observable<BaseResponse<RegisterBean>> register(@Field("phone") String phone,
                                                    @Field("code") String code,
                                                    @Field("password") String password,
                                                    @Field("source") String source,
                                                    @Field("invite_code") String invite_code,
                                                    @Field("user_from") String user_from,
                                                    @Field("captcha") String captcha);

    /**
     * 获取注册验证码，验证到已注册则跳转登录
     */
    @FormUrlEncoded
    @POST("credit-user/reg-get-code")
    Observable<BaseResponse<RegisterCodeBean>> getRegisterCode(@Field("phone") String phone,
                                                               @Field("type") String type,
                                                               @Field("captcha") String captcha);

    /**
     * 忘记密码
     */
//    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-user/reset-pwd-code")
    Observable<BaseResponse<RegisterCodeBean>> forgetPwd(@Field("phone") String phone,
                                                         @Field("type") String type,
                                                         @Field("captcha") String captcha,
                                                         @Field("type2") String type2);

    /**
     * 找回登录密码/交易密码验证用户和手机验证码
     */
//    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-user/verify-reset-password")
    Observable<BaseResponse> verifyResetPwd(@Field("phone") String phone,
                                            @Field("code") String code,
                                            @Field("realname") String realname,
                                            @Field("id_card") String id_card,
                                            @Field("type") String type,
                                            @Field("captcha") String captcha);

    /**
     * 找回密码设置新密码
     */
    @FormUrlEncoded
    @POST("credit-user/reset-password")
    Observable<BaseResponse> resetPwd(@Field("phone") String phone,
                                      @Field("code") String code,
                                      @Field("password") String password);

    /**
     * 修改登录密码
     */
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-user/change-pwd")
    Observable<BaseResponse> changePwd(@Field("old_pwd") String old_pwd,
                                       @Field("new_pwd") String new_pwd);

    /*******************************  登陆模块 end ***********************/

    /**********************************   我的模块       *************************/
    /**
     * 我的页面 数据
     */
    @GET("credit-user/get-info")
    Observable<BaseResponse<MoreBean>> getInfo();

    /**
     * 抽奖码
     */
    @FormUrlEncoded
    @POST("jsaward/awardCenterWeb/userDrawAwardList")
    Observable<BaseResponse<Lottery>> lotteryRequest(@Field("phone") String phone,
                                                     @Field("page") String page,
                                                     @Field("pageSize") String pageSize);

    /**
     * 借款记录
     */
    @FormUrlEncoded
    @POST("credit-loan/get-my-orders")
    Observable<BaseResponse<TransactionBean>> recordRequest(@Field("page") String page,
                                                            @Field("pagsize") String pageSize);

    /*
     * 分享成功后回调
     */
    @FormUrlEncoded
    @POST("integral/sharing-success")
    Observable<BaseResponse> getShareSuccess(@Field("deviceId") String deviceId,
                                             @Field("mobilePhone") String mobilePhone);
    /**
     * 获取用户借款记录
     */
    @GET(ConfigUrlForNew.GET_BORROW_RECORD)
    Observable<BaseResponse<TransactionBean>> getLoanRecord(@Query("userId") String userId,
                                                            @Query("page") String pageNum,
                                                            @Query("pagsize") String pageSize);

    /**
     * 用户借款详情
     */
    @GET(ConfigUrlForNew.GET_BORROWING_DETAIL)
    Observable<BaseResponse<BorrowingRecordBean>> getLoanRecordDetail(@Query("poolId") String poolId);

    /**
     * 上传定位信息
     */
    @FormUrlEncoded
    @POST("credit-info/upload-location")
    Observable<BaseResponse> uploadLocation(@Field("longitude") String longitude,
                                            @Field("latitude") String latitude,
                                            @Field("address") String address,
                                            @Field("time") String time);

    /**
     * 意见反馈
     */
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-info/feedback")
    Observable<BaseResponse> feedBack(@Field("content") String content);

    /**
     * 修改交易密码
     */
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-user/change-paypassword")
    Observable<BaseResponse> changePayPassWord(@Field("old_pwd") String old_pwd,
                                               @Field("new_pwd") String new_pwd);

    /**
     * 设置交易密码
     */
    @FormUrlEncoded
    @POST("credit-user/set-paypassword")
    Observable<BaseResponse> setPayPwd(@Field("password") String password);

    /**********************************    个人认证       *******************************/

    /**
     * 获取认证信息
     */
    @GET("credit-card/get-verification-info")
    Observable<BaseResponse<PerfectInfoBean>> getPerfectInfo();

    /**
     * 获取个人认证信息
     */
    @GET("credit-card/get-person-info")
    Observable<BaseResponse<PersonalInformationRequestBean>> getPersonalInformation();

    /**
     * 保存个人信息未实名认证
     */
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-card/get-person-infos")
    Observable<BaseResponse> getRersonInformation(@FieldMap Map<String, String> map);

    /**
     * 保存个人信息已实名认证
     */
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-info/save-person-info")
    Observable<BaseResponse> getSaveRersonInformation(@FieldMap Map<String, String> map);

    /*******************************    上传图片   *****************************************/
    /**
     * 上传图片
     *
     * @param File
     * @param map
     * @return
     */
    @Multipart
    @POST("picture/upload-image")
    Observable<BaseResponse<ImageDataBean>> uploadImageFile(
            @Part MultipartBody.Part File, @PartMap Map<String, Integer> map);

    /**
     * 获取图片集合
     */
    //1:身份证,2:学历证明,3:工作证明,4:薪资证明,5:财产证明,6、工牌照片、7、个人名片，8、银行卡 100:其它证明
    @FormUrlEncoded
    @POST("picture/get-pic-list")
    Observable<BaseResponse<GetPicListBean>> getPicList(@Field("type") String type);

    /**
     * 获取认证列表-紧急联系人
     */
    @GET("credit-card/get-contacts")
    Observable<BaseResponse<GetRelationBean>> getContacts();

    /**
     * 获取认证列表-保存紧急联系人
     */
    @FormUrlEncoded
    @POST("credit-card/get-contactss")
    Observable<BaseResponse> saveContacts(@Field("type") String type,
                                          @Field("mobile") String mobile,
                                          @Field("name") String name,
                                          @Field("relation_spare") String relation_spare,
                                          @Field("mobile_spare") String mobile_spare,
                                          @Field("name_spare") String name_spare);

    /**
     * 获取工作信息
     */
    @GET("credit-card/get-work-info")
    Observable<BaseResponse<GetWorkInfoBean>> getWorkInfo();

    /**
     * 保存工作信息
     */
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-card/save-work-info")
    Observable<BaseResponse> saveWorkInfo(@FieldMap Map<String, String> params);

    /**
     * 获取银行卡验证码
     */
    @FormUrlEncoded
    @POST("credit-card/get-code")
    Observable<BaseResponse> getCardCode(@Field("phone") String phone);

    /**
     * 获取银行卡列表
     */
    @GET("credit-card/bank-card-info")
    Observable<BaseResponse<GetBankListBean>> getBankCardList();

    /**
     * 添加银行卡
     */
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-card/add-bank-card")
    Observable<BaseResponse<BankInfoBean>> addBankCard(@Field("phone") String phone,
                                                       @Field("code") String code,
                                                       @Field("card_no") String card_no,
                                                       @Field("bank_id") String bank_id);

    /**
     * 上传支付宝数据
     */
    @FormUrlEncoded
    @POST("credit-alipay/get-user-info")
    Observable<BaseResponse<SaveInfoBean>> saveAlipayInfo(@Field("data") String data);

    /**
     * 还款列表明细
     */
    @GET("credit-loan/get-my-loanDetail")
    Observable<BaseResponse<RepaymentDetailBean>> getLoanDetail(@Query("poolId") String poolId,
                                                                @Query("assetOrderId") String assetOrderId);

    /**
     * 检查版本更新
     */
    @GET("getVersion")
    Observable<BaseResponse<VersionUpdateBean>> checkVersion(@Query("channel") String channel);

    /**
     * 获取用户所有积分
     */
    @GET("integral/userIntegral")
    Observable<BaseResponse<AllScoreBean>> getAllScore(@Query("userId") int userId);

    /**
     * 获取积分记录列表
     */
    @GET("integral/record")
    Observable<BaseResponse<ScoreRecordBean>> getScoreRecord(@Query("userId") int userId,
                                                             @Query("pageIndex") int pageIndex,
                                                             @Query("pageSize") int pageSize);

    /**
     * 征信报告查询
     */
    @GET("picture/get-cbpic-list")
    Observable<BaseResponse<CBResponse>> getCbPicList();

    /**
     * 会员标志查询
     */
    @GET("credit-card/query-member-state")
    Observable<BaseResponse<QueryVipStateBean>> getVipState();

    /**
     * 完善资料页面提交申请
     */
    @GET("credit-card/sureCommit")
    Observable<BaseResponse> applyInfo();

    /**
     * 完善资料魔蝎下单
     */
    @GET("mx/consume/order/create")
    Observable<BaseResponse<MoxieApproveBean>> getMoXieApprove(@Query("orderType") String orderType);

    /**
     * 完善资料魔蝎认证状态
     */
    @GET("mx/consume/order/getAuthStatus")
    Observable<BaseResponse<MoxieApproveStateBean>> getMoXieApproveState(@Query("orderNo") String orderNo);

    /**
     * 我的邀请码
     */
    @GET("invite/invite_code")
    Observable<BaseResponse<InvitationDataInfo>> getCode();

    /*
     * 获取延期还款信息
     */
    @GET("repayment/postpone/getInfo")
    Observable<BaseResponse<RepayMentInfoBean>>gteRepaymentInfo(@Query("assetId") String assetId);

    /*
     *  验证是否已经还款
     */
    @GET("repayment/get_payment_status")
    Observable<BaseResponse<RepayMentInfoBean>>getPaymentStatus(@Query("assetOrderId") String assetOrderId);
    /*
     * 授权校验
     */
    @GET("checkLoginAuthorization")
    Observable<BaseResponse<MallCheackAuthorBean>> getCheackAuthorization();
    /*
     * 确认用户授权
     */
    @GET("affirmAuthorization")
    Observable<BaseResponse> getAuthorInfo();

    /*
     * 全部账单
     */
    @GET(ConfigUrlForNew.GET_REPAYMENT)
    Observable<BaseResponse<BorrowingRecordBean>> getListAssetRepayment(@Query("assetOrderId") String assetOrderId);

    /*
     * 立即还款
     */
    @GET(ConfigUrlForNew.REPAY_CHOOSE_MUTIL)
    Observable<BaseResponse> getNowPay(@Query("ids") String ids,
                                       @Query("payType") String payType);

    /*
     * 我的订单
     */
    @GET(ConfigUrlForNew.MY_ORDER_LIST)
    Observable<BaseResponse<MyOrderBean>> getOrderInfo();

    /*
     * 保存信用卡认证taskid
     * 信用卡认证通知接口
     */
    @GET("userCreditCard/saveCreditCard")
    Observable<BaseResponse>saveCreditCard(@Query("taskId") String taskId);

}
