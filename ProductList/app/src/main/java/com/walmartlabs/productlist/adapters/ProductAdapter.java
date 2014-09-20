package com.walmartlabs.productlist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.bitmap.BitmapInfo;
import com.koushikdutta.ion.bitmap.IonBitmapCache;
import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.controller.ProductController;
import com.walmartlabs.productlist.util.AndroidUtils;
import com.walmartlabs.productlist.util.Constants;

public class ProductAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;
    private static IonBitmapCache mIonBitmapCache;

    public ProductAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
        mLayoutInflater = LayoutInflater.from(context);

        mIonBitmapCache = new IonBitmapCache(Ion.getDefault(context.getApplicationContext()));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item_list_product, parent, false);

        ViewHolder viewHolder = new ViewHolder(context);
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
        private Context context;

        ViewHolder(Context context) {
            this.context = context;
        }

        public void apply(final ProductBean productBean) {
            //Clean the previous image from recycled view
            productImage.setImageResource(0);

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
                BitmapInfo cachedBitmapInfo = mIonBitmapCache.get(AndroidUtils.sha256String(productBean.productImage));
                Boolean onCache = (cachedBitmapInfo != null && cachedBitmapInfo.bitmaps[0] != null);

                if (onCache) {
                    productImage.setImageBitmap(cachedBitmapInfo.bitmaps[0]);
                } else {
                    Ion.with(context).load(productBean.productImage).withBitmap().asBitmap()
                            .setCallback(new FutureCallback<Bitmap>() {
                                @Override
                                public void onCompleted(Exception e, Bitmap result) {
                                    if (result != null) {
                                        BitmapInfo info = new BitmapInfo(AndroidUtils.sha256String(productBean.productImage),
                                                Constants.ION_CACHE_IMAGE_TYPE, new Bitmap[]{result}, null);
                                        mIonBitmapCache.put(info);
                                        productImage.setImageBitmap(result);
                                    } else {
                                        productImage.setImageResource(R.drawable.ic_launcher);
                                    }
                                }
                            });
                }
            }
        }
    }

}
