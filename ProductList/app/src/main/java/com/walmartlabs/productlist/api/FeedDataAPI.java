package com.walmartlabs.productlist.api;

import android.content.Context;

import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.bean.ProductBeanResponse;

import retrofit.RestAdapter;
import retrofit.http.Path;

public class FeedDataAPI implements FeedDataInterface{

    private Context mContext;

    public FeedDataAPI(Context context) {
        mContext = context;
    }

    @Override
    public ProductBeanResponse getProductList(@Path("pageNumber") int pageNumber, @Path("pageSize") int pageSize) {
        ProductBeanResponse response = null;
        try {
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(mContext.getString(R.string.api_url)).build();
            FeedDataInterface feedDataInterface = restAdapter.create(FeedDataInterface.class);
            response = feedDataInterface.getProductList(pageNumber, pageSize);
        } catch (Exception e) {
            //Log in Crashlytics
        }
        return response;
    }
}
