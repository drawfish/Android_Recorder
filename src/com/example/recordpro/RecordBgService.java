package com.example.recordpro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Fragment;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;

public class RecordBgService extends PlayerBgService{
	// 音频获取源
	private int audioSource = MediaRecorder.AudioSource.MIC;
	// 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
	private static int sampleRateInHz = 16000;
	// 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
	private static int channelConfig = AudioFormat.CHANNEL_IN_MONO;
	// 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
	private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	// 缓冲区字节大小
	private int bufferSizeInBytes = 0;
	private boolean isRecord = false;// 设置正在录制的状态
	private AudioManager mAudioManager = null; 
	//AudioName裸音频数据文件
	private static final String AudioBufferName = "/sdcard/buffer.3gpp";
	//NewAudioName可播放的音频文件
	private static final String AudioPath = "/sdcard/scutRec/";
	private String AudioName=null;
	private boolean BlueTouchOK=false;
	private BluetoothAdapter adapter;
	private AudioRecord audioRecord;
	static public String World2Rec=null;
	static public String World2RecID=null;
	//static public AppAskforContex AskContex=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		RecordBgServerInint();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			stopRecord(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		audioRecord.release();//释放资源
		audioRecord = null;
	}
	
	public void RecordBgServerInint()
	{
		adapter=BluetoothAdapter.getDefaultAdapter();
		creatAudioRecord();
		File destDir = new File(AudioPath);
	    if (!destDir.exists()) {
	       destDir.mkdirs();
	    } 
	}
	
	private void creatAudioRecord() {
		mAudioManager=(AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
		if(!mAudioManager.isBluetoothScoAvailableOffCall()){
            Log.i("设备问题:", "系统不支持蓝牙录音");
            BlueTouchOK=false;
           	}
		else {
			mAudioManager.startBluetoothSco();
			Log.i("设备问题:", "系统支持蓝牙录音");
			BlueTouchOK=true;
			if(BluetoothProfile.STATE_CONNECTED==adapter.getProfileConnectionState(BluetoothProfile.HEADSET)) 
				BlueTouchOK=true;
			else
			{
				BlueTouchOK=false;
				Log.i("设备问题:", "蓝牙设备未连接");
			}
		}
		// 获得缓冲区字节大小
		bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
				channelConfig, audioFormat);
		// 创建AudioRecord对象
		audioRecord = new AudioRecord(audioSource, sampleRateInHz,
				channelConfig, audioFormat, bufferSizeInBytes);
	}
	
	class AudioRecordThread implements Runnable {
		@Override
		public void run() {
		try {
			writeDateTOFile();//往文件中写入裸数据		
			Log.i("线程","退出");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void startRecord()
	{
		Log.i(null,"recording");
		if(BlueTouchOK==true){
			  getActivity().registerReceiver(new BroadcastReceiver() {
		             @Override
		             public void onReceive(Context context, Intent intent) {
		                int state = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1);
		                if (AudioManager.SCO_AUDIO_STATE_CONNECTED == state) { 
		                	 mAudioManager.setBluetoothScoOn(true);  //打开SCO
		                	 Record();//开始录音
		                	 getActivity().unregisterReceiver(this);  //别遗漏
		                 }else{//等待一秒后再尝试启动SCO
		                     try {
		                    	 Thread.sleep(500);
		                    	 Log.i("Error", "Wait for blue");
		                 } catch (InterruptedException e) {
		                	 	e.printStackTrace();
		                 }
		                     mAudioManager.startBluetoothSco(); 
		                 }
		             }
		         }, new IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_CHANGED));	
			}
			else
				Record();//开始录音
	}
	
	public void Record() {
		Time t=new Time();
		t.setToNow();
		AudioName=AudioPath+World2RecID+"_"+t.month+t.monthDay+t.hour+t.minute+t.second+".wav";
		audioRecord.startRecording();
		// 让录制状态为true
		isRecord = true;
		// 开启音频文件写入线程
		new Thread(new AudioRecordThread()).start();
	}

	public void stopRecord(boolean isSave) throws Exception {
		if (audioRecord != null) {
			Log.i(null,"stopRecord");
			isRecord = false;//停止文件写入
			audioRecord.stop();
			if(isSave){
				Log.i("测试bug","测试bug");
				copyWaveFile(AudioBufferName, AudioName);//给裸数据加上头文件
			}
		}
		if(BlueTouchOK==true){
			 if(mAudioManager.isBluetoothScoOn()){
		            mAudioManager.setBluetoothScoOn(false);
		            mAudioManager.stopBluetoothSco();
			 }
	     }
	}
	public boolean getRecordState()
	{
		return isRecord;
	}
	private void writeDateTOFile() throws IOException{
		// new一个byte数组用来存一些字节数据，大小为缓冲区大小
		byte[] audiodata = new byte[bufferSizeInBytes];
		FileOutputStream fos = null;
		int readsize = 0;
		File file = new File(AudioBufferName);
		if (file.exists()) {
			file.delete();
		}
		fos = new FileOutputStream(file);// 建立一个可存取字节的文件

		while (isRecord == true) {
			readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes);
			if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
					fos.write(audiodata);					
			}
		}
		fos.flush();
		fos.close();// 关闭写入流
	}

	// 这里得到可播放的音频文件
	private void copyWaveFile(String inFilename, String outFilename) throws IOException{
		FileInputStream in = null;
		FileOutputStream out = null;
		long totalAudioLen = 0;
		long totalDataLen = totalAudioLen + 36;
		long longSampleRate = sampleRateInHz;
		int channels = 1;
		long byteRate = 16 * sampleRateInHz * channels / 8;
		byte[] data = new byte[bufferSizeInBytes];
		in = new FileInputStream(inFilename);
		out = new FileOutputStream(outFilename);
		totalAudioLen = in.getChannel().size();
		totalDataLen = totalAudioLen + 36;
		WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
				longSampleRate, channels, byteRate);
		while (in.read(data) != -1) {
			out.write(data);
		}
		out.flush();
		in.close();
		out.close();
		File file = new File(AudioBufferName);
		if (file.exists()) {
			file.delete();
		}
	}

	private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen,
			long totalDataLen, long longSampleRate, int channels, long byteRate)
			throws IOException {
		/**
		 * 这里提供一个头信息。插入这些信息就可以得到可以播放的文件。
		 * 为我为啥插入这44个字节，这个还真没深入研究，不过你随便打开一个wav
		 * 音频的文件，可以发现前面的头文件可以说基本一样哦。每种格式的文件都有
		 * 自己特有的头文件。
		 */
		byte[] header = new byte[44];
		header[0] = 'R'; // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f'; // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16; // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1; // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (channels * 16 / 8); // block align
		header[33] = 0;
		header[34] = 16; // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
		out.write(header, 0, 44);		
	}

	public String getCurrentAudio()
	{
		return AudioName;
	}
	public boolean deleteAudio(String Audio)
	{
		File file=new File(Audio);
		if (file.exists())
		{
			return file.delete();
		}
		else 
			return true;
		
	}
	public boolean deleteCurrentAudio()
	{
		return deleteAudio(getCurrentAudio());
	}
}
