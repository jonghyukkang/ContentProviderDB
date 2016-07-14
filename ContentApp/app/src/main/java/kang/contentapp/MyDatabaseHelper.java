package kang.contentapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kangjonghyuk on 2016. 7. 13..
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ContentAddress";
    private static final int DATABASE_VERSION = 1;

    MyDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ContentDB.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ContentDB.onUpgrade(db, oldVersion, newVersion);
    }
}
