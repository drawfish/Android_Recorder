package com.example.recordpro;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Base64;
import android.util.Xml;


public class AppAskForUpload extends UserDataClass{
	
	private uploadRecord uploadrecord=null;
	public AppAskForUpload(String wavFilePath,String wavFileName) throws IOException
	{
		FileInputStream file=new FileInputStream(wavFileName);
		byte[] wavDataStream=new byte[file.available()];
		file.read(wavDataStream);
		String wavName=wavFileName.substring(wavFilePath.length());
		uploadrecord=new uploadRecord(wavName,wavDataStream);
		file.close();
	}

	private String AppAskClass2Xml()
	{
		StringWriter os = new StringWriter();
		XmlSerializer xml=Xml.newSerializer();
		try {
			xml.setOutput(os);
			xml.startDocument("utf-8", null);
			xml.startTag(null, "appAction");
			xml.attribute(null, "action", uploadrecord.getAction());
			xml.startTag(null, "username");
			xml.text(uploadrecord.getUsername());
			xml.endTag(null, "username");
			xml.startTag(null, "password");
			xml.text(uploadrecord.getPassword());
			xml.endTag(null, "password");
			xml.startTag(null, "soundContex");
			xml.text(uploadrecord.getRecordContex());
			xml.endTag(null, "soundContex");
			xml.startTag(null, "recordData");
			xml.text(Base64.encodeToString(uploadrecord.getRecordWavData(),Base64.DEFAULT));
			xml.endTag(null,"recordData");
			xml.endTag(null, "appAction");
			xml.endDocument();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return os.toString();	
	}
	private uploadResult AskResultXml2Class(String AskResult) throws Exception 
	{
		InputStream is=new ByteArrayInputStream(AskResult.getBytes("utf-8"));
		uploadResult upload_result=null;
		XmlPullParser parser=Xml.newPullParser();
		parser.setInput(is,"utf-8");
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {  
            switch (eventType)
            {  
	            case XmlPullParser.START_DOCUMENT:  
	            	upload_result=new uploadResult();
	                break;  
	            case XmlPullParser.START_TAG:
	            	String xmlName=parser.getName();
	            	if (xmlName.equals("serverAction")){
	            		upload_result.setAction(parser.getAttributeValue(null, "action"));
	            	}
	            	else if(xmlName.equals("username"))
	            	{
	            		parser.next(); 
	            		upload_result.setUsername(parser.getText());
	            	}
	            	else if (xmlName.equals("soundContex"))
	            	{
	            		parser.next(); 
	            		upload_result.setContexOfRecord(parser.getText());
	            	}
	            	else if (xmlName.equals("uploadResult"))
	            	{
	            		parser.next(); 
	            		if(parser.getText().equals("true"))
	            		{
	            			upload_result.setUploadResult(true);
	            		}
	            		else 
	            			upload_result.setUploadResult(false);
	            	}
	                break;  
	            case XmlPullParser.END_TAG:   
	            	if(parser.getName().equals("serverAction"))
	            		return upload_result;
	                break;  
            }  
            eventType = parser.next();  
        }  
		return null;	
	}
	public boolean getUploadResultFromServer() throws Exception
	{
		if(uploadrecord==null
				||uploadrecord.getUsername().equals("")
				||uploadrecord.getPassword().equals("")
				||uploadrecord.getAction().equals("")
				||uploadrecord.getRecordContex().equals("")
				||uploadrecord.getRecordWavData()==null)
		{
			return false;
		}
		else 
		{
			String xml=null;
			xml=AppAskClass2Xml();
			//Log.i("PostData",xml);
			//Post it to the server and wait for the server;
			HttpPostAndGet http=new HttpPostAndGet();
			xml=http.HttpsClientPOST("https://116.57.86.142/AppPost/appUpload/appUpload.php", xml.getBytes());
			//Log.i("收到的结果：",xml);
			return AskResultXml2Class(xml).getUploadResult();
		}
	}
}
