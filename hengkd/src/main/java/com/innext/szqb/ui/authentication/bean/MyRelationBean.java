package com.innext.szqb.ui.authentication.bean;

import java.util.List;

public class MyRelationBean {

	private List<RelationBean> lineal_list;
	private List<RelationBean> other_list;
	/**
	 * lineal_relation : 1
	 * lineal_name : 我和哥哥
	 * lineal_mobile : 13961054289
	 * other_relation : 8
	 * other_name : 小雪1
	 * other_mobile : 15216615443
	 */

	private String lineal_relation;
	private String lineal_name;
	private String lineal_mobile;
	private String other_relation;
	private String other_name;
	private String other_mobile;

	public List<RelationBean> getOther_list()
	{
		return other_list;
	}

	public void setOther_list(List<RelationBean> other_list)
	{
		this.other_list = other_list;
	}

	public List<RelationBean> getLineal_list()
	{
		return lineal_list;
	}

	public void setLineal_list(List<RelationBean> lineal_list)
	{
		this.lineal_list = lineal_list;
	}

	public String getLineal_relation()
	{
		return lineal_relation;
	}

	public void setLineal_relation(String lineal_relation)
	{
		this.lineal_relation = lineal_relation;
	}

	public String getLineal_name()
	{
		return lineal_name;
	}

	public void setLineal_name(String lineal_name)
	{
		this.lineal_name = lineal_name;
	}

	public String getLineal_mobile()
	{
		return lineal_mobile;
	}

	public void setLineal_mobile(String lineal_mobile)
	{
		this.lineal_mobile = lineal_mobile;
	}

	public String getOther_relation()
	{
		return other_relation;
	}

	public void setOther_relation(String other_relation)
	{
		this.other_relation = other_relation;
	}

	public String getOther_name()
	{
		return other_name;
	}

	public void setOther_name(String other_name)
	{
		this.other_name = other_name;
	}

	public String getOther_mobile()
	{
		return other_mobile;
	}

	public void setOther_mobile(String other_mobile)
	{
		this.other_mobile = other_mobile;
	}
}
