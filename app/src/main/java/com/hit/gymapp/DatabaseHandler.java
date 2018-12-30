package com.hit.gymapp;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

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
        private static final String T_A_COL_5 = "minutes";
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
     * @param activityCategory - activity's category
     * @param activity - the activity
     */
    public void addActivity(int activityCategory, String activity)
    {
        //initialize ContentValues object
        ContentValues values = new ContentValues();

        //fill in the values
        values.put(DatabaseHandler.Columns.A_COL_2, activityCategory);
        values.put(DatabaseHandler.Columns.A_COL_3, activity);

        //make the insertion to the db
        if (db.insert(Tables.ACTIVITIES,null,values)!=-1)
        {
            this.showToast(this.context,"Activity added");
        } else {
            this.showToast(this.context,"Error: try again to add");
        }
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
