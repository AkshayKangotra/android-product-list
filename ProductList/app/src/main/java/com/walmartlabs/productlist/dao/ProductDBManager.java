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

    public static ProductDBManager getInstance(Context ctx) {
        if (sDBManager == null) {
            sDBManager = new ProductDBManager(ctx.getApplicationContext());
        }

        return sDBManager;
    }

    private ProductDBManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public Loader<Cursor> getLoader(String table, String[] projection, String selection, String[] selectionArgs, String order) {
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();
        return new CursorLoader(mContext, uri, projection, selection, selectionArgs, order);
    }

    public void insertList(String table, ContentValues[] values) {
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();

        int numberOfRowsInserted = mContext.getContentResolver().bulkInsert(uri, values);
        Log.d(LOG_TAG, "Rows inserted: " + numberOfRowsInserted);

    }

    public boolean delete(String table) {
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();
        return mContext.getContentResolver().delete(uri, null, null) > 0;
    }

    public int count(String table) {
        int count = 0;
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            count = cursor.getCount();
        }

        return count;
    }
}
