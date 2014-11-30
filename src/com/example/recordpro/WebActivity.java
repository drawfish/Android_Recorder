package com.example.recordpro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebActivity extends Activity {
	WebView webBrower=null;
	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	     this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_web);
		webBrower=(WebView)findViewById(R.id.webview);
		Intent intent=this.getIntent();
		String url=intent.getStringExtra("webPageUrl");
		WebSettings websettings=webBrower.getSettings();
		websettings.setJavaScriptEnabled(true);
		webBrower.setWebViewClient(new WebViewClient()
		{
			@Override
			 public boolean shouldOverrideUrlLoading(WebView view, String url)
			 {      
			        view.loadUrl(url);      
			        return true;      
			 } 
			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				handler.proceed();
			}
		});
		webBrower.loadUrl(url);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	 {      
	        if ((keyCode == KeyEvent.KEYCODE_BACK) && webBrower.canGoBack()) {      
	        	webBrower.goBack();      
	            return true;      
	        }      
	        return super.onKeyDown(keyCode, event);      
	    }
}
