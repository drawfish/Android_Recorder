package com.example.recordpro;
import android.content.Context;
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
        else
        {
        	return false;
        }
	}
}
