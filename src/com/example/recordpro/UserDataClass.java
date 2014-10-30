package com.example.recordpro;

public class UserDataClass {
	
	public class appBasis{
		private String username;
		private String password;
		private String action;
		public appBasis(){this.username="";this.password="";this.action="";}
		public void setUsername(String user){this.username=user;}
		public void setPassword(String passwd){this.password=passwd;}
		public void setAction(String ac){this.action=ac;}
		public String getUsername(){return this.username;}
		public String getPassword(){return this.password;}
		public String getAction(){return this.action;}
	}
	
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
		public result() {this.Results=false;}
		public void setResult(Boolean result){this.Results=result;}
		public Boolean getResult(){return this.Results;}
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
	public class recordContex extends appBasis{
		private String contexOfRecord;
		public recordContex(){this.contexOfRecord="";}
		public void setRecordContexContex(String Contex){this.contexOfRecord=Contex;}
		public String getRecordContex(){return this.contexOfRecord;}
	}
	//app上传录音内容
	//需要提供信息：action="recordUpload" username password contexOfRecord recordWavdata
	public class uploadRecord extends recordContex{
		private byte[] recordWavdata;
		public uploadRecord(){this.recordWavdata=null;this.setAction("recordUpload");}
		public byte[] getRecordWavData(){return this.recordWavdata;}
		public void setRecordWavData(byte[] data){recordWavdata=null;recordWavdata=data;}
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
		public void setRecordContexContex(String Contex){this.deleteContexOfRecord=Contex;}
		public String getRecordContex(){return this.deleteContexOfRecord;}
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
	public class RecordWav extends serverBasis{
		private String recordContex;
		private byte[] recordWavFile;
		public RecordWav(){this.recordContex="";this.recordWavFile=null;}
		public void setRecordContex(String contex){recordContex=contex;}
		public void setRecordWavFile(byte[] file){recordWavFile=null;recordWavFile=file;}
		public String getRecordContex(){return this.recordContex;}
		public byte[] getRecordWavFile(){return this.recordWavFile;}
	}
	//app上传纠错结果
	//需要上传信息：action=“CheckoutResult” username password RecordFileContex(with recorder name) checkoutResult
	public class checkoutResult extends appBasis{
		private String recordContex;
		private Boolean checkoutResult;
		public checkoutResult(){this.setAction("CheckoutResult");this.recordContex="";this.checkoutResult=false;}
		public void setRecordContex(String contex){this.recordContex=contex;}
		public void setCheckoutResult(Boolean result){this.checkoutResult=result;}
		public String getRecordContex(){return this.recordContex;}
		public Boolean getCheckoutResult(){return this.checkoutResult;}
	}

}
