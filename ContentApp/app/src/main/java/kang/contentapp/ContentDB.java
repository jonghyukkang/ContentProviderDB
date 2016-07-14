package kang.contentapp;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by kangjonghyuk on 2016. 7. 13..
 */
public class ContentDB {
    public static final String KEY_PEOPLE = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_NAESUN = "naesun";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_DEPART = "depart";

    private static final String LOG_TAG = "ContentDB";
    public static final String SQLITE_TABLE = "Depart";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_PEOPLE + " integer PRIMARY KEY autoincrement, " +
                    KEY_NAME   + "," +
                    KEY_NAESUN + "," +
                    KEY_NUMBER + "," +
                    KEY_EMAIL  + "," +
                    KEY_DEPART + "," +
                    " UNIQUE (" + KEY_NUMBER +"));";

    public static void onCreate(SQLiteDatabase db){
        Log.w(LOG_TAG, DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }
}
