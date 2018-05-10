package com.innext.szqb.ui.my.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.ui.my.bean.TransactionRecordListBean;
import com.innext.szqb.ui.repayment.activity.RepaymentDetailActivity;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hengxinyongli on 2017/2/15 0015.
 * 描述：借款记录适配器
 */

public class TransactionRecordAdapter extends BaseRecyclerAdapter<TransactionRecordAdapter.ViewHolder, TransactionRecordListBean> {

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.transaction_record_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, final int position) {
//        final TransactionRecordListBean bean = getItem(position);
        holder.tv_transaction_title.setText(data.get(position).getTitle());
        holder.tv_transaction_time.setText(data.get(position).getTime());
        holder.tv_transaction_status.setText(Html.fromHtml(data.get(position).getText()));
        /*
         *   【优化】1.去掉借款记录的二级页面 2.将借款记录由具体的借款订单修改为仅显示借款的记录
         *    2.0.2  1.26
         */
        holder.rl_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, BorrowingRecordActivity.class);
//                intent.putExtra("title", "");
//                intent.putExtra("url", data.get(position).getUrl());
//                mContext.startActivity(intent);
//                intent.putExtra(Constant.POOL_ID, data.get(position).getPoolId());
//                mContext.startActivity(intent);

                Intent intent = new Intent(mContext, RepaymentDetailActivity.class);
                intent.putExtra("poolId",data.get(position).getPoolId());
                intent.putExtra("assetOrderId","");
                mContext.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_transaction)
        RelativeLayout rl_transaction;
        @BindView(R.id.tv_transaction_title)
        TextView tv_transaction_title;
        @BindView(R.id.tv_transaction_time)
        TextView tv_transaction_time;
        @BindView(R.id.tv_transaction_status)
        TextView tv_transaction_status;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
