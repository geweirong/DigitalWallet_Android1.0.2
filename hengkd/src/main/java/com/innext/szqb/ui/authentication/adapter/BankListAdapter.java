package com.innext.szqb.ui.authentication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.innext.szqb.R;
import com.innext.szqb.bean.BankItem;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BankListAdapter extends BaseRecyclerAdapter<BankListAdapter.ViewHolder,BankItem> {

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_bank_item,parent,false));
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(item.getUrl()).into(holder.mBankImg);
        if (!StringUtil.isBlank(item.getBank_name()))
            holder.mBankName.setText(item.getBank_name());
        else
            holder.mBankName.setText(item.getBank_info());
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.bankImg)
        ImageView mBankImg;
        @BindView(R.id.bankName)
        TextView mBankName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}


