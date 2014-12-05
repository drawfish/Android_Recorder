package com.example.recordpro;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;


public class AppAskForResult extends UserDataClass{
	private checkoutResult checkresult=null;
	public AppAskForResult(String recContextID,String recContext,boolean recQuality)
	{
		checkresult=new checkoutResult(recContextID,recContext,recQuality);
	}
	private String AppAskClass2Xml()
	{
		StringWriter os = new StringWriter();
		XmlSerializer xml=Xml.newSerializer();
		try {
			xml.setOutput(os);
			xml.startDocument("utf-8", null);
			xml.startTag(null, "appAction");
			xml.attribute(null, "action", checkresult.getAction());
			xml.startTag(null, "username");
			xml.text(checkresult.getUsername());
			xml.endTag(null, "username");
			xml.startTag(null, "password");
			xml.text(checkresult.getPassword());
			xml.endTag(null, "password");
			xml.startTag(null, "contexID");
			xml.text(checkresult.getContexID());
			xml.endTag(null, "contexID");			
			xml.startTag(null, "checkResult");
			xml.text(checkresult.getCheckoutResult()?"true":"false");
			xml.endTag(null, "checkResult");
			xml.endTag(null, "appAction");
			xml.endDocument();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return os.toString();	
	}
	private confirmResult AskXml2Class(String AskResult) throws Exception 
	{
		InputStream is=new ByteArrayInputStream(AskResult.getBytes("utf-8"));
		confirmResult confirm_Result=null;
		XmlPullParser parser=Xml.newPullParser();
		parser.setInput(is,"utf-8");
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {  
            switch (eventType)
            {  
	            case XmlPullParser.START_DOCUMENT:  
	            	confirm_Result=new confirmResult();
	                break;  
	            case XmlPullParser.START_TAG:
	            	String xmlName=parser.getName();
	            	if (xmlName.equals("serverAction")){
	            		confirm_Result.setAction(parser.getAttributeValue(null, "action"));
	            	}
	            	else if(xmlName.equals("username"))
	            	{
	            		parser.next(); 
	            		confirm_Result.setUsername(parser.getText());
	            	}
	            	else if (xmlName.equals("confirm"))
	            	{
	            		parser.next(); 
	            		if(parser.getText().equals("true"))
	            		{
	            			confirm_Result.setConfirmResult(true);
	            		}
	            		else 
	            			confirm_Result.setConfirmResult(false);
	            	}
	                break;  
	            case XmlPullParser.END_TAG:   
	            	if(parser.getName().equals("serverAction"))
	            		return confirm_Result;
	                break;  
            }  
            eventType = parser.next();  
        }  
		return null;	
	}
	public confirmResult getConfirmFromServer() throws Exception
	{
		if(checkresult==null
				||checkresult.getUsername().equals("")
				||checkresult.getPassword().equals("")
				||checkresult.getContexID().equals("")
				||checkresult.getRecordContex().equals(""))
		{
			return new confirmResult();
		}
		else 
		{
			String xml=null;
			xml=AppAskClass2Xml();
			//Post it to the server and wait for the server;
			HttpPostAndGet http=new HttpPostAndGet();
			xml=http.HttpsClientPOST("https://116.57.86.142/AppPost/appCheck/appResult.php", xml.getBytes());
			return AskXml2Class(xml);
		}
	}
}
