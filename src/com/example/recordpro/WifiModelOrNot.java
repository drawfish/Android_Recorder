package com.example.recordpro;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class WifiModelOrNot {
	private Context context;
	public WifiModelOrNot(Context context){this.context=context;}
	public boolean getWifiModel(){
		ConnectivityManager connectivityManager = (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
        	return true;
        }
        	return false;
	}
}
