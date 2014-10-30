package com.example.recordpro;

import android.os.Bundle;

public class MainActivity extends MainButtonListenerActivitySub {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init()
    {
    	 mainActivityButtonInit();
    }
    
}
