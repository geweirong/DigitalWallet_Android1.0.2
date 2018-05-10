package com.innext.szqb.ui.authentication.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.dialog.AlertFragmentDialog;
import com.innext.szqb.ui.authentication.adapter.SelectPicAdapter;
import com.innext.szqb.ui.authentication.bean.PicListBean;
import com.innext.szqb.ui.authentication.bean.SelectPicBean;
import com.innext.szqb.ui.authentication.contract.UpLoadPictureContract;
import com.innext.szqb.ui.authentication.presenter.UpLoadPicturePresenter;
import com.innext.szqb.util.ConvertUtil;
import com.innext.szqb.util.view.StatusViewUtil;
import com.innext.szqb.util.view.StatusViewUtil.IOnTouchRefresh;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.util.Tool;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.innext.szqb.util.common.ToastUtil.showToast;


public class UpLoadPictureActivity extends BaseActivity<UpLoadPicturePresenter> implements UpLoadPictureContract.View
    ,SelectPicAdapter.DeletePhotoListener{

    /**********
     * 上传图片
     */
    private SelectPicAdapter adapter;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.recycler_img)
    RecyclerView mRecyclerImg;
    @BindView(R.id.btn_upload)
    Button mBtnUpload;
    private int max_upload_pic = 3;
    //key值
    public static final String TAG_UPLOAD_KEY = "uploadtype";

    public static final String KEY_UPLOAD_BADGE = "6";//工牌
    //上传类型
    private String uploadType = KEY_UPLOAD_BADGE;

    private SelectPicBean selectPicBean;
    private int mPosition;
    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_pic;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle("");
        adapter = new SelectPicAdapter();
        adapter.setDeletePhotoListener(this);
        mRecyclerImg.setLayoutManager(new GridLayoutManager(mContext,3));
        mRecyclerImg.setAdapter(adapter);
        if (!TextUtils.isEmpty(getIntent().getStringExtra(TAG_UPLOAD_KEY))) {
            uploadType = getIntent().getStringExtra(TAG_UPLOAD_KEY);
        }
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {

            @Override
            public void onItemClick(View view, int position) {
                mPosition = position;
                selectPicBean = adapter.getData().get(position);
                if (selectPicBean.getType() == SelectPicBean.Type_TakePhoto || selectPicBean.getType() == SelectPicBean.Type_Add) {
                    if (adapter.getData().size() - 1 == max_upload_pic) {
                        showToast("已超过图片上传个数");
                        return;
                    }
                    Intent intent = new Intent(UpLoadPictureActivity.this, TakePhotoActivity.class);
                    startActivityForResult(intent, TakePhotoActivity.RESULT_CODE);
                } else //展示大图
                {
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < adapter.getData().size(); i++) {
                        if (adapter.getData().get(i).getType() != SelectPicBean.Type_TakePhoto && adapter.getData().get(i).getType() != SelectPicBean.Type_Add)
                            list.add(adapter.getData().get(i).getUrl());
                    }
                    View img = view.findViewById(R.id.iv_pic);
                    ShowPictureActivity.startAction(mActivity,img,list,position);
                }
            }
        });
        mPresenter.getPicList(uploadType);
    }

    private boolean isChange(){
        for (int i = 0; i < adapter.getData().size(); i++) {
            int type = adapter.getData().get(i).getType();
            if (type!=SelectPicBean.Type_Uploaded&&
                    type!=SelectPicBean.Type_Add&&
                    type!=SelectPicBean.Type_TakePhoto){
                return true;
            }
        }
        return false;
    }

    /**********
     * 上传图片
     */
    private void upLoadPic() {
        List<SelectPicBean> items = adapter.getData();
        for (int i = 0; i < items.size(); i++) {
            SelectPicBean item = items.get(i);
            if (item.getType() == SelectPicBean.Type_None || item.getType() == SelectPicBean.Type_UploadFailed) {
                selectPicBean = item;
                item.setType(SelectPicBean.Type_Uploading);
                adapter.notifyItemChanged(i);
                mPresenter.uploadPic(item,Integer.valueOf(uploadType));
                if (i == items.size() - 1) {
                    //打开button开关
                    mBtnUpload.setClickable(true);
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==TakePhotoActivity.RESULT_CODE){
            if (data != null) {
                String url = data.getStringExtra(TakePhotoActivity.PHOTO_TAG);
                if (selectPicBean==null&&adapter.getData().size()>0){
                    selectPicBean = adapter.getData().get(mPosition);
                }
                if (adapter.getData().size()>0&&adapter.getData().size()<max_upload_pic){
                    selectPicBean.setType(SelectPicBean.Type_Add);
                    SelectPicBean bean = new SelectPicBean();
                    bean.setUrl(url);
                    bean.setType(SelectPicBean.Type_None);
                    adapter.getData().add(adapter.getData().size()-1,bean);
                }else{
                    selectPicBean.setType(SelectPicBean.Type_None);
                    selectPicBean.setUrl(url);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    /*********
     * 针对网络的处理
     *
     */
    private void connectException(String style) {
        StatusViewUtil.showDefaultPopWin(this,new IOnTouchRefresh() {

            @Override
            public void refresh() {
                loadData();
            }
        }, style, null);
    }

    @Override
    public void onBackPressed() {
        if (isChange()) {
            new AlertFragmentDialog.Builder(this)
                    .setContent("有图片未上传,是否继续返回?")
                    .setLeftBtnText("取消")
                    .setRightBtnText("确定")
                    .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                        @Override
                        public void dialogRightBtnClick() {
                            finish();
                        }
                    }).build();
        } else {
            super.onBackPressed();
        }
    }


    @OnClick(R.id.btn_upload)
    public void onClick() {
        if (Tool.isFastDoubleClick()) return;
        if (isChange()){
            //屏蔽多次上传问题
            mBtnUpload.setClickable(false);
            upLoadPic();
        }else{
            ToastUtil.showToast("没有可上传图片");
        }
    }

    @Override
    public void getPicListSuccess(PicListBean data) {
        mTitle.setTitle(true, new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        },data.getTitle());
        mTvTip.setText(data.getNotice());
        if (data.getData() == null
                || data.getData().size() == 0) {
            SelectPicBean baseBean = new SelectPicBean();
            baseBean.setType(SelectPicBean.Type_TakePhoto);
            data.getData().add(baseBean);
        } else if (data.getData().size()<data.getMax_pictures()){
            SelectPicBean baseBean = new SelectPicBean();
            baseBean.setType(SelectPicBean.Type_Add);
            data.getData().add(baseBean);
        }
        adapter.addData(data.getData());
        max_upload_pic = data.getMax_pictures();
        StatusViewUtil.hidePopWin();
    }

    @Override
    public void uploadPicSuccess(SelectPicBean info) {
        info.setType(SelectPicBean.Type_Uploaded);
        adapter.notifyDataSetChanged();
        upLoadPic();
    }

    @Override
    public void showLoading(String content) {
        App.loadingContent(this,content);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        ToastUtil.showToast(msg);
        if (type.equals(mPresenter.TYPE_GET_PIC)){
            connectException(StatusViewUtil.TAG_POP_STYLE_NOCONNECT);
        }else if (type.equals(mPresenter.TYPE_UPLOAD_PIC)){
            selectPicBean.setType(SelectPicBean.Type_UploadFailed);
            adapter.notifyDataSetChanged();
            //屏蔽多次上传问题
            mBtnUpload.setClickable(true);
        }
    }

    @Override
    public void deletePhoto(final int position) {
        new AlertFragmentDialog.Builder(this)
                .setContent("是否要删除？")
                .setLeftBtnText("取消")
                .setRightBtnText("删除")
                .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick() {
                        ConvertUtil.deleteFile(adapter.getData().get(position).getUrl());
                        adapter.getData().remove(position);
                        if(adapter.getData().size()<max_upload_pic){
                            for(int i=0;i<adapter.getData().size();i++){
                                //getType=1:添加状态
                                if(adapter.getData().get(i).getType()==1){
                                    adapter.getData().remove(i);
                                }
                            }
                            SelectPicBean baseBean = new SelectPicBean();
                            baseBean.setType(SelectPicBean.Type_Add);
                            adapter.getData().add(baseBean);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).build();
    }
}
