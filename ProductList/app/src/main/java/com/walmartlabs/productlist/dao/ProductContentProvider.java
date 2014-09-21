package com.walmartlabs.productlist.dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.util.Constants;

public class ProductContentProvider extends ContentProvider {

    public static final String RAW = "raw_query";
    public static final String ONE_ROW_LIMIT = "one_row_limit";
    public static final String ORDER_BY = " ORDER BY ";
    private static String AUTHORITY;
    public static Uri BASE_CONTENT_URI;
    protected ProductSQLHelper sqlHelper;

    @Override
    public boolean onCreate() {
        AUTHORITY = getContext().getString(R.string.content_provider_autorithies);
        BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
        sqlHelper = ProductSQLHelper.getInstance(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd." + AUTHORITY;
    }

    @Override
    public Cursor query(final Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        SQLiteDatabase mDb = sqlHelper.getReadableDatabase();
        String table = uri.getLastPathSegment();
        Uri listenedUri;

        if (uri.toString().contains(RAW)) {
            listenedUri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();
            StringBuilder sql = new StringBuilder(selection);
            if (!TextUtils.isEmpty(sortOrder)) {
                sql.append(ORDER_BY).append(sortOrder);
            }
            cursor = mDb.rawQuery(sql.toString(), selectionArgs);
        } else if (uri.toString().contains(ONE_ROW_LIMIT)) {
            cursor = mDb.query(false, table, projection, selection, selectionArgs, null, null, sortOrder, "1");
            listenedUri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();
        } else {
            listenedUri = uri;
            cursor = mDb.query(false, table, projection, selection, selectionArgs, null, null, sortOrder, null);
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), listenedUri);
        }

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table = uri.getLastPathSegment();
        SQLiteDatabase mDb = sqlHelper.getWritableDatabase();

        mDb.beginTransaction();

        try {
            mDb.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            mDb.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (SQLiteDiskIOException ignored) {

        } finally {
            mDb.endTransaction();
        }

        return uri;
    }

    @Override
    public synchronized int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
        String table = uri.getLastPathSegment();
        SQLiteDatabase mDb = sqlHelper.getWritableDatabase();
        mDb.beginTransaction();

        try {
            for (ContentValues value : values) {
                mDb.insertWithOnConflict(table, null, value, SQLiteDatabase.CONFLICT_REPLACE);
            }

            mDb.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (SQLiteDiskIOException ignored) {

        } finally {
            mDb.endTransaction();
        }

        return values.length;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String table = uri.getLastPathSegment();
        SQLiteDatabase mDb = sqlHelper.getWritableDatabase();
        mDb.beginTransaction();
        int count;
        try {
            count = mDb.update(table, values, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }

        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = uri.getLastPathSegment();
        SQLiteDatabase mDb = sqlHelper.getWritableDatabase();
        int count = mDb.delete(table, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

}