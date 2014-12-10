package com.example.recordpro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SliderBarActivity extends MainActivityButtonEventHandler{
	private Button sliderbarUserInfo=null;
	private Button sliderbarRecModel=null;
	private Button sliderbarCheckModel=null;
	private Button sliderbarTechModel=null;
	private Switch sliderbarAutoUpload=null;
	private SlidingMenu sliderrbarMenu=null;
	private Button sliderbarQuitLogin=null;
	private WifiModelOrNot wifiModel=null;
	public void sliderbarInit()
	{
		sliderViewInit();
		sliderButtonInit();
	}
	private void sliderViewInit()
	{
		sliderrbarMenu=new SlidingMenu(this);
		sliderrbarMenu.setMode(SlidingMenu.LEFT);
		sliderrbarMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sliderrbarMenu.setShadowWidthRes(R.dimen.slidingShadownwidth);
		sliderrbarMenu.setBehindOffsetRes(R.dimen.slidingBehindOffset);
		sliderrbarMenu.setFadeDegree(0.35f);
		sliderrbarMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		sliderrbarMenu.setMenu(R.layout.activity_sliding); 
	}
	private void sliderButtonInit()
	{
		sliderbarUserInfo=(Button)findViewById(R.id.sliderbaruserInfo);
		sliderbarRecModel=(Button)findViewById(R.id.sliderbarrecModel);
		sliderbarCheckModel=(Button)findViewById(R.id.sliderbarcheckModel);
		sliderbarTechModel=(Button)findViewById(R.id.sliderbartechPicture);
		sliderbarAutoUpload=(Switch)findViewById(R.id.sliderbarautoUpload);
		sliderbarQuitLogin=(Button)findViewById(R.id.sliderbarquitLogin);
		//On Click listener.
		sliderbarUserInfo.setOnClickListener(new sliderBarButtonListener());
		sliderbarRecModel.setOnClickListener(new sliderBarButtonListener());
		sliderbarCheckModel.setOnClickListener(new sliderBarButtonListener());
		sliderbarTechModel.setOnClickListener(new sliderBarButtonListener());
		sliderbarQuitLogin.setOnClickListener(new sliderBarButtonListener());
		sliderbarAutoUpload.setOnCheckedChangeListener(new sliderBarSwithListener());
		wifiModel=new WifiModelOrNot(this);
		if(wifiModel.getWifiModel()
				&&AutoUpload.getAutoUploadStatus()
				&&LoginOnlineOrNot.getLoginOnlineOrNot())
		{
			sliderbarAutoUpload.setChecked(true);
			AutoUpload.setAutoUpload(true);
		}
		else 
		{
			sliderbarAutoUpload.setChecked(false);
			AutoUpload.setAutoUpload(false);
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
	private class sliderBarButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==sliderbarUserInfo)
				userInfoButtonHandler();
			if(v==sliderbarRecModel)
				recModelButtonHandler();
			if(v==sliderbarCheckModel)
				checkModelButtonHandler();
			if(v==sliderbarTechModel)
				techModelButtonHandler();
			if(v==sliderbarQuitLogin)
				quitModelButtonHandler();
		}
	}
	
	private class sliderBarSwithListener implements OnCheckedChangeListener
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
				sliderbarAutoUpload.setChecked(false);
			}
		}).show();
	}
}
