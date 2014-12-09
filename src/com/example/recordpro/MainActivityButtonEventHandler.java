package com.example.recordpro;

import android.app.Activity;
import android.content.Intent;

public class MainActivityButtonEventHandler extends Activity {
	private RecordFragment recordFragment=new RecordFragment();
	private CheckFragment checkFragment=new CheckFragment();
	public void userInfoButtonHandler(){
	};
	/*2014-12-06:�Ż��˴˴����룬���ⲻ�ϵ�new Fragment������ʵ������ ���¶�ջ�������ռ�ù��ߵ��ڴ档*/
	public void recModelButtonHandler(){
	   /* getFragmentManager().beginTransaction()
        .add(R.id.maincontainer, new RecordFragment())
        .commit();*/
		if(!recordFragment.isAdded())
		{
			getFragmentManager()
			.beginTransaction()
			.hide(checkFragment)
			.add(R.id.maincontainer, recordFragment)
			.show(recordFragment)
			.commit();
		}
		else 
		{
			getFragmentManager()
			.beginTransaction()
			.hide(checkFragment)
			.show(recordFragment)
			.commit();
		}
	};
	public void checkModelButtonHandler(){
		/*getFragmentManager().beginTransaction()
        .add(R.id.maincontainer, new CheckFragment())
        .commit();*/
		if(!checkFragment.isAdded())
		{
			getFragmentManager()
			.beginTransaction()
			.hide(recordFragment)
			.add(R.id.maincontainer, checkFragment)
			.show(checkFragment)
			.commit();
		}
		else 
		{
			getFragmentManager()
			.beginTransaction()
			.hide(recordFragment)
			.show(checkFragment)
			.commit();
		}
	};
	public void techModelButtonHandler(){};
	public void quitModelButtonHandler(){
		Intent jumptoLogin=new Intent();
		jumptoLogin.setClass(MainActivityButtonEventHandler.this,LoginActivity.class);
		startActivity(jumptoLogin);
		finish();
	};
}
