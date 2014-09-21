package com.walmartlabs.productlist.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.api.FeedDataAPI;
import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.bean.ProductBeanResponse;
import com.walmartlabs.productlist.controller.ProductController;
import com.walmartlabs.productlist.dao.ProductDBManager;
import com.walmartlabs.productlist.dao.ProductSQLHelper;
import com.walmartlabs.productlist.util.Constants;

import java.util.List;

public class FeedDataService extends IntentService {

    public FeedDataService() {
        super("FeedDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int pageNumber = getPageNumber();
        Intent intentBroadcast = new Intent(Constants.LOAD_FINISHED_ACTION);
        ProductBeanResponse productBeanResponse = new FeedDataAPI(getApplicationContext())
                .getProductList(pageNumber, getResources().getInteger(R.integer.number_of_products_per_page));

        if (productBeanResponse != null && productBeanResponse.status == 200) {
            List<ProductBean> productBeanList = productBeanResponse.products;
            int listSize = 0;

            if (productBeanList != null) {
                ProductController productController = new ProductController(this);
                productController.insertProductList(productBeanList);

                listSize = productBeanList.size();
            }

            intentBroadcast.putExtra(Constants.LIST_SIZE_INTENT_EXTRA, listSize);
        }

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(intentBroadcast);
    }

    private int getPageNumber() {
        ProductDBManager productDBManager = ProductDBManager.getInstance(this);
        int productCount = productDBManager.count(ProductSQLHelper.TABLE_PRODUCTS);
        return (productCount / getResources().getInteger(R.integer.number_of_products_per_page)) + 1;
    }

}
