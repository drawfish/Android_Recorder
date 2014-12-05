package com.example.recordpro;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;


import android.util.Xml;

public class AppAskForContex extends UserDataClass{
	private getRecordContex recordcontex=null;
	public AppAskForContex()
	{
		recordcontex=new getRecordContex();
	}
	private String AppAskClass2Xml()
	{
		StringWriter os = new StringWriter();
		XmlSerializer xml=Xml.newSerializer();
		try {
			xml.setOutput(os);
			xml.startDocument("utf-8", null);
			xml.startTag(null, "appAction");
			xml.attribute(null, "action", recordcontex.getAction());
			xml.startTag(null, "username");
			xml.text(recordcontex.getUsername());
			xml.endTag(null, "username");
			xml.startTag(null, "password");
			xml.text(recordcontex.getPassword());
			xml.endTag(null, "password");
			xml.endTag(null, "appAction");
			xml.endDocument();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return os.toString();	
	}
	private recordContex AskResultXml2Class(String AskResult) throws Exception 
	{
		InputStream is=new ByteArrayInputStream(AskResult.getBytes("utf-8"));
		recordContex record_contex=null;
		XmlPullParser parser=Xml.newPullParser();
		parser.setInput(is,"utf-8");
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {  
            switch (eventType)
            {  
	            case XmlPullParser.START_DOCUMENT:  
	            	record_contex=new recordContex();
	                break;  
	            case XmlPullParser.START_TAG:
	            	String xmlName=parser.getName();
	            	if (xmlName.equals("serverAction")){
	            		record_contex.setAction(parser.getAttributeValue(null, "action"));
	            	}
	            	else if(xmlName.equals("username"))
	            	{
	            		parser.next(); 
	            		record_contex.setUsername(parser.getText());
	            	}
	            	else if(xmlName.equals("contexID"))
	            	{
	            		parser.next(); 
	            		record_contex.setRecordContexID(parser.getText());
	            	}
	            	else if (xmlName.equals("soundContex"))
	            	{
	            		parser.next(); 
	            		record_contex.setRecordContexContex(parser.getText());
	            	}
	                break;  
	            case XmlPullParser.END_TAG:   
	            	if(parser.getName().equals("serverAction"))
	            		return record_contex;
	                break;  
            }  
            eventType = parser.next();  
        }  
		return null;	
	}
	public recordContex getContextFromServer() throws Exception
	{
		if(recordcontex==null||recordcontex.getUsername().equals("")||recordcontex.getPassword().equals("")||recordcontex.getAction().equals(""))
		{
			return new recordContex();
		}
		else 
		{
			String xml=null;
			xml=AppAskClass2Xml();
			//Post it to the server and wait for the server;
			HttpPostAndGet http=new HttpPostAndGet();
			xml=http.HttpsClientPOST("https://116.57.86.142/AppPost/appContex/appgetContex.php", xml.getBytes());
			return AskResultXml2Class(xml);
		}
	}
}
