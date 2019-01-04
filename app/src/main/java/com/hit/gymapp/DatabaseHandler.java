package com.hit.gymapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class DatabaseHandler extends SQLiteOpenHelper {

    private SQLiteDatabase db;  //hold ref to the db
    private Context context;    //active activity

    //the database of the app
    public static final String DATABASE_NAME = "Gym.db";

    //all tables
    abstract class Tables{
        private static final String TRAINERS = "trainers";
        private static final String ACTIVITIES = "Activities";
        private static final String TRAINER_ACTIVITY = "Trainer_Activity";
    }

    //all table columns
    abstract class Columns {
        //Trainers table columns
        private static final String T_COL_1 = "_id";
        private static final String T_COL_2 = "first_name";
        private static final String T_COL_3 = "last_name";
        private static final String T_COL_4 = "age";

        //Activities table columns
        private static final String A_COL_1 = "_id";
        private static final String A_COL_2 = "activity_category";
        private static final String A_COL_3 = "activity";

        //Trainer_Activity table columns
        private static final String T_A_COL_1 = "_id";
        private static final String T_A_COL_2 = "trainer_id";
        private static final String T_A_COL_3 = "activity_id";
        private static final String T_A_COL_4 = "timestamp";
        private static final String T_A_COL_5 = "length";
    }

    /***
     * Constructor
     * @param context
     */
    public DatabaseHandler(Context context) {

        //calling SQLiteOpenHelper constructor
        super(context,DATABASE_NAME,null,1 );

        //set which activity is using the database
        this.context = context;

        /*  must be last line in the constructor,
            in order to make sure all variables are initialized prior to onCreate() */
        this.db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //create Trainers table
        db.execSQL("CREATE TABLE " + Tables.TRAINERS + "("+Columns.T_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+Columns.T_COL_2+" TEXT,"+Columns.T_COL_3+" TEXT,"+Columns.T_COL_4+" INTEGER)");

        //create Activites table
        db.execSQL("CREATE TABLE " + Tables.ACTIVITIES + "("+Columns.A_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+Columns.A_COL_2+" INTEGER,"+Columns.A_COL_3+" TEXT)");

        //create Trainer_Activity table
        db.execSQL("CREATE TABLE " + Tables.TRAINER_ACTIVITY + "("+Columns.T_A_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+Columns.T_A_COL_2+" INTEGER,"+Columns.T_A_COL_3+" INTEGER,"+Columns.T_A_COL_4+" INTEGER,"+Columns.T_A_COL_5+" INTEGER)");

        //set default values
        db.execSQL("INSERT INTO "+Tables.ACTIVITIES+"("+Columns.A_COL_2+","+Columns.A_COL_3+") VALUES(1,'running'),(1,'swimming'),(1,'bicycle'),(2,'chest'),(2,'back'),(2,'legs')");
        db.execSQL("INSERT INTO "+Tables.TRAINERS+"("+Columns.T_COL_2+","+Columns.T_COL_3+","+Columns.T_COL_4+") VALUES('trainee','trainee',18)");

        this.showToast(this.context,"initialized");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " +Tables.ACTIVITIES);
        db.execSQL("DROP TABLE " +Tables.TRAINER_ACTIVITY);
        db.execSQL("DROP TABLE " +Tables.TRAINERS);
        onCreate(db);
    }

    //TODO modify this method. it puts values to the wrong table! need to change to Trainer_Activity table (T_A)
    /***
     * Adding a new activity to the DB
     * @param userId - the id of the trainer
     * @param activityId - id for activity to be added
     * @param length - length in km/minuted depend on the activity id (aerobic/anaerobic)
     */
    public void addActivity(int userId, int activityId, int length)
    {
        //timestamp the activity
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Long time = timestamp.getTime();

        //initialize ContentValues object
        ContentValues values = new ContentValues();

        //fill in the values
        values.put(Columns.T_A_COL_2, userId);
        values.put(Columns.T_A_COL_3, activityId);
        values.put(Columns.T_A_COL_4, time);
        values.put(Columns.T_A_COL_5, length);

        //make the insertion to the db
        if (db.insert(Tables.TRAINER_ACTIVITY,null,values)!=-1)
        {
            this.showToast(this.context,"Activity added");
        } else {
            this.showToast(this.context,"Error: try again to add");
        }
    }

    @android.webkit.JavascriptInterface
    public String browseActivities(int activityId) {

        //initialize JSON object to store the dataset of the query
        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();
        json.put("activities",arr);

        //save the dataset inside a cursor
        //"SELECT "+BaseColumns._ID+",                              activity_id,timestamp,length,activity FROM "+Tables.TRAINER_ACTIVITY+" INNER JOIN Activities ON Trainer_Activity.activity_id=Activities._id WHERE "+Columns.T_A_COL_3+"=?",new String[] {search}
        //"SELECT "+BaseColumns._ID+",activity_id,timestamp,length,activity FROM "+Tables.TRAINER_ACTIVITY+" INNER JOIN Activities ON Trainer_Activity.activity_id=Activities._id WHERE "+Columns.T_A_COL_3+"=1",null
        Cursor cursor = db.rawQuery("SELECT Trainer_Activity._id as T_A_ID,Trainer_Activity.trainer_id,Trainer_Activity.activity_id,Trainer_Activity.timestamp,Trainer_Activity.length,Activities._id as A_ID,Activities.activity_category,Activities.activity FROM "+Tables.TRAINER_ACTIVITY+" INNER JOIN "+Tables.ACTIVITIES+" ON "+Tables.TRAINER_ACTIVITY+"."+Columns.T_A_COL_3+"="+Tables.ACTIVITIES+"."+BaseColumns._ID+" WHERE "+Tables.ACTIVITIES+"."+Columns.A_COL_2+"=?",new String[] {String.valueOf(activityId)});



        //looping through the cursor to put the data in the json
        try {
            while (cursor.moveToNext()) {
                JSONObject item = new JSONObject();
                item.put(BaseColumns._ID,cursor.getString(cursor.getColumnIndex("T_A_ID")));
                item.put("activity_id",cursor.getString(cursor.getColumnIndex("activity_id")));
                item.put("timestamp",cursor.getString(cursor.getColumnIndex("timestamp")));
                item.put("length",cursor.getString(cursor.getColumnIndex("length")));
                item.put("activity",cursor.getString(cursor.getColumnIndex("activity")));
                arr.add(item);
            }
        } finally {
            //close the cursor
            cursor.close();
        }

        //return json format with all data from the database
        return json.toJSONString();
    }

    public String selectDetails(int activityId)
    {


        //initialize JSON object to store the dataset of the query
        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();
        json.put("activities",arr);

        Cursor cursor = db.rawQuery("SELECT Trainer_Activity._id as T_A_ID,Trainer_Activity.trainer_id,Trainer_Activity.activity_id,Trainer_Activity.timestamp,Trainer_Activity.length,Activities._id as A_ID,Activities.activity_category,Activities.activity FROM  "+Tables.TRAINER_ACTIVITY+" INNER JOIN "+Tables.ACTIVITIES+" ON "+Tables.TRAINER_ACTIVITY+"."+Columns.T_A_COL_3+"="+Tables.ACTIVITIES+"."+BaseColumns._ID+" WHERE Trainer_Activity._id=?",new String[] {String.valueOf(activityId)});

        //looping through the cursor to put the data in the json
        try {
            while (cursor.moveToNext()) {
                JSONObject item = new JSONObject();
                item.put(BaseColumns._ID,cursor.getString(cursor.getColumnIndex("T_A_ID")));
                item.put("activity_id",cursor.getString(cursor.getColumnIndex("activity_id")));
                item.put("timestamp",cursor.getString(cursor.getColumnIndex("timestamp")));
                item.put("length",cursor.getString(cursor.getColumnIndex("length")));
                item.put("activity",cursor.getString(cursor.getColumnIndex("activity")));
                arr.add(item);
            }
        } finally {
            //close the cursor
            cursor.close();
        }

        //return json format with all data from the database
        return json.toJSONString();
    }

    public String selectCharts(int category)
    {
        long millisecondsPerDay = 86400000; //how many milliseconds in a day
        long subToSundayMidnight = 0;       //how many days I need to substract from today
        long sundayMidnight;                //millisecond on last Sunday's night

        //1 - sun 7 - sat
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //get milliseconds of today's midnight
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long todayMidnight = c.getTimeInMillis();

        //set how many days I need to substract
        for (int i=0;i<dayOfWeek;i++) {
            subToSundayMidnight += 1;
        }

        //select where timestamp is greater than this value
        sundayMidnight = todayMidnight - (subToSundayMidnight*millisecondsPerDay);

        //initialize JSON object to store the dataset of the query
        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();
        json.put("activities",arr);

        String length = new String((category==1)?"km":"minutes");

        //Selecting data from db
        Cursor cursor = db.rawQuery("SELECT sum(length) as "+length+",activity_id,Activities.activity as activity FROM Trainer_Activity INNER JOIN Activities ON Activities._id=Trainer_Activity.activity_id WHERE timestamp>"+sundayMidnight+" AND Activities.activity_category="+category+" GROUP BY activity_id",null);

        //looping through the cursor to put the data in the json
        try {
            while (cursor.moveToNext()) {
                JSONObject item = new JSONObject();
                item.put(length,cursor.getString(cursor.getColumnIndex(length)));
                item.put("activity_id",cursor.getString(cursor.getColumnIndex("activity_id")));
                item.put("activity",cursor.getString(cursor.getColumnIndex("activity")));
                arr.add(item);
            }
        } finally {
            //close the cursor
            cursor.close();
        }

        //return json format with all data from the database
        return json.toJSONString();
    }


    //TODO implement deletion method
    /***
     * Removing a row with a specified ID from the DB
     * @param activityId - this id will be removed from the DB
     */
    public void deleteActivity(int activityId)
    {
        //id to be removed from the DB
        String[] activities_to_delete = {String.valueOf(activityId)};

        //make the deletion
        db.delete(Tables.TRAINER_ACTIVITY,BaseColumns._ID+"=?",activities_to_delete);
    }

    public String browseActivites(int activityCategory) {
        return new String("hello");
    }

    /***
     * Show Toast with specified string
     * @param context - the activity we use to show the Toast
     * @param t - the string of the Toast
     */
    public void showToast(Context context, String t)
    {
        Toast.makeText(context,t,Toast.LENGTH_LONG).show();
    }

    //TODO remove this method (doesDatabaseExist)
    /***
     *
     * @param context always this
     * @param dbName database to check if exists
     * @return true if db exists and false if not
     */
//    public static boolean doesDatabaseExist(Context context, String dbName) {
//        File dbFile = context.getDatabasePath(dbName);
//        return dbFile.exists();
//    }
}
