package com.innext.szqb.ui.authentication.adapter;

import android.app.Activity;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.innext.szqb.R;
import com.innext.szqb.config.Constant;
import com.innext.szqb.widget.TouchImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * author:hengxinyongli
 */
public class ShowPicturePagerAdapter extends PagerAdapter {
    private List<View> views=new ArrayList<>();
    private Activity mActivity;
    private View.OnClickListener listener;
    public ShowPicturePagerAdapter(Activity mActivity) {
        this.mActivity=mActivity;
    }
    public void setImageViewOnclick(View.OnClickListener listener){
        this.listener=listener;
    }
    public void setData(List<String> urls){
        views.clear();
        for (int i = 0; i < urls.size(); i++) {
            View view= LayoutInflater.from(mActivity).inflate(R.layout.list_item_circular_img,null);
            TouchImageView imageView= (TouchImageView) view.findViewById(R.id.tiv_content);
            imageView.setOnClickListener(listener);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setTransitionName(Constant.TRANSITION_ANIMATION_SHOW_PIC+i);
            }
            Glide.with(mActivity)
                    .load(urls.get(i))
                    .placeholder(R.drawable.image_default)
                    .into(imageView);
            views.add(view);
        }
        notifyDataSetChanged();
    }
    // 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
    @Override
    public int getCount() {
        return views.size();
    }

    // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView(views.get(position));
    }
    // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        view.addView(views.get(position));
        return views.get(position);
    }
}

