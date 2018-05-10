package com.innext.szqb.ui.authentication.adapter;

import android.app.Activity;
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
 * 作者：${hengxinyongli} on 2017/2/15 0015 10:00
 * <p>
 * 邮箱：3244345578@qq.com
 */
public class PerfectInformationAdapter extends BaseRecyclerAdapter<PerfectInformationAdapter.ViewHolder,AuthenticationinformationBean> {

    private Activity mActivity;

    public PerfectInformationAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.perfect_information_item_list, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
         Glide.with(mActivity)
                 .load(item.getLogo())
                 .placeholder(R.drawable.image_default)
                 .error(R.drawable.image_default)
                 .into(holder.mIvAuthentication);//设置图片
         holder.mTvTitle.setText(item.getTitle());
         if (item.getTitle_mark() != null) {
             holder.mTvTag.setText(Html.fromHtml(item.getTitle_mark()));
          } else {
             holder.mTvTag.setText("");
         }
         holder.mTvInfo.setText(item.getSubtitle());
         holder.mTvStatus.setText(Html.fromHtml(item.getOperator()));


    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_authentication)
        ImageView mIvAuthentication;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_tag)
        TextView mTvTag;
        @BindView(R.id.tv_info)
        TextView mTvInfo;
        @BindView(R.id.iv_enter)
        ImageView mIvEnter;
        @BindView(R.id.tv_status)
        TextView mTvStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
