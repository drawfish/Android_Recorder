package com.example.recordpro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Switch;

public class MainButtonListenerActivitySub extends SliderBarActivity {
	public Button mainUserInfo=null;
	public Button mainRecModel=null;
	public Button mainCheckModel=null;
	public Button mainTechModel=null;
	public Switch mainAutoUpload=null;
	public Button mainQuitApp=null;
	private Intent UploadService=null;
	private WifiModelOrNot wifiModel=null;
	private RelativeLayout mainLayout=null;
	public void mainActivityButtonInit()
	{
		mainUserInfo=(Button)findViewById(R.id.mainuserInfo);
		mainRecModel=(Button)findViewById(R.id.mainrecModel);
		mainCheckModel=(Button)findViewById(R.id.maincheckModel);
		mainTechModel=(Button)findViewById(R.id.maintechPicture);
		mainAutoUpload=(Switch)findViewById(R.id.mainautoUpload);
		mainQuitApp=(Button)findViewById(R.id.mainquitLogin);
		mainLayout=(RelativeLayout)findViewById(R.id.maincontainer);
		//On Click listener.
		mainUserInfo.setOnClickListener(new mainButtonListener());
		mainRecModel.setOnClickListener(new mainButtonListener());
		mainCheckModel.setOnClickListener(new mainButtonListener());
		mainTechModel.setOnClickListener(new mainButtonListener());
		mainQuitApp.setOnClickListener(new mainButtonListener());
		mainAutoUpload.setOnCheckedChangeListener(new mainSwithListener());
		wifiModel=new WifiModelOrNot(this);
		if(wifiModel.getWifiModel())
		{
			mainAutoUpload.setChecked(true);
			AutoUpload.setAutoUpload(true);
		}
		else 
		{
			mainAutoUpload.setChecked(false);
			AutoUpload.setAutoUpload(false);
		}
		UploadService=new Intent(this,AppUploadService.class);
		startService(UploadService);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(UploadService);
	}
	class mainButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==mainUserInfo)
				userInfoButtonHandler();
			if(v==mainRecModel)
				recModelButtonHandler();
			if(v==mainCheckModel)
				checkModelButtonHandler();
			if(v==mainTechModel)
				techModelButtonHandler();
			if(v==mainQuitApp)
				quitModelButtonHandler();
			mainUserInfo.setEnabled(false);
			mainRecModel.setEnabled(false);
			mainCheckModel.setEnabled(false);
			mainTechModel.setEnabled(false);
			mainQuitApp.setEnabled(false);
			mainAutoUpload.setEnabled(false);
			sliderbarInit();
			mainLayout.setBackgroundColor(0xff000000);
		}
		
	}
	class mainSwithListener implements OnCheckedChangeListener
	{
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked)
			{
				if(wifiModel.getWifiModel())
					AutoUpload.setAutoUpload(true);
				else
				{
					OptionsDial();
				}
			}
			else 
				AutoUpload.setAutoUpload(false);
		}
	}
	private void OptionsDial()
	{
		new AlertDialog.Builder(this)
		.setTitle("确认")
		.setMessage("当前未连接Wifi，使用自动上传会产生流量费用，是否继续启用自动上传功能？")
		.setPositiveButton("是",new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				AutoUpload.setAutoUpload(true);
			}
		}).setNegativeButton("否", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				AutoUpload.setAutoUpload(false);
				mainAutoUpload.setChecked(false);
			}
		}).show();
	}
}
