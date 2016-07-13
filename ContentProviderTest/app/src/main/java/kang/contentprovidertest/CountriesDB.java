package kang.contentprovidertest;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by kangjonghyuk on 2016. 7. 12..
 */
public class CountriesDB {

    public static final String KEY_WORID = "_id";
    public static final String KEY_CODE = "code";
    public static final String KEY_NAME = "name";
    public static final String KEY_CONTINENT = "continent";

    private static final String LOG_TAG = "CountriesDb";
    public static final String SQLITE_TABLE = "Country";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_WORID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_NAME + "," +
                    KEY_CONTINENT + "," +
                    " UNIQUE (" + KEY_CODE +"));";

    public static void onCreate(SQLiteDatabase db){
        Log.w(LOG_TAG, DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }
}
