package com.innext.szqb.ui.authentication.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.innext.szqb.R;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.events.AuthenticationRefreshEvent;
import com.innext.szqb.ui.authentication.adapter.CreditCardAdapter;
import com.innext.szqb.ui.authentication.bean.EmailInfo;
import com.innext.szqb.ui.authentication.contract.CreditCardContract;
import com.innext.szqb.ui.authentication.presenter.CreditCardPresenter;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;
import com.innext.szqb.widget.recycler.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.fraudmetrix.octopus.aspirit.main.OctopusManager;
import cn.fraudmetrix.octopus.aspirit.main.OctopusTaskCallBack;

/*
 * 信用卡邮箱选择界面
 */
public class CreditCardActivity extends BaseActivity<CreditCardPresenter> implements CreditCardContract.View{
    @BindView(R.id.recycler_view)
    RecyclerView mRefreshList;
    private CreditCardAdapter mAdapter;
    private List<EmailInfo> mList;
    @Override
    public int getLayoutId() {
        return R.layout.activity_credit_card;
    }

    @Override
    public void initPresenter() {
         mPresenter.init(this);
         mPresenter.getEmailInfo();
    }
    private void initView(){
        mTitle.setTitle("选择邮箱");
        mRefreshList.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new CreditCardAdapter();
        mRefreshList.setAdapter(mAdapter);
        mList = new ArrayList<>();
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                //对导航栏的返回按钮，导航栏背景，导航栏title进行设置（可选操作）
                OctopusManager.getInstance().setNavImgResId(R.mipmap.icon_back);//设置导航返回图标
                OctopusManager.getInstance().setPrimaryColorResId(R.color.colorPrimary);//设置导航背景
                OctopusManager.getInstance().setTitleColorResId(R.color.c_ffffff);//设置title字体颜色
                OctopusManager.getInstance().setTitleSize(18);//sp 设置title字体大小
                OctopusManager.getInstance().setShowWarnDialog(false);//强制对话框是否显示
                OctopusManager.getInstance().setStatusBarBg(R.color.colorAccent);//设置状态栏背景
                OctopusManager.getInstance().getChannel(mActivity, mList.get(position).getCode(), new OctopusTaskCallBack() {
                    @Override
                    public void onCallBack(int code, String taskId) {
                        String msg = "success:";
                        if (code == 0) {//code
                            msg += taskId;
                            mPresenter.getSaveCredit(taskId);
                            //发送通知更新完善资料界面
                        } else {
                            msg = "failure：" + code;
                        }
                        Log.e("认证结果",""+msg);
//                        ToastUtil.showToast(msg);
                    }
                });
            }
        });
    }
    @Override
    public void loadData() {
        initView();
    }

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {
//        mRefreshLoadLayout.setRefreshing(false);
    }

    @Override
    public void showErrorMsg(String msg, String type) {
//        mRefreshLoadLayout.setRefreshing(false);
    }

    @Override
    public void getEmailSuccess(List<EmailInfo> list) {
//        mRefreshLoadLayout.setRefreshing(false);
        if (list != null && list.size() > 0 ){
            mList = list;
            mAdapter.addData(list);
        }
    }

    @Override
    public void getSaveCreditCardSuccess() {
        EventBus.getDefault().post(new AuthenticationRefreshEvent());
        finish();
        ToastUtil.showToast("认证成功");
    }
}
