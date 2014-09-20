package com.walmartlabs.productlist.helper;

import java.util.List;

import com.walmartlabs.productlist.bean.ProductBean;

import retrofit.RestAdapter;
import retrofit.http.Path;

public class FeedDataHelper implements FeedDataInterface{

    public static final String API_URL = "https://walmart-labs.appspot.com/_ah/api/walmart/v1";

    @Override
    public List<ProductBean> groupProductList(@Path("pageNumber") int pageNumber, @Path("pageSize") int pageSize) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API_URL).build();
        FeedDataInterface feedDataInterface = restAdapter.create(FeedDataInterface.class);
        List<ProductBean> productList = feedDataInterface.groupProductList(0,30);
        return null;
    }
}
