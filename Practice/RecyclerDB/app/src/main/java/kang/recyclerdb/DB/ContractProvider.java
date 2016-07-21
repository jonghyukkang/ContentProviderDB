package kang.recyclerdb.DB;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ContractProvider extends ContentProvider {

    public static final int MENSAGENS = 1;
    public static final int MENSAGENS_POR_ID = 2;

    UriMatcher mUriMatcher;
    DbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(ContractColumns.AUTHORITY, "msgs", MENSAGENS);
        mUriMatcher.addURI(ContractColumns.AUTHORITY, "msgs/#", MENSAGENS_POR_ID);

        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("No Implements.");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (mUriMatcher.match(uri) == MENSAGENS){
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            long id = db.insert(ContractColumns.TABLE_NAME, null, values);
            Uri insertUri = Uri.withAppendedPath(ContractColumns.BASE_URI, String.valueOf(id));
            db.close();
            notifyChanges(uri);
            return insertUri;
        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (mUriMatcher.match(uri) == MENSAGENS_POR_ID){
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            int deleteCount = db.delete(ContractColumns.TABLE_NAME,
                    ContractColumns._ID +" = ?",
                    new String[]{ uri.getLastPathSegment() });
            db.close();
            notifyChanges(uri);
            return deleteCount;

        } else {
            throw new UnsupportedOperationException("Uri invalid.");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (mUriMatcher.match(uri) == MENSAGENS_POR_ID){
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            int updateCount = db.update(ContractColumns.TABLE_NAME,
                    values,
                    ContractColumns._ID +" = ?",
                    new String[]{ uri.getLastPathSegment() });
            db.close();
            notifyChanges(uri);
            return updateCount;

        } else {
            throw new UnsupportedOperationException("Uri invalid.");
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if (mUriMatcher.match(uri) == MENSAGENS){
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor cursor = db.query(ContractColumns.TABLE_NAME,
                    projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;

        } else if (mUriMatcher.match(uri) == MENSAGENS_POR_ID) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor cursor = db.query(ContractColumns.TABLE_NAME,
                    projection,
                    ContractColumns._ID +" = ?",
                    new String[]{ uri.getLastPathSegment() }, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;

        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    private void notifyChanges(Uri uri){
        if (getContext() != null && getContext().getContentResolver() != null){
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }
}