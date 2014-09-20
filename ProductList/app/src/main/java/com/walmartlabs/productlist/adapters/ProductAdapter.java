package com.walmartlabs.productlist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.controller.ProductController;

public class ProductAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;

    public ProductAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item_list_product, parent, false);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.name = (TextView) view.findViewById(R.id.product_txt_name);
        viewHolder.shortDescription = (TextView) view.findViewById(R.id.product_txt_short_description);
        viewHolder.price = (TextView) view.findViewById(R.id.product_txt_price);
        viewHolder.productImage = (ImageView) view.findViewById(R.id.product_image);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.apply(new ProductController(context).getProductBeanFromCursor(cursor));
    }

    private static class ViewHolder{
        private TextView name;
        private TextView shortDescription;
        private TextView price;
        private ImageView productImage;

        public void apply(ProductBean productBean) {
            if (productBean.productName != null) {
                name.setText(productBean.productName);
            }
            if (productBean.shortDescription != null) {
                shortDescription.setText(productBean.shortDescription);
            }
            if (productBean.price != null) {
                price.setText(productBean.price);
            }
            if (productBean.productImage != null) {
                //TODO
                productImage.setImageResource(R.drawable.ic_launcher);
            }
        }

    }
}
