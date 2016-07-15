package kang.recyclerdb.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "ContractAddress", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ ContractColumns.TABLE_NAME+" ("+
                ContractColumns._ID +" INTEGER NOT NULL PRIMARY KEY, " +
                ContractColumns.NAME +" TEXT NOT NULL, " +
                ContractColumns.NAESUN +" TEXT NOT NULL, " +
                ContractColumns.NUMBER +" TEXT NOT NULL, " +
                ContractColumns.EMAIL +" TEXT NOT NULL, " +
                ContractColumns.DEPART +" TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}