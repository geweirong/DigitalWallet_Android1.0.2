package com.innext.szqb.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.innext.szqb.R;
import com.innext.szqb.ui.lend.adapter.CostDetailsAdapter;
import com.innext.szqb.ui.lend.bean.ExpenseDetailBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 服务费用提示fragment
 */

public class ServiceFeeFragmentDialog extends DialogFragment {

    public static String TAG = "ServiceFeeFragmentDialog";

//    @BindView(R.id.tv_interest_money)
//    TextView tv_interest_money;
//
//    @BindView(R.id.tv_account_manager)
//    TextView tv_account_manager;
//
//    @BindView(R.id.tv_credit_inquiry)
//    TextView tv_credit_inquiry;
//
//    @BindView(R.id.tv_fee_sure)
//    TextView tv_fee_sure;
    @BindView(R.id.recycler_service_fee)
    RecyclerView recyclerView;
    private CostDetailsAdapter costDetailAdapter;
    private static String getExpenseDetailBean="expenseDetailBean";


    public static ServiceFeeFragmentDialog newInstance(List<ExpenseDetailBean> result) {
        ServiceFeeFragmentDialog dialog = new ServiceFeeFragmentDialog();
        Bundle bundle = new Bundle();
        ArrayList<ExpenseDetailBean> expenseDetailBeanArrayList= (ArrayList<ExpenseDetailBean>) result;
        bundle.putParcelableArrayList(getExpenseDetailBean,expenseDetailBeanArrayList);
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
        View view = inflater.inflate(R.layout.dialog_service_fee, container, false);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void initDialog() {
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.84), getDialog().getWindow().getAttributes().height);
    }

    private void setData() {
        ArrayList<ExpenseDetailBean> expenseDetailBean = getArguments().getParcelableArrayList(getExpenseDetailBean);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        costDetailAdapter=new CostDetailsAdapter();
        recyclerView.setAdapter(costDetailAdapter);
        recyclerView.setLayoutManager(manager);
        costDetailAdapter.getData().addAll(expenseDetailBean);
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    @OnClick({R.id.tv_fee_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fee_sure:
                dismiss();
                break;
        }
    }



    @Override
    public void show(FragmentManager manager, String tag) {
        //修改commit方法为commitAllowingStateLoss
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

}
