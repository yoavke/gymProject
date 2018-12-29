package com.hit.gymapp;

//TODO remove libraries I dont use
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public DatabaseHandler myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating instance of DatabaseHelper to control the Database
        myDb = new DatabaseHandler(this);

        WebView webby = findViewById(R.id.myWebView);
        webby.getSettings().setJavaScriptEnabled(true);
        webby.setWebViewClient(new MyBrowser());
        webby.loadUrl("file:///android_asset/index.html");
        webby.addJavascriptInterface(new addInteraction(),"addToDb");
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

    public class addInteraction
    {
        @android.webkit.JavascriptInterface
        public void addActivity(String activity)
        {
            int activity_category;

            if (activity.equals("running") || activity.equals("swimming") || activity.equals("bicycle")) {
                activity_category = 1;
            } else {
                activity_category = 2;
            }

            myDb.addActivity(activity_category,activity);
        }
    }


}
