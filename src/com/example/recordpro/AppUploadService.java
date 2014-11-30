package com.example.recordpro;

import java.io.File;





import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AppUploadService extends Service{
	private boolean isUpload=false;
	private boolean canUpload=true;
	private boolean uploadResult;
	private int uploadFailCount=0;
	private boolean isThreadRun=true;
	private static final String AudioPath = "/sdcard/scutRec/";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		new UploadTherad().start();
		Log.i(null,"StartService!");
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isThreadRun=false;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	class UploadTherad extends Thread
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(isThreadRun)
			{
				if(RecordFileNoEmpty()&&new WifiModelOrNot(getApplicationContext()).getWifiModel()
						&&uploadFailCount<=10)
				{
					if(canUpload&&!isUpload)
					{
						isUpload=true;
						canUpload=false;
						try {
							UploadRecord();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						isUpload=false;
						canUpload=true;
					}
				} 
				else
				{
					try {
						Thread.sleep(5000);
						if(uploadFailCount>10)
							uploadFailCount=0;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.i(null,"Service Thread.");
				}
			}
		}
	}
	boolean RecordFileNoEmpty()
	{
		File file =new File(AudioPath);
		if (file.exists()&&file.isDirectory())
		{
			if(file.list().length>0)
				return true;
			else 
				return false;
		}
		else 
			return false;
	}
	void UploadRecord() throws Exception
	{
		File rootDir=new File(AudioPath);
		File[] files=rootDir.listFiles();
		for (File file:files)
		{
			if(!file.isDirectory())
			{
				if(file.getName().contains(".wav"))
				{
					String wavFile=file.getAbsolutePath();
					uploadResult=new AppAskForUpload(wavFile).getUploadResultFromServer();
					//Log.i(null,"上传文件："+wavFile);
					if(uploadResult)
					{
						file.delete();
						uploadFailCount=0;
					}
					else 
					{
						uploadFailCount++;
					}
					Thread.sleep(1000);
				}
			}
		}
	}
}
