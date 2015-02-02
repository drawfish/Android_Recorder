package com.example.recordpro;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;


//用于登录过程中的xml文件的类；

public class AppAskForLogin extends UserDataClass{
	private login loginfo;
	public boolean ServerProblem=false;
	public AppAskForLogin(){loginfo=null;}
	public AppAskForLogin(String username,String password)
	{
		loginfo=new login();
		loginfo.setUsername(username);
		String MD5Pwd=MD5Generate.md5(password);
		loginfo.setPassword(MD5Pwd);
	}
	private String loginInfoClass2Xml()
	{
		StringWriter os = new StringWriter();
		XmlSerializer xml=Xml.newSerializer();
		try {
			xml.setOutput(os);
			xml.startDocument("utf-8", null);
			xml.startTag(null, "appAction");
			xml.attribute(null, "action", loginfo.getAction());
			xml.startTag(null, "username");
			xml.text(loginfo.getUsername());
			xml.endTag(null, "username");
			xml.startTag(null, "password");
			xml.text(loginfo.getPassword());
			xml.endTag(null, "password");
			xml.endTag(null, "appAction");
			xml.endDocument();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return os.toString();	
	}
	private loginResult loginResultXml2Class(String xml) throws Exception
	{
		InputStream is=new ByteArrayInputStream(xml.getBytes("utf-8"));
		loginResult login_result=null;
		XmlPullParser parser=Xml.newPullParser();
		parser.setInput(is,"utf-8");
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {  
            switch (eventType)
            {  
	            case XmlPullParser.START_DOCUMENT:  
	            	login_result=new loginResult();
	                break;  
	            case XmlPullParser.START_TAG:
	            	String xmlName=parser.getName();
	            	if (xmlName.equals("serverAction")){
	            		login_result.setAction(parser.getAttributeValue(null, "action"));
	            	}
	            	else if(xmlName.equals("username"))
	            	{
	            		parser.next(); 
	            		login_result.setUsername(parser.getText());
	            	}
	            	else if (xmlName.equals("loginResult"))
	            	{
	            		parser.next(); 
	            		if(parser.getText().equals("true"))
	            		{
	            			login_result.setLoginResult(true);
	            		}
	            		else 
	            			login_result.setLoginResult(false);
	            	}
	            	else if(xmlName.equals("resultReason"))
	            	{
	            		parser.next(); 
	            		login_result.setReason(parser.getText());
	            	}
	                break;  
	            case XmlPullParser.END_TAG:   
	            	if(parser.getName().equals("serverAction"))
	            		return login_result;
	                break;  
            }  
            eventType = parser.next();  
        }  
		return null;
	}
	public Boolean login2Server() throws Exception
	{
		if(loginfo==null||loginfo.getUsername().equals("")||loginfo.getPassword().equals(""))
		{
			return false;
		}
		else 
		{
			String xml=null;
			xml=loginInfoClass2Xml();
			//Post it to the server and wait for the server;
			HttpPostAndGet http=new HttpPostAndGet();
			xml=http.HttpClientPOST("http://125.217.245.125:10080/AppPost/appLogin/login.php", xml.getBytes());
			loginResult result=loginResultXml2Class(xml);
			if(result.getAction().equals("NoRespond"))
			{
				ServerProblem=true;
				return false;
			}
			else 
			{
				ServerProblem=false;
				return result.getResult();
			}
		}
	}
}

