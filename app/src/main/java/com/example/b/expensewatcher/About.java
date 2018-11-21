package com.example.b.expensewatcher;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class About extends AppCompatActivity {

    WebView wv_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        WebView wv_about = (WebView) findViewById(R.id.wv_about);
        wv_about.setWebViewClient(new AboutWebViewClient());
        wv_about.loadUrl("http://clearskiestechnology.com/");

    }

    private class AboutWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
             if (Uri.parse(url).getHost().equals("http://clearskiestechnology.com/")) {
                    // This is my website, so do not override; let my WebView load the page
                    return false;
              }
               // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
               startActivity(intent);
               return true;
            }
    }
}
