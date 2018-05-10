package com.innext.szqb.ui.repayment.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.innext.szqb.R;
import com.innext.szqb.ui.repayment.bean.RepaymentItemBean;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RepayTypeItemAdapter extends BaseRecyclerAdapter<RepayTypeItemAdapter.ViewHolder,RepaymentItemBean.PayType> {
    private Fragment fragment;
    public RepayTypeItemAdapter(Fragment fragment){
        this.fragment=fragment;
    }
    @Override
    public long getItemId(int position) {
        return (long) position;
    }
    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.repay_type_item_list,parent,false));
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        holder.mTvRepeyTypeText.setText(data.get(position).getTitle()); //设置文字
        Glide.with(fragment)
                .load(data.get(position).getImg_url())
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
                .centerCrop()
                .into(holder.mIvRepeyTypeImg);//设置图片
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_repey_type_img)
        ImageView mIvRepeyTypeImg;
        @BindView(R.id.tv_repey_type_text)
        TextView mTvRepeyTypeText;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
