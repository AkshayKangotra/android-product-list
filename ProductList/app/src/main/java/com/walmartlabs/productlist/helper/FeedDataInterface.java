package com.walmartlabs.productlist.helper;

import java.util.List;

import com.walmartlabs.productlist.bean.ProductBean;
import retrofit.http.GET;
import retrofit.http.Path;

public interface FeedDataInterface {

    @GET("/walmartproducts/{pageNumber}/{pageSize}")
    List<ProductBean> groupProductList(@Path("pageNumber") int pageNumber,
                                       @Path("pageSize") int pageSize);

}
