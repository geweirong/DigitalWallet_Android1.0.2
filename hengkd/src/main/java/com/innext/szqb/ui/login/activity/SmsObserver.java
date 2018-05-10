package com.innext.szqb.ui.login.activity;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.innext.szqb.app.App;
import com.innext.szqb.util.common.LogUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsObserver extends ContentObserver {

    public String verifyNum = "";
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    public static final int SEND_VERIFY_NUM = 10;
    private Handler handler;

    public SmsObserver(Handler handler) {
        super(handler);
        this.handler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
//		super.onChange(selfChange);
        LogUtils.loge("selfChange=" + selfChange);
        // 每当有新短信到来时，使用我们获取短消息的方法
        try {
            getSmsFromPhone();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    public void getSmsFromPhone() {
        ContentResolver cr = App.getContext().getContentResolver();
        String[] projection = new String[]{"body"};
        String where = "date >" + (System.currentTimeMillis() - 5 * 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return;
        if (cur.moveToNext()) {
            String body = cur.getString(cur.getColumnIndex("body"));
            if (body != null && body.contains(App.getAPPName()) && body.contains("验证码")) {
                Pattern p = Pattern.compile("[0-9.]{4,6}"); // java默认的为贪婪匹配模式
                Matcher matcher = p.matcher(body);
                int count = 0;
                while (matcher.find()) {
                    count++;
                    verifyNum = matcher.group();
                }
                // 如果只有一个，那么直接就是了
                if (count == 1) {
                    handler.sendEmptyMessage(SEND_VERIFY_NUM);
                } else {
                    matcher.reset();
                    verifyNum = "";
                    while (matcher.find()) {
                        // 包含.为金额，不行；
                        if (!verifyNum.contains(".")) {
                            // 判断之前4位字符，需要包含“码”字
                            if (matcher.start() > 3 && body.substring(matcher.start() - 4, matcher.start()).contains("码")) {
                                verifyNum = matcher.group();
                                break;
                            } else if (matcher.start() == 3 && body.substring(matcher.start() - 3, matcher.start()).contains("码")) {
                                // 判断之前3位字符，需要包含“码”字，如：验证码786523
                                verifyNum = matcher.group();
                                break;
                            }
                        }
                    }
                    handler.sendEmptyMessage(SEND_VERIFY_NUM);
                }
            }
        }
        cur.close();
    }
}
