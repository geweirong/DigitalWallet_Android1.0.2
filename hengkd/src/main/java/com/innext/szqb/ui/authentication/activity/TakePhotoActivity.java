package com.innext.szqb.ui.authentication.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Toast;

import com.innext.szqb.R;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.base.PermissionsListener;
import com.innext.szqb.util.ConvertUtil;
import com.innext.szqb.util.common.FileUtils;
import com.innext.szqb.util.StatusBarUtil;
import com.innext.szqb.util.common.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 需要在清单文件当中配置
 *
 * @author admin
 */
/*  
 * 
 *	<activity
		android:name=".TakePhotoActivity"
		android:label="@string/app_name"
		android:theme="@style/Theme.Translucent"  >
	</activity>
*
*/
public class TakePhotoActivity extends BaseActivity {
    //返回结果ok
    public final static int RESULT_CODE = 1000;
    public final static String PHOTO_TAG = "imageUri";
    public final static int CODE_TAKE_PHOTO = 2;//选择拍照
    public final static int CODE_CHOOSE_ALBUM = 3;//选择相册

    //生成的图片路径
    private String imagePath = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_take_photo;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        StatusBarUtil.setStatusBarColor(this,R.color.transparent_half);
        if(checkSD()){
            //文件路径
            String path = Environment.getExternalStorageDirectory() + "/" + getApplicationInfo().packageName+"/image/";
            File file = new File(path);
            if (!file.exists())
                file.mkdirs();
            //文件名称
            String fileName = System.currentTimeMillis() + ".jpg";
            imagePath = path + fileName;
        }else{
            finish();
        }
    }

    private void saveFile(final String imagePath) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (new File(imagePath).exists()) {//图片是否存在
                    //获取图片旋转度数
                    int degree = ConvertUtil.getBitmapDegree(imagePath);
                    //压缩图片
                    Bitmap bitmap = ConvertUtil.getCompressedBmp(imagePath);
                    //如果旋转度数大于0则进行校正
                    if (degree > 0){
                        bitmap = ConvertUtil.rotateBitmapByDegree(bitmap, degree);
                    }
                    //保存压缩、校正旋转之后的图片(覆盖掉所拍的图片)
                    ConvertUtil.saveBitmap(imagePath, bitmap);
                    if (bitmap != null){
                        bitmap.recycle();//释放内存
                    }
                    subscriber.onNext(imagePath);
                    subscriber.onCompleted();
                }else{
                    subscriber.onError(new IOException("图片保存失败"));
                }
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Intent intent = new Intent();
                intent.putExtra(PHOTO_TAG, s);
                setResult(RESULT_CODE, intent);
                finish();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ToastUtil.showToast(throwable.getMessage());
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CODE_TAKE_PHOTO:// 拍照
                    saveFile(imagePath);
                    break;
                case CODE_CHOOSE_ALBUM:// 相册
                    String selectPath = ConvertUtil.getPath(this, data.getData());
                    if (new File(selectPath).exists()) {
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                        }
                        Bitmap bmp = ConvertUtil.getCompressedBmp(selectPath);
                        ConvertUtil.saveBitmap(imagePath, bmp);
                        if (bmp != null)
                            bmp.recycle();
                    }
                    break;
            }
            Intent intent = new Intent();
            intent.putExtra(PHOTO_TAG, imagePath);
            setResult(RESULT_CODE, intent);
            finish();
        } else {
            finish();
        }
    }

    File mTmpFile;
    // 拍照
    public void camera(View btn) {

        //判断sd是否存在
        if(checkSD()){
            // 照相 和 读取 内存权限
            requestPermissions(new String[]{Manifest.permission.CAMERA}, new PermissionsListener() {
                //授权
                @Override
                public void onGranted() {
                    // 重新构造Uri：content://
                    if (Build.VERSION.SDK_INT >= 24) {

                        File newFile = new File(imagePath);
                        Uri contentUri = FileProvider.getUriForFile(mContext, "com.innext.hkd.fileProvider", newFile);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                        // 授予目录临时共享权限
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        startActivityForResult(intent, CODE_TAKE_PHOTO);
                    } else {


                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (intent.resolveActivity(TakePhotoActivity.this.getPackageManager()) != null) {
                            try {
//                            Log.e("IMG", "mTmpFIle-=-=-=-=-=-=-");
                                mTmpFile = FileUtils.createTmpFile(TakePhotoActivity.this);
//                            Log.e("IMG", "mTmpFIlexxxxxxxxxx");
                            } catch (IOException e) {
//                            Log.e("IMG", "mTmpFIle========ff=" + e.getMessage());
                                e.printStackTrace();
                            }
//                        Log.e("IMG", "mTmpFIle=" + mTmpFile.getAbsolutePath());
                            if (mTmpFile != null && mTmpFile.exists()) {
                                imagePath = mTmpFile.getAbsolutePath();
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
                                startActivityForResult(intent, CODE_TAKE_PHOTO);
                            } else {

                                Toast.makeText(TakePhotoActivity.this, "图片错误", Toast.LENGTH_SHORT).show();

                            }
                        } else {

                            Toast.makeText(TakePhotoActivity.this, "没有系统相机", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                //拒绝
                @Override
                public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
                    if (isNeverAsk) {
                        toAppSettings("相机权限已被禁止", false);
                    }
                }
            });
        }
    }

    private boolean checkSD() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            Toast.makeText(TakePhotoActivity.this, "SD卡不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // 相册选择
    public void selectPhoto(View btn) {
        selectPhotoOld();
    }

    private void selectPhotoOld() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            if (intent.resolveActivity(getPackageManager()) == null) {
                Toast.makeText(TakePhotoActivity.this, "无法打开系统相册", Toast.LENGTH_SHORT).show();
            } else {
                if (Build.VERSION.SDK_INT < 19) {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                }
                startActivityForResult(intent, CODE_CHOOSE_ALBUM);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(TakePhotoActivity.this, "无法打开系统相册", Toast.LENGTH_SHORT).show();
        }
    }

    // 取消
    public void cancel(View btn) {
        close();
    }
    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.in_from_bottom,
                R.anim.out_from_bottom);
    }

    private void close() {
        finish();
        overridePendingTransition(R.anim.in_from_bottom,
                R.anim.out_from_bottom);
    }
    @OnClick(R.id.iv_gap)
    public void onClick() {
        close();
    }
}
