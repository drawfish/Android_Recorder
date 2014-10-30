package com.example.recordpro;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SliderBarActivity extends ButtonEventHandler{
	public Button sliderbarUserInfo=null;
	public Button sliderbarRecModel=null;
	public Button sliderbarCheckModel=null;
	public Button sliderbarTechModel=null;
	public Switch sliderbarAutoUpload=null;
	public SlidingMenu sliderrbarMenu=null;
	public void sliderbarInit()
	{
		sliderViewInit();
		sliderButtonInit();
	}
	public void sliderViewInit()
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
	public void sliderButtonInit()
	{
		sliderbarUserInfo=(Button)findViewById(R.id.sliderbaruserInfo);
		sliderbarRecModel=(Button)findViewById(R.id.sliderbarrecModel);
		sliderbarCheckModel=(Button)findViewById(R.id.sliderbarcheckModel);
		sliderbarTechModel=(Button)findViewById(R.id.sliderbartechPicture);
		sliderbarAutoUpload=(Switch)findViewById(R.id.sliderbarautoUpload);
		//On Click listener.
		sliderbarUserInfo.setOnClickListener(new sliderBarButtonListener());
		sliderbarRecModel.setOnClickListener(new sliderBarButtonListener());
		sliderbarCheckModel.setOnClickListener(new sliderBarButtonListener());
		sliderbarTechModel.setOnClickListener(new sliderBarButtonListener());
	}
	class sliderBarButtonListener implements OnClickListener{

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
		}
		
	}
}
