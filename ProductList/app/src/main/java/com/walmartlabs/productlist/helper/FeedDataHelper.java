package com.walmartlabs.productlist.helper;

import java.util.List;

import com.walmartlabs.productlist.bean.ProductBean;
import retrofit.http.Path;

public class FeedDataHelper implements FeedDataInterface{

    @Override
    public List<ProductBean> groupProductList(@Path("pageNumber") int pageNumber, @Path("pageSize") int pageSize) {

        return null;
    }
}
