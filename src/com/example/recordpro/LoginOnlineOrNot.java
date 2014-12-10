package com.example.recordpro;

public class LoginOnlineOrNot {
 	private static boolean loginOnline;
 	public static void setLoginOnlineOrNot(boolean Status)
 	{
 		loginOnline=Status;
 	}
 	public static boolean getLoginOnlineOrNot()
 	{
 		return loginOnline;
 	}
}
