package com.example.recordpro;

public class AutoUpload {
	private static boolean EnableUpload=false;
	public static void setAutoUpload(boolean Satus){EnableUpload=Satus;}
	public static boolean getAutoUploadStatus(){return EnableUpload;}
}
