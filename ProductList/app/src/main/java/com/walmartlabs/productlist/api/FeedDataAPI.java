package com.walmartlabs.productlist.api;

import java.util.List;

import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.bean.ProductBeanResponse;
import com.walmartlabs.productlist.util.Constants;

import retrofit.RestAdapter;
import retrofit.http.Path;

public class FeedDataAPI implements FeedDataInterface{

    @Override
    public ProductBeanResponse getProductList(@Path("pageNumber") int pageNumber, @Path("pageSize") int pageSize) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Constants.API_URL).build();
        FeedDataInterface feedDataInterface = restAdapter.create(FeedDataInterface.class);
        ProductBeanResponse productBeanResponse = feedDataInterface.getProductList(pageNumber, pageSize);
        return productBeanResponse;
    }
}
