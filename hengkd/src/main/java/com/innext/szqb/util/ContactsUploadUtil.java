package com.innext.szqb.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.innext.szqb.app.App;
import com.innext.szqb.ui.authentication.bean.ContactBean;
import com.innext.szqb.ui.lend.presenter.UploadContentsPresenter;
import com.innext.szqb.util.check.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ContactsUploadUtil {
	UploadContentsPresenter mPresenter;
	


	/**********
	 * 获取所有联系人(只要手机号码为11位，如果有+86，去除+86。有重复去除重复)
	 * @param context
	 */
	public static List<ContactBean> getAllContacts(Context context)
	{
		List<ContactBean> contacts = new ArrayList<>();//联系人集合
		List<String> phoneList =new ArrayList<>(); //联系人手机号码集合（用于去除重复）
		try {
			String uid = App.getConfig().getUserInfo().getUid() + "";
			Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
					null, null, null, null);
			int contactIdIndex = 0;
			int nameIndex = 0;

			if (cursor.getCount() > 0) {
				contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
				nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			}
			while (cursor.moveToNext()) {
				String contactId = cursor.getString(contactIdIndex);
				String name = cursor.getString(nameIndex);
                /*
	             * 查找该联系人的phone信息
	             */
				Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
						null, null);
				int phoneIndex = 0;
				if (phones.getCount() > 0) {
					phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
				}
				String user_phones = "";//获取用户电话组
				while (phones.moveToNext()) {
					String phoneNumber = StringUtil.toNum(phones.getString(phoneIndex));
					if (!TextUtils.isEmpty(phoneNumber)) {
						if (phoneNumber.length() < 11) {
						} else if (phoneNumber.length() > 11) {
							if (phoneNumber.startsWith("86")) {
								phoneNumber = phoneNumber.substring(2, phoneNumber.length());
								if (phoneNumber.length() == 11) {
									user_phones = phoneNumber;
									break;
								}
							}
						} else {
							if (phoneNumber.startsWith("1")) {
								user_phones = phoneNumber;
								break;
							}
						}
						break;
					}
				}
				if (!TextUtils.isEmpty(user_phones)) {
					if (!phoneList.contains(user_phones)){//去除重复
						phoneList.add(user_phones);

						ContactBean bean = new ContactBean();
						bean.setName(name);
						bean.setUser_id(uid);
						bean.setMobile(user_phones);
						bean.setStatus(0);
						contacts.add(bean);
					}
				}
				if (!phones.isClosed())
					phones.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return contacts;
	}

}
