package com.sheepsqueezers.equityyostocks;

import com.sheepsqueezers.equityyostocks.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class UsersGuideActivity extends Activity {
	
	private WebView oWVMD;
	private ProgressDialog progressDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_users_guide);
        
        progressDialog = ProgressDialog.show(this, "Loading", "...one moment please...");        
        oWVMD = (WebView) findViewById(R.id.webViewUsersGuide);
        oWVMD.setWebViewClient(new WebViewClient() {

           @Override
           public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	   super.shouldOverrideUrlLoading(view, url);

	           oWVMD.loadUrl(url);
	           return true;
           }

           @Override
           public void onPageFinished(WebView view, String url) {
        	   super.onPageFinished(view, url);
                   if (progressDialog.isShowing()) {
                       progressDialog.dismiss();
                   }
           }
        });        
        oWVMD.loadUrl("file:///android_asset/eyug.html");
    }
    
}
