package com.example.recordpro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//登录
public class LoginActivity extends MainActivityButtonEventHandler {
	private final int LOGINRESULT=0x01;
	private Button loginButton=null;
	private TextView forgetPwdText=null;
	private TextView newUserText=null;
	private EditText usernameText=null;
	private EditText passwordText=null;
	private Handler handle=null;
	private Boolean loginresult=false;
	private String username=null;
	private String password=null;
	private AppAskForLogin askLogin=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	     this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		init();
	}
	private void init()
	{
		loginButton=(Button)findViewById(R.id.loginButton);
		forgetPwdText=(TextView)findViewById(R.id.cannotLogin);
		newUserText=(TextView)findViewById(R.id.newregist);
		usernameText=(EditText)findViewById(R.id.userName);
		passwordText=(EditText)findViewById(R.id.passwd);
		
		loginButton.setOnClickListener(new loginListener());
		forgetPwdText.setOnClickListener(new loginListener());
		newUserText.setOnClickListener(new loginListener());
		handle=new loginHandle();
	}
	class loginListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			if(v==loginButton)
				try {loginButtonHandler();} catch (Exception e){}
			if(v==forgetPwdText)
				forgetPwdWebView();
			if(v==newUserText)
				registWebView();
		}
		
	}
	@SuppressLint("HandlerLeak") class loginHandle extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			int what=msg.what;
			switch (what)
			{
				case LOGINRESULT:   
					loginResultHandler();
					break;
			}
		}
	}
	
	public void loginResultHandler()
	{
		
		if(loginresult==true)
		{
			Intent jumpToMain=new Intent();
			jumpToMain.setClass(LoginActivity.this, MainActivity.class);
			startActivity(jumpToMain);
			finish();
		}
		else 
		{
			if(askLogin.ServerProblem)
				new ToastShow(this, "服务器无响应，请离线登录");
			else 
				new ToastShow(this, "用户名或密码错误");
		}
	}
	
	class loginThread extends Thread
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			askLogin=new AppAskForLogin(username, password);
			try 
			{
				loginresult=askLogin.login2Server();
				handle.sendEmptyMessage(LOGINRESULT);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	public void loginButtonHandler() throws Exception{
		username=usernameText.getText().toString();
	    password=passwordText.getText().toString();
		if(username.equals("")||password.equals(""))
		{
			new ToastShow(this, "请检查用户名和密码。");
			return;
		}
		else 
		{
			new loginThread().start();
		}
	}
	public void forgetPwdWebView()
	{
		Intent jumpToWebview=new Intent();
		jumpToWebview.setClass(LoginActivity.this, WebActivity.class);
		jumpToWebview.putExtra("webPageUrl", "https://116.57.86.142/login/ForgetPasswd/");
		startActivity(jumpToWebview);
	}
	public void registWebView()
	{
		Intent jumpToWebview=new Intent();
		jumpToWebview.setClass(LoginActivity.this, WebActivity.class);
		jumpToWebview.putExtra("webPageUrl", "https://116.57.86.142/login/regist/");
		startActivity(jumpToWebview);
	}
}
