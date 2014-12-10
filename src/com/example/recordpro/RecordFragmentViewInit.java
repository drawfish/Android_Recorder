package com.example.recordpro;

import java.io.IOException;
import com.example.recordpro.UserDataClass.recordContex;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

public class RecordFragmentViewInit extends RecordBgService{
	public Button recControlButton=null;
	public Button recJumpOutButton=null;
	public Button recReplayButton=null;
	public Button recDeleteButton=null;
	public TextView recContex=null;
	private Handler handle=null;
	static final int GETCONTEXRESULT=0x02;
	public recordContex AskContex=null;
	public enum recState{
		isRec,isStop,isNext
	}
	private recState buttonState=recState.isRec;
	public RecordFragmentViewInit(){};
	public void recordFragmentButtonInit()
	{
		recControlButton=(Button)getView().findViewById(R.id.recCtrlButton);
		recJumpOutButton=(Button)getView().findViewById(R.id.jumpOutButton);
		recReplayButton=(Button)getView().findViewById(R.id.replayButton);
		recDeleteButton=(Button)getView().findViewById(R.id.deleteButton);
		recContex=(TextView)getView().findViewById(R.id.recContext);
		
		recControlButton.setOnClickListener(new recordFragmentButtonListener());
		recJumpOutButton.setOnClickListener(new recordFragmentButtonListener());
		recReplayButton.setOnClickListener(new recordFragmentButtonListener());
		recDeleteButton.setOnClickListener(new recordFragmentButtonListener());
		
		recJumpOutButton.setEnabled(false);
		recReplayButton.setEnabled(false);
		recDeleteButton.setEnabled(false);
		
		handle=new getContexHandler();
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
				case GETCONTEXRESULT:   
					if(!World2Rec.equals(""))
					{
						recContex.setText(World2Rec);
						startRecord();
					}
					break;
			}
		}
	}
	
	class recordFragmentButtonListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(v==recControlButton){
				if(buttonState==recState.isRec)
				{
					recContex.setText("ÄãºÃ£¬Ä§Òô£¡");
					World2Rec="ÄãºÃ£¬Ä§Òô£¡";
					World2RecID="1";
					startRecord();
					buttonState=recState.isStop;
					recControlButton.setText("Í£Ö¹");
					recJumpOutButton.setEnabled(true);
				}
				else if(buttonState==recState.isStop)
				{
					try {
						stopRecord(true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					buttonState=recState.isNext;
					recControlButton.setText("ÏÂÒ»¸ö");
					recJumpOutButton.setEnabled(false);
					recReplayButton.setEnabled(true);
					recDeleteButton.setEnabled(true);
				}
				else if(buttonState==recState.isNext)
				{
					try {
						sendRecord();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					getContexButtonHandler();
					buttonState=recState.isStop;
					recControlButton.setText("Í£Ö¹");
					recJumpOutButton.setEnabled(true);
					recReplayButton.setEnabled(false);
					recDeleteButton.setEnabled(false);
				}
			};
			if(v==recJumpOutButton)
			{
				recReplayButton.setEnabled(false);
				recDeleteButton.setEnabled(false);
				if(getRecordState())
				{
					try {
						stopRecord(false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					deleteCurrentAudio();
				}
				getContexButtonHandler();
				recControlButton.setText("Í£Ö¹");
				buttonState=recState.isStop;
			};
			if(v==recReplayButton)
			{
				//recContex.setText(getCurrentAudio());
				startPlay(getCurrentAudio());
			};
			if(v==recDeleteButton)
			{
				deleteCurrentAudio();
				recReplayButton.setEnabled(false);
				recDeleteButton.setEnabled(false);
			};
		}
		
	}
	
	private void getContexButtonHandler()
	{
		new getContexThread().start();
	}
	class getContexThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				AskContex=new AppAskForContex().getContextFromServer();
				World2Rec=AskContex.getRecordContex();
				World2RecID=AskContex.getRecordContexID();
				handle.sendEmptyMessage(GETCONTEXRESULT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if(buttonState!=recState.isRec)
		{
			recContex.setText("");
			buttonState=recState.isNext;
			recControlButton.setText("¿ªÊ¼");
		}
		try {
			if(getRecordState())
				stopRecord(false);
			else 
			{
				sendRecord();
				cleanBuffer();
			}
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		super.onPause();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if(!getRecordState())
		{
			try {
				sendRecord();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}
}
