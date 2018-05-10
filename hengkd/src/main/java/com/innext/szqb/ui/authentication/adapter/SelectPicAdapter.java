package com.innext.szqb.ui.authentication.adapter;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.innext.szqb.R;
import com.innext.szqb.config.Constant;
import com.innext.szqb.ui.authentication.bean.SelectPicBean;
import com.innext.szqb.util.ConvertUtil;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.util.Tool;
import com.innext.szqb.util.view.ViewUtil;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectPicAdapter extends BaseRecyclerAdapter<SelectPicAdapter.ViewHolder,SelectPicBean> {
    @Override
    public SelectPicAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_upload_pic_item,parent,false);
        //recycleView左右边距13dp,每行3个item,每个item左右边距2dp,所以要减去13*2+2*2*3
        int size = (ViewUtil.getScreenWidth(mContext)- Tool.dip2px(mContext,26+12))/3;
        view.setLayoutParams(new RecyclerView.LayoutParams(size,size));
        return new ViewHolder(view);
    }
    @Override
    public void mOnBindViewHolder(final ViewHolder holder, int position) {

        switch (item.getType()){
            case SelectPicBean.Type_None:
                holder.mTvStatus.setVisibility(View.GONE);
                holder.mIvDelete.setVisibility(View.VISIBLE);
                break;
            case SelectPicBean.Type_Add:
                holder.mTvStatus.setVisibility(View.GONE);
                holder.mIvDelete.setVisibility(View.GONE);
                holder.mIvPic.setImageResource(R.mipmap.icon_add_photo);
                int px = ConvertUtil.dip2px(mContext, 15);
                holder.mIvPic.setPadding(px, px, px, px);
                break;
            case SelectPicBean.Type_TakePhoto:
                holder.mTvStatus.setVisibility(View.GONE);
                holder.mIvDelete.setVisibility(View.GONE);
                holder.mIvPic.setImageResource(R.mipmap.icon_take_photo);
                int padding = ConvertUtil.dip2px(mContext, 15);
                holder.mIvPic.setPadding(padding, padding, padding, padding);
                break;
            case SelectPicBean.Type_Uploaded:
                holder.mIvDelete.setVisibility(View.GONE);
                holder.mTvStatus.setVisibility(View.VISIBLE);
                holder.mTvStatus.setText("已上传");
                break;
            case SelectPicBean.Type_Uploading:
                holder.mIvDelete.setVisibility(View.GONE);
                holder.mTvStatus.setVisibility(View.VISIBLE);
                holder.mTvStatus.setText("上传中...");
                break;
            case SelectPicBean.Type_UploadFailed:
                holder.mIvDelete.setVisibility(View.VISIBLE);
                holder.mTvStatus.setVisibility(View.VISIBLE);
                holder.mTvStatus.setText("上传失败");
                break;
        }

        if (!StringUtil.isBlank(item.getUrl())) {
            holder.mIvPic.setPadding(1, 1, 1, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.mIvPic.setTransitionName(Constant.TRANSITION_ANIMATION_SHOW_PIC+position);
            }
            Glide.with(mContext)
                    .load(item.getUrl())
                    .centerCrop()
                    .placeholder(R.drawable.image_default)
                    .error(R.drawable.image_default)
                    .crossFade()
                    .into(holder.mIvPic);
        }
        holder.deleteItemClickListener=new DeleteItemClickListener(position);
        holder.mIvDelete.setOnClickListener(holder.deleteItemClickListener);
    }

    private class DeleteItemClickListener implements OnClickListener {
        private int position;

        private DeleteItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(deletePhoto!=null){
                deletePhoto.deletePhoto(position);
            }
        }
    }
    private DeletePhotoListener deletePhoto;

    public void setDeletePhotoListener(DeletePhotoListener deletePhoto) {
        this.deletePhoto = deletePhoto;
    }
    public interface  DeletePhotoListener{
        void deletePhoto(int position);
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_pic)
        ImageView mIvPic;
        @BindView(R.id.tv_status)
        TextView mTvStatus;
        @BindView(R.id.iv_delete)
        ImageView mIvDelete;
        DeleteItemClickListener deleteItemClickListener;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
