package com.innext.szqb.ui.authentication.bean;

import java.util.List;

public class GetWorkInfoBean{
	/**
	 * item : {"company_longitude":"","company_address":"","company_period_list":[{"name":"一年以内","entry_time_type":"1"},{"name":"一到三年","entry_time_type":"2"},{"name":"三到五年","entry_time_type":"3"},{"name":"五年以上","entry_time_type":"4"},{"name":"未知","entry_time_type":"5"}],"company_name":"","company_phone":"","company_address_distinct":"","company_latitude":"","company_picture":"0","company_period":"","company_worktype_list":[{"name":"上班族","work_type_id":"1"},{"name":"自由职业","work_type_id":"2"}]}
	 */

	private ItemBean item;

	public ItemBean getItem() {
		return item;
	}

	public void setItem(ItemBean item) {
		this.item = item;
	}

	public static class ItemBean {
		/**
		 * company_longitude :
		 * company_address :
		 * company_period_list : [{"name":"一年以内","entry_time_type":"1"},{"name":"一到三年","entry_time_type":"2"},{"name":"三到五年","entry_time_type":"3"},{"name":"五年以上","entry_time_type":"4"},{"name":"未知","entry_time_type":"5"}]
		 * company_name :
		 * company_phone :
		 * company_address_distinct :
		 * company_latitude :
		 * company_picture : 0
		 * company_period :
		 * position :
		 * company_worktype_list : [{"name":"上班族","work_type_id":"1"},{"name":"自由职业","work_type_id":"2"}]
		 */

		private String company_longitude;
		private String company_address;
		private String company_name;
		private String company_phone;
		private String company_address_distinct;
		private String company_latitude;
		private String company_picture;
		private String company_period;

		private String position;
		private List<CompanyPeriodListBean> company_period_list;
		private List<CompanyWorktypeListBean> company_worktype_list;

		public String getCompany_longitude() {
			return company_longitude;
		}

		public void setCompany_longitude(String company_longitude) {
			this.company_longitude = company_longitude;
		}

		public String getCompany_address() {
			return company_address;
		}

		public void setCompany_address(String company_address) {
			this.company_address = company_address;
		}

		public String getCompany_name() {
			return company_name;
		}

		public void setCompany_name(String company_name) {
			this.company_name = company_name;
		}

		public String getCompany_phone() {
			return company_phone;
		}

		public String getPosition() {
			return position;
		}

		public void setCompany_phone(String company_phone) {
			this.company_phone = company_phone;
		}
		public void setPosition(String position) {
			this.position = position;
		}

		public String getCompany_address_distinct() {
			return company_address_distinct;
		}

		public void setCompany_address_distinct(String company_address_distinct) {
			this.company_address_distinct = company_address_distinct;
		}

		public String getCompany_latitude() {
			return company_latitude;
		}

		public void setCompany_latitude(String company_latitude) {
			this.company_latitude = company_latitude;
		}

		public String getCompany_picture() {
			return company_picture;
		}

		public void setCompany_picture(String company_picture) {
			this.company_picture = company_picture;
		}

		public String getCompany_period() {
			return company_period;
		}

		public void setCompany_period(String company_period) {
			this.company_period = company_period;
		}

		public List<CompanyPeriodListBean> getCompany_period_list() {
			return company_period_list;
		}

		public void setCompany_period_list(List<CompanyPeriodListBean> company_period_list) {
			this.company_period_list = company_period_list;
		}

		public List<CompanyWorktypeListBean> getCompany_worktype_list() {
			return company_worktype_list;
		}

		public void setCompany_worktype_list(List<CompanyWorktypeListBean> company_worktype_list) {
			this.company_worktype_list = company_worktype_list;
		}

		public static class CompanyPeriodListBean {
			/**
			 * name : 一年以内
			 * entry_time_type : 1
			 */

			private String name;
			private int entry_time_type;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public int getEntry_time_type() {
				return entry_time_type;
			}

			public void setEntry_time_type(int entry_time_type) {
				this.entry_time_type = entry_time_type;
			}
		}

		public static class CompanyWorktypeListBean {
			/**
			 * name : 上班族
			 * work_type_id : 1
			 */

			private String name;
			private int work_type_id;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public int getWork_type_id() {
				return work_type_id;
			}

			public void setWork_type_id(int work_type_id) {
				this.work_type_id = work_type_id;
			}
		}
	}
	/*private ItemBean item;

	public ItemBean getItem() {
		return item;
	}

	public void setItem(ItemBean item) {
		this.item = item;
	}

	public static class ItemBean{
		private String company_name;
		private String company_phone;
		private String company_post;
		private String company_address_distinct;
		private String company_address;
		private int company_salary;
		private int company_period;
		private List<EnterTimeAndSalaryBean> company_salary_list;
		private List<EnterTimeAndSalaryBean> company_period_list;

		public String getCompany_name()
		{
			return company_name;
		}

		public void setCompany_name(String company_name)
		{
			this.company_name = company_name;
		}

		public String getCompany_phone()
		{
			return company_phone;
		}

		public void setCompany_phone(String company_phone)
		{
			this.company_phone = company_phone;
		}

		public String getCompany_post()
		{
			return company_post;
		}

		public void setCompany_post(String company_post)
		{
			this.company_post = company_post;
		}

		public String getCompany_address_distinct()
		{
			return company_address_distinct;
		}

		public void setCompany_address_distinct(String company_address_distinct)
		{
			this.company_address_distinct = company_address_distinct;
		}

		public String getCompany_address()
		{
			return company_address;
		}

		public void setCompany_address(String company_address)
		{
			this.company_address = company_address;
		}

		public int getCompany_salary()
		{
			return company_salary;
		}

		public void setCompany_salary(int company_salary)
		{
			this.company_salary = company_salary;
		}

		public int getCompany_period()
		{
			return company_period;
		}

		public void setCompany_period(int company_period)
		{
			this.company_period = company_period;
		}

		public List<EnterTimeAndSalaryBean> getCompany_salary_list()
		{
			return company_salary_list;
		}

		public void setCompany_salary_list(List<EnterTimeAndSalaryBean> company_salary_list)
		{
			this.company_salary_list = company_salary_list;
		}

		public List<EnterTimeAndSalaryBean> getCompany_period_list()
		{
			return company_period_list;
		}

		public void setCompany_period_list(List<EnterTimeAndSalaryBean> company_period_list)
		{
			this.company_period_list = company_period_list;
		}
	}*/

}
