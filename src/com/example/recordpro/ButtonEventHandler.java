package com.example.recordpro;

import android.app.Activity;
import android.content.Intent;

public class ButtonEventHandler extends Activity {
	public void userInfoButtonHandler(){
	};
	public void recModelButtonHandler(){
	    getFragmentManager().beginTransaction()
        .add(R.id.maincontainer, new RecordFragment())
        .commit();
	};
	public void checkModelButtonHandler(){
		getFragmentManager().beginTransaction()
        .add(R.id.maincontainer, new CheckoutFragment())
        .commit();
	};
	public void techModelButtonHandler(){};
	public void quitModelButtonHandler(){
		Intent jumptoLogin=new Intent();
		jumptoLogin.setClass(ButtonEventHandler.this,LoginActivity.class);
		startActivity(jumptoLogin);
		finish();
	};
}
