package com.example.recordpro;


import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;

public class PlayerBgService extends Fragment{
	private MediaPlayer mediaPlayer;
	static private boolean playerState=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	public void startPlay(String AudioFile)
	{
		mediaPlayer=new MediaPlayer();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				stopPlay();
			}
		});
		try {
			mediaPlayer.setDataSource(AudioFile);
			mediaPlayer.prepare();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		playerState=true;
		mediaPlayer.start();
	}
	public void stopPlay()
	{
		mediaPlayer.release();
		mediaPlayer=null;
		playerState=false;
	}
	
	public boolean getPlayerState()
	{
		return playerState;
	}
}
