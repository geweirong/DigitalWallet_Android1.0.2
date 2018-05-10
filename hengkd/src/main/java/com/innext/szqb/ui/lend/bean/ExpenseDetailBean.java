package com.innext.szqb.ui.lend.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hengxinyongli on 2017/3/7 0007.
 */

public class ExpenseDetailBean implements Parcelable{

    private String name;

    private String value;

    protected ExpenseDetailBean(Parcel in) {
        name = in.readString();
        value = in.readString();
    }

    public static final Creator<ExpenseDetailBean> CREATOR = new Creator<ExpenseDetailBean>() {
        @Override
        public ExpenseDetailBean createFromParcel(Parcel in) {
            return new ExpenseDetailBean(in);
        }

        @Override
        public ExpenseDetailBean[] newArray(int size) {
            return new ExpenseDetailBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ExpenseDetailBean{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(value);
    }
}
