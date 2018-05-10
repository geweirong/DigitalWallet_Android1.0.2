package com.innext.szqb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 银行卡
 */
public class BankItem implements Parcelable {
	private int bank_id;
	private String bank_name;
	private String url;
	private int card_id;
	private int main_card;
	private String bank_info;
	private String bank_info_name;
	private String bank_info_num;
	public int getBank_id() {
		return bank_id;
	}
	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getCard_id() {
		return card_id;
	}
	public void setCard_id(int card_id) {
		this.card_id = card_id;
	}
	public int getMain_card() {
		return main_card;
	}
	public void setMain_card(int main_card) {
		this.main_card = main_card;
	}
	public String getBank_info() {
		return bank_info;
	}
	public void setBank_info(String bank_info) {
		this.bank_info = bank_info;
	}
	public String getBank_info_name() {
		return bank_info_name;
	}
	public void setBank_info_name(String bank_info_name) {
		this.bank_info_name = bank_info_name;
	}
	public String getBank_info_num() {
		return bank_info_num;
	}
	public void setBank_info_num(String bank_info_num) {
		this.bank_info_num = bank_info_num;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.bank_id);
		dest.writeString(this.bank_name);
		dest.writeString(this.url);
		dest.writeInt(this.card_id);
		dest.writeInt(this.main_card);
		dest.writeString(this.bank_info);
		dest.writeString(this.bank_info_name);
		dest.writeString(this.bank_info_num);
	}

	public BankItem() {
	}

	protected BankItem(Parcel in) {
		this.bank_id = in.readInt();
		this.bank_name = in.readString();
		this.url = in.readString();
		this.card_id = in.readInt();
		this.main_card = in.readInt();
		this.bank_info = in.readString();
		this.bank_info_name = in.readString();
		this.bank_info_num = in.readString();
	}

	public static final Parcelable.Creator<BankItem> CREATOR = new Parcelable.Creator<BankItem>() {
		@Override
		public BankItem createFromParcel(Parcel source) {
			return new BankItem(source);
		}

		@Override
		public BankItem[] newArray(int size) {
			return new BankItem[size];
		}
	};
}
