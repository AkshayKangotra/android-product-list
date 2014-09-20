package com.walmartlabs.productlist.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.Loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.walmartlabs.productlist.dao.ProductDBManager;
import com.walmartlabs.productlist.dao.ProductSQLHelper;
import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.services.FeedDataService;

public class ProductController {

    private Context mContext;

    public ProductController(Context context) {
        mContext = context;
    }

    public void insertProductList(List<ProductBean> list) {
        List<ContentValues> cvList = new ArrayList<ContentValues>();

        for (ProductBean productBean : list) {
            cvList.add(productToContentValues(productBean));
        }

        ContentValues[] contentValues = cvList.toArray(new ContentValues[cvList.size()]);
        ProductDBManager.getInstance(mContext).insertList(ProductSQLHelper.TABLE_PRODUCTS, contentValues);
    }

    public ContentValues productToContentValues(ProductBean productBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductSQLHelper.COLUMN_PRODUCT_ID, productBean.productId);
        contentValues.put(ProductSQLHelper.COLUMN_NAME, productBean.productName);
        contentValues.put(ProductSQLHelper.COLUMN_SHORT_DESCRIPTION, productBean.shortDescription);
        contentValues.put(ProductSQLHelper.COLUMN_LONG_DESCRIPTION, productBean.longDescription);
        contentValues.put(ProductSQLHelper.COLUMN_PRICE, productBean.price);
        contentValues.put(ProductSQLHelper.COLUMN_IMAGE_URL, productBean.productImage);
        contentValues.put(ProductSQLHelper.COLUMN_REVIEW_RATING, productBean.reviewRating);
        contentValues.put(ProductSQLHelper.COLUMN_REVIEW_COUNT, productBean.reviewCount);
        contentValues.put(ProductSQLHelper.COLUMN_IN_STOCK, productBean.inStock);
        contentValues.put(ProductSQLHelper.COLUMN_ORDER, System.currentTimeMillis());

        return contentValues;
    }

    public ProductBean getProductBeanFromCursor(Cursor cursor) {
        ProductBean productBean = new ProductBean();
        productBean.productId = cursor.getString(cursor.getColumnIndex(ProductSQLHelper.COLUMN_PRODUCT_ID));
        productBean.productName = cursor.getString(cursor.getColumnIndex(ProductSQLHelper.COLUMN_NAME));
        productBean.shortDescription = cursor.getString(cursor.getColumnIndex(ProductSQLHelper.COLUMN_SHORT_DESCRIPTION));
        productBean.longDescription = cursor.getString(cursor.getColumnIndex(ProductSQLHelper.COLUMN_LONG_DESCRIPTION));
        productBean.price = cursor.getString(cursor.getColumnIndex(ProductSQLHelper.COLUMN_PRICE));
        productBean.productImage = cursor.getString(cursor.getColumnIndex(ProductSQLHelper.COLUMN_IMAGE_URL));
        productBean.reviewRating = cursor.getDouble(cursor.getColumnIndex(ProductSQLHelper.COLUMN_REVIEW_RATING));
        productBean.reviewCount = cursor.getInt(cursor.getColumnIndex(ProductSQLHelper.COLUMN_REVIEW_COUNT));
        productBean.inStock = cursor.getInt(cursor.getColumnIndex(ProductSQLHelper.COLUMN_IN_STOCK)) == 1;

        return productBean;
    }

    public Loader<Cursor> getProductLoader() {
        return ProductDBManager.getInstance(mContext)
                .getLoader(ProductSQLHelper.TABLE_PRODUCTS, null, null, null, ProductSQLHelper.COLUMN_ORDER);
    }

    public void loadProducts(boolean forceLoad){
        //TODO add cache verification
        if (ProductDBManager.getInstance(mContext).count(ProductSQLHelper.TABLE_PRODUCTS) == 0 || forceLoad) {
            Intent intent = new Intent(mContext, FeedDataService.class);
            mContext.startService(intent);
        }
    }

}
