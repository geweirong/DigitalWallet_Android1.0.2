package com.innext.szqb.ui.authentication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.innext.szqb.R;
import com.innext.szqb.ui.authentication.bean.AuthenticationinformationBean;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hengxinyongli on 2017/3/30 0030.
 * 描述：
 */

public class PerfectMustInfoAdapter extends BaseRecyclerAdapter<PerfectMustInfoAdapter.ViewHolder,AuthenticationinformationBean> {
    Context context;
    public PerfectMustInfoAdapter(Context mContext){
        this.context=mContext;
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.perfect_information_must_item_list, parent, false);
        return new ViewHolder(view);
    }

//    @Override
//    public int getItemCount() {
//        return 4;
//    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext)
                .load(item.getLogo())
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
                .into(holder.mIvAuthentication);//设置图片
        holder.mTvTitle.setText(item.getTitle());
        //// TODO: 2017/5/10 状态添加了icon需要做优化
        holder.mTvStatus.setText(Html.fromHtml(item.getOperator()));

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_authentication)
        ImageView mIvAuthentication;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_status)
        TextView mTvStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
