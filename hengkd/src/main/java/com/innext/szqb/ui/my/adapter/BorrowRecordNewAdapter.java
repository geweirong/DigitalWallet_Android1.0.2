package com.innext.szqb.ui.my.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.ui.my.bean.AssetRepaymentList;
import com.innext.szqb.ui.my.bean.BorrowStatus;
import com.innext.szqb.util.TimeUtil;
import com.innext.szqb.util.Tool;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HX0010637 on 2018/5/7.
 *  /**
 * (0:待初审(待机审);
 * -3:初审驳回;
 * 1:初审通过;
 * -4:复审驳回;
 * 20:复审通过,待放款;
 * -5:放款驳回;
 * 22:放款中;
 * -10:放款失败;
 * 21已放款，还款中;
 * 23:部分还款;
 * 30:已还款;
 * -11:已逾期;
 * -20:已坏账，
 * 34逾期已还款；
 *
 * 30,34:已还款 可点击
 * -11,21：待还款
 */

public class BorrowRecordNewAdapter extends BaseRecyclerAdapter<BorrowRecordNewAdapter.ViewHolder, AssetRepaymentList> {
    private SubClickListener mOnItemClickListener;
    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_borrowing_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, final int position) {
        AssetRepaymentList mList = data.get(position);
        holder.mTvPeriod.setText("第" +mList.getPeriod()+ "期");
        holder.mTvRepaymentAmount.setText("￥"+ Tool.toDeciDouble2(mList.getRepaymentAmount()/100.00));
        holder.mTvRepaymentTime.setText("请于"+TimeUtil.formatData(TimeUtil.dateFormatMDofChinese,
                Long.parseLong(mList.getRepaymentTime())) + "日前还款");
        if (mList.getStatus() == -11 || mList.getStatus() == -21){  // * -11:已逾期;
            holder.mTvStatus.setVisibility(View.VISIBLE);
            holder.mTvStatus.setText("已逾期");
        }else if (mList.getStatus() == 30 || mList.getStatus() == 34){//已还款
            holder.mTvStatus.setVisibility(View.VISIBLE);
            holder.mTvStatus.setText("已还清");
        }else {
            holder.mTvStatus.setVisibility(View.GONE);
        }
        if (mList.getSelected()) {
            holder.mCheckBox.setImageResource(R.mipmap.ic_borrowing_record_select);
        } else {
            holder.mCheckBox.setImageResource(R.mipmap.ic_borrowing_record_normal);
        }

        if (mList.getStatus() == -11 || mList.getStatus() == 21){  //待还款
            holder.mCheckBox.setEnabled(true);
        }else {
            holder.mCheckBox.setEnabled(false);
            return;
        }
            holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != 0)
                mOnItemClickListener.OntopicClickListener(position,data);
            }
        });
    }
    public void setsubClickListener(SubClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public interface SubClickListener {
        void OntopicClickListener(int pos,List<AssetRepaymentList> myLiveList);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_borrowing_record_iv_status)
        ImageView mCheckBox;
        @BindView(R.id.tv_period)
        TextView mTvPeriod;
        @BindView(R.id.tv_repaymentAmount)
        TextView mTvRepaymentAmount;
        @BindView(R.id.tv_repaymentTime)
        TextView mTvRepaymentTime;
        @BindView(R.id.tv_status)
        TextView mTvStatus;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
