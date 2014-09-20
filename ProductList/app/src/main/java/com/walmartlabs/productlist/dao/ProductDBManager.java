package com.walmartlabs.productlist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;

public class ProductDBManager {
    private static final String LOG_TAG = ProductDBManager.class.getSimpleName();
    protected Context mContext;
    private static ProductDBManager sDBManager;

    private static final String AND_CONJUNCTION = " =? AND ";
    private static final String AND = " AND ";
    private static final int AND_LENGTH = AND.length();

    public static ProductDBManager getInstance(Context ctx) {
        if (sDBManager == null) {
            sDBManager = new ProductDBManager(ctx.getApplicationContext());
        }

        return sDBManager;
    }

    private ProductDBManager(Context context) {
        mContext = context.getApplicationContext();
    }

    private static String andBuilder(String[] keys) {
        StringBuilder conditionBuilder = new StringBuilder();

        for (String key : keys) {
            conditionBuilder.append(key).append(AND_CONJUNCTION);
        }

        String condition = conditionBuilder.toString();

        if (condition.endsWith(AND)) {
            condition = condition.substring(0, condition.length() - AND_LENGTH);
        }

        return condition;
    }

    public Cursor query(String table, String[] projection, String selection, String[] selectionArgs) {
        return query(table, projection, selection, selectionArgs, null);
    }

    public Cursor query(String table, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();

        Cursor cursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, orderBy);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, orderBy);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor queryAll(String table, String[] projection) {
        return query(table, projection, null, null);
    }

    public Cursor queryAll(String table) {
        return queryAll(table, null);
    }

    public boolean delete(String table, String column, Long id) {
        if (!TextUtils.isEmpty(column)) {
            column = column + " = ?";
        }
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();
        return mContext.getContentResolver().delete(uri, column, id != null ? new String[]{String.valueOf(id)} : null) > 0;
    }

    public boolean deleteAll(String table) {
        return delete(table, "", null);
    }

    public void insertList(String table, ContentValues[] values) {
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();

        int numberOfRowsInserted = mContext.getContentResolver().bulkInsert(uri, values);
        Log.d(LOG_TAG, "Rows inserted: " + numberOfRowsInserted);

    }

    public Loader<Cursor> getLoader(String table) {
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();
        return new CursorLoader(mContext, uri, null, null, null, null);
    }

    public Loader<Cursor> getLoader(String table, String[] projection, String selection, String[] selectionArgs, String order) {
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();
        return new CursorLoader(mContext, uri, projection, selection, selectionArgs, order);
    }

    public Loader<Cursor> getLoader(Uri uri, String[] projection, String selection, String[] selectionArgs, String order) {
        return new CursorLoader(mContext, uri, projection, selection, selectionArgs, order);
    }

    public boolean isEmpty(String table) {
        return isEmpty(table, null, null);
    }

    public boolean isEmpty(String table, String[] columns, String[] values) {
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon()
                .appendPath(ProductContentProvider.ONE_ROW_LIMIT).appendPath(table).build();

        String selection = null;
        String[] selectionArgs = null;

        if (columns.length > 0 && values.length > 0) {
            selection = andBuilder(columns);
            selectionArgs = values;
        }

        Cursor cursor = mContext.getContentResolver().query(uri, null, selection, selectionArgs, null);

        if (cursor != null) {
            boolean isEmpty = cursor.getCount() < 1;
            cursor.close();
            return isEmpty;
        }
        return false;
    }
}
