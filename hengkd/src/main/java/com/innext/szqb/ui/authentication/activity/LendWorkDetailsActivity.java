package com.innext.szqb.ui.authentication.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.dialog.AlertFragmentDialog;
import com.innext.szqb.dialog.PickerViewFragmentDialog;
import com.innext.szqb.events.AuthenticationRefreshEvent;
import com.innext.szqb.ui.authentication.bean.GetWorkInfoBean;
import com.innext.szqb.ui.authentication.contract.WorkDetailContract;
import com.innext.szqb.ui.authentication.presenter.WorkDetailPresenter;
import com.innext.szqb.util.view.StatusViewUtil;
import com.innext.szqb.util.view.StatusViewUtil.IOnTouchRefresh;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.widget.ClearEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 工作信息
 * hengxinyongli
 */
public class LendWorkDetailsActivity extends BaseActivity<WorkDetailPresenter> implements
        WorkDetailContract.View {

    @BindView(R.id.et_company_name)
    ClearEditText mEtCompanyName;
    @BindView(R.id.et_company_phoneNum)
    ClearEditText mEtCompanyPhoneNum;
    @BindView(R.id.tv_company_area)
    TextView mTvCompanyArea;
    @BindView(R.id.et_company_address)
    EditText mEtCompanyAddress;// 所在部门，具体岗位，公司详细地址
    @BindView(R.id.tv_incompany_duration)
    TextView mTvIncompanyDuration;
    @BindView(R.id.et_position_name)
    ClearEditText mEtPositionName;

    private int incompany_duration;
    private int incompany_duration_pos;
    private boolean isChange;
    private List<GetWorkInfoBean.ItemBean.CompanyPeriodListBean> enter_time_list;
    private PoiItem poiItem;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lend_work_details;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle("工作信息");
        mPresenter.getWorkInfo();
    }

    TextWatcher changeText = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            showSaveBtn();
        }
    };

    private void showSaveBtn() {
        isChange = true;
        mTitle.setRightTitle("保存", new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    save();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isChange) {
            new AlertFragmentDialog.Builder(this).setContent("是否要保存修改？")
                    .setLeftBtnText("取消")
                    .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                        @Override
                        public void dialogLeftBtnClick() {
                            finish();
                        }
                    })
                    .setRightBtnText("保存")
                    .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                        @Override
                        public void dialogRightBtnClick() {
                            if (check()) {
                                save();
                            }
                        }
                    }).build();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GDMapActivity.GET_POI_REQUEST_CODE && resultCode == RESULT_OK) {
            poiItem = data.getParcelableExtra("result");
            mTvCompanyArea.setText(poiItem.getTitle() + " — (" + poiItem.getSnippet() + ")");
            showSaveBtn();
        }
    }

    private void initLisenters() {
        mEtCompanyName.addTextChangedListener(changeText);
        mEtPositionName.addTextChangedListener(changeText);
        mEtCompanyPhoneNum.addTextChangedListener(changeText);
        mEtCompanyAddress.addTextChangedListener(changeText);
    }

    private void showPickerView(int curPos, ArrayList<String> list) {
        new PickerViewFragmentDialog(curPos, list, new PickerViewFragmentDialog.OnValueSelectListener() {
            @Override
            public void select(String value, int position) {
                showSaveBtn();
                mTvIncompanyDuration.setText(value);
                incompany_duration_pos = position;
                incompany_duration = Integer.valueOf(enter_time_list.get(position).getEntry_time_type());
            }
        }).show(getSupportFragmentManager(), PickerViewFragmentDialog.TAG);
    }

    private void save() {
        Map<String, String> params = new HashMap<>();
        params.put("company_address", mEtCompanyAddress.getText().toString());
        params.put("company_address_distinct", mTvCompanyArea.getText().toString());
        if (!StringUtil.isBlankEdit(mEtCompanyName)) {
            params.put("company_name", mEtCompanyName.getText().toString());
        }
        //职位名称的判定
        if (!StringUtil.isBlankEdit(mEtPositionName)) {
            params.put("position", mEtPositionName.getText().toString()); //修改为对应的bean字符
        }
        if (!StringUtil.isBlankEdit(mEtCompanyPhoneNum)) {
            params.put("company_phone", mEtCompanyPhoneNum.getText().toString());
        }
        if (incompany_duration != 0) {
            params.put("company_period", String.valueOf(incompany_duration));
        }
        if (poiItem != null && poiItem.getLatLonPoint() != null) {
            params.put("latitude", String.valueOf(poiItem.getLatLonPoint().getLatitude()));
            params.put("longitude", String.valueOf(poiItem.getLatLonPoint().getLongitude()));
        }
        mPresenter.saveWorkInfo(params);
    }

    private boolean check() {
        if (StringUtil.isBlankEdit(mEtPositionName)) {
            ToastUtil.showToast("请输入职位名称");
            return false;
        }
        if (StringUtil.isBlankEdit(mEtCompanyName)) {
            ToastUtil.showToast("请输入单位名称");
            return false;
        }
        if (StringUtil.isBlankEdit(mEtCompanyPhoneNum)) {
            ToastUtil.showToast("请输入单位电话");
            return false;
        }
        if (StringUtil.isBlankEdit(mTvCompanyArea)) {
            ToastUtil.showToast("请选择单位地址");
            return false;
        }
        if (StringUtil.isBlankEdit(mEtCompanyAddress)) {
            ToastUtil.showToast("请输入详细地址");
            return false;
        }
        if (StringUtil.isBlank(mTvIncompanyDuration.getText().toString())) {
            ToastUtil.showToast("请选择工作时长");
            return false;
        }
        return true;
    }


    /*********
     * 针对网络的处理
     *
     * @param style
     */
    private void connectException(String style, String message) {
        StatusViewUtil.showDefaultPopWin(this, new IOnTouchRefresh() {

            @Override
            public void refresh() {
                loadData();
            }
        }, style, message);
    }


    @OnClick({R.id.layout_choose_area, R.id.layout_choose_badge_pic, R.id.layout_choose_incompany_duration})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_choose_area:
                Intent intent = new Intent(LendWorkDetailsActivity.this, GDMapActivity.class);
                startActivityForResult(intent, GDMapActivity.GET_POI_REQUEST_CODE);
                break;
            case R.id.layout_choose_badge_pic:
                Intent intent1 = new Intent(LendWorkDetailsActivity.this, UpLoadPictureActivity.class);
                intent1.putExtra(UpLoadPictureActivity.TAG_UPLOAD_KEY, UpLoadPictureActivity.KEY_UPLOAD_BADGE);
                startActivity(intent1);
                break;
            case R.id.layout_choose_incompany_duration:
                if (enter_time_list != null && enter_time_list.size() > 0) {
                    ArrayList<String> list = new ArrayList<>();
                    for (GetWorkInfoBean.ItemBean.CompanyPeriodListBean result : enter_time_list) {
                        list.add(result.getName());
                    }
                    showPickerView(incompany_duration_pos, list);
                }
                break;
        }
    }

    @Override
    public void getWorkInfoSuccess(GetWorkInfoBean.ItemBean result) {
        if (!StringUtil.isBlank(result.getCompany_name())) {
            mEtCompanyName.setText(result.getCompany_name());
        }
        //读取信息成功之后的显示,新增职位
        if (!StringUtil.isBlank(result.getPosition())) {
            mEtPositionName.setText(result.getPosition());
        }
        if (!StringUtil.isBlank(result.getCompany_phone())) {
            mEtCompanyPhoneNum.setText(result.getCompany_phone());
        }
        if (!StringUtil.isBlank(result.getCompany_address())) {
            mEtCompanyAddress.setText(result.getCompany_address());
            mEtCompanyAddress.setSelection(mEtCompanyAddress.length());
        }
        if (!StringUtil.isBlank(result.getCompany_address_distinct())) {
            mTvCompanyArea.setText(result.getCompany_address_distinct());
        }
        if (TextUtils.isEmpty(result.getCompany_period())) {
            incompany_duration = 0;
        } else {
            incompany_duration = Integer.valueOf(result.getCompany_period());
        }

        enter_time_list = result.getCompany_period_list();
        for (GetWorkInfoBean.ItemBean.CompanyPeriodListBean timeresult : enter_time_list) {
            if (incompany_duration == timeresult.getEntry_time_type()) {
                mTvIncompanyDuration.setText(timeresult.getName());
                incompany_duration_pos = enter_time_list.indexOf(timeresult);
            }
        }
        StatusViewUtil.hidePopWin();
        initLisenters();
    }

    @Override
    public void saveWorkInfoSuccess() {
        EventBus.getDefault().post(new AuthenticationRefreshEvent());
        ToastUtil.showToast("保存成功");
        finish();
    }

    @Override
    public void showLoading(String content) {
        App.loadingContent(this, content);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (type.equals(mPresenter.TYPE_GET)) {
            connectException(StatusViewUtil.TAG_POP_STYLE_NOCONNECT, msg);
        }
        ToastUtil.showToast(msg);
    }
}
