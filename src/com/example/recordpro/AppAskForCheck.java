package com.example.recordpro;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Base64;
import android.util.Xml;


public class AppAskForCheck extends UserDataClass{
	private getRecordWav recordwav=null;
	public AppAskForCheck()
	{
		recordwav=new getRecordWav();
	}
	private String AppAskClass2Xml()
	{
		StringWriter os = new StringWriter();
		XmlSerializer xml=Xml.newSerializer();
		try {
			xml.setOutput(os);
			xml.startDocument("utf-8", null);
			xml.startTag(null, "appAction");
			xml.attribute(null, "action", recordwav.getAction());
			xml.startTag(null, "username");
			xml.text(recordwav.getUsername());
			xml.endTag(null, "username");
			xml.startTag(null, "password");
			xml.text(recordwav.getPassword());
			xml.endTag(null, "password");
			xml.endTag(null, "appAction");
			xml.endDocument();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return os.toString();	
	}
	private RecordWavData AskResultXml2Class(String AskResult) throws Exception 
	{
		InputStream is=new ByteArrayInputStream(AskResult.getBytes("utf-8"));
		RecordWavData record_data=null;
		XmlPullParser parser=Xml.newPullParser();
		parser.setInput(is,"utf-8");
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {  
            switch (eventType)
            {  
	            case XmlPullParser.START_DOCUMENT:  
	            	record_data=new RecordWavData();
	                break;  
	            case XmlPullParser.START_TAG:
	            	String xmlName=parser.getName();
	            	if (xmlName.equals("serverAction")){
	            		record_data.setAction(parser.getAttributeValue(null, "action"));
	            	}
	            	else if(xmlName.equals("username"))
	            	{
	            		parser.next(); 
	            		record_data.setUsername(parser.getText());
	            	}
	            	else if(xmlName.equals("contexID"))
	            	{
	            		parser.next(); 
	            		record_data.setRecordContexID(parser.getText());
	            	}
	            	else if (xmlName.equals("soundContex"))
	            	{
	            		parser.next(); 
	            		record_data.setRecordContex(parser.getText());
	            	}
	            	else if(xmlName.equals("soundData"))
	            	{
	            		parser.next();
	            		String recdataBase64=parser.getText();
	            		
	            		if(!recdataBase64.equals("null"))
	            			record_data.setRecordWavFile(Base64.decode(recdataBase64,Base64.DEFAULT));
	            		else
	            		{
	            			record_data.setRecordWavFile(null);
	            		}
	            	}
	                break;  
	            case XmlPullParser.END_TAG:   
	            	if(parser.getName().equals("serverAction"))
	            		return record_data;	            	
	                break;  
            }  
            eventType = parser.next();  
        }  
		return null;	
	}
	public RecordWavData getRecordFromServer() throws Exception
	{
		if(recordwav==null
				||recordwav.getUsername().equals("")
				||recordwav.getPassword().equals("")
				||recordwav.getAction().equals(""))
		{
			return new RecordWavData();
		}
		else 
		{
			String xml=null;
			xml=AppAskClass2Xml();
			//Post it to the server and wait for the server;
			HttpPostAndGet http=new HttpPostAndGet();
			xml=http.HttpsClientPOST("https://116.57.86.142/AppPost/appCheck/appCheck.php", xml.getBytes());
			return AskResultXml2Class(xml);
		}
	}	
}
