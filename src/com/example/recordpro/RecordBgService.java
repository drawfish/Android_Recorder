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
	// ��Ƶ��ȡԴ
	private int audioSource = MediaRecorder.AudioSource.MIC;
	// ������Ƶ�����ʣ�44100��Ŀǰ�ı�׼������ĳЩ�豸��Ȼ֧��22050��16000��11025
	private static int sampleRateInHz = 16000;
	// ������Ƶ��¼�Ƶ�����CHANNEL_IN_STEREOΪ˫������CHANNEL_CONFIGURATION_MONOΪ������
	private static int channelConfig = AudioFormat.CHANNEL_IN_MONO;
	// ��Ƶ���ݸ�ʽ:PCM 16λÿ����������֤�豸֧�֡�PCM 8λÿ����������һ���ܵõ��豸֧�֡�
	private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	// �������ֽڴ�С
	private int bufferSizeInBytes = 0;
	private boolean isRecord = false;// ��������¼�Ƶ�״̬
	private AudioManager mAudioManager = null; 
	//AudioName����Ƶ�����ļ�
	private static final String AudioBufferName = "/sdcard/buffer.3gpp";
	//NewAudioName�ɲ��ŵ���Ƶ�ļ�
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
		audioRecord.release();//�ͷ���Դ
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
            Log.i("�豸����:", "ϵͳ��֧������¼��");
            BlueTouchOK=false;
           	}
		else {
			mAudioManager.startBluetoothSco();
			Log.i("�豸����:", "ϵͳ֧������¼��");
			BlueTouchOK=true;
			if(BluetoothProfile.STATE_CONNECTED==adapter.getProfileConnectionState(BluetoothProfile.HEADSET)) 
				BlueTouchOK=true;
			else
			{
				BlueTouchOK=false;
				Log.i("�豸����:", "�����豸δ����");
			}
		}
		// ��û������ֽڴ�С
		bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
				channelConfig, audioFormat);
		// ����AudioRecord����
		audioRecord = new AudioRecord(audioSource, sampleRateInHz,
				channelConfig, audioFormat, bufferSizeInBytes);
	}
	
	class AudioRecordThread implements Runnable {
		@Override
		public void run() {
		try {
			writeDateTOFile();//���ļ���д��������		
			Log.i("�߳�","�˳�");
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
		                	 mAudioManager.setBluetoothScoOn(true);  //��SCO
		                	 Record();//��ʼ¼��
		                	 getActivity().unregisterReceiver(this);  //����©
		                 }else{//�ȴ�һ����ٳ�������SCO
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
				Record();//��ʼ¼��
	}
	
	public void Record() {
		Time t=new Time();
		t.setToNow();
		AudioName=AudioPath+World2RecID+"_"+t.month+t.monthDay+t.hour+t.minute+t.second+".wav";
		audioRecord.startRecording();
		// ��¼��״̬Ϊtrue
		isRecord = true;
		// ������Ƶ�ļ�д���߳�
		new Thread(new AudioRecordThread()).start();
	}

	public void stopRecord(boolean isSave) throws Exception {
		if (audioRecord != null) {
			Log.i(null,"stopRecord");
			isRecord = false;//ֹͣ�ļ�д��
			audioRecord.stop();
			if(isSave){
				Log.i("����bug","����bug");
				copyWaveFile(AudioBufferName, AudioName);//�������ݼ���ͷ�ļ�
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
		// newһ��byte����������һЩ�ֽ����ݣ���СΪ��������С
		byte[] audiodata = new byte[bufferSizeInBytes];
		FileOutputStream fos = null;
		int readsize = 0;
		File file = new File(AudioBufferName);
		if (file.exists()) {
			file.delete();
		}
		fos = new FileOutputStream(file);// ����һ���ɴ�ȡ�ֽڵ��ļ�

		while (isRecord == true) {
			readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes);
			if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
					fos.write(audiodata);					
			}
		}
		fos.flush();
		fos.close();// �ر�д����
	}

	// ����õ��ɲ��ŵ���Ƶ�ļ�
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
		 * �����ṩһ��ͷ��Ϣ��������Щ��Ϣ�Ϳ��Եõ����Բ��ŵ��ļ���
		 * Ϊ��Ϊɶ������44���ֽڣ��������û�����о�������������һ��wav
		 * ��Ƶ���ļ������Է���ǰ���ͷ�ļ�����˵����һ��Ŷ��ÿ�ָ�ʽ���ļ�����
		 * �Լ����е�ͷ�ļ���
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
