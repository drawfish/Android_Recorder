package com.example.recordpro;

import android.content.Context;
import android.widget.Toast;

public class ToastShow {
	public ToastShow(Context contex,String information){
		Toast toast=Toast.makeText(contex, information, Toast.LENGTH_SHORT);
		toast.show();
	}
}
