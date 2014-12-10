package com.example.recordpro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.recordpro.UserDataClass.RecordWavData;
import com.example.recordpro.UserDataClass.confirmResult;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class CheckFragmentViewInit extends PlayerBgService{

	private ImageButton checkCtrl=null;
	private ImageButton checkYes=null;
	private ImageButton checkNo=null;
	private EditText checkText=null;
	private RelativeLayout checkchoose=null;
	
	private RecordWavData Data=null;
	private Handler checkMessageHandle=null;
	private static final int GETDATARESULT=0x06;
	private static final int GETPLAYERSTATE=0x07;
	private PlayerBgService player=null;
	private boolean RecordTrueOrFalse=false;
	private confirmResult confirm=null;
	private boolean replay=false;
	private boolean loginOnline=false;
	@SuppressLint("SdCardPath") private static final String CheckFilePath = "/sdcard/scutRec/checkFile";
	
	public void checkViewInit()
	{
		checkCtrl=(ImageButton)getView().findViewById(R.id.checkCtrlButton);
		checkYes=(ImageButton)getView().findViewById(R.id.checkYesButton);
		checkNo=(ImageButton)getView().findViewById(R.id.checkNoButton);
		checkText=(EditText)getView().findViewById(R.id.checkText);
		checkchoose=(RelativeLayout)getView().findViewById(R.id.checkLayout);
		checkCtrl.setOnClickListener(new checkButtonListener());
		checkYes.setOnClickListener(new checkButtonListener());
		checkNo.setOnClickListener(new checkButtonListener());
		
		loginOnline=LoginOnlineOrNot.getLoginOnlineOrNot();
		if(!loginOnline)
			checkText.setText("当前离线状态不允许使用该模式。");
		checkMessageHandle=new getContexHandler();
		player=new PlayerBgService();
		File file=new File(CheckFilePath);
		if(!file.exists())
			file.mkdir();
	}
	
	
	@SuppressLint("HandlerLeak") class getContexHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			int what=msg.what;
			switch (what)
			{
				case GETDATARESULT:  
					if(!Data.getAction().equals("NoRespond")&&Data.getRecordWavFile()!=null)
					{
						checkText.setText(Data.getRecordContex());
						player.startPlay(CheckFilePath+"/check.wav");
						new getPlyerStateThread().start();
					}
					else if(!Data.getAction().equals("NoRespond")&&Data.getRecordWavFile()==null)
					{
						checkText.setText(Data.getRecordContex());
						checkCtrl.setVisibility(View.VISIBLE);
					}
					else 
					{
						new ToastShow(getActivity(),"服务器无响应。");
					}
					break;
				case GETPLAYERSTATE:
					//checkCtrl.setImageDrawable(getResources().getDrawable(R.drawable.recstopplay));
					checkCtrl.setVisibility(View.VISIBLE);
					checkchoose.setVisibility(View.VISIBLE);
					break;
			}
		}
	}

	
	private class checkButtonListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(loginOnline)
			{
				checkCtrl.setVisibility(View.GONE);
				if(v==checkCtrl)
				{
					if(!replay)
						new checkThread().start();
					else 
					{
						checkMessageHandle.sendEmptyMessage(GETDATARESULT);
					}
				}
				else
				{
					checkchoose.setVisibility(View.GONE);
				}
				if(v==checkYes)
				{
					RecordTrueOrFalse=true;
					new uploadResultThread().start();
					new checkThread().start();
				}
				if(v==checkNo)
				{
					RecordTrueOrFalse=false;
					new uploadResultThread().start();
					new checkThread().start();
				}
			}
		}	
	}
	
	
	private class checkThread extends Thread
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				Data=new AppAskForCheck().getRecordFromServer();
				if(!Data.getAction().equals("NoRespond")&&Data.getRecordWavFile()!=null)
				{
					FileSave(Data.getRecordWavFile());
					replay=true;
				}
				checkMessageHandle.sendEmptyMessage(GETDATARESULT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private class getPlyerStateThread extends Thread
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(player.getPlayerState())
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			checkMessageHandle.sendEmptyMessage(GETPLAYERSTATE);
		}
	}
	
	private class uploadResultThread extends Thread
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				confirm=new AppAskForResult(Data.getRecordContexID(),
						Data.getRecordContex(),
						RecordTrueOrFalse).getConfirmFromServer();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void FileSave(byte[] data)throws IOException
	{
		File file =new File(CheckFilePath+"/check.wav");
		if(file.exists())
			file.delete();
		FileOutputStream out=new FileOutputStream(file);
		out.write(data);
		out.flush();
		out.close();
	}
}
