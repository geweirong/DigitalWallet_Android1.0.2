package com.innext.szqb.ui.lend.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.ui.lend.bean.ExpenseDetailBean;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hengxinyongli on 2017/3/7 0007.
 */

public class CostDetailsAdapter extends BaseRecyclerAdapter<CostDetailsAdapter.ViewHolder,ExpenseDetailBean> {

    @Override
    public CostDetailsAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cost_details_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(CostDetailsAdapter.ViewHolder holder, int position) {
        holder.mTvCostType.setText(item.getName()+"：");
        holder.mTvCost.setText(item.getValue()+"元");
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_cost_type)
        TextView mTvCostType;
        @BindView(R.id.tv_cost_)
        TextView mTvCost;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
