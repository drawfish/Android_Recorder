package com.example.recordpro;

import android.app.Activity;

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
}
