package com.zaqoutabed.github.github_users_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class UserPageActivity extends AppCompatActivity {

    private ProgressBar webPageProgressBar;
    private WebView userPageWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        webPageProgressBar = findViewById(R.id.web_page_progress_bar);
        userPageWebView = findViewById(R.id.user_page_web_view);
        Intent intent = getIntent();
        if (intent.hasExtra("USER_NAME")){
            String userName = intent.getStringExtra("USER_NAME");
            loadPage(userName);
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadPage(String userName) {
        userPageWebView.loadUrl("https://github.com/"+userName);
        userPageWebView.getSettings().setJavaScriptEnabled(true);
        userPageWebView.setWebViewClient(new WebViewClient());
        userPageWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                webPageProgressBar.setProgress(newProgress);
                if (newProgress == 100)
                   UserPageActivity.this.findViewById(R.id.progress_bar_linear_layout).setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (userPageWebView.canGoBack()){
            userPageWebView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
