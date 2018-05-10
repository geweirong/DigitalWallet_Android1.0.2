package com.innext.szqb.dialog;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.bean.BankItem;
import com.innext.szqb.ui.authentication.adapter.BankListAdapter;
import com.innext.szqb.util.Tool;
import com.innext.szqb.widget.DrawableCenterTextView;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 银行卡列表
 */

public class BankListFragmentDialog extends DialogFragment {
    public static String TAG = "BankListFragmentDialog";
    @BindView(R.id.rv_bank_list)
    RecyclerView mRvBankList;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_left)
    DrawableCenterTextView mTvLeft;
    private BankListAdapter mAdapter;
    private selectBankCardListener mListener;
    private ArrayList<BankItem> list;

    public void selectBankCardListener(selectBankCardListener mListener) {
        this.mListener = mListener;
    }

    private static BankListFragmentDialog newInstance(ArrayList<BankItem> list) {
        BankListFragmentDialog dialog = new BankListFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list", list);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 这个判断很重要
        if (getDialog() == null) {
            setShowsDialog(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.layout_bank_list, container, false);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void initDialog() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getDialog().getWindow().setLayout(Tool.getWindowWith(getContext()), Tool.getWindowHeight(getContext()) - Tool.getStatusBarHeight(getContext()));
    }

    private void setData() {
        Drawable left = ContextCompat.getDrawable(getContext(),R.mipmap.icon_back);
        left.setBounds(0,0,left.getMinimumWidth(),left.getMinimumHeight());
        mTvLeft.setCompoundDrawables(left,null,null,null);
        mTvTitle.setText("请选择银行卡");
        mRvBankList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvBankList.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
        list = getArguments().getParcelableArrayList("list");
        mAdapter = new BankListAdapter();
        mAdapter.addData(list);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                mListener.selectBankCard(list.get(position));
                dismiss();
            }
        });
        mRvBankList.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        //修改commit方法为commitAllowingStateLoss
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @OnClick(R.id.tv_left)
    public void onClick() {
        dismiss();
    }


    public static class Builder {
        private FragmentActivity activity;

        private selectBankCardListener mListener;

        private List<BankItem> list;

        public Builder(FragmentActivity activity) {
            this.activity = activity;
        }

        public Builder setSelectBankCardListener(selectBankCardListener mListener) {
            this.mListener = mListener;
            return this;
        }

        public List<BankItem> getList() {
            return list;
        }

        public Builder setList(List<BankItem> list) {
            this.list = list;
            return this;
        }

        public BankListFragmentDialog build() {
            BankListFragmentDialog dialogFragment = BankListFragmentDialog.newInstance((ArrayList<BankItem>) list);
            dialogFragment.selectBankCardListener(mListener);
            dialogFragment.show(activity.getSupportFragmentManager(), dialogFragment.TAG);
            return dialogFragment;
        }
    }

    public interface selectBankCardListener {
        void selectBankCard(BankItem list);
    }
}
