package com.walmartlabs.productlist.api;

import com.walmartlabs.productlist.bean.ProductBeanResponse;

import retrofit.http.GET;
import retrofit.http.Path;

public interface FeedDataInterface {

    @GET("/walmartproducts/{pageNumber}/{pageSize}")
    ProductBeanResponse getProductList(@Path("pageNumber") int pageNumber,
                                       @Path("pageSize") int pageSize);

}
