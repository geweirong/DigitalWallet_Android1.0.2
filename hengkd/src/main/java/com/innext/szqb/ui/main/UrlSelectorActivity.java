package com.innext.szqb.ui.main;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.config.Constant;
import com.innext.szqb.util.common.SpUtil;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;
import com.innext.szqb.widget.recycler.DividerItemDecoration;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UrlSelectorActivity extends BaseActivity {
    @BindView(R.id.edt_url)
    EditText mEdtUrl;
    @BindView(R.id.rv_url)
    RecyclerView mRvUrl;
    private UrlAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_url_select;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, "服务器地址");
        mRvUrl.setLayoutManager(new LinearLayoutManager(this));
        mRvUrl.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        adapter =new UrlAdapter();
        adapter.addData(Arrays.asList(App.getConfig().urls));
        mRvUrl.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                SpUtil.putString(Constant.URL_KEY, adapter.getData().get(position));
                App.getConfig().setBaseUrl(SpUtil.getString(Constant.URL_KEY));
                startActivity(MainActivity.class);
                finish();
            }
        });
        String url = SpUtil.getString(Constant.URL_KEY);
        mEdtUrl.setHint("默认服务器地址:" + App.getConfig().getBaseUrl());
        if (!url.isEmpty()) {
            mEdtUrl.setText(url);
            mEdtUrl.setSelection(url.length());
        }
    }


    @OnClick(R.id.btn_next)
    public void onClick() {
        if (!mEdtUrl.getText().toString().isEmpty()) {
            try {
                Uri.parse(mEdtUrl.getText().toString()).getHost().hashCode();
                SpUtil.putString(Constant.URL_KEY, mEdtUrl.getText().toString());
                App.getConfig().setBaseUrl(SpUtil.getString(Constant.URL_KEY));
            } catch (Exception e) {
                ToastUtil.showToast("服务器地址有误,请重新输入");
                return;
            }
        } else {
            SpUtil.putString(Constant.URL_KEY, "");
        }
        startActivity(MainActivity.class);
        finish();
    }

    static class UrlAdapter extends BaseRecyclerAdapter<UrlAdapter.ViewHolder,String> {

        @Override
        public UrlAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.list_item_selector_url, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void mOnBindViewHolder(UrlAdapter.ViewHolder holder, int position) {
            holder.mTvUrl.setText(item);
        }

        static class ViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.tv_url)
            TextView mTvUrl;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
