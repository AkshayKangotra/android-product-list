package com.walmartlabs.productlist.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.bitmap.IonBitmapCache;
import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.dao.ProductDBManager;
import com.walmartlabs.productlist.dao.ProductSQLHelper;
import com.walmartlabs.productlist.util.Constants;
import com.walmartlabs.productlist.util.ImageLoadUtil;

import java.io.UnsupportedEncodingException;

public class ProductFragment extends Fragment {

    View view;
    TextView name;
    TextView price;
    TextView longDescription;
    TextView reviewRating;
    TextView reviewCount;
    TextView inStock;
    ImageView productImage;

    public ProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product, container, false);
        name = (TextView) view.findViewById(R.id.product_txt_name);
        price = (TextView) view.findViewById(R.id.product_txt_price);
        longDescription = (TextView) view.findViewById(R.id.product_txt_long_description);
        reviewRating = (TextView) view.findViewById(R.id.review_rating);
        reviewCount = (TextView) view.findViewById(R.id.reviewCount);
        inStock = (TextView) view.findViewById(R.id.inStock);
        productImage = (ImageView) view.findViewById(R.id.product_image);

        new GetProductItemTask().execute();

        return view;
    }

    public void setLayoutValues(ProductBean productBean) {
        if (productBean != null) {
            if (productBean.productName != null) {
                name.setText(productBean.productName);
            }
            if (productBean.price != null) {
                price.setText(productBean.price);
            }
            if (productBean.longDescription != null) {
                try {
                    longDescription.setText(Html.fromHtml(new String(productBean.longDescription.getBytes(), "UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            reviewRating.setText(getActivity().getString(R.string.review_rating_text, productBean.reviewRating));
            reviewCount.setText(getActivity().getString(R.string.review_count_text, productBean.reviewCount));

            StringBuilder inStockText = new StringBuilder(getActivity().getString(R.string.in_stock_text));
            if (productBean.inStock) {
                inStockText.append(getActivity().getString(R.string.in_stock_yes_text));
            } else {
                inStockText.append(getActivity().getString(R.string.in_stock_no_text));
            }
            inStock.setText(inStockText.toString());

            IonBitmapCache ionBitmapCache = new IonBitmapCache(Ion.getDefault(getActivity().getApplicationContext()));
            ImageLoadUtil.loadImage(getActivity(), productImage, productBean.productImage, ionBitmapCache);
        }
    }

    private class GetProductItemTask extends AsyncTask<Void, Void, ProductBean> {
        @Override
        protected ProductBean doInBackground(Void... params) {
            ProductBean productBean = null;

            if (getArguments() != null) {
                String productId = getArguments().getString(Constants.PRODUCT_ID_INTENT_EXTRA);
                if (productId != null) {
                    productBean = ProductDBManager.getInstance(getActivity()
                            .getApplicationContext()).getItemById(ProductSQLHelper.TABLE_PRODUCTS, productId);
                }
            }
            return productBean;
        }

        @Override
        protected void onPostExecute(ProductBean productBean) {
            setLayoutValues(productBean);
        }
    }
}
