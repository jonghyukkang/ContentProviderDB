package kang.contentapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by kangjonghyuk on 2016. 7. 13..
 */
public class MyContentProvider extends ContentProvider{
    private MyDatabaseHelper dbHelper;
    private static final int ALL_CONTENT = 1;
    private static final int SINGLE_CONTENT = 2;

    private static final String AUTHORITY = "kang.contentapp";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/peoples");
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "peoples", ALL_CONTENT);
        uriMatcher.addURI(AUTHORITY, "peoples/#", SINGLE_CONTENT);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ContentDB.SQLITE_TABLE);

        switch(uriMatcher.match(uri)){
            case ALL_CONTENT :
                break;
            case SINGLE_CONTENT :
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(ContentDB.KEY_PEOPLE + "=" + id);
                break;
            default :
                throw new IllegalArgumentException("Unsupported URI : " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)){
            case ALL_CONTENT :
                return "vnd.android.cursor.dir/vnd.kang.contentapp.peoples";
            case SINGLE_CONTENT :
                return "vnd.android.cursor.item/vnd.kang.contentapp.peoples";
            default :
                throw new IllegalArgumentException("Unsupported URI : " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch(uriMatcher.match(uri)){
            case ALL_CONTENT :
                break;
            default :
                throw new IllegalArgumentException("Unsupported URI : " + uri);
        }
        long id = db.insert(ContentDB.SQLITE_TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch(uriMatcher.match(uri)){
            case ALL_CONTENT :
                break;
            case SINGLE_CONTENT :
                String id = uri.getPathSegments().get(1);
                selection = ContentDB.KEY_PEOPLE + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default :
                throw new IllegalArgumentException("Unsupported URI : " + uri);
        }
        int deleteCount = db.delete(ContentDB.SQLITE_TABLE, selection, selectionArgs);
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch(uriMatcher.match(uri)){
            case ALL_CONTENT :
                break;
            case SINGLE_CONTENT :
                String id = uri.getPathSegments().get(1);
                selection = ContentDB.KEY_PEOPLE + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default :
                throw new IllegalArgumentException("Unsupported URI : " + uri);
        }
        int updateCount = db.update(ContentDB.SQLITE_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
