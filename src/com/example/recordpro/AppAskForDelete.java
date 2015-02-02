package com.example.recordpro;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;


public class AppAskForDelete extends UserDataClass{
	private deleteRecord deleteCmd=null;
	public AppAskForDelete(String recContext)
	{
		deleteCmd=new deleteRecord(recContext);
	}
	private String AppAskClass2Xml()
	{
		StringWriter os = new StringWriter();
		XmlSerializer xml=Xml.newSerializer();
		try {
			xml.setOutput(os);
			xml.startDocument("utf-8", null);
			xml.startTag(null, "appAction");
			xml.attribute(null, "action", deleteCmd.getAction());
			xml.startTag(null, "username");
			xml.text(deleteCmd.getUsername());
			xml.endTag(null, "username");
			xml.startTag(null, "password");
			xml.text(deleteCmd.getPassword());
			xml.endTag(null, "password");
			xml.startTag(null, "soundContex");
			xml.text(deleteCmd.getdeleteContex());
			xml.endTag(null, "soundContex");
			xml.endTag(null, "appAction");
			xml.endDocument();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return os.toString();	
	}
	private deleteResult AskClassXml2Class(String AskResult) throws Exception 
	{
		InputStream is=new ByteArrayInputStream(AskResult.getBytes("utf-8"));
		deleteResult delete_Result=null;
		XmlPullParser parser=Xml.newPullParser();
		parser.setInput(is,"utf-8");
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {  
            switch (eventType)
            {  
	            case XmlPullParser.START_DOCUMENT:  
	            	delete_Result=new deleteResult();
	                break;  
	            case XmlPullParser.START_TAG:
	            	String xmlName=parser.getName();
	            	if (xmlName.equals("serverAction")){
	            		delete_Result.setAction(parser.getAttributeValue(null, "action"));
	            	}
	            	else if(xmlName.equals("username"))
	            	{
	            		parser.next(); 
	            		delete_Result.setUsername(parser.getText());
	            	}
	            	else if (xmlName.equals("soundContex"))
	            	{
	            		parser.next(); 
	            		delete_Result.setRecordContexContex(parser.getText());
	            	}
	            	else if (xmlName.equals("deleteResult"))
	            	{
	            		parser.next(); 
	            		if(parser.getText().equals("true"))
	            		{
	            			delete_Result.setDeleteResult(true);
	            		}
	            		else 
	            			delete_Result.setDeleteResult(false);
	            	}
	                break;  
	            case XmlPullParser.END_TAG:   
	            	if(parser.getName().equals("serverAction"))
	            		return delete_Result;
	                break;  
            }  
            eventType = parser.next();  
        }  
		return null;	
	}
	public String getContextFromServer(String recContext) throws Exception
	{
		if(deleteCmd==null||deleteCmd.getUsername().equals("")||deleteCmd.getPassword().equals("")||deleteCmd.getdeleteContex().equals(""))
		{
			return "";
		}
		else 
		{
			String xml=null;
			xml=AppAskClass2Xml();
			//Post it to the server and wait for the server;
			HttpPostAndGet http=new HttpPostAndGet();
			xml=http.HttpClientPOST("http://125.217.245.125:10080/AppPost/appLogin/deleteAudio.php", xml.getBytes());
			return AskClassXml2Class(xml).getRecordContex();
		}
	}
}
