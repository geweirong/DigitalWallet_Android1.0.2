package com.innext.szqb.dialog;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.util.Tool;
import com.innext.szqb.widget.NumberPicker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择器View
 */

@SuppressLint("ValidFragment")
public class PickerViewFragmentDialog extends DialogFragment {
    public static String TAG = "PickerViewFragmentDialog";
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    @BindView(R.id.number_picker)
    NumberPicker mNumberPicker;

    private OnValueSelectListener listener;
    private ArrayList<String> data;
    private int position;
    @SuppressLint("ValidFragment")
    public PickerViewFragmentDialog(int oldPosition,ArrayList<String> data,OnValueSelectListener listener){
        this.data = data;
        this.position=oldPosition;
        this.listener = listener;
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
        View view = inflater.inflate(R.layout.dialog_picker_view, container, false);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void initDialog() {
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogAnim;
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, Tool.dip2px(getContext(),250));
    }

    private void setData() {
        mNumberPicker.setDisplayedValues(data.toArray(new String[]{}));
        mNumberPicker.setMinValue(0);
        mNumberPicker.setMaxValue(data.size() - 1);
        mNumberPicker.setValue(position);
        mNumberPicker.setWrapSelectorWheel(false);
        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(android.widget.NumberPicker picker, int oldVal, int newVal) {
                position = newVal;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    @OnClick({R.id.tv_cancel, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                listener.select(data.get(position),position);
                dismiss();
                break;
        }
    }

    public interface OnValueSelectListener {
        void select(String value, int position);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        //修改commit方法为commitAllowingStateLoss
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }
}
