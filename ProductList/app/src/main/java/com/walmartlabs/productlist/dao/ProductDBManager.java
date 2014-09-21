package com.walmartlabs.productlist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.controller.ProductController;

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

    public Cursor getAllItems(String table, String orderByClause) {
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();
        return mContext.getContentResolver().query(uri, null, null, null, orderByClause);
    }

    public ProductBean getItemById(String table, String productId){
        ProductBean productBean = null;
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();
        String selectionClause = ProductSQLHelper.COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = new String[]{productId};
        Cursor cursor = mContext.getContentResolver().query(uri, null, selectionClause, selectionArgs, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            productBean = new ProductController(mContext).getProductBeanFromCursor(cursor);
        }

        return productBean;
    }

    //TODO This method needs to be optimized in the future
    public int getPositionById(String table, String productId) {
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(table).build();
        String orderByClause = ProductSQLHelper.COLUMN_ORDER;
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, orderByClause);
        ProductController productController = new ProductController(mContext);

        int result = 0;
        if (cursor != null) {
            ProductBean productBean;
            while(cursor.moveToNext()) {
                productBean = productController.getProductBeanFromCursor(cursor);
                if(productBean.productId.equals(productId)) {
                    result = cursor.getPosition();
                    break;
                }
            }
            cursor.close();
        }
        return result;
    }

    // This is an attempt to optimize the above method, but isn't quite working for some rows.
    // Sorry guys, I tried my best here.
    public int getPositionByIdOptimizationAttempt(String table, String productId) {
        int position = 0;
        Uri uri = ProductContentProvider.BASE_CONTENT_URI.buildUpon()
                .appendPath(ProductContentProvider.RAW).appendPath(table).build();

        String selectionClause = "SELECT COUNT(*) " +
            "FROM " + ProductSQLHelper.TABLE_PRODUCTS + " " +
            "WHERE " + ProductSQLHelper.COLUMN_ORDER + " < " + "(SELECT " + ProductSQLHelper.COLUMN_ORDER +
                                                                " FROM " + ProductSQLHelper.TABLE_PRODUCTS +
                                                                " WHERE " + ProductSQLHelper.COLUMN_PRODUCT_ID +
                                                                " = '" + productId + "')";

        Cursor cursor = mContext.getContentResolver().query(uri, null, selectionClause, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            position = cursor.getInt(0);
        }

        return position;
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
