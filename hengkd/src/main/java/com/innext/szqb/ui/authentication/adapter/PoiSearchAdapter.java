package com.innext.szqb.ui.authentication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.innext.szqb.R;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PoiSearchAdapter extends BaseRecyclerAdapter<PoiSearchAdapter.ViewHolder,PoiItem> {

    @Override
    public PoiSearchAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_poi_item,parent,false));
    }

    @Override
    public void mOnBindViewHolder(PoiSearchAdapter.ViewHolder holder, int position) {
        holder.mTvTitle.setText(item.getTitle());
        holder.mTvAddress.setText(item.getSnippet());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_address)
        TextView mTvAddress;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}


