package com.walmartlabs.productlist.bean;

import java.util.List;

public class ProductBeanResponse {

    public List<ProductBean> products;
    public int totalProducts;
    public int pageNumber;
    public int pageSize;
    public int status;
    public String kind;
    public String etag;

}
