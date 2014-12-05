package com.example.recordpro;

public class UserDataClass {
	
	static public class appBasis{
		static private String username;
		static	private String password;
		private String action;
		public appBasis(){this.action="";}
		public void setUsername(String user){username=user;}
		public void setPassword(String passwd){password=passwd;}
		public void setAction(String ac){this.action=ac;}
		public String getUsername(){return username;}
		public String getPassword(){return password;}
		public String getAction(){return this.action;}
	}
	public appBasis getappBasis(){return new appBasis();}
	public class serverBasis{
		private String username;
		private String action;
		public serverBasis() {this.username="";this.action="";}
		public void setUsername(String user){this.username=user;}
		public void setAction(String ac){this.action=ac;}
		public String getUsername(){return this.username;}
		public String getAction(){return this.action;}
	}
	
	public class result extends serverBasis{
		private Boolean Results;
		private String Reason;
		public result() {this.Results=false;this.Reason="";}
		public void setResult(Boolean result){this.Results=result;}
		public Boolean getResult(){return this.Results;}
		public void setReason(String reason){this.Reason=reason;}
		public String getReason(){return this.Reason;}
	}
	
	//用户登录服务器
	//需要提供的信息：action="login" username password
	public class login extends appBasis{
		public login(){this.setAction("login");}
	}

	//服务器返回结果
	//需要检测信息：action==“Auth” username loginResult==true
	public class loginResult extends result{
		public void setLoginResult(Boolean result){this.setResult(result);}
		public Boolean getLoginResult(){return this.getResult();}
	}
	
	//app获取录音文本
	//需要提供信息;action="askforContex" username password
	public class getRecordContex extends appBasis{
		public getRecordContex(){this.setAction("askforContex");}
	}
	//服务器返回录音文本
	//需要检测信息：action=="sendoutContex"	username contexOfRecord
	public class recordContex extends serverBasis{
		private String contexOfRecord;
		private String contexOfRecordID;
		public recordContex(){this.contexOfRecord="";this.contexOfRecordID="0";}
		public void setRecordContexContex(String Contex){this.contexOfRecord=Contex;}
		public String getRecordContex(){return this.contexOfRecord;}
		public void setRecordContexID(String ID){this.contexOfRecordID=ID;}
		public String getRecordContexID(){return this.contexOfRecordID;}
	}
	//app上传录音内容
	//需要提供信息：action="recordUpload" username password contexOfRecord recordWavdata
	public class uploadRecord extends appBasis{
		private byte[] recordWavdata;
		private String recordContex;
		public uploadRecord(String wavFileName,byte[] wavData)
		{
			this.recordWavdata=null;
			this.setAction("recordUpload");
			this.recordContex=wavFileName;
			this.recordWavdata=wavData;
		}
		public byte[] getRecordWavData(){return this.recordWavdata;}
		public void setRecordWavData(byte[] data){recordWavdata=null;recordWavdata=data;}
		public void setRecordContex(String rContex){this.recordContex=rContex;}
		public String getRecordContex(){return this.recordContex;}
	}
	//服务器返回上传结果
	//需要检测内容：action="uploadResult" username contexOfRecord uploadResults==true
	public class uploadResult extends result{
		private String contexOfRecord;
		public uploadResult() {this.contexOfRecord="";}
		public void setUploadResult(Boolean result){this.setResult(result);}
		public void setContexOfRecord(String contex){this.contexOfRecord=contex;}
		public Boolean getUploadResult(){return this.getResult();}
		public String getContexOfRecord(){return this.contexOfRecord;}
	}
	//app删除服务器内容
	//需要上传信息：action="delete" username password deleteContexOfRecord
	public class deleteRecord extends appBasis{
		private String deleteContexOfRecord;
		public deleteRecord(){this.deleteContexOfRecord="";this.setAction("delete");}
		public deleteRecord(String deleteFilename){this.deleteContexOfRecord=deleteFilename;this.setAction("delete");}
		public void setRecordContexContex(String Contex){this.deleteContexOfRecord=Contex;}
		public String getdeleteContex(){return this.deleteContexOfRecord;}
	}
	//服务器返回删除结果
	//需要检测的内容：action=“deleteResult” username deleteContexOfRecord deleteresult
	public class deleteResult extends result{
		private String deleteContexOfRecord;
		public deleteResult(){this.deleteContexOfRecord="";}
		public void setRecordContexContex(String Contex){this.deleteContexOfRecord=Contex;}
		public void setDeleteResult(Boolean result){this.setResult(result);}
		public String getRecordContex(){return this.deleteContexOfRecord;}
		public Boolean getDelateResult(){return this.getResult();}
	}
	//app请求纠错文件
	//需要上传信息：action="askforRecordFile" username password 
	public class getRecordWav extends appBasis{
		public  getRecordWav(){this.setAction("askforRecordFile");}
	}
	//服务器返回纠错文件
	//需要检测内容：action=="sendoutRecordFile" username RecordFileContex(with recorder name) RecordFile
	public class RecordWavData extends serverBasis{
		private String recordContex;
		private byte[] recordWavFile;
		private String recordID;
		public RecordWavData(){this.recordContex="";this.recordWavFile=null;this.recordID="";}
		public void setRecordContex(String contex){recordContex=contex;}
		public void setRecordContexID(String ID){this.recordID=ID;}
		public void setRecordWavFile(byte[] file){recordWavFile=null;recordWavFile=file;}
		public String getRecordContex(){return this.recordContex;}
		public String getRecordContexID(){return this.recordID;}
		public byte[] getRecordWavFile(){return this.recordWavFile;}
	}
	//app上传纠错结果
	//需要上传信息：action=“CheckoutResult” username password RecordFileContex(with recorder name) checkoutResult
	public class checkoutResult extends appBasis{
		private String recordContex;
		private Boolean checkoutResult;
		private String recordContexID;
		public checkoutResult(String recContextID,String recContext,boolean recQuality){this.setAction("CheckoutResult");this.recordContexID=recContextID;this.recordContex=recContext;this.checkoutResult=recQuality;}
		public void setRecordContex(String contex){this.recordContex=contex;}
		public void setCheckoutResult(Boolean result){this.checkoutResult=result;}
		public String getRecordContex(){return this.recordContex;}
		public Boolean getCheckoutResult(){return this.checkoutResult;}
		public void setContexID(String ID){this.recordContexID=ID;}
		public String getContexID(){return this.recordContexID;}
	}
	//服务器返回纠错确认结果
	//需要检测的内容：action=“confirmResult” username confirmResult
	public class confirmResult extends result{
		public void setConfirmResult(boolean result){this.setResult(result);}
		public boolean getConfirmResult(){return this.getResult();}
	}
}
