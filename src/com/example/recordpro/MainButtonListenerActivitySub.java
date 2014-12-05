package com.example.recordpro;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;

public class MainButtonListenerActivitySub extends SliderBarActivity {
	public Button mainUserInfo=null;
	public Button mainRecModel=null;
	public Button mainCheckModel=null;
	public Button mainTechModel=null;
	public Switch mainAutoUpload=null;
	public Button mainQuitApp=null;
	private Intent UploadService=null;
	
	public void mainActivityButtonInit()
	{
		mainUserInfo=(Button)findViewById(R.id.mainuserInfo);
		mainRecModel=(Button)findViewById(R.id.mainrecModel);
		mainCheckModel=(Button)findViewById(R.id.maincheckModel);
		mainTechModel=(Button)findViewById(R.id.maintechPicture);
		mainAutoUpload=(Switch)findViewById(R.id.mainautoUpload);
		mainQuitApp=(Button)findViewById(R.id.mainquitLogin);
		//On Click listener.
		mainUserInfo.setOnClickListener(new mainButtonListener());
		mainRecModel.setOnClickListener(new mainButtonListener());
		mainCheckModel.setOnClickListener(new mainButtonListener());
		mainTechModel.setOnClickListener(new mainButtonListener());
		mainQuitApp.setOnClickListener(new mainButtonListener());
		if(new WifiModelOrNot(this).getWifiModel())
			mainAutoUpload.setChecked(true);
		else 
			mainAutoUpload.setChecked(false);
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
		}
		
	}
	
}
