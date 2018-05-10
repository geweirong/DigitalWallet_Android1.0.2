package com.innext.szqb.widget

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.innext.szqb.R
import com.youth.banner.loader.ImageLoader

/**
 * @author      : yan
 * @date        : 2018/1/19 11:41
 * @description : banner图片加载器
 */
class BannerImageLoader : ImageLoader() {

    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        /**
         * 注意：
         * 1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         * 2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         * 传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         * 切记不要胡乱强转！
         */
        Glide.with(context).load(path.toString())
            .placeholder(R.mipmap.banner)
            .error(R.mipmap.banner)
            .centerCrop()
            .into(imageView) //设置图片
    }

    //        //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
    //        @Override
    //        public ImageView createImageView(Context context) {
    //            //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
    //            ImageView simpleDraweeView = new ImageView(context);
    //
    //            return simpleDraweeView;
    //        }
}