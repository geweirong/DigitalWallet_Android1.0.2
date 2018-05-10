package com.innext.szqb.ui.authentication.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.innext.szqb.R;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.ui.authentication.fragment.GDSearchFragment;
import com.innext.szqb.util.common.KeyBordUtil;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.util.Tool;
import com.innext.szqb.widget.ClearEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 地图搜索列表
 */
public class GDMapSearchActivity extends BaseActivity {
    public static final int SEARCH_REQUEST_CODE = 1002;
    @BindView(R.id.et_search)
    ClearEditText mEtSearch;
    @BindView(R.id.container)
    FrameLayout mContainer;
    private GDSearchFragment gdSearchFragment;
    private String cityCode;
    @Override
    public int getLayoutId() {
        return R.layout.activity_gdmap_search;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        cityCode = getIntent().getStringExtra("cityCode");
        gdSearchFragment = GDSearchFragment.getInstance(cityCode);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(mContainer.getId(), gdSearchFragment);
        transaction.commitAllowingStateLoss();
    }


    @OnClick({R.id.iv_back, R.id.tv_search_btn})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search_btn:
                if (StringUtil.isBlankEdit(mEtSearch)) {
                    ToastUtil.showToast("请输入要搜索的位置");
                } else {
                    gdSearchFragment.loadSearchData(0, mEtSearch.getText().toString());
                    KeyBordUtil.hideSoftKeyboard(mEtSearch);
                }
                break;
        }
    }
}
