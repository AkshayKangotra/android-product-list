package com.walmartlabs.productlist.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductSQLHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTS = "products";

    public static final String COLUMN_PRODUCT_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
    public static final String COLUMN_LONG_DESCRIPTION = "long_description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_REVIEW_RATING = "review_rating";
    public static final String COLUMN_REVIEW_COUNT = "review_count";
    public static final String COLUMN_IN_STOCK = "in_stock";
    public static final String COLUMN_ORDER = "product_order";

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_PRODUCTS + "("
            + COLUMN_PRODUCT_ID + " TEXT NOT NULL PRIMARY KEY, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_SHORT_DESCRIPTION + " TEXT, "
            + COLUMN_LONG_DESCRIPTION + " TEXT, "
            + COLUMN_PRICE + " TEXT, "
            + COLUMN_IMAGE_URL + " TEXT, "
            + COLUMN_REVIEW_RATING + " REAL, "
            + COLUMN_REVIEW_COUNT + " INT, "
            + COLUMN_IN_STOCK + " INT, "
            + COLUMN_ORDER + " INT"
            +");";

    private static ProductSQLHelper mInstance;

    private ProductSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static ProductSQLHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ProductSQLHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(sqLiteDatabase);
    }
}

