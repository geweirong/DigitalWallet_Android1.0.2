package com.innext.szqb.ui.authentication.activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.base.PermissionsListener;
import com.innext.szqb.dialog.PickerViewFragmentDialog;
import com.innext.szqb.events.AuthenticationRefreshEvent;
import com.innext.szqb.ui.authentication.bean.ContactBean;
import com.innext.szqb.ui.authentication.bean.MyRelationBean;
import com.innext.szqb.ui.authentication.bean.RelationBean;
import com.innext.szqb.ui.authentication.contract.EmergencyContactContract;
import com.innext.szqb.ui.authentication.presenter.EmergencyContactPresenter;
import com.innext.szqb.ui.lend.contract.UploadContentsContract;
import com.innext.szqb.ui.lend.presenter.UploadContentsPresenter;
import com.innext.szqb.util.ContactsUploadUtil;
import com.innext.szqb.util.ConvertUtil;
import com.innext.szqb.util.view.StatusViewUtil;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.util.Tool;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/*
 * 紧急联系人
 * hengxinyongli
 */
public class AuthEmergencyContactActivity extends BaseActivity<EmergencyContactPresenter> implements EmergencyContactContract.View
,UploadContentsContract.View{


    @BindView(R.id.tv_relation)
    TextView mTvRelation;
    @BindView(R.id.tv_contact_name)
    TextView mTvContactName;
    @BindView(R.id.tv_relation2)
    TextView mTvRelation2;
    @BindView(R.id.tv_contact_name2)
    TextView mTvContactName2;
    //上传联系人列表
    private UploadContentsPresenter uploadContentsPresenter;

    /*********
     * 获取关系列表
     */
    private MyRelationBean bean = null;
    private List<RelationBean> relations = null;
    private List<RelationBean> relations2 = null;
    private int CODE_GET_CONTACT = 110;
    private int CODE_GET_CONTACT2 = 111;

    private String message = "紧急联系人手机号码有误，请重新选择";
    private String select_contact_name = "";
    private String select_contact_phone = "";
    private String select_contact_name2 = "";
    private String select_contact_phone2 = "";

    /********
     * 选择与本人的关系
     */
    private int curPos = 0;
    private int curPos2 = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_auth_contact;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
        uploadContentsPresenter = new UploadContentsPresenter();
        uploadContentsPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle("紧急联系人");
        mPresenter.getContacts();
    }


    private void initLisenters() {
        mTvContactName.addTextChangedListener(changeText);
        mTvContactName2.addTextChangedListener(changeText);
        mTvRelation.addTextChangedListener(changeText);
        mTvRelation2.addTextChangedListener(changeText);
    }

    TextWatcher changeText = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {
            showSaveBtn();
        }
    };

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (arg0 == CODE_GET_CONTACT || arg0 == CODE_GET_CONTACT2) {
            if (arg1 == RESULT_OK) {
                Uri contactData = arg2.getData();
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(contactData, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        getContactPhoneInfo(cursor, arg0);
                        if (!cursor.isClosed()) {
                            cursor.close();
                        }
                    } else {
                        toAppSettings("通讯录权限已被禁止", false);
                    }
                } catch (SecurityException e) {
                    ToastUtil.showToast("无法获取系统通讯录");
                }
            }
        }
        super.onActivityResult(arg0, arg1, arg2);
    }


    private void getContactPhoneInfo(Cursor cursor, int code) {
        int phoneColumn = cursor
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);

        if (phoneNum > 0) {
            // 获得联系人的ID号  
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人电话的cursor  
            Cursor phone = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + contactId, null, null);
            if (phone==null){
                ToastUtil.showToast("通讯录获取失败");
                return;
            }
            if (phone.moveToFirst()) {
                //为了防止用户关闭联系人权限，所以在选择到联系人时就开始上传联系人
                if (!isLoadContact){
                    uploadContact();
                }
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    // 取得联系人名字   
                    int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                    if (TextUtils.isEmpty(phone.getString(index))) {
                        ToastUtil.showToast("联系人手机号为空，请重新选择");
                        if (code == CODE_GET_CONTACT) {
                            select_contact_name = "";
                            select_contact_phone = "";
                            mTvContactName.setText(select_contact_name);
                        } else if (code == CODE_GET_CONTACT2) {
                            select_contact_name2 = "";
                            select_contact_phone2 = "";
                            mTvContactName2.setText(select_contact_name2);
                        }
                        if (!phone.isClosed()) {
                            phone.close();
                        }
                        return;
                    } else {
                        String mPhone = StringUtil.toNum(phone.getString(index));
                        if (mPhone.length() < 11) {
//                            if (mPhone.length() == 10 && mPhone.startsWith("0")) {
//
//                            } else {
//                                ToastUtil.showToast(message);
//                                return;
//                            }
                            ToastUtil.showToast(message);
                        } else if (mPhone.length() > 11) {
                            if (!mPhone.startsWith("86")) {
                                ToastUtil.showToast(message);
                                return;
                            } else {
                                mPhone = mPhone.substring(2, mPhone.length());
                                if (mPhone.length() != 11) {
                                    ToastUtil.showToast(message);
                                    return;
                                }
                            }
                        } else {
                            if (!mPhone.startsWith("1") && !mPhone.startsWith("0")) {
                                ToastUtil.showToast(message);
                                return;
                            }
                        }
                        if (code == CODE_GET_CONTACT) {
                            select_contact_name = cursor.getString(nameFieldColumnIndex);
                            select_contact_phone = mPhone;
                            mTvContactName.setText(select_contact_name);
                        } else if (code == CODE_GET_CONTACT2) {
                            select_contact_name2 = cursor.getString(nameFieldColumnIndex);
                            select_contact_phone2 = mPhone;
                            mTvContactName2.setText(select_contact_name2);
                        }
                    }
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            } else {
                ToastUtil.showToast(message);
            }
        } else {
            ToastUtil.showToast(message);
        }

    }

    private void chooseRelation(final int code) {
        Log.e("这里是日志","输出日志信息code===="+code);
        if (relations == null || relations2 == null) {
            mPresenter.getContacts();
        } else {
            if (code == CODE_GET_CONTACT) {
                Log.e("这里是日志","输出日志信息===="+relations.size());
                ArrayList<String> items = new ArrayList<>();
                for (int i = 0; i < relations.size(); i++)
                    items.add(relations.get(i).getName());

                new PickerViewFragmentDialog(curPos, items, new PickerViewFragmentDialog.OnValueSelectListener() {
                    @Override
                    public void select(String value, int position) {
                        curPos = position;
                        mTvRelation.setText(value);
                    }
                }).show(getSupportFragmentManager(),PickerViewFragmentDialog.TAG);
            } else if (code == CODE_GET_CONTACT2) {

                ArrayList<String> items = new ArrayList<String>();
                for (int i = 0; i < relations2.size(); i++)
                    items.add(relations2.get(i).getName());
                new PickerViewFragmentDialog(curPos2, items, new PickerViewFragmentDialog.OnValueSelectListener() {
                    @Override
                    public void select(String value, int position) {
                        curPos2 = position;
                        mTvRelation2.setText(value);
                    }
                }).show(getSupportFragmentManager(),PickerViewFragmentDialog.TAG);
            }
        }
    }


    @OnClick({R.id.rl_relation, R.id.rl_getcontact, R.id.rl_relation2, R.id.rl_getcontact2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_relation:
                chooseRelation(CODE_GET_CONTACT);
                break;
            case R.id.rl_getcontact:
                getSelfContact(CODE_GET_CONTACT);
                break;
            case R.id.rl_relation2:
                chooseRelation(CODE_GET_CONTACT2);
                break;
            case R.id.rl_getcontact2:
                getSelfContact(CODE_GET_CONTACT2);
                break;
        }
    }

    /**
     * 显示保存按钮
     */
    private void showSaveBtn() {
        mTitle.setRightTitle("保存", new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEmergeencyContact();
            }
        });
    }

    /*********
     * 保存联系人信息
     */
    private void saveEmergeencyContact() {
        if (StringUtil.isBlank(select_contact_name) || StringUtil.isBlank(select_contact_phone)) {
            ToastUtil.showToast("请选择直系亲属紧急联系人");
            return;
        }
        if (StringUtil.isBlankEdit(mTvRelation)) {
            ToastUtil.showToast("请选择直系亲属紧急联系人关系");
            return;
        }
        if (StringUtil.isBlank(select_contact_name2) || StringUtil.isBlank(select_contact_phone2)) {
            ToastUtil.showToast("请选择其他联系人");
            return;
        }
        if (StringUtil.isBlankEdit(mTvRelation2)) {
            ToastUtil.showToast("请选择其他联系人关系");
            return;
        }
        //判定两个联系人是否是同一个
        if (select_contact_name.equals(select_contact_name2) || select_contact_phone.equals(select_contact_phone2)) {
            ToastUtil.showToast("紧急联系人不能是同一个人!");
            return;
        }
        if (relations == null || relations2 == null) {
            ToastUtil.showToast("加载数据异常,请稍后再试");
            return;
        }
        mPresenter.saveContacts(String.valueOf(relations.get(curPos).getType()),
                select_contact_phone.trim(),
                select_contact_name.trim(),
                String.valueOf(relations2.get(curPos2).getType()),
                select_contact_phone2.trim(),
                select_contact_name2.trim());
    }


    /**
     * 跳转系统联系人列表
     * @param code
     */
    private void getSelfContact(final int code) {
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, new PermissionsListener() {
            @Override
            public void onGranted() {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, code);
            }

            @Override
            public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
                if (isNeverAsk) {
                    toAppSettings("通讯录权限已被禁止", false);
                }
            }
        });
    }
    @Override
    public void getContactsSuccess(MyRelationBean result) {
        bean = result;
        relations = bean.getLineal_list();
        relations2 = bean.getOther_list();
        setData();
        initLisenters();
        StatusViewUtil.hidePopWin();
    }
    private void setData() {
        mTvContactName.setText(bean.getLineal_name());
        select_contact_name = bean.getLineal_name();
        select_contact_phone = bean.getLineal_mobile();
        mTvContactName2.setText(bean.getOther_name());
        select_contact_name2 = bean.getOther_name();
        select_contact_phone2 = bean.getOther_mobile();
        for (int i = 0; i < relations.size(); i++) {
            RelationBean item = relations.get(i);
            if(!Tool.isBlank(bean.getLineal_relation())){
                if (Integer.valueOf(bean.getLineal_relation()) == item.getType()) {
                    curPos = i;
                    mTvRelation.setText(item.getName());
                }
            }

        }
        for (int i = 0; i < relations2.size(); i++) {
            RelationBean item = relations2.get(i);
            if(!Tool.isBlank(bean.getOther_relation())){
                if (Integer.valueOf(bean.getOther_relation()) == item.getType()) {
                    curPos2 = i;
                    mTvRelation2.setText(item.getName());
                }
            }
        }
    }
    @Override
    public void saveContactsSuccess() {
        EventBus.getDefault().post(new AuthenticationRefreshEvent());
        ToastUtil.showToast("保存成功");
        finish();
    }

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)){
            App.loadingContent(mActivity, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }
    //联系人列表是否上传成功
    private boolean isLoadContact;
    @Override
    public void showErrorMsg(String msg, String type) {
        if (type.equals(uploadContentsPresenter.TYPE_CONTACT)){
            Log.e("uploadContact","联系人列表上传失败");
            return;
        }else if (type.equals(mPresenter.TYPE_GET_CONTACT)){
            connectException(StatusViewUtil.TAG_POP_STYLE_NOCONNECT);
        }else if (type.equals(mPresenter.TYPE_SAVE_CONTACT)){
            ToastUtil.showToast(msg);
        }
    }

    @Override
    public void uploadSuccess(String type) {
        isLoadContact = true;
        Log.e("uploadContact","联系人列表上传成功");
    }


    /*********
     * 针对网络的处理
     *
     * @param style
     */
    private void connectException(String style) {
        StatusViewUtil.showDefaultPopWin(this,new StatusViewUtil.IOnTouchRefresh() {

            @Override
            public void refresh() {
                mPresenter.getContacts();
                mTitle.setRightTitle("",null);
            }
        }, style, null);
    }

    /*************
     * 上传联系人
     */
    public  void uploadContact()
    {
        Observable.create(new Observable.OnSubscribe<List<ContactBean>>() {
            @Override
            public void call(Subscriber<? super List<ContactBean>> subscriber) {
                List<ContactBean> list = ContactsUploadUtil.getAllContacts(mContext);
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ContactBean>>() {
                    @Override
                    public void call(List<ContactBean> contactBeen) {
                        if (contactBeen!=null&&contactBeen.size()>0){
                            String data = ConvertUtil.toJsonString(contactBeen);
                            uploadContentsPresenter.toUploadContents(uploadContentsPresenter.TYPE_CONTACT,data);
                        }
                    }
                });
    }
}
