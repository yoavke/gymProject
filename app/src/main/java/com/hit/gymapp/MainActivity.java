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

        //create webview
        WebView webby = findViewById(R.id.myWebView);

        //set configuration for the webview object
        webby.getSettings().setJavaScriptEnabled(true);
        webby.getSettings().setAllowUniversalAccessFromFileURLs(true);
        WebView.setWebContentsDebuggingEnabled(true);
        webby.setWebViewClient(new MyBrowser());

        //load main page and display it in the current activity
        webby.loadUrl("file:///android_asset/index.html");

        //add interactions with the js code (functions.js) to integrate with the DB
        webby.addJavascriptInterface(new addInteraction(),"addToDb");                   //add activities
        webby.addJavascriptInterface(new browseInteraction(),"browseFromDb");           //browse activities
        webby.addJavascriptInterface(new detailsInteraction(),"detailsFromDb");         //get details about activitiy
        webby.addJavascriptInterface(new kmInteraction(),"kmFromDb");                   //get details for charts
        webby.addJavascriptInterface(new deleteInteraction(), "deleteActivityFromDb");  //delete activity
        webby.addJavascriptInterface(new updateInteraction(), "updateDb");  //delete activity
    }

    //extend WebViewClient to make links load inside the webview and not in a new chrome app
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
        public String browseActivities(String activityId,String startDate,String endDate)
        {
            //TODO add filter to filter out other accounts (user_id)
            return myDb.browseActivities(Integer.parseInt(activityId),startDate,endDate);
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

    public class deleteInteraction {

        @android.webkit.JavascriptInterface
        public void deleteActivity(int activityId) {
            //TODO add filter to filter out other accounts (user_id)
            myDb.deleteActivity(activityId);
        }
    }

    public class updateInteraction {

        @android.webkit.JavascriptInterface
        public void updateActivity(String activityId, String length, String date) {
            //TODO add filter to filter out other accounts (user_id)
            myDb.updateActivity(Integer.parseInt(activityId),length,date);
        }
    }
}