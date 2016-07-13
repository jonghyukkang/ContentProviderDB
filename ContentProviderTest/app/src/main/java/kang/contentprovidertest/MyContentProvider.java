package kang.contentprovidertest;

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
 * Created by kangjonghyuk on 2016. 7. 12..
 */
public class MyContentProvider extends ContentProvider {

    private MyDatabaseHelper dbHelper;
    private static final int ALL_COUNTIRES = 1;
    private static final int SINGLE_COUNTRY = 2;

    private static final String AUTHORITY = "kang.contentprovidertest";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/countries");

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "countries", ALL_COUNTIRES);
        uriMatcher.addURI(AUTHORITY, "countries/#", SINGLE_COUNTRY);
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
        queryBuilder.setTables(CountriesDB.SQLITE_TABLE);

        switch (uriMatcher.match(uri)) {
            case ALL_COUNTIRES:
                break;
            case SINGLE_COUNTRY:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(CountriesDB.KEY_WORID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsuported URI : " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_COUNTIRES:
                return "vnd.android.cursor.dir/vnd.kang.contentprovidertest.countries";
            case SINGLE_COUNTRY:
                return "vnd.android.cursor.item/vnd.kang.contentprovidertest.countries";
            default:
                throw new IllegalArgumentException("Unsupported URI : " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_COUNTIRES:
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI : " + uri);
        }
        long id = db.insert(CountriesDB.SQLITE_TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_COUNTIRES:
                break;
            case SINGLE_COUNTRY:
                String id = uri.getPathSegments().get(1);
                selection = CountriesDB.KEY_WORID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI : " + uri);
        }

        int deleteCount = db.delete(CountriesDB.SQLITE_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_COUNTIRES:
                break;
            case SINGLE_COUNTRY:
                String id = uri.getPathSegments().get(1);
                selection = CountriesDB.KEY_WORID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI : " + uri);
        }
        int updateCount = db.update(CountriesDB.SQLITE_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
