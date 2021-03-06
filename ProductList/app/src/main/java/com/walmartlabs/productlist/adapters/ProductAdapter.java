package com.walmartlabs.productlist.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.bitmap.BitmapInfo;
import com.koushikdutta.ion.bitmap.IonBitmapCache;
import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.controller.ProductController;
import com.walmartlabs.productlist.ui.fragments.ProductListFragment;
import com.walmartlabs.productlist.util.AndroidUtil;
import com.walmartlabs.productlist.util.ImageLoadUtil;

public class ProductAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;
    private static IonBitmapCache sIonBitmapCache;
    private static ProductListFragment.OnProductListActionListener sProductListActionListener;

    public ProductAdapter(Activity activity, Cursor cursor){
        super(activity, cursor, 0);
        mLayoutInflater = LayoutInflater.from(activity);

        sProductListActionListener = (ProductListFragment.OnProductListActionListener) activity;

        sIonBitmapCache = new IonBitmapCache(Ion.getDefault(activity.getApplicationContext()));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_item_product, parent, false);

        ViewHolder viewHolder = new ViewHolder(context);
        viewHolder.name = (TextView) view.findViewById(R.id.product_txt_name);
        viewHolder.shortDescription = (TextView) view.findViewById(R.id.product_txt_short_description);
        viewHolder.price = (TextView) view.findViewById(R.id.product_txt_price);
        viewHolder.productImage = (ImageView) view.findViewById(R.id.product_image);
        viewHolder.container = (LinearLayout) view.findViewById(R.id.list_item_product);
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
        private LinearLayout container;
        private Context context;

        ViewHolder(Context context) {
            this.context = context;
        }

        public void apply(final ProductBean productBean) {
            //Clean the previous image from recycled view
            productImage.setImageResource(R.drawable.placeholder);

            if (productBean.productName != null) {
                name.setText(productBean.productName);
            }
            if (productBean.shortDescription != null) {
                shortDescription.setText(Html.fromHtml(productBean.shortDescription));
            }
            if (productBean.price != null) {
                price.setText(productBean.price);
            }
            if (productBean.productImage != null) {
                BitmapInfo cachedBitmapInfo = sIonBitmapCache.get(AndroidUtil.sha256String(productBean.productImage));
                Boolean onCache = (cachedBitmapInfo != null && cachedBitmapInfo.bitmaps[0] != null);

                if (onCache) {
                    productImage.setImageBitmap(cachedBitmapInfo.bitmaps[0]);
                } else {
                    ImageLoadUtil.loadImage(context, productImage, productBean.productImage, sIonBitmapCache);
                }
            }
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sProductListActionListener != null) {
                        sProductListActionListener.onClickProduct(productBean);
                    }
                }
            });
        }
    }

}
