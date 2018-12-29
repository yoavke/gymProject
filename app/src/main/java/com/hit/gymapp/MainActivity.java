package com.hit.gymapp;

//TODO remove libraries I dont use
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webby = findViewById(R.id.myWebView);
        webby.getSettings().setJavaScriptEnabled(true);
        webby.setWebViewClient(new MyBrowser());
        webby.loadUrl("file:///android_asset/index.html");

        //creating instance of DatabaseHelper to control the Database
        DatabaseHelper myDb = new DatabaseHelper(this);

    }

    private class MyBrowser extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }
}
