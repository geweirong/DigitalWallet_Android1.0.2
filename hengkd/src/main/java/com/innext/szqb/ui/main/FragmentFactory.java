package com.innext.szqb.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.innext.szqb.ui.lend.fragment.LendFragment;
import com.innext.szqb.ui.mall.MallFragment;
import com.innext.szqb.ui.my.fragment.MoreFragment;
import com.innext.szqb.ui.repayment.fragment.RepaymentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * fragment选择管理
 * hengxinyongli
 */
public class FragmentFactory {

	private static List<Fragment> list;
	private static Fragment lastFragment;
	private static FragmentManager manager;
	public enum FragmentStatus
	{
		None,
		Lend,
		MALL,
		FragmentStatus, Account
	}
	
	
	public static void clear()
	{
		if(list!=null)
			list.clear();
		if(manager!=null)
		{
			int count = manager.getBackStackEntryCount();
			while(count>=0)
			{
				manager.popBackStackImmediate();
				count--;
			}
		}
		manager = null;
	}
	
	public static void changeFragment(FragmentManager manager, FragmentStatus status, int id)
	{
		FragmentFactory.manager = manager;
		FragmentTransaction transaction = manager.beginTransaction();
		if(list==null)
			list = new ArrayList<Fragment>();
		Fragment selectFragment = null;
		switch (status) {
		case None:
			return;
		case Lend:
//			selectFragment = LendFragment_Old.getInstance();
			selectFragment = LendFragment.Companion.getInstance();
			break;
		case Account:
			selectFragment = MoreFragment.getInstance();
			break;
		case MALL:
			selectFragment = MallFragment.getInstance();
		default:
			break;
		}
		
		//change
		if(list.contains(selectFragment))
		{
			transaction.hide(lastFragment).show(selectFragment).commitAllowingStateLoss();
		}
		else
		{
			if(list.size()==0)
				transaction.add(id, selectFragment).commitAllowingStateLoss();
			else
				transaction.hide(lastFragment).add(id, selectFragment).commitAllowingStateLoss();
			list.add(selectFragment);
		}
		lastFragment = selectFragment;
	}
	
}
