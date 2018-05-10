package com.innext.szqb.ui.authentication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.innext.szqb.R;
import com.innext.szqb.ui.authentication.bean.EmailInfo;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HX0010637 on 2018/5/2.
 */

public class CreditCardAdapter extends BaseRecyclerAdapter<CreditCardAdapter.ViewHolder,EmailInfo> {

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cred_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext)
                .load(data.get(position).getLogoImg())
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
                .into(holder.ivEmail);//设置图片
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_email)
        ImageView ivEmail;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
