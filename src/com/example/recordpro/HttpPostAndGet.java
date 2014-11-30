package com.example.recordpro;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

public class HttpPostAndGet {
	private myX509TrustManager xtm = new myX509TrustManager();  	  
    private myHostnameVerifier hnv = new myHostnameVerifier();  
    class myX509TrustManager implements X509TrustManager  
    {  
        public void checkClientTrusted(X509Certificate[] chain, String authType){}  
        public void checkServerTrusted(X509Certificate[] chain, String authType){}  
        public X509Certificate[] getAcceptedIssuers(){return null;}  
    }  
   
    class myHostnameVerifier implements HostnameVerifier  
    {  
        public boolean verify(String hostname, SSLSession session){return true;}  
    } 
	
    public void httpsAuth() throws Exception  
	{  
	    SSLContext sslContext = null;   
	    try  
	    {     
	        sslContext = SSLContext.getInstance("SSL"); // 或SSL  
	        X509TrustManager[] xtmArray = new X509TrustManager[]{ xtm };    
	        sslContext.init(null, xtmArray, new SecureRandom());  
	    }   
	    catch (GeneralSecurityException e)  
	    {  
	        e.printStackTrace();  
	    }  
	    if (sslContext != null)  
	    {  
	        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext  
	                .getSocketFactory());  
	    }  
	    HttpsURLConnection.setDefaultHostnameVerifier(hnv);  
	} 
	
	public String HttpsClientPOST(String postUrl,byte[] xml) throws Exception
	{
		String NoRespond=defaultNoRespond();	
		try{
			httpsAuth();
			URL url=new URL(postUrl);
			HttpsURLConnection httpUrlConnect=(HttpsURLConnection)url.openConnection();
			httpUrlConnect.setDoInput(true);
			httpUrlConnect.setDoOutput(true);
			httpUrlConnect.setUseCaches(false);
			httpUrlConnect.setRequestMethod("POST");
			httpUrlConnect.setRequestProperty("Connection", "Keep-Alive");
			httpUrlConnect.setRequestProperty("Charset", "UTF-8");
			httpUrlConnect.setRequestProperty("Content-Length", String.valueOf(xml.length));
			httpUrlConnect.setRequestProperty("Content-Type", "text/xml");
			httpUrlConnect.setConnectTimeout(6*1000);
			DataOutputStream dos=new DataOutputStream(httpUrlConnect.getOutputStream());
			dos.write(xml);
			dos.flush();
			dos.close();	
			if(httpUrlConnect.getResponseCode()==200)
			{
				InputStream is=httpUrlConnect.getInputStream();
				BufferedReader br=new BufferedReader(new InputStreamReader(is));
				String readline=null;
				String Respond="";
				while((readline=br.readLine())!=null)
					Respond+=readline;
				is.close();
				br.close();
				httpUrlConnect.disconnect();
				return Respond;
			}
			else 
				return NoRespond;
		}catch(Exception e){
			return NoRespond;
		}
	}
	
	public String HttpClientPOST(String postUrl,byte[] xml) throws Exception
	{
		String NoRespond=defaultNoRespond();
		Log.i("数据长度",""+xml.length);
		try{
			URL url=new URL(postUrl);
			HttpURLConnection httpUrlConnect=(HttpURLConnection)url.openConnection();
			httpUrlConnect.setDoInput(true);
			httpUrlConnect.setDoOutput(true);
			httpUrlConnect.setUseCaches(false);
			httpUrlConnect.setRequestMethod("POST");
			httpUrlConnect.setRequestProperty("Connection", "Keep-Alive");
			httpUrlConnect.setRequestProperty("Charset", "UTF-8");
			httpUrlConnect.setRequestProperty("Content-Length", String.valueOf(xml.length));
			httpUrlConnect.setRequestProperty("Content-Type", "text/xml");
			httpUrlConnect.setConnectTimeout(6*1000);
			DataOutputStream dos=new DataOutputStream(httpUrlConnect.getOutputStream());
			dos.write(xml);
			dos.flush();
			dos.close();	
			if(httpUrlConnect.getResponseCode()==200)
			{
				InputStream is=httpUrlConnect.getInputStream();
				BufferedReader br=new BufferedReader(new InputStreamReader(is));
				String readline=null;
				String Respond="";
				while((readline=br.readLine())!=null)
					Respond+=readline;
				is.close();
				br.close();
				httpUrlConnect.disconnect();
				return Respond;
			}
			else 
				return NoRespond;
		}catch(Exception e){
			return NoRespond;
		}
	}
	public String HttpClientGet(String geturl) throws Exception
	{
		String NoRespond=defaultNoRespond();
		URL url=new URL(geturl);
		HttpURLConnection httpurlConnect=(HttpURLConnection)url.openConnection();
		httpurlConnect.setConnectTimeout(6*1000);
		httpurlConnect.setDoInput(true);
		httpurlConnect.setRequestMethod("GET");
		httpurlConnect.setUseCaches(false);
		httpurlConnect.setRequestProperty("Content-Type", "text/xml");
		httpurlConnect.setRequestProperty("Charset", "UTF-8");
		httpurlConnect.connect();
		if(httpurlConnect.getResponseCode()==200){
			BufferedReader br=new BufferedReader(new InputStreamReader(httpurlConnect.getInputStream()));
			String Respond="";
			String readline=null;
			while((readline=br.readLine())!=null)
			{
				Respond+=readline;
			}
			return Respond;
		}
		else {
			return NoRespond;
		}
	}
	private String defaultNoRespond() throws Exception
	{
		StringWriter os=new StringWriter();;
        XmlSerializer xml=Xml.newSerializer();
		xml.setOutput(os);
		xml.startDocument("utf-8", null);
		xml.startTag(null, "serverAction");
		xml.attribute(null, "action", "NoRespond");
		xml.endTag(null, "serverAction");
		xml.endDocument();
        return os.toString();
	}
}
