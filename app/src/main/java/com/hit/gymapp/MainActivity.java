package com.hit.gymapp;

//TODO remove libraries I dont use
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


//TODO add stack for history of pages
public class MainActivity extends AppCompatActivity {

    public DatabaseHandler myDb;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating instance of DatabaseHelper to control the Database
        myDb = new DatabaseHandler(this);

        WebView webby = findViewById(R.id.myWebView);
        webby.getSettings().setJavaScriptEnabled(true);
        webby.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webby.setWebViewClient(new MyBrowser());
        webby.loadUrl("file:///android_asset/index.html");
        WebView.setWebContentsDebuggingEnabled(true);
        webby.addJavascriptInterface(new addInteraction(),"addToDb");
        webby.addJavascriptInterface(new browseInteraction(),"browseFromDb");
        webby.addJavascriptInterface(new detailsInteraction(),"detailsFromDb");
        webby.addJavascriptInterface(new kmInteraction(),"kmFromDb");
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
        public void addActivity(String activityId, String length)
        {
            //1 - the trainer id (might get dynamic in next versions
            //in this version, we have only one account
            myDb.addActivity(1,Integer.parseInt(activityId),Integer.parseInt(length));
        }
    }

    public class browseInteraction
    {
        @android.webkit.JavascriptInterface
        public String browseActivities(String activityId)
        {
            //TODO add filter to filter out other accounts (user_id)
            return myDb.browseActivities(Integer.parseInt(activityId));
        }
    }

    public class detailsInteraction
    {
        @android.webkit.JavascriptInterface
        public String selectDetails(int activityId)
        {
            //TODO add filter to filter out other accounts (user_id)
            return myDb.selectDetails(activityId);
        }
    }

    public class kmInteraction
    {
        @android.webkit.JavascriptInterface
        public String selectKm(int activityId)
        {
            //TODO add filter to filter out other accounts (user_id)
            return myDb.selectCharts(activityId);
        }
    }
}