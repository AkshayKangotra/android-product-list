package com.walmartlabs.productlist.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.Loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.walmartlabs.productlist.dao.ProductDBManager;
import com.walmartlabs.productlist.dao.ProductSQLHelper;
import com.walmartlabs.productlist.bean.ProductBean;

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

        ContentValues[] contentValueses = cvList.toArray(new ContentValues[cvList.size()]);
        ProductDBManager.getInstance(mContext).insertList(ProductSQLHelper.TABLE_PRODUCTS, contentValueses);
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


    ////TEMP
    public List<ProductBean> buildFakeList(int qtd) {

        List<ProductBean> productBeans = new ArrayList<ProductBean>();

        for (int i=0; i < qtd; i++) {
            ProductBean productBean = new ProductBean();
            productBean.productId = "" + i;
            productBean.productName = "Produto" + i;
            productBean.shortDescription = "shortDesc" + i;
            productBean.longDescription= "longDesc" + i;
            productBean.price= "$" + i + "." + i;
            productBean.productImage= "http://wp.clicrbs.com.br/pretinhobasico/files/2014/02/faust%C3%A3o-gordo.jpg";
            productBean.reviewRating = 4.4;
            productBean.reviewCount = i;
            productBean.inStock = new Random().nextBoolean();

            productBeans.add(productBean);
        }

        return productBeans;
    }
    ///

    public Loader<Cursor> getProductLoader() {
        return ProductDBManager.getInstance(mContext)
                .getLoader(ProductSQLHelper.TABLE_PRODUCTS, null, null, null, ProductSQLHelper.COLUMN_ORDER);
    }

}
